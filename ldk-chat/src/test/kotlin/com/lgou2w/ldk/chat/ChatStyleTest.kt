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

class ChatStyleTest {

    @Test fun `ChatStyle - normal` () {
        val cs = ChatStyle()
        cs.color = ChatColor.RED
        cs.bold = true
        cs.italic = true
        cs.strikethrough = true
        cs.underlined = true
        cs.obfuscated = true
        cs.clickEvent = ChatClickEvent(ChatClickEvent.Action.OPEN_URL, "github.com")
        cs.hoverEvent = ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT, ChatComponentText("hi"))
        cs.insertion = "Insertion"
        cs.getColor() shouldEqual ChatColor.RED
        cs.getBold() shouldEqual true
        cs.getItalic() shouldEqual true
        cs.getStrikethrough() shouldEqual true
        cs.getUnderlined() shouldEqual true
        cs.getObfuscated() shouldEqual true
        cs.getInsertion() shouldEqual "Insertion"
        cs.getClickEvent() shouldNotEqual null
        cs.getHoverEvent() shouldNotEqual null
        cs.isEmpty() shouldEqual false
        cs.equals(cs) shouldEqual true
        cs.equals(null) shouldEqual false
        cs.hashCode() shouldNotEqual 0
        val cs2 = ChatStyle()
        cs2.getColor() shouldEqual null
        cs2.getBold() shouldEqual false
        cs2.getItalic() shouldEqual false
        cs2.getStrikethrough() shouldEqual false
        cs2.getUnderlined() shouldEqual false
        cs2.getObfuscated() shouldEqual false
        cs.equals(cs2) shouldEqual false
        cs2.setParent(cs)
        cs2.getColor() shouldEqual ChatColor.RED
        cs2.equals(cs) shouldEqual false
    }
}
