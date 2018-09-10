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

package com.lgou2w.ldk.bukkit.attribute

/**
 * ## Attribute (属性)
 *
 * @see [Attributable]
 * @author lgou2w
 */
interface Attribute {

    /**
     * * Get the type of this attribute.
     * * 获取此属性的类型.
     *
     * @see [AttributeType]
     */
    val type: AttributeType

    /**
     * * Get the default value of this attribute.
     * * 获取此属性的默认值
     *
     * @see [AttributeType.defValue]
     */
    val defValue: Double

    /**
     * * Gets or sets the base value of this attribute.
     * * 获取或设置此属性的基础值.
     */
    var baseValue: Double

    /**
     * * Get the value of this attribute after all associated modifiers have been applied.
     * * 在应用了所有关联的修改器后, 获取此属性的最终值.
     */
    val value: Double

    /**
     * * Get all modifiers present on this attribute.
     * * 获取此属性的所有修改器.
     */
    val modifiers: Collection<AttributeModifier>

    /**
     * * Add a modifier to this attribute.
     * * 添加一个修改器到此属性.
     *
     * @param modifier Modifier.
     * @param modifier 修改器.
     * @see [AttributeModifier]
     */
    fun addModifier(modifier: AttributeModifier)

    /**
     * * Remove a modifier from this attribute.
     * * 从此属性删除一个修改器.
     *
     * @param modifier Modifier.
     * @param modifier 修改器.
     * @see [AttributeModifier]
     */
    fun removeModifier(modifier: AttributeModifier)
}
