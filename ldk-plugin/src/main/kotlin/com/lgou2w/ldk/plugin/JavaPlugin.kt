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

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

/**
 * @author lgou2w
 * @since LDK 0.1.9
 */
abstract class JavaPlugin : PluginBase {

  private lateinit var mFile : File
  private lateinit var mDataFolder : File
  private lateinit var mLoader : PluginLoader
  private lateinit var mLogger : Logger
  private lateinit var mMetadata : PluginMetadata
  private lateinit var mClassLoader : ClassLoader
  private var enable = false

  @Suppress("LeakingThis")
  constructor() {
    val classLoader = javaClass.classLoader as? JavaPluginClassLoader
      ?: throw IllegalStateException("The plugin requires a plugin class loader to load.")
    classLoader.initialize(this)
  }

  protected constructor(
    loader: JavaPluginLoader,
    metadata: PluginMetadata,
    dataFolder: File,
    file: File
  ) {
    val classLoader = javaClass.classLoader
    check(classLoader !is JavaPluginClassLoader) {
      "This initialization constructor cannot be used at runtime."
    }
    initialize(loader, metadata, dataFolder, file, classLoader)
  }

  internal fun initialize(
    loader: JavaPluginLoader,
    metadata: PluginMetadata,
    dataFolder: File,
    file: File,
    classLoader: ClassLoader
  ) {
    this.mLoader = loader
    this.mFile = file
    this.mDataFolder = dataFolder
    this.mMetadata = metadata
    this.mClassLoader = classLoader
    this.mLogger = LoggerFactory.getLogger(metadata.name)
  }

  override val dataFolder: File
    get() = mDataFolder

  override val metadata: PluginMetadata
    get() = mMetadata

  override val logger: Logger
    get() = mLogger

  override val loader: PluginLoader
    get() = mLoader

  override val isEnabled: Boolean
    get() = enable

  internal val file : File
    get() = mFile

  internal val classLoader : ClassLoader
    get() = mClassLoader

  internal fun setEnable(enable: Boolean) {
    if (this.enable != enable) {
      this.enable = enable
      if (isEnabled) onEnable()
      else onDisable()
    }
  }
}
