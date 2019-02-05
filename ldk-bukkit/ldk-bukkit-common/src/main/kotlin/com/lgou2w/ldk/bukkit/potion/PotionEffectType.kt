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

package com.lgou2w.ldk.bukkit.potion

import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.bukkit.version.MinecraftVersion
import com.lgou2w.ldk.common.Valuable
import com.lgou2w.ldk.common.isOrLater
import java.util.*

enum class PotionEffectType(
        val id: Int,
        val legacy: String,
        val type: String,
        var mcVer: MinecraftVersion? = null
) : Valuable<String> {

    SPEED(1, "speed", "speed"),
    SLOWNESS(2, "slow", "slowness"),
    HASTE(3, "fast_digging", "haste"),
    MINING_FATIGUE(4, "slow_digging", "mining_fatigue"),
    STRENGTH(5, "increase_damage", "strength"),
    INSTANT_HEALTH(6, "heal", "instant_health"),
    INSTANT_DAMAGE(7, "harm", "instant_damage"),
    JUMP_BOOST(8, "jump", "jump_boost"),
    NAUSEA(9, "confusion", "nausea"),
    REGENERATION(10, "regeneration", "regeneration"),
    RESISTANCE(11, "damage_resistance", "resistance"),
    FIRE_RESISTANCE(12, "fire_resistance", "fire_resistance"),
    WATER_BREATHING(13, "water_breathing", "water_breathing"),
    INVISIBILITY(14, "invisibility", "invisibility"),
    BLINDNESS(15, "blindness", "blindness"),
    NIGHT_VISION(16, "night_vision", "night_vision"),
    HUNGER(17, "hunger", "hunger"),
    WEAKNESS(18, "weakness", "weakness"),
    POISON(19, "poison", "poison"),
    WITHER(20, "wither", "wither"),
    HEALTH_BOOST(21, "health_boost", "health_boost"),
    ABSORPTION(22, "absorption", "absorption"),
    SATURATION(23, "saturation", "saturation"),
    GLOWING(24, "glowing", "glowing", MinecraftVersion.V1_9),
    LEVITATION(25, "levitation", "levitation", MinecraftVersion.V1_9),
    LUCK(26, "luck", "luck", MinecraftVersion.V1_9),
    UNLUCK(27, "unluck", "unluck", MinecraftVersion.V1_9),
    SLOW_FALLING(28, "slow_falling", "slow_falling", MinecraftVersion.V1_13),
    CONDUIT_POWER(29, "conduit_power", "conduit_power", MinecraftVersion.V1_13),
    DOLPHINS_GRACE(30, "dolphins_grace", "dolphins_grace", MinecraftVersion.V1_13),
    ;

    override val value: String
        get() = type

    fun isInstant() : Boolean
            = this == INSTANT_HEALTH ||
              this == INSTANT_DAMAGE ||
              this == SATURATION

    fun toBukkit() : org.bukkit.potion.PotionEffectType {
        return org.bukkit.potion.PotionEffectType.getByName(
                if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
                    type
                else
                    legacy
        )
    }

    companion object {

        @JvmStatic private val ID_MAP : MutableMap<Int, PotionEffectType> = HashMap()
        @JvmStatic private val NAME_MAP : MutableMap<String, PotionEffectType> = HashMap()

        init {
            values().forEach {
                ID_MAP[it.id] = it
                NAME_MAP[it.legacy] = it
                NAME_MAP[it.type] = it
            }
        }

        @JvmStatic
        fun fromBukkit(effect: org.bukkit.potion.PotionEffectType) : PotionEffectType
                = fromName(effect.name)

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromName(name: String) : PotionEffectType
                = NAME_MAP[name.toLowerCase(Locale.US)]
                  ?: throw IllegalArgumentException("Invalid effect type name: $name.")

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromId(id: Int) : PotionEffectType
                = ID_MAP[id] ?: throw IllegalArgumentException("Invalid effect ID $id value.")
    }
}
