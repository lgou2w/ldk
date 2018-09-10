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
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Member
import java.util.*
import java.util.regex.Pattern

abstract class ReflectMatcher<T>(
        val reflect: Reflect,
        initialize: Collection<T>? = null
) where T: AccessibleObject, T: Member {

    protected var values: MutableList<T> =
            if (initialize != null) ArrayList(initialize)
            else ArrayList()

    open fun with(block: Function<T, Boolean>): ReflectMatcher<T> {
        values = values.filter(block).toMutableList()
        return this
    }

    open fun withVisibilities(vararg visibilities: Visibility): ReflectMatcher<T>
            = with { accessible -> visibilities.all { it.isMatches(accessible) } }

    open fun withName(regex: String): ReflectMatcher<T>
            = with { Pattern.compile(regex).matcher(it.name).matches() }

    open fun <A: Annotation> withAnnotation(annotation: Class<A>): ReflectMatcher<T>
            = with { it.getAnnotation(annotation) != null }

    open fun <A: Annotation> withAnnotationIf(annotation: Class<A>, block: Predicate<A>): ReflectMatcher<T>
            = with { it.getAnnotation(annotation).letIfNotNull(block) == true }

    abstract fun withType(clazz: Class<*>): ReflectMatcher<T>

    abstract fun withParams(vararg parameters: Class<*>): ReflectMatcher<T>

    protected open val resultTransform: Function<T, T> = {
        if (reflect.isForceAccess && !it.isAccessible)
            it.isAccessible = true
        it
    }

    open fun results(): List<T>
            = values.map(resultTransform)

    open fun result(): T
            = results().first()

    open fun resultOrNull(): T?
            = results().firstOrNull()

    abstract fun resultAccessors(): List<Accessor<T>>

    abstract fun resultAccessor(): Accessor<T>

    abstract fun resultAccessorOrNull(): Accessor<T>?
}
