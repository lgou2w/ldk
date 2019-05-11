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

import com.google.gson.JsonSyntaxException
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
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
}
