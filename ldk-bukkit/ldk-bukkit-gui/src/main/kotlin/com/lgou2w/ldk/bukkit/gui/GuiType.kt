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

import com.lgou2w.ldk.common.Valuable
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

enum class GuiType(
        val size: Int,
        val title: String
) : Valuable<String> {

    /**
     * A chest inventory, with 54 slots of type CONTAINER.
     */
    CHEST_6(54, "Chest") {
        override fun createInventory(owner: InventoryHolder?, title: String): Inventory {
            return Bukkit.createInventory(owner, 54, title)
        }
    },
    /**
     * A chest inventory, with 45 slots of type CONTAINER.
     */
    CHEST_5(45, "Chest") {
        override fun createInventory(owner: InventoryHolder?, title: String): Inventory {
            return Bukkit.createInventory(owner, 45, title)
        }
    },
    /**
     * A chest inventory, with 36 slots of type CONTAINER.
     */
    CHEST_4(36, "Chest") {
        override fun createInventory(owner: InventoryHolder?, title: String): Inventory {
            return Bukkit.createInventory(owner, 36, title)
        }
    },
    /**
     * A chest inventory, with 27 slots of type CONTAINER.
     */
    CHEST_3(27, "Chest") {
        override fun createInventory(owner: InventoryHolder?, title: String): Inventory {
            return Bukkit.createInventory(owner, 27, title)
        }
    },
    /**
     * A chest inventory, with 18 slots of type CONTAINER.
     */
    CHEST_2(18, "Chest") {
        override fun createInventory(owner: InventoryHolder?, title: String): Inventory {
            return Bukkit.createInventory(owner, 18, title)
        }
    },
    /**
     * A chest inventory, with 9 slots of type CONTAINER.
     */
    CHEST_1(9, "Chest") {
        override fun createInventory(owner: InventoryHolder?, title: String): Inventory {
            return Bukkit.createInventory(owner, 9, title)
        }
    },
    /**
     * A workbench inventory, with 9 CRAFTING slots and a RESULT slot.
     */
    WORKBENCH(10, "Crafting") {
        override fun createInventory(owner: InventoryHolder?, title: String): Inventory {
            return Bukkit.createInventory(owner, InventoryType.WORKBENCH, title)
        }
    },
    /**
     * An enchantment table inventory, with two CRAFTING slots and three
     * enchanting buttons.
     */
    ENCHANTING(2, "Enchanting") {
        override fun createInventory(owner: InventoryHolder?, title: String): Inventory {
            return Bukkit.createInventory(owner, InventoryType.ENCHANTING, title)
        }
    },
    /**
     * An anvil inventory, with 2 CRAFTING slots and 1 RESULT slot
     */
    ANVIL(3, "Repairing") {
        override fun createInventory(owner: InventoryHolder?, title: String): Inventory {
            return Bukkit.createInventory(owner, InventoryType.ANVIL, title)
        }
    },
    /**
     * The merchant inventory, with 2 TRADE-IN slots, and 1 RESULT slot.
     */
    MERCHANT(3, "Merchant") {
        override fun createInventory(owner: InventoryHolder?, title: String): Inventory {
            return Bukkit.createInventory(owner, InventoryType.MERCHANT, title)
        }
    },
    ;

    abstract fun createInventory(owner: InventoryHolder?, title: String): Inventory

    override val value: String
        get() = name
}
