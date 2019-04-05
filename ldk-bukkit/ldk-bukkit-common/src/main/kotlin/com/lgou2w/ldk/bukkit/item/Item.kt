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

package com.lgou2w.ldk.bukkit.item

import com.lgou2w.ldk.bukkit.compatibility.XMaterial
import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * * Get whether this item stack is `null` or air.
 * * 获取此物品栈是否为 `null` 或空气.
 */
fun ItemStack?.isAir(): Boolean
        = this == null || type == Material.AIR

/**
 * * Get this item stack is not `null` and air.
 * * 获取此物品栈是否不是 `null` 和空气.
 */
fun ItemStack?.isNotAir(): Boolean
        = this != null && type != Material.AIR

/**
 * * Create an Item Builder [ItemBuilder] object from the given item material.
 * * 从给定的物品材料创建一个物品构建者 [ItemBuilder] 对象.
 *
 * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
 * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
 * @see [ItemBuilder.of]
 */
@JvmOverloads
@Throws(UnsupportedOperationException::class)
inline fun Material.builder(count: Int = 1, durability: Int = 0, block: Applicator<ItemBuilder> = {}): ItemBuilder
        = ItemBuilder.of(this, count, durability).also(block)

/**
 * * Create an Item Builder [ItemBuilder] object from the given item stack.
 * * 从给定的物品栈创建一个物品构建者 [ItemBuilder] 对象.
 *
 * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
 * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
 * @see [ItemBuilder.of]
 */
@JvmOverloads
@Throws(UnsupportedOperationException::class)
inline fun ItemStack.builder(block: Applicator<ItemBuilder> = {}): ItemBuilder
        = ItemBuilder.of(this).also(block)

/**
 * * Read NBT tag data from a given item stack.
 * * 从给定的物品栈读取 NBT 标签数据.
 *
 * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
 * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
 * @see [ItemFactory.readTag]
 */
@JvmOverloads
@Throws(UnsupportedOperationException::class)
inline fun ItemStack.readTag(block: Applicator<NBTTagCompound?> = {}): NBTTagCompound?
        = ItemFactory.readTag(this).also(block)

/**
 * * Safely read NBT tag data from a given item stack.
 * * 从给定的物品栈安全读取 NBT 标签数据.
 *
 * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
 * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
 * @see [ItemFactory.readTagSafe]
 */
@JvmOverloads
@Throws(UnsupportedOperationException::class)
inline fun ItemStack.readTagSafe(block: Applicator<NBTTagCompound> = {}): NBTTagCompound
        = ItemFactory.readTagSafe(this).also(block)

/**
 * * Write the given NBT tag data to the given item stack [itemStack].
 * * 将给定的 NBT 标签数据写入到给定的物品栈 [itemStack] 中.
 *
 * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
 * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
 * @see [ItemFactory.writeTag]
 */
@Throws(UnsupportedOperationException::class)
fun NBTTagCompound?.writeTag(itemStack: ItemStack): ItemStack
        = ItemFactory.writeTag(itemStack, this)

/**
 * * Modify the NBT tag data for the given item stack.
 * * 将给定的物品栈进行 NBT 标签数据的修改.
 *
 * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
 * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
 * @see [ItemFactory.modifyTag]
 * @since LDK 0.1.7-rc3
 */
@Throws(UnsupportedOperationException::class)
fun ItemStack.modifyTag(block: Applicator<NBTTagCompound>): ItemStack
        = ItemFactory.modifyTag(this, block)

/**
 * * Create an Item Builder [ItemBuilder] object from the given item material.
 * * 从给定的物品材料创建一个物品构建者 [ItemBuilder] 对象.
 *
 * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
 * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
 * @see [XMaterial]
 * @see [ItemBuilder.of]
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
@Throws(UnsupportedOperationException::class)
fun XMaterial.builder(count: Int = 1, durability: Int = 0, block: Applicator<ItemBuilder> = {}): ItemBuilder
        = ItemBuilder.of(toBukkit(), count, durability).also(block)

/**
 * * Determine if the given item material matche [xMaterial].
 * * 判断给定的物品材料是否和 [xMaterial] 匹配.
 *
 * @since LDK 0.1.8-rc
 */
infix fun Material.eq(xMaterial: XMaterial): Boolean
        = this == xMaterial.toBukkit()

/**
 * * Determine if the given item material does not match [xMaterial].
 * * 判断给定的物品材料是否不和 [xMaterial] 匹配.
 *
 * @since LDK 0.1.8-rc
 */
infix fun Material.notEq(xMaterial: XMaterial): Boolean
        = this != xMaterial.toBukkit()
