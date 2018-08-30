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

/**
 * ## ChatComponent (聊天组件)
 *
 * * The `ChatComponent` is a `JSON` format chat message in Minecraft. [Details](https://minecraft.gamepedia.com/Commands#Raw_JSON_text)
 * * 聊天组件是 Minecraft 中的 `JSON` 格式聊天消息. [详情](https://minecraft.gamepedia.com/Commands#Raw_JSON_text)
 *
 * @see [ChatSerializer]
 * @see [ChatComponentFancy]
 * @see [ChatComponentAbstract]
 * @see [ChatComponentText]
 * @see [ChatComponentTranslation]
 * @see [ChatComponentScore]
 * @see [ChatComponentSelector]
 * @see [ChatComponentKeybind]
 * @author lgou2w
 */
interface ChatComponent {

    /**
     * * Gets or sets the style of this chat component.
     * * 获取或设置此聊天组件的样式.
     *
     * @see [setStyle]
     * @see [ChatStyle]
     */
    var style: ChatStyle

    /**
     * * Sets the style of this chat component. If `null` then use the default.
     * * 获取或设置此聊天组件的样式. 如果为 `null` 则使用默认.
     *
     * @param style Chat style.
     * @param style 聊天样式.
     * @see [ChatStyle]
     */
    fun setStyle(style: ChatStyle?): ChatComponent

    /**
     * * Get a list of extra component for this chat component.
     * * 获取此聊天组件的附加组件列表.
     */
    val extras: MutableList<ChatComponent>

    /**
     * * Get a size of extra component for this chat component.
     * * 获取此聊天组件的附加组件大小.
     */
    val extraSize: Int

    /**
     * * Appends the given string as a [ChatComponentText] to the list of extra component.
     * * 将给定字符串以 [ChatComponentText] 追加到附加组件列表.
     *
     * @see [ChatComponentText]
     * @param text Append string.
     * @param text 追加字符串.
     */
    fun append(text: String): ChatComponent

    /**
     * * Appends the given chat component to the list of extra component.
     * * 将给定聊天组件追加到附加组件列表.
     *
     * @param extra Extra component.
     * @param extra 附加组件.
     */
    fun append(extra: ChatComponent): ChatComponent

    /**
     * * Convert this chat component to a `JSON` string.
     * * 将此聊天组件转换为 `JSON` 字符串.
     *
     * @see [ChatSerializer.toJson]
     */
    fun toJson(): String

    /**
     * * Convert this chat component to a raw string.
     * * 将此聊天组件转换为源字符串.
     *
     * @see [ChatSerializer.toRaw]
     * @param color Whether it has a color.
     * @param color 是否拥有颜色.
     */
    fun toRaw(color: Boolean = true): String

    // SEE: ldk-bukkit-common / com.lgou2w.ldk.bukkit.chat.ChatFactory

//    /**
//     * * Send this chat component to a given player.
//     * * 将此聊天组件发送到给定的玩家.
//     *
//     * @see [PacketOutChat]
//     * @param player Player.
//     * @param player 玩家.
//     * @param action Chat action.
//     * @param action 聊天交互.
//     */
//    fun send(player: Player, action: ChatAction = ChatAction.CHAT)

    /**
     * @see [append]
     */
    operator fun plus(text: String): ChatComponent

    /**
     * @see [append]
     */
    operator fun plus(extra: ChatComponent): ChatComponent

    companion object {
        /**
         * ## Null of ChatComponent
         *
         * ### Sample:
         * ```kotlin
         * println(ChatComponent.NULL == null) // false
         * println(ChatComponent.NULL == ChatComponent) // false
         * println(ChatComponent.NULL is ChatComponent) // true
         * ```
         */
        val NULL: ChatComponent by lazy {
            object: ChatComponent {
                override var style: ChatStyle
                    get() = throw UnsupportedOperationException()
                    @Suppress("UNUSED_PARAMETER")
                    set(value) = throw UnsupportedOperationException()
                override fun setStyle(style: ChatStyle?): ChatComponent
                        = throw UnsupportedOperationException()
                override val extras: MutableList<ChatComponent>
                    get() = throw UnsupportedOperationException()
                override val extraSize: Int
                    get() = 0
                override fun append(text: String): ChatComponent
                        = throw UnsupportedOperationException()
                override fun append(extra: ChatComponent): ChatComponent
                        = throw UnsupportedOperationException()
                override fun toJson(): String
                        = throw UnsupportedOperationException()
                override fun toRaw(color: Boolean): String
                        = throw UnsupportedOperationException()
//                override fun send(player: Player, action: ChatAction)
//                        = throw UnsupportedOperationException()
                override fun plus(text: String): ChatComponent
                        = throw UnsupportedOperationException()
                override fun plus(extra: ChatComponent): ChatComponent
                        = throw UnsupportedOperationException()
                override fun toString(): String
                        = "ChatComponent(NULL)"
                override fun hashCode(): Int
                        = 0
                override fun equals(other: Any?): Boolean
                        = other === NULL
            }
        }
    }
}
