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

package com.lgou2w.ldk.reflect

import java.lang.reflect.AccessibleObject
import java.lang.reflect.Member

/**
 * ## Accessor (访问器)
 *
 * @see [Accessors]
 * @see [AccessorConstructor]
 * @see [AccessorMethod]
 * @see [AccessorField]
 * @author lgou2w
 */
interface Accessor<out T> where T : AccessibleObject, T : Member {

  /**
   * * The source object of the accessor.
   * * 访问器的源对象.
   *
   * @see [java.lang.reflect.Constructor]
   * @see [java.lang.reflect.Method]
   * @see [java.lang.reflect.Field]
   */
  val source : T
}
