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

/**
 * ## CommandParser (命令解析器)
 *
 * @see [CommandManager.parser]
 * @author lgou2w
 */
interface CommandParser {

    /**
     * * Parse the given command source [source] object.
     * * 解析给定的命令源 [source] 对象.
     *
     * @throws [CommandParseException] If parsing error.
     * @throws [CommandParseException] 如果解析时错误.
     */
    @Throws(CommandParseException::class)
    fun parse(manager: CommandManager, source: Any): RegisteredCommand

    /**
     * * Parse the given command source [source] object.
     * * 解析给定的命令源 [source] 对象.
     *
     * @throws [CommandParseException] If parsing error.
     * @throws [CommandParseException] 如果解析时错误.
     */
    @Throws(CommandParseException::class)
    fun parse(manager: CommandManager, parent: RegisteredCommand?, source: Any): RegisteredCommand
}
