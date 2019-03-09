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

/**
 * ## DependPlaceholderAPI (PlaceholderAPI 插件依赖)
 *
 * @see [Depend]
 * @see [DependBase]
 * @author lgou2w
 */
class DependPlaceholderAPI : DependBase<PlaceholderAPIPlugin>(getPlugin(NAME)) {

    /**
     * * Gets whether this PlaceholderAPI plugin dependency supports relational placeholders.
     * * 获取此 PlaceholderAPI 插件依赖是否支持关系占位符.
     *
     * **Note: Requires PlaceholderAPI greater than or equal to version 2.8.0.**
     */
    val canSupportRelational : Boolean
        get() {
            checkReference()
            return pluginVersion >= "2.8.0"
        }

    /**
     * * Placeholders processing for the given player [player] and text [text]. Pattern: `%identifier_params%`
     * * 将给定的玩家 [player] 和文本 [text] 进行占位符处理. 模式: `%identifier_params%`
     */
    fun setPlaceholders(player: Player?, text: String): String {
        checkReference()
        return PlaceholderAPI.setPlaceholders(player, text)
    }

    /**
     * * Placeholders processing for the given player [player] and text collection [texts]. Pattern: `%identifier_params%`
     * * 将给定的玩家 [player] 和文本集合 [texts] 进行占位符处理. 模式: `%identifier_params%`
     */
    fun setPlaceholders(player: Player?, texts: List<String>): List<String> {
        checkReference()
        return PlaceholderAPI.setPlaceholders(player, texts)
    }

    /**
     * * Placeholders processing for the given player [player] and text [text]. Pattern: `{identifier_params}`
     * * 将给定的玩家 [player] 和文本 [text] 进行占位符处理. 模式: `{identifier_params}`
     */
    fun setBracketPlaceholders(player: Player?, text: String): String {
        checkReference()
        return PlaceholderAPI.setBracketPlaceholders(player, text)
    }

    /**
     * * Placeholders processing for the given player [player] and text collection [texts]. Pattern: `{identifier_params}`
     * * 将给定的玩家 [player] 和文本集合 [texts] 进行占位符处理. 模式: `{identifier_params}`
     */
    fun setBracketPlaceholders(player: Player?, texts: List<String>): List<String> {
        checkReference()
        return PlaceholderAPI.setBracketPlaceholders(player, texts)
    }

    /**
     * * Relational placeholder processing for given players [one], [two], and text [text]. Pattern: `%rel_identifier_params%`
     * * 将给定的玩家 [one]、[two] 和文本 [text] 进行关系占位符处理. 模式: `%rel_identifier_params%`
     *
     * @throws [DependCannotException] If the PlaceholderAPI plugin is smaller than the `2.8.0` version.
     * @throws [DependCannotException] 如果 PlaceholderAPI 插件小于 `2.8.0` 版本.
     */
    @Throws(DependCannotException::class)
    fun setRelationalPlaceholders(one: Player?, two: Player?, text: String): String {
        checkReference()
        if (canSupportRelational) {
            return PlaceholderAPI.setRelationalPlaceholders(one, two, text)
        } else {
            throw DependCannotException("PlaceholderAPI#setRelationalPlaceholders() Minimum requirements 2.8.0 version.")
        }
    }

    /**
     * * Relational placeholder processing for given players [one], [two], and text collection [texts]. Pattern: `%rel_identifier_params%`
     * * 将给定的玩家 [one]、[two] 和文本集合 [texts] 进行关系占位符处理. 模式: `%rel_identifier_params%`
     *
     * @throws [DependCannotException] If the PlaceholderAPI plugin is smaller than the `2.8.0` version.
     * @throws [DependCannotException] 如果 PlaceholderAPI 插件小于 `2.8.0` 版本.
     */
    @Throws(DependCannotException::class)
    fun setRelationalPlaceholders(one: Player?, two: Player?, texts: List<String>): List<String> {
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
