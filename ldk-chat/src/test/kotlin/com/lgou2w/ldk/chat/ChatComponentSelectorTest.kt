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

class ChatComponentSelectorTest {

    @Test fun `ChatComponentSelector - test`() {
        val ccs = ChatComponentSelector("@p")
        ccs.selector shouldEqual "@p"
        ccs.selector = "@e"
        ccs.selector shouldEqual "@e"
        ccs.setSelector("@s").selector shouldEqual "@s"
        ccs.hashCode() shouldBeGreaterThan 1
        ccs.equals(ccs) shouldEqual true
        ccs.equals(ChatComponentSelector("@s")) shouldEqual true
        ccs.equals(ChatComponentSelector("@e")) shouldEqual false
        ccs.equals(null) shouldEqual false
        ccs.style.color = ChatColor.RED
        ccs.equals(ChatComponentSelector("@s")) shouldEqual false
        ccs.toJson() shouldContain "@s"
        ccs.toString() shouldNotEqual null
        (ChatSerializer.fromJson(ccs.toJson()) as ChatComponentSelector).selector shouldEqual "@s"
    }
}
