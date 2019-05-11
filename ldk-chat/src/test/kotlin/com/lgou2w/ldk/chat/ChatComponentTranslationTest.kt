/*
 * Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>
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

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.junit.Test

class ChatComponentTranslationTest {

    @Test fun `ChatComponentTranslation - test`() {
        val cct = ChatComponentTranslation("item.diamond")
        cct.key shouldEqual "item.diamond"
        cct.key = "item.key"
        cct.setKey("item.key").key shouldEqual "item.key"
        cct.withs.size shouldEqual 0
        cct.withs = mutableListOf()
        cct.addWiths(arrayOf(1, "str", 2.0, 3f))
        cct.withs.size shouldEqual 4
        cct.hashCode() shouldNotEqual 0
        cct.toString() shouldNotEqual null
        cct.equals(cct) shouldEqual true
        cct.equals(null) shouldEqual false
        cct.equals(ChatComponentTranslation("key")) shouldEqual false
        cct.equals(ChatComponentTranslation("item.key")) shouldEqual false
        cct.equals(ChatComponentTranslation("item.key", cct.withs.toMutableList())) shouldEqual true
        cct.style.color = ChatColor.RED
        cct.equals(ChatComponentTranslation("item.key", cct.withs.toMutableList())) shouldEqual false
        (ChatSerializer.fromJson(cct.toJson()) as ChatComponentTranslation).key shouldEqual "item.key"
    }
}
