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

import org.amshove.kluent.shouldEqual
import org.junit.Test

class ComparisonChainTest {

    class Job(val priority: Int)

    private fun start() = ComparisonChain.start()

    @Test fun `ComparisonChain - normal comparison`() {
        start().compare(1, 0).result shouldEqual 1 // 1 > 0
        start().compare(0, 1).compare(0, 0).result shouldEqual -1 // 0 < 1, last skip
        start().compare(1L, 0L).result shouldEqual 1 // 1 > 0
        start().compare(0L, 1L).compare(0L, 0L).result shouldEqual -1 // 0 < 1, last skip
        start().compare(1.0, 0.1).result shouldEqual 1 // 1.0 > 0.1
        start().compare(0.1, 1.0).compare(0.0, 0.0).result shouldEqual -1 // 0.1 < 1.0, last skip
        start().compare(1.0f, 0.1f).result shouldEqual 1 // 1.0 > 0.1
        start().compare(0.1f, 1.0f).compare(0.0f, 0.0f).result shouldEqual -1 // 0.1 < 1.0, last skip
        start().compare(true, right = false).result shouldEqual 1 // true > false
        start().compare(true, right = true).compare(false, right = false).result shouldEqual 0 // true == true, last skip
        start().compare("A", "B").result shouldEqual -1 // A < B
        start().compare("B", "A").compare("A", "A").result shouldEqual 1 // B > A, last skip
        start().compareTrueFirst(true, right = false).result shouldEqual -1 // false < true, right.compareTo(left)
        start().compareTrueFirst(false, right = true).compareTrueFirst(false, right = false).result shouldEqual 1 // true > false, right.compareTo(left), last skip
        start().compareFalseFirst(true, right = false).result shouldEqual 1 // true > false, left.compareTo(right)
        start().compareFalseFirst(false, right = true).compareFalseFirst(false, right = false).result shouldEqual -1 // false < true, left.compareTo(right), last skip
        start()
            .compare(Job(3), Job(1), Comparator { o1, o2 -> o1.priority.compareTo(o2.priority) })
            .result shouldEqual 1 // 3 > 1
        start()
            .compare(Job(1), Job(3), Comparator { o1, o2 -> o1.priority.compareTo(o2.priority) })
            .compare(Job(0), Job(0), Comparator { o1, o2 -> o1.priority.compareTo(o2.priority) })
            .result shouldEqual -1 // 1 < 3, last skip
    }
}
