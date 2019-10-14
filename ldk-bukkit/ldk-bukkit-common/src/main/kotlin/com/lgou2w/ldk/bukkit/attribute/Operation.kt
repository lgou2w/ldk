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

package com.lgou2w.ldk.bukkit.attribute

import com.lgou2w.ldk.common.Valuable

/**
 * ## Operation (运算方式)
 *
 * @see [Valuable]
 * @see [AttributeModifier]
 * @see [AttributeItemModifier]
 * @author lgou2w
 */
enum class Operation : Valuable<Int> {

  /**
   * * Adds all of the modifiers amount to the current value of the attribute.
   * * 将所有属性修改器的数量添加到属性的当前值.
   */
  ADD_NUMBER,

  /**
   * * Multiplies the current value of the attribute by (1 + `x`), where `x` is the sum of the modifiers amount.
   * * 将属性的当前值乘以 (1 + `x`), 其中 `x` 是属性修饰符量的总和.
   */
  ADD_SCALAR,

  /**
   * * For every modifier, multiplies the current value of the attribute by (1 + `x`), where `x` is the amount of the particular modifier.
   *      Functions the same as Operation 1 if there is only a single modifier with operation 1 or 2. However,
   *      for multiple modifiers it will multiply the modifiers rather than adding them.
   * * 对于每个属性修改器，将属性的当前值乘以 (1 + `x`), 其中 `x` 是特定属性修改器的数量. 如果只有一个属性修改器具有操作1或2, 则与操作1的功能相同.
   *      但是, 对于多个属性修改器, 它将乘以属性修改器而不是添加它们.
   */
  MULTIPLY_SCALAR_1,
  ;

  override val value : Int
    get() = ordinal
}
