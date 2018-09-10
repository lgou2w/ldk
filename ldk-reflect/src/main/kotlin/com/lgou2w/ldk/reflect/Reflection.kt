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

import java.util.*

abstract class Reflection private constructor() {

    @JvmOverloads
    open fun getCallerClass(depth: Int? = null) : Class<*>? {
        return null
    }

    fun getCallerClasses(depth: Int? = null) : List<Class<*>>
            = getCallerClasses(null, depth)

    @JvmOverloads
    open fun getCallerClasses(expected: Class<*>? = null, depth: Int? = null) : List<Class<*>> {
        return emptyList()
    }

    companion object {

        @JvmStatic
        @Deprecated("sun.reflect.Reflection", ReplaceWith("safe"))
        fun sun() : Reflection
                = SunReflection()

        @JvmStatic
        @Deprecated("It is recommended to use safe to get.", ReplaceWith("safe"))
        fun thread() : Reflection
                = ThreadReflection()

        @JvmStatic
        private var SAFE : Reflection? = null

        @JvmStatic
        fun safe() : Reflection {
            if (SAFE == null) {
                var thread = true
                try {
                    Class.forName("sun.reflect.Reflection")
                    SAFE = SunReflection()
                    SAFE?.getCallerClass(1)  // test method
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
        override fun getCallerClasses(expected: Class<*>?, depth: Int?): List<Class<*>> {
            val callerClasses = ArrayList<Class<*>>()
            try {
                @Suppress("DEPRECATION")
                for (index in 0 until (depth?.plus(1) ?: 0x7FFFFFFF)) try {
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
        override fun getCallerClasses(expected: Class<*>?, depth: Int?): List<Class<*>> {
            val elements = Thread.currentThread().stackTrace
            val callerClasses = ArrayList<Class<*>>()
            for (index in 0 until Math.min(elements.size, depth?.plus(1) ?: 0x7FFFFFFF)) try {
                val clazz = Class.forName(elements[index].className) ?: break
                if (expected != null && expected.isAssignableFrom(clazz)) callerClasses.add(clazz)
                else if (expected == null) callerClasses.add(clazz)
            } catch (e: Exception) {
            }
            return if (callerClasses.isEmpty()) emptyList() else callerClasses
        }
    }
}
