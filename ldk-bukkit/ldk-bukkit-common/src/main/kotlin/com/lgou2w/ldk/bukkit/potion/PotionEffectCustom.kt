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

package com.lgou2w.ldk.bukkit.potion

import com.lgou2w.ldk.common.ComparisonChain
import com.lgou2w.ldk.nbt.NBT
import com.lgou2w.ldk.nbt.NBTSavable
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.configuration.serialization.ConfigurationSerializable

data class PotionEffectCustom @JvmOverloads constructor(
        val type: PotionEffectType,
        val amplifier: Int,
        val duration: Int,
        val ambient: Boolean = true,
        val particle: Boolean = true,
        val icon: Boolean = true
) : ConfigurationSerializable,
        NBTSavable,
        Comparable<PotionEffectCustom> {

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
            val icon = args["color"]?.toString()?.toBoolean() ?: false
            return PotionEffectCustom(type, amplifier, duration, ambient, particle, icon)
        }
    }
}
