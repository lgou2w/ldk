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

package com.lgou2w.ldk.reflect

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * ## Accessors (访问器工具)
 *
 * * An accessor object can be created from a given construct, method, or field.
 * * 可以通过给定的构造、方法或字段创建出访问器对象.
 *
 * @see [Accessor]
 * @see [AccessorConstructor]
 * @see [AccessorMethod]
 * @see [AccessorField]
 * @author lgou2w
 */
object Accessors {

    /**
     * * Create a constructor accessor for the given constructor [constructor].
     * * 将给定的构造函数 [constructor] 创建一个构造访问器.
     *
     * @param constructor Constructor
     * @param constructor 构造函数
     */
    @JvmStatic
    fun <T> ofConstructor(constructor: Constructor<T>): AccessorConstructor<T>
            = AccessorConstructorImpl(constructor)

    /**
     * * Create a method accessor for the given method [method].
     * * 将给定的方法 [method] 创建一个方法访问器.
     *
     * @param method Method
     * @param method 方法
     */
    @JvmStatic
    fun <T, R> ofMethod(method: Method): AccessorMethod<T, R>
            = AccessorMethodImpl(method)

    /**
     * * Create a field accessor for the given field [field].
     * * 将给定的字段 [field] 创建一个字段访问器.
     *
     * @param field Field
     * @param field 字段
     */
    @JvmStatic
    fun <T, R> ofField(field: Field): AccessorField<T, R>
            = AccessorFieldImpl(field)

    private class AccessorConstructorImpl<T>(
            override val source: Constructor<T>
    ) : AccessorConstructor<T> {
        override fun newInstance(vararg params: Any?): T {
            try {
                return source.newInstance(*params)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Unable to use constructor.", e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException("An internal error has occurred.", e.cause)
            } catch (e: InstantiationException) {
                throw RuntimeException("Unable to instantiate object.", e)
            }
        }
        override fun toString(): String {
            return "AccessorConstructor(source=$source)"
        }
    }

    private class AccessorMethodImpl<T, R>(
            override val source: Method
    ) : AccessorMethod<T, R> {
        override fun invoke(instance: T?, vararg params: Any?): R? {
            try {
                @Suppress("UNCHECKED_CAST")
                return source.invoke(instance, *params) as R?
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Unable to use reflector.", e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException("An internal error has occurred.", e.cause)
            }
        }
        override fun toString(): String {
            return "AccessorMethod(source=$source)"
        }
    }

    private class AccessorFieldImpl<T, R>(
            override val source: Field
    ) : AccessorField<T, R> {
        override fun get(instance: T?): R? {
            try {
                @Suppress("UNCHECKED_CAST")
                return source.get(instance) as R?
            } catch (e: IllegalArgumentException) {
                throw RuntimeException("Unable to read field: $source", e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Unable to use reflector.", e)
            }
        }
        override fun set(instance: T?, value: R?) {
            try {
                source.set(instance, value)
            } catch (e: IllegalArgumentException) {
                throw RuntimeException("Unable to set the field: The value of $source is: $value", e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Unable to use reflector.", e)
            }
        }
        override fun toString(): String {
            return "AccessorField(source=$source)"
        }
    }
}
