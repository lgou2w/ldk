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

package com.lgou2w.ldk.bukkit

/**
 * ## PluginArchive (插件存档)
 *
 * @see [org.bukkit.plugin.PluginDescriptionFile]
 * @author lgou2w
 */
interface PluginArchive {

  /**
   * * The prefix of this plugin.
   * * 此插件的前缀.
   */
  val pluginPrefix : String?

  /**
   * * The name of this plugin.
   * * 此插件的名称.
   */
  val pluginName : String

  /**
   * * The entry point of this plugin.
   * * 此插件的入口点.
   */
  val pluginMain : String

  /**
   * * The version of this plugin.
   * * 此插件的版本.
   */
  val pluginVersion : String

  /**
   * * The website of this plugin.
   * * 此插件的网站.
   */
  val pluginWebsite : String?

  /**
   * * The description of this plugin.
   * * 此插件的说明.
   */
  val pluginDescription : String?

  /**
   * * The author set of this plugin.
   * * 此插件的作者集合.
   */
  val pluginAuthors : Set<String>

  /**
   * * The dependency set of this plugin.
   * * 此插件的依赖集合.
   */
  val pluginDepends : Set<String>

  /**
   * * The soft dependency set of this plugin.
   * * 此插件的软依赖集合.
   */
  val pluginSoftDepends : Set<String>
}
