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

import com.lgou2w.ldk.reflect.DataType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object TypeCompletes {

    @JvmField
    val EMPTY = object : TypeCompleter {
        override fun onComplete(
                parameter: RegisteredCommand.ChildParameter,
                sender: CommandSender,
                value: String
        ): List<String> {
            return emptyList()
        }
    }

    @JvmField
    val DEFAULT = object : TypeCompleter {
        override fun onComplete(
                parameter: RegisteredCommand.ChildParameter,
                sender: CommandSender,
                value: String
        ): List<String> {
            val type = parameter.type.simpleName
            return if (parameter.canNull) {
                Collections.singletonList("[$type=${parameter.optional?.def}]")
            } else {
                Collections.singletonList("<$type>")
            }
        }
    }

    @JvmField
    val PLAYER = object : TypeCompleter {
        override fun onComplete(
                parameter: RegisteredCommand.ChildParameter,
                sender: CommandSender,
                value: String
        ): List<String> {
            return if (Player::class.java.isAssignableFrom(parameter.type)) {
                val senderPlayer = sender as? Player
                val matchedPlayers = sender.server.onlinePlayers.filter { player ->
                    (senderPlayer == null || senderPlayer.canSee(player)) && player.name.startsWith(value)
                }.map { player ->
                    player.name
                }
                Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER)
                matchedPlayers
            } else {
                emptyList()
            }
        }
    }

    @JvmField
    val ENUM = object : TypeCompleter {
        override fun onComplete(
                parameter: RegisteredCommand.ChildParameter,
                sender: CommandSender,
                value: String
        ): List<String> {
            return if (parameter.type.isEnum) {
                val constants = parameter.type.enumConstants
                return constants.map { it as Enum<*> }.filter { enum ->
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
    val BOOLEAN = object : TypeCompleter {
        override fun onComplete(
                parameter: RegisteredCommand.ChildParameter,
                sender: CommandSender,
                value: String
        ): List<String> {
            // boolean、Boolean
            // kotlin Boolean? => java.lang.Boolean
            // kotlin Boolean => java.lang.Boolean.TYPE <=> boolean
            return if (DataType.ofPrimitive(parameter.type) == Boolean::class.java) {
                val first = when {
                    parameter.optional != null -> "[Boolean=${parameter.optional.def}]"
                    parameter.isNullable -> "null"
                    else -> "<Boolean>"
                }
                listOf(first, "true", "false").filter { it.startsWith(value) }
            } else {
                emptyList()
            }
        }
    }

    @JvmStatic
    fun addDefaultTypeCompletes(manager: CommandManager) {
        manager.addTypeCompleter(Player::class.java, PLAYER)
        manager.addTypeCompleter(Enum::class.java, ENUM)
        manager.addTypeCompleter(Boolean::class.java, BOOLEAN)
        manager.addTypeCompleter(java.lang.Boolean::class.java, BOOLEAN)
    }
}
