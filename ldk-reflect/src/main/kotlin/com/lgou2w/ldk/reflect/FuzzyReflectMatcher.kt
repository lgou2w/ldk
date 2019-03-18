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

import com.lgou2w.ldk.common.BiFunction
import com.lgou2w.ldk.common.Callable
import com.lgou2w.ldk.common.Function
import com.lgou2w.ldk.common.Predicate
import com.lgou2w.ldk.common.letIfNotNull
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Member
import java.util.regex.Pattern

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
) where T: AccessibleObject, T: Member {

    protected var values: MutableList<T> =
            if (initialize != null) ArrayList(initialize)
            else ArrayList()

    /**
     * * Match reflection values ​​from a given [predicate] condition.
     * * 从给定的谓词条件 [predicate] 中匹配反射值.
     *
     * @param predicate Condition
     * @param predicate 条件
     */
    open fun with(predicate: Predicate<T>): FuzzyReflectMatcher<T> {
        if (values.isEmpty()) // if is empty
            return this
        values = values.asSequence().filter(predicate).toMutableList()
        return this
    }

    /**
     * * Initialize the given value first, then match the reflection value from the given [predicate] condition.
     * * 首先初始化给定值, 然后从给定的谓词条件 [predicate] 中匹配反射值.
     *
     * @param initialize Initialize value
     * @param initialize 初始化值
     * @param predicate Condition
     * @param predicate 条件
     * @since LDK 0.1.7-rc3
     */
    open fun <U> with(initialize: Callable<U>, predicate: BiFunction<T, U, Boolean>): FuzzyReflectMatcher<T> {
        if (values.isEmpty()) // if is empty
            return this
        val initializeValue = initialize()
        values = values.asSequence().filter { predicate(it, initializeValue) }.toMutableList()
        return this
    }

    /**
     * * Match reflection values ​​from a given [visibilities].
     * * 从给定的可见性 [visibilities] 中匹配反射值.
     *
     * @param visibilities Visibilities
     * @param visibilities 可见性
     */
    open fun withVisibilities(vararg visibilities: Visibility): FuzzyReflectMatcher<T>
            = with { accessible -> visibilities.all { it.isMatches(accessible) } }

    /**
     * * Match reflection values ​​from the given name rule [regex].
     * * 从给定的名称规则 [regex] 中匹配反射值.
     *
     * @param regex Name regex
     * @param regex 名称规则
     */
    open fun withName(regex: String): FuzzyReflectMatcher<T>
            = with({ Pattern.compile(regex) }) { it, pattern -> pattern.matcher(it.name).matches() }

    /**
     * * Match reflection values ​​from the given [annotation] class.
     * * 从给定的注解类 [annotation] 匹配反射值.
     *
     * @param annotation Annotation class
     * @param annotation 注解类
     */
    open fun <A: Annotation> withAnnotation(annotation: Class<A>): FuzzyReflectMatcher<T>
            = with { it.getAnnotation(annotation) != null }

    /**
     * * Matches the reflection value from the given [annotation] class and with the specified predicate condition [block].
     * * 从给定的注解类 [annotation] 并以指定谓词条件 [block] 匹配反射值.
     *
     * @param annotation Annotation class
     * @param annotation 注解类
     * @param block Condition
     * @param block 条件
     */
    open fun <A: Annotation> withAnnotationIf(annotation: Class<A>, block: Predicate<A>): FuzzyReflectMatcher<T>
            = with { it.getAnnotation(annotation).letIfNotNull(block) == true }

    /**
     * * Match reflection values ​​from a given type [clazz].
     * * 从给定的类型匹配反射值.
     *
     * @param clazz Class
     * @param clazz 类
     */
    abstract fun withType(clazz: Class<*>): FuzzyReflectMatcher<T>

    /**
     * * Matches the reflection value from the given parameter type [parameters].
     * * 从给定的参数类型 [parameters] 匹配反射值.
     *
     * @param parameters Parameter
     * @param parameters 参数
     */
    abstract fun withParams(vararg parameters: Class<*>): FuzzyReflectMatcher<T>

    /**
     * * Matches the reflection value from the given parameter [count].
     * * 从给定的参数数量 [count] 匹配反射值.
     *
     * @param count Parameter count
     * @param count 参数数量
     * @since LDK 0.1.8-rc
     */
    abstract fun withParamsCount(count: Int): FuzzyReflectMatcher<T>

    /**
     * * Result converter.
     * * 结果转变器.
     */
    protected open val resultTransform: Function<T, T> = {
        if (reflect.isForceAccess && !it.isAccessible)
            it.isAccessible = true
        it
    }

    /**
     * * Get the result set of this fuzzy reflect matcher.
     * * 获取此模糊反射匹配器的结果集合.
     */
    open fun results(): List<T>
            = values.map(resultTransform)

    /**
     * * Get the first valid result of this fuzzy reflection matcher.
     * * 获取此模糊反射匹配器的第一个有效结果.
     *
     * @throws NoSuchElementException If the match result is empty.
     * @throws NoSuchElementException 如果匹配结果为空.
     */
    @Throws(NoSuchElementException::class)
    open fun result(): T
            = results().first()

    /**
     * * Get the first valid result of this fuzzy reflection matcher or `null`.
     * * 获取此模糊反射匹配器的第一个有效结果或 `null`.
     */
    open fun resultOrNull(): T?
            = results().firstOrNull()

    /**
     * * Get the result accessor collection of this fuzzy reflection matcher.
     * * 获取此模糊反射匹配器的结果访问器集合.
     */
    abstract fun resultAccessors(): List<Accessor<T>>

    /**
     * * Get the first valid result accessor for this fuzzy reflection matcher.
     * * 获取此模糊反射匹配器的第一个有效结果访问器.
     *
     * @throws NoSuchElementException If the match result is empty.
     * @throws NoSuchElementException 如果匹配结果为空.
     */
    abstract fun resultAccessor(): Accessor<T>

    /**
     * * Get the first valid result accessor for this fuzzy reflection matcher or `null`.
     * * 获取此模糊反射匹配器的第一个有效结果访问器或 `null`.
     */
    abstract fun resultAccessorOrNull(): Accessor<T>?
}
