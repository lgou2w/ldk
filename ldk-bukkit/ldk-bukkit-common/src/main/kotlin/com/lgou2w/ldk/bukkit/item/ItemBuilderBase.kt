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

import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.chat.ChatComponent
import com.lgou2w.ldk.chat.ChatSerializer
import com.lgou2w.ldk.common.Predicate
import com.lgou2w.ldk.common.isOrLater
import com.lgou2w.ldk.nbt.NBT
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.ofCompound
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import java.util.*

abstract class ItemBuilderBase : ItemBuilder {

    private val itemStack: ItemStack

    constructor(itemStack: ItemStack) {
        this.itemStack = itemStack
        this.tag = ItemFactory.readTagSafe(itemStack)
    }

    constructor(material: Material, count: Int, durability: Int) {
        var lazyDurability = false
        this.itemStack = try {
            ItemStack(material, count, durability.toShort())
        } catch (e: NoSuchMethodException) {
            try {
                ItemStack(material, count).apply { (itemMeta as Damageable).damage = durability }
            } catch (e1: ClassNotFoundException) {
                // The item stack does not support the durability, joking?
                lazyDurability = true
                ItemStack(material, count)
            }
        }
        this.tag = ItemFactory.readTagSafe(itemStack)
        if (lazyDurability)
            setDurability(durability)
    }

    private fun <T> NBTTagCompound.removeIf(key: String, predicate: Predicate<T>?) {
        if (predicate == null) {
            remove(key)
            return
        }
        val value = get(key) ?: return
        @Suppress("UNCHECKED_CAST")
        if (value.type.isWrapper() && predicate(value as T))
            remove(key)
        else if (predicate(value.value as T))
            remove(key)
    }

    override fun build(): ItemStack {
        return ItemFactory.writeTag(itemStack, tag)
    }

    final override val tag: NBTTagCompound

    final override fun getDurability(block: (ItemBuilder, Int) -> Unit): ItemBuilder {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
            block(this, tag.getShortOrNull(NBT.TAG_DAMAGE)?.toInt() ?: 0)
        else
            block(this, itemStack.durability.toInt())
        return this
    }

    final override fun setDurability(durability: Int): ItemBuilder {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
            tag.putShort(NBT.TAG_DAMAGE, durability)
        else
            itemStack.durability = durability.toShort()
        return this
    }

    override fun getDisplayName(block: (ItemBuilder, String?) -> Unit): ItemBuilder {
        val displayName = tag.getCompoundOrNull(NBT.TAG_DISPLAY)
            ?.getStringOrNull(NBT.TAG_DISPLAY_NAME)
        block(this, displayName)
        return this
    }

    override fun setDisplayName(displayName: String): ItemBuilder {
        tag.getCompoundOrDefault(NBT.TAG_DISPLAY)
            .putString(NBT.TAG_DISPLAY_NAME, displayName)
        return this
    }

    override fun setDisplayName(displayName: ChatComponent): ItemBuilder {
        val value = if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
            displayName.toJson()
        else
            ChatSerializer.toRaw(displayName)
        return setDisplayName(value)
    }

    override fun removeDisplayName(predicate: Predicate<String>?): ItemBuilder {
        tag.getCompoundOrNull(NBT.TAG_DISPLAY)
            ?.removeIf(NBT.TAG_DISPLAY_NAME, predicate)
        return this
    }

    override fun setLore(vararg lore: String): ItemBuilder {
        tag.getCompoundOrDefault(NBT.TAG_DISPLAY)
            .getListOrDefault(NBT.TAG_DISPLAY_LORE)
            .apply { lore.forEach { addString(it) } }
        return this
    }

    private val dynamicEnchantment = if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
        NBT.TAG_ENCH_FRESHLY
    else
        NBT.TAG_ENCH_LEGACY

    override fun addEnchantment(enchantment: Enchantment, level: Int): ItemBuilder {
        tag.getListOrDefault(dynamicEnchantment)
            .addCompound(ofCompound {
                if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
                    putString(NBT.TAG_ENCH_ID, enchantment.key)
                else
                    putShort(NBT.TAG_ENCH_ID, enchantment.id)
                putShort(NBT.TAG_ENCH_LVL, level)
            })
        return this
    }

    // test
    override fun a(): ItemBuilder {
        tag.getListOrDefault(NBT.TAG_ATTRIBUTE_MODIFIERS)
            .addCompound(ofCompound {
                val uuid = UUID.randomUUID()
                putString(NBT.TAG_ATTRIBUTE_SLOT, "mainhand")
                putString(NBT.TAG_ATTRIBUTE_NAME, "abc")
                putString(NBT.TAG_ATTRIBUTE_TYPE, "generic.attackDamage")
                putInt(NBT.TAG_ATTRIBUTE_OPERATION, 0)
                putDouble(NBT.TAG_ATTRIBUTE_AMOUNT, 10.0)
                putLong(NBT.TAG_ATTRIBUTE_UUID_LEAST, uuid.leastSignificantBits)
                putLong(NBT.TAG_ATTRIBUTE_UUID_MOST, uuid.mostSignificantBits)
            })
        return this
    }
}
