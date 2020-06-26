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
 * ## ChatStyle (聊天样式)
 *
 * @author lgou2w
 */
class ChatStyle {

  companion object {
    @JvmField val EMPTY = ChatStyle()
  }

  @JvmField internal var color : Color? = null
  @JvmField internal var bold : Boolean? = null
  @JvmField internal var italic : Boolean? = null
  @JvmField internal var underlined : Boolean? = null
  @JvmField internal var strikethrough : Boolean? = null
  @JvmField internal var obfuscated : Boolean? = null
  @JvmField internal var clickEvent : ChatClickEvent? = null
  @JvmField internal var hoverEvent : ChatHoverEvent? = null
  @JvmField internal var insertion : String? = null
  @JvmField internal var font : String? = null

  /**
   * @see [Color]
   * @since LDK 0.2.1
   */
  fun getColor(): Color?
    = color

  /**
   * @see [ChatColor.BOLD]
   */
  fun getBold(): Boolean?
    = bold

  /**
   * @see [ChatColor.ITALIC]
   */
  fun getItalic(): Boolean?
    = italic

  /**
   * @see [ChatColor.STRIKETHROUGH]
   */
  fun getStrikethrough(): Boolean?
    = strikethrough

  /**
   * @see [ChatColor.UNDERLINE]
   */
  fun getUnderlined(): Boolean?
    = underlined

  /**
   * @see [ChatColor.OBFUSCATED]
   */
  fun getObfuscated(): Boolean?
    = obfuscated

  /**
   * @see [ChatClickEvent]
   */
  fun getClickEvent(): ChatClickEvent?
    = clickEvent

  /**
   * @see [ChatHoverEvent]
   */
  fun getHoverEvent(): ChatHoverEvent?
    = hoverEvent

  fun getInsertion(): String?
    = insertion

  /**
   * @since LDK 0.2.1
   */
  fun getFont(): String?
    = font

  fun setParent(parent: ChatStyle): ChatStyle {
    if (this == EMPTY)
      return parent
    if (parent == EMPTY)
      return this
    val copy = ChatStyle()
    copy.color = color ?: parent.color
    copy.bold = bold ?: parent.bold
    copy.italic = italic ?: parent.italic
    copy.underlined = underlined ?: parent.underlined
    copy.strikethrough = strikethrough ?: parent.strikethrough
    copy.obfuscated = obfuscated ?: parent.obfuscated
    copy.clickEvent = clickEvent ?: parent.clickEvent
    copy.hoverEvent = hoverEvent ?: parent.hoverEvent
    copy.insertion = insertion ?: parent.insertion
    copy.font = font ?: parent.font
    return copy
  }

  /**
   * @see [Color]
   * @since LDK 0.2.1
   */
  fun setColor(color: Color?): ChatStyle {
    val copy = clone()
    copy.color = color
    return copy
  }

  /**
   * @see [ChatColor.BOLD]
   */
  fun setBold(bold: Boolean?): ChatStyle {
    val copy = clone()
    copy.bold = bold
    return copy
  }

  /**
   * @see [ChatColor.ITALIC]
   */
  fun setItalic(italic: Boolean?): ChatStyle {
    val copy = clone()
    copy.italic = italic
    return copy
  }

  /**
   * @see [ChatColor.STRIKETHROUGH]
   */
  fun setStrikethrough(strikethrough: Boolean?): ChatStyle {
    val copy = clone()
    copy.strikethrough = strikethrough
    return copy
  }

  /**
   * @see [ChatColor.UNDERLINE]
   */
  fun setUnderlined(underlined: Boolean?): ChatStyle {
    val copy = clone()
    copy.underlined = underlined
    return copy
  }

  /**
   * @see [ChatColor.OBFUSCATED]
   */
  fun setObfuscated(obfuscated: Boolean?): ChatStyle {
    val copy = clone()
    copy.obfuscated = obfuscated
    return copy
  }

  /**
   * @see [ChatClickEvent]
   */
  fun setClickEvent(clickEvent: ChatClickEvent?): ChatStyle {
    val copy = clone()
    copy.clickEvent = clickEvent
    return copy
  }

  /**
   * @see [ChatHoverEvent]
   */
  fun setHoverEvent(hoverEvent: ChatHoverEvent?): ChatStyle {
    val copy = clone()
    copy.hoverEvent = hoverEvent
    return copy
  }

  fun setInsertion(insertion: String?): ChatStyle {
    val copy = clone()
    copy.insertion = insertion
    return copy
  }

  /**
   * @since LDK 0.2.1
   */
  fun setFont(font: String?): ChatStyle {
    val copy = clone()
    copy.font = font
    return copy
  }

  /**
   * * Get this chat style is empty.
   * * 获取此聊天样式是否为空.
   */
  fun isEmpty(): Boolean
    = color == null &&
    bold == null &&
    italic == null &&
    strikethrough == null &&
    underlined == null &&
    obfuscated == null &&
    clickEvent == null &&
    hoverEvent == null &&
    insertion == null &&
    font == null

  /**
   * * Shallow a clone of this chat style object.
   * * 浅克隆一份此聊天样式的对象.
   */
  fun clone(): ChatStyle {
    val copy = ChatStyle()
    copy.color = color
    copy.bold = bold
    copy.italic = italic
    copy.strikethrough = strikethrough
    copy.underlined = underlined
    copy.obfuscated = obfuscated
    copy.clickEvent = clickEvent
    copy.hoverEvent = hoverEvent
    copy.insertion = insertion
    copy.font = font
    return copy
  }

  override fun equals(other: Any?): Boolean {
    if (other === this)
      return true
    if (other is ChatStyle) {
      if (color != other.color) return false
      if (bold != other.bold) return false
      if (italic != other.italic) return false
      if (underlined != other.underlined) return false
      if (strikethrough != other.strikethrough) return false
      if (obfuscated != other.obfuscated) return false
      if (clickEvent != other.clickEvent) return false
      if (hoverEvent != other.hoverEvent) return false
      if (insertion != other.insertion) return false
      if (font != other.font) return false
      return true
    }
    return false
  }

  override fun hashCode(): Int {
    var result = color.hashCode()
    result = 31 * result + bold.hashCode()
    result = 31 * result + italic.hashCode()
    result = 31 * result + underlined.hashCode()
    result = 31 * result + strikethrough.hashCode()
    result = 31 * result + obfuscated.hashCode()
    result = 31 * result + clickEvent.hashCode()
    result = 31 * result + hoverEvent.hashCode()
    result = 31 * result + insertion.hashCode()
    result = 31 * result + font.hashCode()
    return result
  }

  override fun toString(): String {
    return "ChatStyle(color=${color?.rgb}, bold=$bold, italic=$italic, underlined=$underlined, strikethrough=$strikethrough, obfuscated=$obfuscated, clickEvent=$clickEvent, hoverEvent=$hoverEvent, insertion=$insertion, font=$font)"
  }
}
