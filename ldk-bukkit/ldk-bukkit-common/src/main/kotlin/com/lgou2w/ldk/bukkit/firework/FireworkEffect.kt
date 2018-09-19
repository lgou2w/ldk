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

package com.lgou2w.ldk.bukkit.firework

import com.lgou2w.ldk.common.Enums
import com.lgou2w.ldk.nbt.NBT
import com.lgou2w.ldk.nbt.NBTSavable
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.Color
import org.bukkit.configuration.serialization.ConfigurationSerializable
import java.util.*

data class FireworkEffect(
        val type: FireworkType,
        val canFlicker: Boolean,
        val hasTrail: Boolean,
        val colors: List<Color>,
        val fades: List<Color>
) : ConfigurationSerializable,
        NBTSavable {

    override fun save(root: NBTTagCompound): NBTTagCompound {
        root.putByte(NBT.TAG_FIREWORKS_TYPE, type.value)
        root.putBoolean(NBT.TAG_FIREWORKS_FLICKER, canFlicker)
        root.putBoolean(NBT.TAG_FIREWORKS_TRAIL, hasTrail)
        root.putIntArray(NBT.TAG_FIREWORKS_COLORS, colors.map { it.asRGB() }.toIntArray())
        root.putIntArray(NBT.TAG_FIREWORKS_FADE_COLORS, fades.map { it.asRGB() }.toIntArray())
        return root
    }

    override fun serialize(): MutableMap<String, Any> {
        val result = LinkedHashMap<String, Any>()
        result["type"] = type.value
        result["flicker"] = canFlicker
        result["trail"] = hasTrail
        result["colors"] = colors.map { it.asRGB() }
        result["fades"] = fades.map { it.asRGB() }
        return result
    }

    fun toBukkit() : org.bukkit.FireworkEffect {
        return org.bukkit.FireworkEffect.builder()
            .with(Enums.ofName(org.bukkit.FireworkEffect.Type::class.java, type.name))
            .flicker(canFlicker)
            .trail(hasTrail)
            .withColor(colors)
            .withFade(fades)
            .build()
    }

    companion object {

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun deserialize(args: Map<String, Any>) : FireworkEffect {
            val type = Enums.ofValuableNotNull(FireworkType::class.java, args["type"].toString().toInt())
            val flicker = args["flicker"]?.toString()?.toBoolean() ?: false
            val trail = args["trail"]?.toString()?.toBoolean() ?: false
            val colors = (args["colors"] as? List<Int>)?.map { Color.fromRGB(it) } ?: emptyList()
            val fadeColors = (args["fades"] as? List<Int>)?.map { Color.fromRGB(it) } ?: emptyList()
            return FireworkEffect(type, flicker, trail, colors, fadeColors)
        }

        @JvmStatic
        fun deserialize(root: NBTTagCompound) : FireworkEffect {
            val type = Enums.ofValuableNotNull(FireworkType::class.java, root.getInt(NBT.TAG_FIREWORKS_TYPE))
            val flicker = root.getBoolean(NBT.TAG_FIREWORKS_FLICKER)
            val trail = root.getBoolean(NBT.TAG_FIREWORKS_TRAIL)
            val colors = root.getList(NBT.TAG_FIREWORKS_COLORS).asElements<Int>().map { Color.fromRGB(it) }
            val fades = root.getList(NBT.TAG_FIREWORKS_FADE_COLORS).asElements<Int>().map { Color.fromRGB(it) }
            return FireworkEffect(type, flicker, trail, colors, fades)
        }

        @JvmStatic
        fun builder(type: FireworkType) : FireworkEffectBuilder
                = FireworkEffectBuilder(type)
    }
}
