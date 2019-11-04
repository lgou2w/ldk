/*
* MIT License
*
* Copyright (c) 2018 Mouse
*
* https://github.com/Mouse0w0/EventBus
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

/*
 * Copyright (C) 2019 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.eventbus

/*
 *  Modify: Mouse EventBus Java -> Kotlin
 *  by lgou2w on 04/09/2019
 */

import java.util.LinkedList
import java.util.function.Supplier

/**
 * @author Mouse
 * @since LDK 0.1.9
 */
class SortedList<E> private constructor(
  private val list: MutableList<E>,
  private val comparator: Comparator<E>
) : AbstractMutableList<E>() {

  companion object {

    @JvmStatic fun <E> create(comparator: Comparator<E>): SortedList<E>
      = create(comparator, Supplier<MutableList<E>> { LinkedList() })

    @JvmStatic fun <E> create(comparator: Comparator<E>, constructor: Supplier<MutableList<E>>)
      = SortedList(constructor.get(), comparator)

    @JvmStatic fun <E : Comparable<E>> create(): SortedList<E>
      = create(Comparator { o1, o2 -> o1.compareTo(o2) })

    @JvmStatic fun <E> copyOf(list: MutableList<E>, comparator: Comparator<E>): SortedList<E> {
      for (i in 0 until list.size) {
        val el = list[i]
        if (el == null)
          list.remove(el)
      }
      return SortedList(list, comparator)
    }

    @JvmStatic fun <E : Comparable<E>> copyOf(list: MutableList<E>): SortedList<E>
      = copyOf(list, Comparator { o1, o2 -> o1.compareTo(o2) })

    @JvmStatic fun <E> of(comparator: Comparator<E>, vararg elements: E): SortedList<E>
      = SortedList(arrayListOf(*elements), comparator)

    @JvmStatic fun <E : Comparable<E>> of(vararg elements: E): SortedList<E>
      = of(Comparator { o1, o2 -> o1.compareTo(o2) }, *elements)
  }

  override fun add(element: E): Boolean {
    var index = 0
    while (index < size) {
      if (comparator.compare(element, get(index)) < 0) {
        list.add(index, element)
        return true
      }
      index++
    }
    return list.add(element)
  }

  override fun add(index: Int, element: E)
    = throw UnsupportedOperationException()

  override fun addAll(index: Int, elements: Collection<E>): Boolean
    = throw UnsupportedOperationException()

  override fun set(index: Int, element: E): E
    = throw UnsupportedOperationException()

  override fun get(index: Int): E
    = list[index]

  override fun removeAt(index: Int): E
    = list.removeAt(index)

  override val size: Int
    get() = list.size
}
