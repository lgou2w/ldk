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

package com.lgou2w.ldk.bukkit.potion

import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.common.ComparisonChain
import com.lgou2w.ldk.nbt.NBT
import com.lgou2w.ldk.nbt.NBTSavable
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect

/**
 * ## PotionEffectCustom (自定义药水效果)
 *
 * @see [PotionEffectType]
 * @author lgou2w
 */
data class PotionEffectCustom @JvmOverloads constructor(
        /**
         * * The type of potion effect of this custom potion.
         * * 此自定义药水的药水效果类型.
         */
        val type: PotionEffectType,
        /**
         * * The potion amplifier of this custom potion.
         * * 此自定义药水的药水强度.
         */
        val amplifier: Int,
        /**
         * * The potion duration of this custom potion.
         * * 此自定义药水的药水时间.
         */
        val duration: Int,
        /**
         * * Makes potion effect produce more, translucent, particles.
         * * 此自定义药水是否产生更多的半透明粒子.
         */
        val ambient: Boolean = true,
        /**
         * * Whether this custom potion have a particle.
         * * 此自定义药水是否有粒子效果.
         */
        val particle: Boolean = true,
        /**
         * * Whether this custom potion have a icon.
         * * 此自定义药水是否有图标.
         */
        val icon: Boolean = true
) : ConfigurationSerializable,
        NBTSavable,
        Comparable<PotionEffectCustom> {

    /**
     * * Apply this custom potion effect to the given [entity].
     * * 将此自定义药水效果应用到给定的实体 [entity] 中.
     *
     * @since LDK 0.1.7-rc3
     */
    @JvmOverloads
    fun applyToEntity(entity: LivingEntity, force: Boolean = false): Boolean {
        val effect = if (MinecraftBukkitVersion.isV113OrLater)
            PotionEffect(type.toBukkit(), duration, amplifier, ambient, particle, icon)
        else
            PotionEffect(type.toBukkit(), duration, amplifier, ambient, particle)
        return entity.addPotionEffect(effect, force)
    }

    override fun save(root: NBTTagCompound): NBTTagCompound {
        root.putByte(NBT.TAG_POTION_ID, type.id)
        root.putByte(NBT.TAG_POTION_AMPLIFIER, amplifier)
        root.putInt(NBT.TAG_POTION_DURATION, duration)
        root.putBoolean(NBT.TAG_POTION_AMBIENT, ambient)
        root.putBoolean(NBT.TAG_POTION_SHOW_PARTICLES, particle)
        root.putBoolean(NBT.TAG_POTION_SHOW_ICON, icon)
        return root
    }

    override fun compareTo(other: PotionEffectCustom): Int {
        return ComparisonChain.start()
            .compare(type, other.type)
            .compare(amplifier, other.amplifier)
            .compare(duration, other.duration)
            .compare(ambient, other.ambient)
            .compare(particle, other.particle)
            .compare(icon, other.icon)
            .result
    }

    override fun serialize(): MutableMap<String, Any> {
        val result = LinkedHashMap<String, Any>()
        result["type"] = type.type
        result["amplifier"] = amplifier
        result["duration"] = duration
        result["ambient"] = ambient
        result["particle"] = particle
        result["icon"] = icon
        return result
    }

    companion object {

        /**
         * @see [ConfigurationSerializable]
         */
        @JvmStatic
        fun deserialize(args: Map<String, Any>): PotionEffectCustom {
            val type = PotionEffectType.fromName(args["type"].toString())
            val amplifier = args["amplifier"]?.toString()?.toInt() ?: 0
            val duration = args["duration"]?.toString()?.toInt() ?: 0
            val ambient = args["ambient"]?.toString()?.toBoolean() ?: false
            val particle = args["particle"]?.toString()?.toBoolean() ?: false
            val icon = args["icon"]?.toString()?.toBoolean() ?: false
            return PotionEffectCustom(type, amplifier, duration, ambient, particle, icon)
        }
    }
}
