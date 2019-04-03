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

package com.lgou2w.ldk.bukkit.firework

import com.lgou2w.ldk.common.Enums
import com.lgou2w.ldk.common.notNull
import com.lgou2w.ldk.nbt.NBT
import com.lgou2w.ldk.nbt.NBTSavable
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.Color
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.ConfigurationSerialization
import java.util.LinkedHashMap

/**
 * ## FireworkEffect (烟花效果)
 *
 * @see [FireworkEffectBuilder]
 * @author lgou2w
 */
data class FireworkEffect(
        /**
         * * The type of this firework effect.
         * * 此烟花效果的类型.
         *
         * @see [FireworkType]
         */
        val type: FireworkType,
        /**
         * * Indicates whether this firework effect can be flicker.
         * * 表示此烟花效果是否可以闪烁.
         */
        val canFlicker: Boolean,
        /**
         * * Indicates whether this firework effect has a trail.
         * * 表示此烟花效果是否有尾迹.
         */
        val hasTrail: Boolean,
        /**
         * * A list of colors for this firework effect.
         * * 此烟花效果的颜色列表.
         */
        val colors: List<Color>,
        /**
         * * A list of fade colors for this firework effect.
         * * 此烟花效果的淡化颜色列表.
         */
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

    /**
     * * Convert this firework effect to Bukkit firework effect object.
     * * 将此烟花效果转换为 Bukkit 的烟花效果对象.
     *
     * @see [org.bukkit.FireworkEffect]
     */
    fun toBukkit(): org.bukkit.FireworkEffect {
        val type = Enums.ofName(org.bukkit.FireworkEffect.Type::class.java, type.name).notNull()
        return org.bukkit.FireworkEffect.builder()
            .with(type)
            .flicker(canFlicker)
            .trail(hasTrail)
            .withColor(colors)
            .withFade(fades)
            .build()
    }

    companion object {

        init {
            ConfigurationSerialization.registerClass(FireworkEffect::class.java)
        }

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun deserialize(args: Map<String, Any>): FireworkEffect {
            val type = Enums.ofValuableNotNull(FireworkType::class.java, args["type"].toString().toInt())
            val flicker = args["flicker"]?.toString()?.toBoolean() ?: false
            val trail = args["trail"]?.toString()?.toBoolean() ?: false
            val colors = (args["colors"] as? List<Int>)?.map { Color.fromRGB(it) } ?: emptyList()
            val fadeColors = (args["fades"] as? List<Int>)?.map { Color.fromRGB(it) } ?: emptyList()
            return FireworkEffect(type, flicker, trail, colors, fadeColors)
        }

        @JvmStatic
        fun deserialize(root: NBTTagCompound): FireworkEffect {
            val type = Enums.ofValuableNotNull(FireworkType::class.java, root.getInt(NBT.TAG_FIREWORKS_TYPE))
            val flicker = root.getBoolean(NBT.TAG_FIREWORKS_FLICKER)
            val trail = root.getBoolean(NBT.TAG_FIREWORKS_TRAIL)
            val colors = root.getList(NBT.TAG_FIREWORKS_COLORS).asElements<Int>().map { Color.fromRGB(it) }
            val fades = root.getList(NBT.TAG_FIREWORKS_FADE_COLORS).asElements<Int>().map { Color.fromRGB(it) }
            return FireworkEffect(type, flicker, trail, colors, fades)
        }

        /**
         * * Create a firework effect builder from a given firework type.
         * * 从给定的烟花类型创建一个烟花效果构建者.
         *
         * @see [FireworkEffectBuilder]
         */
        @JvmStatic
        fun builder(type: FireworkType): FireworkEffectBuilder
                = FireworkEffectBuilder(type)
    }
}
