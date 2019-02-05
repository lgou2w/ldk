/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
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
import com.lgou2w.ldk.bukkit.firework.FireworkEffect
import com.lgou2w.ldk.bukkit.firework.FireworkType
import com.lgou2w.ldk.bukkit.potion.PotionBase
import com.lgou2w.ldk.bukkit.potion.PotionEffectCustom
import com.lgou2w.ldk.bukkit.potion.PotionEffectType
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.chat.ChatComponent
import com.lgou2w.ldk.chat.ChatSerializer
import com.lgou2w.ldk.common.ApplicatorFunction
import com.lgou2w.ldk.common.BiFunction
import com.lgou2w.ldk.common.Enums
import com.lgou2w.ldk.common.Predicate
import com.lgou2w.ldk.common.isOrLater
import com.lgou2w.ldk.common.isTrue
import com.lgou2w.ldk.common.letIfNotNull
import com.lgou2w.ldk.nbt.NBT
import com.lgou2w.ldk.nbt.NBTBase
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.NBTTagString
import com.lgou2w.ldk.nbt.NBTType
import com.lgou2w.ldk.nbt.ofCompound
import com.lgou2w.ldk.nbt.removeIf
import com.lgou2w.ldk.nbt.removeIfIndexed
import org.bukkit.Color
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

    override fun build(): ItemStack {
        return ItemFactory.writeTag(itemStack, tag)
    }

    final override val tag: NBTTagCompound

    final override val material: Material
        get() = itemStack.type

    final override val maxDurability: Int
        get() = itemStack.type.maxDurability.toInt()

    final override val maxStackSize: Int
        get() = itemStack.type.maxStackSize

    final override fun reBuilder(material: Material, count: Int, durability: Int): ItemBuilder
            = ItemBuilder.of(material, count, durability)

    final override fun reBuilder(material: Material, count: Int): ItemBuilder
            = ItemBuilder.of(material, count, this.durability)

    final override fun reBuilder(material: Material): ItemBuilder
            = ItemBuilder.of(material, this.count, this.durability)

    override fun reBuilder(count: Int): ItemBuilder
            = ItemBuilder.of(this.material, count, this.durability)

    override fun reBuilder(count: Int, durability: Int): ItemBuilder
            = ItemBuilder.of(this.material, count, durability)

    //<editor-fold desc="ItemBuilder - Generic" defaultstate="collapsed">

    final override var count: Int
        get() = itemStack.amount
        set(value) { itemStack.amount = value }

    override fun getCount(block: (ItemBuilder, Int) -> Unit): ItemBuilder {
        block(this, count)
        return this
    }

    override fun setCount(count: Int): ItemBuilder {
        this.count = count
        return this
    }

    override fun setCountIf(count: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.count = count
        return this
    }

    override fun increaseCount(count: Int): ItemBuilder {
        this.count += count
        return this
    }

    override fun increaseCountIf(count: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.count += count
        return this
    }

    override fun decreaseCount(count: Int): ItemBuilder {
        this.count -= count
        return this
    }

    override fun decreaseCountIf(count: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.count -= count
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Durability" defaultstate="collapsed">

    override var durability: Int
        get() {
            return if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
                tag.getShortOrNull(NBT.TAG_DAMAGE)?.toInt() ?: 0
            else
                @Suppress("DEPRECATION")
                itemStack.durability.toInt()
        }
        set(value) {
            if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
                tag.putShort(NBT.TAG_DAMAGE, value)
            else
                @Suppress("DEPRECATION")
                itemStack.durability = value.toShort()
        }

    final override fun getDurability(block: (ItemBuilder, Int) -> Unit): ItemBuilder {
        block(this, durability)
        return this
    }

    final override fun setDurability(durability: Int): ItemBuilder {
        this.durability = durability
        return this
    }

    override fun setDurabilityIf(durability: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.durability = durability
        return this
    }

    override fun increaseDurability(durability: Int): ItemBuilder {
        this.durability -= durability // The smaller the value, the higher the durability
        return this
    }

    override fun increaseDurabilityIf(durability: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.durability -= durability
        return this
    }

    override fun decreaseDurability(durability: Int): ItemBuilder {
        this.durability += durability // The higher the value, the lower the durability
        return this
    }

    override fun decreaseDurabilityIf(durability: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.durability += durability
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - DisplayName" defaultstate="collapsed">

    override var displayName: ChatComponent?
        get() {
            val displayName = tag.getCompoundOrNull(NBT.TAG_DISPLAY)
                ?.getStringOrNull(NBT.TAG_DISPLAY_NAME)
            return if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
                ChatSerializer.fromJsonOrNull(displayName)
            else
                ChatSerializer.fromRawOrNull(displayName)
        }
        set(value) {
            if (value == null)
                removeDisplayName()
            else {
                val displayName = if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
                    value.toJson()
                else
                    ChatSerializer.toRaw(value)
                tag.getCompoundOrDefault(NBT.TAG_DISPLAY)
                    .putString(NBT.TAG_DISPLAY_NAME, displayName)
            }
        }

    override fun getDisplayName(block: (ItemBuilder, ChatComponent?) -> Unit): ItemBuilder {
        block(this, displayName)
        return this
    }

    override fun setDisplayName(displayName: String?): ItemBuilder {
        this.displayName = ChatSerializer.fromRawOrNull(displayName)
        return this
    }

    override fun setDisplayNameIf(displayName: String?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.displayName = ChatSerializer.fromRawOrNull(displayName)
        return this
    }

    override fun setDisplayName(displayName: ChatComponent?): ItemBuilder {
        this.displayName = displayName
        return this
    }

    override fun setDisplayNameIf(displayName: ChatComponent?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.displayName = displayName
        return this
    }

    override fun removeDisplayName(): ItemBuilder {
        removeDisplayName(null)
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

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - LocalizedName" defaultstate="collapsed">

    override var localizedName: ChatComponent?
        get() {
            return if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
                displayName
            } else {
                val value = tag.getCompoundOrNull(NBT.TAG_DISPLAY)
                    ?.getStringOrNull(NBT.TAG_DISPLAY_LOC_NAME)
                ChatSerializer.fromRawOrNull(value)
            }
        }
        set(value) {
            if (value == null)
                removeLocalizedName()
            else {
                if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
                    setDisplayName(value)
                } else {
                    tag.getCompoundOrDefault(NBT.TAG_DISPLAY)
                        .putString(NBT.TAG_DISPLAY_LOC_NAME, value.toRaw())
                }
            }
        }

    override fun getLocalizedName(block: (ItemBuilder, ChatComponent?) -> Unit): ItemBuilder {
        block(this, localizedName)
        return this
    }

    override fun setLocalizedName(localizedName: ChatComponent?): ItemBuilder {
        this.localizedName = localizedName
        return this
    }

    override fun setLocalizedNameIf(localizedName: ChatComponent?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.localizedName = localizedName
        return this
    }

    override fun removeLocalizedName(): ItemBuilder {
        removeLocalizedName(null)
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

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Lore" defaultstate="collapsed">

    override var lore: List<String>?
        get() {
            return tag.getCompoundOrNull(NBT.TAG_DISPLAY)
                ?.getListOrNull(NBT.TAG_DISPLAY_LORE)
                ?.asElements()
        }
        set(value) {
            clearLore()
            if (value != null)
                addLore(*value.toTypedArray())
        }

    override fun getLore(block: (ItemBuilder, List<String>?) -> Unit): ItemBuilder {
        block(this, lore)
        return this
    }

    override fun setLore(lore: List<String>?): ItemBuilder {
        this.lore = lore
        return this
    }

    override fun setLoreIf(lore: List<String>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.lore = lore
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

    override fun addLoreIf(vararg lore: String, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            addLore(*lore)
        return this
    }

    override fun removeLore(predicate: Predicate<String>?): ItemBuilder {
        removeLoreIndexed { _, value -> predicate?.invoke(value).isTrue() }
        return this
    }

    override fun removeLoreIndexed(block: BiFunction<Int, String, Boolean>?): ItemBuilder {
        tag.getCompoundOrNull(NBT.TAG_DISPLAY)
            ?.getListOrNull(NBT.TAG_DISPLAY_LORE)
            ?.removeIfIndexed(block)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Enchantment" defaultstate="collapsed">

    override var enchantments: Map<Enchantment, Int>?
        get() {
            return if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
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
        }
        set(value) {
            clearEnchantment()
            value?.forEach { addEnchantment(it.key, it.value) }
        }

    override fun getEnchantment(block: (ItemBuilder, Map<Enchantment, Int>?) -> Unit): ItemBuilder {
        block(this, enchantments)
        return this
    }

    override fun setEnchantment(enchantments: Map<Enchantment, Int>?): ItemBuilder {
        this.enchantments = enchantments
        return this
    }

    override fun setEnchantmentIf(enchantments: Map<Enchantment, Int>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.enchantments = enchantments
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

    override fun addEnchantment(enchantment: Pair<Enchantment, Int>): ItemBuilder {
        addEnchantment(enchantment.first, enchantment.second)
        return this
    }

    override fun addEnchantmentIf(enchantment: Pair<Enchantment, Int>, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            addEnchantment(enchantment.first, enchantment.second)
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

    override fun addEnchantmentIf(enchantment: Enchantment, level: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            addEnchantment(enchantment, level)
        return this
    }

    override fun removeEnchantment(enchantment: Enchantment): ItemBuilder {
        removeEnchantment { it.first == enchantment }
        return this
    }

    override fun removeEnchantment(predicate: Predicate<Pair<Enchantment, Int>>?): ItemBuilder {
        removeEnchantmentIndexed { _, value -> predicate?.invoke(value).isTrue() }
        return this
    }

    override fun removeEnchantmentIndexed(block: BiFunction<Int, Pair<Enchantment, Int>, Boolean>?): ItemBuilder {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
            tag.getListOrNull(NBT.TAG_ENCH_FRESHLY)
                ?.removeIfIndexed<NBTTagCompound, Pair<Enchantment, Int>>({
                    val id = it.getString(NBT.TAG_ENCH_ID)
                    Enchantment.fromName(id) to it.getShort(NBT.TAG_ENCH_LVL).toInt()
                }, block)
        } else {
            tag.getListOrNull(NBT.TAG_ENCH_LEGACY)
                ?.removeIfIndexed<NBTTagCompound, Pair<Enchantment, Int>>({
                    val id = it.getShort(NBT.TAG_ENCH_ID).toInt()
                    Enchantment.fromId(id) to it.getShort(NBT.TAG_ENCH_LVL).toInt()
                }, block)
        }
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - ItemFlag" defaultstate="collapsed">

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

    private fun getFlags(modifier: Int?) : Array<out ItemFlag>? {
        return if (modifier == null)
            null
        else {
            ItemFlag.values()
                .filter { it.ordinal and (1 shl it.ordinal) == 1 shl it.ordinal }
                .toTypedArray()
        }
    }

    override var flags: Array<out ItemFlag>?
        get() {
            val modifier = tag.getIntOrNull(NBT.TAG_HIDE_FLAGS)
            return getFlags(modifier)
        }
        set(value) {
            clearFlag()
            if (value != null)
                addFlag(*value)
        }

    override fun getFlag(block: (ItemBuilder, Array<out ItemFlag>?) -> Unit): ItemBuilder {
        block(this, flags)
        return this
    }

    override fun setFlag(flags: Array<out ItemFlag>?): ItemBuilder {
        this.flags = flags
        return this
    }

    override fun setFlagIf(flags: Array<out ItemFlag>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.flags = flags
        return this
    }

    override fun clearFlag(): ItemBuilder {
        tag.remove(NBT.TAG_HIDE_FLAGS)
        return this
    }

    override fun addFlag(vararg flags: ItemFlag): ItemBuilder {
        val modifier = tag.getIntOrDefault(NBT.TAG_HIDE_FLAGS)
        tag.putInt(NBT.TAG_HIDE_FLAGS, addFlagBit(modifier, *flags))
        return this
    }

    override fun addFlagIf(vararg flags: ItemFlag, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            addFlag(*flags)
        return this
    }

    override fun removeFlag(vararg flags: ItemFlag): ItemBuilder {
        val modifier = tag.getIntOrDefault(NBT.TAG_HIDE_FLAGS)
        tag.putInt(NBT.TAG_HIDE_FLAGS, removeFlagBit(modifier, *flags))
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Unbreakable" defaultstate="collapsed">

    override var isUnbreakable: Boolean
        get() = tag.getBooleanOrNull(NBT.TAG_UNBREAKABLE) ?: false
        set(value) { tag.putBoolean(NBT.TAG_UNBREAKABLE, value) }

    override fun isUnbreakable(block: (ItemBuilder, Boolean) -> Unit): ItemBuilder {
        block(this, isUnbreakable)
        return this
    }

    override fun setUnbreakable(unbreakable: Boolean): ItemBuilder {
        this.isUnbreakable = unbreakable
        return this
    }

    override fun setUnbreakableIf(unbreakable: Boolean, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.isUnbreakable = unbreakable
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Attribute" defaultstate="collapsed">

    override var attributes: List<AttributeItemModifier>?
        get() {
            return tag.getListOrNull(NBT.TAG_ATTRIBUTE_MODIFIERS)
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
        }
        set(value) {
            clearAttribute()
            if (value != null)
                tag.getListOrDefault(NBT.TAG_ATTRIBUTE_MODIFIERS)
                    .addCompound(*value.map {
                        it.save(ofCompound())
                    }.toTypedArray())
        }

    override fun getAttribute(block: (ItemBuilder, List<AttributeItemModifier>?) -> Unit): ItemBuilder {
        block(this, attributes)
        return this
    }

    override fun setAttribute(attributes: List<AttributeItemModifier>?): ItemBuilder {
        this.attributes = attributes
        return this
    }

    override fun setAttributeIf(attributes: List<AttributeItemModifier>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.attributes = attributes
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

    override fun addAttributeIf(attribute: AttributeItemModifier, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            addAttribute(attribute)
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
        removeAttributeIndexed { _, value -> predicate?.invoke(value).isTrue() }
        return this
    }

    override fun removeAttributeIndexed(block: BiFunction<Int, AttributeItemModifier, Boolean>?): ItemBuilder {
        tag.getListOrNull(NBT.TAG_ATTRIBUTE_MODIFIERS)
            ?.removeIfIndexed<NBTTagCompound, AttributeItemModifier>({ attribute ->
                val type = Enums.ofValuableNotNull(AttributeType::class.java, attribute.getString(NBT.TAG_ATTRIBUTE_TYPE))
                val name = attribute.getString(NBT.TAG_ATTRIBUTE_NAME)
                val operation = Enums.ofValuableNotNull(Operation::class.java, attribute.getInt(NBT.TAG_ATTRIBUTE_OPERATION))
                val slot = Enums.ofValuable(Slot::class.java, attribute.getStringOrNull(NBT.TAG_ATTRIBUTE_SLOT))
                val amount = attribute.getDouble(NBT.TAG_ATTRIBUTE_AMOUNT)
                val uuidMost = attribute.getLongOrNull(NBT.TAG_ATTRIBUTE_UUID_MOST)
                val uuidLeast = attribute.getLongOrNull(NBT.TAG_ATTRIBUTE_UUID_LEAST)
                val uuid = if (uuidLeast == null || uuidMost == null) UUID.randomUUID() else UUID(uuidMost, uuidLeast)
                AttributeItemModifier(type, name, operation, slot, amount, uuid)
            }, block)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - CanDestroy" defaultstate="collapsed">

    private fun matchMaterial(type: String) : Material? {
        return try {
            if (type.startsWith("minecraft:", true))
                Material.matchMaterial(type.substring("minecraft:".length))
            else
                Material.matchMaterial(type)
        } catch (e: Exception) {
            null
        }
    }

    override var canDestroy: List<Material>?
        get() {
            return tag.getListOrNull(NBT.TAG_CAN_DESTROY)
                ?.asElements<String>()
                ?.asSequence()
                ?.map { matchMaterial(it) }
                ?.filterNotNull()
                ?.toList()
        }
        set(value) {
            clearCanDestroy()
            if (value != null)
                addCanDestroy(*value.toTypedArray())
        }

    override fun getCanDestroy(block: (ItemBuilder, List<Material>?) -> Unit): ItemBuilder {
        block(this, canDestroy)
        return this
    }

    override fun setCanDestroy(types: List<Material>?): ItemBuilder {
        this.canDestroy = types
        return this
    }

    override fun setCanDestroyIf(types: List<Material>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.canDestroy = types
        return this
    }

    override fun clearCanDestroy(): ItemBuilder {
        tag.remove(NBT.TAG_CAN_DESTROY)
        return this
    }

    override fun addCanDestroy(vararg types: Material): ItemBuilder {
        tag.getListOrDefault(NBT.TAG_CAN_DESTROY)
            .addString(*types.map { ItemFactory.materialType(it) }.toTypedArray())
        return this
    }

    override fun addCanDestroyIf(vararg types: Material, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            addCanDestroy(*types)
        return this
    }

    override fun removeCanDestroy(vararg types: Material): ItemBuilder {
        return removeCanDestroy { types.contains(it) }
    }

    override fun removeCanDestroy(predicate: Predicate<Material>?): ItemBuilder {
        removeCanDestroyIndexed { _, value -> predicate?.invoke(value).isTrue() }
        return this
    }

    override fun removeCanDestroyIndexed(block: BiFunction<Int, Material, Boolean>?): ItemBuilder {
        tag.getListOrNull(NBT.TAG_CAN_DESTROY)
            ?.removeIfIndexed<String, Material>({ matchMaterial(it) ?: Material.AIR }, block)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - CanPlaceOn" defaultstate="collapsed">

    override var canPlaceOn: List<Material>?
        get() {
            return tag.getListOrNull(NBT.TAG_CAN_PLACE_ON)
                ?.asElements<String>()
                ?.asSequence()
                ?.map { matchMaterial(it) }
                ?.filterNotNull()
                ?.toList()
        }
        set(value) {
            clearCanPlaceOn()
            if (value != null)
                addCanPlaceOn(*value.toTypedArray())
        }

    override fun getCanPlaceOn(block: (ItemBuilder, List<Material>?) -> Unit): ItemBuilder {
        block(this, canPlaceOn)
        return this
    }

    override fun setCanPlaceOn(types: List<Material>?): ItemBuilder {
        this.canPlaceOn = types
        return this
    }

    override fun setCanPlaceOnIf(types: List<Material>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.canPlaceOn = types
        return this
    }

    override fun clearCanPlaceOn(): ItemBuilder {
        tag.remove(NBT.TAG_CAN_PLACE_ON)
        return this
    }

    override fun addCanPlaceOn(vararg types: Material): ItemBuilder {
        tag.getListOrDefault(NBT.TAG_CAN_PLACE_ON)
            .addString(*types.map { ItemFactory.materialType(it) }.toTypedArray())
        return this
    }

    override fun addCanPlaceOnIf(vararg types: Material, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            addCanPlaceOn(*types)
        return this
    }

    override fun removeCanPlaceOn(vararg types: Material): ItemBuilder {
        return removeCanPlaceOn { types.contains(it) }
    }

    override fun removeCanPlaceOn(predicate: Predicate<Material>?): ItemBuilder {
        removeCanPlaceOnIndexed { _, value -> predicate?.invoke(value).isTrue() }
        return this
    }

    override fun removeCanPlaceOnIndexed(block: BiFunction<Int, Material, Boolean>?): ItemBuilder {
        tag.getListOrNull(NBT.TAG_CAN_PLACE_ON)
            ?.removeIfIndexed<String, Material>({ matchMaterial(it) ?: Material.AIR }, block)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - RepairCost" defaultstate="collapsed">

    override var repairCost: Int?
        get() = tag.getIntOrNull(NBT.TAG_REPAIR_COST)
        set(value) {
            removeRepairCost()
            if (value != null)
                tag.putInt(NBT.TAG_REPAIR_COST, value)
        }

    override fun getRepairCost(block: (ItemBuilder, Int?) -> Unit): ItemBuilder {
        block(this, repairCost)
        return this
    }

    override fun setRepairCost(repairCost: Int?): ItemBuilder {
        this.repairCost = repairCost
        return this
    }

    override fun setRepairCostIf(repairCost: Int?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.repairCost = repairCost
        return this
    }

    override fun removeRepairCost(): ItemBuilder {
        removeRepairCost(null)
        return this
    }

    override fun removeRepairCost(predicate: Predicate<Int>?): ItemBuilder {
        tag.removeIf(NBT.TAG_REPAIR_COST, predicate)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - LeatherColor" defaultstate="collapsed">

    override var leatherColor: Color?
        get() {
            return tag.getCompoundOrNull(NBT.TAG_DISPLAY)
                ?.getIntOrNull(NBT.TAG_LEATHER_ARMOR_COLOR)
                ?.let { Color.fromRGB(it) }
        }
        set(value) {
            removeLeatherColor()
            if (value != null)
                tag.getCompoundOrDefault(NBT.TAG_DISPLAY)
                    .putInt(NBT.TAG_LEATHER_ARMOR_COLOR, value.asRGB())
        }

    override fun getLeatherColor(block: (ItemBuilder, Color?) -> Unit): ItemBuilder {
        block(this, leatherColor)
        return this
    }

    override fun setLeatherColor(leatherColor: Color?): ItemBuilder {
        this.leatherColor = leatherColor
        return this
    }

    override fun setLeatherColorIf(leatherColor: Color?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.leatherColor = leatherColor
        return this
    }

    override fun removeLeatherColor(): ItemBuilder {
        removeLeatherColor(null)
        return this
    }

    override fun removeLeatherColor(predicate: Predicate<Color>?): ItemBuilder {
        tag.getCompoundOrNull(NBT.TAG_DISPLAY)
            ?.removeIf<Int, Color>(NBT.TAG_LEATHER_ARMOR_COLOR, {
                Color.fromRGB(it)
            }, predicate)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - BookTitle" defaultstate="collapsed">

    override var bookTitle: String?
        get() = tag.getStringOrNull(NBT.TAG_BOOK_TITLE)
        set(value) {
            removeBookTitle()
            if (value != null)
                tag.putString(NBT.TAG_BOOK_TITLE, value)
        }

    override fun getBookTitle(block: (ItemBuilder, String?) -> Unit): ItemBuilder {
        block(this, bookTitle)
        return this
    }

    override fun setBookTitle(title: String?): ItemBuilder {
        this.bookTitle = title
        return this
    }

    override fun setBookTitleIf(title: String?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.bookTitle = title
        return this
    }

    override fun removeBookTitle(): ItemBuilder {
        removeBookTitle(null)
        return this
    }

    override fun removeBookTitle(predicate: Predicate<String>?): ItemBuilder {
        tag.removeIf(NBT.TAG_BOOK_TITLE, predicate)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - BookAuthor" defaultstate="collapsed">

    override var bookAuthor: String?
        get() = tag.getStringOrNull(NBT.TAG_BOOK_AUTHOR)
        set(value) {
            removeBookAuthor()
            if (value != null)
                tag.putString(NBT.TAG_BOOK_AUTHOR, value)
        }

    override fun getBookAuthor(block: (ItemBuilder, String?) -> Unit): ItemBuilder {
        block(this, bookAuthor)
        return this
    }

    override fun setBookAuthor(author: String?): ItemBuilder {
        this.bookAuthor = author
        return this
    }

    override fun setBookAuthorIf(author: String?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.bookAuthor = author
        return this
    }

    override fun removeBookAuthor(): ItemBuilder {
        removeBookAuthor(null)
        return this
    }

    override fun removeBookAuthor(predicate: Predicate<String>?): ItemBuilder {
        tag.removeIf(NBT.TAG_BOOK_AUTHOR, predicate)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - BookGeneration" defaultstate="collapsed">

    override var bookGeneration: Generation?
        get() {
            return tag.getIntOrNull(NBT.TAG_BOOK_GENERATION)
                ?.letIfNotNull { Enums.ofValuable(Generation::class.java, this) }
        }
        set(value) {
            removeBookGeneration()
            if (value != null)
                tag.putInt(NBT.TAG_BOOK_GENERATION, value.ordinal)
        }

    override fun getBookGeneration(block: (ItemBuilder, Generation?) -> Unit): ItemBuilder {
        block(this, bookGeneration)
        return this
    }

    override fun setBookGeneration(generation: Generation): ItemBuilder {
        this.bookGeneration = generation
        return this
    }

    override fun setBookGenerationIf(generation: Generation, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.bookGeneration = generation
        return this
    }

    override fun removeBookGeneration(): ItemBuilder {
        removeBookGeneration(null)
        return this
    }

    override fun removeBookGeneration(predicate: Predicate<Generation>?): ItemBuilder {
        tag.removeIf<Int, Generation>(NBT.TAG_BOOK_GENERATION, {
            Enums.ofValuableNotNull(Generation::class.java, it)
        }, predicate)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - BookPages" defaultstate="collapsed">

    override var bookPages: List<ChatComponent>?
        get() {
            return tag.getListOrNull(NBT.TAG_BOOK_PAGES)
                ?.asElements<String>()
                ?.map { ChatSerializer.fromJsonOrLenient(it) }
        }
        set(value) {
            clearBookPages()
            if (value != null)
                addBookPage(*value.toTypedArray())
        }

    override fun getBookPages(block: (ItemBuilder, List<ChatComponent>?) -> Unit): ItemBuilder {
        block(this, bookPages)
        return this
    }

    override fun getBookPage(index: Int, block: (ItemBuilder, ChatComponent?) -> Unit): ItemBuilder {
        block(this, bookPages?.getOrNull(index))
        return this
    }

    override fun getBookPage(index: Int): ChatComponent? {
        return bookPages?.getOrNull(index)
    }

    override fun setBookPages(pages: List<ChatComponent>?): ItemBuilder {
        this.bookPages = pages
        return this
    }

    override fun setBookPagesIf(pages: List<ChatComponent>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.bookPages = pages
        return this
    }

    override fun setBookPage(index: Int, page: ChatComponent): ItemBuilder {
        tag.getListOrDefault(NBT.TAG_BOOK_PAGES)
            .add(index, NBTTagString(page.toJson()))
        return this
    }

    override fun setBookPageIf(index: Int, page: ChatComponent, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            setBookPage(index, page)
        return this
    }

    override fun clearBookPages(): ItemBuilder {
        tag.remove(NBT.TAG_BOOK_PAGES)
        return this
    }

    override fun addBookPage(vararg pages: ChatComponent): ItemBuilder {
        tag.getListOrDefault(NBT.TAG_BOOK_PAGES)
            .addString(*pages.map { it.toJson() }.toTypedArray())
        return this
    }

    override fun addBookPage(vararg pages: ChatComponent, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            addBookPage(*pages)
        return this
    }

    override fun removeBookPage(predicate: Predicate<ChatComponent>?): ItemBuilder {
        removeBookPageIndexed { _, value -> predicate?.invoke(value).isTrue() }
        return this
    }

    override fun removeBookPageIndexed(block: BiFunction<Int, ChatComponent, Boolean>?): ItemBuilder {
        tag.getListOrNull(NBT.TAG_BOOK_PAGES)
            ?.removeIfIndexed<String, ChatComponent>({
                ChatSerializer.fromJsonLenient(it)
            }, block)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - StoredEnchantment" defaultstate="collapsed">

    override var storedEnchantments: Map<Enchantment, Int>?
        get() {
            return tag.getListOrNull(NBT.TAG_STORED_ENCHANTMENTS)
                ?.asElements<NBTTagCompound>()
                ?.associate {
                    val level = it.getShort(NBT.TAG_ENCH_LVL).toInt()
                    val enchantment = if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
                        Enchantment.fromName(it.getString(NBT.TAG_ENCH_ID))
                    else
                        Enchantment.fromId(it.getShort(NBT.TAG_ENCH_ID).toInt())
                    enchantment to level
                }
        }
        set(value) {
            clearStoredEnchantments()
            value?.forEach { addStoredEnchantment(it.key, it.value) }
        }

    override fun getStoredEnchantments(block: (ItemBuilder, Map<Enchantment, Int>?) -> Unit): ItemBuilder {
        block(this, storedEnchantments)
        return this
    }

    override fun setStoredEnchantments(storedEnchantments: Map<Enchantment, Int>?): ItemBuilder {
        this.storedEnchantments = storedEnchantments
        return this
    }

    override fun setStoredEnchantmentsIf(storedEnchantments: Map<Enchantment, Int>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.storedEnchantments = storedEnchantments
        return this
    }

    override fun clearStoredEnchantments(): ItemBuilder {
        tag.remove(NBT.TAG_STORED_ENCHANTMENTS)
        return this
    }

    override fun addStoredEnchantment(enchantment: Pair<Enchantment, Int>): ItemBuilder {
        addStoredEnchantment(enchantment.first, enchantment.second)
        return this
    }

    override fun addStoredEnchantmentIf(enchantment: Pair<Enchantment, Int>, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            addStoredEnchantment(enchantment.first, enchantment.second)
        return this
    }

    override fun addStoredEnchantment(enchantment: Enchantment, level: Int): ItemBuilder {
        tag.getListOrDefault(NBT.TAG_STORED_ENCHANTMENTS)
            .addCompound(ofCompound {
                putShort(NBT.TAG_ENCH_LVL, level)
                if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
                    putString(NBT.TAG_ENCH_ID, enchantment.type)
                else
                    putShort(NBT.TAG_ENCH_ID, enchantment.id)
            })
        return this
    }

    override fun addStoredEnchantmentIf(enchantment: Enchantment, level: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            addStoredEnchantment(enchantment, level)
        return this
    }

    override fun removeStoredEnchantment(enchantment: Enchantment): ItemBuilder {
        removeStoredEnchantment { it.first == enchantment }
        return this
    }

    override fun removeStoredEnchantment(predicate: Predicate<Pair<Enchantment, Int>>?): ItemBuilder {
        removeStoredEnchantmentIndexed { _, value -> predicate?.invoke(value).isTrue() }
        return this
    }

    override fun removeStoredEnchantmentIndexed(block: BiFunction<Int, Pair<Enchantment, Int>, Boolean>?): ItemBuilder {
        tag.getListOrNull(NBT.TAG_STORED_ENCHANTMENTS)
            ?.removeIfIndexed<NBTTagCompound, Pair<Enchantment, Int>>({
                val level = it.getShort(NBT.TAG_ENCH_LVL).toInt()
                val enchantment = if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
                    Enchantment.fromName(it.getString(NBT.TAG_ENCH_ID))
                else
                    Enchantment.fromId(it.getShort(NBT.TAG_ENCH_ID).toInt())
                enchantment to level
            }, block)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - SkullOwner" defaultstate="collapsed">

    override var skullOwner: String?
        get() {
            val skullOwner = tag[NBT.TAG_SKULL_OWNER] ?: return null
            return if (skullOwner.type == NBTType.TAG_COMPOUND)
                skullOwner.asCompound().getStringOrNull(NBT.TAG_SKULL_OWNER_NAME)
            else
                skullOwner.value?.toString()
        }
        set(value) {
            removeSkullOwner()
            if (value != null)
                tag.putString(NBT.TAG_SKULL_OWNER, value)
        }

    override fun getSkullOwner(block: (ItemBuilder, String?) -> Unit): ItemBuilder {
        block(this, skullOwner)
        return this
    }

    override fun setSkullOwner(skullOwner: String?): ItemBuilder {
        this.skullOwner = skullOwner
        return this
    }

    override fun setSkullOwnerIf(skullOwner: String?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.skullOwner = skullOwner
        return this
    }

    override fun removeSkullOwner(): ItemBuilder {
        removeSkullOwner(null)
        return this
    }

    override fun removeSkullOwner(predicate: Predicate<String>?): ItemBuilder {
        tag.removeIf<NBTBase<*>, String>(NBT.TAG_SKULL_OWNER, {
            if (it.type == NBTType.TAG_COMPOUND)
                it.asCompound().getStringOrNull(NBT.TAG_SKULL_OWNER_NAME) ?: "INVALID"
            else
                it.value.toString()
        }, predicate)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - SkullOwner Value" defaultstate="collapsed">

    override var skullOwnerValue: String?
        get() {
            if (tag[NBT.TAG_SKULL_OWNER]?.type == NBTType.TAG_STRING)
                tag.remove(NBT.TAG_SKULL_OWNER)
            return tag.getCompoundOrNull(NBT.TAG_SKULL_OWNER)
                ?.getCompoundOrNull(NBT.TAG_SKULL_OWNER_PROPERTIES)
                ?.getListOrNull(NBT.TAG_SKULL_OWNER_TEXTURES)
                ?.asElements<NBTTagCompound>()
                ?.firstOrNull()
                ?.getStringOrNull(NBT.TAG_SKULL_OWNER_TEXTURES_VALUE)
        }
        set(value) {
            removeSkullOwnerValue()
            if (value != null)
                setSkullOwnerValue(value, null, null)
        }

    override fun getSkullOwnerValue(block: (ItemBuilder, String?) -> Unit): ItemBuilder {
        block(this, skullOwnerValue)
        return this
    }

    override fun setSkullOwnerValue(value: String?): ItemBuilder {
        this.skullOwnerValue = value
        return this
    }

    override fun setSkullOwnerValueIf(value: String?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.skullOwnerValue = value
        return this
    }

    override fun setSkullOwnerValue(value: String, name: String?, id: UUID?): ItemBuilder {
        if (tag[NBT.TAG_SKULL_OWNER]?.type == NBTType.TAG_STRING)
            tag.remove(NBT.TAG_SKULL_OWNER)
        tag.getCompoundOrDefault(NBT.TAG_SKULL_OWNER)
            .getCompoundOrDefault(NBT.TAG_SKULL_OWNER_PROPERTIES)
            .getListOrDefault(NBT.TAG_SKULL_OWNER_TEXTURES)
            .addCompound(ofCompound {
                putString(NBT.TAG_SKULL_OWNER_TEXTURES_VALUE, value)
            })
        val skullOwner = tag.getCompoundOrDefault(NBT.TAG_SKULL_OWNER)
        skullOwner.putString(NBT.TAG_SKULL_OWNER_ID, id?.toString() ?: UUID.randomUUID().toString())
        if (name != null)
            skullOwner.putString(NBT.TAG_SKULL_OWNER_NAME, name)
        return this
    }

    override fun setSkullOwnerValue(value: String, name: String?, id: UUID?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            setSkullOwnerValue(value, name, id)
        return this
    }

    override fun removeSkullOwnerValue(): ItemBuilder {
        removeSkullOwner(null)
        return this
    }

    override fun removeSkullOwnerValue(predicate: Predicate<String>?): ItemBuilder {
        return if (tag[NBT.TAG_SKULL_OWNER]?.type == NBTType.TAG_STRING) {
            this
        } else {
            tag.getCompoundOrNull(NBT.TAG_SKULL_OWNER)
                ?.getCompoundOrNull(NBT.TAG_SKULL_OWNER_PROPERTIES)
                ?.getListOrNull(NBT.TAG_SKULL_OWNER_TEXTURES)
                ?.removeIf<NBTTagCompound, String>({
                    it.getString(NBT.TAG_SKULL_OWNER_TEXTURES_VALUE)
                }, predicate)
            this
        }
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - PotionColor" defaultstate="collapsed">

    override var potionColor: Color?
        get() = tag.getIntOrNull(NBT.TAG_CUSTOM_POTION_COLOR).letIfNotNull { Color.fromRGB(this) }
        set(value) {
            removePotionColor()
            if (value != null)
                tag.putInt(NBT.TAG_CUSTOM_POTION_COLOR, value.asRGB())
        }

    override fun getPotionColor(block: (ItemBuilder, Color?) -> Unit): ItemBuilder {
        block(this, potionColor)
        return this
    }

    override fun setPotionColor(color: Color): ItemBuilder {
        this.potionColor = color
        return this
    }

    override fun setPotionColorIf(color: Color, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.potionColor = color
        return this
    }

    override fun removePotionColor(): ItemBuilder {
        removePotionColor(null)
        return this
    }

    override fun removePotionColor(predicate: Predicate<Color>?): ItemBuilder {
        tag.removeIf<Int, Color>(NBT.TAG_CUSTOM_POTION_COLOR, {
            Color.fromRGB(it)
        }, predicate)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - PotionBase" defaultstate="collapsed">

    override var potionBase: PotionBase?
        get() = tag.getStringOrNull(NBT.TAG_POTION).letIfNotNull { PotionBase.valueOf(this) }
        set(value) {
            removePotionBase()
            if (value != null)
                tag.putString(NBT.TAG_POTION, value.value)
        }

    override fun getPotionBase(block: (ItemBuilder, PotionBase?) -> Unit): ItemBuilder {
        block(this, potionBase)
        return this
    }

    override fun setPotionBase(base: PotionBase?): ItemBuilder {
        this.potionBase = base
        return this
    }

    override fun setPotionBaseIf(base: PotionBase?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.potionBase = base
        return this
    }

    override fun removePotionBase(): ItemBuilder {
        removePotionBase(null)
        return this
    }

    override fun removePotionBase(predicate: Predicate<PotionBase>?): ItemBuilder {
        tag.removeIf<String, PotionBase>(NBT.TAG_POTION, {
            PotionBase.valueOf(it)
        }, predicate)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - PotionCustom" defaultstate="collapsed">

    override var potionCustoms: List<PotionEffectCustom>?
        get() {
            return tag.getListOrNull(NBT.TAG_CUSTOM_POTION_EFFECTS)
                ?.asElements<NBTTagCompound>()
                ?.map { effect ->
                    val type = PotionEffectType.fromId(effect.getByte(NBT.TAG_POTION_ID).toInt())
                    val amplifier = effect.getByte(NBT.TAG_POTION_AMPLIFIER).toInt()
                    val duration = effect.getInt(NBT.TAG_POTION_DURATION)
                    val ambient = effect.getBoolean(NBT.TAG_POTION_AMBIENT)
                    val particle = effect.getBoolean(NBT.TAG_POTION_SHOW_PARTICLES)
                    val icon = effect.getBooleanOrNull(NBT.TAG_POTION_SHOW_ICON) ?: false
                    PotionEffectCustom(type, amplifier, duration, ambient, particle, icon)
                }
        }
        set(value) {
            clearPotionCustoms()
            value?.forEach { addPotionCustom(it) }
        }

    override fun getPotionCustoms(block: (ItemBuilder, List<PotionEffectCustom>?) -> Unit): ItemBuilder {
        block(this, potionCustoms)
        return this
    }

    override fun getPotionCustom(type: PotionEffectType): PotionEffectCustom? {
        return potionCustoms?.firstOrNull { it.type == type }
    }

    override fun getPotionCustom(type: PotionEffectType, block: (ItemBuilder, PotionEffectCustom?) -> Unit): ItemBuilder {
        block(this, getPotionCustom(type))
        return this
    }

    override fun setPotionCustoms(customs: List<PotionEffectCustom>?): ItemBuilder {
        this.potionCustoms = customs
        return this
    }

    override fun setPotionCustoms(customs: List<PotionEffectCustom>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.potionCustoms = customs
        return this
    }

    override fun clearPotionCustoms(): ItemBuilder {
        tag.remove(NBT.TAG_CUSTOM_POTION_EFFECTS)
        return this
    }

    override fun addPotionCustom(effect: PotionEffectCustom): ItemBuilder {
        addPotionCustom(effect, false)
        return this
    }

    override fun addPotionCustom(effect: PotionEffectCustom, override: Boolean): ItemBuilder {
        return if (!override) {
            tag.getListOrDefault(NBT.TAG_CUSTOM_POTION_EFFECTS)
                .addCompound(effect.save(ofCompound()))
            this
        } else {
            val list = tag.getListOrNull(NBT.TAG_CUSTOM_POTION_EFFECTS)
            if (list == null) {
                addPotionCustom(effect, false)
                this
            } else {
                val index = list.indexOfFirst {
                    val type = PotionEffectType.fromId(it.asCompound().getByte(NBT.TAG_POTION_ID).toInt())
                    effect.type == type
                }
                if (index != -1)
                    list[index] = effect.save(ofCompound())
                this
            }
        }
    }

    override fun addPotionCustomIf(effect: PotionEffectCustom, override: Boolean, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            addPotionCustom(effect, override)
        return this
    }

    override fun removePotionCustom(type: PotionEffectType): ItemBuilder {
        removePotionCustom { it.type == type }
        return this
    }

    override fun removePotionCustom(predicate: Predicate<PotionEffectCustom>?): ItemBuilder {
        removePotionCustomIndexed { _, value -> predicate?.invoke(value).isTrue() }
        return this
    }

    override fun removePotionCustomIndexed(block: BiFunction<Int, PotionEffectCustom, Boolean>?): ItemBuilder {
        tag.getListOrNull(NBT.TAG_CUSTOM_POTION_EFFECTS)
            ?.removeIfIndexed<NBTTagCompound, PotionEffectCustom>({
                val type = PotionEffectType.fromId(it.getByte(NBT.TAG_POTION_ID).toInt())
                val amplifier = it.getByte(NBT.TAG_POTION_AMPLIFIER).toInt()
                val duration = it.getInt(NBT.TAG_POTION_DURATION)
                val ambient = it.getBoolean(NBT.TAG_POTION_AMBIENT)
                val particle = it.getBoolean(NBT.TAG_POTION_SHOW_PARTICLES)
                val icon = it.getBooleanOrNull(NBT.TAG_POTION_SHOW_ICON) ?: false
                PotionEffectCustom(type, amplifier, duration, ambient, particle, icon)
            }, block)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - FireworkStar" defaultstate="collapsed">

    override var fireworkStar: FireworkEffect?
        get() = tag.getCompoundOrNull(NBT.TAG_FIREWORKS_EXPLOSION).letIfNotNull { FireworkEffect.deserialize(this) }
        set(value) {
            removeFireworkStar()
            if (value != null)
                tag[NBT.TAG_FIREWORKS_EXPLOSION] = value.save(ofCompound())
        }

    override fun getFireworkStar(block: (ItemBuilder, FireworkEffect?) -> Unit): ItemBuilder {
        block(this, fireworkStar)
        return this
    }

    override fun setFireworkStar(effect: FireworkEffect?): ItemBuilder {
        this.fireworkStar = effect
        return this
    }

    override fun setFireworkStar(effect: FireworkEffect?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.fireworkStar = effect
        return this
    }

    override fun removeFireworkStar(): ItemBuilder {
        removeFireworkStar(null)
        return this
    }

    override fun removeFireworkStar(predicate: Predicate<FireworkEffect>?): ItemBuilder {
        tag.removeIf<NBTTagCompound, FireworkEffect>(NBT.TAG_FIREWORKS_EXPLOSION, {
            FireworkEffect.deserialize(it)
        }, predicate)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - FireworkRocket Effect" defaultstate="collapsed">

    override var fireworkRocketEffects: List<FireworkEffect>?
        get() {
            return tag.getCompoundOrNull(NBT.TAG_FIREWORKS)
                ?.getListOrNull(NBT.TAG_FIREWORKS_EXPLOSIONS)
                ?.asElements<NBTTagCompound>()
                ?.map { FireworkEffect.deserialize(it) }
        }
        set(value) {
            clearFireworkRocketEffects()
            value?.forEach { addFireworkRocketEffect(it) }
        }

    override fun getFireworkRocketEffects(block: (ItemBuilder, List<FireworkEffect>?) -> Unit): ItemBuilder {
        block(this, fireworkRocketEffects)
        return this
    }

    override fun getFireworkRocketEffect(type: FireworkType): FireworkEffect? {
        return fireworkRocketEffects?.find { it.type == type }
    }

    override fun getFireworkRocketEffect(type: FireworkType, block: (ItemBuilder, FireworkEffect?) -> Unit): ItemBuilder {
        block(this, getFireworkRocketEffect(type))
        return this
    }

    override fun setFireworkRocketEffects(effect: List<FireworkEffect>?): ItemBuilder {
        this.fireworkRocketEffects = effect
        return this
    }

    override fun setFireworkRocketEffectsIf(effect: List<FireworkEffect>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.fireworkRocketEffects = effect
        return this
    }

    override fun clearFireworkRocketEffects(): ItemBuilder {
        tag.getCompoundOrNull(NBT.TAG_FIREWORKS)
            ?.remove(NBT.TAG_FIREWORKS_EXPLOSIONS)
        return this
    }

    override fun addFireworkRocketEffect(effect: FireworkEffect): ItemBuilder {
        tag.getCompoundOrDefault(NBT.TAG_FIREWORKS)
            .getListOrDefault(NBT.TAG_FIREWORKS_EXPLOSIONS)
            .addCompound(effect.save(ofCompound()))
        return this
    }

    override fun addFireworkRocketEffectIf(effect: FireworkEffect, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            addFireworkRocketEffect(effect)
        return this
    }

    override fun removeFireworkRocketEffect(type: FireworkType): ItemBuilder {
        removeFireworkRocketEffect { it.type == type }
        return this
    }

    override fun removeFireworkRocketEffect(predicate: Predicate<FireworkEffect>?): ItemBuilder {
        removeFireworkRocketEffectIndexed { _, value -> predicate?.invoke(value).isTrue() }
        return this
    }

    override fun removeFireworkRocketEffectIndexed(block: BiFunction<Int, FireworkEffect, Boolean>?): ItemBuilder {
        tag.getCompoundOrNull(NBT.TAG_FIREWORKS)
            ?.getListOrNull(NBT.TAG_FIREWORKS_EXPLOSIONS)
            ?.removeIfIndexed<NBTTagCompound, FireworkEffect>({
                FireworkEffect.deserialize(it)
            }, block)
        return this
    }

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - FireworkRocket Flight" defaultstate="collapsed">

    override var fireworkRocketFlight: Int?
        get() = tag.getCompoundOrNull(NBT.TAG_FIREWORKS)?.getIntOrNull(NBT.TAG_FIREWORKS_FLIGHT)
        set(value) {
            removeFireworkRocketFlight()
            if (value != null)
                tag.getCompoundOrDefault(NBT.TAG_FIREWORKS).putInt(NBT.TAG_FIREWORKS_FLIGHT, value)
        }

    override fun getFireworkRocketFlight(block: (ItemBuilder, Int?) -> Unit): ItemBuilder {
        block(this, fireworkRocketFlight)
        return this
    }

    override fun setFireworkRocketFlight(flight: Int?): ItemBuilder {
        this.fireworkRocketFlight = flight
        return this
    }

    override fun setFireworkRocketFlightIf(flight: Int?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder {
        if (block().isTrue())
            this.fireworkRocketFlight = flight
        return this
    }

    override fun removeFireworkRocketFlight(): ItemBuilder {
        removeFireworkRocketFlight(null)
        return this
    }

    override fun removeFireworkRocketFlight(predicate: Predicate<Int>?): ItemBuilder {
        tag.getCompoundOrNull(NBT.TAG_FIREWORKS)
            ?.removeIf(NBT.TAG_FIREWORKS_FLIGHT, predicate)
        return this
    }

    //</editor-fold>
}
