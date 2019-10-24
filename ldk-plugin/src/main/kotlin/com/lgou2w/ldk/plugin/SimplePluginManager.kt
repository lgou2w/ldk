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
import java.util.logging.Level
import java.util.logging.Logger

class SimplePluginManager(
  private val factory: PluginLoaderFactory
): PluginManager {

  private val mPlugins : MutableList<Plugin> = ArrayList()
  private val mLookupNames : MutableMap<String, Plugin> = HashMap()
  private val logger = Logger.getLogger(SimplePluginManager::class.java.simpleName)

  override val loader : PluginLoader
    get() = factory.loader

  @Synchronized
  override fun getPlugin(name: String): Plugin?
    = mLookupNames[name.replace(" ", "_")]

  override val plugins: Array<Plugin>
    @Synchronized
    get() = mPlugins.toTypedArray()

  @Synchronized
  override fun loadPlugin(file: File): Plugin? {
    if (!factory.isValidPlugin(file))
      return null
    val plugin = factory.loader.loadPlugin(file, factory.metadataReader)
    mPlugins.add(plugin)
    mLookupNames[plugin.name] = plugin
    return plugin
  }

  override fun loadPlugins(dir: File): Array<Plugin> {
    require(dir.isDirectory) { "Must be directory file: ${dir.path}" }
    val files = dir.listFiles() ?: return emptyArray()
    val plugins : MutableList<Plugin> = ArrayList()
    for (file in files) {
      val plugin = try {
        loadPlugin(file)
      } catch (e: Exception) {
        logger.log(Level.SEVERE, "Could not load plugin '${file.path}' in folder '${dir.path}'", e)
        null
      } ?: continue
      plugins.add(plugin)
    }
    return plugins.toTypedArray()
  }

  override fun enablePlugins() {
    val plugins = plugins
    var i = 0
    while (i < plugins.size)
      enablePlugin(plugins[i++])
  }

  override fun disablePlugins() {
    val plugins = plugins
    var i = plugins.size - 1
    while (i >= 0)
      disablePlugin(plugins[i--])
  }

  override fun clearPlugins() {
    synchronized (this) {
      disablePlugins()
      mPlugins.clear()
      mLookupNames.clear()
    }
  }

  override fun enablePlugin(plugin: Plugin) {
    if (!plugin.isEnabled) try {
      plugin.loader.enablePlugin(plugin)
    } catch (e: Exception) {
      logger.log(Level.SEVERE,
        "Error occurred (in the plugin loader) while enabling ${plugin.metadata.fullName} (Is it up to date?)", e)
    }
  }

  override fun disablePlugin(plugin: Plugin) {
    if (plugin.isEnabled) try {
      plugin.loader.disablePlugin(plugin)
    } catch (e: Exception) {
      logger.log(Level.SEVERE,
        "Error occurred (in the plugin loader) while disabling ${plugin.metadata.fullName} (Is it up to date?)", e)
    }
  }

  override fun isPluginEnabled(name: String): Boolean {
    val plugin = getPlugin(name)
    return isPluginEnabled(plugin)
  }

  override fun isPluginEnabled(plugin: Plugin?): Boolean {
    return if (plugin != null && plugin in mPlugins)
      plugin.isEnabled
    else
      false
  }
}
