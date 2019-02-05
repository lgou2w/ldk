/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
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
 * ## ChatComponentKeybind (聊天组件按键)
 *
 * @see [ChatComponent]
 * @see [ChatComponentAbstract]
 * @author lgou2w
 * @constructor ChatComponentKeybind
 * @param keybind Keybind
 * @param keybind 按键
 */
class ChatComponentKeybind(
        /**
         * * Gets or sets the keybind object for this chat component keybind.
         * * 获取或设置此聊天组件按键的按键对象.
         */
        var keybind: String

) : ChatComponentAbstract() {

    /**
     * @see [ChatComponentKeybind.keybind]
     */
    fun setKeybind(keybind: String): ChatComponentKeybind {
        this.keybind = keybind
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is ChatComponentKeybind)
            return super.equals(other) && keybind == other.keybind
        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + keybind.hashCode()
        return result
    }

    override fun toString(): String {
        return "ChatComponentKeybind(keybind='$keybind', style=$style, extras=$extras)"
    }
}
