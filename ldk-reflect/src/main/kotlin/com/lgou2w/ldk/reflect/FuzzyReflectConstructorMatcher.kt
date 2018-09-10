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
import java.lang.reflect.Constructor

class FuzzyReflectConstructorMatcher<T: Any>(
        reflect: FuzzyReflect,
        initialize: Collection<Constructor<T>>? = null
) : FuzzyReflectMatcher<Constructor<T>>(reflect, initialize) {

    override fun with(block: Function<Constructor<T>, Boolean>): FuzzyReflectConstructorMatcher<T> {
        return super.with(block) as FuzzyReflectConstructorMatcher
    }

    override fun withVisibilities(vararg visibilities: Visibility): FuzzyReflectConstructorMatcher<T> {
        return super.withVisibilities(*visibilities) as FuzzyReflectConstructorMatcher
    }

    override fun withName(regex: String): FuzzyReflectConstructorMatcher<T> {
        return this // Constructor does not support nameable
    }

    override fun <A : Annotation> withAnnotation(annotation: Class<A>): FuzzyReflectConstructorMatcher<T> {
        return super.withAnnotation(annotation) as FuzzyReflectConstructorMatcher<T>
    }

    override fun <A : Annotation> withAnnotationIf(annotation: Class<A>, block: Predicate<A>): FuzzyReflectConstructorMatcher<T> {
        return super.withAnnotationIf(annotation, block) as FuzzyReflectConstructorMatcher<T>
    }

    override fun withType(clazz: Class<*>): FuzzyReflectConstructorMatcher<T> {
        return this // Constructor does not need a return type
    }

    override fun withParams(vararg parameters: Class<*>): FuzzyReflectConstructorMatcher<T> {
        val primitiveTypes = DataType.ofPrimitive(parameters)
        return with { DataType.compare(it.parameterTypes, primitiveTypes) }
    }

    override fun resultAccessors(): List<AccessorConstructor<T>>
            = results().map { Accessors.ofConstructor(it) }

    override fun resultAccessor(): AccessorConstructor<T>
            = resultAccessorAs()

    override fun resultAccessorOrNull(): AccessorConstructor<T>?
            = resultOrNull()?.letIfNotNull { Accessors.ofConstructor(this) }

    @Suppress("UNCHECKED_CAST")
    fun <R> resultAccessorAs(): AccessorConstructor<R>
            = Accessors.ofConstructor(result() as Constructor<R>)
}
