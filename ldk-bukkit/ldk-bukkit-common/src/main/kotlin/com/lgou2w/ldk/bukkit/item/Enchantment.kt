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

package com.lgou2w.ldk.bukkit.item

import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.bukkit.version.MinecraftVersion
import com.lgou2w.ldk.common.Valuable
import com.lgou2w.ldk.common.isOrLater
import org.bukkit.NamespacedKey
import java.util.*

/**
 * ## Enchantment (附魔)
 *
 * * Enumerate all enchant effects in Minecraft.
 * * 枚举 Minecraft 中所有的附魔效果.
 *
 * @see [Valuable]
 * @see [org.bukkit.enchantments.Enchantment]
 * @author lgou2w
 * @since 2.0
 */
enum class Enchantment(
        /**
         * * The id of this enchantment.
         * * 此附魔的 Id.
         */
        val id: Int,
        /**
         * * The max level of this enchantment.
         * * 此附魔的最大等级.
         */
        val max: Int,
        /**
         * * This enchanted type name in the old version.
         * * 此附魔在旧版本的类型名
         */
        val legacy: String,
        /**
         * * The type of this enchantment.
         * * 此附魔的类型名.
         */
        val type: String,
        /**
         * * The minimum version of this enchantment.
         * * 此附魔的最小版本.
         */
        val minimum: MinecraftVersion? = null
) : Valuable<String> {

    /**
     * * Enchantment: Environmental Protection
     * * 附魔类型: 保护
     */
    PROTECTION(0, 4, "protection_environmental", "protection"),
    /**
     * * Enchantment: Fire Protection
     * * 附魔类型: 火焰保护
     */
    FIRE_PROTECTION(1, 4, "protection_fire", "fire_protection"),
    /**
     * * Enchantment: Feather Falling
     * * 附魔类型: 摔落保护
     */
    FEATHER_FALLING(2, 4, "protection_fall", "feather_falling"),
    /**
     * * Enchantment: Blast Protection
     * * 附魔类型: 爆炸保护
     */
    BLAST_PROTECTION(3, 4, "protection_explosions", "blast_protection"),
    /**
     * * Enchantment: Projectile Protection
     * * 附魔类型: 弹射物保护
     */
    PROJECTILE_PROTECTION(4, 4, "protection_projectile", "projectile_protection"),
    /**
     * * Enchantment: Respiration
     * * 附魔类型: 水下呼吸
     */
    RESPIRATION(5, 3, "oxygen", "respiration"),
    /**
     * * Enchantment: Aqua Affinity
     * * 附魔类型: 水下速掘
     */
    AQUA_AFFINITY(6, 1, "water_worker", "aqua_affinity"),
    /**
     * * Enchantment: Thorns
     * * 附魔类型: 荆棘
     */
    THORNS(7, 3, "thorns", "thorns"),
    /**
     * * Enchantment: Depth Strider
     * * 附魔类型: 深海探索者
     */
    DEPTH_STRIDER(8, 3, "depth_strider", "depth_strider"),
    /**
     * * Enchantment: Frost Walker
     * * 附魔类型: 冰霜行者
     */
    FROST_WALKER(9, 2, "frost_walker", "frost_walker", MinecraftVersion.V1_9),
    /**
     * * Enchantment: Binding Curse
     * * 附魔类型: 绑定诅咒
     */
    BINDING_CURSE(10, 1, "binding_curse", "binding_curse", MinecraftVersion.V1_11),

    /**
     * * Enchantment: Sharpness
     * * 附魔类型: 锋利
     */
    SHARPNESS(16, 5, "damage_all", "sharpness"),
    /**
     * * Enchantment: Smite
     * * 附魔类型: 亡灵杀手
     */
    SMITE(17, 5, "damage_undead", "smite"),
    /**
     * * Enchantment: Bane of arthropods
     * * 附魔类型: 节肢杀手
     */
    BANE_OF_ARTHROPODS(18, 5, "damage_arthropods", "bane_of_arthropods"),
    /**
     * * Enchantment: Knockback
     * * 附魔类型: 击退
     */
    KNOCKBACK(19, 2, "knockback", "knockback"),
    /**
     * * Enchantment: Fire Aspect
     * * 附魔类型: 火焰附加
     */
    FIRE_ASPECT(20, 2, "fire_aspect", "fire_aspect"),
    /**
     * * Enchantment: Looting
     * * 附魔类型: 抢夺
     */
    LOOTING(21, 3, "loot_bonus_mobs", "looting"),
    /**
     * * Enchantment: Sweeping
     * * 附魔类型: 横扫之刃
     */
    SWEEPING(22, 3, "sweeping_edge", "sweeping", MinecraftVersion(1, 11, 1)),

    /**
     * * Enchantment: Efficiency
     * * 附魔类型: 效率
     */
    EFFICIENCY(32, 5, "dig_speed", "efficiency"),
    /**
     * * Enchantment: Silk Touch
     * * 附魔类型: 精准采集
     */
    SILK_TOUCH(33, 1, "silk_touch", "silk_touch"),
    /**
     * * Enchantment: Unbreaking
     * * 附魔类型: 耐久
     */
    UNBREAKING(34, 3, "durability", "unbreaking"),
    /**
     * * Enchantment: Fortune
     * * 附魔类型: 时运
     */
    FORTUNE(35, 3, "loot_bonus_blocks", "fortune"),

    /**
     * * Enchantment: Power
     * * 附魔类型: 力量
     */
    POWER(48, 5, "arrow_damage", "power"),
    /**
     * * Enchantment: Punch
     * * 附魔类型: 冲击
     */
    PUNCH(49, 2, "arrow_knockback", "punch"),
    /**
     * * Enchantment: Flame
     * * 附魔类型: 火矢
     */
    FLAME(50, 1, "arrow_fire", "flame"),
    /**
     * * Enchantment: Infinite
     * * 附魔类型: 无限
     */
    INFINITE(51, 1, "arrow_infinite", "infinite"),

    /**
     * * Enchantment: Luck of the sea
     * * 附魔类型: 海之眷顾
     */
    LUCK_OF_THE_SEA(61, 3, "luck", "luck_of_the_sea"),
    /**
     * * Enchantment: Lure
     * * 附魔类型: 饵钓
     */
    LURE(62, 3, "lure", "lure"),

    /**
     * * Enchantment: Loyalty
     * * 附魔类型: 忠诚
     */
    LOYALTY(65, 3, "loyalty", "loyalty", MinecraftVersion.V1_13),
    /**
     * * Enchantment: Impaling
     * * 附魔类型: 穿刺
     */
    IMPALING(66, 5, "impaling", "impaling", MinecraftVersion.V1_13),
    /**
     * * Enchantment: Riptide
     * * 附魔类型: 激流
     */
    RIPTIDE(67, 3, "riptide", "riptide", MinecraftVersion.V1_13),
    /**
     * * Enchantment: Channeling
     * * 附魔类型: 引雷
     */
    CHANNELING(68, 1, "channeling", "channeling", MinecraftVersion.V1_13),

    /**
     * * Enchantment: Mending
     * * 附魔类型: 经验修补
     */
    MENDING(70, 1, "mending", "mending", MinecraftVersion.V1_9),
    /**
     * * Enchantment: Vanishing Curse
     * * 附魔类型: 消失诅咒
     */
    VANISHING_CURSE(71, 1, "vanishing_curse", "vanishing_curse", MinecraftVersion.V1_11),
    ;

    override val value: String
        get() = type

    fun toBukkit(): org.bukkit.enchantments.Enchantment {
        return if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
            org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft(type))
        } else {
            @Suppress("DEPRECATION")
            org.bukkit.enchantments.Enchantment.getByName(legacy)
        }
    }

    companion object {

        @JvmStatic private val ID_MAP: MutableMap<Int, Enchantment> = HashMap()
        @JvmStatic private val NAME_MAP: MutableMap<String, Enchantment> = HashMap()

        init {
            values().forEach {
                ID_MAP[it.id] = it
                // Maximize compatibility with old and new version type names
                // 最大化兼容旧版本和新版本的类型名称
                NAME_MAP[it.legacy] = it
                NAME_MAP[it.type] = it
            }
        }

        /**
         * * Get the enchantment type from the given name.
         * * 从给定的名称获取附魔类型.
         *
         * @throws IllegalArgumentException If the name does not exist.
         * @throws IllegalArgumentException 如果名称不存在.
         */
        @JvmStatic
        @JvmName("fromName")
        fun fromName(name: String): Enchantment
                = NAME_MAP[name.toLowerCase(Locale.US)] ?: throw IllegalArgumentException("无效的附魔类型名: $name.")

        /**
         * * Get the enchantment type from the given id.
         * * 从给定的 Id 获取附魔类型.
         *
         * @throws IllegalArgumentException If the id does not exist.
         * @throws IllegalArgumentException 如果 Id 不存在.
         */
        @JvmStatic
        @JvmName("fromId")
        @Throws(IllegalArgumentException::class)
        fun fromId(id: Int): Enchantment
                = ID_MAP[id] ?: throw IllegalArgumentException("无效的附魔效果 ID $id 值.")

        /**
         * * Whether the enchantment type exists from the given Id.
         * * 从给定的 Id 获取附魔类型是否存在.
         */
        @JvmStatic
        @JvmName("hasId")
        fun hasId(id: Int): Boolean
                = ID_MAP.containsKey(id)
    }
}
