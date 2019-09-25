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
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotEqual
import org.amshove.kluent.shouldThrow
import org.junit.Test

class ChatTest {

  @Test fun `Chat - toColor`() {
    val raw = "&aGreen"
    val target = "§aGreen"
    raw.toColor() shouldEqual target
    arrayOf(raw).toColor()[0] shouldEqual target
    listOf(raw).toColor().first() shouldEqual target
    "&Green".toColor() shouldEqual "&Green"
  }

  @Test fun `Chat - stripColor`() {
    val raw = "§aGreen"
    val target = "Green"
    raw.stripColor() shouldEqual target
    arrayOf(raw).stripColor()[0] shouldEqual target
    listOf(raw).stripColor().first() shouldEqual target
  }

  @Test fun `Chat - toComponent`() {
    val raw = "&aGreen"
    val component = raw.toComponent()
    component shouldBeInstanceOf ChatComponentText::class
    component.extraSize shouldEqual 1
    component.extras.first().style.color shouldEqual ChatColor.GREEN
    (component.extras.first() as ChatComponentText).text shouldEqual "Green"
    component.toRaw() shouldEqual "§aGreen"
    component.toRaw(false) shouldEqual "Green"
  }

  @Test fun `Chat - toComponentOrNull`() {
    var str : String? = null
    str.toComponentOrNull() shouldEqual null
    str = "Text"
    str.toComponentOrNull()?.toRaw() shouldEqual "Text"
  }

  @Test fun `ChatEvent - test`() {
    val cce = ChatClickEvent(ChatClickEvent.Action.OPEN_URL, "github.com")
    cce.action shouldEqual ChatClickEvent.Action.OPEN_URL
    cce.value shouldEqual "github.com"
    val che = ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT, ChatComponentText("hi"))
    che.action shouldEqual ChatHoverEvent.Action.SHOW_TEXT
    che.value shouldEqual ChatComponentText("hi")
  }

  @Test fun `ChatComponent - NULL`() {
    ChatComponent.NULL shouldNotEqual null
    ChatComponent.NULL shouldBeInstanceOf ChatComponent::class
    ChatComponent.NULL shouldBe ChatComponent.NULL
    ChatComponent.NULL.hashCode() shouldEqual 0
    ChatComponent.NULL.extraSize shouldEqual 0
    ChatComponent.NULL.equals(ChatComponent.NULL) shouldEqual true
    ChatComponent.NULL.equals(ChatComponentText()) shouldEqual false
    invoking { ChatComponent.NULL.style = ChatStyle(); null } shouldThrow UnsupportedOperationException::class
    invoking { ChatComponent.NULL.style } shouldThrow UnsupportedOperationException::class
    invoking { ChatComponent.NULL.setStyle(null) } shouldThrow UnsupportedOperationException::class
    invoking { ChatComponent.NULL.extras } shouldThrow UnsupportedOperationException::class
    invoking { ChatComponent.NULL.append("") } shouldThrow UnsupportedOperationException::class
    invoking { ChatComponent.NULL.append(ChatComponentText()) } shouldThrow UnsupportedOperationException::class
    invoking { ChatComponent.NULL.toJson() } shouldThrow UnsupportedOperationException::class
    invoking { ChatComponent.NULL.toRaw(false) } shouldThrow UnsupportedOperationException::class
    invoking { ChatComponent.NULL.plus("") } shouldThrow UnsupportedOperationException::class
    invoking { ChatComponent.NULL.plus(ChatComponentText()) } shouldThrow UnsupportedOperationException::class
  }
}
