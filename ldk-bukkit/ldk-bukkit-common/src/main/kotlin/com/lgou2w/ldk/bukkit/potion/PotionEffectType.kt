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

import com.lgou2w.ldk.bukkit.version.Draft
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.bukkit.version.MinecraftVersion
import com.lgou2w.ldk.common.Valuable
import com.lgou2w.ldk.common.notNull
import java.util.HashMap
import java.util.Locale

/**
 * ## PotionEffectType (药水效果类型)
 *
 * * Enumerate all potion effects in Minecraft.
 * * 枚举 Minecraft 中所有的药水效果.
 *
 * @author lgou2w
 */
enum class PotionEffectType(
        /**
         * * This type of Id value.
         * * 此类型的 Id 值.
         */
        val id: Int,
        /**
         * * The legacy type name of this type.
         * * 此类型的旧类型名.
         */
        val legacy: String,
        /**
         * * The type name of this type.
         * * 此类型的旧类型名.
         */
        val type: String,
        /**
         * * Minecraft version of this type with minimum requirement.
         * * 此类型最低需求的 Minecraft 版本.
         */
        var mcVer: MinecraftVersion? = null
) : Valuable<String> {

    /**
     * * Potion effect type: Speed
     * * 药水效果类型: 速度
     */
    SPEED(1, "speed", "speed"),
    /**
     * * Potion effect type: Slowness
     * * 药水效果类型: 缓慢
     */
    SLOWNESS(2, "slow", "slowness"),
    /**
     * * Potion effect type: Haste
     * * 药水效果类型: 急迫
     */
    HASTE(3, "fast_digging", "haste"),
    /**
     * * Potion effect type: Mining Fatigue
     * * 药水效果类型: 挖掘疲劳
     */
    MINING_FATIGUE(4, "slow_digging", "mining_fatigue"),
    /**
     * * Potion effect type: Strength
     * * 药水效果类型: 力量
     */
    STRENGTH(5, "increase_damage", "strength"),
    /**
     * * Potion effect type: Instant Health
     * * 药水效果类型: 瞬间治疗
     */
    INSTANT_HEALTH(6, "heal", "instant_health"),
    /**
     * * Potion effect type: Instant Damage
     * * 药水效果类型: 瞬间伤害
     */
    INSTANT_DAMAGE(7, "harm", "instant_damage"),
    /**
     * * Potion effect type: Jump Boost
     * * 药水效果类型: 跳跃提升
     */
    JUMP_BOOST(8, "jump", "jump_boost"),
    /**
     * * Potion effect type: Nausea
     * * 药水效果类型: 反胃
     */
    NAUSEA(9, "confusion", "nausea"),
    /**
     * * Potion effect type: Regeneration
     * * 药水效果类型: 生命恢复
     */
    REGENERATION(10, "regeneration", "regeneration"),
    /**
     * * Potion effect type: Resistance
     * * 药水效果类型: 抗性提升
     */
    RESISTANCE(11, "damage_resistance", "resistance"),
    /**
     * * Potion effect type: Fire Resistance
     * * 药水效果类型: 防火
     */
    FIRE_RESISTANCE(12, "fire_resistance", "fire_resistance"),
    /**
     * * Potion effect type: Water Breathing
     * * 药水效果类型: 水下呼吸
     */
    WATER_BREATHING(13, "water_breathing", "water_breathing"),
    /**
     * * Potion effect type: Invisibility
     * * 药水效果类型: 隐身
     */
    INVISIBILITY(14, "invisibility", "invisibility"),
    /**
     * * Potion effect type: Blindness
     * * 药水效果类型: 失明
     */
    BLINDNESS(15, "blindness", "blindness"),
    /**
     * * Potion effect type: Night Vision
     * * 药水效果类型: 夜视
     */
    NIGHT_VISION(16, "night_vision", "night_vision"),
    /**
     * * Potion effect type: Hunger
     * * 药水效果类型: 饥饿
     */
    HUNGER(17, "hunger", "hunger"),
    /**
     * * Potion effect type: Weakness
     * * 药水效果类型: 虚弱
     */
    WEAKNESS(18, "weakness", "weakness"),
    /**
     * * Potion effect type: Poison
     * * 药水效果类型: 中毒
     */
    POISON(19, "poison", "poison"),
    /**
     * * Potion effect type: Wither
     * * 药水效果类型: 凋零
     */
    WITHER(20, "wither", "wither"),
    /**
     * * Potion effect type: Health Boost
     * * 药水效果类型: 生命提升
     */
    HEALTH_BOOST(21, "health_boost", "health_boost"),
    /**
     * * Potion effect type: Absorption
     * * 药水效果类型: 伤害吸收
     */
    ABSORPTION(22, "absorption", "absorption"),
    /**
     * * Potion effect type: Saturation
     * * 药水效果类型: 饱和
     */
    SATURATION(23, "saturation", "saturation"),
    /**
     * * Potion effect type: Glowing
     * * 药水效果类型: 发光
     *
     * @since [MinecraftVersion.V1_9]
     */
    GLOWING(24, "glowing", "glowing", MinecraftVersion.V1_9),
    /**
     * * Potion effect type: Levitation
     * * 药水效果类型: 飘浮
     */
    LEVITATION(25, "levitation", "levitation", MinecraftVersion.V1_9),
    /**
     * * Potion effect type: Luck
     * * 药水效果类型: 幸运
     *
     * @since [MinecraftVersion.V1_9]
     */
    LUCK(26, "luck", "luck", MinecraftVersion.V1_9),
    /**
     * * Potion effect type: Unluck
     * * 药水效果类型: 霉运
     *
     * @since [MinecraftVersion.V1_9]
     */
    UNLUCK(27, "unluck", "unluck", MinecraftVersion.V1_9),
    /**
     * * Potion effect type: Slow Falling
     * * 药水效果类型: 缓降
     *
     * @since [MinecraftVersion.V1_13]
     */
    SLOW_FALLING(28, "slow_falling", "slow_falling", MinecraftVersion.V1_13),
    /**
     * * Potion effect type: Conduit Power
     * * 药水效果类型: 潮涌能量
     *
     * @since [MinecraftVersion.V1_13]
     */
    CONDUIT_POWER(29, "conduit_power", "conduit_power", MinecraftVersion.V1_13),
    /**
     * * Potion effect type: Dolphins Grace
     * * 药水效果类型: 海豚的恩惠
     *
     * @since [MinecraftVersion.V1_13]
     */
    DOLPHINS_GRACE(30, "dolphins_grace", "dolphins_grace", MinecraftVersion.V1_13),

    @Draft @Deprecated("Draft")
    BAD_OMEN(31, "bad_omen", "bad_omen", MinecraftVersion.V1_14),

    @Draft @Deprecated("Draft")
    HERO_OF_THE_VILLAGE(32, "hero_of_the_village", "hero_of_the_village", MinecraftVersion.V1_14), // since 19w13a
    ;

    override val value : String
        get() = type

    /**
     * * Indicate whether this potion effect is instantaneous.
     * * 表示此药水效果是否是即时的.
     */
    fun isInstant(): Boolean
            = this == INSTANT_HEALTH ||
              this == INSTANT_DAMAGE ||
              this == SATURATION

    /**
     * * Convert this potion effect type to Bukkit potion effect type.
     * * 将此药水效果类型转换为 Bukkit 的药水效果类型.
     *
     * @see [org.bukkit.potion.PotionEffectType]
     */
    fun toBukkit() : org.bukkit.potion.PotionEffectType {
        return org.bukkit.potion.PotionEffectType.getByName(
                if (MinecraftBukkitVersion.isV113OrLater)
                    type
                else
                    legacy
        ).notNull()
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

        /**
         * * Get this potion effect type from the given Bukkit potion [effect] type.
         * * 从给定的 Bukkit 药水效果类型 [effect] 获取此药水效果类型.
         */
        @JvmStatic
        fun fromBukkit(effect: org.bukkit.potion.PotionEffectType): PotionEffectType
                = fromName(effect.name)

        /**
         * * Get this potion effect type from the given potion effect type [name].
         * * 从给定的药水效果类型名 [name] 获取此药水效果类型.
         *
         * @throws [IllegalArgumentException] If the invalid potion effect type name.
         * @throws [IllegalArgumentException] 如果无效的药水效果类型名.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromName(name: String): PotionEffectType
                = NAME_MAP[name.toLowerCase(Locale.US)]
                  ?: throw IllegalArgumentException("Invalid effect type name: $name.")

        /**
         * * Get the potion effect type from the given potion effect [id].
         * * 从给定的药水效果 [id] 获取药水效果类型.
         *
         * @throws [IllegalArgumentException] If the invalid potion effect [id].
         * @throws [IllegalArgumentException] 如果无效的药水效果 [id].
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromId(id: Int): PotionEffectType
                = ID_MAP[id] ?: throw IllegalArgumentException("Invalid effect ID $id value.")
    }
}
