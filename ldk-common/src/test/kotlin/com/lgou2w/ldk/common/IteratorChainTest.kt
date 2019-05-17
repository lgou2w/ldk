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

import org.amshove.kluent.invoking
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.junit.Test

class IteratorChainTest {

    @Test fun `IteratorChain - Iterator element validation`() {
        val it1 = listOf(0, 1, 2).iterator()
        val it2 = listOf(3, 4, 5).iterator()
        val chain = IteratorChain.concat(it1, it2)
        chain.hasNext() shouldEqual true
        chain.next() shouldEqual 0
        chain.next() shouldEqual 1
        chain.next() shouldEqual 2
        chain.next() shouldEqual 3
        chain.next() shouldEqual 4
        chain.next() shouldEqual 5
        chain.hasNext() shouldEqual false
        invoking { chain.next() } shouldThrow NoSuchElementException::class
    }

    @Suppress("UNCHECKED_CAST", "PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    @Test fun `IteratorChain - Empty chain validation`() {
        val chain = IteratorChain.concat(emptyList<Iterator<Int>>())
        val currentIteratorField = IteratorChain::class.java.getDeclaredField("currentIterator").apply { isAccessible = true }
        chain.hasNext() shouldEqual false
        invoking { chain.next() } shouldThrow NoSuchElementException::class
        invoking { chain as MutableIterator<Int> } shouldThrow ClassCastException::class // Because kotlin mutable iteration do type checking
        invoking { (chain as java.util.Iterator<Int>).remove() } shouldThrow UnsupportedOperationException::class
        invoking { (currentIteratorField.get(chain) as MutableIterator<Int>).remove() } shouldThrow UnsupportedOperationException::class
    }
}
