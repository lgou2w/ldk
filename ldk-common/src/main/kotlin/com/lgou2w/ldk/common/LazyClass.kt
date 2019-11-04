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

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * ## LazyClass (延迟加载类)
 *
 * * Used to lazy loading a specified class.
 * * 用于延迟加载指定的一个类.
 *
 * > * `val clazz by lazyClass { String::class.java }`
 *
 * @see [lazyClass]
 * @see [ReadOnlyProperty]
 * @see [Lazy]
 * @author lgou2w
 */
class LazyClass<T>(
  initializer: Callable<Class<T>>
) : ReadOnlyProperty<Any, Class<T>>,
  Lazy<Class<T>> {

  private val lazyObj = lazy { initializer() }

  override fun getValue(thisRef: Any, property: KProperty<*>): Class<T>
    = lazyObj.getValue(thisRef, property)

  override val value: Class<T>
    get() = lazyObj.value

  override fun isInitialized(): Boolean
    = lazyObj.isInitialized()
}
