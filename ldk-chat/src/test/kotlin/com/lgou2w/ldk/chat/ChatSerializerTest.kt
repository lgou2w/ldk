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

import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotContain
import org.amshove.kluent.shouldNotEqual
import org.amshove.kluent.shouldThrow
import org.junit.Test

class ChatSerializerTest {

  @Test fun `ChatSerializer - fromJson`() {
    val json = "{\"text\":\"HelloWorld\",\"color\":\"red\"}"
    val compound = ChatSerializer.fromJson(json)
    compound shouldBeInstanceOf ChatComponentText::class
    compound.style.color shouldEqual ChatColor.RED
    (compound as ChatComponentText).text shouldEqual "HelloWorld"
    ChatSerializer.fromJsonOrLenient(json) shouldNotEqual null
    ChatSerializer.fromJsonOrNull(null) shouldEqual null
    ChatSerializer.fromJsonOrNull(json) shouldEqual compound
  }

  @Test fun `ChatSerializer - fromJsonLenient`() {
    val jsonLenient = "{ keybind : ctrl; color : red }"
    invoking { ChatSerializer.fromJson(jsonLenient) } shouldThrow JsonSyntaxException::class
    invoking { ChatSerializer.fromJsonOrNull(jsonLenient) } shouldThrow JsonSyntaxException::class
    ChatSerializer.fromJsonLenient(jsonLenient) shouldNotEqual null
    ChatSerializer.fromJsonOrLenient(jsonLenient) shouldBeInstanceOf ChatComponentKeybind::class
    (ChatSerializer.fromJsonOrLenient(jsonLenient) as ChatComponentKeybind).keybind shouldEqual "ctrl"
    (ChatSerializer.fromJsonOrLenient(jsonLenient) as ChatComponentKeybind).toRaw(false) shouldEqual ""
  }

  @Test fun `ChatSerializer - fromRaw - Null and empty should not return null`() {
    val ecct = ChatComponentText()
    ChatSerializer.fromRaw(null) shouldEqual ecct
    ChatSerializer.fromRaw("") shouldEqual ecct
    ChatSerializer.toJson(ecct) shouldEqual "{\"text\":\"\"}"
  }

  @Test fun `ChatSerializer - fromRaw - Invalid color should be white`() {
    val raw = "§pInvalid color code"
    val component = ChatSerializer.fromRaw(raw)
    component.toRaw() shouldEqual raw
  }

  @Test fun `ChatSerializer - fromRaw - Only formatter`() {
    val raw = "§lHello"
    val component = ChatSerializer.fromRaw(raw)
    component.toRaw(false) shouldEqual "Hello"
    component.toRaw() shouldEqual raw
  }

  @Test fun `ChatSerializer - fromRaw` () {
    val raw = "&a&l&n&o&m&kHello&r&gWorld!"
    val component = ChatSerializer.fromRaw(raw)
    component.toRaw(false) shouldEqual raw.toColor().stripColor()
  }

  @Test fun `ChatSerializer - RawMessage`() {
    val name = "com.lgou2w.ldk.chat.ChatSerializer\$RawMessage"
    val clazz = try {
      ChatSerializerTest::class.java.classLoader.loadClass(name)
    } catch (e: ClassNotFoundException) {
      return
    }
    val inst = clazz.getConstructor(String::class.java).newInstance("") // empty
    val get = clazz.getDeclaredMethod("get")
    get.isAccessible = true
    get.invoke(inst) shouldBeInstanceOf ChatComponentText::class
  }

  @Test fun `ChatSerializer - serialize - validation`() {
    ChatComponentText().append("extraText")
      .toJson() shouldContain "extra" // extra JsonArray
    ChatComponentTranslation("key")
      .toJson() shouldNotContain "with" // empty withs
    ChatComponentTranslation("key").addWiths(arrayOf(ChatComponentText("arg")))
      .toJson() shouldContain "with" // with JsonArray
  }

  class MyComponent : ChatComponentAbstract()

  @Test fun `ChatSerializer - serialize - Special type should not be serialize`() {
    invoking { MyComponent().toJson() } shouldThrow JsonParseException::class
  }

