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

import org.bukkit.command.CommandSender

/**
 * ## CommandFeedback (命令反馈)
 *
 * * If you need to implement it yourself,
 *      then you should extends [SimpleCommandFeedback] instead of implementing this interface.
 * * 如果需要自己实现, 那么你应该继承 [SimpleCommandFeedback] 而不是实现此接口.
 *
 * @see [SimpleCommandFeedback]
 * @see [SimpleChineseCommandFeedback]
 * @author lgou2w
 */
interface CommandFeedback {

    /**
     * * Called when the executor has no permission.
     * * 当执行者没有权限时被调用.
     */
    fun onPermission(
            sender: CommandSender,
            name: String,
            command: RegisteredCommand,
            executor: CommandExecutor?,
            args: Array<out String>,
            permission: String
    )

    /**
     * * Called when the console execute only the player executable command.
     * * 当控制台执行仅玩家可执行命令时被调用.
     */
    fun onPlayerOnly(
            sender: CommandSender,
            name: String,
            command: RegisteredCommand,
            executor: CommandExecutor,
            args: Array<out String>
    )

    /**
     * * Called when the length of the parameter executed by the executor does not match.
     * * 当执行者执行的参数长度不符合时被调用.
     */
    fun onMinimum(
            sender: CommandSender,
            name: String,
            command: RegisteredCommand,
            executor: CommandExecutor,
            args: Array<out String>,
            current: Int,
            min: Int
    )

    /**
     * * Called when the executor parameter transform and the expected does not match.
     * * 当执行器参数转变后和预期不匹配时被调用.
     */
    fun onTransform(
            sender: CommandSender,
            name: String,
            command: RegisteredCommand,
            executor: CommandExecutor,
            args: Array<out String>,
            expected: Class<*>,
            value: String?,
            transformed: Any?
    )

    /**
     * * Called when an unhandled exception is thrown.
     * * 当不可处理的异常抛出时被调用.
     */
    fun onUnhandled(
            sender: CommandSender,
            name: String,
            command: RegisteredCommand,
            executor: CommandExecutor,
            args: Array<out String>,
            e: Exception
    )
}
