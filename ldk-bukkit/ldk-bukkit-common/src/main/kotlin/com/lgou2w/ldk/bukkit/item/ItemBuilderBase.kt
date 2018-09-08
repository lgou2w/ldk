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
import com.lgou2w.ldk.common.Function
import com.lgou2w.ldk.common.Predicate
import com.lgou2w.ldk.common.isOrLater
import com.lgou2w.ldk.nbt.NBT
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.NBTTagList
import com.lgou2w.ldk.nbt.ofCompound
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

abstract class ItemBuilderBase : ItemBuilder {

    private val itemStack: ItemStack

    constructor(itemStack: ItemStack) {
        this.itemStack = itemStack
        this.tag = ItemFactory.readTagSafe(itemStack)
    }

    constructor(material: Material, count: Int, durability: Int) {
        var lazyDurability = false
        this.itemStack = try {
            @Suppress("DEPRECATION")
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
        return removeIf<T, T>(key, { it }, predicate)
    }

    private fun <T, R> NBTTagCompound.removeIf(key: String, transform: Function<T, R>, predicate: Predicate<R>?) {
        if (predicate == null) {
            remove(key)
        } else {
            val value = get(key) ?: return
            @Suppress("UNCHECKED_CAST")
            if (value.type.isWrapper() && predicate(transform(value as T)))
                remove(key)
            else if (predicate(transform(value.value as T)))
                remove(key)
        }
    }

    override fun build(): ItemStack {
        return ItemFactory.writeTag(itemStack, tag)
    }

    final override val tag: NBTTagCompound

    final override fun getDurability(block: (ItemBuilder, Int) -> Unit): ItemBuilder {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
            block(this, tag.getShortOrNull(NBT.TAG_DAMAGE)?.toInt() ?: 0)
        else
            @Suppress("DEPRECATION")
            block(this, itemStack.durability.toInt())
        return this
    }

    final override fun setDurability(durability: Int): ItemBuilder {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
            tag.putShort(NBT.TAG_DAMAGE, durability)
        else
            @Suppress("DEPRECATION")
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

    override fun getLore(block: (ItemBuilder, List<String>?) -> Unit): ItemBuilder {
        val lore = tag.getCompoundOrNull(NBT.TAG_DISPLAY)
            ?.getListOrNull(NBT.TAG_DISPLAY_LORE)
            ?.asElements<String>()
        block(this, lore)
        return this
    }

    override fun setLore(vararg lore: String): ItemBuilder {
        clearLore()
        addLore(*lore)
        return this
    }

    override fun clearLore(): ItemBuilder {
        tag.getCompoundOrNull(NBT.TAG_DISPLAY)
            ?.remove(NBT.TAG_DISPLAY_LORE)
        return this
    }

    override fun addLore(vararg lore: String): ItemBuilder {
        tag.getCompoundOrDefault(NBT.TAG_DISPLAY)
            .getListOrDefault(NBT.TAG_DISPLAY_LORE)
            .addString(*lore)
        return this
    }

    override fun removeLore(predicate: Predicate<List<String>>?): ItemBuilder {
        tag.getCompoundOrNull(NBT.TAG_DISPLAY)
            ?.removeIf<NBTTagList, List<String>>(NBT.TAG_DISPLAY_LORE, { it.asElements() }, predicate)
        return this
    }

    override fun getEnchantment(block: (ItemBuilder, Map<Enchantment, Int>?) -> Unit): ItemBuilder {
        val enchantments = if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
            tag.getListOrNull(NBT.TAG_ENCH_FRESHLY)
                ?.asElements<NBTTagCompound>()
                ?.associate {
                    val id = it.getString(NBT.TAG_ENCH_ID)
                    Enchantment.fromName(id) to it.getShort(NBT.TAG_ENCH_LVL).toInt()
                }
        } else {
            tag.getListOrNull(NBT.TAG_ENCH_LEGACY)
                ?.asElements<NBTTagCompound>()
                ?.associate {
                    val id = it.getShort(NBT.TAG_ENCH_ID).toInt()
                    Enchantment.fromId(id) to it.getShort(NBT.TAG_ENCH_LVL).toInt()
                }
        }
        block(this, enchantments)
        return this
    }

    override fun setEnchantment(enchantments: Map<Enchantment, Int>): ItemBuilder {
        clearEnchantment()
        enchantments.forEach { addEnchantment(it.key, it.value) }
        return this
    }

    override fun clearEnchantment(): ItemBuilder {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
            tag.remove(NBT.TAG_ENCH_FRESHLY)
        } else {
            tag.remove(NBT.TAG_ENCH_LEGACY)
        }
        return this
    }

    override fun addEnchantment(enchantment: Enchantment, level: Int): ItemBuilder {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
            tag.getListOrDefault(NBT.TAG_ENCH_FRESHLY)
                .addCompound(ofCompound {
                    putString(NBT.TAG_ENCH_ID, enchantment.type)
                    putShort(NBT.TAG_ENCH_LVL, level)
                })
        } else {
            tag.getListOrDefault(NBT.TAG_ENCH_LEGACY)
                .addCompound(ofCompound {
                    putShort(NBT.TAG_ENCH_ID, enchantment.id)
                    putShort(NBT.TAG_ENCH_LVL, level)
                })
        }
        return this
    }
}
