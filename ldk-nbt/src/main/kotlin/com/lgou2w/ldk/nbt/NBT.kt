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

package com.lgou2w.ldk.nbt

import java.io.DataInput
import java.io.DataOutput
import java.io.IOException

/**
 * ## NBT
 *
 * @see [NBTBase]
 * @author lgou2w
 */
interface NBT<T> : Cloneable {

    /**
     * * The name of this NBT tag.
     * * 此 NBT 标签的名称.
     */
    val name: String

    /**
     * The type of this NBT tag.
     * * 此 NBT 标签的类型.
     *
     * @see [NBTType]
     */
    val type: NBTType

    /**
     * * The value of this NBT tag.
     * * 此 NBT 标签的值.
     */
    var value: T

    /**
     * * Reads the NBT tag from the given [input] data input.
     * * 从给定的 [input] 数据输入读取 NBT 标签.
     *
     * @throws [IOException] I/O
     */
    @Throws(IOException::class)
    fun read(input: DataInput)

    /**
     * * Write the given [output] data output to this NBT tag.
     * * 将给定的 [output] 数据输出写入此 NBT 标签.
     *
     * @throws [IOException] I/O
     */
    @Throws(IOException::class)
    fun write(output: DataOutput)

    /**
     * * Convert this NBT tag to a string in the `JSON` format.
     * * 将此 NBT 标签转换为 `JSON` 格式的字符串.
     *
     * @see [NBTTagList.toJson]
     * @see [NBTTagCompound.toJson]
     */
    fun toJson(): String

    /**
     * * Convert this NBT tag to a `JSON` format string with a Minecraft specific type value suffix.
     * * 将此 NBT 标签转换为携有 Minecraft 特定类型值后缀的 `JSON` 格式字符串.
     *
     * @see [NBTTagList.toMojangson]
     * @see [NBTTagCompound.toMojangson]
     * @see [NBTType.mojangsonSuffix]
     */
    fun toMojangson(): String

    companion object Constants {

        // const tag key
        // TODO Do not advocate doing this
        // SEE ISSUES: https://github.com/lgou2w/ldk/issues/5

        //<editor-fold desc="TAG" defaultstate="collapsed">

        const val TAG = "tag"
        const val TAG_ID = "id"
        const val TAG_COUNT = "Count"
        const val TAG_DAMAGE = "Damage"
        const val TAG_ENCH_LEGACY = "ench"
        const val TAG_ENCH_FRESHLY = "Enchantments"
        const val TAG_ENCH_ID = "id"
        const val TAG_ENCH_LVL = "lvl"
        const val TAG_DISPLAY = "display"
        const val TAG_DISPLAY_LORE = "Lore"
        const val TAG_DISPLAY_NAME = "Name"
        const val TAG_DISPLAY_LOC_NAME = "LocName"
        const val TAG_ENTITY_TAG = "EntityTag"
        const val TAG_ENTITY_TAG_ID = "id"
        const val TAG_HIDE_FLAGS = "HideFlags"
        const val TAG_REPAIR_COST = "RepairCost"
        const val TAG_CAN_DESTROY = "CanDestroy"
        const val TAG_CAN_PLACE_ON = "CanPlaceOn"
        const val TAG_UNBREAKABLE = "Unbreakable"
        const val TAG_ATTRIBUTE_SLOT = "Slot"
        const val TAG_ATTRIBUTE_NAME = "Name"
        const val TAG_ATTRIBUTE_AMOUNT = "Amount"
        const val TAG_ATTRIBUTE_TYPE = "AttributeName"
        const val TAG_ATTRIBUTE_OPERATION = "Operation"
        const val TAG_ATTRIBUTE_UUID_MOST = "UUIDMost"
        const val TAG_ATTRIBUTE_UUID_LEAST= "UUIDLeast"
        const val TAG_ATTRIBUTE_MODIFIERS = "AttributeModifiers"
        const val TAG_STORED_ENCHANTMENTS = "StoredEnchantments"
        const val TAG_CUSTOM_POTION_EFFECTS = "CustomPotionEffects"
        const val TAG_CUSTOM_POTION_COLOR = "CustomPotionColor"
        const val TAG_BOOK_TITLE = "title"
        const val TAG_BOOK_PAGES = "pages"
        const val TAG_BOOK_AUTHOR = "author"
        const val TAG_BOOK_GENERATION = "generation"
        const val TAG_LEATHER_ARMOR_COLOR = "color"
        const val TAG_MAP_COLOR = "MapColor"
        const val TAG_MAP_SCALING = "map_is_scaling"
        const val TAG_SKULL_OWNER = "SkullOwner"
        const val TAG_SKULL_PROPERTIES = "Properties"
        const val TAG_SKULL_TEXTURES = "textures"
        const val TAG_SKULL_TEXTURES_ID = "Id"
        const val TAG_SKULL_TEXTURES_VALUE = "Value"
        const val TAG_POTION = "Potion"
        const val TAG_POTION_ID = "Id"
        const val TAG_POTION_AMPLIFIER = "Amplifier"
        const val TAG_POTION_DURATION = "Duration"
        const val TAG_POTION_AMBIENT = "Ambient"
        const val TAG_POTION_SHOW_PARTICLES = "ShowParticles"
        const val TAG_BLOCK_ENTITY_TAG = "BlockEntityTag"
        const val TAG_BANNER_PATTERNS = "Patterns"
        const val TAG_BANNER_COLOR = "Color"
        const val TAG_BANNER_PATTERN = "Pattern"
        const val TAG_FIREWORKS = "Fireworks"
        const val TAG_FIREWORKS_FLIGHT = "Flight"
        const val TAG_FIREWORKS_EXPLOSION = "Explosion"
        const val TAG_FIREWORKS_EXPLOSIONS = "Explosions"
        const val TAG_FIREWORKS_FLICKER = "Flicker"
        const val TAG_FIREWORKS_TRAIL = "Trail"
        const val TAG_FIREWORKS_TYPE = "Type"
        const val TAG_FIREWORKS_COLORS = "Colors"
        const val TAG_FIREWORKS_FADE_COLORS = "FadeColors"
        const val TAG_KNOWLEDGE_BOOK_RECIPES = "Recipes"

        //</editor-fold>
    }
}

/**
 * * Create an `NBTList` object that specifies the NBT type.
 * * 创建一个指定 NBT 类型的 `NBTList` 对象.
 *
 * @see [NBTTagList]
 * @param name Name
 * @param name 名称
 * @param block Initializer
 * @param block 初始化
 */
@JvmOverloads
inline fun <E: NBTBase<*>> ofList(
        name: String = "",
        block: NBTTagList<E>.() -> Unit = {}
) : NBTTagList<E> {
    val list = NBTTagList<E>(name)
    list.block()
    return list
}

/**
 * * Create an NBT object of `NBTTagCompound`.
 * * 创建一个 `NBTTagCompound` 的 NBT 对象.
 *
 * @see [NBTTagCompound]
 * @param name Name
 * @param name 名称
 * @param block Initializer
 * @param block 初始化
 */
@JvmOverloads
inline fun ofCompound(
        name: String = "",
        block: NBTTagCompound.() -> Unit = {}
) : NBTTagCompound {
    val compound = NBTTagCompound(name)
    compound.block()
    return compound
}
