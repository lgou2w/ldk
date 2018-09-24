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

import org.bukkit.inventory.Inventory

object GuiFactory {

    @JvmStatic
    fun fromInventory(inventory: Inventory?) : Gui? {
        return inventory?.holder as? Gui
    }

    @JvmStatic
    fun isGui(inventory: Inventory?) : Boolean {
        return fromInventory(inventory) != null
    }

    @JvmStatic
    fun coordinateToIndex(x: Int, y: Int): Int {
        return (y * 9) - (9 - x) - 1
    }

    @JvmStatic
    fun indexToCoordinate(index: Int): Pair<Int, Int> {
        val x = (index + 1) % 9
        val y = (index + (9 - x) + 1) / 9
        return x to y
    }
}
