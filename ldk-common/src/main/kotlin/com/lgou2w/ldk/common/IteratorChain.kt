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

        @JvmStatic
        fun <T> concat(vararg iterators: Iterator<T>) : IteratorChain<T>
                = IteratorChain(iterators)
        @JvmStatic
        fun <T> concat(iterators: Collection<Iterator<T>>) : IteratorChain<T>
                = IteratorChain(iterators.toTypedArray())
    }
}
