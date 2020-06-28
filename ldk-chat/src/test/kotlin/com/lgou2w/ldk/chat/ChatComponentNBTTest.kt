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
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotBe
import org.junit.Test

@Suppress("ReplaceCallWithBinaryOperator")
class ChatComponentNBTTest {

  @Test fun `ChatComponentNBT - constructor`() {
    object : ChatComponentNBT("") { }
    object : ChatComponentNBT("", true) { }
    object : ChatComponentNBT("", true, "") { }
  }

  @Test fun `ChatComponentNBT - Block - test`() {
    val ccnb = ChatComponentNBTBlock("Items")
    ccnb.nbt shouldBeEqualTo "Items"
    ccnb.interpret shouldBe null
    ccnb.path shouldBe null
    ccnb.hashCode() shouldNotBe 0
    ccnb.equals(ccnb) shouldBe true
    ccnb.equals(ChatComponentNBTBlock("Items", null, null)) shouldBe true
    ccnb.interpret = true
    ccnb.path = "test"
    ccnb.equals(ChatComponentNBTBlock("Items")) shouldBe false
    ccnb.equals(null) shouldBe false
  }

  @Test fun `ChatComponentNBT - Entity - test`() {
    val ccnb = ChatComponentNBTEntity("Health")
    ccnb.nbt shouldBeEqualTo "Health"
    ccnb.interpret = true
    ccnb.path = ""
    ccnb.hashCode() shouldNotBe 0
  }

  @Test fun `ChatComponentNBT - Storage - test`() {
    val ccnb = ChatComponentNBTStorage("diamond")
    ccnb.nbt shouldBeEqualTo "diamond"
    ccnb.nbt = "apple"
    ccnb.toString() shouldContain "apple"
  }

  @Test fun `ChatComponentNBT - equals`() {
    val ccnb = ChatComponentNBTBlock("nbt", true, "path")
    ccnb.equals(ChatComponentNBTBlock("nbt", true, "path2")) shouldBe false
    ccnb.equals(ChatComponentNBTBlock("nbt", false, "path")) shouldBe false
    ccnb.equals(ChatComponentNBTBlock("nbt2", true, "path")) shouldBe false
    ccnb.equals(ChatComponentNBTBlock("nbt", true, "path")) shouldBe true
  }
}
