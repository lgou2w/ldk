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

import com.lgou2w.ldk.common.Valuable

enum class PotionType(
        val type: String,
        val effect: PotionEffectType?,
        val canUpgradable: Boolean,
        val canExtendable: Boolean
) : Valuable<String> {

    UNCRAFTABLE("empty", null, false, false),
    WATER("water", null, false, false),
    MUNDANE("mundane", null, false, false),
    THICK("thick", null, false, false),
    AWKWARD("awkward", null, false, false),
    NIGHT_VISION("night_vision", PotionEffectType.NIGHT_VISION, false, true),
    INVISIBILITY("invisibility", PotionEffectType.INVISIBILITY, false, true),
    LEAPING("leaping", PotionEffectType.JUMP_BOOST, true, true),
    FIRE_RESISTANCE("fire_resistance", PotionEffectType.FIRE_RESISTANCE, false, true),
    SWIFTNESS("swiftness", PotionEffectType.SPEED, true, true),
    SLOWNESS("slowness", PotionEffectType.SLOWNESS, false, true),
    WATER_BREATHING("water_breathing", PotionEffectType.WATER_BREATHING, false, true),
    HEALING("healing", PotionEffectType.INSTANT_HEALTH, true, false),
    HARMING("harming", PotionEffectType.INSTANT_DAMAGE, true, false),
    POISON("poison", PotionEffectType.POISON, true, true),
    REGENERATION("regeneration", PotionEffectType.REGENERATION, true, true),
    STRENGTH("strength", PotionEffectType.STRENGTH, true, true),
    WEAKNESS("weakness", PotionEffectType.WEAKNESS, false, true),
    LUCK("luck", PotionEffectType.LUCK, false, false),
    TURTLE_MASTER("turtle_master", PotionEffectType.SLOWNESS, true, true),
    SLOW_FALLING("slow_falling", PotionEffectType.SLOW_FALLING, false, true),
    ;

    override val value: String
        get() = type

    fun isInstant() : Boolean
            = effect != null && effect.isInstant()
}
