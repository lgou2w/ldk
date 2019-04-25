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

import com.lgou2w.ldk.common.Version
import com.lgou2w.ldk.common.isOrLater
import org.bukkit.Bukkit
import java.util.NavigableMap
import java.util.TreeMap
import java.util.regex.Pattern

/**
 * ## MinecraftBukkitVersion (Minecraft Bukkit 版本)
 *
 * @see [Version]
 * @see [Comparable]
 * @author lgou2w
 */
class MinecraftBukkitVersion(
        major: Int,
        minor: Int,
        build: Int
) : Version(major, minor, build),
        Comparable<Version> {

    companion object {

        @JvmField val UNKNOWN = MinecraftBukkitVersion(-1, -1, -1)
        @JvmField val V1_8_R1 = MinecraftBukkitVersion(1, 8, 1)
        @JvmField val V1_8_R2 = MinecraftBukkitVersion(1, 8, 2)
        @JvmField val V1_8_R3 = MinecraftBukkitVersion(1, 8, 3)
        @JvmField val V1_9_R1 = MinecraftBukkitVersion(1, 9, 1)
        @JvmField val V1_9_R2 = MinecraftBukkitVersion(1, 9, 2)
        @JvmField val V1_10_R1 = MinecraftBukkitVersion(1, 10, 1)
        @JvmField val V1_11_R1 = MinecraftBukkitVersion(1, 11, 1)
        @JvmField val V1_12_R1 = MinecraftBukkitVersion(1, 12, 1)
        @JvmField val V1_13_R1 = MinecraftBukkitVersion(1, 13, 1)
        @JvmField val V1_13_R2 = MinecraftBukkitVersion(1, 13, 2)
        @JvmField val V1_14_R1 = MinecraftBukkitVersion(1, 14, 1)
        /**
         * @since LDK 0.1.8-rc
         */
        @Draft
        @Deprecated("Minecraft 1.15 Draft")
        @JvmField val V1_15_R1 = MinecraftBukkitVersion(1, 15, 1)

        @JvmStatic
        private val VERSION_PATTERN = Pattern.compile("(?i)^v(\\d+)_(\\d+)_r(\\d+)$")

        /**
         * * Get the current version of the Minecraft Bukkit implementation of the Bukkit server.
         * * 获取当前 Bukkit 服务器的 Minecraft Bukkit 实现版本.
         */
        @JvmStatic
        var CURRENT : MinecraftBukkitVersion = UNKNOWN
            private set
            get() {
                if (field == UNKNOWN) {
                    val packageSplit = Bukkit.getServer().javaClass.`package`.name.split(Pattern.compile("\\."))
                    val matcher = VERSION_PATTERN.matcher(packageSplit.last())
                    if (!matcher.matches())
                        throw IllegalStateException("Bukkit NMS version number not successfully matched: ${packageSplit.last()}")
                    field = MinecraftBukkitVersion(
                            matcher.group(1).toInt(),
                            matcher.group(2).toInt(),
                            matcher.group(3).toInt())
                }
                return field
            }

        // lazy

        /**
         * * Gets whether the current Bukkit server is an implementation version of `1.9` or later.
         * * 获取当前 Bukkit 服务器是否是 `1.9` 或之后的实现版本.
         *
         * @since LDK 0.1.8-rc
         */
        @JvmStatic val isV19OrLater by lazy { CURRENT.isOrLater(V1_9_R1) }

        /**
         * * Gets whether the current Bukkit server is an implementation version of `1.10` or later.
         * * 获取当前 Bukkit 服务器是否是 `1.10` 或之后的实现版本.
         *
         * @since LDK 0.1.8-rc
         */
        @JvmStatic val isV110OrLater by lazy { CURRENT.isOrLater(V1_10_R1) }

        /**
         * * Gets whether the current Bukkit server is an implementation version of `1.11` or later.
         * * 获取当前 Bukkit 服务器是否是 `1.11` 或之后的实现版本.
         *
         * @since LDK 0.1.8-rc
         */
        @JvmStatic val isV111OrLater by lazy { CURRENT.isOrLater(V1_11_R1) }

        /**
         * * Gets whether the current Bukkit server is an implementation version of `1.12` or later.
         * * 获取当前 Bukkit 服务器是否是 `1.12` 或之后的实现版本.
         *
         * @since LDK 0.1.8-rc
         */
        @JvmStatic val isV112OrLater by lazy { CURRENT.isOrLater(V1_12_R1) }

        /**
         * * Gets whether the current Bukkit server is an implementation version of `1.13` or later.
         * * 获取当前 Bukkit 服务器是否是 `1.13` 或之后的实现版本.
         *
         * @since LDK 0.1.8-rc
         */
        @JvmStatic val isV113OrLater by lazy { CURRENT.isOrLater(V1_13_R1) }

        /**
         * * Gets whether the current Bukkit server is an implementation version of `1.14` or later.
         * * 获取当前 Bukkit 服务器是否是 `1.14` 或之后的实现版本.
         *
         * @since LDK 0.1.8-rc
         */
        @JvmStatic val isV114OrLater by lazy { CURRENT.isOrLater(V1_14_R1) }

        /**
         * * Gets whether the current Bukkit server is an implementation version of `1.15` or later.
         * * 获取当前 Bukkit 服务器是否是 `1.15` 或之后的实现版本.
         *
         * @since LDK 0.1.8-rc
         */
        @Draft
        @Deprecated("Minecraft 1.15 Draft")
        @JvmStatic val isV115OrLater by lazy { CURRENT.isOrLater(V1_15_R1) }

        private val LOOKUP : NavigableMap<MinecraftVersion, MinecraftBukkitVersion> = createLookup()
        private fun createLookup(): NavigableMap<MinecraftVersion, MinecraftBukkitVersion> {
            val map = object: TreeMap<MinecraftVersion, MinecraftBukkitVersion>() {
                operator fun set(keys: Array<out MinecraftVersion>, value: MinecraftBukkitVersion)
                        = keys.forEach { put(it, value) }
            }
            map[arrayOf(
                    MinecraftVersion(1, 8, 0),
                    MinecraftVersion(1, 8, 1),
                    MinecraftVersion(1, 8, 2)
                    // ---> net.minecraft.server.v1_8_R1
            )] = V1_8_R1
            map[arrayOf(
                    MinecraftVersion(1, 8, 3),
                    MinecraftVersion(1, 8, 4),
                    MinecraftVersion(1, 8, 5),
                    MinecraftVersion(1, 8, 6),
                    MinecraftVersion(1, 8, 7)
                    // ---> net.minecraft.server.v1_8_R2
            )] = V1_8_R2
            map[arrayOf(
                    MinecraftVersion(1, 8, 8),
                    MinecraftVersion(1, 8, 9)
                    // ---> net.minecraft.server.v1_8_R3
            )] = V1_8_R3
            map[arrayOf(
                    MinecraftVersion(1, 9, 0),
                    MinecraftVersion(1, 9, 1),
                    MinecraftVersion(1, 9, 2),
                    MinecraftVersion(1, 9, 3)
                    // ---> net.minecraft.server.v1_9_R1
            )] = V1_9_R1
            map[arrayOf(
                    MinecraftVersion(1, 9, 4)
                    // ---> net.minecraft.server.v1_9_R2
            )] = V1_9_R2
            map[arrayOf(
                    MinecraftVersion(1, 10, 0),
                    MinecraftVersion(1, 10, 1),
                    MinecraftVersion(1, 10, 2)
                    // ---> net.minecraft.server.v1_10_R1
            )] = V1_10_R1
            map[arrayOf(
                    MinecraftVersion(1, 11, 0),
                    MinecraftVersion(1, 11, 1),
                    MinecraftVersion(1, 11, 2)
                    // ---> net.minecraft.server.v1_11_R1
            )] = V1_11_R1
            map[arrayOf(
                    MinecraftVersion(1, 12, 0),
                    MinecraftVersion(1, 12, 1),
                    MinecraftVersion(1, 12, 2)
                    // ---> net.minecraft.server.v1_12_R1
            )] = V1_12_R1
            map[arrayOf(
                    MinecraftVersion(1, 13, 0)
                    // ---> net.minecraft.server.v1_13_R1
            )] = V1_13_R1
            map[arrayOf(
                    MinecraftVersion(1, 13, 1),
                    MinecraftVersion(1, 13, 2)
                    // ---> net.minecraft.server.v1_13_R2
            )] = V1_13_R2
            map[arrayOf(
                    MinecraftVersion(1, 14, 0)
                    // ---> net.minecraft.server.v1_14_R1
            )] = V1_14_R1
            map[arrayOf(
                    MinecraftVersion(1, 15, 0)
                    // ---> net.minecraft.server.v1_15_R1
            )] = V1_15_R1
            return map
        }

        /**
         * * Get the Bukkit implementation version from the given Minecraft version [mcVer].
         * * 从给定的 Minecraft 版本 [mcVer] 获取 Bukkit 实现版本.
         */
        @JvmStatic
        fun lookup(mcVer: MinecraftVersion): MinecraftBukkitVersion {
            val compilerVer = if (mcVer.isPre) MinecraftVersion(mcVer.major, mcVer.minor, mcVer.build) else mcVer
            val entry = LOOKUP.floorEntry(compilerVer)
            return if (entry != null) entry.value else UNKNOWN
        }
    }

    override val version: String
        get() = "v${major}_${minor}_R$build"

    override fun toString(): String {
        return "MinecraftBukkitVersion(major=$major, minor=$minor, build=$build)"
    }
}
