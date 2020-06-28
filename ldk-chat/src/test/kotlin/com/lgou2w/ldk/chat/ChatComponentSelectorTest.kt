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
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.Test

class ChatComponentSelectorTest {

  @Suppress("ReplaceCallWithBinaryOperator")
  @Test fun `ChatComponentSelector - test`() {
    val ccs = ChatComponentSelector("@p")
    ccs.selector shouldBeEqualTo "@p"
    ccs.selector = "@e"
    ccs.selector shouldBeEqualTo "@e"
    ccs.selector = "@s"
    ccs.selector shouldBeEqualTo "@s"
    ccs.hashCode() shouldBeGreaterThan 1
    ccs.equals(ccs) shouldBeEqualTo true
    ccs.equals(ChatComponentSelector("@s")) shouldBeEqualTo true
    ccs.equals(ChatComponentSelector("@e")) shouldBeEqualTo false
    ccs.equals(null) shouldBeEqualTo false
    ccs.style.color = ChatColor.RED
    ccs.equals(ChatComponentSelector("@s")) shouldBeEqualTo false
    ccs.toJson() shouldContain "@s"
    ccs.toString() shouldNotBeEqualTo null
    (ChatSerializer.fromJson(ccs.toJson()) as ChatComponentSelector).selector shouldBeEqualTo "@s"
  }
}
