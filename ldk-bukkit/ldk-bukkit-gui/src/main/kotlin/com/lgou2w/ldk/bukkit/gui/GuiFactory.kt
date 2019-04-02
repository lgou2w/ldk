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

import org.bukkit.inventory.Inventory

/**
 * ## GuiFactory (界面工厂)
 *
 * @see [Gui]
 * @author lgou2w
 */
object GuiFactory {

    /**
     * * Get the Gui object from the given [inventory].
     * * 从给定的物品栏 [inventory] 获取 Gui 对象.
     */
    @JvmStatic
    fun fromInventory(inventory: Inventory?): Gui? {
        return inventory?.holder as? Gui
    }

    /**
     * * Get whether the given [inventory] is of Gui.
     * * 获取给定的物品栏 [inventory] 是否为 Gui.
     */
    @JvmStatic
    fun isGui(inventory: Inventory?): Boolean {
        return fromInventory(inventory) != null
    }

    /**
     * * Convert the given two-dimensional coordinate [x], [y] to index value.
     * * 将给定的二维坐标 [x], [y] 转换为索引值.
     */
    @JvmStatic
    fun coordinateToIndex(x: Int, y: Int): Int {
        return (y * 9) - (9 - x) - 1
    }

    /**
     * * Convert the given index value [index] to a two-dimensional coordinate value.
     * * 将给定的索引值 [index] 转换为二维坐标值.
     */
    @JvmStatic
    fun indexToCoordinate(index: Int): Pair<Int, Int> {
        val x = (index + 1) % 9
        val y = (index + (9 - x) + 1) / 9
        return x to y
    }
}
