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

import com.lgou2w.ldk.common.Function
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

interface CommandManager {

    val plugin : Plugin

    var globalFeedback : CommandFeedback

    fun registerCommand(source: Class<*>, vararg args: Any) : Boolean

    fun registerCommand(source: Any) : Boolean

    fun getCommand(command: String) : RegisteredCommand?

    fun getCommand(source: Class<*>) : RegisteredCommand?

    fun parseChildProvider(parsed: RegisteredCommand, provider: ChildProvider) : RegisteredCommand.Child?

    /**
     * @see [TypeTransforms.addDefaultTypeTransform]
     */
    fun addDefaultTypeTransforms()

    fun <T> addTypeTransform(type: Class<T>, transform: Function<String, T?>)

    fun <T> addTypeTransform(type: Class<T>, transform: TypeTransform<T>)

    fun <T> getTypeTransform(type: Class<T>) : TypeTransform<T>?

    fun hasTypeTransform(type: Class<*>) : Boolean

    /**
     * @see [TypeCompletes.addDefaultTypeCompletes]
     */
    fun addDefaultTypeCompletes()

    fun addTypeCompleter(type: Class<*>, completer: (
            parameter: RegisteredCommand.ChildParameter,
            sender: CommandSender,
            value: String) -> List<String>
    )

    fun addTypeCompleter(type: Class<*>, completer: TypeCompleter)

    fun getTypeCompleter(type: Class<*>) : TypeCompleter?

    fun hasTypeCompleter(type: Class<*>) : Boolean
}
