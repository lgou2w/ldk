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

package com.lgou2w.ldk.bukkit.particle

import com.lgou2w.ldk.bukkit.item.ItemFactory
import com.lgou2w.ldk.bukkit.packet.PacketFactory
import com.lgou2w.ldk.bukkit.reflect.lazyCraftBukkitClassOrNull
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClassOrNull
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.common.Predicate
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

/**
 * ## ParticleFactory (粒子工厂)
 *
 * @see [Particle]
 * @author lgou2w
 */
object ParticleFactory {

    @JvmStatic private val CLASS_PACKET_OUT_PARTICLES by lazyMinecraftClass("PacketPlayOutWorldParticles")
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

    // NMS.Particles -> public static NMS.Particle<?> a(String type)
    @JvmStatic private val METHOD_GET_PARTICLE_FRESH : AccessorMethod<Any, Any>? by lazy {
        FuzzyReflect.of(CLASS_PARTICLES.notNull(), true)
            .useMethodMatcher()
            .withVisibilities(Visibility.STATIC)
            .withType(CLASS_PARTICLE.notNull())
            .withParams(String::class.java)
            .resultAccessorOrNull()
    }

    // Since Minecraft 1.14 Pre-Release 5
    // NMS.Particles -> public static final Particle XXX
    @JvmStatic private val PARTICLE_FIELD_ACCESSORS : MutableMap<Particle, AccessorField<Any, Any>?> = HashMap()
    @JvmStatic private val PARTICLE_FIELD : (Particle) -> AccessorField<Any, Any>? = { particle ->
        var accessor = PARTICLE_FIELD_ACCESSORS[particle]
        if (accessor == null) {
            accessor = FuzzyReflect.of(CLASS_PARTICLES.notNull(), true)
                .useFieldMatcher()
                .withType(CLASS_PARTICLE.notNull())
                .withVisibilities(Visibility.PUBLIC, Visibility.STATIC, Visibility.FINAL)
                .withName(particle.type.toUpperCase())
                .resultAccessorOrNull()
            PARTICLE_FIELD_ACCESSORS[particle] = accessor
        }
        accessor
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
     */

    /**
     * * Create a particle effect packet with the given [particle].
     * * 将给定的粒子效果 [particle] 以给定的参数创建粒子效果数据包.
     *
     * @param data [Material] | [ItemStack] | [Block] | [ParticleDust] | [Color] | `null`
     * @throws [IllegalArgumentException] If wrong particle data.
     * @since LDK 0.1.8-rc
     */
    @Throws(IllegalArgumentException::class)
    fun createPacket(
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
    ): Any {
        return if (MinecraftBukkitVersion.isV113OrLater) {
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
    ): Any {
        val nms = if (MinecraftBukkitVersion.isV114OrLater) {
            // Since Minecraft 1.14 Pre-Release 5
            PARTICLE_FIELD(particle)?.get(null).notNull("This particle '$particle' is not supported by the server version.")
        } else {
            METHOD_GET_PARTICLE_FRESH.notNull().invoke(null, particle.type)
        }
        var overrideOffsetX = offsetX
        val param = if (particle == Particle.ITEM) {
            val stack = when (data) {
                is ItemStack -> data
                is Material -> ItemStack(data)
                is ParticleData -> ItemStack(data.type)
                null -> ItemStack(Material.AIR) // nullable
                else-> throw IllegalArgumentException("Particle ITEM data can only be Material | ItemStack | ParticleData.")
            }
            CONSTRUCTOR_PARAM_ITEM.notNull().newInstance(nms, ItemFactory.asNMSCopy(stack))
        } else if (particle == Particle.BLOCK || particle == Particle.BLOCKDUST || particle == Particle.FALLING_DUST) {
            val block = when (data) {
                is Block -> FIELD_CRAFT_BLOCK_DATA_STATE.notNull()[data.blockData]
                is Material -> METHOD_GET_BLOCK_DATA.notNull().invoke(null, data, 0.toByte())
                is ParticleData -> METHOD_GET_BLOCK_DATA.notNull().invoke(null, data.type, data.data.toByte())
                null -> METHOD_GET_BLOCK_DATA.notNull().invoke(null, Material.AIR, 0.toByte()) // nullable
                else -> throw IllegalArgumentException("Particle BLOCK | BLOCKDUST | FALLING_DUST data can only be Material | Block | ParticleData.")
            }
            CONSTRUCTOR_PARAM_BLOCK.notNull().newInstance(nms, block)
        } else if (particle == Particle.DUST) {
            val (color, size) = when (data) {
                is ParticleDust -> data.color to data.size
                is Color -> data to 1f
                null -> Color.WHITE to 1f // nullable
                else -> throw IllegalArgumentException("Particle DUST data can only be ParticleDust | Color.")
            }
            CONSTRUCTOR_PARAM_REDSTONE.notNull().newInstance(
                    color.red / 255.0f,
                    color.green / 255.0f,
                    color.blue / 255.0f,
                    size)
        } else {
            nms
        }
        if (particle == Particle.NOTE && data is ParticleNote && data.value in 0..24) {
            overrideOffsetX = data.value / 24f
        }
        return CONSTRUCTOR_FRESH.notNull().newInstance(
                param,
                longDistance,
                x, y, z,
                overrideOffsetX,
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
    ): Any {
        val nms = METHOD_GET_PARTICLE_LEGACY.notNull().invoke(null, particle.value)
                  ?: throw IllegalArgumentException("This particle '$particle' is not supported by the server version.")
        var overrideOffsetX = offsetX
        var overrideOffsetY = offsetY
        var overrideOffsetZ = offsetZ
        val packetData = if (particle == Particle.ITEM) {
            val (type, value) = when (data) {
                is ItemStack -> data.type to data.data.notNull().data.toInt()
                is Material -> data to 0
                is ParticleData -> data.type to data.data
                null -> Material.AIR to 0 // nullable
                else-> throw IllegalArgumentException("Particle ITEM data can only be Material | ItemStack | ParticleData.")
            }
            intArrayOf(type.id, value)
        } else if (particle == Particle.BLOCK || particle == Particle.BLOCKDUST || particle == Particle.FALLING_DUST) {
            val (type, value) = when (data) {
                is Block -> data.type to data.data.toInt()
                is Material -> data to 0
                is ParticleData -> data.type to data.data
                null -> Material.AIR to 0 // nullable
                else -> throw IllegalArgumentException("Particle BLOCK | BLOCKDUST | FALLING_DUST data can only be Material | Block | ParticleData.")
            }
            intArrayOf(type.id or (value shl 12))
        } else {
            intArrayOf()
        }
        if (particle == Particle.DUST && (data is ParticleDust || data is Color || data == null)) {
            val color = (data as? ParticleDust)?.color ?: data as? Color ?: Color.WHITE
            overrideOffsetX = color.red / 255.0f
            overrideOffsetY = color.green / 255.0f
            overrideOffsetZ = color.blue / 255.0f
        }
        if (particle == Particle.NOTE && data is ParticleNote && data.value in 0..24) {
            overrideOffsetX = data.value / 24f
        }
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

    /**
     * @param data [Particle.ITEM] => [Material] | [ItemStack] | [ParticleData] | `null`
     * @param data [Particle.BLOCK] | [Particle.BLOCKDUST] | [Particle.FALLING_DUST] => [Material] | [Block] | [ParticleData] | `null`
     * @param data [Particle.DUST] => [Color] | [ParticleDust] | `null`
     * @param data [Particle.NOTE] => [ParticleNote] | `null`
     */
    @JvmStatic
    @JvmOverloads
    fun sendParticle(
            particle: Particle,
            center: Location,
            range: Double = 32.0,
            offsetX: Float = 0f,
            offsetY: Float = 0f,
            offsetZ: Float = 0f,
            speed: Float = 0.0f,
            count: Int = 1,
            data: Any? = null
    ) {
        val packet = createPacket(particle, center.x.toFloat(), center.y.toFloat(), center.z.toFloat(), offsetX, offsetY, offsetZ, speed, count, range > 256.0, data)
        PacketFactory.sendPacketToNearby(packet, center, range)
    }

    /**
     * @param data [Particle.ITEM] => [Material] | [ItemStack] | [ParticleData] | `null`
     * @param data [Particle.BLOCK] | [Particle.BLOCKDUST] | [Particle.FALLING_DUST] => [Material] | [Block] | [ParticleData] | `null`
     * @param data [Particle.DUST] => [Color] | [ParticleDust] | `null`
     * @param data [Particle.NOTE] => [ParticleNote] | `null`
     */
    @JvmStatic
    @JvmOverloads
    fun sendParticle(
            sender: Player?,
            particle: Particle,
            center: Location,
            offsetX: Float = 0f,
            offsetY: Float = 0f,
            offsetZ: Float = 0f,
            speed: Float = 0.0f,
            count: Int = 1,
            data: Any? = null
    ) {
        val world = center.world.notNull()
        sendParticle(world.players, { sender == null || sender.canSee(it) }, particle, center, offsetX, offsetY, offsetZ, speed, count, data)
    }

    /**
     * @param data [Particle.ITEM] => [Material] | [ItemStack] | [ParticleData] | `null`
     * @param data [Particle.BLOCK] | [Particle.BLOCKDUST] | [Particle.FALLING_DUST] => [Material] | [Block] | [ParticleData] | `null`
     * @param data [Particle.DUST] => [Color] | [ParticleDust] | `null`
     * @param data [Particle.NOTE] => [ParticleNote] | `null`
     */
    @JvmStatic
    @JvmOverloads
    fun sendParticle(
            players: List<Player>,
            particle: Particle,
            center: Location,
            offsetX: Float = 0f,
            offsetY: Float = 0f,
            offsetZ: Float = 0f,
            speed: Float = 0.0f,
            count: Int = 1,
            data: Any? = null
    ) {
        sendParticle(players, { it.world == center.world }, particle, center, offsetX, offsetY, offsetZ, speed, count, data)
    }

    @JvmStatic
    private fun sendParticle(
            players: List<Player>,
            filter: Predicate<Player>,
            particle: Particle,
            center: Location,
            offsetX: Float,
            offsetY: Float,
            offsetZ: Float,
            speed: Float,
            count: Int,
            data: Any?
    ) {
        var longDistance = false
        val results = players
            .asSequence()
            .filter {
                val result = filter(it)
                if (result && !longDistance && it.location.distanceSquared(center) >= 256.0)
                    longDistance = true
                result
            }
            .toList()
            .toTypedArray()
        val packet = createPacket(particle, center.x.toFloat(), center.y.toFloat(), center.z.toFloat(), offsetX, offsetY, offsetZ, speed, count, longDistance, data)
        PacketFactory.sendPacketTo(packet, *results)
    }
}
