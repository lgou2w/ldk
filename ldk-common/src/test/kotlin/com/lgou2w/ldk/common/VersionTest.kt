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
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotThrow
import org.amshove.kluent.shouldStartWith
import org.amshove.kluent.shouldThrow
import org.junit.Test

@Suppress("ReplaceCallWithBinaryOperator")
class VersionTest {

  @Test fun `Version - 1 should be less than version 2`() {
    val v1 = Version(1, 0, 0)
    val v2 = Version(2, 0, 0)
    v1.major shouldBeEqualTo 1
    v2.major shouldBeEqualTo 2
    v1 shouldNotBeEqualTo v2
    (v1 < v2) shouldBeEqualTo true
    (v1 > v2) shouldBeEqualTo false
    v1.equals(v2) shouldBeEqualTo false
    v2.equals(v1) shouldBeEqualTo false
    v1 shouldBeEqualTo Version(1, 0, 0)
    v2 shouldBeEqualTo Version(2, 0, 0)
    Version(1, 0, 1).let { it > v1 && it < v2 } shouldBeEqualTo true
  }

  @Test fun `Version - version member comparison`() {
    val ver = Version(1, 0, 1)
    ver.major shouldBeEqualTo 1
    ver.minor shouldBeEqualTo 0
    ver.build shouldBeEqualTo 1
    ver.version shouldBeEqualTo "1.0.1"
    ver shouldBe ver
    ver.equals(ver) shouldBeEqualTo true
    ver.equals("ver") shouldBeEqualTo false
    ver.toString() shouldStartWith "Version"
    ver.equals(Version(1, 0, 1)) shouldBeEqualTo true
  }

  @Test fun `Version - version parsing`() {
    Version.parse("1.0.1").version shouldBeEqualTo "1.0.1"
    Version.parseSafely("1.0.1") shouldBeEqualTo Version.parse("1.0.1")
    Version.parseSafely("error") shouldBeEqualTo null
    Version.parseSafely(null) shouldBeEqualTo null
    Version.parseSafely("") shouldBeEqualTo null
    invoking { Version.parse("error") } shouldThrow IllegalArgumentException::class
    invoking { Version.parseSafely("error") } shouldNotThrow IllegalArgumentException::class
  }

  @Test fun `Version - zero version hashCode should be zero`() {
    val zeroVer = Version(0, 0, 0)
    zeroVer.version shouldBeEqualTo "0.0.0"
    zeroVer.hashCode() shouldBeEqualTo 0
  }

  @Test fun `Version - equals`() {
    Version(1, 0, 1).equals(Version(1, 1, 0)) shouldBeEqualTo false
    Version(1, 0, 1).equals(Version(1, 0, 2)) shouldBeEqualTo false
  }
}
