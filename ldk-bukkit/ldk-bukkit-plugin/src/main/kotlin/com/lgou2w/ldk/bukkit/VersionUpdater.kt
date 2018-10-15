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
import com.google.gson.JsonObject
import com.lgou2w.ldk.chat.ChatColor
import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.common.Callable
import com.lgou2w.ldk.common.Consumer
import com.lgou2w.ldk.common.isTrue
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.net.URL
import java.util.logging.Level

class VersionUpdater internal constructor(private val plugin: LDKPlugin) {

    private val currentVersion = plugin.pluginVersion
    private var lastCheckedAt : Long = 0L
    private var lastRelease : Release? = null

    private fun canRecheck() : Boolean
            = System.currentTimeMillis() - lastCheckedAt > MAX_CHECK_INTERVAL

    private fun doCheck(callback: Applicator<VersionUpdater>? = null) {
        val now = System.currentTimeMillis()
        if (now - lastCheckedAt > MAX_CHECK_INTERVAL) {
            try {
                lastRelease = parseLatestRelease()
                lastCheckedAt = now
                callback?.invoke(this)
            } catch (e: Exception) {
                plugin.logger.log(Level.WARNING, "Exception when requesting version update check.", e)
            }
        }
    }

    fun firstCheck() {
        plugin.runTaskAsyncLater({
            if (Bukkit.getPluginManager().getPlugin(LDKPlugin.NAME)?.isEnabled.isTrue())
                pushRelease(Bukkit.getConsoleSender())
        }, 30 * 20L) // Wait 30 seconds
    }

    fun pushRelease(sender: CommandSender) {
        var receiver = sender
        val last = lastRelease
        val pushed : Consumer<Release> = { release ->
            if (!release.isPreRelease) {
                receiver.sendMessage(LDKPlugin.PREFIX + ChatColor.GRAY + "There are new available. please update, plz, plz :")
                receiver.sendMessage(" = " + ChatColor.GRAY + "Version: " + ChatColor.GOLD + release.tag)
                receiver.sendMessage(" = " + ChatColor.GRAY + "Released: " + ChatColor.GREEN + release.releasedAt)
                receiver.sendMessage(" = " + ChatColor.GRAY + "Author: " + ChatColor.AQUA + release.author)
                receiver.sendMessage(" = " + ChatColor.GRAY + "Url: " + ChatColor.AQUA + release.authorUrl)
                receiver.sendMessage(" = " + ChatColor.GRAY + "Download: " + ChatColor.GREEN + release.fileDownloadUrl)
            } else {
                receiver.sendMessage(LDKPlugin.PREFIX + ChatColor.GRAY + "There is a new preview available:")
                receiver.sendMessage(" = " + ChatColor.GRAY + "Version: " + ChatColor.GOLD + release.tag)
                receiver.sendMessage(" = " + ChatColor.GRAY + "Released: " + ChatColor.GREEN + release.releasedAt)
                receiver.sendMessage(" = " + ChatColor.GRAY + "Url: " + ChatColor.AQUA + URL_RELEASE + release.tag)
            }
        }
        val block : Callable<Release?> = if (last == null || canRecheck()) {
            {
                receiver.sendMessage(LDKPlugin.PREFIX + ChatColor.GRAY + "Checking version, please wait...")
                doCheck()
                lastRelease
            }
        } else {
            {
                last
            }
        }
        plugin.runTaskAsync {
            if (receiver is Player && !(receiver as Player).isOnline)
                receiver = Bukkit.getConsoleSender()
            receiver.sendMessage(LDKPlugin.PREFIX + ChatColor.GRAY + "The LDK plugin version: ${ChatColor.GREEN}$currentVersion")
            val release = block()
            if (release == null) {
                receiver.sendMessage(LDKPlugin.PREFIX + ChatColor.RED + "Internal error, check is not available.")
            } else {
                if (currentVersion >= release.tag)
                    receiver.sendMessage(LDKPlugin.PREFIX + ChatColor.GREEN + "You are using the latest version of the LDK plugin.")
                else
                    pushed(release)
            }
        }
    }

    data class Release(
            val tag: String,
            val releasedAt: String,
            val isPreRelease: Boolean,
            val author: String,
            val authorUrl: String,
            val fileName: String?,
            val fileDownloadUrl: String?
    )

    companion object Constants {

        private const val MAX_CHECK_INTERVAL = 10 * 60 * 1000L // 10 minutes
        private const val API_VERSION = "https://api.github.com/repos/lgou2w/ldk/releases/latest"
        private const val URL_RELEASE = "https://github.com/lgou2w/ldk/releases/tag/" // {tag}
        private const val RELEASE_TAG = "tag_name" // String
        private const val RELEASE_PRE = "prerelease" // Boolean
        private const val RELEASE_AT = "published_at" // yyyy-MM-ddTHH:mm:ssZ
        private const val RELEASE_AUTHOR = "author" // JsonObject
        private const val RELEASE_AUTHOR_NAME = "login" // string
        private const val RELEASE_AUTHOR_URL = "html_url" // string
        private const val RELEASE_DOWNLOADS = "assets" // JsonArray
        private const val RELEASE_DOWNLOAD_NAME = "name" // String
        private const val RELEASE_DOWNLOAD_URL = "browser_download_url" // String

        @Throws(Exception::class)
        private fun parseLatestRelease() : Release? {
            val content = URL(API_VERSION).readText(Charsets.UTF_8)
            val json = Gson().fromJson<JsonObject>(content, JsonObject::class.java)
            val tag = json.get(RELEASE_TAG)?.asString ?: return null
            val releasedAt = json.get(RELEASE_AT).asString
            val isPreRelease = json.get(RELEASE_PRE).asBoolean
            val authorJson = json.get(RELEASE_AUTHOR)?.asJsonObject
            val author = authorJson?.get(RELEASE_AUTHOR_NAME)?.asString ?: "UNKNOWN"
            val authorUrl = authorJson?.get(RELEASE_AUTHOR_URL)?.asString ?: "404 Not Found"
            val downloadJson = json.get(RELEASE_DOWNLOADS)?.asJsonArray?.firstOrNull()?.asJsonObject
            val fileName = downloadJson?.get(RELEASE_DOWNLOAD_NAME)?.asString
            val fileDownloadUrl = downloadJson?.get(RELEASE_DOWNLOAD_URL)?.asString
            return Release(tag, releasedAt, isPreRelease, author, authorUrl, fileName, fileDownloadUrl)
        }
    }
}
