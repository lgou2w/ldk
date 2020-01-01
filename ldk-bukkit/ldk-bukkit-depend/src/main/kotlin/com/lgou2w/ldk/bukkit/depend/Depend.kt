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

package com.lgou2w.ldk.bukkit.depend

import com.lgou2w.ldk.bukkit.PluginArchive
import com.lgou2w.ldk.common.ApplicatorFunction
import com.lgou2w.ldk.common.letIfNotNull
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.InputStream
import java.util.logging.Logger

/**
 * ## Depend (插件依赖)
 *
 * @see [Depends]
 * @see [DependBase]
 * @see [PluginArchive]
 * @author lgou2w
 */
interface Depend : PluginArchive {

  /**
   * * The plugin object that this plugin depends on.
   * * 此插件依赖的插件对象.
   */
  val plugin : Plugin

  /**
   * * The plugin name that this plugin depends on.
   * * 此插件依赖的插件名称.
   */
  val name : String

  /**
   * * Get this dependent plugin data folder object.
   * * 获取此依赖插件的数据目录对象.
   */
  val dataFolder : File

  /**
   * * Get this dependent plugin logger object.
   * * 获取此依赖插件的日志对象.
   */
  val logger : Logger

  /**
   * * Get this dependent plugin configuration object.
   * * 获取此依赖插件的配置文件对象.
   */
  val config : FileConfiguration?

  /**
   * * Gets this dependent plugin resource Input stream from the given file name.
   * * 从给定的文件名获取此依赖插件的资源输入流.
   *
   * @param filename File name.
   * @param filename 文件名.
   */
  fun getResource(filename: String): InputStream?

  companion object Factory {

    /**
     * * Get or load plugin dependency object from the given plugin [depend] class.
     * * 从给定的插件依赖类 [depend] 获取或加载插件依赖对象.
     *
     * @throws [DependCannotException] If the plugin dependency is not available.
     * @throws [DependCannotException] 如果插件依赖不可用.
     * @since LDK 0.1.8-rc
     */
    @JvmStatic
    @Throws(DependCannotException::class)
    fun <T : Depend> of(depend: Class<T>): T
      = Depends.getOrLoad(depend)

    /**
     * * Safely get or load plugin dependency object from the given plugin [depend] class.
     * * 安全的从给定的插件依赖类 [depend] 获取或加载插件依赖对象.
     *
     * @since LDK 0.1.8-rc
     */
    @JvmStatic
    fun <T : Depend> ofSafely(depend: Class<T>): T? = try {
      of(depend)
    } catch (e: DependCannotException) {
      null
    }

    /**
     * * Get or load plugin dependency object from the given plugin [depend] class. Then apply and return the result [R].
     * * 从给定的插件依赖类 [depend] 获取或加载插件依赖对象. 然后应用并返回结果 [R].
     *
     * @throws [DependCannotException] If the plugin dependency is not available.
     * @throws [DependCannotException] 如果插件依赖不可用.
     * @see [of]
     * @since LDK 0.1.8-rc
     */
    @JvmStatic
    @Throws(DependCannotException::class)
    fun <T : Depend, R> ofApply(depend: Class<T>, applicator: ApplicatorFunction<T, R>): R
      = of(depend).let(applicator)

    /**
     * * Safely get or load plugin dependency object from the given plugin [depend] class. Then apply and return the result [R] or `null`.
     * * 安全的从给定的插件依赖类 [depend] 获取或加载插件依赖对象. 然后应用并返回结果 [R] 或 `null`.
     *
     * @see [ofSafely]
     * @since LDK 0.1.8-rc
     */
    @JvmStatic
    fun <T : Depend, R> ofSafelyApply(depend: Class<T>, applicator: ApplicatorFunction<T, R>): R?
      = ofSafely(depend).letIfNotNull(applicator)
  }
}
