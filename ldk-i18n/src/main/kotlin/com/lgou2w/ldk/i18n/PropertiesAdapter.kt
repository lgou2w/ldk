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

package com.lgou2w.ldk.i18n

import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset
import java.util.Collections
import java.util.Enumeration
import java.util.Properties

/**
 * ## PropertiesAdapter (属性文件适配器)
 *
 * @see [LanguageAdapter]
 * @author lgou2w
 */
class PropertiesAdapter @JvmOverloads constructor(
  /**
   * * The encoding of this property file.
   * * 此属性文件的编码.
   */
  val charset: Charset = Charsets.UTF_8
) : LanguageAdapter {

  override val fileExtension : String = "properties"

  override fun adapt(input: InputStream): Map<String, String> {
    val properties = LinkedProperties()
    val entries = LinkedHashMap<String, String>()
    properties.load(InputStreamReader(input, charset))
    properties.keys.forEach { entries[it.toString()] = properties.getProperty(it.toString()) }
    properties.clear()
    return entries
  }

  override fun readapt(output: OutputStream, entries: MutableMap<String, String>) {
    val properties = LinkedProperties()
    entries.forEach { properties.setProperty(it.key, it.value) }
    properties.store(OutputStreamWriter(output, charset), null)
    properties.clear()
  }

  private class LinkedProperties : Properties() {
    companion object {
      private const val serialVersionUID = -4334218671926846212L
    }
    override val keys : MutableSet<Any>
      = Collections.synchronizedSet(LinkedHashSet<Any>())
    override fun keys(): Enumeration<Any> {
      return Collections.enumeration(keys)
    }
    override fun put(key: Any, value: Any?): Any? {
      keys.add(key)
      return super.put(key, value)
    }
    override fun stringPropertyNames(): MutableSet<String> {
      val set = LinkedHashSet<String>()
      keys.forEach { set.add(it.toString()) }
      return set
    }
  }
}
