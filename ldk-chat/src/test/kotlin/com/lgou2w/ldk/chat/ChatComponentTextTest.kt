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

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.Test

class ChatComponentTextTest {

  @Suppress("ReplaceCallWithBinaryOperator")
  @Test fun `ChatComponentText - test`() {
    val component = ChatComponentText()
    component.text shouldBeEqualTo ""
    component.text = "abc"
    component.text shouldBeEqualTo "abc"
    val component2 = ChatComponentText(component)
    component2.text shouldBeEqualTo component.text
    component2.equals(component) shouldBeEqualTo true
    component2.equals(null) shouldBeEqualTo false
    component2.text = "cba"
    component2.equals(component) shouldBeEqualTo false
    component2.equals(component2) shouldBeEqualTo true
    component2.equals(ChatComponentText("cba")) shouldBeEqualTo true
    component2.hashCode() shouldNotBeEqualTo component.hashCode()
    component2.style.bold = true
    component2.equals(ChatComponentText("cba")) shouldBeEqualTo false
  }
}
