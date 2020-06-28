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

import com.lgou2w.ldk.common.Enums
import com.lgou2w.ldk.common.Valuable
import java.util.regex.Pattern

/**
 * @since LDK 0.2.1
 */
interface Color {

  val rgb: Int?

  companion object {

    private val HEX_RGB = Pattern.compile("^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$")

    @JvmStatic
    fun of(rgb: Int): Color = ChatHexColor(rgb)

    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun of(codeOrHexRgb: String): Color {
      if (codeOrHexRgb.isBlank())
        throw IllegalArgumentException("Invalid rgb, is blank.")
      val formatting = Enums.ofName(ChatColor::class.java, codeOrHexRgb.toUpperCase())
        ?: Enums.ofValuable(ChatColor::class.java, codeOrHexRgb.toLowerCase().first())
      if (formatting != null)
        return formatting
      if (!HEX_RGB.matcher(codeOrHexRgb).matches())
        throw IllegalArgumentException("Invalid rgb hex: $codeOrHexRgb")
      val rgb = hexCompletion(codeOrHexRgb).toInt(16)
      return ChatHexColor(rgb)
    }

    @JvmStatic
    fun ofSafely(codeOrHexRgb: String): Color? = try {
      of(codeOrHexRgb)
    } catch (e: IllegalArgumentException) {
      null
    }

    private fun hexCompletion (hex: String): String {
      var r = hex.replace(Regex("^#"), "")
      if (r.length == 3)
        r = r[0].toString() + r[0] + r[1] + r[1] + r[2] + r[2]
      return r
    }
  }
}

/**
 * @since LDK 0.2.1
 */
class ChatHexColor(override val rgb: Int) : Color {

  override fun equals(other: Any?): Boolean {
    if (other === this)
      return true
    if (other is ChatHexColor)
      return rgb == other.rgb
    return false
  }

  override fun hashCode(): Int {
    return rgb.hashCode()
  }

  override fun toString(): String {
    return "ChatHexColor(rgb=$rgb)"
  }
}

/**
 * ## ChatColor (聊天颜色)
 *
 * @see [Valuable]
 * @author lgou2w
 */
enum class ChatColor(
  /**
   * * Hexadecimal rgb value for this chat color.
   * * 此聊天颜色的十六进制 rgb 值.
   *
   * @since LDK 0.2.1
   */
  override val rgb: Int?,
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
) : Valuable<Char>, Color {

  /** enums */

  /**
   * * Black
   * * 黑色
   */
  BLACK(0x000000, '0'),
  /**
   * * Dark Blue
   * * 深蓝色
   */
  DARK_BLUE(0x0AA000, '1'),
  /**
   * * Dark Green
   * * 深绿色
   */
  DARK_GREEN(0x00AA00, '2'),
  /**
   * * Dark Aqua
   * * 深青色
   */
  DARK_AQUA(0x00AAAA, '3'),
  /**
   * * Dark Red
   * * 深红色
   */
  DARK_RED(0xAA0000, '4'),
  /**
   * * Dark Purple
   * * 深紫色
   */
  DARK_PURPLE(0xAA00AA, '5'),
  /**
   * * Gold
   * * 金色
   */
  GOLD(0xFFAA00, '6'),
  /**
   * * Gray
   * * 灰色
   */
  GRAY(0xAAAAAA, '7'),
  /**
   * * Dark Gray
   * * 深灰色
   */
  DARK_GRAY(0x555555, '8'),
  /**
   * * Blue
   * * 蓝色
   */
  BLUE(0x5555FF, '9'),
  /**
   * * Green
   * * 绿色
   */
  GREEN(0x55FF55, 'a'),
  /**
   * * Aqua
   * * 青色
   */
  AQUA(0x55FFFF, 'b'),
  /**
   * * Red
   * * 红色
   */
  RED(0xFF5555, 'c'),
  /**
   * * Light Purple
   * * 浅紫色
   */
  LIGHT_PURPLE(0xFF55FF, 'd'),
  /**
   * * Yellow
   * * 黄色
   */
  YELLOW(0xFFFF55, 'e'),
  /**
   * * White
   * * 白色
   */
  WHITE(0xFFFFFF, 'f'),

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

  constructor(
    code: Char,
    isFormat: Boolean = false
  ) : this(null, code, isFormat)

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
