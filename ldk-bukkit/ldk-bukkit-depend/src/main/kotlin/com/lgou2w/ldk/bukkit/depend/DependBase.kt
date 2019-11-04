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

package com.lgou2w.ldk.bukkit.depend

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.logging.Logger

/**
 * ## DependBase (插件依赖基础)
 *
 * @see [Depend]
 * @author lgou2w
 */
abstract class DependBase<T : Plugin>(
  target: Plugin?
) : Depend {

  private val targetName : String
  private var reference : WeakReference<T> // auto gc and unregister

  init {
    if (target == null || !target.isEnabled)
      throw DependCannotException("The dependency plugin does not exist or is not loaded.")
    @Suppress("UNCHECKED_CAST")
    this.reference = WeakReference(target as T)
    this.targetName = target.name
    this.checkCanRegistered()
  }

  @Suppress("UNCHECKED_CAST")
  final override val plugin : T
    get() {
      val ref = reference.get()
      return if (ref != null && ref.isEnabled) {
        ref
      } else {
        val target = getPlugin(targetName)
        if (target != null && target.isEnabled) {
          reference = WeakReference(target as T)
          target
        } else {
          Depends.unregister(this::class.java)
          throw DependCannotException("The dependency plugin does not exist or is not loaded.")
        }
      }
    }

  /**
   * * Check plugin object reference.
   * * 检查插件对象引用.
   */
  @Throws(DependCannotException::class)
  protected fun checkReference() {
    plugin.isEnabled
  }

  private fun checkCanRegistered() {
    if (!Depends.hasDepend(this::class.java))
      Depends.register(this)
  }

  final override val name : String
    get() = targetName

  final override val pluginPrefix : String?
    get() = plugin.description.prefix

  final override val pluginName : String
    get() = plugin.description.name

  final override val pluginMain : String
    get() = plugin.description.main

  final override val pluginVersion : String
    get() = plugin.description.version

  final override val pluginWebsite : String?
    get() = plugin.description.website

  final override val pluginDescription : String?
    get() = plugin.description.description

  final override val pluginAuthors : Set<String>
    get() = plugin.description.authors.toSet()

  final override val pluginDepends : Set<String>
    get() = plugin.description.depend.toSet()

  final override val pluginSoftDepends : Set<String>
    get() = plugin.description.softDepend.toSet()

  final override val dataFolder : File
    get() = plugin.dataFolder

  final override val logger : Logger
    get() = plugin.logger

  final override val config : FileConfiguration?
    get() = plugin.config

  final override fun getResource(filename: String): InputStream?
    = plugin.getResource(filename)

  final override fun hashCode(): Int {
    return targetName.hashCode()
  }

  final override fun equals(other: Any?): Boolean {
    if (other === this)
      return true
    if (other is DependBase<*>)
      return targetName == other.targetName
    return false
  }

  final override fun toString(): String {
    return "Depend(plugin=$targetName)"
  }

  companion object {

    @JvmStatic
    fun getPlugin(name: String): Plugin?
      = Bukkit.getPluginManager().getPlugin(name)
  }
}
