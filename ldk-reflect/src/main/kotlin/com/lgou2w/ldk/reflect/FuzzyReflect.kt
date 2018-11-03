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
import java.lang.reflect.Method

/**
 * ## FuzzyReflect (模糊反射器)
 *
 * * Quick, convenient, and fuzzy matching to find reflection values ​​with various attributes.
 * * 快速、方便，以模糊的方式匹配查找具备各种属性的反射值.
 *
 * ### Sample:
 * ```kotlin
 * class My {
 *      fun say(msg: String) : Int {
 *          println(msg)
 *          return 0
 *      }
 * }
 * fun main(args: Array<String>) {
 *      val say = FuzzyReflect.of(My::class.java)
 *          .useMethodMatcher()                                 // Method matcher
 *          .withVisibilities(Visibility.PUBLIC)            // Public function
 *          .withType(Int::class.java)                           // Return Int
 *          .withParams(String::class.java)                 // Method parameters
 *          .withName("say")                                        // Method name
 *          .resultAccessorAs<My, Int>()                    // My is instance, Int is method return value

 *      val value = say.invoke(My(), "hello world")   // value = 0
 *      // "hello world" Printed
 * }
 * ```
 *
 * @see [Reflect]
 * @author lgou2w
 */
class FuzzyReflect private constructor(
        override val reference: Class<*>,
        override val isForceAccess: Boolean
) : Reflect {

    override fun useForceAccess(): FuzzyReflect {
        return FuzzyReflect(reference, true)
    }

    fun useConstructorMatcher(): FuzzyReflectConstructorMatcher<Any>
            = FuzzyReflectConstructorMatcher(this,
            @Suppress("UNCHECKED_CAST")
            constructors.map { it as Constructor<Any> })

    fun useMethodMatcher(): FuzzyReflectMethodMatcher
            = FuzzyReflectMethodMatcher(this, methods)

    fun useFieldMatcher(): FuzzyReflectFieldMatcher
            = FuzzyReflectFieldMatcher(this, fields)

    override val constructors: Set<Constructor<*>>
        get() = if (isForceAccess)
            LinkedHashSet((reference.declaredConstructors + reference.constructors).toMutableList())
        else
            LinkedHashSet(reference.constructors.toMutableList())

    override val methods: Set<Method>
        get() = if (isForceAccess)
            LinkedHashSet((reference.declaredMethods + reference.methods).toMutableList())
        else
            LinkedHashSet(reference.methods.toMutableList())

    override val fields: Set<Field>
        get() = if (isForceAccess)
            LinkedHashSet((reference.declaredFields + reference.fields).toMutableList())
        else
            LinkedHashSet(reference.fields.toMutableList())

    companion object {

        /**
         * * Create a fuzzy reflector from the given reference class [reference].
         * * 从给定的引用类 [reference] 创建一个模糊反射器.
         *
         * @param reference Reference class
         * @param reference 引用类
         * @param isForceAccess Whether to force access
         * @param isForceAccess 是否强制访问
         */
        @JvmStatic
        @JvmOverloads
        fun of(reference: Class<*>, isForceAccess: Boolean = false): FuzzyReflect {
            return FuzzyReflect(reference, isForceAccess)
        }

        /**
         * * Create a fuzzy reflector from the given reference object [reference].
         * * 从给定的引用对象 [reference] 创建一个模糊反射器.
         *
         * @param reference Reference object
         * @param reference 引用对象
         * @param isForceAccess Whether to force access
         * @param isForceAccess 是否强制访问
         */
        @JvmStatic
        @JvmOverloads
        fun of(reference: Any, isForceAccess: Boolean = false): FuzzyReflect {
            return FuzzyReflect(reference.javaClass, isForceAccess)
        }
    }
}
