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

package com.lgou2w.ldk.chat

import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldThrow
import org.junit.Test

class ChatColorTest {

  @Test fun `ChatColor - append`() {
    ChatColor.GOLD.code shouldBeEqualTo '6'
    (ChatColor.GOLD + "gold") shouldBeEqualTo "§6gold"
  }

  @Test fun `ChatColor - of hex rgb int`() {
    val color = Color.of(0xff0000)
    color shouldBeInstanceOf ChatHexColor::class
    color.rgb shouldBeEqualTo 0xff0000
  }

  @Test fun `ChatColor - of hex rgb string is blank or invalid, should throw exception`() {
    invoking { Color.of("") } shouldThrow IllegalArgumentException::class
    invoking { Color.of("!abc") } shouldThrow IllegalArgumentException::class
  }

  @Test fun `ChatColor - of hex rgb string samples`() {
    Color.of("#f00").rgb shouldBeEqualTo Color.of("#ff0000").rgb
    Color.ofSafely("invalid") shouldBe null
  }

  @Suppress("ReplaceCallWithBinaryOperator")
  @Test fun `ChatHexColor - std validation`() {
    ChatHexColor(0).hashCode() shouldBeEqualTo 0.hashCode()
    val color = ChatHexColor(0)
    color.equals(color) shouldBe true
    color.equals(null) shouldBe false
    color.equals(ChatHexColor(0)) shouldBe true
    color.equals(ChatHexColor(1)) shouldBe false
  }
}
