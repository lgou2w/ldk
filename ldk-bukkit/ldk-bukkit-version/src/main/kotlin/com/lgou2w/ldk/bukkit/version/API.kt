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

package com.lgou2w.ldk.bukkit.version

/**
 * * The minimum required Bukkit API version to indicate a feature.
 * * 用于指示一个功能的最低需求 Bukkit API 版本.
 *
 * @see [Level]
 * @author lgou2w
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class API(val level: Level)

/**
 * * Level enumeration for the Bukkit API.
 * * 用于 Bukkit API 的等级枚举.
 *
 * @see [API]
 * @author lgou2w
 */
@Suppress("EnumEntryName")
enum class Level {

    Minecraft_V1_8,
    Minecraft_V1_9,
    Minecraft_V1_10,
    Minecraft_V1_11,
    Minecraft_V1_12,
    Minecraft_V1_13,
    Minecraft_V1_14,
    /**
     * @since LDK 0.1.8-rc
     */
    @Draft
    @Deprecated("Minecraft 1.15 Draft")
    Minecraft_V1_15,
    ;

    /**
     * * Get the version of Minecraft for this Bukkit API level.
     * * 获取此 Bukkit API 等级的 Minecraft 版本.
     *
     * @see [MinecraftVersion]
     */
    val version : MinecraftVersion
        get() = MinecraftVersion.fromLevel(this)
}

/**
 * ## Draft (草案)
 *
 * * 表明这个功能是未来版本的草案, 随时进行改动, 不建议使用.
 * * Indicates that this feature is a draft of a future version,
 *      which is subject to change at any time and is not recommended.
 *
 * @since LDK 0.1.8-rc
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class Draft
