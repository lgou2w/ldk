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

package com.lgou2w.ldk.bukkit.cmd.xx

import com.lgou2w.ldk.chat.ChatColor
import com.lgou2w.ldk.common.Consumer
import com.lgou2w.ldk.reflect.DataType
import org.bukkit.command.CommandException
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class DefaultRegisteredCommand(
        override val manager: CommandManager,
        override val source: Any,
        override val parent: DefaultRegisteredCommand?,
        override val name: String,
        override val aliases: Array<out String>,
        override val permission: Array<out String>?,
        override val description: Description?,
        children: Map<String, DefaultRegisteredCommand>,
        executors: Map<String, DefaultCommandExecutor>
) : RegisteredCommand {

    private val mChildren = ConcurrentHashMap<String, DefaultRegisteredCommand>(children)
    private val mExecutors = ConcurrentHashMap<String, DefaultCommandExecutor>(executors)

    override val children: Map<String, RegisteredCommand>
        get() = HashMap(mChildren)
    override val executors: Map<String, CommandExecutor>
        get() = HashMap(mExecutors)

    override val fallbackPrefix: String = description?.fallbackPrefix ?: ""
    override var prefix: String = description?.prefix?.replace(COMMAND_PLACEHOLDER, name) ?: ""
        set(value) { field = value.replace(COMMAND_PLACEHOLDER, name) }

    override var feedback: CommandFeedback? = null
    override var isAllowCompletion: Boolean = true

    /**************************************************************************
     *
     * API
     *
     **************************************************************************/

    override val rootParent: RegisteredCommand?
        get() = getRootParent(this)

    override fun registerChild(child: RegisteredCommand, forcibly: Boolean): Boolean {
        if (child !is DefaultRegisteredCommand)
            throw IllegalArgumentException("The subcommand must be an instance of DefaultRegisteredCommand.")
        val existed = findChild(child.name, false)
        if (existed != null && !forcibly)
            return false
        mChildren[child.name] = child
        return true
    }

    override fun findChild(name: String, allowAlias: Boolean): DefaultRegisteredCommand? {
        var child = mChildren[name]
        if (child == null && allowAlias)
            child = mChildren.values.find { c -> c.aliases.any { alias -> alias == name } }
        return child
    }

    override fun findExecutor(name: String, allowAlias: Boolean): DefaultCommandExecutor? {
        var executor = mExecutors[name]
        if (executor == null && allowAlias)
            executor = mExecutors.values.find { e -> e.aliases.any { alias -> alias == name } }
        return executor
    }

    /**************************************************************************
     *
     * Significant
     *
     **************************************************************************/

    private fun getRootParent(command: RegisteredCommand?) : RegisteredCommand? {
        val parent = command?.parent
        return if (parent == null)
            command
        else
            getRootParent(parent)
    }

    override fun execute(sender: CommandSender, name: String, args: Array<out String>) : Boolean {
        if (!testPermissionIfFailed(sender, permission) {
                    (feedback ?: manager.globalFeedback)
                        .onPermission(sender, name, this, null, args, it)
                })
            return true
        return if (args.isEmpty()) {
            typeHelp(sender)
            true
        } else {
            val child = findChild(args.first())
            child?.execute(sender, name, pollArgument(args))
                   ?: invokeExecutor(sender, args)
        }
    }

    private fun invokeExecutor(sender: CommandSender, args: Array<out String>) : Boolean {
        val executor = findExecutor(args.first())
        val arguments = pollArgument(args)
        val success = if (executor != null) try {
            val parameterValues = ArrayList<Any?>()
            val feedback = feedback ?: manager.globalFeedback
            if (!parseExecutorArguments(executor, feedback, sender, arguments, parameterValues))
                return true
            return try {
                executor.execute(*parameterValues.toTypedArray())
                true
            } catch (e: Exception) {
                feedback.onUnhandled(sender, name, this, executor, arguments, e)
                true
            }
        } catch (e: Exception) {
            throw CommandException(e.message, e)
        } else {
            false
        }
        if (!success)
            typeHelp(sender)
        return success
    }

    private fun parseExecutorArguments(
            executor: DefaultCommandExecutor,
            feedback: CommandFeedback,
            sender: CommandSender,
            args: Array<out String>,
            parameterValues: MutableList<Any?>
    ) : Boolean {
        if (!testPermissionIfFailed(sender, executor.permission) {
                    feedback.onPermission(sender, name, this, executor, args, it) }) {
            return false
        }
        if (executor.isPlayable && sender !is Player) {
            feedback.onPlayable(sender, name, this, executor, args)
            return false
        }
        if (args.size < executor.min) {
            feedback.onMinimum(sender, name, this, executor, args, args.size, executor.min)
            return false
        }
        parameterValues.add(if (executor.isPlayable) sender as Player else sender)
        for (index in 0 until executor.max) {
            val parameter = executor.parameters[index]
            var transform = manager.transforms.getTransform(parameter.type)
            if (transform == null && DataType.ofPrimitive(parameter.type).isPrimitive)
                transform = manager.transforms.getTransform(DataType.ofPrimitive(parameter.type))
            val value = args.getOrNull(index) ?: parameter.defValue
            val transformed = if (value != null) transform?.transform(value) else value
            if ((transformed == null && !parameter.canNullable) ||
                (transformed != null && !parameter.type.isAssignableFrom(transformed::class.java))
            ) {
                feedback.onTransform(sender, name, this, executor, args, parameter.type, value, transformed)
                return false
            }
            parameterValues.add(transformed)
        }
        return true
    }

    private fun testPermissionIfFailed(
            sender: CommandSender,
            permission: Array<out String>?,
            block: Consumer<String>? = null
    ): Boolean {
        return permission == null || permission.all {
            val result = sender.hasPermission(it)
            if (!result && block != null)
                block(it)
            result
        }
    }

    private fun pollArgument(args: Array<out String>) : Array<out String> {
        return if (args.size <= 1)
            emptyArray()
        else
            args.copyOfRange(1, args.size)
    }

    private fun typeHelp(sender: CommandSender) {
        val root = rootParent ?: this
        val usage = description?.usage
        if (usage == null || usage.isEmpty())
            sender.sendMessage(root.prefix + ChatColor.RED + "Unknown parameter. Type \"/${root.name} help\" for help.")
        else
            sender.sendMessage(root.prefix + usage.replace(COMMAND_PLACEHOLDER, root.name))
    }

    override fun complete(sender: CommandSender, name: String, args: Array<out String>): List<String> {
        if (!testPermissionIfFailed(sender, permission))
            return emptyList()
        return if (args.size <= 1) {
            mChildren.filter { testPermissionIfFailed(sender, it.value.permission) }.map { it.key } +
            mExecutors.filter { testPermissionIfFailed(sender, it.value.permission) }.map { it.key }
                .filter {
                    val first = args.firstOrNull()
                    (first == null || it.startsWith(first))
                }
        } else {
            val child = findChild(args.first())
            child?.complete(sender, name, pollArgument(args))
                ?: invokeExecutorComplete(sender, args)
        }
    }

    private fun invokeExecutorComplete(sender: CommandSender, args: Array<out String>) : List<String> {
        val executor = findExecutor(args.first())
        return if (executor == null || args.lastIndex > executor.max || !testPermissionIfFailed(sender, executor.permission))
            emptyList()
        else {
            val parameter = executor.parameters[args.lastIndex - 1]
            val completer = manager.completes.getCompleter(parameter.type) ?: Completer.DEFAULT
            completer.onComplete(parameter, sender, args.last())
        }
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = result * 31 + name.hashCode()
        result = result * 31 + Arrays.hashCode(aliases)
        result = result * 31 + mChildren.hashCode()
        result = result * 31 + mExecutors.hashCode()
        result = result * 31 + (description?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is DefaultRegisteredCommand)
            return source == other.source &&
                   name == other.name &&
                   Arrays.equals(aliases, other.aliases) &&
                   mChildren == other.mChildren &&
                   mExecutors == other.mExecutors &&
                   description == other.description
        return false
    }

    override fun toString(): String {
        return "DefaultRegisteredCommand(name=$name, aliases=${Arrays.toString(aliases)})"
    }

    companion object Constants {
        const val COMMAND_PLACEHOLDER = "<command>"
    }
}
