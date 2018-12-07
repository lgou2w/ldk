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

package com.lgou2w.ldk.bukkit.block

import com.lgou2w.ldk.bukkit.nbt.NBTFactory
import com.lgou2w.ldk.bukkit.packet.PacketFactory
import com.lgou2w.ldk.bukkit.reflect.lazyCraftBukkitClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClass
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.NBTType
import com.lgou2w.ldk.reflect.AccessorConstructor
import com.lgou2w.ldk.reflect.AccessorMethod
import com.lgou2w.ldk.reflect.FuzzyReflect
import org.bukkit.Bukkit
import org.bukkit.block.Block

object BlockFactory {

    @JvmStatic val CLASS_WORLD by lazyMinecraftClass("World")
    @JvmStatic val CLASS_TILE_ENTITY by lazyMinecraftClass("TileEntity")
    @JvmStatic val CLASS_BLOCK_POSITION by lazyMinecraftClass("BlockPosition")

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

    // NMS.World -> public NMS.TileEntity getTileEntity(NMS.BlockPosition)
    @JvmStatic val METHOD_WORLD_GET_TILE_ENTITY : AccessorMethod<Any, Any?> by lazy {
        FuzzyReflect.of(CLASS_WORLD)
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

    @JvmStatic
    fun getTileEntity(block: Block): Any? {
        val x = block.x
        val y = block.y
        val z = block.z
        val world = METHOD_CRAFT_WORLD_HANDLE.invoke(block.world)
        val position = CONSTRUCTOR_BLOCK_POSITION.newInstance(x, y, z)
        return METHOD_WORLD_GET_TILE_ENTITY.invoke(world, position)
    }

    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun readTag(block: Block): NBTTagCompound {
        val tileEntity = getTileEntity(block) ?: throw IllegalArgumentException("The block type is not a TileEntity object.")
        val tag = NBTFactory.createInternal(NBTType.TAG_COMPOUND)
        METHOD_TILE_ENTITY_READ.invoke(tileEntity, tag)
        return NBTFactory.fromNMS(tag) as NBTTagCompound
    }

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
}
