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

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.util.Collections

/**
 * ## Completer (补全器)
 *
 * @see [Completes]
 * @author lgou2w
 */
@FunctionalInterface
interface Completer {

    /**
     * * Called when the parameter needs to be TAB completion.
     * * 当参数需要进行 TAB 补全时调用.
     *
     * @param parameter Executor parameter.
     * @param parameter 执行器参数.
     * @param sender Executor.
     * @param sender 执行者.
     * @param value Completion value.
     * @param value 补全值.
     */
    fun onComplete(
            parameter: CommandExecutor.Parameter,
            sender: CommandSender,
            value: String
    ): List<String>?

    companion object Constants {

        /**
         * * An empty completer.
         * * 表示空的补全器.
         */
        @JvmField
        val EMPTY = object : Completer {
            override fun onComplete(
                    parameter: CommandExecutor.Parameter,
                    sender: CommandSender,
                    value: String
            ): List<String>? {
                return null
            }
        }

        /**
         * * Indicate the default generic type completer.
         * * 表示默认通用类型补全器.
         */
        @JvmField
        val DEFAULT = object : Completer {
            override fun onComplete(
                    parameter: CommandExecutor.Parameter,
                    sender: CommandSender,
                    value: String
            ): List<String>? {
                val alias = if (parameter.vararg == null) parameter.name ?: parameter.type.simpleName
                    else parameter.name ?: parameter.vararg.simpleName
                return if (parameter.canNullable) {
                    if (parameter.vararg == null) Collections.singletonList("[$alias=${parameter.defValue}]")
                    else Collections.singletonList("[$alias=${parameter.defValue}...]")
                } else {
                    if (parameter.vararg == null) Collections.singletonList("<$alias>")
                    else Collections.singletonList("<$alias...>")
                }.let {
                    if (parameter.isPlayerName && Bukkit.getOnlinePlayers().isNotEmpty())
                        it.toMutableList().apply { addAll(Completes.matchOnlinePlayers(sender, value)) }
                    else
                        it
                }
            }
        }

        /**
         * * Indicate the player [org.bukkit.entity.Player] type completer.
         * * 表示玩家 [org.bukkit.entity.Player] 类型补全器.
         */
        @JvmField
        val PLAYER = object : Completer {
            override fun onComplete(
                    parameter: CommandExecutor.Parameter,
                    sender: CommandSender,
                    value: String
            ): List<String>? {
                return Completes.matchOnlinePlayers(sender, value)
            }
        }

        /**
         * * Indicate the enum [Enum] type completer.
         * * 表示枚举 [Enum] 类型补全器.
         */
        @JvmField
        val ENUM = object : Completer {
            override fun onComplete(
                    parameter: CommandExecutor.Parameter,
                    sender: CommandSender,
                    value: String
            ): List<String>? {
                return if (parameter.type.isEnum) {
                    val constants = parameter.type.enumConstants
                    constants.map { it as Enum<*> }.filter { enum ->
                        enum.name.startsWith(value)
                    }.map { enum ->
                        enum.name
                    }
                } else {
                    emptyList()
                }
            }
        }

        /**
         * * Indicate the boolean [Boolean] type completer.
         * * 表示布尔 [Boolean] 类型补全器.
         */
        @JvmField
        val BOOLEAN = object : Completer {
            override fun onComplete(
                    parameter: CommandExecutor.Parameter,
                    sender: CommandSender,
                    value: String
            ): List<String>? {
                val alias = if (parameter.vararg == null) parameter.name ?: "Boolean"
                    else parameter.name ?: parameter.vararg.simpleName
                val first = when {
                    parameter.isNullable -> "null"
                    parameter.defValue != null ->
                        if (parameter.vararg == null) "[$alias=${parameter.defValue}]" else "[$alias=${parameter.defValue}...]"
                    else ->
                        if (parameter.vararg == null) "<$alias>" else "<$alias...>"
                }
                return listOf(first, "false", "true")
                    .filter { it.startsWith(value) }
            }
        }
    }
}
