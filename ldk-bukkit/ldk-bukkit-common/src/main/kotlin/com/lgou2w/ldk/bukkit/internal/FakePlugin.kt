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

package com.lgou2w.ldk.bukkit.internal

import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.PluginBase
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginLoader
import java.io.File
import java.io.InputStream
import java.util.logging.Logger

/**
 * * This makes it possible to implement an object of a fake plugin, but is not sure about the unknown risk.
 * * Currently in the running environment test everything is normal, and only event processing, and can not query plugin information.
 *
 * * 这样虽然能够实现一个虚假插件的对象，但是不确定未知的风险。
 * * 目前在运行环境测试一切正常，并且只有事件处理，以及无法查询到插件信息。
 *
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
@Deprecated("INTERNAL ONLY")
class FakePlugin(name: String) : PluginBase() {
    private val pdf = PluginDescriptionFile(name, "0", "0")
    override fun getDataFolder(): File = throw UnsupportedOperationException()
    override fun getPluginLoader(): PluginLoader = throw UnsupportedOperationException()
    override fun getServer(): Server = Bukkit.getServer()
    override fun isEnabled(): Boolean = true // must
    override fun getDescription(): PluginDescriptionFile = pdf // must
    override fun getConfig(): FileConfiguration = throw UnsupportedOperationException()
    override fun reloadConfig() { }
    override fun saveConfig() { }
    override fun saveDefaultConfig() { }
    override fun saveResource(resourcePath: String, replace: Boolean) { }
    override fun getResource(filename: String): InputStream? = null
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean = false
    override fun onTabComplete(sender: CommandSender, cmd: Command, alias: String, args: Array<out String>): List<String>? = null
    override fun isNaggable(): Boolean = false
    override fun setNaggable(canNag: Boolean) { }
    override fun getLogger(): Logger = Bukkit.getLogger()
    override fun onLoad() { }
    override fun onEnable() { }
    override fun onDisable() { }
    override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator? = null
}
