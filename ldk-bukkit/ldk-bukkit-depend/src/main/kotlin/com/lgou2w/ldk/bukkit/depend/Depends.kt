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

object Depends {

    @JvmStatic
    @Suppress("DEPRECATION")
    private val PLUGIN : Plugin by lazy {
        val ldk : Plugin? = Bukkit.getPluginManager().getPlugin(Constants.LDK)
        if (ldk != null && ldk.isEnabled) ldk else FakePlugin("LDKDependInternalFakePlugin")
    }

    @JvmStatic
    fun register(depend: Depend): Boolean {
        val type = depend::class.java
        @Suppress("UNCHECKED_CAST")
        return register(type as Class<Depend>, depend)
    }

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

    @JvmStatic
    fun <T : Depend> unregister(depend: Class<T>): Boolean {
        val implemented = get(depend) ?: return false
        Bukkit.getServicesManager().unregister(depend, implemented)
        return !hasDepend(depend)
    }

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

    @JvmStatic
    operator fun get(name: String): Depend? {
        val registrations = Bukkit.getServicesManager().getRegistrations(PLUGIN)
        return registrations
            .find { it.provider is Depend && (it.provider as Depend).name == name }
            ?.provider as? Depend
    }

    @JvmStatic
    operator fun <T : Depend> get(depend: Class<T>): T? {
        val registration = Bukkit.getServicesManager().getRegistration(depend) ?: return null
        return if (registration.service == depend) registration.provider else null
    }

    @JvmStatic
    @Throws(DependCannotException::class)
    fun <T : Depend> getOrLoad(depend: Class<T>): T {
        val registration = get(depend)
        return if (registration != null)
            registration
        else {
            try {
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
    }

    @JvmStatic
    fun hasDepend(name: String): Boolean
            = get(name) != null

    @JvmStatic
    fun <T : Depend> hasDepend(depend: Class<T>): Boolean
            = get(depend) != null
}
