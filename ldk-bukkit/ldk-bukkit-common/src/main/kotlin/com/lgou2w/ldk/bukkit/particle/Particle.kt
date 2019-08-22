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

import com.lgou2w.ldk.bukkit.version.API
import com.lgou2w.ldk.bukkit.version.Draft
import com.lgou2w.ldk.bukkit.version.Level
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.common.Valuable
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.Collections
import java.util.HashMap
import kotlin.math.abs

/**
 * ## Particle (粒子效果)
 *
 * @see [Valuable]
 * @see [ParticleFactory]
 * @author lgou2w
 */
enum class Particle(
        private val internal: Int,
        /**
         * * The old version name of this particle.
         * * 此粒子的旧版本名称.
         */
        val legacy: String,
        /**
         * * The new version name of this particle.
         * * 此粒子的新版本名称.
         */
        val type: String
) : Valuable<Int> {

    /**
     * * 如果粒子由 `@Deprecated` 注解那么代表在 `Minecraft 1.13` 中被移除.
     *      不过依然是能够用的, 仅仅只是兼容层. 只是不会记住 Id 而已.
     *
     * * 由 `@API` 注解的粒子说明是对应版本才加入的, 使用时注意.
     *
     * 目前 `Minecraft 1.12.2` 为止, 可用的粒子效果一共为 49 种. [SPIT] 结束.
     * 在 `Minecraft 1.13` 中分别移除了 [UNDERWATER_DEPTH]、[FOOTSTEP]、[ITEM_SNOW_SHOVEL]、[BLOCKDUST]、[TAKE]，
     *      不过没关系的, 这些在 1.13 依然能够使用, LDK 已经为这些做好兼容.
     */

    POOF(34, "explosion_normal", "poof"),
    EXPLOSION(19, "explosion_large", "explosion"),
    EXPLOSION_EMITTER(18, "explosion_huge", "explosion_emitter"),
    FIREWORK(21, "fireworks_spark", "firework"),
    BUBBLE(4, "water_bubble", "bubble"),
    SPLASH(43, "water_splash", "splash"),
    FISHING(22, "water_wake", "fishing"),
    UNDERWATER(42, "suspended", "underwater"),
    @Deprecated("Removed in Minecraft 1.13, Automatically adapt if used.", replaceWith = ReplaceWith("UNDERWATER"))
    UNDERWATER_DEPTH("suspended_depth", -42),
    CRIT(6, "crit", "crit"),
    ENCHANTED_HIT(14, "crit_magic", "enchanted_hit"),
    SMOKE(37, "smoke_normal", "smoke"),
    LARGE_SMOKE(30, "smoke_large", "large_smoke"),
    EFFECT(12, "spell", "effect"),
    INSTANT_EFFECT(26, "spell_instant", "instant_effect"),
    ENTITY_EFFECT(17, "spell_mob", "entity_effect"),
    AMBIENT_ENTITY_EFFECT(0, "spell_mob_ambient", "ambient_entity_effect"),
    WITCH(44, "spell_witch", "witch"),
    DRIPPING_WATER(10, "drip_water", "dripping_water"),
    DRIPPING_LAVA(9, "drip_lava", "dripping_lava"),
    ANGRY_VILLAGER(1, "villager_angry", "angry_villager"),
    HAPPY_VILLAGER(24, "villager_happy", "happy_villager"),
    MYCELIUM(32, "town_aura", "mycelium"),
    NOTE(33, "note", "note"),
    PORTAL(35, "portal", "portal"),
    ENCHANT(15, "enchantment_table", "enchant"),
    FLAME(23, "flame", "flame"),
    LAVA(31, "lava", "lava"),
    @Deprecated("Removed in Minecraft 1.13, Automatically adapt if used.")
    FOOTSTEP("footstep", -2),
    CLOUD(5, "cloud", "cloud"),
    DUST(11, "red_dust", "dust"),
    ITEM_SNOWBALL(29, "snowball", "item_snowball"),
    @Deprecated("Removed in Minecraft 1.13, Automatically adapt if used.", replaceWith = ReplaceWith("ITEM_SNOWBALL"))
    ITEM_SNOW_SHOVEL("snow_shovel", -29),
    ITEM_SLIME(28, "slime", "item_slime"),
    HEART(25, "heart", "heart"),
    BARRIER(2, "barrier", "barrier"),
    ITEM(27, "item_crack", "item"),
    BLOCK(3, "block_crack", "block"),
    @Deprecated("Removed in Minecraft 1.13, Automatically adapt if used.", replaceWith = ReplaceWith("BLOCK"))
    BLOCKDUST("blockdust", -3),
    RAIN(36, "water_drop", "rain"),
    @Deprecated("Removed in Minecraft 1.13, Automatically adapt if used.", replaceWith = ReplaceWith("ITEM"))
    TAKE("item_take", -27),
    ELDER_GUARDIAN(13, "mob_appearance", "elder_guardian"),

    @API(Level.Minecraft_V1_9) DRAGON_BREATH(8, "dragon_breath", "dragon_breath"),
    @API(Level.Minecraft_V1_9) END_ROD(16, "end_rod", "end_rod"),
    @API(Level.Minecraft_V1_9) DAMAGE_INDICATOR(7, "damage_indicator", "damage_indicator"),
    @API(Level.Minecraft_V1_9) SWEEP_ATTACK(40, "sweep_attack", "sweep_attack"),
    @API(Level.Minecraft_V1_10) FALLING_DUST(20, "falling_dust", "falling_dust"),
    @API(Level.Minecraft_V1_11) TOTEM_OF_UNDYING(41, "totem", "totem_of_undying"),
    @API(Level.Minecraft_V1_11) SPIT(38, "spit", "spit"),

    @API(Level.Minecraft_V1_13) SQUID_INK(39, "squid_ink", "squid_ink"),
    @API(Level.Minecraft_V1_13) BUBBLE_POP(45, "bubble_pop", "bubble_pop"),
    @API(Level.Minecraft_V1_13) CURRENT_DOWN(46, "current_down", "current_down"),
    @API(Level.Minecraft_V1_13) BUBBLE_COLUMN_UP(47, "bubble_column_up", "bubble_column_up"),
    @API(Level.Minecraft_V1_13) NAUTILUS(48, "nautilus", "nautilus"),
    @API(Level.Minecraft_V1_13) DOLPHIN(49, "dolphin", "dolphin"),

    @API(Level.Minecraft_V1_14) SNEEZE(50, "sneeze", "sneeze"),
    @API(Level.Minecraft_V1_14) CAMPFIRE_COSY_SMOKE(51, "campfire_cosy_smoke", "campfire_cosy_smoke"),
    @API(Level.Minecraft_V1_14) CAMPFIRE_SIGNAL_SMOKE(52, "campfire_signal_smoke", "campfire_signal_smoke"),
    @API(Level.Minecraft_V1_14) COMPOSTER(53, "composter", "composter"),
    @API(Level.Minecraft_V1_14) FLASH(54, "flash", "flash"),
    @API(Level.Minecraft_V1_14) FALLING_LAVA(55, "falling_lava", "falling_lava"),
    @API(Level.Minecraft_V1_14) LANDING_LAVA(56, "landing_lava", "landing_lava"),
    @API(Level.Minecraft_V1_14) FALLING_WATER(57, "falling_water", "falling_water"),

    @Draft @Deprecated("Minecraft 1.15 Draft") DRIPPING_HONEY(58, "dripping_honey", "dripping_honey"),
    @Draft @Deprecated("Minecraft 1.15 Draft") FALLING_HONEY(59, "falling_honey", "falling_honey"),
    @Draft @Deprecated("Minecraft 1.15 Draft") LANDING_HONEY(60, "landing_honey", "landing_honey"),
    @Draft @Deprecated("Minecraft 1.15 Draft") FALLING_NECTAR(61, "falling_nectar", "falling_nectar"),
    ;

    constructor(
            legacy: String,
            equivalent: Int
    ) : this(equivalent, legacy, legacy)

    override val value : Int
        get() = if (MinecraftBukkitVersion.isV113OrLater)
            @Suppress("DEPRECATION")
            (abs(internal))
        else
            ordinal

    companion object {

        @JvmStatic var COMPATIBILITIES : Array<Particle> private set
        @JvmStatic private val ID_MAP : Map<Int, Particle>
        @JvmStatic private val NAME_MAP : Map<String, Particle>

        // internal < 0 => skip
        // Particle > SPIT && CURRENT < 1.13 => skip

        init {
            val isV113OrLater = MinecraftBukkitVersion.isV113OrLater
            val idMap = HashMap<Int, Particle>()
            val nameMap = HashMap<String, Particle>()
            values().forEach { particle ->
                idMap[particle.value] = particle
                nameMap[particle.legacy] = particle
                nameMap[particle.type] = particle
            }
            ID_MAP = Collections.unmodifiableMap(idMap)
            NAME_MAP = Collections.unmodifiableMap(nameMap)
            COMPATIBILITIES = ID_MAP.values
                .filter { (isV113OrLater && it.internal >= 0) || (!isV113OrLater && it.ordinal <= SPIT.ordinal) }
                .toTypedArray()
        }

        @JvmStatic
        fun fromId(id: Int): Particle
                = ID_MAP[id] ?: throw IllegalArgumentException("Unsupported particle effect type: $id")

        @JvmStatic
        fun fromName(name: String): Particle
                = NAME_MAP[name] ?: throw IllegalArgumentException("Unsupported particle effect type: $name")
    }

    /**************************************************************************
     *
     * Particle Send Extended
     *
     **************************************************************************/

    /**
     * @since LDK 0.1.8-rc
     */
    @JvmOverloads
    fun display(
            center: Location,
            range: Double = 32.0,
            offsetX: Float = 0f,
            offsetY: Float = 0f,
            offsetZ: Float = 0f,
            speed: Float = 0.0f,
            count: Int = 1,
            data: Any? = null
    ) = ParticleFactory.sendParticle(this, center, range, offsetX, offsetY, offsetZ, speed, count, data)

    /**
     * @since LDK 0.1.8-rc
     */
    @JvmOverloads
    fun display(
            sender: Player?,
            center: Location,
            offsetX: Float = 0f,
            offsetY: Float = 0f,
            offsetZ: Float = 0f,
            speed: Float = 0.0f,
            count: Int = 1,
            data: Any? = null
    ) = ParticleFactory.sendParticle(sender, this, center, offsetX, offsetY, offsetZ, speed, count, data)

    /**
     * @since LDK 0.1.8-rc
     */
    @JvmOverloads
    fun display(
            players: List<Player>,
            center: Location,
            offsetX: Float = 0f,
            offsetY: Float = 0f,
            offsetZ: Float = 0f,
            speed: Float = 0.0f,
            count: Int = 1,
            data: Any? = null
    ) = ParticleFactory.sendParticle(players, this, center, offsetX, offsetY, offsetZ, speed, count, data)
}
