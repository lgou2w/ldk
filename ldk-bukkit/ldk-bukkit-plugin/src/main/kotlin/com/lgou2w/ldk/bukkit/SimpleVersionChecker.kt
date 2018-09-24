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

import com.google.gson.Gson
import com.google.gson.JsonArray
import org.bukkit.Bukkit
import java.net.URL

class SimpleVersionChecker(plugin: LDKPlugin, var block: ((version: String, commit: String, Throwable?) -> Unit)? = null) {

    private val API = "https://api.github.com/repos/lgou2w/ldk"
    private val API_VERSION = "https://api.github.com/repos/lgou2w/ldk/tags"

    init {
        Bukkit.getScheduler().runTaskAsynchronously(plugin) {
            try {
                val content = URL(API_VERSION).readText()
                val json = Gson().fromJson<JsonArray>(content, JsonArray::class.java)
                val tag = json.first().asJsonObject
                val version = tag["name"].asString
                val commit = tag["commit"].asJsonObject["sha"].asString
                block?.invoke(version, commit, null)
            } catch (e: Exception) {
                block?.invoke(e.message ?: "", "", e)
            } finally {
                block = null // gc
            }
        }
    }
}
