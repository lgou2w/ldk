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

import org.amshove.kluent.invoking
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.amshove.kluent.shouldThrow
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
        cs2.getClickEvent() shouldEqual null
        cs2.getHoverEvent() shouldEqual null
        cs2.getInsertion() shouldEqual null
        cs.equals(cs2) shouldEqual false
        cs2.equals(cs) shouldEqual false
        cs2.setParent(cs)
        cs2.getColor() shouldEqual ChatColor.RED
        cs2.equals(cs) shouldEqual false
    }

    @Test fun `ChatStyle - equals`() {
        val cs1 = ChatStyle()
        val cs2 = ChatStyle()
        cs1.italic = true
        cs1.equals(cs2) shouldEqual false
        cs2.italic = cs1.italic
        cs1.underlined = true
        cs1.equals(cs2) shouldEqual false
        cs2.underlined = cs1.underlined
        cs1.strikethrough = true
        cs1.equals(cs2) shouldEqual false
        cs2.strikethrough = cs1.strikethrough
        cs1.obfuscated = true
        cs1.equals(cs2) shouldEqual false
        cs2.obfuscated = cs1.obfuscated
        cs1.clickEvent = ChatClickEvent(ChatClickEvent.Action.OPEN_URL, "github.com")
        cs1.equals(cs2) shouldEqual false
        cs2.clickEvent = cs1.clickEvent
        cs1.hoverEvent = ChatHoverEvent(ChatHoverEvent.Action.SHOW_ITEM, ChatComponentText("hi"))
        cs1.equals(cs2) shouldEqual false
        cs2.hoverEvent = cs1.hoverEvent
        cs1.insertion = "Insertion"
        cs1.equals(cs2) shouldEqual false
        cs2.insertion = cs1.insertion
        cs1.equals(cs2) shouldEqual true
    }

    @Test fun `ChatStyle - ROOT - These operations should not be supported`() {
        val rootField = ChatStyle::class.java.getDeclaredField("ROOT")
                       ?: return
        rootField.isAccessible = true
        val root = rootField.get(null) as ChatStyle
        invoking { root.setParent(null) } shouldThrow UnsupportedOperationException::class
        invoking { root.setColor(null) } shouldThrow UnsupportedOperationException::class
        invoking { root.setBold(null) } shouldThrow UnsupportedOperationException::class
        invoking { root.setItalic(null) } shouldThrow UnsupportedOperationException::class
        invoking { root.setStrikethrough(null) } shouldThrow UnsupportedOperationException::class
        invoking { root.setUnderlined(null) } shouldThrow UnsupportedOperationException::class
        invoking { root.setObfuscated(null) } shouldThrow UnsupportedOperationException::class
        invoking { root.setClickEvent(null) } shouldThrow UnsupportedOperationException::class
        invoking { root.setHoverEvent(null) } shouldThrow UnsupportedOperationException::class
        invoking { root.setInsertion(null) } shouldThrow UnsupportedOperationException::class
        root.toString() shouldEqual "ChatStyle.ROOT"
    }
}
