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

package com.lgou2w.ldk.bukkit.i18n

import com.lgou2w.ldk.i18n.ResourceProvider
import org.bukkit.plugin.Plugin
import java.io.FileNotFoundException
import java.io.InputStream

class PluginResourceProvider(
        val plugin: Plugin
) : ResourceProvider(PluginResourceProvider::class.java.classLoader) {

    override fun isValid(name: String): Boolean {
        return plugin.getResource(name) != null
    }

    override fun load(name: String): InputStream? {
        return plugin.getResource(name)
               ?: throw FileNotFoundException("The resource file $name in the plugin was not found.")
    }
}
