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

interface CommandFeedback {

    fun onPermission(
            sender: CommandSender,
            name: String,
            registered: RegisteredCommand,
            child: RegisteredCommand.Child,
            args: Array<out String>,
            permission: String
    )

    fun onPlayable(
            sender: CommandSender,
            name: String,
            registered: RegisteredCommand,
            child: RegisteredCommand.Child,
            args: Array<out String>
    )

    fun onMinimum(
            sender: CommandSender,
            name: String,
            registered: RegisteredCommand,
            child: RegisteredCommand.Child,
            args: Array<out String>,
            current: Int,
            min: Int
    )

    fun onTransform(
            sender: CommandSender,
            name: String,
            registered: RegisteredCommand,
            child: RegisteredCommand.Child,
            args: Array<out String>,
            expected: Class<*>,
            transformed: Any
    )

    fun onUnhandled(
            sender: CommandSender,
            name: String,
            registered: RegisteredCommand,
            child: RegisteredCommand.Child,
            args: Array<out String>,
            e: Exception
    )
}
