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

import com.lgou2w.ldk.common.notNull
import java.io.File
import java.net.MalformedURLException
import java.net.URLClassLoader
import java.util.concurrent.ConcurrentHashMap

/**
 * @author lgou2w
 * @since LDK 0.1.9
 */
class JavaPluginClassLoader
@Throws(InvalidPluginException::class, MalformedURLException::class)
constructor(
  private val loader: JavaPluginLoader,
  parent: ClassLoader,
  private val metadata: PluginMetadata,
  private val dataFolder: File,
  private val file: File
): URLClassLoader(
  arrayOf(file.toURI().toURL()), parent
) {

  companion object {
    init {
      registerAsParallelCapable()
    }
  }

  private val classes : MutableMap<String, Class<*>>
  private var pluginInitialize : JavaPlugin? = null
  internal var plugin : JavaPlugin private set

  override fun findClass(name: String): Class<*>? {
    return findClass(name, true)
  }

  @Throws(ClassNotFoundException::class)
  internal fun findClass(name: String, global: Boolean): Class<*>? {
    var result = classes[name]
    if (result == null) {
      if (global)
        result = loader.getClassByName(name)
      if (result == null) {
        result = super.findClass(name)
        if (result != null)
          loader.setClass(name, result)
      }
      classes[name] = result.notNull()
    }
    return result
  }

  internal val classNames : Set<String>
    get() = classes.keys

  @Synchronized
  internal fun initialize(plugin: JavaPlugin) {
    require(plugin.javaClass.classLoader == this) { "The plugin could not be initialized because it is not the current plugin class loader object." }
    require(pluginInitialize == null) { "The plugin has been initialized." }
    pluginInitialize = plugin
    plugin.initialize(loader, metadata, dataFolder, file, this)
  }

  init {
    this.classes = ConcurrentHashMap()
    try {
      val mainClass = try {
        Class.forName(metadata.main, true, this)
      } catch (e: ClassNotFoundException) {
        throw InvalidPluginException("Unable to find plugin main class: ${metadata.main}", e)
      }
      val moduleClass = try {
        mainClass.asSubclass(JavaPlugin::class.java)
      } catch (e: ClassCastException) {
        throw InvalidPluginException("The plugin main class '${metadata.main}' does not extends JavaPlugin class.", e)
      }
      this.plugin = moduleClass.newInstance()
    } catch (e: IllegalAccessException) {
      throw InvalidPluginException("The constructor in the plugin is not a public decoration.", e)
    } catch (e: InstantiationException) {
      throw InvalidPluginException("Incorrect plugin.", e)
    }
  }
}
