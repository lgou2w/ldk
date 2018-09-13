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

import com.lgou2w.ldk.bukkit.event.registerListeners
import com.lgou2w.ldk.bukkit.gui.ButtonEvent
import com.lgou2w.ldk.bukkit.gui.ButtonSame
import com.lgou2w.ldk.bukkit.gui.GuiType
import com.lgou2w.ldk.bukkit.gui.SimpleGui
import com.lgou2w.ldk.bukkit.item.isAir
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.bukkit.version.MinecraftVersion
import com.lgou2w.ldk.chat.ChatColor
import com.lgou2w.ldk.chat.toColor
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.logging.Level

class LDKPlugin : PluginBase() {

    override val enableDependencies = arrayOf(
            dependency {
                name = "WorldEdit"
                softDepend = true
            },
            dependency {
                name = "Vault"
                softDepend = true
            },
            dependency {
                name = "PlaceholderAPI"
                softDepend = true
            }
    )

    override fun load() {
    }

    override fun enable() {
        logger.info("A lgou2w development kit of Bukkit.")
        logger.info("Game Version: ${MinecraftVersion.CURRENT.version} Impl Version: ${MinecraftBukkitVersion.CURRENT.version}")
        try {
            Metrics(this)
        } catch (e: Exception) {
            logger.log(Level.WARNING, "Metrics stats service not loaded successfully.", e.cause ?: e)
        }
        registerListeners {
            event<PlayerCommandPreprocessEvent> {
                if (message == "/gui") {
                    val gui = SimpleGui(GuiType.CHEST_6, "Crafting Gui")
                    gui.setSameButton(intArrayOf(
                              0,   1,   2,   3,   4,   5,   6,   7,  8,
                              9,                        14, 15, 16, 17,
                            18,                        23,       25, 26,
                            27,                        32, 33, 34, 35,
                            36,                        41, 42, 43, 44,
                            45, 46, 47, 48, 49, 50, 51, 52, 53
                    ), ItemStack(Material.RED_STAINED_GLASS_PANE), ButtonEvent.CANCELLED)
                    gui.setSameButton(intArrayOf(
                            10, 11, 12, 13,
                            19, 20, 21, 22,
                            28, 29, 30, 31,
                            37, 38, 39, 40
                    )) { event ->
                        Bukkit.getScheduler().runTaskLater(plugin, {
                            val result = event.button.parent.getButton(24)
                            val stacks = (event.button as ButtonSame).stacks
                            if (stacks.count { it != null && it.type == Material.OAK_LOG } == 1) {
                                result?.stack = ItemStack(Material.OAK_PLANKS, 4)
                            } else {
                                result?.stack = null
                            }
                        }, 1L)
                    }
                    gui.setButton(24) { event ->
                        val cursor = event.clicker.itemOnCursor
                        if (cursor.isAir()) {
                            val clicked = event.clicked
                            event.button.parent.getButton(10)?.stack = null
                            event.button.stack = null
                            Bukkit.getScheduler().runTaskLater(plugin, {
                                event.clicker.itemOnCursor = clicked
                            }, 1L)
                        } else {
                            event.source.isCancelled = true
                        }
                    }
                    gui.isAllowMove = true
                    gui.open(player)
                }
            }
        }
    }

    override fun disable() {
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
            sender.sendMessage(ChatColor.GRAY + "The LDK plugin version: ${ChatColor.GREEN}$pluginVersion")
            sender.sendMessage(ChatColor.GRAY + "Checking version, please wait...")
            SimpleVersionChecker(this) { last, ex ->
                if (ex != null) {
                    sender.sendMessage(ChatColor.RED + "Exception when checking version: ${ex.message}")
                    ex.printStackTrace()
                } else {
                    sender.sendMessage(ChatColor.GRAY + "Latest version: ${ChatColor.GREEN}$last")
                    sender.sendMessage(ChatColor.GRAY + "https://github.com/lgou2w/ldk/releases/tag/$last")
                }
            }
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
