/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
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

import org.bukkit.plugin.Plugin

/**
 * ## CommandManager (命令管理器)
 *
 * @see [CommandManagerBase]
 * @see [SimpleCommandManager]
 * @see [DefaultCommandManager]
 * @author lgou2w
 */
interface CommandManager {

    /**
     * * The plugin object for this command manager.
     * * 此命令管理器的插件对象.
     */
    val plugin : Plugin

    /**
     * * The parser object for this command manager.
     * * 此命令管理器的解析器对象.
     */
    val parser : CommandParser

    /**
     * * Registered command map for this command manager.
     * * 此命令管理器的已注册命令映射.
     */
    val commands : Map<String, RegisteredCommand>

    /**
     * * The transforms object for this command manager.
     * * 此命令管理器的转变器对象.
     */
    val transforms : Transforms

    /**
     * * The completes object for this command manager.
     * * 此命令管理器的补全器对象.
     */
    val completes : Completes

    /**
     * * The global feedback object for this command manager.
     * * 此命令管理器的全局反馈对象.
     */
    var globalFeedback : CommandFeedback

    /**
     * * Parse and register the given command [source].
     * * 将给定的命令源 [source] 进行解析并注册.
     *
     * @throws [CommandParseException] If parsing error.
     * @throws [CommandParseException] 如果解析时错误.
     * @throws [UnsupportedOperationException] If register error.
     * @throws [UnsupportedOperationException] 如果注册时错误.
     */
    @Throws(CommandParseException::class, UnsupportedOperationException::class)
    fun registerCommand(source: Any): RegisteredCommand

    /**
     * * Unregister the given command name [command].
     * * 将给定的命令名 [command] 进行卸载注销.
     *
     * @since LDK 0.1.8-rc
     */
    fun unregisterCommand(command: String): Boolean

    /**
     * * Unregister the given command [command].
     * * 将给定的命令 [command] 进行卸载注销.
     *
     * @since LDK 0.1.8-rc
     */
    fun unregisterCommand(command: RegisteredCommand): Boolean

    /**
     * * Unregister all commands of this command manager.
     * * 将此命令管理器的所有命令进行卸载注销.
     *
     * @since LDK 0.1.8-rc
     */
    fun unregisterCommands(): Boolean

    /**
     * * Get the registered command from the given command name [command].
     * * 从给定的命令名 [command] 获取已注册的命令.
     */
    fun getCommand(command: String): RegisteredCommand?
}
