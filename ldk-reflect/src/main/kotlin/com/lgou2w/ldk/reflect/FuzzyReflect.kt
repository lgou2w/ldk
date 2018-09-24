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

class FuzzyReflect private constructor(
        private val reference: Class<*>,
        override val isForceAccess: Boolean
) : Reflect {

    override fun useForceAccess(): FuzzyReflect {
        return FuzzyReflect(reference, true)
    }

    override fun useConstructorMatcher(): FuzzyReflectConstructorMatcher<Any>
            = FuzzyReflectConstructorMatcher(this,
            @Suppress("UNCHECKED_CAST")
            constructors.map { it as Constructor<Any> })

    override fun useMethodMatcher(): FuzzyReflectMethodMatcher
            = FuzzyReflectMethodMatcher(this, methods)

    override fun useFieldMatcher(): FuzzyReflectFieldMatcher
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

        @JvmStatic
        @JvmOverloads
        fun of(reference: Class<*>, isForceAccess: Boolean = false): FuzzyReflect {
            return FuzzyReflect(reference, isForceAccess)
        }

        @JvmStatic
        @JvmOverloads
        fun of(reference: Any, isForceAccess: Boolean = false): FuzzyReflect {
            return FuzzyReflect(reference.javaClass, isForceAccess)
        }
    }
}
