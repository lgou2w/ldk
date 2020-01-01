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

package com.lgou2w.ldk.bukkit.depend

import org.bukkit.entity.Player

/**
 * @since LDK 0.1.8-rc
 */
@Suppress("OverridingDeprecatedMember")
internal class PlaceholderExpansionProxy(
  private val proxy : PlaceholderExpansion
) : me.clip.placeholderapi.expansion.PlaceholderExpansion() {

  override fun getIdentifier(): String? = proxy.identifier
  override fun getPlugin(): String? = proxy.requiredPlugin
  override fun getRequiredPlugin(): String? = proxy.requiredPlugin
  override fun getAuthor(): String? = proxy.author
  override fun getVersion(): String? = proxy.version
  override fun getPlaceholders(): MutableList<String>? = proxy.placeholders
  override fun persist(): Boolean = proxy.isPersist
  override fun isRegistered(): Boolean = proxy.isRegistered()
  override fun register(): Boolean = proxy.register()
  override fun canRegister(): Boolean = proxy.canRegister()
  override fun onPlaceholderRequest(player: Player?, params: String): String?
    = proxy.onPlaceholderRequest(player, params)
}
