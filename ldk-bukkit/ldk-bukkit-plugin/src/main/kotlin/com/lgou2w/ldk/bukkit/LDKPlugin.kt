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
        logger.info("游戏版本: ${MinecraftVersion.CURRENT.version} 实现版本: ${MinecraftBukkitVersion.CURRENT.version}")
    }

    override fun disable() {
    }
}
