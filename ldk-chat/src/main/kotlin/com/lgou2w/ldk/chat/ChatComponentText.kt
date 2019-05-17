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

/**
 * ## ChatComponentText (聊天组件文本)
 *
 * @see [ChatComponent]
 * @see [ChatComponentAbstract]
 * @author lgou2w
 * @constructor ChatComponentText
 * @param text Text.
 * @param text 文本.
 */
class ChatComponentText(
        /**
         * * Gets or sets the text object for this chat component text.
         * * 获取或设置此聊天组件文本的文本对象.
         */
        var text: String
) : ChatComponentAbstract() {

    /**
     * @constructor ChatComponentText
     *
     * * Using `empty` string.
     * * 使用 `空` 字符串.
     */
    constructor() : this("")

    /**
     * @constructor ChatComponentText
     *
     * @param text Chat component text.
     * @param text 聊天组件文本.
     */
    constructor(text: ChatComponentText) : this(text.text)

    fun setText(text: String): ChatComponentText {
        this.text = text
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is ChatComponentText)
            return super.equals(other) && text == other.text
        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }

    override fun toString(): String {
        return "ChatComponentText(text='$text', style=$style, extras=$extras)"
    }
}
