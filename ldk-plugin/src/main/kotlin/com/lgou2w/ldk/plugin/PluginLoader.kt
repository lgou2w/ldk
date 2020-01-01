/*
 * Copyright (C) 2016-2020 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.plugin

import java.io.File

/**
 * @author lgou2w
 * @since LDK 0.1.9
 */
interface PluginLoader {

  @Throws(InvalidPluginException::class)
  fun loadPlugin(file: File, metadataReader: PluginMetadataReader): Plugin

  @Throws(InvalidMetadataException::class)
  fun getMetadata(file: File, metadataReader: PluginMetadataReader): PluginMetadata

  fun enablePlugin(plugin: Plugin)

  fun disablePlugin(plugin: Plugin)
}
