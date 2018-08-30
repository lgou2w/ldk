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

package com.lgou2w.ldk.common

/**************************************************************************
 *
 * Type alias
 *
 **************************************************************************/

typealias Predicate<T> = (T) -> Boolean
typealias Function<T, R> = (T) -> R
typealias BiFunction<T, U, R> = (T, U) -> R
typealias Consumer<T> = (T) -> Unit
typealias Applicator<T> = T.() -> Unit
typealias ApplicatorFunction<T, R> = T.() -> R
typealias Callable<T> = () -> T

/**************************************************************************
 *
 * Extended functions
 *
 **************************************************************************/

@JvmOverloads
@Throws(NullPointerException::class)
fun <T> T?.notNull(cause: String = "NPE"): T {
    if (this == null)
        throw NullPointerException(cause)
    return this
}

inline fun <T> T?.applyIfNotNull(block: Applicator<T>) {
    if (this != null)
        block(this)
}

inline fun <T, R> T?.letIfNotNull(block: ApplicatorFunction<T, R>) : R? {
    if (this != null)
        return block(this)
    return null
}

/**
 * Returns true if this is true.
 */
fun Boolean?.isTrue() : Boolean
        = this != null && this

/**
 * Returns true if this is null or false.
 */
fun Boolean?.isFlase() : Boolean
        = this == null || !this

/**
 * Returns true if this is null or true.
 */
fun Boolean?.orTrue() : Boolean
        = this == null || this == true

/**
 * Returns false if this is null or false.
 */
fun Boolean?.orFalse() : Boolean
        = !(this == null || this == false)

fun <T, C: Comparable<T>> C.isLater(other: T) : Boolean
        = compareTo(other) > 0

fun <T, C: Comparable<T>> C.isOrLater(other: T) : Boolean
        = compareTo(other) >= 0

fun <T, C: Comparable<T>> C.isRange(min: T, max: T) : Boolean
        = compareTo(min) > 0 && compareTo(max) < 0

fun <T, C: Comparable<T>> C.isOrRange(min: T, max: T) : Boolean
        = compareTo(min) >= 0 && compareTo(max) <= 0

/**************************************************************************
 *
 * Delegates
 *
 **************************************************************************/

fun <T> lazyClass(initializer: Callable<Class<T>>)
        = LazyClass(initializer)

fun lazyAnyClass(initializer: Callable<Class<*>>)
        = LazyAnyClass(initializer)

fun lazyAnyOrNullClass(initializer: Callable<Class<*>?>)
        = LazyAnyOrNullClass(initializer)
