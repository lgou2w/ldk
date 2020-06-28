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
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.Test

@Suppress("ReplaceCallWithBinaryOperator")
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
    cs.font = "default"
    cs.getColor() shouldBeEqualTo ChatColor.RED
    cs.getBold() shouldBeEqualTo true
    cs.getItalic() shouldBeEqualTo true
    cs.getStrikethrough() shouldBeEqualTo true
    cs.getUnderlined() shouldBeEqualTo true
    cs.getObfuscated() shouldBeEqualTo true
    cs.getInsertion() shouldBeEqualTo "Insertion"
    cs.getFont() shouldBeEqualTo "default"
    cs.getClickEvent() shouldNotBeEqualTo null
    cs.getHoverEvent() shouldNotBeEqualTo null
    cs.isEmpty() shouldBeEqualTo false
    cs.equals(cs) shouldBeEqualTo true
    cs.equals(null) shouldBeEqualTo false
    cs.hashCode() shouldNotBeEqualTo 0
    var cs2 = ChatStyle()
    cs2.getColor() shouldBeEqualTo null
    cs2.getBold() shouldBeEqualTo null
    cs2.getItalic() shouldBeEqualTo null
    cs2.getStrikethrough() shouldBeEqualTo null
    cs2.getUnderlined() shouldBeEqualTo null
    cs2.getObfuscated() shouldBeEqualTo null
    cs2.getClickEvent() shouldBeEqualTo null
    cs2.getHoverEvent() shouldBeEqualTo null
    cs2.getInsertion() shouldBeEqualTo null
    cs2.getFont() shouldBeEqualTo null
    cs.equals(cs2) shouldBeEqualTo false
    cs2.equals(cs) shouldBeEqualTo false
    cs2 = cs2.setParent(cs)
    cs2.getColor() shouldBeEqualTo ChatColor.RED
    cs2.equals(cs) shouldBeEqualTo true
  }

  @Test fun `ChatStyle - equals`() {
    val cs1 = ChatStyle()
    val cs2 = ChatStyle()
    cs1.italic = true
    cs1.equals(cs2) shouldBeEqualTo false
    cs2.italic = cs1.italic
    cs1.underlined = true
    cs1.equals(cs2) shouldBeEqualTo false
    cs2.underlined = cs1.underlined
    cs1.strikethrough = true
    cs1.equals(cs2) shouldBeEqualTo false
    cs2.strikethrough = cs1.strikethrough
    cs1.obfuscated = true
    cs1.equals(cs2) shouldBeEqualTo false
    cs2.obfuscated = cs1.obfuscated
    cs1.clickEvent = ChatClickEvent(ChatClickEvent.Action.OPEN_URL, "github.com")
    cs1.equals(cs2) shouldBeEqualTo false
    cs2.clickEvent = cs1.clickEvent
    cs1.hoverEvent = ChatHoverEvent(ChatHoverEvent.Action.SHOW_ITEM, ChatComponentText("hi"))
    cs1.equals(cs2) shouldBeEqualTo false
    cs2.hoverEvent = cs1.hoverEvent
    cs1.insertion = "Insertion"
    cs1.equals(cs2) shouldBeEqualTo false
    cs2.insertion = cs1.insertion
    cs1.font = "default"
    cs1.equals(cs2) shouldBeEqualTo false
    cs2.font = cs1.font
    cs1.equals(cs2) shouldBeEqualTo true
  }

//  @Test fun `ChatStyle - ROOT - These operations should not be supported`() {
//    val rootField = ChatStyle::class.java.getDeclaredField("ROOT")
//      ?: return
//    rootField.isAccessible = true
//    val root = rootField.get(null) as ChatStyle
//    invoking { root.setParent(null) } shouldThrow UnsupportedOperationException::class
//    invoking { root.setColor(null) } shouldThrow UnsupportedOperationException::class
//    invoking { root.setBold(null) } shouldThrow UnsupportedOperationException::class
//    invoking { root.setItalic(null) } shouldThrow UnsupportedOperationException::class
//    invoking { root.setStrikethrough(null) } shouldThrow UnsupportedOperationException::class
//    invoking { root.setUnderlined(null) } shouldThrow UnsupportedOperationException::class
//    invoking { root.setObfuscated(null) } shouldThrow UnsupportedOperationException::class
//    invoking { root.setClickEvent(null) } shouldThrow UnsupportedOperationException::class
//    invoking { root.setHoverEvent(null) } shouldThrow UnsupportedOperationException::class
//    invoking { root.setInsertion(null) } shouldThrow UnsupportedOperationException::class
//    root.toString() shouldBeEqualTo "ChatStyle.ROOT"
//  }

  @Test fun `ChatStyle - setParent`() {
    val self = ChatStyle().setColor(Color.of(0))
    self.setParent(ChatStyle.EMPTY) shouldBeEqualTo self // because parent is empty

    var n = ChatStyle.EMPTY.setParent(ChatStyle().setColor(Color.of(0xff0000)))
    n.color shouldBeEqualTo Color.of(0xff0000)
    n = n.setParent(ChatStyle().setBold(true))
    n.bold shouldBeEqualTo true
    n = n.setParent(ChatStyle().setItalic(true))
    n.italic shouldBeEqualTo true
    n = n.setParent(ChatStyle().setUnderlined(true))
    n.underlined shouldBeEqualTo true
    n = n.setParent(ChatStyle().setStrikethrough(true))
    n.strikethrough shouldBeEqualTo true
    n = n.setParent(ChatStyle().setObfuscated(true))
    n.obfuscated shouldBeEqualTo true
    n = n.setParent(ChatStyle().setClickEvent(ChatClickEvent(ChatClickEvent.Action.RUN_COMMAND, "/say hi")))
    n.clickEvent shouldBeEqualTo ChatClickEvent(ChatClickEvent.Action.RUN_COMMAND, "/say hi")
    n = n.setParent(ChatStyle().setHoverEvent(ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT, ChatComponentText("hi"))))
    n.hoverEvent shouldBeEqualTo ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT, ChatComponentText("hi"))
    n = n.setParent(ChatStyle().setInsertion("insertion"))
    n.insertion shouldBeEqualTo "insertion"
    n = n.setParent(ChatStyle().setFont("default"))
    n.font shouldBeEqualTo "default"
    n.isEmpty() shouldBe false

    ChatStyle().setFont("default").setParent(ChatStyle().setFont("parent")).font shouldBeEqualTo "default"
  }

  @Test fun `ChatStyle - isEmpty`() {
    val s = ChatStyle()
    s.isEmpty() shouldBe true
    s.setFont("default").isEmpty() shouldBe false
    s.setInsertion("insertion").isEmpty() shouldBe false
    s.setHoverEvent(ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT, ChatComponentText("hi"))).isEmpty() shouldBe false
    s.setClickEvent(ChatClickEvent(ChatClickEvent.Action.RUN_COMMAND, "/say hi")).isEmpty() shouldBe false
  }
}
