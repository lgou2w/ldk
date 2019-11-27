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

import com.lgou2w.ldk.common.BiFunction
import com.lgou2w.ldk.common.Callable
import com.lgou2w.ldk.common.Predicate
import com.lgou2w.ldk.common.letIfNotNull
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

/**
 * ## FuzzyReflectFieldMatcher (模糊反射字段匹配器)
 *
 * @see [FuzzyReflect]
 * @see [FuzzyReflectMatcher]
 * @see [Field]
 * @author lgou2w
 */
class FuzzyReflectFieldMatcher(
  reflect: FuzzyReflect,
  initialize: Collection<Field>? = null
) : FuzzyReflectMatcher<Field>(reflect, initialize) {

  override fun with(predicate: Predicate<Field>): FuzzyReflectFieldMatcher {
    return super.with(predicate) as FuzzyReflectFieldMatcher
  }

  override fun <U> with(initialize: Callable<U>, predicate: BiFunction<Field, U, Boolean>): FuzzyReflectFieldMatcher {
    return super.with(initialize, predicate) as FuzzyReflectFieldMatcher
  }

  override fun withVisibilities(vararg visibilities: Visibility): FuzzyReflectFieldMatcher {
    return super.withVisibilities(*visibilities) as FuzzyReflectFieldMatcher
  }

  override fun withName(regex: String): FuzzyReflectFieldMatcher {
    return super.withName(regex) as FuzzyReflectFieldMatcher
  }

  override fun <A : Annotation> withAnnotation(annotation: Class<A>): FuzzyReflectFieldMatcher {
    return super.withAnnotation(annotation) as FuzzyReflectFieldMatcher
  }

  override fun <A : Annotation> withAnnotationIf(annotation: Class<A>, block: Predicate<A>): FuzzyReflectFieldMatcher {
    return super.withAnnotationIf(annotation, block) as FuzzyReflectFieldMatcher
  }

  override fun withType(clazz: Class<*>): FuzzyReflectFieldMatcher {
    val primitiveType = DataType.ofPrimitive(clazz)
    values = values.filter { primitiveType.isAssignableFrom(it.type) }.toMutableList()
    return this
  }

  /**
   * * Matches the reflection value from the given [rawType] and the [actualTypeArguments].
   * * 从给定的原始类型 [rawType] 和实际类型参数 [actualTypeArguments] 匹配反射值.
   *
   * @param rawType Raw type.
   * @param rawType 原始类型.
   * @param actualTypeArguments Actual type arguments.
   * @param actualTypeArguments 实际类型参数.
   * @since LDK 0.1.8-rc
   */
  fun withParameterizedType(rawType: Class<*>?, vararg actualTypeArguments: Class<*>): FuzzyReflectFieldMatcher {
    val primitiveRawType = if (rawType != null) DataType.ofPrimitive(rawType) else null
    val primitiveActualTypeArguments = DataType.ofPrimitive(actualTypeArguments)
    val subActualTypeArgumentSize= primitiveActualTypeArguments.size
    values = values.filter { field ->
      val parameterizedType = field.genericType as? ParameterizedType
      val parameterizedRawType = parameterizedType?.rawType
      if (parameterizedRawType != null && parameterizedRawType is Class<*>) {
        val parameterizedActualTypeArguments = parameterizedType.actualTypeArguments
          .filterIsInstance(Class::class.java)
          .let {
            if (it.size > subActualTypeArgumentSize)
              it.subList(0, subActualTypeArgumentSize).toTypedArray()
            else it.toTypedArray()
          }
        (primitiveRawType == null || primitiveRawType.isAssignableFrom(parameterizedRawType)) &&
          DataType.compare(primitiveActualTypeArguments, parameterizedActualTypeArguments)
      } else false
    }.toMutableList()
    return this
  }

  override fun withParams(vararg parameters: Class<*>): FuzzyReflectFieldMatcher {
    return this // Field does not support parameters
  }

  override fun withParamsCount(count: Int): FuzzyReflectFieldMatcher {
    return this // Field does not support parameters
  }

  override fun resultAccessors(): List<AccessorField<Any, Any>>
    = results().map(Accessors::ofField)

  override fun resultAccessor(): AccessorField<Any, Any>
    = resultAccessorAs()

  override fun resultAccessorOrNull(): AccessorField<Any, Any>?
    = resultOrNull()?.letIfNotNull(Accessors::ofField)

  /**
   * * Get the first valid result accessor for this fuzzy reflection matcher.
   * * 获取此模糊反射匹配器的第一个有效结果访问器.
   *
   * @throws NoSuchElementException If the match result is empty.
   * @throws NoSuchElementException 如果匹配结果为空.
   */
  fun <T, R> resultAccessorAs(): AccessorField<T, R>
    = Accessors.ofField(result())
}
