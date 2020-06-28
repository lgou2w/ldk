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

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotBe
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotContain
import org.amshove.kluent.shouldThrow
import org.junit.Test

class ChatSerializerTest {

  @Test fun `ChatSerializer - fromJson`() {
    val json = "{\"text\":\"HelloWorld\",\"color\":\"red\"}"
    val compound = ChatSerializer.fromJson(json)
    compound shouldBeInstanceOf ChatComponentText::class
    compound.style.color shouldBeEqualTo ChatColor.RED
    (compound as ChatComponentText).text shouldBeEqualTo "HelloWorld"
    ChatSerializer.fromJsonOrLenient(json) shouldNotBeEqualTo null
    ChatSerializer.fromJsonOrNull(null) shouldBeEqualTo null
    ChatSerializer.fromJsonOrNull(json) shouldBeEqualTo compound
  }

  @Test fun `ChatSerializer - fromJsonLenient`() {
    val jsonLenient = "{ keybind : ctrl; color : red }"
    invoking { ChatSerializer.fromJson(jsonLenient) } shouldThrow JsonSyntaxException::class
    invoking { ChatSerializer.fromJsonOrNull(jsonLenient) } shouldThrow JsonSyntaxException::class
    ChatSerializer.fromJsonLenient(jsonLenient) shouldNotBeEqualTo null
    ChatSerializer.fromJsonOrLenient(jsonLenient) shouldBeInstanceOf ChatComponentKeybind::class
    (ChatSerializer.fromJsonOrLenient(jsonLenient) as ChatComponentKeybind).keybind shouldBeEqualTo "ctrl"
    (ChatSerializer.fromJsonOrLenient(jsonLenient) as ChatComponentKeybind).toRaw(false) shouldBeEqualTo ""
  }

  @Test fun `ChatSerializer - fromRaw - Null and empty should not return null`() {
    val ecct = ChatComponentText()
    ChatSerializer.fromRaw(null) shouldBeEqualTo ecct
    ChatSerializer.fromRaw("") shouldBeEqualTo ecct
    ChatSerializer.toJson(ecct) shouldBeEqualTo "{\"text\":\"\"}"
  }

  @Test fun `ChatSerializer - fromRaw - Invalid color should be white`() {
    val raw = "§pInvalid color code"
    val component = ChatSerializer.fromRaw(raw)
    component.toRaw() shouldBeEqualTo raw
  }

  @Test fun `ChatSerializer - fromRaw - Only formatter`() {
    val raw = "§lHello"
    val component = ChatSerializer.fromRaw(raw)
    component.toRaw(false) shouldBeEqualTo "Hello"
    component.toRaw() shouldBeEqualTo raw
  }

  @Test fun `ChatSerializer - fromRaw` () {
    val raw = "&a&l&n&o&m&kHello&r&gWorld!"
    val component = ChatSerializer.fromRaw(raw)
    component.toRaw(false) shouldBeEqualTo raw.toColor().stripColor()
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
      .text shouldBeEqualTo "CCT2" // JsonArray
    (ChatSerializer.fromJson("{\"translate\":\"key\"}") as ChatComponentTranslation)
      .withs.isEmpty() shouldBeEqualTo true // empty withs
    (ChatSerializer.fromJson("{\"translate\":\"key\",\"with\":[\"arg\"]}") as ChatComponentTranslation)
      .withs.first() shouldBeEqualTo "arg" // with ChatComponentText.text
    ((ChatSerializer.fromJson("{\"translate\":\"key\",\"with\":[{\"text\":\"arg\",\"color\":\"red\"}]}") as ChatComponentTranslation)
      .withs.first() as ChatComponentText).style.getColor() shouldBeEqualTo ChatColor.RED
    ((ChatSerializer.fromJson("{\"translate\":\"key\",\"with\":[{\"keybind\":\"ctrl\"}]}") as ChatComponentTranslation)
      .withs.first() as ChatComponentKeybind).keybind shouldBeEqualTo "ctrl"
    (ChatSerializer.fromJson("{\"text\":\"\",\"extra\":[\"Hello\",\"World\"]}") as ChatComponentText)
      .toRaw() shouldBeEqualTo "HelloWorld"
  }

  @Test fun `ChatSerializer - deserialize - The name and objective of the score should be`() {
    invoking { ChatSerializer.fromJson("{\"score\":{}}") } shouldThrow JsonParseException::class
    invoking { ChatSerializer.fromJson("{\"score\":{\"name\":\"test\"}}") } shouldThrow JsonParseException::class
    (ChatSerializer.fromJson("{\"score\":{\"name\":\"test\",\"objective\":\"target\",\"value\":\"v\"}}") as ChatComponentScore)
      .name shouldBeEqualTo "test"
  }

  @Test fun `ChatSerializer - deserialize - Don't know how to parse should throw exception`() {
    invoking { ChatSerializer.fromJson("{\"invalid\":\"chat\"}") } shouldThrow JsonParseException::class
  }

