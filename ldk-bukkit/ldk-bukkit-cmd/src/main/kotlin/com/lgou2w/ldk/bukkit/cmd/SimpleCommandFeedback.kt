/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
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

import com.lgou2w.ldk.chat.ChatColor
import org.bukkit.command.CommandException
import org.bukkit.command.CommandSender

/**
 * ## SimpleCommandFeedback (简单命令反馈)
 *
 * @see [CommandFeedback]
 * @author lgou2w
 */
open class SimpleCommandFeedback : CommandFeedback {

    protected fun sendMessage(
            command: RegisteredCommand,
            sender: CommandSender,
            message: String
    ) {
        val prefix = command.rootParent?.prefix ?: command.prefix
        sender.sendMessage(prefix + message)
    }

    override fun onPermission(
            sender: CommandSender,
            name: String,
            command: RegisteredCommand,
            executor: CommandExecutor?,
            args: Array<out String>,
            permission: String
    ) {
        sendMessage(command, sender, ChatColor.RED + "You do not have permission to use this command.")
    }

    override fun onPlayable(
            sender: CommandSender,
            name: String,
            command: RegisteredCommand,
            executor: CommandExecutor,
            args: Array<out String>
    ) {
        sendMessage(command, sender, ChatColor.RED + "The console cannot execute this command.")
    }

    override fun onMinimum(
            sender: CommandSender,
            name: String,
            command: RegisteredCommand,
            executor: CommandExecutor,
            args: Array<out String>,
            current: Int,
            min: Int
    ) {
        sendMessage(
                command,
                sender,
                ChatColor.RED + "The arg length is less than the command min length. (Current: $current, Min: $min)"
        )
    }

    override fun onTransform(
            sender: CommandSender,
            name: String,
            command: RegisteredCommand,
            executor: CommandExecutor,
            args: Array<out String>,
            expected: Class<*>,
            value: String?,
            transformed: Any?
    ) {
        sendMessage(
                command,
                sender,
                ChatColor.RED + "Transformed '$value' to '$transformed' type does not match. (Expected: ${expected.simpleName})"
        )
    }

    @Throws(CommandException::class)
    override fun onUnhandled(
            sender: CommandSender,
            name: String,
            command: RegisteredCommand,
            executor: CommandExecutor,
            args: Array<out String>,
            e: Exception
    ) {
        if (e is CommandException)
            throw e
        else
            throw CommandException(e.message, e)
    }
}
