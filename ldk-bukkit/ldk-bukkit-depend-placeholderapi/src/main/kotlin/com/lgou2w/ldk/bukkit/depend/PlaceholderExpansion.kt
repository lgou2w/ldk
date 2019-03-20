/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
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

    abstract fun onPlaceholderRequest(player: Player?, identifier: String): String?

    abstract val identifier : String?
    abstract val requiredPlugin : String?
    abstract val author : String?
    abstract val version : String?

    open val placeholders : MutableList<String>? = null
    open val isPersist = false

    /**
     * PlaceholderAPI expansion proxy object
     */
    private val proxy by lazy { PlaceholderExpansionProxy(this) }
    private fun checkApiLoaded() = if (Bukkit.getPluginManager().isPluginEnabled(DependPlaceholderAPI.NAME)) true else
            throw DependCannotException("The PlaceholderAPI plugin is not loaded yet.")

    @Throws(DependCannotException::class)
    fun isRegistered(): Boolean {
        val identifier = this.identifier.notNull("Placeholder identifier can not be null!")
        checkApiLoaded()
        @Suppress("DEPRECATION")
        return  me.clip.placeholderapi.PlaceholderAPI.getRegisteredPlaceholderPlugins().contains(identifier)
    }

    @Throws(DependCannotException::class)
    fun canRegister(): Boolean {
        checkApiLoaded()
        val requiredPlugin = this.requiredPlugin
        return requiredPlugin == null || Bukkit.getPluginManager().getPlugin(requiredPlugin) != null
    }

    @Throws(DependCannotException::class)
    fun register(): Boolean {
        val identifier = this.identifier.notNull("Placeholder identifier can not be null!")
        checkApiLoaded()
        return if (Bukkit.getPluginManager().getPlugin(DependPlaceholderAPI.NAME).notNull().description.version < "2.8.7")
            me.clip.placeholderapi.PlaceholderAPI.registerPlaceholderHook(identifier, proxy)
        else
            me.clip.placeholderapi.PlaceholderAPI.registerExpansion(proxy) // since PlaceholderAPI 2.8.7
    }

    fun registerSafely(): Boolean {
        return try {
            register()
        } catch (e: DependCannotException) {
            false
        }
    }
}
