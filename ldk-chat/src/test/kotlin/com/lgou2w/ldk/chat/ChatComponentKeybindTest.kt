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
import org.junit.Test

class ChatComponentKeybindTest {

  @Test fun `ChatComponentKeybind - test`() {
    val cck = ChatComponentKeybind("ctrl")
    cck.keybind shouldEqual "ctrl"
    cck.keybind = "ctrl + shift"
    cck.keybind shouldEqual "ctrl + shift"
    cck.setKeybind("ctrl").keybind shouldEqual "ctrl"
    cck.hashCode() shouldBeGreaterThan 1
    cck.equals(cck) shouldEqual true
    cck.equals(ChatComponentKeybind("ctrl")) shouldEqual true
    cck.equals(ChatComponentKeybind("shift")) shouldEqual false
    cck.equals(null) shouldEqual false
    cck.style.color = ChatColor.RED
    cck.equals(ChatComponentKeybind("ctrl")) shouldEqual false
    cck.toJson() shouldContain "ctrl"
    (ChatSerializer.fromJson(cck.toJson()) as ChatComponentKeybind).keybind shouldEqual "ctrl"
  }
}
