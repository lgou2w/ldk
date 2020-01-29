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

package com.lgou2w.ldk.security

import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.amshove.kluent.shouldThrow
import org.junit.Test

class EmailParserTest {

  @Test fun `EmailParser - simple parse`() {
    val v = "1@s.t"
    val email = SimpleEmailParser.parse(v)
    email.id shouldBeEqualTo "1"
    email.secondaryDomain shouldBeEqualTo "s"
    email.topDomain shouldBeEqualTo "t"
    email.fullyQualifiedEmail shouldBeEqualTo v
  }

  @Test fun `EmailParser - simple parse - examples`() {
    SimpleEmailParser.parse("1@s.t") shouldNotBe null
    SimpleEmailParser.parse("aaa@bbb.ccc") shouldNotBe null
    SimpleEmailParser.parse("id@seconrday.top") shouldNotBe null
    SimpleEmailParser.parseSafely("!#$%@^&*.()") shouldNotBe null
  }

  @Test fun `EmailParser - simple parse - error`() {
    invoking { SimpleEmailParser.parse("1111") } shouldThrow IllegalArgumentException::class
    invoking { SimpleEmailParser.parse("1@") } shouldThrow IllegalArgumentException::class
    invoking { SimpleEmailParser.parse("@.") } shouldThrow IllegalArgumentException::class

    SimpleEmailParser.parseSafely(null) shouldBe null
    SimpleEmailParser.parseSafely("") shouldBe null
    SimpleEmailParser.parseSafely("@.") shouldBe null
  }

  @Test fun `EmailParser - standard parse`() {
    val v = "aaa@me.cc"
    val email = StandardEmailParser.parse(v)
    email.id shouldBeEqualTo "aaa"
    email.secondaryDomain shouldBeEqualTo "me"
    email.topDomain shouldBeEqualTo "cc"
    email.fullyQualifiedEmail shouldBeEqualTo v
  }

  @Test fun `EmailParser - standard parse - examples`() {
    StandardEmailParser.parse("aaa@me.cc") shouldNotBe null
    StandardEmailParser.parse("this-is-my-email@your-domain.com.cn") shouldNotBe null
    StandardEmailParser.parse("1234567890@qq.com") shouldNotBe null
    StandardEmailParser.parse("l_love_you_my_whole_life_y@yahoo.com") shouldNotBe null
    StandardEmailParser.parse("A-Za-z0-9_.-@A-Za-z0-9_-.AZaz") shouldNotBe null
    StandardEmailParser.parseSafely("-----hello-----world-----@domain.net") shouldNotBe null
  }

  @Test fun `EmailParser - standard parse - error`() {
    invoking { StandardEmailParser.parse("1111") } shouldThrow IllegalArgumentException::class
    invoking { StandardEmailParser.parse("1@s.t") } shouldThrow IllegalArgumentException::class
    invoking { StandardEmailParser.parse("my-id@web.com.cn.net") } shouldThrow IllegalArgumentException::class
    invoking { StandardEmailParser.parse("!#$%@^&*.()") } shouldThrow IllegalArgumentException::class

    StandardEmailParser.parseSafely(null) shouldBe null
    StandardEmailParser.parseSafely("") shouldBe null
    StandardEmailParser.parseSafely("!#$%@^&*.()") shouldBe null
  }
}
