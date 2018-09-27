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
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Member

/**
 * ## FuzzyReflectMatcher (模糊反射匹配器)
 *
 * @see [FuzzyReflect]
 * @see [FuzzyReflectConstructorMatcher]
 * @see [FuzzyReflectMethodMatcher]
 * @see [FuzzyReflectFieldMatcher]
 * @author lgou2w
 */
abstract class FuzzyReflectMatcher<T>(
        reflect: FuzzyReflect,
        initialize: Collection<T>? = null
) : ReflectMatcher<T>(reflect, initialize) where T: AccessibleObject, T: Member {

    override fun with(block: Function<T, Boolean>): FuzzyReflectMatcher<T> {
        return super.with(block) as FuzzyReflectMatcher<T>
    }

    override fun withVisibilities(vararg visibilities: Visibility): FuzzyReflectMatcher<T> {
        return super.withVisibilities(*visibilities) as FuzzyReflectMatcher<T>
    }

    override fun withName(regex: String): FuzzyReflectMatcher<T> {
        return super.withName(regex) as FuzzyReflectMatcher<T>
    }

    override fun <A : Annotation> withAnnotation(annotation: Class<A>): FuzzyReflectMatcher<T> {
        return super.withAnnotation(annotation) as FuzzyReflectMatcher<T>
    }

    override fun <A : Annotation> withAnnotationIf(annotation: Class<A>, block: Predicate<A>): FuzzyReflectMatcher<T> {
        return super.withAnnotationIf(annotation, block) as FuzzyReflectMatcher<T>
    }

    abstract override fun withType(clazz: Class<*>): FuzzyReflectMatcher<T>

    abstract override fun withParams(vararg parameters: Class<*>): FuzzyReflectMatcher<T>
}
