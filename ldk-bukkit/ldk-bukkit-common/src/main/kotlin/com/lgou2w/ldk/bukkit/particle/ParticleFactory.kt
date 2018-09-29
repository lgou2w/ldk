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

package com.lgou2w.ldk.bukkit.particle

import com.lgou2w.ldk.bukkit.item.ItemFactory
import com.lgou2w.ldk.bukkit.packet.PacketFactory
import com.lgou2w.ldk.bukkit.reflect.lazyCraftBukkitClassOrNull
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClassOrNull
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.common.isOrLater
import com.lgou2w.ldk.common.notNull
import com.lgou2w.ldk.reflect.AccessorConstructor
import com.lgou2w.ldk.reflect.AccessorField
import com.lgou2w.ldk.reflect.AccessorMethod
import com.lgou2w.ldk.reflect.FuzzyReflect
import com.lgou2w.ldk.reflect.Visibility
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object ParticleFactory {

    @JvmStatic val CLASS_PACKET_OUT_PARTICLES by lazyMinecraftClass("PacketPlayOutWorldParticles")

    @JvmStatic private val CLASS_ENUM_PARTICLE by lazyMinecraftClassOrNull("EnumParticle", "PacketPlayOutWorldParticles\$EnumParticle")
    @JvmStatic private val CLASS_PARTICLE by lazyMinecraftClassOrNull("Particle")
    @JvmStatic private val CLASS_PARTICLES by lazyMinecraftClassOrNull("Particles")
    @JvmStatic private val CLASS_PARTICLE_PARAM by lazyMinecraftClassOrNull("ParticleParam")
    @JvmStatic private val CLASS_PARTICLE_PARAM_ITEM by lazyMinecraftClassOrNull("ParticleParamItem")
    @JvmStatic private val CLASS_PARTICLE_PARAM_BLOCK by lazyMinecraftClassOrNull("ParticleParamBlock")
    @JvmStatic private val CLASS_PARTICLE_PARAM_REDSTONE by lazyMinecraftClassOrNull("ParticleParamRedstone")

    @JvmStatic private val CLASS_IBLOCK_DATA by lazyMinecraftClassOrNull("IBlockData")
    @JvmStatic private val CLASS_CRAFT_BLOCK_DATA by lazyCraftBukkitClassOrNull("block.data.CraftBlockData")

    // After 1.13 => public constructor(NMS.ParticleParam, boolean, float, float, float, float, float, float, float, int)
    @JvmStatic private val CONSTRUCTOR_FRESH : AccessorConstructor<Any>? by lazy {
        FuzzyReflect.of(CLASS_PACKET_OUT_PARTICLES)
            .useConstructorMatcher()
            .withParams(
                    CLASS_PARTICLE_PARAM.notNull(), // Param
                    Boolean::class.java, // longDistance
                    Float::class.java, // x
                    Float::class.java, // y
                    Float::class.java, // z
                    Float::class.java, // offsetX
                    Float::class.java, // offsetY
                    Float::class.java, // offsetZ
                    Float::class.java, // speed
                    Int::class.java) // count
            .resultAccessorOrNull()
    }

    // NMS.ParticleParamItem -> public constructor(NMS.Particle, NMS.ItemStack)
    @JvmStatic private val CONSTRUCTOR_PARAM_ITEM : AccessorConstructor<Any>? by lazy {
        FuzzyReflect.of(CLASS_PARTICLE_PARAM_ITEM.notNull())
            .useConstructorMatcher()
            .withParams(CLASS_PARTICLE.notNull(), ItemFactory.CLASS_ITEMSTACK)
            .resultAccessorOrNull()
    }

    // NMS.ParticleParamBlock -> public constructor(NMS.Particle, NMS.IBlockData)
    @JvmStatic private val CONSTRUCTOR_PARAM_BLOCK : AccessorConstructor<Any>? by lazy {
        FuzzyReflect.of(CLASS_PARTICLE_PARAM_BLOCK.notNull())
            .useConstructorMatcher()
            .withParams(CLASS_PARTICLE.notNull(), CLASS_IBLOCK_DATA.notNull())
            .resultAccessorOrNull()
    }

    // NMS.ParticleParamRedstone -> public constructor(float, float, float, float)
    @JvmStatic private val CONSTRUCTOR_PARAM_REDSTONE : AccessorConstructor<Any>? by lazy {
        FuzzyReflect.of(CLASS_PARTICLE_PARAM_REDSTONE.notNull())
            .useConstructorMatcher()
            .withParams(Float::class.java, Float::class.java, Float::class.java, Float::class.java)
            .resultAccessorOrNull()
    }

    // Before 1.13 => public constructor(NMS.EnumParticle, boolean, float, float, float, float, float, float, float, int, int[])
    @JvmStatic private val CONSTRUCTOR_LEGACY : AccessorConstructor<Any>? by lazy {
        FuzzyReflect.of(CLASS_PACKET_OUT_PARTICLES)
            .useConstructorMatcher()
            .withParams(
                    CLASS_ENUM_PARTICLE.notNull("Joke? 1.13 Didn't have this class before?"), // Particle
                    Boolean::class.java, // longDistance
                    Float::class.java, // x
                    Float::class.java, // y
                    Float::class.java, // z
                    Float::class.java, // offsetX
                    Float::class.java, // offsetY
                    Float::class.java, // offsetZ
                    Float::class.java, // speed
                    Int::class.java, // count
                    IntArray::class.java) // data
            .resultAccessorOrNull()
    }

    @JvmStatic private val METHOD_GET_PARTICLE_FRESH : AccessorMethod<Any, Any>? by lazy {
        FuzzyReflect.of(CLASS_PARTICLES.notNull(), true)
            .useMethodMatcher()
            .withVisibilities(Visibility.STATIC)
            .withType(CLASS_PARTICLE.notNull())
            .withParams(String::class.java)
            .resultAccessorOrNull()
    }

    // NMS.EnumParticle -> public static NMS.EnumParticle getById(int)
    @JvmStatic private val METHOD_GET_PARTICLE_LEGACY : AccessorMethod<Any, Any>? by lazy {
        FuzzyReflect.of(CLASS_ENUM_PARTICLE.notNull())
            .useMethodMatcher()
            .withVisibilities(Visibility.PUBLIC, Visibility.STATIC)
            .withType(CLASS_ENUM_PARTICLE.notNull())
            .withParams(Int::class.java)
            .resultAccessorOrNull()
    }

    // OBC.CraftMagicNumbers -> public static NMS.IBlockData getBlock(Material, byte)
    @JvmStatic private val METHOD_GET_BLOCK_DATA : AccessorMethod<Any, Any>? by lazy {
        FuzzyReflect.of(ItemFactory.CLASS_CRAFT_MAGIC_NUMBERS)
            .useMethodMatcher()
            .withVisibilities(Visibility.PUBLIC, Visibility.STATIC)
            .withType(CLASS_IBLOCK_DATA.notNull())
            .withParams(Material::class.java, Byte::class.java)
            .resultAccessorOrNull()
    }

    // OBC.CraftBlockData -> private NMS.IBlockData state
    @JvmStatic private val FIELD_CRAFT_BLOCK_DATA_STATE : AccessorField<Any, Any>? by lazy {
        FuzzyReflect.of(CLASS_CRAFT_BLOCK_DATA.notNull(), true)
            .useFieldMatcher()
            .withType(CLASS_IBLOCK_DATA.notNull())
            .resultAccessorOrNull()
    }

    /**
     * After 1.13 => ParticleParam
     * Before 1.13 => Array<Int>
     *     * ITEM_CRACK => Material Id , Data
     *     * BLOCK_CRACK | BLOCK_DUST | FALLING_DUST
     *          * => Block Id ^ Data << 12
     *     * RED_DUST => RED / 255F = offsetX, GREEN / 255F = offsetY, BLUE / 255F = offsetZ
     *          * if (RED == 0) offsetX = Float.MIN_NORMAL
     */

    /**
     * @param data [Material] | [ItemStack] | [Block] | [ParticleDust] | [Color]
     */
    @Throws(IllegalArgumentException::class)
    private fun createPacket(
            particle: Particle,
            x: Float,
            y: Float,
            z: Float,
            offsetX: Float,
            offsetY: Float,
            offsetZ: Float,
            speed: Float,
            count: Int,
            longDistance: Boolean,
            data: Any? = null
    ) : Any {
        return if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
            createPacketFresh(particle, x, y, z, offsetX, offsetY, offsetZ, speed, count, longDistance, data)
        } else {
            createPacketLegacy(particle, x, y, z, offsetX, offsetY, offsetZ, speed, count, longDistance, data)
        }
    }

    @Suppress("DEPRECATION")
    private fun createPacketFresh(
            particle: Particle,
            x: Float,
            y: Float,
            z: Float,
            offsetX: Float,
            offsetY: Float,
            offsetZ: Float,
            speed: Float,
            count: Int,
            longDistance: Boolean,
            data: Any?
    ) : Any {
        val nms = METHOD_GET_PARTICLE_FRESH.notNull().invoke(null, particle.type)
        val param = if (particle == Particle.ITEM) {
            val stack = data as? ItemStack ?: if (data is Material) ItemStack(data) else
                throw IllegalArgumentException("Particle ITEM data type can only be Material or ItemStack.")
            CONSTRUCTOR_PARAM_ITEM.notNull().newInstance(nms, ItemFactory.asNMSCopy(stack))
        } else if (particle == Particle.BLOCK || particle == Particle.BLOCKDUST || particle == Particle.FALLING_DUST) {
            val block = when (data) {
                is Block -> FIELD_CRAFT_BLOCK_DATA_STATE.notNull()[data.blockData]
                is Material -> METHOD_GET_BLOCK_DATA.notNull().invoke(null, data, 0.toByte())
                else -> throw IllegalArgumentException("Particle BLOCK or BLOCKDUST or FALLING_DUST data type can only be Material or Block.")
            }
            CONSTRUCTOR_PARAM_BLOCK.notNull().newInstance(nms, block)
        } else if (particle == Particle.DUST) {
            val dust = data as? ParticleDust ?: if (data is Color) ParticleDust(data, 0.01f) else
                throw IllegalArgumentException("Particle DUST data type can only be ParticleDust or Color.")
            CONSTRUCTOR_PARAM_REDSTONE.notNull().newInstance(
                    dust.color.red / 255.0f,
                    dust.color.green / 255.0f,
                    dust.color.blue / 255.0f,
                    dust.size)
        } else {
            nms
        }
        return CONSTRUCTOR_FRESH.notNull().newInstance(
                param,
                longDistance,
                x, y, z,
                offsetX,
                offsetY,
                offsetZ,
                speed,
                count)
    }

    @Suppress("DEPRECATION")
    private fun createPacketLegacy(
            particle: Particle,
            x: Float,
            y: Float,
            z: Float,
            offsetX: Float,
            offsetY: Float,
            offsetZ: Float,
            speed: Float,
            count: Int,
            longDistance: Boolean,
            data: Any?
    ) : Any {
        var overrideOffsetX = offsetX
        var overrideOffsetY = offsetY
        var overrideOffsetZ = offsetZ
        val packetData = if (particle == Particle.ITEM) {
            val type = (data as? ItemStack)?.type ?: data as? Material
                       ?: throw IllegalArgumentException("Particle ITEM data type can only be Material or ItemStack.")
            val value = (data as? ItemStack)?.data?.data?.toInt() ?: 0
            intArrayOf(type.id, value)
        } else if (particle == Particle.BLOCK || particle == Particle.BLOCKDUST || particle == Particle.FALLING_DUST) {
            val type = (data as? Block)?.type ?: data as? Material
                       ?: throw IllegalArgumentException("Particle BLOCK or BLOCKDUST or FALLING_DUST data type can only be Material or Block.")
            if (!type.isBlock)
                throw IllegalArgumentException("The material type '$type' is not a block.")
            val value = (data as? Block)?.data?.toInt() ?: 0
            intArrayOf(type.id or (value shl 12))
        } else {
            intArrayOf()
        }
        if (particle == Particle.DUST && (data is ParticleDust || data is Color)) {
            val color = (data as? ParticleDust)?.color ?: data as Color
            overrideOffsetX = color.red / 255.0f
            overrideOffsetY = color.green / 255.0f
            overrideOffsetZ = color.blue / 255.0f
            if (color.red == 0)
                overrideOffsetX = Float.MIN_VALUE
        }
        val nms = METHOD_GET_PARTICLE_LEGACY.notNull().invoke(null, particle.value)
        return CONSTRUCTOR_LEGACY.notNull().newInstance(
                nms,
                longDistance,
                x, y, z,
                overrideOffsetX,
                overrideOffsetY,
                overrideOffsetZ,
                speed,
                count,
                packetData)
    }

    @JvmStatic
    fun sendParticle(
            player: Player?,
            particle: Particle,
            center: Location,
            offsetX: Float,
            offsetY: Float,
            offsetZ: Float,
            speed: Float,
            count: Int,
            longDistance: Boolean,
            data: Any?
    ) {
        val packet = createPacket(particle, center.x.toFloat(), center.y.toFloat(), center.z.toFloat(), offsetX, offsetY, offsetZ, speed, count, longDistance, data)
        PacketFactory.sendToNearby(player, false, center, packet)
    }
}