  @Test fun `ChatSerializer - deserialize - validation`() {
    (ChatSerializer.fromJson("[\"CCT1\",\"CCT2\"]").extras.last() as ChatComponentText)
      .text shouldEqual "CCT2" // JsonArray
    (ChatSerializer.fromJson("{\"translate\":\"key\"}") as ChatComponentTranslation)
      .withs.isEmpty() shouldEqual true // empty withs
    (ChatSerializer.fromJson("{\"translate\":\"key\",\"with\":[\"arg\"]}") as ChatComponentTranslation)
      .withs.first() shouldEqual "arg" // with ChatComponentText.text
    ((ChatSerializer.fromJson("{\"translate\":\"key\",\"with\":[{\"text\":\"arg\",\"color\":\"red\"}]}") as ChatComponentTranslation)
      .withs.first() as ChatComponentText).style.getColor() shouldEqual ChatColor.RED
    ((ChatSerializer.fromJson("{\"translate\":\"key\",\"with\":[{\"keybind\":\"ctrl\"}]}") as ChatComponentTranslation)
      .withs.first() as ChatComponentKeybind).keybind shouldEqual "ctrl"
    (ChatSerializer.fromJson("{\"text\":\"\",\"extra\":[\"Hello\",\"World\"]}") as ChatComponentText)
      .toRaw() shouldEqual "HelloWorld"
  }

  @Test fun `ChatSerializer - deserialize - The name and objective of the score should be`() {
    invoking { ChatSerializer.fromJson("{\"score\":{}}") } shouldThrow JsonParseException::class
    invoking { ChatSerializer.fromJson("{\"score\":{\"name\":\"test\"}}") } shouldThrow JsonParseException::class
    (ChatSerializer.fromJson("{\"score\":{\"name\":\"test\",\"objective\":\"target\",\"value\":\"v\"}}") as ChatComponentScore)
      .name shouldEqual "test"
  }

  @Test fun `ChatSerializer - deserialize - Don't know how to parse should throw exception`() {
    invoking { ChatSerializer.fromJson("{\"invalid\":\"chat\"}") } shouldThrow JsonParseException::class
  }

  @Test fun `ChatSerializer - deserialize - Empty extra should throw exception`() {
    invoking { ChatSerializer.fromJson("{\"text\":\"\",\"extra\":[]}") } shouldThrow JsonParseException::class
    (ChatSerializer.fromJson("{\"text\":\"\",\"extra\":[\"CCT\"]}") as ChatComponentText)
      .toRaw() shouldEqual "CCT"
  }

  @Test fun `ChatSerializer - serialize - style - validation`() {
    ChatComponentText().setStyle(ChatStyle().setBold(true).setColor(null))
      .toJson() shouldNotContain "color"
    ChatComponentText().setStyle(ChatStyle()
      .setBold(true)
      .setItalic(true)
      .setUnderlined(true)
      .setStrikethrough(true)
      .setObfuscated(true)
      .setInsertion("Insertion")
      .setClickEvent(ChatClickEvent(ChatClickEvent.Action.OPEN_URL, "github.com"))
      .setHoverEvent(ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT, ChatComponentText("hi")))
    ).toJson() shouldContain "Insertion"
    ChatComponentText().setStyle(ChatStyle()
      .setHoverEvent(ChatHoverEvent(ChatHoverEvent.Action.SHOW_ITEM, ChatSerializer.ChatComponentRaw("raw")))
    ).toJson() shouldContain "raw"
  }

  @Test fun `ChatSerializer - deserialize - style - validation`() {
    ChatSerializer.fromJson("{\"text\":\"\",\"bold\":true}")
      .style.bold shouldEqual true
    ChatSerializer.fromJson("{\"text\":\"\",\"bold\":true,\"italic\":true,\"underlined\":true,\"strikethrough\":true,\"obfuscated\":true," +
      "\"insertion\":\"Insertion\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"github.com\"}," +
      "\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"hi\"]}}"
    ).toRaw(false) shouldEqual ""
  }

  @Test fun `ChatSerializer - deserialize - style - Click and hover should be valid`() {
    ChatSerializer.fromJson("{\"text\":\"\",\"hoverEvent\":\"invalid\",\"clickEvent\":\"invalid\"}}")
      .style.hoverEvent shouldEqual null
    ChatSerializer.fromJson("{\"text\":\"\",\"hoverEvent\":{\"action\":\"invalid\"},\"clickEvent\":{\"action\":\"invalid\"}}")
      .style.hoverEvent shouldEqual null
  }
}
