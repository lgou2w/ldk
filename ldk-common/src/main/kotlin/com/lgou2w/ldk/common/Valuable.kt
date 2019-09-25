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

package com.lgou2w.ldk.common

/**
 * ## Valuable (有价值的)
 *
 * * Let it have a read-only member attribute of [value].
 *      Generally used for enumeration, refer to [Enums.ofValuable], [Enums.fromValuable] function.
 * * 让其具备一个 [value] 的只读成员属性.
 *      一般用于枚举, 参考 [Enums.ofValuable], [Enums.fromValuable] 功能.
 *
 * @see [Enums.ofValuable]
 * @see [Enums.fromValuable]
 * @author lgou2w
 */
interface Valuable<T> {

  /**
   * @see [Valuable]
   */
  val value: T
}
