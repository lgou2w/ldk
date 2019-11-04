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

import com.lgou2w.ldk.common.ApplicatorFunction
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * ## LazyFuzzyReflect (延迟加载模糊反射)
 *
 * @see [ReadOnlyProperty]
 * @see [Lazy]
 * @author lgou2w
 */
class LazyFuzzyReflect<T> @JvmOverloads constructor(
  source: Class<*>,
  isForceAccess: Boolean = false,
  initializer: ApplicatorFunction<FuzzyReflect, T>
) : ReadOnlyProperty<Any, T>,
  Lazy<T> {

  private val lazyObj = lazy { initializer(FuzzyReflect.of(source, isForceAccess)) }

  override fun getValue(thisRef: Any, property: KProperty<*>): T {
    return lazyObj.getValue(thisRef, property)
  }

  override val value : T
    get() = lazyObj.value

  override fun isInitialized(): Boolean {
    return lazyObj.isInitialized()
  }
}

/**
 * * Lazy fuzzy reflect delegate.
 * * 延迟模糊反射委托.
 */
@JvmOverloads
fun <T> lazyFuzzyReflect(
  source: Class<*>,
  isForceAccess: Boolean = false,
  initializer: ApplicatorFunction<FuzzyReflect, T>
) = LazyFuzzyReflect(source, isForceAccess, initializer)

/**
 * * Lazy fuzzy reflect delegate.
 * * 延迟模糊反射委托.
 */
@JvmOverloads
fun <T> lazyFuzzyReflect(
  source: Any,
  isForceAccess: Boolean = false,
  initializer: ApplicatorFunction<FuzzyReflect, T>
) = LazyFuzzyReflect(source.javaClass, isForceAccess, initializer)
