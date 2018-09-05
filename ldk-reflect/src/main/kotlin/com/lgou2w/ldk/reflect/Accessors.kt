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

object Accessors {

    fun <T> ofConstructor(constructor: Constructor<T>): AccessorConstructor<T>
            = AccessorConstructorImpl(constructor)

    fun <T, R> ofMethod(method: Method): AccessorMethod<T, R>
            = AccessorMethodImpl(method)

    fun <T, R> ofField(field: Field): AccessorField<T, R>
            = AccessorFieldImpl(field)

    private class AccessorConstructorImpl<T>(
            override val source: Constructor<T>
    ) : AccessorConstructor<T> {
        override fun newInstance(vararg params: Any?): T {
            try {
                return source.newInstance(*params)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("无法使用构造函数.", e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException("发生内部错误.", e.cause)
            } catch (e: InstantiationException) {
                throw RuntimeException("无法实例化对象.", e)
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
                throw RuntimeException("无法使用反射器.", e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException("发生内部错误.", e.cause)
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
                throw RuntimeException("无法读取字段: $source", e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("无法使用反射器.", e)
            }
        }
        override fun set(instance: T?, value: R?) {
            try {
                source.set(instance, value)
            } catch (e: IllegalArgumentException) {
                throw RuntimeException("无法设置字段: $source 的值为: $value", e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("无法使用反射器.", e)
            }
        }
        override fun toString(): String {
            return "AccessorField(source=$source)"
        }
    }
}
