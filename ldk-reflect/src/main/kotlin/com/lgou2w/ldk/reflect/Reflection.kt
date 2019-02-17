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

package com.lgou2w.ldk.reflect

/**
 * ## Reflection
 *
 * ### Sample:
 * ```kotlin
 * package my.app
 * class App {
 *   fun test() {
 *     Reflection.safe().getCallerClasses(10).forEach { println(it) }
 *   }
 * }
 * fun main(args: Array<String>) {
 *   App().test()
 *   // class sun.reflect.Reflection | java.lang.Thread
 *   // class com.lgou2w.ldk.reflect.Reflection$SunReflection | ThreadReflection
 *   // class com.lgou2w.ldk.reflect.Reflection
 *   // class my.app.App
 *   // ...
 * }
 * ```
 *
 * @see [com.lgou2w.ldk.reflect.Reflection.safe]
 * @see [getCallerClass]
 * @see [getCallerClasses]
 * @author lgou2w
 */
abstract class Reflection private constructor() {

    /**
     * * Get the caller class for the given depth [depth].
     * * 获取给定深度 [depth] 的调用者类.
     *
     * @param depth Depth
     * @param depth 深度
     */
    @JvmOverloads
    open fun getCallerClass(depth: Int? = null) : Class<*>? {
        return null
    }

    /**
     * * Get the expected class from `0` to the given depth [depth].
     * * 获取从 `0` 到给定深度 [depth] 的预期类.
     *
     * @param depth Depth
     * @param depth 深度
     * @param expected Expected type
     * @param expected 预期类型
     */
    @JvmOverloads
    open fun getCallerClasses(depth: Int? = null, expected: Class<*>? = null) : List<Class<*>> {
        return emptyList()
    }

    companion object {

        /**
         * * Get the wrapped reflection implemented by the underlying `sun.reflect.Reflection` of `Sun`.
         * * 获取由 `Sun` 的底层 `sun.reflect.Reflection` 实现的包装反射.
         *
         * > `WARNING` : This depends on whether the `JDK` is reserved, please use [safe] method.
         *
         * @see [safe]
         */
        @JvmStatic
        @Deprecated("sun.reflect.Reflection", ReplaceWith("safe"))
        fun sun() : Reflection
                = SunReflection()

        /**
         * * Get the wrapped reflection implemented by the stack information of `Thread`.
         * * 获取由 `Thread` 的堆栈信息实现的包装反射.
         *
         * @see [safe]
         */
        @JvmStatic
        @Deprecated("It is recommended to use safe to get.", ReplaceWith("safe"))
        fun thread() : Reflection
                = ThreadReflection()

        @JvmStatic
        private var SAFE : Reflection? = null

        /**
         * * Give preference to the underlying implementation of `Sun`, if not available then use the wrapped reflection of the `Thread` stack information.
         * * 优先使用 `Sun` 的底层实现, 如果不可用那么使用 `Thread` 的堆栈信息实现的包装反射.
         */
        @JvmStatic
        fun safe() : Reflection {
            if (SAFE == null) {
                var thread = true
                try {
                    Class.forName("sun.reflect.Reflection")
                    SAFE = SunReflection()
                    SAFE?.getCallerClasses(depth = 1)  // test method
                    thread = false
                } catch (e: ClassNotFoundException) {
                } catch (e: NoSuchMethodException) {
                } catch (e: Exception) {
                } finally {
                    if (thread)
                        SAFE = ThreadReflection()
                }
            }
            return SAFE!!
        }

        @JvmStatic
        private fun safeDepth(depth: Int?) : Int {
            return if (depth == 0x7FFFFFFF)
                depth
            else
                depth?.plus(1) ?: 0x7FFFFFFF
        }
    }

    private class SunReflection : Reflection() {
        override fun getCallerClass(depth: Int?): Class<*>? {
            return try {
                @Suppress("DEPRECATION")
                sun.reflect.Reflection.getCallerClass(depth ?: 0)
            } catch (e: NoSuchMethodException) {
                try {
                    sun.reflect.Reflection.getCallerClass()
                } catch (e1: NoSuchMethodException) {
                    null
                }
            }
        }
        override fun getCallerClasses(depth: Int?, expected: Class<*>?): List<Class<*>> {
            val callerClasses = ArrayList<Class<*>>()
            try {
                @Suppress("DEPRECATION")
                for (index in 0 until safeDepth(depth)) try {
                    val clazz = sun.reflect.Reflection.getCallerClass(index) ?: break
                    if (expected != null && expected.isAssignableFrom(clazz)) callerClasses.add(clazz)
                    else if (expected == null) callerClasses.add(clazz)
                } catch (e: Exception) {
                }
            } catch (e: NoSuchMethodException) {
                try {
                    callerClasses.add(sun.reflect.Reflection.getCallerClass())
                } catch (e1: NoSuchMethodException) {
                }
            }
            return if (callerClasses.isEmpty()) emptyList() else callerClasses
        }
    }

    private class ThreadReflection : Reflection() {
        override fun getCallerClass(depth: Int?): Class<*>? {
            val elements = Thread.currentThread().stackTrace
            val className = elements.getOrNull(depth ?: 0)?.className
            return try {
                Class.forName(className)
            } catch (e: ClassNotFoundException) {
                null
            }
        }
        override fun getCallerClasses(depth: Int?, expected: Class<*>?): List<Class<*>> {
            val elements = Thread.currentThread().stackTrace
            val callerClasses = ArrayList<Class<*>>()
            for (index in 0 until Math.min(elements.size, safeDepth(depth))) try {
                val clazz = Class.forName(elements[index].className) ?: break
                if (expected != null && expected.isAssignableFrom(clazz)) callerClasses.add(clazz)
                else if (expected == null) callerClasses.add(clazz)
            } catch (e: Exception) {
            }
            return if (callerClasses.isEmpty()) emptyList() else callerClasses
        }
    }
}
