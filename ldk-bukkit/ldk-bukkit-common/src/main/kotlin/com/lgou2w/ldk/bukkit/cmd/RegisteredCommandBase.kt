/*
 * Copyright (C) 2018 The lgou2w (lgou2w@hotmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lgou2w.ldk.bukkit.cmd

import com.lgou2w.ldk.common.Consumer
import com.lgou2w.ldk.reflect.AccessorMethod
import org.bukkit.command.CommandException
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.reflect.Modifier
import java.util.*

open class RegisteredCommandBase(
        final override val manager: CommandManager,
        final override val root: CommandRoot,
        final override val source: Any,
        private val childMap: MutableMap<String, RegisteredCommand.Child>
) : RegisteredCommand {

    open class ChildBase(
            final override val command: Command,
            final override val permission: Permission?,
            final override val isPlayable: Boolean,
            final override val parameters: Array<out RegisteredCommand.ChildParameter>,
            override val accessor: AccessorMethod<Any, Any>
    ) : RegisteredCommand.Child {

        internal lateinit var registered: RegisteredCommand
        override val parent: RegisteredCommand
            get() = registered

        override val name: String
            get() = command.value
        override val aliases: Array<out String>
            get() = command.aliases
        override val description: String
            get() = command.description
        override val usage: String
            get() = command.usage

        override val length: Int
            get() = parameters.size
        override val max: Int
            get() = length
        override val min: Int
            get() = max - parameters.count { it.optional != null }

        override fun invoke(vararg args: Any?): Any? {
            return if (Modifier.isStatic(accessor.source.modifiers))
                accessor.invoke(null, *args)
            else
                accessor.invoke(parent.source, *args)
        }

        override fun execute(sender: CommandSender, name: String, args: Array<out String>): Boolean {
            val feedback = parent.feedback ?: parent.manager.globalFeedback
            var failedPermission = ""

            if (!testPermissionIfFailed(sender) { failedPermission = it }) {
                feedback.onPermission(sender, name, parent, this, args, failedPermission)
                return true
            }
            if (isPlayable && sender !is Player) {
                feedback.onPlayable(sender, name, parent, this, args)
                return true
            }
            if (args.size < min) {
                feedback.onMinimum(sender, name, parent, this, args, args.size, min)
                return true
            }
            val parameterValues : MutableList<Any?> = ArrayList()
            parameterValues.add(if (isPlayable) sender as Player else sender)

            for (index in 0 until max) {
                val parameter = parameters[index]
                val optional = parameter.optional
                val transform = parent.manager.getTypeTransform(parameter.type)
                val value = args.getOrNull(index) ?: optional?.def
                val transformed = if (value != null) transform?.transform(value) else value
                if (transformed != null && !parameter.type.isAssignableFrom(transformed::class.java)) {
                    feedback.onTransform(sender, name, parent, this, args, parameter.type, transformed)
                    return true
                }
                parameterValues.add(transformed)
            }

            return try {
                invoke(*parameterValues.toTypedArray())
                true
            } catch (e: Exception) {
                feedback.onUnhandled(sender, name, parent, this, args, e)
                true
            }
        }

        override fun testPermission(sender: CommandSender): Boolean {
            return permission == null || permission.values.all { sender.hasPermission(it) }
        }

        override fun testPermissionIfFailed(sender: CommandSender, block: Consumer<String>): Boolean {
            return permission == null || permission.values.all {
                val result = sender.hasPermission(it)
                if (!result)
                    block(it)
                result
            }
        }

        override fun toString(): String {
            return "Child(name=$name, aliases=${Arrays.toString(aliases)}, min=$min, max=$max)"
        }
    }

    override val proxy = object : org.bukkit.command.Command(
            root.value,
            root.description,
            root.usage,
            root.aliases.toMutableList()
    ) {
        override fun execute(sender: CommandSender, command: String, args: Array<out String>): Boolean {
            return this@RegisteredCommandBase.execute(sender, command, args)
        }
        override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): List<String> {
            return this@RegisteredCommandBase.complete(sender, alias, args)
        }
    }

    override val children: Map<String, RegisteredCommand.Child>
        get() = synchronized (childMap) {
            HashMap(childMap)
        }

    override val childrenKeys: Set<String>
        get() = synchronized (childMap) {
            HashSet(childMap.keys)
        }

    override var feedback: CommandFeedback? = null
    override var prefix: String = root.prefix

    override val name: String
        get() = root.value
    override val aliases: Array<out String>
        get() = root.aliases
    override val description: String
        get() = root.description
    override val usage: String
        get() = root.usage
    override val fallbackPrefix: String
        get() = root.fallbackPrefix

    override fun getChild(name: String): RegisteredCommand.Child? {
        synchronized (childMap) {
            var child = childMap[name]
            if (child == null)
                child = childMap.values.find { c -> c.aliases.any { alias -> alias == name } }
            return child
        }
    }

    override fun execute(sender: CommandSender, name: String, args: Array<out String>): Boolean {
        if (!manager.plugin.isEnabled)
            throw CommandException("Cannot execute command '$name' in plugin ${manager.plugin.description.fullName} - plugin is disabled.")
        return if (args.isEmpty()) {
            usageMessage(sender, name, null)
            false
        } else {
            val child = getChild(args.first())
            val success = if (child != null) {
                val childArgs = if (args.size > 1) args.copyOfRange(1, args.size) else emptyArray()
                child.execute(sender, name, childArgs)
            } else {
                false
            }
            if (!success)
                usageMessage(sender, name, child)
            success
        }
    }

    protected open fun usageMessage(sender: CommandSender, name: String, child: RegisteredCommand.Child?) {
        val usage = child?.usage ?: usage
        if (usage.isNotEmpty()) {
            val description = child?.description ?: description
            var replaced = usage.replace("<command>", name)
            if (child != null) replaced = replaced.replace("<child>", child.name)
            sender.sendMessage(prefix + replaced + if (description.isNotEmpty()) " - $description" else "")
        }
    }

    override var completeProxy: CompleteProxy? = null

    override fun complete(sender: CommandSender, name: String, args: Array<out String>): List<String> {
        val value = completeProxy?.tabComplete(this, sender, name, args)
        return value ?: Collections.emptyList()
    }

    override fun toString(): String {
        return "RegisteredCommand(command=$name, aliases=${Arrays.toString(aliases)})"
    }
}
