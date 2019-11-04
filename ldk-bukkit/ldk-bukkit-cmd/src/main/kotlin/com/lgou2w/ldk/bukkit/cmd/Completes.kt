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

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

/**
 * ## Completes (补全器)
 *
 * @see [Iterable]
 * @see [Completer]
 * @author lgou2w
 */
open class Completes : Iterable<Completer> {

  protected val completes : MutableMap<Class<*>, Completer> = ConcurrentHashMap()

  /**
   * @see [Map.keys]
   */
  val keys : MutableSet<Class<*>>
    get() = completes.keys

  /**
   * @see [Map.values]
   */
  val values : MutableCollection<Completer>
    get() = completes.values

  /**
   * @see [Map.entries]
   */
  val entries : MutableSet<MutableMap.MutableEntry<Class<*>, Completer>>
    get() = completes.entries

  /**
   * * Add a completer of the given type [type].
   * * 添加给定类型 [type] 的补全器.
   */
  fun addCompleter(type: Class<*>, completer: Completer) {
    completes[type] = completer
  }

  /**
   * * Add a completer of the given type [type].
   * * 添加给定类型 [type] 的补全器.
   */
  fun addCompleter(type: Class<*>, completer: (
    parameter: CommandExecutor.Parameter,
    sender: CommandSender,
    value: String) -> List<String>?
  ) {
    addCompleter(type, object : Completer {
      override fun onComplete(
        parameter: CommandExecutor.Parameter,
        sender: CommandSender,
        value: String
      ): List<String>? {
        return completer.invoke(parameter, sender, value)
      }
    })
  }

  /**
   * * Remove the completer of the given type [type].
   * * 移除给定类型 [type] 的补全器.
   */
  fun removeCompleter(type: Class<*>): Completer? {
    return completes.remove(type)
  }

  /**
   * * Get the completer of the given type [type].
   * * 获取给定类型 [type] 的补全器.
   */
  fun getCompleter(type: Class<*>): Completer? {
    return completes[type]
  }

  override fun iterator(): Iterator<Completer> {
    return completes.values.iterator()
  }

  /**
   * * Add the default type completer.
   * * 添加默认的类型补全器.
   *
   * @see [Completer.PLAYER]
   * @see [Completer.ENUM]
   * @see [Completer.BOOLEAN]
   */
  fun addDefaultCompletes() {
    addCompleter(Player::class.java, Completer.PLAYER)
    addCompleter(Enum::class.java, Completer.ENUM)
    addCompleter(Boolean::class.java, Completer.BOOLEAN)
    addCompleter(java.lang.Boolean::class.java, Completer.BOOLEAN)
  }

  companion object {
    @JvmStatic fun matchOnlinePlayers(sender: CommandSender, value: String) : List<String> {
      val senderPlayer = sender as? Player
      val matchedPlayers = sender.server.onlinePlayers
        .asSequence()
        .filter { player ->
          (senderPlayer == null || senderPlayer.canSee(player)) && player.name.startsWith(value)
        }
        .map(Player::getName)
        .toList()
      Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER)
      return matchedPlayers
    }
  }
}
