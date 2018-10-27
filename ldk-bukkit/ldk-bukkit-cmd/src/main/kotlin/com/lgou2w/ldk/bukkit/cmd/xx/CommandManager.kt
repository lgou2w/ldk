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

import org.bukkit.plugin.Plugin

interface CommandManager {

    val plugin : Plugin

    val parser : CommandParser

    val transforms : Transforms

    val globalFeedback : CommandFeedback

    @Throws(CommandParseException::class)
    fun registerCommand(source: Any) : RegisteredCommand

    fun getCommand(command: String) : RegisteredCommand?
}
