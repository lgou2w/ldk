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
 * ## ChatComponentSelector (聊天组件选择器)
 *
 * @see [ChatComponent]
 * @see [ChatComponentAbstract]
 * @author lgou2w
 * @constructor ChatComponentSelector
 * @param selector Selector.
 * @param selector 选择器.
 */
class ChatComponentSelector(
        /**
         * * Gets or sets the selector object for this chat component selector.
         * * 获取或设置此聊天组件选择器的选择器对象.
         */
        var selector: String

) : ChatComponentAbstract() {

    /**
     * @see [ChatComponentSelector.selector]
     */
    fun setSelector(selector: String): ChatComponentSelector {
        this.selector = selector
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is ChatComponentSelector)
            return super.equals(other) && selector == other.selector
        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + selector.hashCode()
        return result
    }

    override fun toString(): String {
        return "ChatComponentSelector(selector='$selector', style=$style, extras=$extras)"
    }
}
