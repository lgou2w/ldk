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

import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import java.util.*

open class RegisteredCommandBase(
        final override val plugin: Plugin,
        final override val manager: CommandManager,
        final override val root: CommandRoot,
        final override val source: Any,
        private val childMap: MutableMap<String, RegisteredCommand.Child>
) : RegisteredCommand {

    open class ChildBase(
            final override val parent: RegisteredCommand,
            final override val command: Command,
            final override val permission: Permission,
            final override val isPlayable: Boolean,
            final override val parameters: Array<out RegisteredCommand.ChildParameter>
    ) : RegisteredCommand.Child {

        override val name: String
            get() = command.value
        override val aliases: Array<out String>
            get() = command.aliases
        override val description: String
            get() = command.description

        override val length: Int
            get() = parameters.size
        override val max: Int
            get() = length
        override val min: Int
            get() = max - parameters.count { it.optional != null }
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

    protected fun labelMatched(command: String) : Boolean {
        return name == command || aliases.contains(command)
    }

    protected fun findChild(name: String, useAlias : Boolean) : RegisteredCommand.Child? {
        synchronized (childMap) {
            var child = childMap[name]
            if (child == null && useAlias)
                child = childMap.values.find { c -> c.aliases.any { alias -> alias == name } }
            return child
        }
    }

    override val children: Map<String, RegisteredCommand.Child>
        get() = HashMap(childMap)

    override val name: String
        get() = root.value
    override val aliases: Array<out String>
        get() = root.aliases
    override val description: String
        get() = root.description
    override val usage: String
        get() = root.usage

    override fun getChild(name: String): RegisteredCommand.Child? {
        return findChild(name, true)
    }

    override fun execute(sender: CommandSender, name: String, args: Array<out String>): Boolean {
        TODO() // Command execute
    }

    override var completeProxy: CompleteProxy? = null

    override fun complete(sender: CommandSender, name: String, args: Array<out String>): List<String> {
        val value = completeProxy?.tabComplete(this, sender, name, args)
        return value ?: Collections.emptyList()
    }
}
