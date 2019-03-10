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

import com.lgou2w.ldk.bukkit.internal.FakePlugin
import com.lgou2w.ldk.common.Constants
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.ServicePriority

/**
 * ## Depends (插件依赖)
 *
 * @see [Depend]
 * @author lgou2w
 */
object Depends {

    @JvmStatic
    @Suppress("DEPRECATION")
    private val PLUGIN : Plugin by lazy {
        val ldk : Plugin? = Bukkit.getPluginManager().getPlugin(Constants.LDK)
        if (ldk != null && ldk.isEnabled) ldk else FakePlugin("LDKDependInternalFakePlugin")
    }

    /**
     * * Register the given plugin dependency [depend].
     * * 将给定的插件依赖 [depend] 进行注册.
     */
    @JvmStatic
    fun register(depend: Depend): Boolean {
        val type = depend::class.java
        @Suppress("UNCHECKED_CAST")
        return register(type as Class<Depend>, depend)
    }

    /**
     * * Register the given plugin dependency class [depend] and implementation instance [implemented].
     * * 将给定的插件依赖类 [depend] 和实现实例 [implemented] 进行注册.
     *
     * @throws [IllegalArgumentException] If the implementation instance [implemented] is not an instance object of the [depend] class.
     * @throws [IllegalArgumentException] 如果实现实例 [implemented] 不是 [depend] 类的实例对象.
     */
    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun <T : Depend> register(depend: Class<T>, implemented: T): Boolean {
        if (!depend.isInstance(implemented))
            throw IllegalArgumentException("The depend implementation is not an instance object of the $depend class.")
        if (hasDepend(depend))
            return false
        Bukkit.getServicesManager().register(depend, implemented, PLUGIN, ServicePriority.Normal)
        return hasDepend(depend)
    }

    /**
     * * Unregister with the given plugin dependency class [depend].
     * * 将给定的插件依赖类 [depend] 进行卸载注销.
     */
    @JvmStatic
    fun <T : Depend> unregister(depend: Class<T>): Boolean {
        val implemented = get(depend) ?: return false
        Bukkit.getServicesManager().unregister(depend, implemented)
        return !hasDepend(depend)
    }

    /**
     * * Unregister all plugin dependencies.
     * * 将所有的插件依赖卸载注销.
     */
    @JvmStatic
    fun unregisterAll() {
        @Suppress("DEPRECATION")
        if (PLUGIN is FakePlugin)
            Bukkit.getServicesManager().unregisterAll(PLUGIN)
        else {
            // Is LDK, Only unregister depend
            Bukkit.getServicesManager().getRegistrations(PLUGIN)
                .filter { it.provider is Depend }
                .forEach {
                    Bukkit.getServicesManager().unregister(it.service, it.provider)
                }
        }
    }

    /**
     * * Gets the plugin dependency object from the given name [name]. Returns `null` if it doesn't exist.
     * * 从给定的名称 [name] 获取插件依赖对象. 如果不存在则返回 `null`.
     */
    @JvmStatic
    operator fun get(name: String): Depend? {
        val registrations = Bukkit.getServicesManager().getRegistrations(PLUGIN)
        return registrations
            .find { it.provider is Depend && (it.provider as Depend).name == name }
            ?.provider as? Depend
    }

    /**
     * * Get the plugin dependency object from the given plugin dependency class [depend]. Returns `null` if it doesn't exist.
     * * 从给定的插件依赖类 [depend] 获取插件依赖对象. 如果不存在则返回 `null`.
     */
    @JvmStatic
    operator fun <T : Depend> get(depend: Class<T>): T? {
        val registration = Bukkit.getServicesManager().getRegistration(depend) ?: return null
        return if (registration.service == depend) registration.provider else null
    }

    /**
     * * Gets or loads a plugin dependency object from the given plugin dependency class [depend].
     * * 从给定的插件依赖类 [depend] 获取或加载插件依赖对象.
     *
     * @throws [DependCannotException] If the plugin dependency is not available.
     * @throws [DependCannotException] 如果插件依赖不可用.
     */
    @JvmStatic
    @Throws(DependCannotException::class)
    fun <T : Depend> getOrLoad(depend: Class<T>): T {
        val registration = get(depend)
        return registration ?: try {
            val implemented = depend.newInstance()
            register(depend, implemented)
            implemented
        } catch (e: Exception) {
            if (e is DependCannotException)
                throw e
            else
                throw DependCannotException(e)
        }
    }

    /**
     * * Gets whether the plugin dependency object exists from the given name [name].
     * * 从给定的名称 [name] 获取插件依赖对象是否存在.
     */
    @JvmStatic
    fun hasDepend(name: String): Boolean
            = get(name) != null

    /**
     * * Gets whether the plugin dependent object exists from the given plugin dependency class [depend].
     * * 从给定的插件依赖类 [depend] 获取插件依赖对象是否存在.
     */
    @JvmStatic
    fun <T : Depend> hasDepend(depend: Class<T>): Boolean
            = get(depend) != null
}
