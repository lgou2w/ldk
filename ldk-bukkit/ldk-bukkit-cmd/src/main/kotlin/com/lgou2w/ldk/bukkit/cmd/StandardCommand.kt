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

import com.lgou2w.ldk.chat.toColor
import org.bukkit.command.CommandSender

abstract class StandardCommand : Initializable {

    lateinit var command : RegisteredCommand
        private set

    final override fun initialize(manager: CommandManager, registered: RegisteredCommand) {
        command = registered
        initialize(manager)
    }

    protected abstract fun initialize(manager: CommandManager)

    protected fun CommandSender.send(message: String) {
        sendMessage(command.prefix + message.toColor())
    }
}
