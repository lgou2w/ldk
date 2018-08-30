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

package com.lgou2w.ldk.common

object Enums {

    /**************************************************************************
     *
     * Enum Class
     *
     **************************************************************************/

    @JvmOverloads
    fun <T : Enum<T>> of(clazz: Class<T>, predicate: Predicate<T>, def: T? = null) : T? {
        return clazz.enumConstants?.find(predicate) ?: def
    }

    @JvmOverloads
    fun <T : Enum<T>> ofName(clazz: Class<T>, name: String, def: T? = null) : T?
            = of(clazz, { it.name == name }, def)

    @JvmOverloads
    fun <T : Enum<T>> ofOrigin(clazz: Class<T>, origin: Int, def: T? = null) : T?
            = of(clazz, { it.ordinal == origin }, def)

    @JvmOverloads
    fun <V, T> ofValuable(clazz: Class<T>, value: V?, def: T? = null) : T? where T : Enum<T>, T : Valuable<V>
            = of(clazz, { it.value == value }, def)

    /**************************************************************************
     *
     * Class
     *
     **************************************************************************/

    @JvmOverloads
    fun <T> from(clazz: Class<T>, predicate: Predicate<Enum<*>>, def: Enum<*>? = null) : Enum<*>? {
        if (!clazz.isEnum)
            throw IllegalArgumentException("参数类 $clazz 不是一个枚举类型.")
        return clazz.enumConstants?.map { it as Enum<*> }?.find(predicate) ?: def
    }

    @JvmOverloads
    fun <V, T> fromValuable(clazz: Class<T>, value: V?, def: Enum<*>? = null) : Enum<*>? where T : Enum<*>, T : Valuable<V> {
        if (!clazz.isEnum)
            throw IllegalArgumentException("参数类 $clazz 不是一个枚举类型.")
        return clazz.enumConstants?.map { it as T }?.find { it.value == value } ?: def
    }

    @JvmOverloads
    fun <T> fromName(clazz: Class<T>, name: String, def: Enum<*>? = null) : Enum<*>?
            = from(clazz, { it.name == name }, def)

    @JvmOverloads
    fun <T> fromOrigin(clazz: Class<T>, origin: Int, def: Enum<*>? = null) : Enum<*>?
            = from(clazz, { it.ordinal == origin }, def)
}
