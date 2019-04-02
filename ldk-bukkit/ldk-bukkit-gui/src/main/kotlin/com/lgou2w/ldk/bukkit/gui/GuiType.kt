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

import com.lgou2w.ldk.common.Valuable
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

/**
 * ## GuiType (界面类型)
 *
 * @see [Gui]
 * @see [Gui.type]
 * @see [Valuable]
 * @author lgou2w
 */
enum class GuiType(
        /**
         * * The size of this Gui type.
         * * 此界面类型的大小.
         */
        val size: Int,
        /**
         * * The title of this Gui type.
         * * 此界面类型的标题.
         */
        val title: String
) : Valuable<String> {

    /**
     * A chest inventory, with 54 slots of type CONTAINER.
     */
    CHEST_6(54, "Chest"),
    /**
     * A chest inventory, with 45 slots of type CONTAINER.
     */
    CHEST_5(45, "Chest"),
    /**
     * A chest inventory, with 36 slots of type CONTAINER.
     */
    CHEST_4(36, "Chest"),
    /**
     * A chest inventory, with 27 slots of type CONTAINER.
     */
    CHEST_3(27, "Chest"),
    /**
     * A chest inventory, with 18 slots of type CONTAINER.
     */
    CHEST_2(18, "Chest"),
    /**
     * A chest inventory, with 9 slots of type CONTAINER.
     */
    CHEST_1(9, "Chest"),
    ;

    fun createInventory(owner: InventoryHolder?, title: String): Inventory {
        return Bukkit.createInventory(owner, size, title)
    }

    override val value: String
        get() = name
}
