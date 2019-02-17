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

package com.lgou2w.ldk.bukkit.version

@Retention(AnnotationRetention.RUNTIME)
annotation class API(val level: Level)

@Suppress("EnumEntryName")
enum class Level {

    Minecraft_V1_8,
    Minecraft_V1_9,
    Minecraft_V1_10,
    Minecraft_V1_11,
    Minecraft_V1_12,
    Minecraft_V1_13,
    Minecraft_V1_14,
    ;

    val version : MinecraftVersion
        get() = MinecraftVersion.fromLevel(this)
}
