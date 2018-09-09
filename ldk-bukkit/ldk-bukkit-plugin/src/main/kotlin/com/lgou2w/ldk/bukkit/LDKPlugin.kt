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

import com.lgou2w.ldk.bukkit.chat.send
import com.lgou2w.ldk.bukkit.chat.tooltipItem
import com.lgou2w.ldk.bukkit.event.registerListeners
import com.lgou2w.ldk.bukkit.item.Enchantment
import com.lgou2w.ldk.bukkit.item.builder
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.bukkit.version.MinecraftVersion
import com.lgou2w.ldk.chat.ChatColor
import com.lgou2w.ldk.chat.ChatComponentFancy
import com.lgou2w.ldk.chat.ChatSerializer
import com.lgou2w.ldk.chat.toColor
import org.bukkit.Material
import org.bukkit.event.player.PlayerCommandPreprocessEvent

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
        registerListeners {
            event<PlayerCommandPreprocessEvent> {
                if (!player.isOp)
                    return@event
                when (message) {
                    "/ib" -> {
                        val stack = Material.IRON_SWORD.builder()
                            .setDurability(100)
                            .setDisplayName(ChatSerializer.fromRaw("&aIron Sword"))
                            .addEnchantment(Enchantment.SHARPNESS, 5)
                            .addEnchantment(Enchantment.UNBREAKING, 1)
                            .clearEnchantment()
                            .addEnchantment(Enchantment.SHARPNESS, 5)
                            .addLore(*arrayOf("&aLore").toColor(), "Lore")
                            .getEnchantment { _, enchantments -> enchantments?.forEach { println(it) } }
                            .getLore { _, lore -> lore?.forEach { println(it) } }
                            .setDurability(0)
                            .removeLore { it.startsWith(ChatColor.CHAR_COLOR) }
                            .removeEnchantment { it.first == Enchantment.SHARPNESS && it.second > 3 }
                            .build()
                        ChatComponentFancy("You get an item: ")
                            .color(ChatColor.GREEN)
                            .then("[")
                            .color(ChatColor.GRAY)
                            .then("Iron Sword")
                            .color(ChatColor.GREEN)
                            .tooltipItem(stack)
                            .then("]")
                            .color(ChatColor.GRAY)
                            .build()
                            .send(player)
                        player.inventory.addItem(stack)
                    }
                }
            }
        }
    }

    override fun disable() {
    }
}
