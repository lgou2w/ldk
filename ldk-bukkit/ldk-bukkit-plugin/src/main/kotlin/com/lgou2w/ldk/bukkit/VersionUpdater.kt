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

internal class VersionUpdater(private val plugin: LDKPlugin) {

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
        }, 60 * 20L) // Wait 60 seconds
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
        private const val API_GITHUB = "https://api.github.com/repos/lgou2w/ldk/releases"
        private const val API_JITPACK = "https://jitpack.io/api/builds/com.github.lgou2w.ldk/ldk"
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
        private const val RELEASE_GROUP_ID = "com.github.lgou2w" // JsonObject
        private const val RELEASE_ARTIFACT_ID = "ldk" // JsonObject

        @Throws(Exception::class)
        private fun parseLatestRelease() : Release? {
            val content : String
            try {
                content = URL(API_GITHUB).readText(Charsets.UTF_8)
            } catch (e: Exception) {
                return parseLatestReleaseFromJitpack()
            }
            val releases = Gson().fromJson<JsonArray>(content, JsonArray::class.java)
            val release = releases.firstOrNull()?.asJsonObject ?: return null
            val tag = release.get(RELEASE_TAG).asString
            val releasedAt = release.get(RELEASE_AT).asString
            val isPreRelease = release.get(RELEASE_PRE).asBoolean
            val authorJson = release.get(RELEASE_AUTHOR)?.asJsonObject
            val author = authorJson?.get(RELEASE_AUTHOR_NAME)?.asString ?: "Unknown"
            val authorUrl = authorJson?.get(RELEASE_AUTHOR_URL)?.asString ?: "404 Not Found"
            val downloadJson = release.get(RELEASE_DOWNLOADS)?.asJsonArray?.firstOrNull()?.asJsonObject
            val fileName = downloadJson?.get(RELEASE_DOWNLOAD_NAME)?.asString
            val fileDownloadUrl = downloadJson?.get(RELEASE_DOWNLOAD_URL)?.asString
            return Release(tag, releasedAt, isPreRelease, author, authorUrl, fileName, fileDownloadUrl)
        }

        @Throws(Exception::class)
        private fun parseLatestReleaseFromJitpack() : Release? {
            val content = URL(API_JITPACK).readText(Charsets.UTF_8)
            val releases = Gson().fromJson<JsonObject>(content, JsonObject::class.java)
            val release = releases[RELEASE_GROUP_ID]?.asJsonObject ?: return null
            val tags = release[RELEASE_ARTIFACT_ID]?.asJsonObject ?: return null
            val tag = tags.entrySet().lastOrNull()?.key ?: return null
            val releasedAt = "Unknown, Non github.com source. See download url."
            val isPreRelease = if (tag < "1.0") !tag.contains("rc") else tag.contains("-")
            val fileDownloadUrl = URL_RELEASE + tag
            return Release(tag, releasedAt, isPreRelease, "lgou2w", "https://github.com/lgou2w", null, fileDownloadUrl)
        }
    }
}
