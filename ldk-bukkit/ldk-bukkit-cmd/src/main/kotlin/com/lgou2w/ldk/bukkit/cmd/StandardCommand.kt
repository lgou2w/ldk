/*
 * Copyright (C) 2016-2020 The lgou2w <lgou2w@hotmail.com>
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

import com.lgou2w.ldk.chat.toColor
import org.bukkit.command.CommandSender

/**
 * ## StandardCommand (标准命令)
 *
 * @see [Initializable]
 * @author lgou2w
 */
abstract class StandardCommand : Initializable {

  /**
   * * Registered command object for this standard command.
   * * 此标准命令的已注册命令对象.
   */
  lateinit var command : RegisteredCommand private set

  final override fun initialize(command: RegisteredCommand) {
    this.command = command
    this.initialize()
  }

  /**
   * * Initialize this standard command.
   * * 初始化此标准命令.
   */
  protected open fun initialize() { }

  fun CommandSender.send(message: String) {
    val prefix = command.rootParent?.prefix ?: command.prefix
    sendMessage(prefix + message.toColor())
  }

  fun CommandSender.send(message: Array<out String>) {
    val prefix = command.rootParent?.prefix ?: command.prefix
    sendMessage(message.toColor().map { msg -> prefix + msg }.toTypedArray())
  }
}
