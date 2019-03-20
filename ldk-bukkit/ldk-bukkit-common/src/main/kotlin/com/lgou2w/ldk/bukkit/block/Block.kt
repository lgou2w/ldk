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

package com.lgou2w.ldk.bukkit.block

import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.block.Block

/**************************************************************************
 *
 * org.bukkit.block.Block Extended
 *
 **************************************************************************/

/**
 * * Read NBT tag data from a given block entity.
 * * 从给定的方块实体读取 NBT 标签数据.
 *
 * @throws [IllegalArgumentException] If the block is not of type `TileEntity`.
 * @throws [IllegalArgumentException] 如果方块不是 `TileEntity` 类型.
 * @see [BlockFactory.readTag]
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
@Throws(IllegalArgumentException::class)
fun Block.readTag(block: Applicator<NBTTagCompound> = {}): NBTTagCompound
        = BlockFactory.readTag(this).also(block)

/**
 * * Modify the NBT tag data for a given block entity.
 * * 将给定的方块实体进行 NBT 标签数据的修改.
 *
 * @throws [IllegalArgumentException] If the block is not of type `TileEntity`.
 * @throws [IllegalArgumentException] 如果方块不是 `TileEntity` 类型.
 * @see [BlockFactory.modifyTag]
 * @since LDK 0.1.7-rc3
 */
@Throws(IllegalArgumentException::class)
fun Block.modifyTag(block: Applicator<NBTTagCompound>): Block
        = BlockFactory.modifyTag(this, block)
