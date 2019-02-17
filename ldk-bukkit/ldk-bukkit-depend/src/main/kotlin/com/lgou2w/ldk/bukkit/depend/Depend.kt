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

import com.lgou2w.ldk.bukkit.PluginArchive
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.InputStream
import java.util.logging.Logger

interface Depend : PluginArchive {

    val plugin : Plugin

    val name: String

    /**
     * * Get this dependent plugin data folder object.
     * * 获取此依赖插件的数据目录对象.
     */
    val dataFolder: File

    /**
     * * Get this dependent plugin logger object.
     * * 获取此依赖插件的日志对象.
     */
    val logger: Logger

    /**
     * * Get this dependent plugin configuration object.
     * * 获取此依赖插件的配置文件对象.
     */
    val config: FileConfiguration?

    /**
     * * Gets this dependent plugin resource Input stream from the given file name.
     * * 从给定的文件名获取此依赖插件的资源输入流.
     *
     * @param filename File name.
     * @param filename 文件名.
     */
    fun getResource(filename: String): InputStream?
}
