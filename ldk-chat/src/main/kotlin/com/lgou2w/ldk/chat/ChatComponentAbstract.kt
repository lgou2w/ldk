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

import com.lgou2w.ldk.common.notNull

/**
 * ## ChatComponentAbstract (聊天组件抽象)
 *
 * @see [ChatComponent]
 * @see [ChatComponentText]
 * @see [ChatComponentTranslation]
 * @see [ChatComponentScore]
 * @see [ChatComponentSelector]
 * @see [ChatComponentKeybind]
 * @author lgou2w
 */
abstract class ChatComponentAbstract : ChatComponent {

    private var _style: ChatStyle? = null
    private var _extras: MutableList<ChatComponent> = ArrayList()

    override var style: ChatStyle
        get() {
            if (_style == null) {
                _style = ChatStyle()
                extras.forEach { it.style.setParent(style) }
            }
            return _style.notNull()
        }
        set(value) {
            _style = value
            extras.forEach { it.style.setParent(style) }
        }

    override fun setStyle(style: ChatStyle?): ChatComponent {
        _style = style
        extras.forEach { it.style.setParent(style) }
        return this
    }

    override val extras: MutableList<ChatComponent>
        get() = _extras

    override val extraSize: Int
        get() = extras.size

    override fun append(text: String): ChatComponent
            = append(ChatComponentText(text))

    override fun append(extra: ChatComponent): ChatComponent {
        extra.style.setParent(style)
        extras.add(extra)
        return this
    }

    override fun toJson(): String
            = ChatSerializer.toJson(this)

    override fun toRaw(color: Boolean): String
            = ChatSerializer.toRaw(this, color)

    // SEE: ldk-bukkit-common / com.lgou2w.ldk.bukkit.chat.ChatFactory

//    override fun send(player: Player, action: ChatAction)
//            = PacketOutChat(this, action).send(player)

    override fun plus(text: String): ChatComponent
            = plus(ChatSerializer.fromRaw(text))

    override fun plus(extra: ChatComponent): ChatComponent
            = append(extra)

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is ChatComponentAbstract)
            return style == other.style && extras == other.extras
        return false
    }

    override fun hashCode(): Int {
        var result = _style?.hashCode() ?: 0
        result = 31 * result + extras.hashCode()
        return result
    }

    override fun toString(): String {
        return "ChatComponentAbstract(style=$style, extras=$extras)"
    }
}
