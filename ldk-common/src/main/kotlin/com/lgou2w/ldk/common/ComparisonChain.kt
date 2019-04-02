/*
 * Copyright (C) 2009 The Guava Authors
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

/*
 * Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>
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

/*
 *  Modify: Guava ComparisonChain Java -> Kotlin
 *  by MoonLake on 24/08/2017
 */

/**
 * * ## ComparisonChain (链式比较器)
 *
 * * Chain comparison separated by Google's `Guava` library.
 *      Used to compare the properties of the current class and an object.
 * * 由 Google 的 `Guava` 库分离出来的链式比较器. 用来比较当前类和一个对象的属性.
 *
 * ### Sample:
 * ```kotlin
 * class Entity(val id: Int, val name: String) : Comparable<Entity> {
 *      override fun compareTo(other: Entity): Int {
 *          return ComparisonChain.start()
 *              .compare(id, other.id)
 *              .compare(name, other.name)
 *              .result
 *      }
 * }
 * ```
 *
 * @see [start]
 * @see [Comparable]
 * @see [Comparable.compareTo]
 * @author Guava, lgou2w
 */
abstract class ComparisonChain private constructor() {

    companion object {

        private val ACTIVE : ComparisonChain = ActiveComparisonChain()
        private val LESS : ComparisonChain = InactiveComparisonChain(-1)
        private val GREATER : ComparisonChain = InactiveComparisonChain(1)

        /**
         * * Perform chain comparison.
         * * 进行链式比较器.
         */
        @JvmStatic
        fun start(): ComparisonChain
                = ACTIVE
    }

    /*
     * Modify: ACTIVE -> ActiveComparisonChain
     * by MoonLake on 24/08/2017
     */

    private class ActiveComparisonChain : ComparisonChain() {
        override fun <T : Comparable<T>> compare(left: T, right: T): ComparisonChain
                = classify(left.compareTo(right))
        override fun <T> compare(left: T, right: T, comparator: Comparator<T>): ComparisonChain
                = classify(comparator.compare(left, right))
        override fun compare(left: Int, right: Int): ComparisonChain
                = classify(left.compareTo(right))
        override fun compare(left: Long, right: Long): ComparisonChain
                = classify(left.compareTo(right))
        override fun compare(left: Float, right: Float): ComparisonChain
                = classify(left.compareTo(right))
        override fun compare(left: Double, right: Double): ComparisonChain
                = classify(left.compareTo(right))
        override fun compareTrueFirst(left: Boolean, right: Boolean): ComparisonChain
                = classify(right.compareTo(left))
        override fun compareFalseFirst(left: Boolean, right: Boolean): ComparisonChain
                = classify(left.compareTo(right))
        override val result: Int
                = 0
        private fun classify(result: Int): ComparisonChain
                = if (result < 0) LESS else if (result > 0) GREATER else ACTIVE
    }

    private class InactiveComparisonChain(
            override val result: Int
    ) : ComparisonChain() {
        override fun <T : Comparable<T>> compare(left: T, right: T): ComparisonChain
                = this
        override fun <T> compare(left: T, right: T, comparator: Comparator<T>): ComparisonChain
                = this
        override fun compare(left: Int, right: Int): ComparisonChain
                = this
        override fun compare(left: Long, right: Long): ComparisonChain
                = this
        override fun compare(left: Float, right: Float): ComparisonChain
                = this
        override fun compare(left: Double, right: Double): ComparisonChain
                = this
        override fun compareTrueFirst(left: Boolean, right: Boolean): ComparisonChain
                = this
        override fun compareFalseFirst(left: Boolean, right: Boolean): ComparisonChain
                = this
    }

    /*
     * Modify: Comparable<?> -> <T extends Comparable<T>>
     * by MoonLake on 24/08/2017
     */

    abstract fun <T : Comparable<T>> compare(left: T, right: T): ComparisonChain

    abstract fun <T> compare(left: T, right: T, comparator: Comparator<T>): ComparisonChain

    abstract fun compare(left: Int, right: Int): ComparisonChain

    abstract fun compare(left: Long, right: Long): ComparisonChain

    abstract fun compare(left: Float, right: Float): ComparisonChain

    abstract fun compare(left: Double, right: Double): ComparisonChain

    abstract fun compareTrueFirst(left: Boolean, right: Boolean): ComparisonChain

    abstract fun compareFalseFirst(left: Boolean, right: Boolean): ComparisonChain

    abstract val result: Int
}
