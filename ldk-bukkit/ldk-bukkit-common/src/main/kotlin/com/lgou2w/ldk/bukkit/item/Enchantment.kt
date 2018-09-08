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
    PROTECTION(0, 4, "PROTECTION_ENVIRONMENTAL", "protection"),
    /**
     * * Enchantment: Fire Protection
     * * 附魔类型: 火焰保护
     */
    PROTECTION_FIRE(1, 4, "PROTECTION_FIRE", "fire_protection"),
    /**
     * * Enchantment: Fall Protection
     * * 附魔类型: 摔落保护
     */
    PROTECTION_FALL(2, 4, "PROTECTION_FALL", "feather_falling"),
    /**
     * * Enchantment: Explosions Protection
     * * 附魔类型: 爆炸保护
     */
    PROTECTION_EXPLOSIONS(3, 4, "PROTECTION_EXPLOSIONS", "blast_protection"),
    /**
     * * Enchantment: Projectile Protection
     * * 附魔类型: 弹射物保护
     */
    PROTECTION_PROJECTILE(4, 4, "PROTECTION_PROJECTILE", "projectile_protection"),
    /**
     * * Enchantment: Oxygen
     * * 附魔类型: 水下呼吸
     */
    OXYGEN(5, 3, "OXYGEN", "respiration"),
    /**
     * * Enchantment: Water Worker
     * * 附魔类型: 水下速掘
     */
    WATER_WORKER(6, 1, "WATER_WORKER", "aqua_affinity"),
    /**
     * * Enchantment: Thorns
     * * 附魔类型: 荆棘
     */
    THORNS(7, 3, "THORNS", "thorns"),
    /**
     * * Enchantment: Depth Strider
     * * 附魔类型: 深海探索者
     */
    DEPTH_STRIDER(8, 3, "DEPTH_STRIDER", "depth_strider"),
    /**
     * * Enchantment: Frost Walker
     * * 附魔类型: 冰霜行者
     */
    FROST_WALKER(9, 2, "FROST_WALKER", "frost_walker", MinecraftVersion.V1_9),
    /**
     * * Enchantment: Binding Curse
     * * 附魔类型: 绑定诅咒
     */
    BINDING_CURSE(10, 1, "BINDING_CURSE", "binding_curse", MinecraftVersion.V1_11),

    /**
     * * Enchantment: All Damage
     * * 附魔类型: 锋利
     */
    DAMAGE(16, 5, "DAMAGE_ALL", "sharpness"),
    /**
     * * Enchantment: Undead Damage
     * * 附魔类型: 亡灵杀手
     */
    DAMAGE_UNDEAD(17, 5, "DAMAGE_UNDEAD", "smite"),
    /**
     * * Enchantment: Arthropods Damage
     * * 附魔类型: 节肢杀手
     */
    DAMAGE_ARTHROPODS(18, 5, "DAMAGE_ARTHROPODS", "bane_of_arthropods"),
    /**
     * * Enchantment: Knockback
     * * 附魔类型: 击退
     */
    KNOCKBACK(19, 2, "KNOCKBACK", "knockback"),
    /**
     * * Enchantment: Fire Aspect
     * * 附魔类型: 火焰附加
     */
    FIRE_ASPECT(20, 2, "FIRE_ASPECT", "fire_aspect"),
    /**
     * * Enchantment: Loot Bonus Mobs
     * * 附魔类型: 抢夺
     */
    LOOT_BONUS_MOBS(21, 3, "LOOT_BONUS_MOBS", "looting"),
    /**
     * * Enchantment: Sweeping Edge
     * * 附魔类型: 横扫之刃
     */
    SWEEPING_EDGE(22, 3, "SWEEPING_EDGE", "sweeping", MinecraftVersion(1, 11, 1)),

    /**
     * * Enchantment: Dig Speed
     * * 附魔类型: 效率
     */
    DIG_SPEED(32, 5, "DIG_SPEED", "efficiency"),
    /**
     * * Enchantment: Silk Touch
     * * 附魔类型: 精准采集
     */
    SILK_TOUCH(33, 1, "SILK_TOUCH", "silk_touch"),
    /**
     * * Enchantment: Durability
     * * 附魔类型: 耐久
     */
    DURABILITY(34, 3, "DURABILITY", "unbreaking"),
    /**
     * * Enchantment: Lott Bonus Blocks
     * * 附魔类型: 时运
     */
    LOOT_BONUS_BLOCKS(35, 3, "LOOT_BONUS_BLOCKS", "fortune"),

    /**
     * * Enchantment: Arrow Damage
     * * 附魔类型: 力量
     */
    ARROW_DAMAGE(48, 5, "ARROW_DAMAGE", "power"),
    /**
     * * Enchantment: Arrow Knockback
     * * 附魔类型: 冲击
     */
    ARROW_KNOCKBACK(49, 2, "ARROW_KNOCKBACK", "punch"),
    /**
     * * Enchantment: Arrow Fire
     * * 附魔类型: 火矢
     */
    ARROW_FIRE(50, 1, "ARROW_FIRE", "flame"),
    /**
     * * Enchantment: Arrow Infinite
     * * 附魔类型: 无限
     */
    ARROW_INFINITE(51, 1, "ARROW_INFINITE", "infinite"),

    /**
     * * Enchantment: Luck
     * * 附魔类型: 海之眷顾
     */
    LUCK(61, 3, "LUCK", "luck_of_the_sea"),
    /**
     * * Enchantment: Lure
     * * 附魔类型: 饵钓
     */
    LURE(62, 3, "LURE", "lure"),

    /**
     * * Enchantment: Loyalty
     * * 附魔类型: 忠诚
     */
    LOYALTY(65, 3, "LOYALTY", "loyalty", MinecraftVersion.V1_13),
    /**
     * * Enchantment: Impaling
     * * 附魔类型: 穿刺
     */
    IMPALING(66, 5, "IMPALING", "impaling", MinecraftVersion.V1_13),
    /**
     * * Enchantment: Riptide
     * * 附魔类型: 激流
     */
    RIPTIDE(67, 3, "RIPTIDE", "riptide", MinecraftVersion.V1_13),
    /**
     * * Enchantment: Channeling
     * * 附魔类型: 引雷
     */
    CHANNELING(68, 1, "CHANNELING", "channeling", MinecraftVersion.V1_13),

    /**
     * * Enchantment: Mending
     * * 附魔类型: 经验修补
     */
    MENDING(70, 1, "MENDING", "mending", MinecraftVersion.V1_9),
    /**
     * * Enchantment: Vanishing Curse
     * * 附魔类型: 消失诅咒
     */
    VANISHING_CURSE(71, 1, "VANISHING_CURSE", "vanishing_curse", MinecraftVersion.V1_11),
    ;

    override val value: String
        get() = type

    fun toBukkit(): org.bukkit.enchantments.Enchantment {
        return if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
            org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft(type))
        } else {
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
                = NAME_MAP[name] ?: throw IllegalArgumentException("无效的附魔类型名: $name.")

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
