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

package com.lgou2w.ldk.bukkit

import com.lgou2w.ldk.common.ComparisonChain
import org.bukkit.Bukkit

/**
 * ## PluginDependency (插件依赖)
 *
 * @see [PluginBase.enableDependencies]
 * @see [Comparable]
 * @author lgou2w
 */
data class PluginDependency(
  /**
   * * The name of the plugin dependency.
   * * 所依赖的插件名称.
   */
  val name: String,
  /**
   * * Whether it is a soft dependency method.
   * * 是否为软依赖方式.
   */
  val softDepend: Boolean,
  /**
   * * The minimum version of the plugin dependency.
   * * 所依赖的插件最低版本.
   */
  val version: String? = null,
  /**
   * * The entry point full class name of the plugin dependency.
   * * 所依赖的插件入口点全类名.
   */
  val className: String? = null
) : Comparable<PluginDependency> {

  /**
   * * Get whether can depended.
   * * 获取是否可以依赖.
   */
  fun canDepended(): Boolean {
    val plugin = Bukkit.getPluginManager().getPlugin(name)
    if (plugin == null || !plugin.isEnabled)
      return softDepend
    if (className != null && plugin.javaClass.canonicalName != className)
      return false
    if (version != null && plugin.description.version < version)
      return false
    return true
  }

  override fun compareTo(other: PluginDependency): Int {
    return ComparisonChain.start()
      .compare(name, other.name)
      .compare(softDepend, other.softDepend)
      .compare(version ?: "", other.version ?: "")
      .compare(className ?: "", other.className ?: "")
      .result
  }
}
