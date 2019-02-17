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
 * ## ChatHoverEvent (聊天移动事件)
 *
 * @see [ChatComponent]
 * @see [ChatStyle]
 * @see [ChatStyle.setHoverEvent]
 * @author lgou2w
 * @param action Hover action type.
 * @param action 点击交互类型.
 * @param value Action value.
 * @param value 交互值.
 */
data class ChatHoverEvent(
        /**
         * * The action type of this chat hover event.
         * * 此聊天移动事件的交互类型.
         *
         * @see [Action]
         */
        val action: Action,
        /**
         * * The action value of this chat hover event.
         * * 此聊天移动事件的交互值.
         */
        val value: ChatComponent) {

    /**
     * ## Action (交互类型)
     *
     * @see [ChatHoverEvent]
     * @author lgou2w
     */
    enum class Action {

        /**
         * * Chat Hover Event: Show Text
         * * 聊天移动事件: 显示文本
         */
        SHOW_TEXT,
        /**
         * * Chat Hover Event: Show Achievement
         * * 聊天移动事件: 显示成就
         *
         * > * Since Minecraft 1.12, this no longer exists. advancements instead simply use [SHOW_TEXT].
         *      The ID of an achievement or statistic to display.
         *
         * > * 自从 Minecraft 1.12, 这个不再存在了. 进度只需使用 [SHOW_TEXT]. 要显示的进度 ID 或统计信息.
         *
         * ### Sample:
         * ```json
         * {"action":"show_text","value":"achievement.openInventory"}
         * ```
         */
        SHOW_ACHIEVEMENT,
        /**
         * * Chat Hover Event: Show Item
         * * 聊天移动事件: 显示物品栈
         */
        SHOW_ITEM,
        /**
         * * Chat Hover Event: Show Entity
         * * 聊天移动事件: 显示实体
         */
        SHOW_ENTITY,
        ;
    }
}
