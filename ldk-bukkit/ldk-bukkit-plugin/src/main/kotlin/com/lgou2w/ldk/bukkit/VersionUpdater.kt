/*
 * Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>
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
import com.lgou2w.ldk.common.ComparisonChain
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
    // cli : -Dldk.bukkit.autoUpdater=true
    val enable = System.getProperty("ldk.bukkit.autoUpdater")?.toBoolean() ?: true
    if (!enable)
      return
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
      receiver.sendMessage(LDKPlugin.PREFIX + ChatColor.GRAY + "The plugin version: ${ChatColor.GREEN}$currentVersion")
      val release = block()
      if (release == null) {
        receiver.sendMessage(LDKPlugin.PREFIX + ChatColor.RED + "Internal error, check is not available.")
      } else {
        if (!needUpdate(release.tag))
          receiver.sendMessage(LDKPlugin.PREFIX + ChatColor.GREEN + "You are using the latest version.")
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

  private fun needUpdate(tag: String): Boolean {
    val currentVersion = parseVersion(currentVersion)
    val tagVersion = parseVersion(tag)
    return currentVersion < tagVersion
  }

  private fun parseVersion(versionOnly: String): Version {
    return if (versionOnly < "1.0") {
      // e.g.: 0.1.8-beta10, 0.2.0-alpha2, 0.2.1-beta2-hotfix1, 0.3.0-rc-SNAPSHOT, 0.1.8-SNAPSHOT
      val versions = versionOnly.split('-')
      val ver = versions[0]
      val (type, value) = parseZeroVersionValue(versions.getOrNull(1))
      val hotfix = parseZeroVersionHotfix(versions.getOrNull(2))
      val isSnapshot = versionOnly.endsWith("-SNAPSHOT")
      Version.ZeroVersion(ver, type, value, hotfix, isSnapshot)
    } else {
      // e.g.: 1.0, 1.0.1-SNAPSHOT
      val versions = versionOnly.split('-')
      val ver = versions[0]
      val type = versions.getOrNull(1)
      val isSnapshot = versionOnly.endsWith("-SNAPSHOT")
      Version.ReleaseVersion(ver, type, isSnapshot)
    }
  }

  private fun parseZeroVersionValue(zeroVersionTypeOnly: String?): Pair<String, Int> {
    if (zeroVersionTypeOnly == null || zeroVersionTypeOnly == "SNAPSHOT")
      return "release" to 0
    val idx = zeroVersionTypeOnly.indexOfFirst { it in '0'..'9' }
    if (idx == -1)
      return zeroVersionTypeOnly to 0
    val type = zeroVersionTypeOnly.substring(0, idx)
    val typeVer = zeroVersionTypeOnly.substring(idx)
    return type to (typeVer.toIntOrNull() ?: 0)
  }

  private fun parseZeroVersionHotfix(zeroVersionHotfixOnly: String?): Int? {
    if (zeroVersionHotfixOnly == null) return null
    val idx = zeroVersionHotfixOnly.indexOfFirst { it in '0'..'9' }
    if (idx == -1)
      return 0
    return zeroVersionHotfixOnly.substring(idx).toIntOrNull() ?: 0
  }

  private sealed class Version(val ver: String, val isSnapshot: Boolean) : Comparable<Version> {
    class ZeroVersion(ver: String, val type: String, val value: Int, val hotfix: Int?, isSnapshot: Boolean) : Version(ver, isSnapshot)
    class ReleaseVersion(ver: String, val type: String?, isSnapshot: Boolean) : Version(ver, isSnapshot)
    override fun compareTo(other: Version): Int = compareVersion(this, other)
    companion object {
      private fun compareVersion(left: Version, right: Version): Int {
        return when (left) {
          is ZeroVersion -> when (right) {
            is ReleaseVersion -> -1
            is ZeroVersion -> {
              ComparisonChain.start()
                .compare(left.ver, right.ver)
                .compare(left.type, right.type)
                .compare(left.value, right.value)
                .compare(left.hotfix ?: 0, right.hotfix ?: 0)
                .compareTrueFirst(left.isSnapshot, right.isSnapshot)
                .result
            }
          }
          is ReleaseVersion -> when (right) {
            is ZeroVersion -> 1
            is ReleaseVersion -> {
              ComparisonChain.start()
                .compare(left.ver, right.ver)
                .compare(left.type ?: "", right.type ?: "")
                .compareTrueFirst(left.isSnapshot, right.isSnapshot)
                .result
            }
          }
        }
      }
    }
  }

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
    private fun parseLatestRelease(): Release? {
      val content : String
      try {
        content = URL(API_GITHUB).readText(Charsets.UTF_8)
      } catch (e: Exception) {
        return parseLatestReleaseFromJitpack()
      }
      val releases = Gson().fromJson(content, JsonArray::class.java)
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
    private fun parseLatestReleaseFromJitpack(): Release? {
      val content = URL(API_JITPACK).readText(Charsets.UTF_8)
      val releases = Gson().fromJson(content, JsonObject::class.java)
      val release = releases[RELEASE_GROUP_ID]?.asJsonObject ?: return null
      val tags = release[RELEASE_ARTIFACT_ID]?.asJsonObject ?: return null
      val tag = tags.entrySet().lastOrNull()?.key ?: return null
      val releasedAt = "Unknown, Non github.com source. See download url."
      val isPreRelease = if (tag < "1.0") !tag.contains("rc", true) || tag.endsWith("-SNAPSHOT") else tag.contains("-")
      val fileDownloadUrl = URL_RELEASE + tag
      return Release(tag, releasedAt, isPreRelease, "lgou2w", "https://github.com/lgou2w", null, fileDownloadUrl)
    }
  }
}
