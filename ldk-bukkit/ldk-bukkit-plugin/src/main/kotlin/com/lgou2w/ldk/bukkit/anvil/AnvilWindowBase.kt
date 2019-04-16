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

package com.lgou2w.ldk.bukkit.anvil

import com.lgou2w.ldk.asm.ASMClassLoader
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin

// FIXME: DEBUG CODE
abstract class AnvilWindowBase(val plugin: Plugin) {

    open fun open(player: Player) {
    }

    abstract val inventory : Inventory

    companion object {

        private val classes by lazy {
            ASMClassLoader.ofInstance()
                .defineClasses(AnvilWindowGenerator().generate())
        }

        @JvmStatic
        fun of(plugin: Plugin): AnvilWindowBase {
            val clazz = classes.find { AnvilWindowBase::class.java.isAssignableFrom(it) } as Class<out AnvilWindowBase>
            return clazz.getConstructor(Plugin::class.java).newInstance(plugin)
        }
    }
}
