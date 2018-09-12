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
import com.lgou2w.ldk.bukkit.gui.GuiType
import com.lgou2w.ldk.bukkit.gui.SimpleGui
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.bukkit.version.MinecraftVersion
import org.bstats.bukkit.Metrics
import org.bukkit.Material
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.inventory.ItemStack
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
                    val gui = SimpleGui(GuiType.CHEST_6, "My Gui")
                    val button = gui.setButton(0)
                    button.stack = ItemStack(Material.APPLE)
                    button.onClicked = ButtonEvent.thenCancelled { _ -> }
                    val buttonSame = gui.setSameButton(intArrayOf(1, 2, 3, 4, 5, 6, 7, 8))
                    buttonSame.stack = ItemStack(Material.BARRIER)
                    buttonSame.onClicked = ButtonEvent.thenCancelled { _ -> }
                    gui.open(player)
                }
            }
        }
    }

    override fun disable() {
    }
}
