/*
 * Copyright (C) 2018 The lgou2w (lgou2w@hotmail.com)
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

import com.lgou2w.ldk.common.Valuable

/**
 * ## ChatAction (聊天交互)
 *
 * * Enum minecraft chat action position.
 * * 枚举 Minecraft 聊天交互位置.
 *
 * @see [Valuable]
 * @author lgou2w
 */
enum class ChatAction(
        /**
         * * Enum value.
         * * 枚举值.
         */
        val id: Int

) : Valuable<Int> {

    /**
     * * Chat Action: Chat
     * * 聊天交互: 聊天栏
     */
    CHAT(0),
    /**
     * * Chat Action: System
     * * 聊天交互: 系统栏
     */
    SYSTEM(1),
    /**
     * * Chat Action: Action Bar
     * * 聊天交互: 交互栏
     */
    ACTIONBAR(2),
    ;

    override val value: Int
        get() = id

    /** static */

    companion object {

        /**
         * * Gets the chat action object from the given enumeration value. If unknown, the result is [CHAT].
         * * 从给定的枚举值获取聊天交互对象. 如果未知则结果为 [CHAT].
         *
         * @see [ChatAction]
         * @see [ChatAction.CHAT]
         * @see [ChatAction.SYSTEM]
         * @see [ChatAction.ACTIONBAR]
         * @param value Enum value.
         * @param value 枚举值.
         */
        @JvmStatic
        fun fromValue(value: Int): ChatAction = when (value) {
            0 -> CHAT
            1 -> SYSTEM
            2 -> ACTIONBAR
            else -> CHAT // else default chat
        }
    }
}
