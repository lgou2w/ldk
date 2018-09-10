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
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.chat.ChatComponent
import com.lgou2w.ldk.chat.ChatSerializer
import com.lgou2w.ldk.common.Enums
import com.lgou2w.ldk.common.Function
import com.lgou2w.ldk.common.Predicate
import com.lgou2w.ldk.common.isOrLater
import com.lgou2w.ldk.nbt.NBT
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.NBTTagList
import com.lgou2w.ldk.nbt.ofCompound
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import java.util.*

abstract class ItemBuilderBase : ItemBuilder {

    private val itemStack: ItemStack

    constructor(itemStack: ItemStack) {
        this.itemStack = itemStack.clone()
        this.tag = ItemFactory.readTagSafe(this.itemStack)
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
        removeIf<T, T>(key, { it }, predicate)
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

    private fun <T> NBTTagList.removeIf(predicate: Predicate<T>?) {
        removeIf<T, T>({ it }, predicate)
    }

    private fun <T, R> NBTTagList.removeIf(transform: Function<T, R>, predicate: Predicate<R>?) {
        if (predicate == null) {
            clear()
        } else {
            @Suppress("UNCHECKED_CAST")
            value.removeIf {
                predicate(transform(
                        if (it.type.isWrapper()) it as T
                        else it.value as T
                ))
            }
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

    override fun increaseDurability(durability: Int): ItemBuilder {
        var current = 0
        getDurability { _, value -> current = value }
        setDurability(current - durability) // The smaller the value, the higher the durability
        return this
    }

    override fun decreaseDurability(durability: Int): ItemBuilder {
        var current = 0
        getDurability { _, value -> current = value }
        setDurability(current + durability) // The higher the value, the lower the durability
        return this
    }

    override fun getDisplayName(block: (ItemBuilder, ChatComponent?) -> Unit): ItemBuilder {
        val displayName = tag.getCompoundOrNull(NBT.TAG_DISPLAY)
            ?.getStringOrNull(NBT.TAG_DISPLAY_NAME)
        val value = if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
            ChatSerializer.fromJsonOrNull(displayName)
        else
            ChatSerializer.fromRawOrNull(displayName)
        block(this, value)
        return this
    }

    override fun setDisplayName(displayName: ChatComponent): ItemBuilder {
        val value = if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
            displayName.toJson()
        else
            ChatSerializer.toRaw(displayName)
        tag.getCompoundOrDefault(NBT.TAG_DISPLAY)
            .putString(NBT.TAG_DISPLAY_NAME, value)
        return this
    }

    override fun removeDisplayName(predicate: Predicate<ChatComponent>?): ItemBuilder {
        tag.getCompoundOrNull(NBT.TAG_DISPLAY)
            ?.removeIf<String, ChatComponent>(NBT.TAG_DISPLAY_NAME, {
                if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
                    ChatSerializer.fromJson(it)
                else
                    ChatSerializer.fromRaw(it)
            }, predicate)
        return this
    }

    override fun getLocalizedName(block: (ItemBuilder, ChatComponent?) -> Unit): ItemBuilder {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
            getDisplayName(block)
        } else {
            val value = tag.getCompoundOrNull(NBT.TAG_DISPLAY)
                ?.getStringOrNull(NBT.TAG_DISPLAY_LOC_NAME)
            block(this, ChatSerializer.fromRawOrNull(value))
        }
        return this
    }

    override fun setLocalizedName(localizedName: ChatComponent): ItemBuilder {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
            setDisplayName(localizedName)
        } else {
            tag.getCompoundOrDefault(NBT.TAG_DISPLAY)
                .putString(NBT.TAG_DISPLAY_LOC_NAME, localizedName.toRaw())
        }
        return this
    }

    override fun removeLocalizedName(predicate: Predicate<ChatComponent>?): ItemBuilder {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
            removeDisplayName(predicate)
        } else {
            tag.getCompoundOrNull(NBT.TAG_DISPLAY)
                ?.removeIf<String, ChatComponent>(NBT.TAG_DISPLAY_LOC_NAME, {
                    ChatSerializer.fromRaw(it)
                }, predicate)
        }
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

    override fun removeLore(predicate: Predicate<String>?): ItemBuilder {
        tag.getCompoundOrNull(NBT.TAG_DISPLAY)
            ?.getListOrNull(NBT.TAG_DISPLAY_LORE)
            ?.removeIf(predicate)
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

    override fun removeEnchantment(enchantment: Enchantment): ItemBuilder {
        removeEnchantment { it.first == enchantment }
        return this
    }

    override fun removeEnchantment(predicate: Predicate<Pair<Enchantment, Int>>?): ItemBuilder {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
            tag.getListOrNull(NBT.TAG_ENCH_FRESHLY)
                ?.removeIf<NBTTagCompound, Pair<Enchantment, Int>>({
                    val id = it.getString(NBT.TAG_ENCH_ID)
                    Enchantment.fromName(id) to it.getShort(NBT.TAG_ENCH_LVL).toInt()
                }, predicate)
        } else {
            tag.getListOrNull(NBT.TAG_ENCH_LEGACY)
                ?.removeIf<NBTTagCompound, Pair<Enchantment, Int>>({
                    val id = it.getShort(NBT.TAG_ENCH_ID).toInt()
                    Enchantment.fromId(id) to it.getShort(NBT.TAG_ENCH_LVL).toInt()
                }, predicate)
        }
        return this
    }

    private fun addFlagBit(modifier: Int, vararg flag: ItemFlag) : Int {
        var value = modifier
        flag.forEach { value = value or (1 shl it.ordinal) }
        return value
    }

    private fun removeFlagBit(modifier: Int, vararg flag: ItemFlag) : Int {
        var value = modifier
        flag.forEach { value = value and (1 shl it.ordinal).inv() }
        return value
    }

    private fun getFlags(modifier: Int?) : Array<ItemFlag>? {
        return if (modifier == null)
            null
        else {
            ItemFlag.values()
                .filter { it.ordinal and (1 shl it.ordinal) == 1 shl it.ordinal }
                .toTypedArray()
        }
    }

    override fun getFlag(block: (ItemBuilder, Array<ItemFlag>?) -> Unit): ItemBuilder {
        val modifier = tag.getIntOrNull(NBT.TAG_HIDE_FLAGS)
        block(this, getFlags(modifier))
        return this
    }

    override fun setFlag(flag: Array<ItemFlag>): ItemBuilder {
        clearFlag()
        addFlag(*flag)
        return this
    }

    override fun clearFlag(): ItemBuilder {
        tag.remove(NBT.TAG_HIDE_FLAGS)
        return this
    }

    override fun addFlag(vararg flag: ItemFlag): ItemBuilder {
        val modifier = tag.getIntOrDefault(NBT.TAG_HIDE_FLAGS)
        tag.putInt(NBT.TAG_HIDE_FLAGS, addFlagBit(modifier, *flag))
        return this
    }

    override fun removeFlag(vararg flag: ItemFlag): ItemBuilder {
        val modifier = tag.getIntOrDefault(NBT.TAG_HIDE_FLAGS)
        tag.putInt(NBT.TAG_HIDE_FLAGS, removeFlagBit(modifier, *flag))
        return this
    }

    override fun isUnbreakable(block: (ItemBuilder, Boolean) -> Unit): ItemBuilder {
        val value = tag.getBooleanOrNull(NBT.TAG_UNBREAKABLE)
        block(this, value ?: false)
        return this
    }

    override fun setUnbreakable(unbreakable: Boolean): ItemBuilder {
        tag.putBoolean(NBT.TAG_UNBREAKABLE, unbreakable)
        return this
    }

    override fun getAttribute(block: (ItemBuilder, List<AttributeItemModifier>?) -> Unit): ItemBuilder {
        val attributeModifiers = tag.getListOrNull(NBT.TAG_ATTRIBUTE_MODIFIERS)
            ?.asElements<NBTTagCompound>()
            ?.map { attribute ->
                val type = Enums.ofValuableNotNull(AttributeType::class.java, attribute.getString(NBT.TAG_ATTRIBUTE_TYPE))
                val name = attribute.getString(NBT.TAG_ATTRIBUTE_NAME)
                val operation = Enums.ofValuableNotNull(Operation::class.java, attribute.getInt(NBT.TAG_ATTRIBUTE_OPERATION))
                val slot = Enums.ofValuable(Slot::class.java, attribute.getStringOrNull(NBT.TAG_ATTRIBUTE_SLOT))
                val amount = attribute.getDouble(NBT.TAG_ATTRIBUTE_AMOUNT)
                val uuidMost = attribute.getLongOrNull(NBT.TAG_ATTRIBUTE_UUID_MOST)
                val uuidLeast = attribute.getLongOrNull(NBT.TAG_ATTRIBUTE_UUID_LEAST)
                val uuid = if (uuidLeast == null || uuidMost == null) UUID.randomUUID() else UUID(uuidMost, uuidLeast)
                AttributeItemModifier(type, name, operation, slot, amount, uuid)
            }
        block(this, attributeModifiers)
        return this
    }

    override fun setAttribute(attributes: List<AttributeItemModifier>): ItemBuilder {
        clearAttribute()
        tag.getListOrDefault(NBT.TAG_ATTRIBUTE_MODIFIERS)
            .addCompound(*attributes.map {
                it.save(ofCompound())
            }.toTypedArray())
        return this
    }

    override fun clearAttribute(): ItemBuilder {
        tag.remove(NBT.TAG_ATTRIBUTE_MODIFIERS)
        return this
    }

    override fun addAttribute(attribute: AttributeItemModifier): ItemBuilder {
        tag.getListOrDefault(NBT.TAG_ATTRIBUTE_MODIFIERS)
            .addCompound(attribute.save(ofCompound()))
        return this
    }

    override fun addAttribute(type: AttributeType, operation: Operation, amount: Double, uuid: UUID): ItemBuilder {
        addAttribute(AttributeItemModifier(type, type.name, operation, null, amount, uuid))
        return this
    }

    override fun addAttribute(type: AttributeType, name: String, operation: Operation, amount: Double, uuid: UUID): ItemBuilder {
        addAttribute(AttributeItemModifier(type, name, operation, null, amount, uuid))
        return this
    }

    override fun addAttribute(type: AttributeType, operation: Operation, slot: Slot?, amount: Double, uuid: UUID): ItemBuilder {
        addAttribute(AttributeItemModifier(type, type.name, operation, slot, amount, uuid))
        return this
    }

    override fun addAttribute(type: AttributeType, name: String, operation: Operation, slot: Slot?, amount: Double, uuid: UUID): ItemBuilder {
        addAttribute(AttributeItemModifier(type, name, operation, slot, amount, uuid))
        return this
    }

    override fun removeAttribute(type: AttributeType): ItemBuilder {
        removeAttribute { it.type == type }
        return this
    }

    override fun removeAttribute(predicate: Predicate<AttributeItemModifier>?): ItemBuilder {
        tag.getListOrNull(NBT.TAG_ATTRIBUTE_MODIFIERS)
            ?.removeIf<NBTTagCompound, AttributeItemModifier>({ attribute ->
                val type = Enums.ofValuableNotNull(AttributeType::class.java, attribute.getString(NBT.TAG_ATTRIBUTE_TYPE))
                val name = attribute.getString(NBT.TAG_ATTRIBUTE_NAME)
                val operation = Enums.ofValuableNotNull(Operation::class.java, attribute.getInt(NBT.TAG_ATTRIBUTE_OPERATION))
                val slot = Enums.ofValuable(Slot::class.java, attribute.getStringOrNull(NBT.TAG_ATTRIBUTE_SLOT))
                val amount = attribute.getDouble(NBT.TAG_ATTRIBUTE_AMOUNT)
                val uuidMost = attribute.getLongOrNull(NBT.TAG_ATTRIBUTE_UUID_MOST)
                val uuidLeast = attribute.getLongOrNull(NBT.TAG_ATTRIBUTE_UUID_LEAST)
                val uuid = if (uuidLeast == null || uuidMost == null) UUID.randomUUID() else UUID(uuidMost, uuidLeast)
                AttributeItemModifier(type, name, operation, slot, amount, uuid)
            }, predicate)
        return this
    }
}
