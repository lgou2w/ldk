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

import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.bukkit.version.MinecraftVersion
import com.lgou2w.ldk.chat.toColor
import com.lgou2w.ldk.common.isOrLater
import org.bstats.bukkit.Metrics
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*
import java.util.logging.Level

class LDKPlugin : PluginBase() {

    companion object Constants {
        const val NAME = com.lgou2w.ldk.common.Constants.LDK
        const val PREFIX = "[$NAME] "
        const val GITHUB = com.lgou2w.ldk.common.Constants.LDK_GITHUB
    }

    private var updater : VersionUpdater? = null

    override fun load() {
    }

    override fun enable() {
        val safeCurrent = try {
            MinecraftBukkitVersion.CURRENT
        } catch (e: Exception) {
            null
        }
        if (safeCurrent == null || !safeCurrent.isOrLater(MinecraftBukkitVersion.V1_8_R1)) {
            // Very sorry. Although can load LDK, it is not compatible with 1.7 and previous versions from the beginning of design.
            logger.severe("-------- A lgou2w development kit of Bukkit -----")
            logger.severe("  Very sorry. Although can load LDK, it is not compatible")
            logger.severe("  with 1.7 and previous versions from the beginning of design.")
            logger.severe("  Thank you for downloaded and trying this plugin.")
            logger.severe("-------------")
            server.pluginManager.disablePlugin(this)
            return
        }
        logger.info("A lgou2w development kit of Bukkit.")
        logger.info("Open source: $GITHUB")
        logger.info("Game Version: ${MinecraftVersion.CURRENT.version} Impl Version: ${MinecraftBukkitVersion.CURRENT.version}")
        try {
            Metrics(this)
        } catch (e: Exception) {
            logger.log(Level.WARNING, "Metrics stats service not loaded successfully.", e.cause ?: e)
        }
        updater = VersionUpdater(this)
        updater?.firstCheck()
    }

    override fun disable() {
        updater = null
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val first = args.firstOrNull()
        return if (first == null || first.equals("help", true)) {
            sender.sendMessage(arrayOf(
                    "&7-------- &aA lgou2w development kit of Bukkit &7-----",
                    "&6/ldk help &8- &7View command help.",
                    "&6/ldk version &8- &7View current plugin version."
            ).toColor())
            true
        } else if (first.equals("version", true)) {
            updater?.pushRelease(sender)
            true
        } else {
            false
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        if (args.isEmpty())
            return Collections.emptyList()
        val lastWord = args.last()
        val keyWords = arrayOf("help", "version").filter { it.startsWith(lastWord) }
        return if (keyWords.isEmpty()) Collections.emptyList() else keyWords
    }
}
