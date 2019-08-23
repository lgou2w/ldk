/*
 * Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>
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
import org.bukkit.command.CommandSender

/**
 * ## SimpleChineseCommandFeedback (简单中文命令反馈)
 *
 * @see [SimpleCommandFeedback]
 * @author lgou2w
 */
class SimpleChineseCommandFeedback : SimpleCommandFeedback() {

    override fun onPermission(
            sender: CommandSender,
            name: String,
            command: RegisteredCommand,
            executor: CommandExecutor?,
            args: Array<out String>,
            permission: String
    ) {
        sendMessage(command, sender, ChatColor.RED + "你没有权限使用此命令.")
    }

    override fun onPlayerOnly(
            sender: CommandSender,
            name: String,
            command: RegisteredCommand,
            executor: CommandExecutor,
            args: Array<out String>
    ) {
        sendMessage(command, sender, ChatColor.RED + "控制台无法执行此命令.")
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
                ChatColor.RED + "参数长度小于命令最小长度. (当前: $current, 最小: $min)"
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
                ChatColor.RED + "转变 '$value' 到 '$transformed' 类型不匹配. (预期: ${expected.simpleName})"
        )
    }
}
