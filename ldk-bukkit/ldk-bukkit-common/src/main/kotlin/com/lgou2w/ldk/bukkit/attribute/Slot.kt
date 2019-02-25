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

import com.lgou2w.ldk.common.Valuable

/**
 * ## Slot (槽位)
 *
 * * Enumeration can be used for attribute item stack modifier takes effect in which slot.
 * * 枚举可用于属性物品修改器的生效槽位.
 *
 * @see [AttributeItemModifier]
 * @author lgou2w
 */
enum class Slot(
        /**
         * * Enum type name.
         * * 枚举类型名称.
         */
        val type: String

) : Valuable<String> {

    /**
     * * Attribute Slot: Main hand
     * * 属性部位: 主手
     */
    MAIN_HAND("mainhand"),
    /**
     * * Attribute Slot: Off hand
     * * 属性部位: 副手
     */
    OFF_HAND("offhand"),
    /**
     * * Attribute Slot: Head
     * * 属性部位: 头
     */
    HEAD("head"),
    /**
     * * Attribute Slot: Legs
     * * 属性部位: 腿
     */
    LEGS("legs"),
    /**
     * * Attribute Slot: Chest
     * * 属性部位: 胸
     */
    CHEST("chest"),
    /**
     * * Attribute Slot: Feet
     * * 属性部位: 脚
     */
    FEET("feet"),
    ;

    override val value : String
        get() = type
}
