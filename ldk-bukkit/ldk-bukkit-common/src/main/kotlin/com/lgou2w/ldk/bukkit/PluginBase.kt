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

package com.lgou2w.ldk.bukkit

import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.common.notNull
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File
import java.util.logging.Level

/**
 * ## PluginBase (插件基础)
 *
 * @see [Plugin]
 * @see [JavaPlugin]
 * @author lgou2w
 */
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
            logger.log(Level.SEVERE, "Dependency failure when plugin enable, unload dependency: ${failedDependency?.name}.")
            failedDependency(failedDependency.notNull())
            server.pluginManager.disablePlugin(this)
            return
        }
        try {
            enable()
        } catch (e: Exception) {
            if (enableExceptionDisabled) {
                logger.log(Level.SEVERE, "The plugin is disabled due to an exception at enable:", e)
                server.pluginManager.disablePlugin(this)
                return
            } else
                logger.log(Level.SEVERE, "Error enable, skip plugin disabled, exception:", e)
        }
        if (isEnabled) {
            val endTime = System.currentTimeMillis()
            logger.info("Plugin $pluginName v$pluginVersion successfully enabled, total time: ${endTime - startTime}ms.")
        }
    }

    final override fun onDisable() {
        super.onDisable()
        disable()
    }

    /**
     * * Called when the plugin is load.
     * * 当插件加载时调用.
     */
    protected abstract fun load()

    /**
     * * Called when the plugin is enable.
     * * 当插件启用时调用.
     */
    protected abstract fun enable()

    /**
     * * Called when the plugin is disable.
     * * 当插件禁用时调用.
     */
    protected abstract fun disable()

    /**************************************************************************
     *
     * Enable Optional
     *
     **************************************************************************/

    /**
     * * Indicates an exception when the plugin is enabled. Then whether to disable the plugin.
     * * 表示当插件启用时异常. 那么是否禁用插件.
     */
    protected open val enableExceptionDisabled : Boolean = true

    /**************************************************************************
     *
     * Plugin Dependency
     *
     **************************************************************************/

    /**
     * * Indicates that the plugin enables the required plugin dependency item.
     * * 表示插件启用所需求的插件依赖项.
     *
     * @see [PluginDependency]
     */
    protected open val enableDependencies : Array<PluginDependency> = emptyArray()

    /**
     * * Called when a plugin dependency fails to load.
     * * 当一个插件依赖项加载失败时被调用.
     *
     * @see [PluginDependency]
     */
    protected open fun failedDependency(dependency: PluginDependency) { }

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

    /**************************************************************************
     *
     * Plugin Archive
     *
     **************************************************************************/

    override val pluginPrefix : String?
        get() = description.prefix

    override val pluginName : String
        get() = description.name

    override val pluginMain : String
        get() = description.main

    override val pluginVersion : String
        get() = description.version

    override val pluginWebsite : String?
        get() = description.website

    override val pluginDescription : String?
        get() = description.description

    override val pluginAuthors : Set<String>
        get() = description.authors.toSet()

    override val pluginDepends : Set<String>
        get() = description.depend.toSet()

    override val pluginSoftDepends : Set<String>
        get() = description.softDepend.toSet()
}
