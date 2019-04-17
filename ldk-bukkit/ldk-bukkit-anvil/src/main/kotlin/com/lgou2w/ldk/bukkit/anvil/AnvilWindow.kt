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

import com.lgou2w.ldk.common.Consumer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

interface AnvilWindow {

    val plugin : Plugin

    var isAllowMove : Boolean

    val isOpened : Boolean

    @Throws(IllegalStateException::class)
    fun open(player: Player)

    fun onOpened(block: Consumer<AnvilWindowOpenEvent>?)

    fun onClosed(block: Consumer<AnvilWindowCloseEvent>?)

    fun onClicked(block: Consumer<AnvilWindowClickEvent>?)

    fun onInputted(block: Consumer<AnvilWindowInputEvent>?)

    @Throws(IllegalStateException::class)
    fun getItem(slot: AnvilWindowSlot): ItemStack?

    @Throws(IllegalStateException::class)
    fun setItem(slot: AnvilWindowSlot, stack: ItemStack?)

    fun clearItems()

    companion object Factory {

        @JvmStatic
        fun of(plugin: Plugin): AnvilWindow
                = @Suppress("DEPRECATION") AnvilWindowBase.of(plugin)
    }
}
