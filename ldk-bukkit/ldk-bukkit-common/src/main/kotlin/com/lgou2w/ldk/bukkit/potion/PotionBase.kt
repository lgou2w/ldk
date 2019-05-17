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

import com.lgou2w.ldk.common.ComparisonChain
import com.lgou2w.ldk.common.Enums
import com.lgou2w.ldk.common.Valuable

/**
 * ## PotionBase (药水基础)
 *
 * @see [Valuable]
 * @see [PotionType]
 * @author lgou2w
 */
data class PotionBase(
        /**
         * * The potion type of this potion.
         * * 此药水的药水类型.
         */
        val type: PotionType,
        /**
         * * Indicate whether this potion is can upgraded.
         * * 表示此药水是否可升级.
         */
        val isUpgraded: Boolean,
        /**
         * * Indicate whether this potion is can extended.
         * * 表示此药水是否可延长.
         */
        val isExtended: Boolean
) : Valuable<String>,
        Comparable<PotionBase> {

    init {
        if (! (!isUpgraded || type.canUpgradable))
            throw IllegalArgumentException("Potion Type $type is not upgradable.")
        if (! (!isExtended || type.canExtendable))
            throw IllegalArgumentException("Potion Type $type is not extendable.")
        if (! (!isExtended || !isUpgraded))
            throw IllegalArgumentException("Potion cannot be both extended and upgraded.")
    }

    override fun compareTo(other: PotionBase): Int {
        return ComparisonChain.start()
            .compare(type, other.type)
            .compare(isUpgraded, other.isUpgraded)
            .compare(isExtended, other.isExtended)
            .result
    }

    override val value : String
        get() = when {
            isUpgraded -> PREFIX_UPGRADED + REPLACEMENT + type.value
            isExtended -> PREFIX_EXTENDED + REPLACEMENT + type.value
            else -> type.value
        }

    companion object {

        const val PREFIX_UPGRADED = "strong"
        const val PREFIX_EXTENDED = "long"
        const val REPLACEMENT = "_"

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun valueOf(value: String): PotionBase {
            val isUpgraded = value.startsWith(PREFIX_UPGRADED)
            val isExtended = value.startsWith(PREFIX_EXTENDED)
            val replacement = REPLACEMENT.length
            val type = when {
                isUpgraded -> Enums.ofValuableNotNull(PotionType::class.java, value.substring(PREFIX_UPGRADED.length + replacement))
                isExtended -> Enums.ofValuableNotNull(PotionType::class.java, value.substring(PREFIX_EXTENDED.length + replacement))
                else -> PotionType.WATER
            }
            return if (type == PotionType.WATER) PotionBase(PotionType.WATER, isUpgraded = false, isExtended = false)
            else PotionBase(type, isUpgraded, isExtended)
        }
    }
}
