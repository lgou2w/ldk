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

import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.junit.Test

class ChatComponentScoreTest {

    @Test fun `ChatComponentScore - test`() {
        val ccs = ChatComponentScore("key", "obj")
        ccs.name shouldEqual "key"
        ccs.name = "key2"
        ccs.name shouldEqual "key2"
        ccs.setName("key2").name shouldEqual "key2"
        ccs.hashCode() shouldBeGreaterThan 1
        ccs.equals(ccs) shouldEqual true
        ccs.equals(ChatComponentScore("key2", "obj")) shouldEqual true
        ccs.equals(ChatComponentScore("key", "obj")) shouldEqual false
        ccs.equals(null) shouldEqual false
        ccs.objective shouldEqual "obj"
        ccs.objective = "object"
        ccs.setObjective("object").objective shouldEqual "object"
        ccs.value shouldEqual null
        ccs.value = "value"
        ccs.setValue("value").value shouldEqual "value"
        ccs.equals(ChatComponentScore("key2", "obj")) shouldEqual false
        ccs.equals(ChatComponentScore("key2", "object", "v")) shouldEqual false
        ccs.equals(ChatComponentScore("key2", "object", "value")) shouldEqual true
        ccs.style.color = ChatColor.RED
        ccs.equals(ChatComponentScore("key2", "obj")) shouldEqual false
        ccs.toJson() shouldContain "key2"
        ccs.toString() shouldNotEqual null
        ccs.value = null
        (ChatSerializer.fromJson(ccs.toJson()) as ChatComponentScore).name shouldEqual "key2"
    }
}
