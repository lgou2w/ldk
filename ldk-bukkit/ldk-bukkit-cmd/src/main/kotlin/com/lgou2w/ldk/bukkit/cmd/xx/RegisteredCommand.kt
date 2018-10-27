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

import org.bukkit.command.CommandSender

interface RegisteredCommand {

    val manager : CommandManager

    val source : Any

    val parent : RegisteredCommand?

    val children : Map<String, RegisteredCommand>

    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    val name : String

    val aliases : Array<out String>

    val permission : Array<out String>?

    val executors : Map<String, CommandExecutor>

    var prefix : String

    var feedback : CommandFeedback?

    /**************************************************************************
     *
     * API
     *
     **************************************************************************/

    fun registerChild(child: RegisteredCommand, forcibly: Boolean) : Boolean

    fun findChild(name: String, allowAlias: Boolean = true) : RegisteredCommand?

    fun findExecutor(name: String, allowAlias: Boolean = true) : CommandExecutor?

    /**************************************************************************
     *
     * Significant
     *
     **************************************************************************/

    fun execute(sender: CommandSender, name: String, args: Array<out String>) : Boolean

    fun complete(sender: CommandSender, name: String, args: Array<out String>) : List<String>
}
