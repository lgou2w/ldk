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

import java.io.InputStream
import java.io.OutputStream

/**
 * ## LanguageAdapter (语言适配器)
 *
 * @see [PropertiesAdapter]
 * @see [LanguageManager.adapter]
 * @author lgou2w
 */
interface LanguageAdapter {

  /**
   * * The file extension of this language adapter.
   * * 此语言适配器的文件扩展名.
   */
  val fileExtension : String

  /**
   * * Adapt the given [input] stream to a language key-value pair map.
   * * 将给定的输入流 [input] 适配为语言键值对映射.
   *
   * **Note: The caller close the input stream.**
   */
  fun adapt(input: InputStream): Map<String, String>

  /**
   * * Re-adapt the given language key-value mapping to the [output] stream.
   * * 将给定的语言键值对映射重新适配到输出流 [output] 中.
   *
   * **Note: The caller close the output stream.**
   */
  fun readapt(output: OutputStream, entries: MutableMap<String, String>)
}
