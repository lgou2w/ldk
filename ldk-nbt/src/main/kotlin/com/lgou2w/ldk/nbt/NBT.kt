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

interface NBT<T> : Cloneable {

    val name: String

    val type: NBTType

    var value: T

    @Throws(IOException::class)
    fun read(input: DataInput)

    @Throws(IOException::class)
    fun write(output: DataOutput)

    /**
     * * See the json format of Gson.
     *
     * @see [NBTTagList.toJson]
     * @see [NBTTagCompound.toJson]
     */
    fun toJson(): String

    /**
     * * See the nbt format of Minecraft.
     *
     * @see [NBTTagList.toMojangson]
     * @see [NBTTagCompound.toMojangson]
     * @see [NBTType.mojangsonSuffix]
     */
    fun toMojangson(): String

    companion object Constants {

        // const tag key
        // TODO Do not advocate doing this

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

@JvmOverloads
inline fun <E: NBTBase<*>> ofList(
        name: String = "",
        block: NBTTagList<E>.() -> Unit = {}
) : NBTTagList<E> {
    val list = NBTTagList<E>(name)
    list.block()
    return list
}

@JvmOverloads
inline fun ofCompound(
        name: String = "",
        block: NBTTagCompound.() -> Unit = {}
) : NBTTagCompound {
    val compound = NBTTagCompound(name)
    compound.block()
    return compound
}

@JvmOverloads
inline fun <reified E> Collection<E>.ofList(
        name: String = "",
        block: NBTTagList<NBTBase<E>>.() -> Unit = {}
) : NBTTagList<NBTBase<E>> {
    val list = NBTTagList<NBTBase<E>>(name)
    if (isNotEmpty()) {
        val elementType = NBTType.fromClass(E::class.java)
        if (elementType == null)
            throw IllegalArgumentException("不支持的集合元素类型: ${E::class.java}.")
        @Suppress("UNCHECKED_CAST")
        forEach {
            val tag = NBTType.createTag(elementType)
            (tag as NBTBase<E>).value = it
            list.add(tag)
        }
    }
    list.block()
    return list
}
