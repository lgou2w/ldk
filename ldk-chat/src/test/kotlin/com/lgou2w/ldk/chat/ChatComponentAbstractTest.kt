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

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.Test

class ChatComponentAbstractTest {

  @Suppress("ReplaceCallWithBinaryOperator", "SENSELESS_COMPARISON")
  @Test fun `ChatComponentAbstract - test`() {
    val cca = object : ChatComponentAbstract() {}
    val cca2 = object : ChatComponentAbstract() {}
    (cca == cca) shouldBeEqualTo true
    (cca == null) shouldBeEqualTo false
    (cca2 == cca2) shouldBeEqualTo true
    cca.style = ChatStyle().setColor(ChatColor.RED)
    cca.equals(cca2) shouldBeEqualTo false
    cca2.style = cca.style.clone()
    cca.plus("extra1")
    cca.equals(cca2) shouldBeEqualTo false
    cca.toString() shouldNotBeEqualTo cca2.toString()
    cca.equals(null) shouldBe false
    cca.setStyle(null).style shouldBeEqualTo ChatStyle.EMPTY
  }
}
