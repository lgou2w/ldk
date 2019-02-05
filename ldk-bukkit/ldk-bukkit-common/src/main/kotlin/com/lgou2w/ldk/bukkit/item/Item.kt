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

@JvmOverloads
inline fun Material.builder(count: Int = 1, durability: Int = 0, block: Applicator<ItemBuilder> = {}): ItemBuilder
        = ItemBuilder.of(this, count, durability).also(block)

@JvmOverloads
inline fun ItemStack.builder(block: Applicator<ItemBuilder> = {}): ItemBuilder
        = ItemBuilder.of(this).also(block)

@JvmOverloads
inline fun ItemStack.readTag(block: Applicator<NBTTagCompound?> = {}): NBTTagCompound?
        = ItemFactory.readTag(this).also(block)

@JvmOverloads
inline fun ItemStack.readTagSafe(block: Applicator<NBTTagCompound> = {}): NBTTagCompound
        = ItemFactory.readTagSafe(this).also(block)

fun NBTTagCompound?.writeTag(itemStack: ItemStack): ItemStack
        = ItemFactory.writeTag(itemStack, this)

/**
 * @since LDK 0.1.7-rc3
 */
fun ItemStack.modifyTag(block: Applicator<NBTTagCompound>): ItemStack
        = ItemFactory.modifyTag(this, block)

/**
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
@Throws(UnsupportedOperationException::class)
fun XMaterial.builder(count: Int = 1, durability: Int = 0, block: Applicator<ItemBuilder> = {}): ItemBuilder
        = ItemBuilder.of(toBukkit(), count, durability).also(block)
