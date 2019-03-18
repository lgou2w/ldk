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
 * ## ChatComponentTranslation (聊天组件翻译)
 *
 * @see [ChatComponent]
 * @see [ChatComponentAbstract]
 * @author lgou2w
 * @constructor ChatComponentTranslation
 * @param key Key.
 * @param key 键.
 * @param withs Parameters.
 * @param withs 参数.
 */
class ChatComponentTranslation(
        /**
         * * Gets or sets the key object for this chat component translation.
         * * 获取或设置此聊天组件翻译的键对象.
         */
        var key: String,
        /**
         * * Gets or sets the withs list object for this chat component translation.
         * * 获取或设置此聊天组件翻译的参数列表对象.
         */
        var withs: MutableList<Any> = ArrayList()
) : ChatComponentAbstract() {

    /**
     * @see [ChatComponentTranslation.key]
     */
    fun setKey(key: String): ChatComponentTranslation {
        this.key = key
        return this
    }

    /**
     * @see [ChatComponentTranslation.withs]
     */
    fun addWiths(withs: Array<Any>): ChatComponentTranslation {
        this.withs.addAll(withs)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is ChatComponentTranslation)
            return super.equals(other) && key == other.key && withs == other.withs
        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + key.hashCode()
        result = 31 * result + withs.hashCode()
        return result
    }

    override fun toString(): String {
        return "ChatComponentTranslation(key='$key', withs=$withs, style=$style, extras=$extras)"
    }
}
