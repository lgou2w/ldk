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

import com.lgou2w.ldk.common.Function
import com.lgou2w.ldk.common.Predicate
import com.lgou2w.ldk.common.letIfNotNull
import java.lang.reflect.Method

class FuzzyReflectMethodMatcher(
        reflect: FuzzyReflect,
        initialize: Collection<Method>? = null
) : FuzzyReflectMatcher<Method>(reflect, initialize) {

    override fun with(block: Function<Method, Boolean>): FuzzyReflectMethodMatcher {
        return super.with(block) as FuzzyReflectMethodMatcher
    }

    override fun withVisibilities(vararg visibilities: Visibility): FuzzyReflectMethodMatcher {
        return super.withVisibilities(*visibilities) as FuzzyReflectMethodMatcher
    }

    override fun withName(regex: String): FuzzyReflectMethodMatcher {
        return super.withName(regex) as FuzzyReflectMethodMatcher
    }

    override fun <A : Annotation> withAnnotation(annotation: Class<A>): FuzzyReflectMethodMatcher {
        return super.withAnnotation(annotation) as FuzzyReflectMethodMatcher
    }

    override fun <A : Annotation> withAnnotationIf(annotation: Class<A>, block: Predicate<A>): FuzzyReflectMethodMatcher {
        return super.withAnnotationIf(annotation, block) as FuzzyReflectMethodMatcher
    }

    override fun withType(clazz: Class<*>): FuzzyReflectMethodMatcher {
        val primitiveType = DataType.ofPrimitive(clazz)
        values = values.filter { primitiveType.isAssignableFrom(it.returnType) }.toMutableList()
        return this
    }

    override fun withParams(vararg parameters: Class<*>): FuzzyReflectMethodMatcher {
        val primitiveTypes = DataType.ofPrimitive(parameters)
        return with { DataType.compare(it.parameterTypes, primitiveTypes) }
    }

    override fun resultAccessors(): List<AccessorMethod<Any, Any>>
            = results().map { Accessors.ofMethod<Any, Any>(it) }

    override fun resultAccessor(): AccessorMethod<Any, Any>
            = resultAccessorAs()

    override fun resultAccessorOrNull(): AccessorMethod<Any, Any>?
            = resultOrNull()?.letIfNotNull { Accessors.ofMethod<Any, Any>(this) }

    fun <T, R> resultAccessorAs(): AccessorMethod<T, R>
            = Accessors.ofMethod(result())
}