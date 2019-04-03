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

import com.lgou2w.ldk.common.Valuable

/**
 * ## PotionType (药水类型)
 *
 * @see [Valuable]
 * @see [PotionEffectType]
 * @author lgou2w
 */
enum class PotionType(
        /**
         * * The type name of this potion.
         * * 此药水的类型名称.
         */
        val type: String,
        /**
         * * This potion corresponds to the potion effect.
         * * 此药水对应的药水效果.
         *
         * @see [PotionEffectType]
         */
        val effect: PotionEffectType?,
        /**
         * * Indicates whether this potion is upgradeable.
         * * 表示此药水是否是可升级型的.
         */
        val canUpgradable: Boolean,
        /**
         * * Indicates whether this potion is extendable.
         * * 表示此药水是否是可延长型的.
         */
        val canExtendable: Boolean
) : Valuable<String> {

    /**
     * * Potion type: Uncraftable Potion
     * * 药水类型: 不可合成的药水
     */
    UNCRAFTABLE("empty", null, false, false),
    /**
     * * Potion type: Water Bottle
     * * 药水类型: 水瓶
     */
    WATER("water", null, false, false),
    /**
     * * Potion type: Mundane Potion
     * * 药水类型: 平凡的药水
     */
    MUNDANE("mundane", null, false, false),
    /**
     * * Potion type: Thick Potion
     * * 药水类型: 浑浊的药水
     */
    THICK("thick", null, false, false),
    /**
     * * Potion type: Awkward Potion
     * * 药水类型: 粗制的药水
     */
    AWKWARD("awkward", null, false, false),
    /**
     * * Potion type: Night Vision
     * * 药水类型: 夜视药水
     */
    NIGHT_VISION("night_vision", PotionEffectType.NIGHT_VISION, false, true),
    /**
     * * Potion type: Invisibility
     * * 药水类型: 隐身药水
     */
    INVISIBILITY("invisibility", PotionEffectType.INVISIBILITY, false, true),
    /**
     * * Potion type: Leaping
     * * 药水类型: 跳跃药水
     */
    LEAPING("leaping", PotionEffectType.JUMP_BOOST, true, true),
    /**
     * * Potion type: Fire Resistance
     * * 药水类型: 抗火药水
     */
    FIRE_RESISTANCE("fire_resistance", PotionEffectType.FIRE_RESISTANCE, false, true),
    /**
     * * Potion type: Swiftness
     * * 药水类型: 迅捷药水
     */
    SWIFTNESS("swiftness", PotionEffectType.SPEED, true, true),
    /**
     * * Potion type:
     * * 药水类型:
     */
    SLOWNESS("slowness", PotionEffectType.SLOWNESS, false, true),
    /**
     * * Potion type: Water Breathing
     * * 药水类型: 水下呼吸药水
     */
    WATER_BREATHING("water_breathing", PotionEffectType.WATER_BREATHING, false, true),
    /**
     * * Potion type: Healing
     * * 药水类型: 治疗药水
     */
    HEALING("healing", PotionEffectType.INSTANT_HEALTH, true, false),
    /**
     * * Potion type: Harming
     * * 药水类型: 伤害药水
     */
    HARMING("harming", PotionEffectType.INSTANT_DAMAGE, true, false),
    /**
     * * Potion type: Poison
     * * 药水类型: 中毒药水
     */
    POISON("poison", PotionEffectType.POISON, true, true),
    /**
     * * Potion type: Regeneration
     * * 药水类型: 再生药水
     */
    REGENERATION("regeneration", PotionEffectType.REGENERATION, true, true),
    /**
     * * Potion type: Strength
     * * 药水类型: 力量药水
     */
    STRENGTH("strength", PotionEffectType.STRENGTH, true, true),
    /**
     * * Potion type: Weakness
     * * 药水类型: 虚弱药水
     */
    WEAKNESS("weakness", PotionEffectType.WEAKNESS, false, true),
    /**
     * * Potion type: Luck
     * * 药水类型: 幸运药水
     */
    LUCK("luck", PotionEffectType.LUCK, false, false),
    /**
     * * Potion type: The Turtle Master
     * * 药水类型: 神龟药水
     */
    TURTLE_MASTER("turtle_master", PotionEffectType.SLOWNESS, true, true), // Slowness IV and Resistance III
    /**
     * * Potion type: Slow Falling
     * * 药水类型: 缓降药水
     */
    SLOW_FALLING("slow_falling", PotionEffectType.SLOW_FALLING, false, true),
    ;

    override val value : String
        get() = type

    /**
     * @see [PotionEffectType.isInstant]
     */
    fun isInstant(): Boolean
            = effect != null && effect.isInstant()
}
