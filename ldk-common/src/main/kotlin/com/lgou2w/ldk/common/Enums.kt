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

package com.lgou2w.ldk.common

/**
 * ## Enums (枚举工具类)
 *
 * * Find matching enum items from the specified enum class with the specified criteria.
 * * 从指定枚举类中以指定条件查找匹配的枚举项.
 *
 * @author lgou2w
 */
object Enums {

    /**************************************************************************
     *
     * Enum Class
     *
     **************************************************************************/

    /**
     * * Finds the matching item from the specified condition from the given enum class.
     * * 将给定的枚举类从指定条件内查找匹配项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param predicate Condition
     * @param predicate 条件
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     */
    @JvmStatic
    @JvmOverloads
    fun <T : Enum<T>> of(clazz: Class<T>, predicate: Predicate<T>, def: T? = null): T? {
        return clazz.enumConstants?.find(predicate) ?: def
    }

    /**
     * * Finds the matching item from the given enum name for the given enum class.
     * * 将给定的枚举类从指定枚举名称内查找匹配项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param name 名称
     * @param name 名称
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @see [Enum.name]
     */
    @JvmStatic
    @JvmOverloads
    fun <T : Enum<T>> ofName(clazz: Class<T>, name: String, def: T? = null): T?
            = of(clazz, { it.name == name }, def)

    /**
     * * Finds the matching item from the given enum origin for the given enum class.
     * * 将给定的枚举类从指定枚举序数内查找匹配项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param origin Ordinal
     * @param origin 序数
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @see [Enum.ordinal]
     */
    @JvmStatic
    @JvmOverloads
    fun <T : Enum<T>> ofOrigin(clazz: Class<T>, origin: Int, def: T? = null): T?
            = of(clazz, { it.ordinal == origin }, def)

    /**
     * * Finds the matching item from the specified [value] in the [Valuable] interface of the given enum class.
     * * 将给定的枚举类以它实现的 [Valuable] 接口中从指定的 [value] 内查找匹配项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param value Specified value
     * @param value 指定值
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @see [Valuable]
     * @see [Valuable.value]
     */
    @JvmStatic
    @JvmOverloads
    fun <V, T> ofValuable(clazz: Class<T>, value: V?, def: T? = null): T? where T : Enum<T>, T : Valuable<V>
            = of(clazz, { it.value == value }, def)

    /**
     * * Finds the matching item from the specified [value] in the [Valuable] interface of the given enum class.
     * * 将给定的枚举类以它实现的 [Valuable] 接口中从指定的 [value] 内查找匹配项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param value Specified value
     * @param value 指定值
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @throws NullPointerException If no matching items are found.
     * @throws NullPointerException 如果未找到匹配项.
     * @see [Valuable]
     * @see [Valuable.value]
     * @see [ofValuable]
     */
    @JvmStatic
    @JvmOverloads
    @Throws(NullPointerException::class)
    fun <V, T> ofValuableNotNull(clazz: Class<T>, value: V?, def: T? = null): T where T : Enum<T>, T : Valuable<V>
            = ofValuable(clazz, value, def)
              ?: throw NullPointerException("The value of type $clazz was not found successfully: $value.")

    /**************************************************************************
     *
     * Class
     *
     **************************************************************************/

    /**
     * * Finds the matching item from the specified condition from the given enum class.
     * * 将给定的枚举类从指定条件内查找匹配项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param predicate Condition
     * @param predicate 条件
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @throws IllegalArgumentException If [clazz] is not an enum class.
     * @throws IllegalArgumentException 如果 [clazz] 不是一个枚举类.
     */
    @JvmStatic
    @JvmOverloads
    fun <T> from(clazz: Class<T>, predicate: Predicate<Enum<*>>, def: Enum<*>? = null): Enum<*>? {
        if (!clazz.isEnum)
            throw IllegalArgumentException("The parameter class $clazz is not an enum type.")
        return clazz.enumConstants?.map { it as Enum<*> }?.find(predicate) ?: def
    }

    /**
     * * Finds the matching item from the specified [value] in the [Valuable] interface of the given enum class.
     * * 将给定的枚举类以它实现的 [Valuable] 接口中从指定的 [value] 内查找匹配项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param value Specified value
     * @param value 指定值
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @throws IllegalArgumentException If [clazz] is not an enum class.
     * @throws IllegalArgumentException 如果 [clazz] 不是一个枚举类.
     * @throws IllegalArgumentException If [clazz] does not implement the [Valuable] interface
     * @throws IllegalArgumentException 如果 [clazz] 未实现 [Valuable] 接口
     * @see [Valuable]
     * @see [Valuable.value]
     */
    @JvmStatic
    @JvmOverloads
    fun <V, T> fromValuable(clazz: Class<T>, value: V?, def: Enum<*>? = null): Enum<*>? where T : Enum<*>, T : Valuable<V> {
        if (!clazz.isEnum)
            throw IllegalArgumentException("The parameter class $clazz is not an enum type.")
        if (clazz.interfaces.find { Valuable::class.java.isAssignableFrom(it) } == null)
            throw IllegalArgumentException("Enum class $clazz does not implement the Valuable interface.")
        return clazz.enumConstants?.map { it as T }?.find { it.value == value } ?: def
    }

    /**
     * * Finds the matching item from the given enum name for the given enum class.
     * * 将给定的枚举类从指定枚举名称内查找匹配项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param name 名称
     * @param name 名称
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @throws IllegalArgumentException If [clazz] is not an enum class.
     * @throws IllegalArgumentException 如果 [clazz] 不是一个枚举类.
     * @see [Enum.name]
     */
    @JvmStatic
    @JvmOverloads
    fun <T> fromName(clazz: Class<T>, name: String, def: Enum<*>? = null): Enum<*>?
            = from(clazz, { it.name == name }, def)

    /**
     * * Finds the matching item from the given enum origin for the given enum class.
     * * 将给定的枚举类从指定枚举序数内查找匹配项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param origin Ordinal
     * @param origin 序数
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @throws IllegalArgumentException If [clazz] is not an enum class.
     * @throws IllegalArgumentException 如果 [clazz] 不是一个枚举类.
     * @see [Enum.ordinal]
     */
    @JvmStatic
    @JvmOverloads
    fun <T> fromOrigin(clazz: Class<T>, origin: Int, def: Enum<*>? = null): Enum<*>?
            = from(clazz, { it.ordinal == origin }, def)
}
