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

package com.lgou2w.ldk.bukkit.item

import com.lgou2w.ldk.chat.ChatComponent
import com.lgou2w.ldk.common.Builder
import com.lgou2w.ldk.common.Predicate
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface ItemBuilder : Builder<ItemStack> {

    val tag: NBTTagCompound

    fun getDurability(block: (ItemBuilder, Int) -> Unit) : ItemBuilder

    fun setDurability(durability: Int) : ItemBuilder

    fun getDisplayName(block: (ItemBuilder, String?) -> Unit) : ItemBuilder

    fun setDisplayName(displayName: String) : ItemBuilder

    fun setDisplayName(displayName: ChatComponent) : ItemBuilder

    fun removeDisplayName(predicate: Predicate<String>? = null) : ItemBuilder

    fun setLore(vararg lore: String) : ItemBuilder

    fun addEnchantment(enchantment: Enchantment, level: Int) : ItemBuilder

    fun a() : ItemBuilder

    companion object {

        fun of(itemStack: ItemStack) : ItemBuilder
                = SimpleItemBuilder(itemStack)

        @JvmOverloads
        fun of(material: Material, count: Int = 1, durability: Int = 0) : ItemBuilder
                = SimpleItemBuilder(material, count, durability)

        fun <T: ItemBuilder> of(builder: T) : T {
            return builder
        }
    }
}
