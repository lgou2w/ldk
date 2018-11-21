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

interface RegisteredCommand {

    val manager : CommandManager

    val source : Any

    val parent : RegisteredCommand?

    val children : Map<String, RegisteredCommand>

    val executors : Map<String, CommandExecutor>

    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    val name : String

    val aliases : Array<out String>

    val permission : Array<out String>?

    val fallbackPrefix : String

    var prefix : String

    var description : String?

    var usage : String?

    var feedback : CommandFeedback?

    var isAllowCompletion : Boolean

    /**************************************************************************
     *
     * API
     *
     **************************************************************************/

    val rootParent : RegisteredCommand?

    @Throws(IllegalArgumentException::class, CommandParseException::class)
    fun registerChild(child: Any, forcibly: Boolean = false) : Boolean

    @Throws(IllegalArgumentException::class)
    fun registerChild(child: RegisteredCommand, forcibly: Boolean = false) : Boolean

    fun findChild(name: String, allowAlias: Boolean = true) : RegisteredCommand?

    fun findExecutor(name: String, allowAlias: Boolean = true) : CommandExecutor?

    /**************************************************************************
     *
     * Significant
     *
     **************************************************************************/

    fun execute(sender: CommandSender, label: String, args: Array<out String>) : Boolean

    fun complete(sender: CommandSender, alias: String, args: Array<out String>) : List<String>
}
