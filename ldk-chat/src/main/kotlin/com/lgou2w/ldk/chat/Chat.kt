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
 * * Converts the given string to a color string with the [altColorChar] character.
 * * 将给定的字符串以 [altColorChar] 字符转换为颜色字符串.
 *
 * @param [altColorChar] Color character, default `&` value
 * @param [altColorChar] 颜色字符, 默认 `&` 值
 */
@JvmOverloads
fun String.toColor(altColorChar: Char = '&'): String
  = ChatColor.translateAlternateColorCodes(altColorChar, this)

/**
 * * Converts the given string array to an array of color strings with the [altColorChar] character.
 * * 将给定的字符串数组以 [altColorChar] 字符转换为颜色字符串数组.
 *
 * @param [altColorChar] Color character, default `&` value
 * @param [altColorChar] 颜色字符, 默认 `&` 值
 */
@JvmOverloads
fun Array<out String>.toColor(altColorChar: Char = '&'): Array<out String>
  = toList().map { it.toColor(altColorChar) }.toTypedArray()

/**
 * * Converts a given list of strings to a list of color strings with the [altColorChar] character.
 * * 将给定的字符串集合以 [altColorChar] 字符转换为颜色字符串集合.
 *
 * @param [altColorChar] Color character, default `&` value
 * @param [altColorChar] 颜色字符, 默认 `&` 值
 */
@JvmOverloads
fun Iterable<String>.toColor(altColorChar: Char = '&'): List<String>
  = map { it.toColor(altColorChar) }

/**
 * * Remove color characters from the given string.
 * * 从给定的字符串剔除颜色字符.
 */
fun String.stripColor(): String
  = ChatColor.stripColor(this)

/**
 * * Remove color characters from a given array of strings.
 * * 从给定的字符串数组剔除颜色字符.
 */
fun Array<out String>.stripColor(): Array<out String>
  = toList().map { it.stripColor() }.toTypedArray()

/**
 * * Remove color characters from a given list of strings.
 * * 从给定的字符串集合剔除颜色字符.
 */
fun Iterable<String>.stripColor(): List<String>
  = map { it.stripColor() }

/**
 * * Converts the given string source to a chat component.
 * * 将给定的字符串源转换为聊天组件.
 */
fun String.toComponent(): ChatComponent
  = ChatSerializer.fromRaw(this)

/**
 * * Converts the given string source to a chat component.
 * * 将给定的字符串源转换为聊天组件.
 */
fun String?.toComponentOrNull(): ChatComponent?
  = ChatSerializer.fromRawOrNull(this)
