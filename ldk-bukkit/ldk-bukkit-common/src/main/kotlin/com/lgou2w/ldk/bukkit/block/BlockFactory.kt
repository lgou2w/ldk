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

package com.lgou2w.ldk.bukkit.block

import com.lgou2w.ldk.bukkit.nbt.NBTFactory
import com.lgou2w.ldk.bukkit.packet.PacketFactory
import com.lgou2w.ldk.bukkit.reflect.lazyCraftBukkitClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClass
import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.NBTType
import com.lgou2w.ldk.reflect.AccessorConstructor
import com.lgou2w.ldk.reflect.AccessorMethod
import com.lgou2w.ldk.reflect.FuzzyReflect
import org.bukkit.Bukkit
import org.bukkit.block.Block

/**
 * ## BlockFactory (方块工厂)
 *
 * @author lgou2w
 */
object BlockFactory {

    @JvmStatic val CLASS_WORLD by lazyMinecraftClass("World")
    @JvmStatic val CLASS_TILE_ENTITY by lazyMinecraftClass("TileEntity")
    @JvmStatic val CLASS_BLOCK_POSITION by lazyMinecraftClass("BlockPosition")
    @JvmStatic val CLASS_IBLOCK_ACCESS by lazyMinecraftClass("IBlockAccess")

    @JvmStatic val CLASS_CRAFT_WORLD by lazyCraftBukkitClass("CraftWorld")

    // NMS.BlockPosition -> public constructor(Int, Int, Int) // x, y, z
    @JvmStatic val CONSTRUCTOR_BLOCK_POSITION : AccessorConstructor<Any> by lazy {
        FuzzyReflect.of(CLASS_BLOCK_POSITION)
            .useConstructorMatcher()
            .withParams(Int::class.java, Int::class.java, Int::class.java)
            .resultAccessor()
    }

    // OBC.CraftWorld -> public NMS.WorldServer getHandle()
    @JvmStatic val METHOD_CRAFT_WORLD_HANDLE : AccessorMethod<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_CRAFT_WORLD)
            .useMethodMatcher()
            .withName("getHandle")
            .resultAccessor()
    }

    // Since Minecraft 1.14 Pre-Release 5
    // NMS.IBlockAccess -> public NMS.TileEntity getTileEntity(NMS.BlockPosition)
    @JvmStatic val METHOD_IBLOCK_ACCESS_GET_TILE_ENTITY : AccessorMethod<Any, Any?> by lazy {
        FuzzyReflect.of(CLASS_IBLOCK_ACCESS)
            .useMethodMatcher()
            .withName("getTileEntity")
            .withType(CLASS_TILE_ENTITY)
            .withParams(CLASS_BLOCK_POSITION)
            .resultAccessorAs<Any, Any?>()
    }

    // NMS.TileEntity -> public NMS.Packet getUpdatePacket()
    @JvmStatic val METHOD_TILE_ENTITY_GET_UPDATE_PACKET : AccessorMethod<Any, Any?> by lazy {
        FuzzyReflect.of(CLASS_TILE_ENTITY)
            .useMethodMatcher()
            .withName("getUpdatePacket")
            .resultAccessorAs<Any, Any?>()
    }

    // NMS.TileEntity -> public void load(NMS.NBTTagCompound)
    // 1.12 or after is 'load', before is 'a'.
    @JvmStatic val METHOD_TILE_ENTITY_WRITE : AccessorMethod<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_TILE_ENTITY)
            .useMethodMatcher()
            .withName("load|a")
            .withParams(NBTFactory.CLASS_NBT_TAG_COMPOUND)
            .resultAccessor()
    }

    // NMS.TileEntity -> public void save(NMS.NBTTagCompound)
    // 1.9 or after is 'save', before is 'b'.
    @JvmStatic val METHOD_TILE_ENTITY_READ : AccessorMethod<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_TILE_ENTITY)
            .useMethodMatcher()
            .withName("save|b")
            .withParams(NBTFactory.CLASS_NBT_TAG_COMPOUND)
            .resultAccessor()
    }

    /**
     * * Get the `TileEntity` object for the given [block]. If the block is not `TileEntity` then return `null`.
     * * 获取给定方块 [block] 的 `TileEntity` 对象. 如果方块不是 `TileEntity` 则返回 `null`.
     */
    @JvmStatic
    fun getTileEntity(block: Block): Any? {
        val x = block.x
        val y = block.y
        val z = block.z
        val world = METHOD_CRAFT_WORLD_HANDLE.invoke(block.world)
        val position = CONSTRUCTOR_BLOCK_POSITION.newInstance(x, y, z)
        return METHOD_IBLOCK_ACCESS_GET_TILE_ENTITY.invoke(world, position)
    }

    /**
     * * Read NBT tag data from a given block entity [block].
     * * 从给定的方块实体 [block] 读取 NBT 标签数据.
     *
     * @throws [IllegalArgumentException] If the block is not of type `TileEntity`.
     * @throws [IllegalArgumentException] 如果方块不是 `TileEntity` 类型.
     */
    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun readTag(block: Block): NBTTagCompound {
        val tileEntity = getTileEntity(block) ?: throw IllegalArgumentException("The block type is not a TileEntity object.")
        val tag = NBTFactory.createInternal(NBTType.TAG_COMPOUND)
        METHOD_TILE_ENTITY_READ.invoke(tileEntity, tag)
        return NBTFactory.fromNMS(tag) as NBTTagCompound
    }

    /**
     * * Write the given NBT tag data [tag] to the given block entity [block].
     * * 将给定的 NBT 标签数据 [tag] 写入到给定的方块实体 [block] 中.
     *
     * @throws [IllegalArgumentException] If the block is not of type `TileEntity`.
     * @throws [IllegalArgumentException] 如果方块不是 `TileEntity` 类型.
     */
    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun writeTag(block: Block, tag: NBTTagCompound) {
        val tileEntity = getTileEntity(block) ?: throw IllegalArgumentException("The block type is not a TileEntity object.")
        val internal = NBTFactory.toNMS(tag)
        METHOD_TILE_ENTITY_WRITE.invoke(tileEntity, internal)
        val packet = METHOD_TILE_ENTITY_GET_UPDATE_PACKET.invoke(tileEntity)
        if (packet != null) {
            val range = Bukkit.getServer().viewDistance * 32 + .0
            PacketFactory.sendPacketToNearby(packet, block.location, range)
        }
    }

    /**
     * * Modify the NBT tag data for the given block entity [block].
     * * 将给定的方块实体 [block] 进行 NBT 标签数据的修改.
     *
     * @throws [IllegalArgumentException] If the block is not of type `TileEntity`.
     * @throws [IllegalArgumentException] 如果方块不是 `TileEntity` 类型.
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun modifyTag(block: Block, applicator: Applicator<NBTTagCompound>): Block {
        val tag = readTag(block)
        applicator(tag)
        writeTag(block, tag)
        return block
    }
}
