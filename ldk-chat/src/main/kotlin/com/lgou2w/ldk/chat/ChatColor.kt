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

import com.lgou2w.ldk.common.Valuable
import java.util.regex.Pattern

/**
 * ## ChatColor (聊天颜色)
 *
 * @see [Valuable]
 * @author lgou2w
 */
enum class ChatColor(
        /**
         * * Enum color code.
         * * 枚举颜色代码.
         */
        val code: Char,
        /**
         * * Whether this chat color is a format character.
         * * 此聊天颜色是否为格式符.
         */
        val isFormat: Boolean = false

) : Valuable<Char> {

    /** enums */

    /**
     * * Black
     * * 黑色
     */
    BLACK('0'),
    /**
     * * Dark Blue
     * * 深蓝色
     */
    DARK_BLUE('1'),
    /**
     * * Dark Green
     * * 深绿色
     */
    DARK_GREEN('2'),
    /**
     * * Dark Aqua
     * * 深青色
     */
    DARK_AQUA('3'),
    /**
     * * Dark Red
     * * 深红色
     */
    DARK_RED('4'),
    /**
     * * Dark Purple
     * * 深紫色
     */
    DARK_PURPLE('5'),
    /**
     * * Gold
     * * 金色
     */
    GOLD('6'),
    /**
     * * Gray
     * * 灰色
     */
    GRAY('7'),
    /**
     * * Dark Gray
     * * 深灰色
     */
    DARK_GRAY('8'),
    /**
     * * Blue
     * * 蓝色
     */
    BLUE('9'),
    /**
     * * Green
     * * 绿色
     */
    GREEN('a'),
    /**
     * * Aqua
     * * 青色
     */
    AQUA('b'),
    /**
     * * Red
     * * 红色
     */
    RED('c'),
    /**
     * * Light Purple
     * * 浅紫色
     */
    LIGHT_PURPLE('d'),
    /**
     * * Yellow
     * * 黄色
     */
    YELLOW('e'),
    /**
     * * White
     * * 白色
     */
    WHITE('f'),

    /**
     * * Format: Obfuscated
     * * 格式符: 随机字符
     */
    OBFUSCATED('k', true),
    /**
     * * Format: Bold
     * * 格式符: 粗体
     */
    BOLD('l', true),
    /**
     * * Format: Strikethrough
     * * 格式符: 删除线
     */
    STRIKETHROUGH('m', true),
    /**
     * * Format: Underline
     * * 格式符: 下划线
     */
    UNDERLINE('n', true),
    /**
     * * Format: Italic
     * * 格式符: 斜体
     */
    ITALIC('o', true),
    /**
     * * Special: Reset
     * * 特殊符: 重置
     */
    RESET('r'),
    ;

    override val value: Char
        get() = code

    override fun toString(): String
            = "$CHAR_COLOR$code"

    operator fun plus(charSequence: CharSequence): String
            = toString() + charSequence

    companion object {

        /**
         * Color character
         */
        const val CHAR_COLOR = '§'
        const val CHAR_CODE = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr"

        @JvmStatic
        private val STRIP_COLOR = Pattern.compile("(?i)$CHAR_COLOR[0-9A-FK-OR]")

        /**
         * * Remove color characters from the given [input] string.
         * * 从给定的 [input] 字符串剔除颜色字符.
         *
         * @param input String to be culled
         * @param input 需要剔除的字符串
         */
        @JvmStatic
        fun stripColor(input: String): String
                = STRIP_COLOR.matcher(input).replaceAll("")

        /**
         * * Converts the given [textToTranslate] string to the specified [altColorChar] color character to a color string.
         * * 将给定的 [textToTranslate] 字符串以指定的 [altColorChar] 颜色字符转换为颜色字符串.
         *
         * @param [altColorChar] Color character
         * @param [altColorChar] 颜色字符
         * @param textToTranslate String to be converted
         * @param textToTranslate 需要转换的字符串
         */
        @JvmStatic
        fun translateAlternateColorCodes(altColorChar: Char, textToTranslate: String): String {
            val chars = textToTranslate.toCharArray()
            for (i in 0 until chars.size - 1) {
                if (chars[i] == altColorChar && CHAR_CODE.indexOf(chars[i + 1]) > -1) {
                    chars[i] = CHAR_COLOR
                    chars[i + 1] = Character.toLowerCase(chars[i + 1])
                }
            }
            return String(chars)
        }
    }
}
