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

package com.lgou2w.ldk.i18n

import java.io.IOException
import java.util.Locale

/**
 * ## Language (语言)
 *
 * @see [SimpleLanguage]
 * @author lgou2w
 */
interface Language {

  /**
   * * The manager object for this language.
   * * 此语言的管理器对象.
   *
   * @since LDK 0.1.7-rc2
   */
  val manager : LanguageManager

  /**
   * * Local to this language.
   * * 此语言的本地.
   */
  val locale : Locale

  /**
   * * Formatter of this language.
   * * 此语言的格式化.
   */
  var formatter : Formatter?

  /**
   * * The number size of this language.
   * * 此语言的数量大小.
   */
  val size : Int

  /**
   * * The set of keys for this language.
   * * 此语言的键集合.
   */
  val keys : Set<String>

  /**
   * * A set of key-value pairs for this language.
   * * 此语言的键值对集合.
   */
  val entries : Set<Map.Entry<String, String>>

  /**
   * * Save this language.
   * * 将此语言进行保存.
   *
   * @throws [IOException] I/O
   * @since LDK 0.1.7-rc2
   */
  @Throws(IOException::class)
  fun save()

  /**
   * * Get the language value from the given [key].
   * * 从给定的键 [key] 获取语言值.
   */
  operator fun get(key: String): String?

  /**
   * * Get the language value from the given [key] and the [args].
   * * 从给定的键 [key] 和参数 [args] 获取语言值.
   */
  fun get(key: String, vararg args: Any?): String?

  /**
   * * Gets the language value from the given [key], or returns the [def] value if it does not exist.
   * * 从给定的键 [key] 获取语言值, 如果不存在则返回 [def] 值.
   */
  fun getOr(key: String, def: String?): String?

  /**
   * * Gets the language value from the given [key] and the [args], and returns the [def] value if it does not exist.
   * * 从给定的键 [key] 和参数 [args] 获取语言值, 如果不存在则返回 [def] 值.
   */
  fun getOr(key: String, def: String?, vararg args: Any?): String?

  /**
   * * Set the given [key] to the given language [value].
   * * 将给定的键 [key] 设置给定的语言值 [value].
   */
  operator fun set(key: String, value: String)

  /**
   * * Gets the given [key] if there is a language value.
   * * 获取给定的键 [key] 是否存在语言值.
   */
  fun has(key: String): Boolean

  /**
   * * Clear all language values.
   * * 清除所有的语言值.
   */
  fun clear()

  /**
   * * Adds a given language key-value pair mapping [entries] to this language object.
   * * 将给定的语言键值对映射 [entries] 添加到此语言对象内.
   */
  fun addAll(entries: Map<String, String>)
}
