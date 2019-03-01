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

import com.lgou2w.ldk.bukkit.compatibility.XMaterial
import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun ItemStack?.isAir(): Boolean
        = this == null || type == Material.AIR

fun ItemStack?.isNotAir(): Boolean
        = this != null && type != Material.AIR

/**
 * @throws [UnsupportedOperationException] If the item type is illegal, e.g. `WALL_BANNER`
 */
@JvmOverloads
@Throws(UnsupportedOperationException::class)
inline fun Material.builder(count: Int = 1, durability: Int = 0, block: Applicator<ItemBuilder> = {}): ItemBuilder
        = ItemBuilder.of(this, count, durability).also(block)

/**
 * @throws [UnsupportedOperationException] If the item type is illegal, e.g. `WALL_BANNER`
 */
@JvmOverloads
@Throws(UnsupportedOperationException::class)
inline fun ItemStack.builder(block: Applicator<ItemBuilder> = {}): ItemBuilder
        = ItemBuilder.of(this).also(block)

/**
 * @throws [UnsupportedOperationException] If the item type is illegal, e.g. `WALL_BANNER`
 */
@JvmOverloads
@Throws(UnsupportedOperationException::class)
inline fun ItemStack.readTag(block: Applicator<NBTTagCompound?> = {}): NBTTagCompound?
        = ItemFactory.readTag(this).also(block)

/**
 * @throws [UnsupportedOperationException] If the item type is illegal, e.g. `WALL_BANNER`
 */
@JvmOverloads
@Throws(UnsupportedOperationException::class)
inline fun ItemStack.readTagSafe(block: Applicator<NBTTagCompound> = {}): NBTTagCompound
        = ItemFactory.readTagSafe(this).also(block)

/**
 * @throws [UnsupportedOperationException] If the item type is illegal, e.g. `WALL_BANNER`
 */
@Throws(UnsupportedOperationException::class)
fun NBTTagCompound?.writeTag(itemStack: ItemStack): ItemStack
        = ItemFactory.writeTag(itemStack, this)

/**
 * @throws [UnsupportedOperationException] If the item type is illegal, e.g. `WALL_BANNER`
 * @since LDK 0.1.7-rc3
 */
@Throws(UnsupportedOperationException::class)
fun ItemStack.modifyTag(block: Applicator<NBTTagCompound>): ItemStack
        = ItemFactory.modifyTag(this, block)

/**
 * @throws [UnsupportedOperationException] If the item type is illegal, e.g. `WALL_BANNER`
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
@Throws(UnsupportedOperationException::class)
fun XMaterial.builder(count: Int = 1, durability: Int = 0, block: Applicator<ItemBuilder> = {}): ItemBuilder
        = ItemBuilder.of(toBukkit(), count, durability).also(block)
/**
 * @since LDK 0.1.8-rc
 */
infix fun Material.eq(xMaterial: XMaterial): Boolean
        = this == xMaterial.toBukkit()

/**
 * @since LDK 0.1.8-rc
 */
infix fun Material.notEq(xMaterial: XMaterial): Boolean
        = this != xMaterial.toBukkit()
