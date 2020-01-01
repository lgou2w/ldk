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

package com.lgou2w.ldk.plugin

import java.util.Properties
import java.util.jar.JarFile

/**
 * @author lgou2w
 * @since LDK 0.1.9
 */
class PropertiesMetadataReader : PluginMetadataReader {

  override val metadataName = "plugin.properties"

  override fun read(jarFile: JarFile): PluginMetadata {
    val entry = jarFile.getJarEntry(metadataName)
    val input = jarFile.getInputStream(entry)
    return try {
      val properties = Properties().apply { load(input) }
      val name = properties.getProperty("name")
      val main = properties.getProperty("main")
      val version = properties.getProperty("version")
      val website : String? = properties.getProperty("website")
      val description : String? = properties.getProperty("description")
      val authors = properties.getProperty("author")?.split(",") ?: emptyList()
      PluginMetadata(
        name, main, version, website, description, authors
      )
    } catch (e: Exception) {
      throw InvalidMetadataException(e)
    } finally {
      try {
        input.close()
      } catch (e: Exception) {
      }
    }
  }
}
