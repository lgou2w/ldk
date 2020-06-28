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

class ChatComponentTranslationTest {

  @Suppress("ReplaceCallWithBinaryOperator")
  @Test fun `ChatComponentTranslation - test`() {
    val cct = ChatComponentTranslation("item.diamond")
    cct.key shouldBeEqualTo "item.diamond"
    cct.key = "item.key"
    cct.key shouldBeEqualTo "item.key"
    cct.withs.size shouldBeEqualTo 0
    cct.withs = mutableListOf()
    cct.addWiths(arrayOf(1, "str", 2.0, 3f))
    cct.withs.size shouldBeEqualTo 4
    cct.hashCode() shouldNotBeEqualTo 0
    cct.toString() shouldNotBeEqualTo null
    cct.equals(cct) shouldBeEqualTo true
    cct.equals(null) shouldBeEqualTo false
    cct.equals(ChatComponentTranslation("key")) shouldBeEqualTo false
    cct.equals(ChatComponentTranslation("item.key")) shouldBeEqualTo false
    cct.equals(ChatComponentTranslation("item.key", cct.withs.toMutableList())) shouldBeEqualTo true
    cct.style.color = ChatColor.RED
    cct.equals(ChatComponentTranslation("item.key", cct.withs.toMutableList())) shouldBeEqualTo false
    (ChatSerializer.fromJson(cct.toJson()) as ChatComponentTranslation).key shouldBeEqualTo "item.key"
  }
}
