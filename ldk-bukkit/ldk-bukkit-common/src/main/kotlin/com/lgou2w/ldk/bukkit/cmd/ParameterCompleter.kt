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
import org.bukkit.entity.Player
import java.util.*

abstract class ParameterCompleter {

    abstract fun onComplete(
            parameter: RegisteredCommand.ChildParameter,
            sender: CommandSender,
            value: String
    ) : List<String>

    companion object Constants {

        @JvmStatic
        fun adaptive(type: Class<*>) : ParameterCompleter {
            return when {
                Player::class.java.isAssignableFrom(type) -> PLAYER
                type.isEnum -> ENUM
                else -> DEFAULT
            }
        }

        @JvmField val EMPTY = object : ParameterCompleter() {
            override fun onComplete(
                    parameter: RegisteredCommand.ChildParameter,
                    sender: CommandSender,
                    value: String
            ): List<String> {
                return emptyList()
            }
        }

        @JvmField val DEFAULT = object : ParameterCompleter() {
            override fun onComplete(
                    parameter: RegisteredCommand.ChildParameter,
                    sender: CommandSender,
                    value: String
            ): List<String> {
                val type = parameter.type.simpleName
                return if (parameter.optional != null) {
                    Collections.singletonList("[$type=${parameter.optional.def}]")
                } else {
                    Collections.singletonList("<$type>")
                }
            }
        }

        @JvmField val PLAYER = object : ParameterCompleter() {
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

        @JvmField val ENUM = object : ParameterCompleter() {
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
    }
}
