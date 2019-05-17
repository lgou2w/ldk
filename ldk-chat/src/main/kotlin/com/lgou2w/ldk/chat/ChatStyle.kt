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
 * ## ChatStyle (聊天样式)
 *
 * @author lgou2w
 */
open class ChatStyle {

    /** member */

    private var parent : ChatStyle? = null

    @JvmField internal var color : ChatColor? = null
    @JvmField internal var bold : Boolean? = null
    @JvmField internal var italic : Boolean? = null
    @JvmField internal var underlined : Boolean? = null
    @JvmField internal var strikethrough : Boolean? = null
    @JvmField internal var obfuscated : Boolean? = null
    @JvmField internal var clickEvent : ChatClickEvent? = null
    @JvmField internal var hoverEvent : ChatHoverEvent? = null
    @JvmField internal var insertion : String? = null

    /** static */

    companion object {

        @JvmStatic
        private val ROOT = object : ChatStyle() {
            override fun getColor(): ChatColor?
                    = null
            override fun getBold(): Boolean?
                    = false
            override fun getItalic(): Boolean?
                    = false
            override fun getStrikethrough(): Boolean?
                    = false
            override fun getUnderlined(): Boolean?
                    = false
            override fun getObfuscated(): Boolean?
                    = false
            override fun getClickEvent(): ChatClickEvent?
                    = null
            override fun getHoverEvent(): ChatHoverEvent?
                    = null
            override fun getInsertion(): String?
                    = null
            override fun setParent(parent: ChatStyle?): ChatStyle
                    = throw UnsupportedOperationException()
            override fun setColor(color: ChatColor?): ChatStyle
                    = throw UnsupportedOperationException()
            override fun setBold(bold: Boolean?): ChatStyle
                    = throw UnsupportedOperationException()
            override fun setItalic(italic: Boolean?): ChatStyle
                    = throw UnsupportedOperationException()
            override fun setStrikethrough(strikethrough: Boolean?): ChatStyle
                    = throw UnsupportedOperationException()
            override fun setUnderlined(underlined: Boolean?): ChatStyle
                    = throw UnsupportedOperationException()
            override fun setObfuscated(obfuscated: Boolean?): ChatStyle
                    = throw UnsupportedOperationException()
            override fun setClickEvent(clickEvent: ChatClickEvent?): ChatStyle
                    = throw UnsupportedOperationException()
            override fun setHoverEvent(hoverEvent: ChatHoverEvent?): ChatStyle
                    = throw UnsupportedOperationException()
            override fun setInsertion(insertion: String?): ChatStyle
                    = throw UnsupportedOperationException()
            override fun toString(): String
                    = "ChatStyle.ROOT"
        }
    }

    private fun getParent(): ChatStyle
            = parent ?: ROOT

    /**
     * @see [ChatColor]
     */
    open fun getColor(): ChatColor?
            = color ?: getParent().getColor()

    /**
     * @see [ChatColor.BOLD]
     */
    open fun getBold(): Boolean?
            = bold ?: getParent().getBold()

    /**
     * @see [ChatColor.ITALIC]
     */
    open fun getItalic(): Boolean?
            = italic ?: getParent().getItalic()

    /**
     * @see [ChatColor.STRIKETHROUGH]
     */
    open fun getStrikethrough(): Boolean?
            = strikethrough ?: getParent().getStrikethrough()

    /**
     * @see [ChatColor.UNDERLINE]
     */
    open fun getUnderlined(): Boolean?
            = underlined ?: getParent().getUnderlined()

    /**
     * @see [ChatColor.OBFUSCATED]
     */
    open fun getObfuscated(): Boolean?
            = obfuscated ?: getParent().getObfuscated()

    /**
     * @see [ChatClickEvent]
     */
    open fun getClickEvent(): ChatClickEvent?
            = clickEvent ?: getParent().getClickEvent()

    /**
     * @see [ChatHoverEvent]
     */
    open fun getHoverEvent(): ChatHoverEvent?
            = hoverEvent ?: getParent().getHoverEvent()

    open fun getInsertion(): String?
            = insertion ?: getParent().getInsertion()

    open fun setParent(parent: ChatStyle?): ChatStyle {
        this.parent = parent
        return this
    }

    /**
     * @see [ChatColor]
     */
    open fun setColor(color: ChatColor?): ChatStyle {
        this.color = color
        return this
    }

    /**
     * @see [ChatColor.BOLD]
     */
    open fun setBold(bold: Boolean?): ChatStyle {
        this.bold = bold
        return this
    }

    /**
     * @see [ChatColor.ITALIC]
     */
    open fun setItalic(italic: Boolean?): ChatStyle {
        this.italic = italic
        return this
    }

    /**
     * @see [ChatColor.STRIKETHROUGH]
     */
    open fun setStrikethrough(strikethrough: Boolean?): ChatStyle {
        this.strikethrough = strikethrough
        return this
    }

    /**
     * @see [ChatColor.UNDERLINE]
     */
    open fun setUnderlined(underlined: Boolean?): ChatStyle {
        this.underlined = underlined
        return this
    }

    /**
     * @see [ChatColor.OBFUSCATED]
     */
    open fun setObfuscated(obfuscated: Boolean?): ChatStyle {
        this.obfuscated = obfuscated
        return this
    }

    /**
     * @see [ChatClickEvent]
     */
    open fun setClickEvent(clickEvent: ChatClickEvent?): ChatStyle {
        this.clickEvent = clickEvent
        return this
    }

    /**
     * @see [ChatHoverEvent]
     */
    open fun setHoverEvent(hoverEvent: ChatHoverEvent?): ChatStyle {
        this.hoverEvent = hoverEvent
        return this
    }

    open fun setInsertion(insertion: String?): ChatStyle {
        this.insertion = insertion
        return this
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
            insertion == null

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
        return copy
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is ChatStyle) {
            if (parent != other.parent) return false
            if (color != other.color) return false
            if (bold != other.bold) return false
            if (italic != other.italic) return false
            if (underlined != other.underlined) return false
            if (strikethrough != other.strikethrough) return false
            if (obfuscated != other.obfuscated) return false
            if (clickEvent != other.clickEvent) return false
            if (hoverEvent != other.hoverEvent) return false
            if (insertion != other.insertion) return false
            return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result = color?.hashCode() ?: 0
        result = 31 * result + (bold?.hashCode() ?: 0)
        result = 31 * result + (italic?.hashCode() ?: 0)
        result = 31 * result + (underlined?.hashCode() ?: 0)
        result = 31 * result + (strikethrough?.hashCode() ?: 0)
        result = 31 * result + (obfuscated?.hashCode() ?: 0)
        result = 31 * result + (clickEvent?.hashCode() ?: 0)
        result = 31 * result + (hoverEvent?.hashCode() ?: 0)
        result = 31 * result + (insertion?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "ChatStyle(hasParent=${parent != null}, color=${color?.name}, bold=$bold, italic=$italic, underlined=$underlined, strikethrough=$strikethrough, obfuscated=$obfuscated, clickEvent=$clickEvent, hoverEvent=$hoverEvent, insertion=$insertion)"
    }
}
