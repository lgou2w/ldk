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

package com.lgou2w.ldk.bukkit.attribute

import com.lgou2w.ldk.bukkit.version.IllegalBukkitVersionException
import com.lgou2w.ldk.bukkit.version.MinecraftVersion
import com.lgou2w.ldk.common.Valuable
import com.lgou2w.ldk.common.isOrLater

/**
 * ## AttributeType (属性类型)
 *
 * * Enumerate the attribute types available in minecraft. [Details](https://minecraft.gamepedia.com/Attribute)
 * * 枚举出 Minecraft 中可用的属性类型. [详情](https://minecraft.gamepedia.com/Attribute)
 *
 * @author lgou2w
 */
enum class AttributeType(
        /**
         * * The type name of this attribute type.
         * * 此属性类型的类型名称.
         */
        val type: String,
        /**
         * * The default value of this attribute type.
         * * 此属性类型的默认值.
         */
        val defValue: Double,
        /**
         * * The minimum value of this attribute type.
         * * 此属性类型的最小值
         */
        val minValue: Double,
        /**
         * * The maximum value of this attribute type.
         * * 此属性类型的最大值
         */
        val maxValue: Double,
        /**
         * * Minecraft version supported by this attribute type. If `null` then all is supported.
         * * 此属性类型支持的 Minecraft 版本. 如果 `null` 则支持所有.
         */
        val mcVer: MinecraftVersion? = null

) : Valuable<String> {

    /**
     * * Attribute Type: Max Health
     * * 属性类型: 最大生命
     */
    MAX_HEALTH("generic.maxHealth", 20.0, .0, 2048.0),
    /**
     * * Attribute Type: Follow Range
     * * 属性类型: 追踪范围
     */
    FOLLOW_RANGE("generic.followRange", 32.0, .0, 2048.0),
    /**
     * * Attribute Type: Knockback Resistance
     * * 属性类型: 击退抗性
     */
    KNOCKBACK_RESISTANCE("generic.knockbackResistance", .0, .0, 1.0),
    /**
     * * Attribute Type: Movement Speed
     * * 属性类型: 移动速度
     */
    MOVEMENT_SPEED("generic.movementSpeed", 0.699999988079071, .0, 2048.0),
    /**
     * * Attribute Type: Attack Damage
     * * 属性类型: 攻击伤害
     */
    ATTACK_DAMAGE("generic.attackDamage", 2.0, .0, 2048.0),
    /**
     * * Attribute Type: Attack Speed
     * * 属性类型: 攻击速度
     */
    ATTACK_SPEED("generic.attackSpeed", 4.0, .0, 1024.0, MinecraftVersion.V1_9),
    /**
     * * Attribute Type: Armor
     * * 属性类型: 护甲
     */
    ARMOR("generic.armor", .0, .0, 30.0, MinecraftVersion.V1_9),
    /**
     * * Attribute Type: Armor Toughness
     * * 属性类型: 护甲韧性
     */
    ARMOR_TOUGHNESS("generic.armorToughness", .0, .0, 20.0, MinecraftVersion.V1_9),
    /**
     * * Attribute Type: Luck
     * * 属性类型: 幸运
     */
    LUCK("generic.luck", .0, -1024.0, 1024.0, MinecraftVersion.V1_9),
    /**
     * * Attribute Type: Flying Speed
     * * 属性类型: 飞行速度
     */
    FLYING_SPEED("generic.flyingSpeed", 0.4000000059604645, .0, 1024.0, MinecraftVersion.V1_12),
    /**
     * * Attribute Type: Horse Jump Strength
     * * 属性类型: 马弹跳力
     *
     * @see [org.bukkit.entity.Horse]
     */
    HORSE_JUMP_STRENGTH("horse.jumpStrength", 0.69999999999999996, .0, 2.0),
    /**
     * * Attribute Type: Zombie Spawn Reinforcements
     * * 属性类型: 僵尸增援生成率
     *
     * @see [org.bukkit.entity.Zombie]
     */
    ZOMBIE_SPAWN_REINFORCEMENTS("zombie.spawnReinforcements", .0, .0, 1.0),
    ;

    override val value: String
        get() = type

    /**
     * * Verify that the attribute type is supported.
     * * 验证当前属性是否支持.
     *
     * @throws IllegalBukkitVersionException If the attribute type is not supported.
     * @throws IllegalBukkitVersionException 如果属性类型不支持.
     */
    @Throws(IllegalBukkitVersionException::class)
    fun canSupport() : Boolean {
        if (mcVer != null && !MinecraftVersion.CURRENT.isOrLater(mcVer))
            throw IllegalBukkitVersionException("This $this attribute type is not supported in current Bukkit versions.")
        return true
    }
}
