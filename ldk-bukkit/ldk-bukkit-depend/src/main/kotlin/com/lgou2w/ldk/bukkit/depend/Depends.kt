/*
 * Copyright (C) 2018 The lgou2w (lgou2w@hotmail.com)
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
import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginBase
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginLoader
import org.bukkit.plugin.ServicePriority
import java.io.File
import java.io.InputStream
import java.util.*
import java.util.logging.Logger

object Depends {

    @JvmStatic
    private val PLUGIN : Plugin by lazy {
        val ldk : Plugin? = Bukkit.getPluginManager().getPlugin("LDK")
        if (ldk != null && ldk.isEnabled) ldk else InternalFakePlugin()
    }

    @JvmStatic
    fun register(depend: Depend) : Boolean {
        val type = depend::class.java
        @Suppress("UNCHECKED_CAST")
        return register(type as Class<Depend>, depend)
    }

    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun <T: Depend> register(depend: Class<T>, implemented: T) : Boolean {
        if (!depend.isInstance(implemented))
            throw IllegalArgumentException("The depend implementation is not an instance object of the $depend class.")
        if (hasDepend(depend))
            return false
        Bukkit.getServicesManager().register(depend, implemented, PLUGIN, ServicePriority.Normal)
        return hasDepend(depend)
    }

    @JvmStatic
    fun <T: Depend> unregister(depend: Class<T>) : Boolean {
        val implemented = get(depend) ?: return false
        Bukkit.getServicesManager().unregister(depend, implemented)
        return !hasDepend(depend)
    }

    @JvmStatic
    fun unregisterAll() {
        Bukkit.getServicesManager().unregisterAll(PLUGIN)
    }

    operator fun get(name: String) : Depend? {
        val registrations = Bukkit.getServicesManager().getRegistrations(PLUGIN)
        return registrations
            .find { it.provider is Depend && (it.provider as Depend).name == name }
            ?.provider as? Depend
    }

    operator fun <T: Depend> get(depend: Class<T>) : T? {
        val registration = Bukkit.getServicesManager().getRegistration(depend) ?: return null
        return if (registration.service == depend) registration.provider else null
    }

    @Throws(DependCannotException::class)
    fun <T: Depend> getOrLoad(depend: Class<T>) : T {
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
                    throw DependCannotException(e.message, e.cause ?: e)
            }
        }
    }

    fun hasDepend(name: String) : Boolean
            = get(name) != null

    fun <T: Depend> hasDepend(depend: Class<T>) : Boolean
            = get(depend) != null

    /**
     * * This makes it possible to implement an object of a fake plugin, but is not sure about the unknown risk.
     * * Currently in the running environment test everything is normal, and only event processing, and can not query plugin information.
     *
     * * 这样虽然能够实现一个虚假插件的对象，但是不确定未知的风险。
     * * 目前在运行环境测试一切正常，并且只有事件处理，以及无法查询到插件信息。
     *
     * @author lgou2w
     */
    private class InternalFakePlugin : PluginBase() {
        override fun getDataFolder(): File? = null
        override fun getPluginLoader(): PluginLoader? = null
        override fun getServer(): Server = Bukkit.getServer()
        override fun isEnabled(): Boolean = true
        override fun getDescription(): PluginDescriptionFile = PluginDescriptionFile("LDKDependInternalFakePlugin", "0", "0")
        override fun getConfig(): FileConfiguration? = null
        override fun reloadConfig() { }
        override fun saveConfig() { }
        override fun saveDefaultConfig() { }
        override fun saveResource(resourcePath: String?, replace: Boolean) { }
        override fun getResource(filename: String?): InputStream? = null
        override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean = false
        override fun onTabComplete(sender: CommandSender?, command: Command?, alias: String?, args: Array<out String>?): MutableList<String> = Collections.emptyList()
        override fun isNaggable(): Boolean = false
        override fun setNaggable(canNag: Boolean) { }
        override fun getLogger(): Logger = Bukkit.getLogger()
        override fun onLoad() { }
        override fun onEnable() { }
        override fun onDisable() { }
        override fun getDefaultWorldGenerator(worldName: String?, id: String?): ChunkGenerator? = null
    }
}
