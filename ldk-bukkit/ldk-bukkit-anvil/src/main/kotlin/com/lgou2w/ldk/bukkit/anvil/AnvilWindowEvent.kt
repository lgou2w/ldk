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

package com.lgou2w.ldk.bukkit.anvil

import com.lgou2w.ldk.bukkit.event.Cancellable
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * ## AnvilWindowEvent (铁砧窗口事件)
 *
 * @see [AnvilWindowOpenEvent]
 * @see [AnvilWindowCloseEvent]
 * @see [AnvilWindowClickEvent]
 * @see [AnvilWindowInputEvent]
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
abstract class AnvilWindowEvent(
  /**
   * * Anvil window object for this event.
   * * 此事件的铁砧窗口对象.
   */
  val anvilWindow: AnvilWindow,
  /**
   * * Player object for this event.
   * * 此事件的玩家对象.
   */
  val player: Player
)

/**
 * ## AnvilWindowOpenEvent (铁砧窗口打开事件)
 *
 * @see [AnvilWindowEvent]
 * @see [AnvilWindow.onOpened]
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
class AnvilWindowOpenEvent(
  anvilWindow: AnvilWindow,
  player: Player
) : AnvilWindowEvent(anvilWindow, player)

/**
 * ## AnvilWindowCloseEvent (铁砧窗口关闭事件)
 *
 * @see [AnvilWindowEvent]
 * @see [AnvilWindow.onClosed]
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
class AnvilWindowCloseEvent(
  anvilWindow: AnvilWindow,
  player: Player
) : AnvilWindowEvent(anvilWindow, player)

/**
 * ## AnvilWindowClickEvent (铁砧窗口点击事件)
 *
 * @see [AnvilWindowEvent]
 * @see [AnvilWindow.onClicked]
 * @see [Cancellable]
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
class AnvilWindowClickEvent(
  anvilWindow: AnvilWindow,
  player: Player,
  /**
   * * The anvil window of this event click on the slot.
   * * 此事件的铁砧窗口点击槽位.
   *
   * @see [AnvilWindowSlot]
   */
  val slot: AnvilWindowSlot,
  /**
   * * The anvil window of this event click on the slot item stack.
   * * 此事件的铁砧窗口点击槽位物品栈.
   */
  val clicked: ItemStack?
) : AnvilWindowEvent(anvilWindow, player), Cancellable {
  private var cancel = false
  override fun isCancelled(): Boolean = cancel
  override fun setCancelled(cancel: Boolean) { this.cancel = cancel }
}

/**
 * ## AnvilWindowInputEvent (铁砧窗口输入事件)
 *
 * @see [AnvilWindowEvent]
 * @see [AnvilWindow.onInputted]
 * @see [Cancellable]
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
class AnvilWindowInputEvent(
  anvilWindow: AnvilWindow,
  player: Player,
  /**
   * * Indicates the text value inputted by this event anvil window.
   * * 表示事件的铁砧窗口输入文本.
   */
  var value: String
) : AnvilWindowEvent(anvilWindow, player), Cancellable {
  private var cancel = false
  override fun isCancelled(): Boolean = cancel
  override fun setCancelled(cancel: Boolean) { this.cancel = cancel }
}
