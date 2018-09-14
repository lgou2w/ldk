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

interface RegisteredCommand {

    val plugin : Plugin

    val manager : CommandManager

    val source : Any

    val proxy : org.bukkit.command.Command

    val root : CommandRoot

    val name : String

    val aliases : Array<out String>

    val description : String

    val usage : String

    val children : Map<String, Child>

    fun getChild(name: String) : Child?

    fun execute(sender: CommandSender, name: String, args: Array<out String>) : Boolean

    var completeProxy : CompleteProxy?

    fun complete(sender: CommandSender, name: String, args: Array<out String>) : List<String>

    interface Child {

        val parent : RegisteredCommand

        val command : Command

        val permission : Permission

        val isPlayable : Boolean

        val name : String

        val aliases : Array<out String>

        val description : String

        val parameters : Array<out ChildParameter>

        val length : Int

        val max : Int

        val min : Int
    }

    data class ChildParameter(
            val parent : Child,
            val type : Class<*>,
            val optional : Optional?
    )
}
