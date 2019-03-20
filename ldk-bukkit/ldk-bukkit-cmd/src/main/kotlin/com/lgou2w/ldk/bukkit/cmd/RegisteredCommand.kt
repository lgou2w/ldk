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

import org.bukkit.command.CommandSender

/**
 * ## RegisteredCommand (已注册命令)
 *
 * @see [DefaultRegisteredCommand]
 * @author lgou2w
 */
interface RegisteredCommand {

    /**
     * * The manager object for this command.
     * * 此命令的管理器对象.
     */
    val manager : CommandManager

    /**
     * * The source object for this command.
     * * 此命令的源对象.
     */
    val source : Any

    /**
     * * The parent command object for this command.
     * * 此命令的父命令对象.
     */
    val parent : RegisteredCommand?

    /**
     * * Child command mapping for this command.
     * * 此命令的子命令映射.
     */
    val children : Map<String, RegisteredCommand>

    /**
     * * Executor mapping for this command.
     * * 此命令的执行器映射.
     */
    val executors : Map<String, CommandExecutor>

    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    /**
     * * The name of this command.
     * * 此命令的名称.
     */
    val name : String

    /**
     * * The aliases of this command.
     * * 此命令的别名.
     */
    val aliases : Array<out String>

    /**
     * * The permission of this command.
     * * 此命令的权限.
     */
    val permission : Array<out String>?

    /**
     * * The fallback prefix of this command.
     * * 此命令的后备前缀.
     */
    val fallbackPrefix : String

    /**
     * * The message prefix of this command.
     * * 此命令的消息前缀.
     */
    var prefix : String

    /**
     * * The description of this command.
     * * 此命令的描述.
     */
    var description : String?

    /**
     * * The usage of this command.
     * * 此命令的用法.
     */
    var usage : String?

    /**
     * * The feedback object of this command.
     * * 此命令的反馈对象.
     */
    var feedback : CommandFeedback?

    /**
     * * Indicates whether this command allows TAB completion.
     * * 表示此命令是否允许 TAB 键补全.
     */
    var isAllowCompletion : Boolean

    /**************************************************************************
     *
     * API
     *
     **************************************************************************/

    /**
     * * The root parent of this command. if not, returns `null`.
     * * 此命令的根父命令. 如果没有则返回 `null`.
     */
    val rootParent : RegisteredCommand?

    /**
     * * Parse and register the given [child] command source.
     * * 将给定的子命令源 [child] 解析并注册.
     *
     * @throws [CommandParseException] If parsing error.
     * @throws [CommandParseException] 如果解析时错误.
     * @throws [IllegalArgumentException] If register error.
     * @throws [IllegalArgumentException] 如果注册时错误.
     */
    @Throws(IllegalArgumentException::class, CommandParseException::class)
    fun registerChild(child: Any, forcibly: Boolean = false): Boolean

    /**
     * * Parse and register the given [child] command.
     * * 将给定的子命令 [child] 解析并注册.
     *
     * @throws [IllegalArgumentException] If register error.
     * @throws [IllegalArgumentException] 如果注册时错误.
     */
    @Throws(IllegalArgumentException::class)
    fun registerChild(child: RegisteredCommand, forcibly: Boolean = false): Boolean

    /**
     * * Find registered child command from the given [name].
     * * 从给定的名称 [name] 查找已注册的子命令.
     */
    fun findChild(name: String, allowAlias: Boolean = true): RegisteredCommand?

    /**
     * * Find executor from the given [name].
     * * 从给定的名称 [name] 查找执行器.
     */
    fun findExecutor(name: String, allowAlias: Boolean = true): CommandExecutor?

    /**
     * * 映射此命令的描述信息.
     * * Mapping the description of this command.
     *
     * @since LDK 0.1.7-rc6
     */
    fun mappingDescriptions(description: String?, usage: String?)

    /**
     * * 映射此命令的执行器描述信息.
     * * Mapping the executor description of this command.
     *
     * @since LDK 0.1.7-rc6
     */
    fun mappingExecutorDescriptions(mapping: Map<String, String?>)

    /**************************************************************************
     *
     * Significant
     *
     **************************************************************************/

    fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean

    fun complete(sender: CommandSender, alias: String, args: Array<out String>): List<String>
}
