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
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.Test

class EnumsTest {

  enum class MyEnum(override val value: Int) : Valuable<Int> {

    ENTRY_A(15), // ordinal = 0, value = 15
    ENTRY_B(20), // ordinal = 1, value = 20
    ;
  }

  class NoEnum(override val value: Int) : Valuable<Int>

  @Test fun `Enums - of - from`() {
    Enums.of(MyEnum::class.java, { it.name == "ENTRY_A" && it.ordinal == 0 }) shouldBeEqualTo MyEnum.ENTRY_A
    Enums.of(MyEnum::class.java, { it.ordinal == 100 }, MyEnum.ENTRY_A) shouldBeEqualTo MyEnum.ENTRY_A
    Enums.ofNotNull(MyEnum::class.java, { it.ordinal == 0 }) shouldBeEqualTo MyEnum.ENTRY_A
    invoking { Enums.ofNotNull(MyEnum::class.java, { it.name == "404" }) } shouldThrow NoSuchElementException::class
    Enums.from(MyEnum::class.java, { it.ordinal == 1 }) shouldBeEqualTo MyEnum.ENTRY_B
    Enums.fromValuable(MyEnum::class.java, 20) shouldBeEqualTo MyEnum.ENTRY_B
  }

  @Test fun `Enums - from class should be an enum class`() {
    val clazz = String::class.java
    invoking { Enums.from(clazz, { it.name == "STRING" }) } shouldThrow IllegalArgumentException::class
    invoking { Enums.fromValuable(NoEnum::class.java, 1) } shouldThrow IllegalArgumentException::class
  }

  @Test fun `Enums - ofName - fromName`() {
    Enums.ofName(MyEnum::class.java, "ENTRY_A") shouldBeEqualTo MyEnum.ENTRY_A
    Enums.ofName(MyEnum::class.java, "ENTRY_B") shouldBeEqualTo MyEnum.ENTRY_B
    Enums.ofName(MyEnum::class.java, "404 Not Found") shouldBeEqualTo null
    Enums.ofNameNotNull(MyEnum::class.java, "ENTRY_A") shouldBeEqualTo MyEnum.ENTRY_A
    invoking { Enums.ofNameNotNull(MyEnum::class.java, "404") } shouldThrow NoSuchElementException::class
    Enums.fromName(MyEnum::class.java, "ENTRY_A") shouldBeEqualTo MyEnum.ENTRY_A
    Enums.fromName(MyEnum::class.java, "ENTRY_B") shouldBeEqualTo MyEnum.ENTRY_B
    Enums.fromName(MyEnum::class.java, "404 Not Found") shouldBeEqualTo null
  }

  @Suppress("DEPRECATION")
  @Test fun `Enums - ofOrdinal - fromOrdinal`() {
    Enums.ofOrdinal(MyEnum::class.java, 1) shouldBeEqualTo MyEnum.ENTRY_B // .. = 1
    Enums.ofOrdinal(MyEnum::class.java, 404) shouldBeEqualTo null
    Enums.ofOrdinalNotNull(MyEnum::class.java, 0) shouldBeEqualTo MyEnum.ENTRY_A
    invoking { Enums.ofOrdinalNotNull(MyEnum::class.java, 233) } shouldThrow NoSuchElementException::class
    Enums.fromOrdinal(MyEnum::class.java, 1) shouldBeEqualTo MyEnum.ENTRY_B // .. = 1
    Enums.fromOrdinal(MyEnum::class.java, 404) shouldBeEqualTo null
  }

  @Test fun `Enums - ofValuable - fromValuable`() {
    Enums.ofValuable(MyEnum::class.java, 15) shouldBeEqualTo MyEnum.ENTRY_A // value = 15
    Enums.ofValuable(MyEnum::class.java, 20) shouldBeEqualTo MyEnum.ENTRY_B // .. = 20
    Enums.ofValuable(MyEnum::class.java, 404) shouldBeEqualTo null
    Enums.ofValuableNotNull(MyEnum::class.java, 15) shouldBeEqualTo MyEnum.ENTRY_A
    invoking { Enums.ofValuableNotNull(MyEnum::class.java, 404) } shouldThrow NoSuchElementException::class
    Enums.fromValuable(MyEnum::class.java, 15) shouldBeEqualTo MyEnum.ENTRY_A // value = 15
    Enums.fromValuable(MyEnum::class.java, 20) shouldBeEqualTo MyEnum.ENTRY_B // .. = 20
    Enums.fromValuable(MyEnum::class.java, 404) shouldBeEqualTo null
    Enums.fromValuable(MyEnum::class.java, 233, MyEnum.ENTRY_A) shouldBeEqualTo MyEnum.ENTRY_A
  }
}
