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

import java.util.Collections

/**
 * ## DataType (数据类型)
 *
 * * Eight basic data types and enumeration classes of type `void`.
 * * 八大基本数据类型和 `void` 类型的枚举类.
 *
 * > * 在 `Kotlin` 中如果你使用了例如 `Int?` 作为类型. 那么最终编译出来的是 `java.lang.Integer` 封装类型, 而不是 `int` 的基本类型.
 *          因为 `?` 代表可 `null` 所以必须使用封装类. 其他数据类型同理.
 *
 * @author lgou2w
 */
enum class DataType(
  /**
   * * The basic data class of this type.
   * * 该类型的基本数据类.
   */
  val primitive: Class<*>,
  /**
   * * This type of wrapped data class.
   * * 该类型的封装数据类.
   */
  val reference: Class<*>
) {

  /**
   * * Void
   *      * `void` <=> `kotlin.Unit`
   *      * `java.lang.Void` <=> `kotlin.Unit`
   */
  VOID(java.lang.Void.TYPE, java.lang.Void::class.java),
  /**
   * * Byte
   *      * `byte` <=> `kotlin.Byte`
   *      * `java.lang.Byte` <=> `kotlin.Byte?`
   */
  BYTE(java.lang.Byte.TYPE, java.lang.Byte::class.java),
  /**
   * * Short
   *      * `short` <=> `kotlin.Short`
   *      * `java.lang.Short` <=> `kotlin.Short?`
   */
  SHORT(java.lang.Short.TYPE, java.lang.Short::class.java),
  /**
   * * Integer
   *      * `int` <=> `kotlin.Int`
   *      * `java.lang.Integer` <=> `kotlin.Int?`
   */
  INTEGER(java.lang.Integer.TYPE, java.lang.Integer::class.java),
  /**
   * * Long
   *      * `long` <=> `kotlin.Long`
   *      * `java.lang.Long` <=> `kotlin.Long?`
   */
  LONG(java.lang.Long.TYPE, java.lang.Long::class.java),
  /**
   * * Char
   *      * `char` <=> `kotlin.Char`
   *      * `java.lang.Character` <=> `kotlin.Char?`
   */
  CHAR(java.lang.Character.TYPE, java.lang.Character::class.java),
  /**
   * * Float
   *      * `float` <=> `kotlin.Float`
   *      * `java.lang.Float` <=> `kotlin.Float?`
   */
  FLOAT(java.lang.Float.TYPE, java.lang.Float::class.java),
  /**
   * * Double
   *      * `double` <=> `kotlin.Double`
   *      * `java.lang.Double` <=> `kotlin.Double?`
   */
  DOUBLE(java.lang.Double.TYPE, java.lang.Double::class.java),
  /**
   * * Boolean
   *      * `boolean` <=> `kotlin.Boolean`
   *      * `java.lang.Boolean` <=> `kotlin.Boolean?`
   */
  BOOLEAN(java.lang.Boolean.TYPE, java.lang.Boolean::class.java),
  ;

  companion object {

    private val CLASS_MAP : Map<Class<*>, DataType>

    init {
      val classMap = HashMap<Class<*>, DataType>()
      values().forEach {
        classMap[it.primitive] = it
        classMap[it.reference] = it
      }
      CLASS_MAP = Collections.unmodifiableMap(classMap)
    }

    /**
     * * Get the data type from the given class.
     * * 从给定的类获取数据类型.
     *
     * @param clazz Class
     * @param clazz 类
     */
    @JvmStatic
    fun fromClass(clazz: Class<*>): DataType? {
      return CLASS_MAP[clazz]
    }

    /**
     * * Get the base data class from the given class. If the given class is not a primitive type, then return the given class.
     * * 从给定的类获取基本数据类. 如果给定类不是基本类型, 那么返回给定类.
     *
     * @param clazz Class
     * @param clazz 类
     */
    @JvmStatic
    fun ofPrimitive(clazz: Class<*>): Class<*>
      = fromClass(clazz)?.primitive ?: clazz

    /**
     * * Get the wrapped data class from the given class. If the given class is not a wrapped type, then return the given class.
     * * 从给定的类获取封装数据类. 如果给定类不是封装类型, 那么返回给定类.
     *
     * @param clazz Class
     * @param clazz 类
     */
    @JvmStatic
    fun ofReference(clazz: Class<*>): Class<*>
      = fromClass(clazz)?.reference ?: clazz

    /**
     * * Converts the given array class element to a primitive data class. If the element is not a primitive data type, then it is an element class.
     * * 将给定的数组类元素转换为基本数据类. 如果元素不是基本数据类型, 那么为元素类.
     *
     * @param clazz Array class
     * @param clazz 数据类
     */
    @JvmStatic
    fun ofPrimitive(clazz: Array<out Class<*>>): Array<Class<*>>
      = clazz.map(::ofPrimitive).toTypedArray()

    /**
     * * Converts the given array class element to a wrapped data class. If the element is not a wrapped data type, then it is an element class.
     * * 将给定的数组类元素转换为封装数据类. 如果元素不是封装数据类型, 那么为元素类.
     *
     * @param clazz Array class
     * @param clazz 数据类
     */
    @JvmStatic
    fun ofReference(clazz: Array<out Class<*>>): Array<Class<*>>
      = clazz.map(::ofReference).toTypedArray()

    /**
     * * Compares the contents of the given [left] and [right] array classes.
     * * 将给定的 [left] 和 [right] 数组类进行内容比较.
     *
     * @param left Array class
     * @param right Array class
     */
    @JvmStatic
    fun compare(left: Array<Class<*>>?, right: Array<Class<*>>?): Boolean {
      if (left === right)
        return true
      if (left != null && right != null && left.size == right.size) {
        (0 until left.size)
          .filter { left[it] != right[it] && !left[it].isAssignableFrom(right[it]) }
          .forEach { _ -> return false }
        return true
      }
      return left == null && right == null
    }
  }
}
