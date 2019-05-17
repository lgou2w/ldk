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

class ChatComponentTextTest {

    @Test fun `ChatComponentText - test`() {
        val component = ChatComponentText()
        component.text shouldEqual ""
        component.text = "abc"
        component.text shouldEqual "abc"
        val component2 = ChatComponentText(component)
        component2.text shouldEqual component.text
        component2.equals(component) shouldEqual true
        component2.equals(null) shouldEqual false
        component2.setText("cba")
        component2.equals(component) shouldEqual false
        component2.equals(component2) shouldEqual true
        component2.equals(ChatComponentText("cba")) shouldEqual true
        component2.hashCode() shouldNotEqual component.hashCode()
        component2.style.setBold(true)
        component2.equals(ChatComponentText("cba")) shouldEqual false
    }
}
