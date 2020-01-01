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

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * ## ExternalProvider (外部语言提供者)
 *
 * @see [LanguageProvider]
 * @author lgou2w
 */
class ExternalProvider(
  /**
   * * A directory of external language provider.
   * * 此外部语言提供者的目录.
   */
  val directory : File
) : LanguageProvider {

  override fun load(name: String): InputStream? {
    val file = File(directory, name)
    if (file.parentFile?.exists() != true)
      file.parentFile?.mkdirs()
    if (!file.exists())
      return null
    return FileInputStream(file)
  }

  override fun isValid(name: String): Boolean {
    return File(directory, name).exists()
  }

  override fun write(name: String): OutputStream {
    val file = File(directory, name)
    return FileOutputStream(file)
  }
}
