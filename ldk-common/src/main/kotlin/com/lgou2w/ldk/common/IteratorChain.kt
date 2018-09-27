/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

/*
 * Modify: Apache common-collections IteratorChain Java -> Kotlin
 * by lgou2w on 19/09/2018
 */

/**
 * ## IteratorChain (迭代链)
 *
 * * An iterator chain separated by Apache's `common-collections` library.
 *      A tool class used to connect multiple iterators.
 * * 由 Apache 的 `common-collections` 库分离出来的迭代链. 用来将多个迭代器连接起来的工具类.
 *
 * ### Sample:
 * ```kotlin
 * val list1 = listOf(0, 1, 2)
 * val list2 = listOf(3, 4, 5)
 * val iterator = IteratorChain.concat(list1.iterator(), list2.iterator(), ...)
 * while (iterator.hasNext())
 *      println(iterator.next()) // 0, 1, 2, 3, 4, 5
 * ```
 *
 * @see [concat]
 * @see [Iterator]
 * @author Apache, lgou2w
 */
class IteratorChain<T> private constructor(
        iterators: Array<out Iterator<T>>
) : Iterator<T> {

    private val iterators : List<Iterator<T>> = iterators.toList()
    private var lastUsedIterator : Iterator<T>? = null
    private var currentIterator : Iterator<T>? = null
    private var currentIndex = 0

    private fun updateCurrentIterator() {
        if (currentIterator == null) {
            @Suppress("UNCHECKED_CAST")
            currentIterator = if (iterators.isEmpty()) EMPTY as Iterator<T> else iterators[0]
            lastUsedIterator = currentIterator
        } else {
            while (!currentIterator.notNull().hasNext() && currentIndex < iterators.size - 1) {
                currentIndex += 1
                currentIterator = iterators[currentIndex]
            }
        }
    }

    override fun hasNext(): Boolean {
        updateCurrentIterator()
        lastUsedIterator = currentIterator
        return currentIterator.notNull().hasNext()
    }

    override fun next(): T {
        updateCurrentIterator()
        lastUsedIterator = currentIterator
        return currentIterator.notNull().next()
    }

    companion object {

        @JvmStatic
        private val EMPTY = object : MutableIterator<Any> {
            override fun hasNext(): Boolean = false
            override fun next(): Any = throw NoSuchElementException("Empty Iterator")
            override fun remove() = throw UnsupportedOperationException("Empty Iterator")
        }

        /**
         * * Joins a given iterator array into an iterator chain object.
         * * 将给定的迭代器数组连接成一个迭代链对象.
         *
         * @param [iterators] Iterator array
         * @param [iterators] 迭代器数组
         */
        @JvmStatic
        fun <T> concat(vararg iterators: Iterator<T>) : IteratorChain<T>
                = IteratorChain(iterators)

        /**
         * * Joins a given iterator collection into an iterator chain object.
         * * 将给定的迭代器集合连接成一个迭代链对象.
         *
         * @param [iterators] Iterator collection
         * @param [iterators] 迭代器集合
         */
        @JvmStatic
        fun <T> concat(iterators: Collection<Iterator<T>>) : IteratorChain<T>
                = IteratorChain(iterators.toTypedArray())
    }
}
