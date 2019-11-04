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

package com.lgou2w.ldk.nbt

import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.common.BiFunction
import com.lgou2w.ldk.common.Function
import com.lgou2w.ldk.common.Predicate
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
  val name : String

  /**
   * * The type of this NBT tag.
   * * 此 NBT 标签的类型.
   *
   * @see [NBTType]
   */
  val type : NBTType

  /**
   * * The type id of this NBT tag.
   * * 此 NBT 标签的类型 Id.
   */
  val typeId : Int

  /**
   * * The value of this NBT tag.
   * * 此 NBT 标签的值.
   */
  var value : T

  /**
   * * Reads the NBT tag from the given data [input].
   * * 从给定的 [input] 数据输入读取 NBT 标签.
   *
   * @param input Input stream
   * @param input 输入流
   * @throws [IOException] I/O
   */
  @Throws(IOException::class)
  fun read(input: DataInput)

  /**
   * * Write the given data [output] to this NBT tag.
   * * 将给定的 [output] 数据输出写入此 NBT 标签.
   *
   * @param output Output stream
   * @param output 输出流
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

  /**
   * * Convert this NBT tag to a `JSON` format string with a Minecraft specific type value suffix and color.
   * * 将此 NBT 标签转换为携有 Minecraft 特定类型值后缀和具有颜色的 `JSON` 格式字符串.
   *
   * > Note: that only color characters are used.
   *
   * @see [NBTTagList.toMojangsonWithColor]
   * @see [NBTTagCompound.toMojangsonWithColor]
   * @see [NBTType.mojangsonSuffix]
   */
  fun toMojangsonWithColor(): String

  companion object Constants {

    // const tag key
    // Do not advocate doing this
    // SEE ISSUES: https://github.com/lgou2w/ldk/issues/5

    //<editor-fold desc="ItemStack - TAG" defaultstate="collapsed">

    const val TAG = "tag"
    const val TAG_ID = "id"
    const val TAG_COUNT = "Count"
    const val TAG_DAMAGE = "Damage"
    const val TAG_ENCH_LEGACY = "ench"  // since 18w21a remove and change to -> Enchantments
    const val TAG_ENCH_FRESHLY = "Enchantments"
    const val TAG_ENCH_ID = "id"
    const val TAG_ENCH_LVL = "lvl"
    const val TAG_DISPLAY = "display"
    const val TAG_DISPLAY_LORE = "Lore"
    const val TAG_DISPLAY_NAME = "Name"
    const val TAG_DISPLAY_LOC_NAME = "LocName"  // since 18w01a remove and change to -> Name
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
    const val TAG_SKULL_OWNER_ID = "Id"
    const val TAG_SKULL_OWNER_NAME = "Name"
    const val TAG_SKULL_OWNER_PROPERTIES = "Properties"
    const val TAG_SKULL_OWNER_TEXTURES = "textures"
    const val TAG_SKULL_OWNER_TEXTURES_VALUE = "Value"
    const val TAG_POTION = "Potion"
    const val TAG_POTION_ID = "Id"
    const val TAG_POTION_AMPLIFIER = "Amplifier"
    const val TAG_POTION_DURATION = "Duration"
    const val TAG_POTION_AMBIENT = "Ambient"
    const val TAG_POTION_SHOW_PARTICLES = "ShowParticles"
    const val TAG_POTION_SHOW_ICON = "ShowIcon" // since Minecraft 1.13
    const val TAG_BLOCK_ENTITY_TAG = "BlockEntityTag"
    const val TAG_BANNER_CUSTOM_NAME = "CustomName"
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

    /// Since LDK 0.1.8-rc
    const val TAG_CUSTOM_MODEL_DATA = "CustomModelData" // since Minecraft 1.14
    const val TAG_CROSSBOW_CHARGED = "Charged" // since Minecraft 1.14
    const val TAG_CROSSBOW_CHARGED_PROJECTILES = "ChargedProjectiles" // since Minecraft 1.14
    ///

    /// Since LDK 0.1.9
    const val TAG_SUSPICIOUS_STEW_EFFECTS = "Effects" // since Minecraft 1.14
    const val TAG_SUSPICIOUS_STEW_EFFECT_ID = "EffectId" // since Minecraft 1.14
    const val TAG_SUSPICIOUS_STEW_EFFECT_DURATION = "EffectDuration" // since Minecraft 1.14
    ///

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
inline fun ofList(
  name: String = "",
  block: Applicator<NBTTagList> = {}
): NBTTagList {
  val list = NBTTagList(name)
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
  block: Applicator<NBTTagCompound> = {}
): NBTTagCompound {
  val compound = NBTTagCompound(name)
  compound.block()
  return compound
}

fun <T> NBTTagCompound.removeIf(key: String, predicate: Predicate<T>?)
  = removeIf<T, T>(key, { it }, predicate)

fun <T, R> NBTTagCompound.removeIf(
  key: String,
  transform: Function<T, R>,
  predicate: Predicate<R>?
): NBTTagCompound {
  if (predicate == null) {
    remove(key)
  } else {
    val value = get(key) ?: return this
    //@Suppress("UNCHECKED_CAST") // SEE : https://github.com/lgou2w/ldk/issues/82
    if (value.type.isWrapper() && predicate(transform(value as T)))
      remove(key)
    else if (!value.type.isWrapper() && predicate(transform(value.value as T)))
      remove(key)
  }
  return this
}

fun <T> NBTTagList.removeIf(predicate: Predicate<T>?)
  = if (predicate == null) removeIfIndexed<T, T>({ it }, null)
else removeIfIndexed { _, value: T -> predicate(value) }

fun <T, R> NBTTagList.removeIf(transform: Function<T, R>, predicate: Predicate<R>?): NBTTagList
  = if (predicate == null) removeIfIndexed(transform, null)
else removeIfIndexed { _, value: T -> predicate(transform(value)) }

fun <T> NBTTagList.removeIfIndexed(block: BiFunction<Int, T, Boolean>?)
  = removeIfIndexed<T, T>({ it }, block)

fun <T, R> NBTTagList.removeIfIndexed(
  transform: Function<T, R>,
  block: BiFunction<Int, R, Boolean>?
): NBTTagList {
  if (block == null) {
    clear()
  } else {
    val iterator = iterator()
    var index = 0
    while (iterator.hasNext()) {
      val next = iterator.next()
      val value = if (next.type.isWrapper()) next as T else next.value as T
      if (block(index++, transform(value)))
        iterator.remove()
    }
  }
  return this
}
