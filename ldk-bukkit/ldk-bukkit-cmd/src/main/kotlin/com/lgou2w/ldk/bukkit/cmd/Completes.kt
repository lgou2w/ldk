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
import java.util.concurrent.ConcurrentHashMap

open class Completes : Iterable<Completer> {

    protected val completes : MutableMap<Class<*>, Completer> = ConcurrentHashMap()

    val keys : MutableSet<Class<*>>
        get() = completes.keys

    val values : MutableCollection<Completer>
        get() = completes.values

    val entries : MutableSet<MutableMap.MutableEntry<Class<*>, Completer>>
        get() = completes.entries

    fun addCompleter(type: Class<*>, completer: Completer) {
        completes[type] = completer
    }

    fun addCompleter(type: Class<*>, completer: (
            parameter: CommandExecutor.Parameter,
            sender: CommandSender,
            value: String) -> List<String>) {
        addCompleter(type, object : Completer {
            override fun onComplete(
                    parameter: CommandExecutor.Parameter,
                    sender: CommandSender,
                    value: String
            ): List<String> {
                return completer.invoke(parameter, sender, value)
            }
        })
    }

    fun removeCompleter(type: Class<*>) : Completer? {
        return completes.remove(type)
    }

    fun getCompleter(type: Class<*>) : Completer? {
        return completes[type]
    }

    override fun iterator(): Iterator<Completer> {
        return completes.values.iterator()
    }

    fun addDefaultCompletes() {
        addCompleter(Player::class.java, Completer.PLAYER)
        addCompleter(Enum::class.java, Completer.ENUM)
        addCompleter(Boolean::class.java, Completer.BOOLEAN)
        addCompleter(java.lang.Boolean::class.java, Completer.BOOLEAN)
    }

    companion object {
        @JvmStatic fun matchOnlinePlayers(sender: CommandSender, value: String) : List<String> {
            val senderPlayer = sender as? Player
            val matchedPlayers = sender.server.onlinePlayers.filter { player ->
                (senderPlayer == null || senderPlayer.canSee(player)) && player.name.startsWith(value)
            }.map { player ->
                player.name
            }
            Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER)
            return matchedPlayers
        }
    }
}
