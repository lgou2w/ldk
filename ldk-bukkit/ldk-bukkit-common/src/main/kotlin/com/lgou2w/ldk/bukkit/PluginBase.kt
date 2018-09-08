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

package com.lgou2w.ldk.bukkit

import com.lgou2w.ldk.common.Applicator
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File
import java.util.logging.Level

abstract class PluginBase : JavaPlugin, Plugin {

    constructor() : super()
    constructor(
            loader: JavaPluginLoader,
            description: PluginDescriptionFile,
            dataFolder: File,
            file: File
    ) : super(loader, description, dataFolder, file)

    final override fun onLoad() {
        super.onLoad()
        load()
    }

    final override fun onEnable() {
        super.onEnable()
        val startTime = System.currentTimeMillis()
        var failedDependency: PluginDependency? = null
        if (enableDependencies.isNotEmpty() && !enableDependencies.all { dependency ->
                    dependency.canDepended().also {
                        if (!it)
                            failedDependency = dependency
                    }
                }
        ) {
            logger.log(Level.SEVERE, "插件加载时依赖失败, 未成功依赖项: ${failedDependency?.name}.")
            server.pluginManager.disablePlugin(this)
            return
        }
        try {
            enable()
        } catch (e: Exception) {
            if (enableExceptionDisabled) {
                logger.log(Level.SEVERE, "插件已禁用, 由于加载时异常:", e)
                server.pluginManager.disablePlugin(this)
                return
            } else
                logger.log(Level.SEVERE, "加载时错误,跳过插件禁用, 异常:", e)
        }
        val endTime = System.currentTimeMillis()
        logger.info("插件 $pluginName v$pluginVersion 成功加载, 总耗时: ${endTime - startTime}ms.")
    }

    final override fun onDisable() {
        super.onDisable()
        disable()
    }

    protected abstract fun load()
    protected abstract fun enable()
    protected abstract fun disable()

    /**************************************************************************
     *
     * Enable Optional
     *
     **************************************************************************/

    protected open val enableExceptionDisabled: Boolean = true

    /**************************************************************************
     *
     * Plugin Dependency
     *
     **************************************************************************/

    protected open val enableDependencies: Array<PluginDependency> = emptyArray()

    protected class PluginDependencyScope {
        var name: String = "UNKNOWN"
        var softDepend: Boolean = false
        var version: String? = null
        var className: String? = null
    }

    protected inline fun dependency(block: Applicator<PluginDependencyScope>): PluginDependency {
        val scope = PluginDependencyScope().also(block)
        return PluginDependency(scope.name, scope.softDepend, scope.version, scope.className)
    }

    /**************************************************************************
     *
     * Plugin Public API
     *
     **************************************************************************/

    fun initPluginFolder() {
        if (!dataFolder.exists())
            dataFolder.mkdirs()
        val config = File(dataFolder, "config.yml")
        if (!config.exists() && getResource("config.yml") != null)
            saveDefaultConfig()
    }

    /**************************************************************************
     *
     * Plugin Archive
     *
     **************************************************************************/

    override val pluginPrefix: String
        get() = description.prefix

    override val pluginName: String
        get() = description.name

    override val pluginMain: String
        get() = description.main

    override val pluginVersion: String
        get() = description.version

    override val pluginWebsite: String
        get() = description.website

    override val pluginDescription: String
        get() = description.description

    override val pluginAuthors: Set<String>
        get() = description.authors.toSet()

    override val pluginDepends: Set<String>
        get() = description.depend.toSet()

    override val pluginSoftDepends: Set<String>
        get() = description.softDepend.toSet()
}