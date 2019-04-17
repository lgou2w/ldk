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

abstract class AnvilWindowEvent(
        val anvilWindow: AnvilWindow,
        val player: Player
)

class AnvilWindowOpenEvent(
        anvilWindow: AnvilWindow,
        player: Player
) : AnvilWindowEvent(anvilWindow, player)

class AnvilWindowCloseEvent(
        anvilWindow: AnvilWindow,
        player: Player
) : AnvilWindowEvent(anvilWindow, player)

class AnvilWindowClickEvent(
        anvilWindow: AnvilWindow,
        player: Player,
        val slot: AnvilWindowSlot,
        val clicked: ItemStack?
) : AnvilWindowEvent(anvilWindow, player), Cancellable {
    private var cancel = false
    override fun isCancelled(): Boolean = cancel
    override fun setCancelled(cancel: Boolean) { this.cancel = cancel }
}

class AnvilWindowInputEvent(
        anvilWindow: AnvilWindow,
        player: Player,
        var value: String
) : AnvilWindowEvent(anvilWindow, player), Cancellable {
    private var cancel = false
    override fun isCancelled(): Boolean = cancel
    override fun setCancelled(cancel: Boolean) { this.cancel = cancel }
}
