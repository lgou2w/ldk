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

import com.lgou2w.ldk.common.notNull
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * ## PlaceholderExpansion (占位符扩展)
 *
 * @see [DependPlaceholderAPI]
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
abstract class PlaceholderExpansion {

  /**
   * PlaceholderAPI expansion proxy object
   */
  private val proxy by lazy { PlaceholderExpansionProxy(this) }

  /**
   * * Called when a placeholder is requested from this hook.
   * * 当此占位符扩展被请求占位符时调用.
   *
   * @param [player] to request the placeholder value for, `null` if not needed for a player.
   * @param [params] String passed to the hook to determine what value to return.
   * @return value for the requested player and params.
   */
  abstract fun onPlaceholderRequest(player: Player?, params: String): String?

  /**
   * * The placeholder identifier of this expansion.
   * * 此占位符扩展的占位符标识符.
   */
  abstract val identifier : String?

  /**
   * * The name of the plugin that this expansion hooks into.
   * * 此占位符扩展挂钩的插件名称.
   */
  abstract val requiredPlugin : String?

  /**
   * * The author of this expansion.
   * * 此占位符扩展的作者.
   */
  abstract val author : String?

  /**
   * * The version of this expansion.
   * * 此占位符扩展的版本.
   */
  abstract val version : String?

  /**
   * * The placeholders associated with this expansion.
   * * 与此占位符扩展相关联的占位符列表.
   */
  open val placeholders : MutableList<String>? = null

  /**
   * * Expansions that do not use the `ecloud` and instead register from the dependency should set this
   *      to `true` to ensure that your placeholder expansion is not unregistered when the `papi reload`
   *      command is used.
   * * 不使用 `ecloud` 而是从依赖项注册的扩展应将此值设置为 `true`,
   *      以确保在使用 `papi reload` 命令时不会取消注册占位符扩展.
   */
  open val isPersist = false

  /**
   * * Check if this placeholder identifier has already been registered.
   * * 检查此占位符扩展标识符是否已注册.
   */
  fun isRegistered(): Boolean {
    val identifier = this.identifier.notNull("Placeholder identifier can not be null!")
    try {
      checkApiLoaded() // Return false if the api is not loaded
    } catch (e: DependCannotException) {
      return false
    }
    @Suppress("DEPRECATION")
    return  me.clip.placeholderapi.PlaceholderAPI.getRegisteredPlaceholderPlugins().contains(identifier)
  }

  /**
   * * If any requirements need to be checked before this expansion should register, you can check them here.
   * * 如果在注册此占位符扩展之前需要检查任何要求, 可以在此处查看.
   */
  fun canRegister(): Boolean {
    try {
      checkApiLoaded() // Return false if the api is not loaded
    } catch (e: DependCannotException) {
      return false
    }
    val requiredPlugin = this.requiredPlugin
    return requiredPlugin == null || Bukkit.getPluginManager().getPlugin(requiredPlugin) != null
  }

  /**
   * * Attempt to register this placeholder expansion.
   * * 尝试注册此占位符扩展.
   *
   * @throws [DependCannotException] If the placeholder API plugin is not available.
   * @throws [DependCannotException] 如果占位符 API 插件不可用.
   */
  @Throws(DependCannotException::class)
  fun register(): Boolean {
    val identifier = this.identifier.notNull("Placeholder identifier can not be null!")
    checkApiLoaded()
    return if (Bukkit.getPluginManager().getPlugin(DependPlaceholderAPI.NAME).notNull().description.version < "2.8.7")
      me.clip.placeholderapi.PlaceholderAPI.registerPlaceholderHook(identifier, proxy)
    else
      me.clip.placeholderapi.PlaceholderAPI.registerExpansion(proxy) // since PlaceholderAPI 2.8.7
  }

  /**
   * * Safely attempt to register this placeholder expansion.
   * * 安全的尝试注册此占位符扩展.
   */
  fun registerSafely(): Boolean {
    return try {
      register()
    } catch (e: DependCannotException) {
      false
    }
  }

  companion object {
    @JvmStatic
    @Throws(DependCannotException::class)
    private fun checkApiLoaded() = if (Bukkit.getPluginManager().isPluginEnabled(DependPlaceholderAPI.NAME)) true else
      throw DependCannotException("The PlaceholderAPI plugin is not loaded yet.")
  }
}
