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

package com.lgou2w.ldk.bukkit.gui

import com.lgou2w.ldk.common.Consumer
import com.lgou2w.ldk.common.andThenConsume
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

data class ButtonEvent(
        val button: Button,
        val clicker: HumanEntity,
        val clicked: ItemStack?,
        val source: InventoryClickEvent
) {

    fun isPlayer() : Boolean
            = clicker is Player

    fun hasItem() : Boolean
            = clicked != null && clicked.type != Material.AIR

    companion object Constants {

        @JvmStatic
        val CANCELLED : Consumer<ButtonEvent> = { event ->
            event.source.isCancelled = true
            event.source.result = Event.Result.DENY
        }

        @JvmStatic
        val CLOSE : Consumer<ButtonEvent> = { event ->
            event.clicker.closeInventory()
        }

        @JvmStatic
        fun cancelledThen(after : Consumer<ButtonEvent>) : Consumer<ButtonEvent> {
            return CANCELLED andThenConsume after
        }
    }
}
