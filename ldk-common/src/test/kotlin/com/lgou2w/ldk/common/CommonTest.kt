/*
 * Copyright (C) 2016-2020 The lgou2w <lgou2w@hotmail.com>
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
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotThrow
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.Test

class CommonTest {

  @Test fun `nullable - should return itself`() {
    val obj = "NotNull".nullable()
    obj shouldBeEqualTo obj
    obj shouldNotBeEqualTo null
    obj shouldBeEqualTo "NotNull"
  }

  @Test fun `notNull - should throw null pointer exception`() {
    invoking { null.notNull("NPE") } shouldThrow NullPointerException::class withMessage "NPE"
    invoking { "NotNull".notNull() } shouldNotThrow NullPointerException::class
  }

  @Test fun `notNullAndBlank - should throw null pointer exception`() {
    invoking { "".notNullAndBlank("BLANK") } shouldThrow NullPointerException::class withMessage "BLANK"
    invoking { null.notNullAndBlank("NPE") } shouldThrow NullPointerException::class withMessage "NPE"
    invoking { "string".notNullAndBlank() } shouldNotThrow NullPointerException::class
  }

  @Test fun `applyIfNotNull - null should return null`() {
    val obj : Any? = null
    obj shouldBeEqualTo null
    obj.applyIfNotNull {  } shouldBeEqualTo null
  }

  @Test fun `applyIfNotNull - add an element to the list so size should be 1`() {
    val list = arrayListOf<Int>().applyIfNotNull { add(1) }
    list shouldNotBeEqualTo null
    list?.size shouldBeEqualTo 1
    list?.isEmpty() shouldBeEqualTo false
  }

  @Test fun `letIfNotNull - null should return null`() {
    val obj : Any? = null
    obj.letIfNotNull {  } shouldBeEqualTo null
  }

  @Test fun `letIfNotNull - string to int should be int type`() {
    val obj = "1".letIfNotNull { it.toInt() }
    obj shouldNotBeEqualTo null
    obj shouldBeEqualTo 1
  }

  @Test fun `isTrue - null or false should return false`() {
    val obj : Boolean? = null
    obj.isTrue() shouldBeEqualTo false
    false.isTrue() shouldBeEqualTo false
    true.isTrue() shouldBeEqualTo true
  }

  @Test fun `isFalse - null or false should return true`() {
    val obj : Boolean? = null
    obj.isFalse() shouldBeEqualTo true
    false.isFalse() shouldBeEqualTo true
    true.isFalse() shouldBeEqualTo false
  }

  @Test fun `orTrue - null or true should return true`() {
    val obj : Boolean? = null
    obj.orTrue() shouldBeEqualTo true
    true.orTrue() shouldBeEqualTo true
    false.orTrue() shouldBeEqualTo false
  }

  @Test fun `orFalse - null or false should return false`() {
    val obj : Boolean? = null
    obj.orFalse() shouldBeEqualTo false
    false.orFalse() shouldBeEqualTo false
    true.orFalse() shouldBeEqualTo true
  }

  @Test fun `isLater - 'b' should be after 'a'`() {
    "b".isLater("a") shouldBeEqualTo true
    "a".isLater("b") shouldBeEqualTo false
  }

  @Test fun `isOrLater - 'b' should be after 'a' or equal to 'b'`() {
    "b".isOrLater("a") shouldBeEqualTo true
    "b".isOrLater("b") shouldBeEqualTo true
    "a".isOrLater("b") shouldBeEqualTo false
  }

  @Test fun `isRange - 'b' should be between 'a' and 'c'`() {
    "b".isRange("a", "c") shouldBeEqualTo true
    "b".isRange("a", "b") shouldBeEqualTo false
    "b".isRange("a", "a") shouldBeEqualTo false
    "b".isRange("b", "c") shouldBeEqualTo false
  }

  @Test fun `isOrRange - 'b' should be equal to or between 'a' and 'c'`() {
    "b".isOrRange("a", "c") shouldBeEqualTo true
    "b".isOrRange("b", "b") shouldBeEqualTo true
    "b".isOrRange("a", "a") shouldBeEqualTo false
    "b".isOrRange("c", "a") shouldBeEqualTo false
  }

  @Suppress("UNCHECKED_CAST")
  private val lazyClassDelegate by lazyClass { Class.forName("java.lang.String") as Class<String> }

  @Suppress("UNCHECKED_CAST")
  @Test fun `lazyClass - new should not be initialized yet`() {
    val lazyClass = lazyClass { Class.forName("java.lang.String") as Class<String> }
    lazyClass.isInitialized() shouldBeEqualTo false
    lazyClass.value.simpleName shouldBeEqualTo "String"
    lazyClass.isInitialized() shouldBeEqualTo true
    lazyClass.value shouldBe lazyClassDelegate
  }

  private val lazyAnyClassDelegate by lazyAnyClass { Class.forName("java.lang.String") }

  @Test fun `lazyAnyClass - new should not be initialized yet`() {
    val lazyAnyClass = lazyAnyClass { Class.forName("java.lang.String") }
    lazyAnyClass.isInitialized() shouldBeEqualTo false
    lazyAnyClass.value.simpleName shouldBeEqualTo "String"
    lazyAnyClass.isInitialized() shouldBeEqualTo true
    lazyAnyClass.value shouldBe lazyAnyClassDelegate
  }

  private val lazyAnyOrNullClassDelegate by lazyAnyOrNullClass { null }

  @Test fun `lazyAnyOrNullClass - new that does not exist should return null`() {
    val lazyAnyOrNullClass = lazyAnyOrNullClass {
      try { Class.forName("404 Not Found") } catch (e: ClassNotFoundException) { null }
    }
    lazyAnyOrNullClass.isInitialized() shouldBeEqualTo false
    lazyAnyOrNullClass.value shouldBeEqualTo null
    lazyAnyOrNullClass.isInitialized() shouldBeEqualTo true
    lazyAnyOrNullClass.value shouldBe lazyAnyOrNullClassDelegate
  }

  @Test fun `Predicate - and - or - negate`() {
    val p1 : Predicate<Int> = { it > 0 }
    val p2 : Predicate<Int> = { it % 2 == 0 }
    // normal
    p1.invoke(1) shouldBeEqualTo true           // 1 > 0 = true
    p2.invoke(3) shouldBeEqualTo false          // 3 % 2 == 0 = false
    // and
    (p1 and p2).invoke(2) shouldBeEqualTo true  // 2 > 0 && 2 % 2 == 0 = true
    (p1 and p2).invoke(3) shouldBeEqualTo false // 3 > 0 && 3 % 2 == 0 = false
    // or
    (p1 or p2).invoke(2) shouldBeEqualTo true   // 2 > 0 || 2 % 2 == 0 = true
    (p1 or p2).invoke(3) shouldBeEqualTo true   // 3 > 0 || 3 % 2 == 0 = true
    // negate
    p1.negate().invoke(2) shouldBeEqualTo false // 2 > 0 = true, !true = false
    p2.negate().invoke(3) shouldBeEqualTo true  // 3 % 2 == 0 = false, !false = true
  }

  @Test fun `Function - compose - andThen`() {
    val f1 : Function<Int, Int> = { it * 2 }
    val f2 : Function<Int, Int> = { it * it } // it ^ 2
    // normal
    f1.invoke(3) shouldBeEqualTo 6 // 3 * 2 = 6
    f2.invoke(5) shouldBeEqualTo 25 // 5 ^ 2 = 25
    // compose
    (f1 compose f2).invoke(3) shouldBeEqualTo 18 // (3 ^ 2) * 2 = 18, f1(f2(3))
    (f2 compose f1).invoke(3) shouldBeEqualTo 36 // (3 * 2) ^ 2 = 36, f2(f1(3))
    // andThen
    (f1 andThen f2).invoke(3) shouldBeEqualTo 36 // (3 * 2) ^ 2 = 36, f2(f1(3))
    (f2 andThen f1).invoke(3) shouldBeEqualTo 18 // (3 ^ 2) * 2 = 18, f1(f2(3))
  }

  @Test fun `Consumer - andThenConsume - list`() {
    val list = arrayListOf<Int>()
    val c1 : Consumer<Int> = { list.add(it) }
    val c2 : Consumer<Int> = { list.remove(it) }
    // normal
    c1.invoke(1); list.size shouldBe 1
    c2.invoke(1); list.isEmpty() shouldBe true
    // andThenConsume
    (c1 andThenConsume c2).invoke(1); list.size shouldBe 0
    (c2 andThenConsume c1).invoke(1); list.size shouldBe 1
    list shouldContain 1
    list.clear()
  }

  @Test fun `Constants - string`() {
    Constants shouldBe Constants
    Constants.LDK shouldBeEqualTo "LDK"
  }
}
