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

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.util.*

@FunctionalInterface
interface Completer {

    fun onComplete(
            parameter: CommandExecutor.Parameter,
            sender: CommandSender,
            value: String
    ) : List<String>

    companion object Constants {

        @JvmField
        val EMPTY = object : Completer {
            override fun onComplete(
                    parameter: CommandExecutor.Parameter,
                    sender: CommandSender,
                    value: String
            ): List<String> {
                return emptyList()
            }
        }

        @JvmField
        val DEFAULT = object : Completer {
            override fun onComplete(
                    parameter: CommandExecutor.Parameter,
                    sender: CommandSender,
                    value: String
            ): List<String> {
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

        @JvmField
        val PLAYER = object : Completer {
            override fun onComplete(
                    parameter: CommandExecutor.Parameter,
                    sender: CommandSender,
                    value: String
            ): List<String> {
                return Completes.matchOnlinePlayers(sender, value)
            }
        }

        @JvmField
        val ENUM = object : Completer {
            override fun onComplete(
                    parameter: CommandExecutor.Parameter,
                    sender: CommandSender,
                    value: String
            ): List<String> {
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

        @JvmField
        val BOOLEAN = object : Completer {
            override fun onComplete(
                    parameter: CommandExecutor.Parameter,
                    sender: CommandSender,
                    value: String
            ): List<String> {
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
