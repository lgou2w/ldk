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

import com.lgou2w.ldk.bukkit.attribute.AttributeItemModifier
import com.lgou2w.ldk.bukkit.attribute.AttributeType
import com.lgou2w.ldk.bukkit.attribute.Operation
import com.lgou2w.ldk.bukkit.attribute.Slot
import com.lgou2w.ldk.chat.ChatComponent
import com.lgou2w.ldk.common.Builder
import com.lgou2w.ldk.common.Predicate
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

interface ItemBuilder : Builder<ItemStack> {

    val tag: NBTTagCompound

    fun getDurability(block: (ItemBuilder, Int) -> Unit) : ItemBuilder

    fun setDurability(durability: Int) : ItemBuilder

    fun increaseDurability(durability: Int) : ItemBuilder

    fun decreaseDurability(durability: Int) : ItemBuilder

    fun getDisplayName(block: (ItemBuilder, ChatComponent?) -> Unit) : ItemBuilder

    fun setDisplayName(displayName: ChatComponent) : ItemBuilder

    fun removeDisplayName(predicate: Predicate<ChatComponent>? = null) : ItemBuilder

    fun getLocalizedName(block: (ItemBuilder, ChatComponent?) -> Unit) : ItemBuilder

    fun setLocalizedName(localizedName: ChatComponent) : ItemBuilder

    fun removeLocalizedName(predicate: Predicate<ChatComponent>? = null) : ItemBuilder

    fun getLore(block: (ItemBuilder, List<String>?) -> Unit) : ItemBuilder

    fun setLore(vararg lore: String) : ItemBuilder

    fun clearLore() : ItemBuilder

    fun addLore(vararg lore: String) : ItemBuilder

    fun removeLore(predicate: Predicate<String>? = null) : ItemBuilder

    fun getEnchantment(block: (ItemBuilder, Map<Enchantment, Int>?) -> Unit) : ItemBuilder

    fun setEnchantment(enchantments : Map<Enchantment, Int>) : ItemBuilder

    fun clearEnchantment() : ItemBuilder

    fun addEnchantment(enchantment: Enchantment, level: Int) : ItemBuilder

    fun removeEnchantment(enchantment: Enchantment) : ItemBuilder

    fun removeEnchantment(predicate: Predicate<Pair<Enchantment, Int>>? = null) : ItemBuilder

    fun getFlag(block: (ItemBuilder, Array<ItemFlag>?) -> Unit) : ItemBuilder

    fun setFlag(flag: Array<ItemFlag>) : ItemBuilder

    fun clearFlag() : ItemBuilder

    fun addFlag(vararg flag: ItemFlag) : ItemBuilder

    fun removeFlag(vararg flag: ItemFlag) : ItemBuilder

    fun isUnbreakable(block: (ItemBuilder, Boolean) -> Unit) : ItemBuilder

    fun setUnbreakable(unbreakable: Boolean) : ItemBuilder

    fun getAttribute(block: (ItemBuilder, List<AttributeItemModifier>?) -> Unit) : ItemBuilder

    fun setAttribute(attributes: List<AttributeItemModifier>) : ItemBuilder

    fun clearAttribute() : ItemBuilder

    fun addAttribute(attribute: AttributeItemModifier) : ItemBuilder

    fun addAttribute(type: AttributeType, operation: Operation, amount: Double, uuid: UUID = UUID.randomUUID()): ItemBuilder

    fun addAttribute(type: AttributeType, operation: Operation, slot: Slot?, amount: Double, uuid: UUID = UUID.randomUUID()): ItemBuilder

    fun addAttribute(type: AttributeType, name: String = type.value, operation: Operation, amount: Double, uuid: UUID = UUID.randomUUID()): ItemBuilder

    fun addAttribute(type: AttributeType, name: String = type.value, operation: Operation, slot: Slot?, amount: Double, uuid: UUID = UUID.randomUUID()): ItemBuilder

    fun removeAttribute(type: AttributeType) : ItemBuilder

    fun removeAttribute(predicate: Predicate<AttributeItemModifier>?) : ItemBuilder

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
