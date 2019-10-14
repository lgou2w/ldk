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

package com.lgou2w.ldk.bukkit.gui

import com.lgou2w.ldk.common.Consumer
import com.lgou2w.ldk.common.andThenConsume
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

/**
 * ## ButtonEvent (按钮点击事件)
 *
 * @see [Gui]
 * @see [Button]
 * @see [Button.onClicked]
 * @see [InventoryClickEvent]
 * @author lgou2w
 */
class ButtonEvent(
  /**
   * * The button object for this event.
   * * 此事件的按钮对象.
   */
  val button: Button,
  /**
   * * The clicker object for this event.
   * * 此事件的点击者对象.
   */
  val clicker: HumanEntity,
  /**
   * * The clicked item stack object for this event.
   * * 此事件的点击物品栈对象.
   */
  val clicked: ItemStack?,
  /**
   * * The source Bukkit event object for this event.
   * * 此事件的源 Bukkit 事件对象.
   */
  val source: InventoryClickEvent
) {

  /**
   * * Get the Gui object that interacts with the current event.
   * * 获取当前事件交互的 Gui 对象.
   */
  val view : Gui
    get() = button.parent

  /**
   * * The clicker player object for this event.
   * * 此事件的点击者玩家对象.
   *
   * @throws [ClassCastException] If the clicker is not a player.
   * @throws [ClassCastException] 如果点击者不是玩家.
   * @since LDK 0.1.8
   */
  val player : Player
    get() = clicker as Player

  /**
   * * Get this event whether the clicker is a player.
   * * 获取此事件点击者是否为玩家.
   *
   */
  fun isPlayer() : Boolean
    = clicker is Player

  /**
   * * Get this event and click on the item stack is not empty.
   * * 获取此事件点击物品栈不为空的.
   */
  fun hasItem() : Boolean
    = clicked != null && clicked.type != Material.AIR

  companion object Constants {

    @JvmField
    val CANCEL : Consumer<ButtonEvent> = { event ->
      event.source.isCancelled = true
      event.source.result = Event.Result.DENY
    }

    @JvmField
    val CLOSE : Consumer<ButtonEvent> = { event ->
      event.clicker.closeInventory()
    }

    @JvmStatic
    fun cancelThen(after : Consumer<ButtonEvent>) : Consumer<ButtonEvent> {
      return CANCEL andThenConsume after
    }

    @JvmStatic
    fun cancelThenConsumeAndClose(after: Consumer<ButtonEvent>) : Consumer<ButtonEvent> {
      return CANCEL andThenConsume after andThenConsume CLOSE
    }
  }
}
