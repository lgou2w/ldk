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

class ChatComponentScoreTest {

  @Suppress("ReplaceCallWithBinaryOperator")
  @Test fun `ChatComponentScore - test`() {
    val ccs = ChatComponentScore("key", "obj")
    ccs.name shouldBeEqualTo "key"
    ccs.name = "key2"
    ccs.name shouldBeEqualTo "key2"
    ccs.setName("key2").name shouldBeEqualTo "key2"
    ccs.hashCode() shouldBeGreaterThan 1
    ccs.equals(ccs) shouldBeEqualTo true
    ccs.equals(ChatComponentScore("key2", "obj")) shouldBeEqualTo true
    ccs.equals(ChatComponentScore("key", "obj")) shouldBeEqualTo false
    ccs.equals(null) shouldBeEqualTo false
    ccs.objective shouldBeEqualTo "obj"
    ccs.objective = "object"
    ccs.setObjective("object").objective shouldBeEqualTo "object"
    ccs.value shouldBeEqualTo null
    ccs.value = "value"
    ccs.setValue("value").value shouldBeEqualTo "value"
    ccs.equals(ChatComponentScore("key2", "obj")) shouldBeEqualTo false
    ccs.equals(ChatComponentScore("key2", "object", "v")) shouldBeEqualTo false
    ccs.equals(ChatComponentScore("key2", "object", "value")) shouldBeEqualTo true
    ccs.style.color = ChatColor.RED
    ccs.equals(ChatComponentScore("key2", "obj")) shouldBeEqualTo false
    ccs.toJson() shouldContain "key2"
    ccs.toString() shouldNotBeEqualTo null
    ccs.value = null
    (ChatSerializer.fromJson(ccs.toJson()) as ChatComponentScore).name shouldBeEqualTo "key2"
  }
}
