/*
 * Copyright (C) 2016-2020 The lgou2w <lgou2w@hotmail.com>
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

  override var style = ChatStyle()
  override val extras = ArrayList<ChatComponent>()

  override fun setStyle(style: ChatStyle?): ChatComponent {
    this.style = style ?: ChatStyle()
    return this
  }

  override val extraSize : Int
    get() = extras.size

  override fun append(text: String): ChatComponent
    = append(ChatComponentText(text))

  override fun append(extra: ChatComponent): ChatComponent {
    extras.add(extra)
    return this
  }

  override fun toJson(): String
    = ChatSerializer.toJson(this)

  override fun toRaw(color: Boolean): String
    = ChatSerializer.toRaw(this, color)

// SEE: ldk-bukkit-common / com.lgou2w.ldk.bukkit.chat.ChatFactory
//
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
    var result = style.hashCode()
    result = 31 * result + extras.hashCode()
    return result
  }

  override fun toString(): String {
    return "ChatComponentAbstract(style=$style, extras=$extras)"
  }
}
