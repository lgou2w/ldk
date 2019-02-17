/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.bukkit.depend

import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.PlaceholderAPIPlugin
import org.bukkit.entity.Player

class DependPlaceholderAPI : DependBase<PlaceholderAPIPlugin>(getPlugin(NAME)) {

    val canSupportRelational : Boolean
        get() {
            checkReference()
            return pluginVersion >= "2.8.0"
        }

    fun setPlaceholders(player: Player, text: String) : String {
        checkReference()
        return PlaceholderAPI.setPlaceholders(player, text)
    }

    fun setPlaceholders(player: Player, texts: List<String>) : List<String> {
        checkReference()
        return PlaceholderAPI.setPlaceholders(player, texts)
    }

    fun setBracketPlaceholders(player: Player, text: String) : String {
        checkReference()
        return PlaceholderAPI.setBracketPlaceholders(player, text)
    }

    fun setBracketPlaceholders(player: Player, texts: List<String>) : List<String> {
        checkReference()
        return PlaceholderAPI.setBracketPlaceholders(player, texts)
    }

    @Throws(DependCannotException::class)
    fun setRelationalPlaceholders(one: Player, two: Player, text: String) : String {
        checkReference()
        if (canSupportRelational) {
            return PlaceholderAPI.setRelationalPlaceholders(one, two, text)
        } else {
            throw DependCannotException("PlaceholderAPI#setRelationalPlaceholders() Minimum requirements 2.8.0 version.")
        }
    }

    @Throws(DependCannotException::class)
    fun setRelationalPlaceholders(one: Player, two: Player, texts: List<String>) : List<String> {
        checkReference()
        if (canSupportRelational) {
            return PlaceholderAPI.setRelationalPlaceholders(one, two, texts)
        } else {
            throw DependCannotException("PlaceholderAPI#setRelationalPlaceholders() Minimum requirements 2.8.0 version.")
        }
    }

    companion object {
        const val NAME = "PlaceholderAPI"
    }
}
