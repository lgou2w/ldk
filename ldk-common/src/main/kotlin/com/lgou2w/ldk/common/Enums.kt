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
     * * Find the matching enum item from the specified condition [predicate] from the given enum [clazz].
     * * 将给定的枚举类 [clazz] 从指定条件 [predicate] 内查找匹配枚举项.
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
     * * Find the matching enum item from the specified condition [predicate] from the given enum [clazz].
     * * 将给定的枚举类 [clazz] 从指定条件 [predicate] 内查找匹配枚举项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param predicate Condition
     * @param predicate 条件
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @throws [NoSuchElementException] If no matching enum item are found.
     * @throws [NoSuchElementException] 如果未找到匹配枚举项.
     * @since LDK 0.1.8-rc
     */
    @JvmStatic
    @JvmOverloads
    @Throws(NoSuchElementException::class)
    fun <T : Enum<T>> ofNotNull(clazz: Class<T>, predicate: Predicate<T>, def: T? = null): T
            = of(clazz, predicate, def)
              ?: throw NoSuchElementException("Cannot find type $clazz enumeration.")

    /**
     * * Find the matching enum item from the given enum [name] for the given enum [clazz].
     * * 将给定的枚举类 [clazz] 从指定枚举名称 [name] 内查找匹配枚举项.
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
     * * Find the matching enum item from the given enum [name] for the given enum [clazz].
     * * 将给定的枚举类 [clazz] 从指定枚举名称 [name] 内查找匹配枚举项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param name 名称
     * @param name 名称
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @throws [NoSuchElementException] If no matching enum item are found.
     * @throws [NoSuchElementException] 如果未找到匹配枚举项.
     * @see [Enum.name]
     * @since LDK 0.1.8-rc
     */
    @JvmStatic
    @JvmOverloads
    @Throws(NoSuchElementException::class)
    fun <T : Enum<T>> ofNameNotNull(clazz: Class<T>, name: String, def: T? = null): T
            = ofName(clazz, name, def)
              ?: throw NoSuchElementException("Cannot find type $clazz enumeration name: $name")

    @JvmStatic
    @JvmOverloads
    @Deprecated("RENAME", replaceWith = ReplaceWith("ofOrdinal"))
    fun <T : Enum<T>> ofOrigin(clazz: Class<T>, ordinal: Int, def: T? = null): T?
            = ofOrdinal(clazz, ordinal, def)

    /**
     * * Find the matching enum item from the given enum [ordinal] for the given enum [clazz].
     * * 将给定的枚举类 [clazz] 从指定枚举序数 [ordinal] 内查找匹配枚举项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param ordinal Ordinal
     * @param ordinal 序数
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @see [Enum.ordinal]
     * @since LDK 0.1.8-rc
     */
    @JvmStatic
    @JvmOverloads
    fun <T : Enum<T>> ofOrdinal(clazz: Class<T>, ordinal: Int, def: T? = null): T?
            = of(clazz, { it.ordinal == ordinal }, def)

    @JvmStatic
    @JvmOverloads
    @Throws(NoSuchElementException::class)
    @Deprecated("RENAME", replaceWith = ReplaceWith("ofOrdinalNotNull"))
    fun <T : Enum<T>> ofOriginNotNull(clazz: Class<T>, ordinal: Int, def: T? = null): T
            = ofOrdinalNotNull(clazz, ordinal, def)

    /**
     * * Find the matching enum item from the given enum [ordinal] for the given enum [clazz].
     * * 将给定的枚举类 [clazz] 从指定枚举序数 [ordinal] 内查找匹配枚举项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param ordinal Ordinal
     * @param ordinal 序数
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @throws [NoSuchElementException] If no matching enum item are found.
     * @throws [NoSuchElementException] 如果未找到匹配枚举项.
     * @see [Enum.ordinal]
     * @since LDK 0.1.8-rc
     */
    @JvmStatic
    @JvmOverloads
    @Throws(NoSuchElementException::class)
    fun <T : Enum<T>> ofOrdinalNotNull(clazz: Class<T>, ordinal: Int, def: T? = null): T
            = ofOrdinal(clazz, ordinal, def)
              ?: throw NoSuchElementException("Cannot find type $clazz enumeration ordinal: $ordinal")

    /**
     * * Find the matching enum item from the specified [value] in the [Valuable] interface of the given enum [clazz].
     * * 将给定的枚举类 [clazz] 以它实现的 [Valuable] 接口中从指定的 [value] 内查找匹配枚举项.
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
     * * Find the matching enum item from the specified [value] in the [Valuable] interface of the given enum [clazz].
     * * 将给定的枚举类 [clazz] 以它实现的 [Valuable] 接口中从指定的 [value] 内查找匹配枚举项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param value Specified value
     * @param value 指定值
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @throws [NoSuchElementException] If no matching enum item are found.
     * @throws [NoSuchElementException] 如果未找到匹配枚举项.
     * @see [Valuable]
     * @see [Valuable.value]
     * @see [ofValuable]
     */
    @JvmStatic
    @JvmOverloads
    @Throws(NoSuchElementException::class)
    fun <V, T> ofValuableNotNull(clazz: Class<T>, value: V?, def: T? = null): T where T : Enum<T>, T : Valuable<V>
            = ofValuable(clazz, value, def)
              ?: throw NoSuchElementException("Cannot find type $clazz enumeration value: $value")

    /**************************************************************************
     *
     * Class
     *
     **************************************************************************/

    /**
     * * Find the matching enum item from the specified condition [predicate] from the given enum [clazz].
     * * 将给定的枚举类 [clazz] 从指定条件 [predicate] 内查找匹配枚举项.
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
     * * Finds the matching enum item from the specified [value] in the [Valuable] interface of the given enum [clazz].
     * * 将给定的枚举类 [clazz] 以它实现的 [Valuable] 接口中从指定的 [value] 内查找匹配枚举项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param value Specified value
     * @param value 指定值
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @throws IllegalArgumentException If [clazz] is not an enum class.
     * @throws IllegalArgumentException 如果 [clazz] 不是一个枚举类.
     * @see [Valuable]
     * @see [Valuable.value]
     */
    @JvmStatic
    @JvmOverloads
    fun <V, T> fromValuable(clazz: Class<T>, value: V?, def: Enum<*>? = null): Enum<*>? where T : Valuable<V> {
        if (!clazz.isEnum)
            throw IllegalArgumentException("The parameter class $clazz is not an enum type.")
        return clazz.enumConstants?.map { it as T }?.find { it.value == value } as? Enum<*> ?: def
    }

    /**
     * * Finds the matching enum item from the given enum [name] for the given enum [clazz].
     * * 将给定的枚举类 [clazz] 从指定枚举名称 [name] 内查找匹配枚举项.
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

    @JvmStatic
    @JvmOverloads
    @Deprecated("RENAME", replaceWith = ReplaceWith("fromOrdinal"))
    fun <T> fromOrigin(clazz: Class<T>, ordinal: Int, def: Enum<*>? = null): Enum<*>?
            = fromOrdinal(clazz, ordinal, def)

    /**
     * * Finds the matching enum item from the given enum [ordinal] for the given enum [clazz].
     * * 将给定的枚举类 [clazz] 从指定枚举序数 [ordinal] 内查找匹配枚举项.
     *
     * @param clazz Enum class
     * @param clazz 枚举类
     * @param ordinal Ordinal
     * @param ordinal 序数
     * @param def Default value, The default is `null`
     * @param def 默认值, 默认为 `null` 值
     * @throws IllegalArgumentException If [clazz] is not an enum class.
     * @throws IllegalArgumentException 如果 [clazz] 不是一个枚举类.
     * @see [Enum.ordinal]
     * @since LDK 0.1.8-rc
     */
    @JvmStatic
    @JvmOverloads
    fun <T> fromOrdinal(clazz: Class<T>, ordinal: Int, def: Enum<*>? = null): Enum<*>?
            = from(clazz, { it.ordinal == ordinal }, def)
}
