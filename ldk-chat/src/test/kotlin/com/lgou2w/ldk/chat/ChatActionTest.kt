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
import org.junit.Test

class ChatActionTest {

    @Test fun `ChatAction - test`() {
        ChatAction.CHAT.id shouldEqual 0
        ChatAction.CHAT.value shouldEqual 0
    }

    @Test fun `ChatAction - fromValue`() {
        ChatAction.fromValue(0) shouldEqual ChatAction.CHAT
        ChatAction.fromValue(1) shouldEqual ChatAction.SYSTEM
        ChatAction.fromValue(2) shouldEqual ChatAction.ACTIONBAR
    }

    @Test fun `ChatAction - fromValue - Invalid value should always be CHAT`() {
        ChatAction.fromValue(404) shouldEqual ChatAction.CHAT
        ChatAction.fromValue(0) shouldEqual ChatAction.CHAT
    }
}
