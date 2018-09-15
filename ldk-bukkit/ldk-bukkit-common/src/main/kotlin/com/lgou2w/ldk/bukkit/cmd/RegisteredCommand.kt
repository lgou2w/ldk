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

import com.lgou2w.ldk.reflect.AccessorMethod
import org.bukkit.command.CommandSender

interface RegisteredCommand {

    val manager : CommandManager

    val source : Any

    val proxy : org.bukkit.command.Command

    val root : CommandRoot

    val name : String

    val aliases : Array<out String>

    val description : String

    val usage : String

    val fallbackPrefix : String

    val children : Map<String, Child>

    val childrenKeys : Set<String>

    fun getChild(name: String) : Child?

    fun execute(sender: CommandSender, name: String, args: Array<out String>) : Boolean

    var completeProxy : CompleteProxy?

    fun complete(sender: CommandSender, name: String, args: Array<out String>) : List<String>

    interface Child {

        val parent : RegisteredCommand

        val command : Command

        val permission : Permission?

        val isPlayable : Boolean

        val name : String

        val aliases : Array<out String>

        val description : String

        val usage : String

        val parameters : Array<out ChildParameter>

        val accessor: AccessorMethod<Any, Any>

        val length : Int

        val max : Int

        val min : Int

        fun invoke(vararg args: Any?) : Any?

        fun execute(sender: CommandSender, name: String, args: Array<out String>) : Boolean

        fun testPermission(sender: CommandSender) : Boolean
    }

    class ChildParameter(
            val type : Class<*>,
            val optional: Optional?
    ) {

        internal lateinit var child : RegisteredCommand.Child
        val parent: RegisteredCommand.Child
            get() = child
    }
}
