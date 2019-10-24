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

package com.lgou2w.ldk.plugin

import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.jar.JarFile

class JavaPluginLoader : PluginLoader {

  private val classes : MutableMap<String, Class<*>> = ConcurrentHashMap()
  private val loaders : MutableList<JavaPluginClassLoader> = CopyOnWriteArrayList()

  override fun loadPlugin(file: File, metadataReader: PluginMetadataReader): Plugin {
    if (!file.exists())
      throw InvalidPluginException(FileNotFoundException("The plugin file '${file.path}' does not exist."))
    val metadata = try {
      getMetadata(file, metadataReader)
    } catch (e: InvalidMetadataException) {
      throw InvalidPluginException(e)
    }
    val dataFolder = File(file.parentFile, metadata.name)
    val loader = try {
      JavaPluginClassLoader(this, javaClass.classLoader, metadata, dataFolder, file)
    } catch (e: InvalidPluginException) {
      throw e
    } catch (e: Exception) {
      throw InvalidPluginException(e)
    }
    loaders.add(loader)
    return loader.plugin
  }

  override fun getMetadata(file: File, metadataReader: PluginMetadataReader): PluginMetadata {
    return try {
      JarFile(file).use { jarFile ->
        metadataReader.read(jarFile)
      }
    } catch (e: Exception) {
      throw if (e is InvalidMetadataException) e
      else InvalidMetadataException(e)
    }
  }

  override fun enablePlugin(plugin: Plugin) {
    if (plugin !is JavaPlugin)
      throw IllegalArgumentException("The plugin object has nothing to do with the current plugin class loader.")
    if (!plugin.isEnabled) {
      plugin.logger.info("Enable plugin: ${plugin.metadata.fullName}")
      val loader = plugin.classLoader as JavaPluginClassLoader
      if (loader !in loaders)
        loaders.add(loader)
      try {
        plugin.setEnable(true)
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  override fun disablePlugin(plugin: Plugin) {
    if (plugin !is JavaPlugin)
      throw IllegalArgumentException("The plugin object has nothing to do with the current plugin class loader.")
    if (plugin.isEnabled) {
      plugin.logger.info("Disable plugin: ${plugin.metadata.fullName}")
      val loader = plugin.classLoader
      try {
        plugin.setEnable(false)
      } catch (e: Exception) {
        e.printStackTrace()
      }
      if (loader is JavaPluginClassLoader) {
        loaders.remove(loader)
        for (name in loader.classNames)
          removeClass(name)
      }
    }
  }

  internal fun getClassByName(name: String): Class<*>? {
    var clazz = classes[name]
    if (clazz == null) {
      for (loader in loaders) try {
        clazz = loader.findClass(name, false)
      } catch (e: Exception) {
        if (clazz != null)
          return clazz
      }
    }
    return clazz
  }

  internal fun setClass(name: String, clazz: Class<*>) {
    if (!classes.containsKey(name))
      classes[name] = clazz
  }

  private fun removeClass(name: String) {
    classes.remove(name)
  }
}