  @Test fun `ChatSerializer - deserialize - Empty extra should throw exception`() {
    invoking { ChatSerializer.fromJson("{\"text\":\"\",\"extra\":[]}") } shouldThrow JsonParseException::class
    (ChatSerializer.fromJson("{\"text\":\"\",\"extra\":[\"CCT\"]}") as ChatComponentText)
      .toRaw() shouldBeEqualTo "CCT"
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
      .setFont("default")
      .setClickEvent(ChatClickEvent(ChatClickEvent.Action.OPEN_URL, "github.com"))
      .setHoverEvent(ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT, ChatComponentText("hi")))
    ).toJson() shouldContain "Insertion"
    ChatComponentText().setStyle(ChatStyle()
      .setHoverEvent(ChatHoverEvent(ChatHoverEvent.Action.SHOW_ITEM, ChatSerializer.ChatComponentRaw("raw")))
    ).toJson() shouldContain "raw"
  }

  @Test fun `ChatSerializer - deserialize - style - validation`() {
    ChatSerializer.fromJson("{\"text\":\"\",\"bold\":true}")
      .style.bold shouldBeEqualTo true
    ChatSerializer.fromJson("{\"text\":\"\",\"bold\":true,\"italic\":true,\"underlined\":true,\"strikethrough\":true,\"obfuscated\":true," +
      "\"insertion\":\"Insertion\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"github.com\"}," +
      "\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"hi\"]}}"
    ).toRaw(false) shouldBeEqualTo ""
  }

  @Test fun `ChatSerializer - deserialize - style - Click and hover should be valid`() {
    ChatSerializer.fromJson("{\"text\":\"\",\"hoverEvent\":\"invalid\",\"clickEvent\":\"invalid\"}}")
      .style.hoverEvent shouldBeEqualTo null
    ChatSerializer.fromJson("{\"text\":\"\",\"hoverEvent\":{\"action\":\"invalid\"},\"clickEvent\":{\"action\":\"invalid\"}}")
      .style.hoverEvent shouldBeEqualTo null
  }

  @Test fun `ChatSerializer - deserialize - style - copy to clipboard`() {
    val json = "{\"text\":\"click me\",\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\"hello\"}}"
    val component = ChatSerializer.fromJson(json)
    (component) shouldBeInstanceOf ChatComponentText::class
    (component as ChatComponentText).text shouldBeEqualTo "click me"
    component.style.clickEvent shouldNotBe null
    component.style.clickEvent?.action shouldBeEqualTo ChatClickEvent.Action.COPY_TO_CLIPBOARD
    component.style.clickEvent?.value shouldBeEqualTo "hello"
  }

  @Test fun `ChatSerializer - deserialize - style - font`() {
    val json = "{\"text\":\"font\",\"font\":\"custom\"}"
    val component = ChatSerializer.fromJson(json)
    component shouldBeInstanceOf ChatComponentText::class.java
    (component as ChatComponentText).text shouldBeEqualTo "font"
    component.style.font shouldBeEqualTo "custom"
  }

  @Test fun `ChatSerializer - serialize - style - font`() {
    val component = ChatComponentText()
    component.style.font = "custom"
    component.toJson() shouldContain "custom"
  }

  @Test fun `ChatSerializer - serialize - component nbt`() {
    val ccnbb = ChatComponentNBTBlock("nbt", true, "block").toJson()
    ccnbb shouldContain "nbt"
    ccnbb shouldContain "block"
    val ccnbe = ChatComponentNBTEntity("nbt", null, "entity").toJson()
    ccnbe shouldContain "nbt"
    ccnbe shouldContain "entity"
    val ccnbs = ChatComponentNBTStorage("nbt", null, "storage").toJson()
    ccnbs shouldContain "nbt"
    ccnbs shouldContain "storage"
  }

  @Test fun `ChatSerializer - serialize - rgb color`() {
    ChatComponentText()
      .setStyle(ChatStyle.EMPTY.setColor(Color.of(0)))
      .toJson() shouldContain "black"
    val cct = ChatComponentText()
    cct.setStyle(ChatStyle.EMPTY.setColor(Color.of("#f00"))).toJson() shouldContain "#ff0000"
    cct.setStyle(ChatStyle.EMPTY.setColor(Color.of("#0f0"))).toJson() shouldContain "#00ff00"
    cct.setStyle(ChatStyle.EMPTY.setColor(Color.of("#00f"))).toJson() shouldContain "#0000ff"
  }

  @Test fun `ChatSerializer - deserialize - component nbt`() {
    val ccnbb = ChatSerializer.fromJson("{\"nbt\":\"nbt\",\"block\":\"test\"}")
    ccnbb shouldBeInstanceOf ChatComponentNBTBlock::class
    (ccnbb as ChatComponentNBTBlock).nbt shouldBeEqualTo "nbt"
    ccnbb.interpret shouldBe null
    ccnbb.path shouldBeEqualTo "test"
    val ccnbe = ChatSerializer.fromJson("{\"nbt\":\"nbt\",\"entity\":\"@s\",\"interpret\":true}")
    ccnbe shouldBeInstanceOf ChatComponentNBTEntity::class
    (ccnbe as ChatComponentNBTEntity).nbt shouldBeEqualTo "nbt"
    ccnbe.interpret shouldBe true
    ccnbe.path shouldBeEqualTo "@s"
    val ccnbs = ChatSerializer.fromJson("{\"nbt\":\"nbt\",\"storage\":\"diamond\",\"interpret\":true}")
    ccnbs shouldBeInstanceOf ChatComponentNBTStorage::class
    (ccnbs as ChatComponentNBTStorage).nbt shouldBeEqualTo "nbt"
    ccnbs.interpret shouldBe true
    ccnbs.path shouldBeEqualTo "diamond"
  }

  @Test fun `ChatSerializer - deserialize - component nbt path must be block, entity or storage`() {
    invoking { ChatSerializer.fromJson("{\"nbt\":\"nbt\"}") } shouldThrow JsonParseException::class
    invoking { ChatSerializer.fromJson("{\"nbt\":\"nbt\",\"path\":\"entity\"}") } shouldThrow JsonParseException::class
  }

  @Test fun `ChatSerializer - deserialize - style - gson`() {
    val field = ChatSerializer::class.java.getDeclaredField("GSON")
    field.isAccessible = true
    val gson = field.get(null) as Gson
    val adapter = gson.getAdapter(ChatStyle::class.java)
    adapter.fromJson("[]") shouldBe null
    adapter.fromJson("{}") shouldBeEqualTo ChatStyle.EMPTY
  }
}
