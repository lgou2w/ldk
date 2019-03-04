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

import com.lgou2w.ldk.common.ComparisonChain
import com.lgou2w.ldk.common.Version
import org.bukkit.Bukkit
import java.util.Collections
import java.util.regex.Pattern

class MinecraftVersion(
        major: Int,
        minor: Int,
        build: Int,
        /**
         * @since LDK 0.1.8-rc
         */
        val pre: Int? = null
) : Version(major, minor, build),
        Comparable<Version> {

    companion object {

        @JvmField val UNKNOWN = MinecraftVersion(-1, -1, -1, -1)
        @JvmField val V1_8   = MinecraftVersion(1, 8, 0)
        @JvmField val V1_9   = MinecraftVersion(1, 9, 0)
        @JvmField val V1_10 = MinecraftVersion(1, 10, 0)
        @JvmField val V1_11 = MinecraftVersion(1, 11, 0)
        @JvmField val V1_12 = MinecraftVersion(1, 12, 0)
        @JvmField val V1_13 = MinecraftVersion(1, 13, 0)
        @JvmField val V1_14 = MinecraftVersion(1, 14, 0)

        @JvmStatic
        private val VERSION_PATTERN = Pattern.compile(".*\\(.*MC.\\s*([a-zA-Z0-9\\-.]+)\\s*\\)")

        @JvmStatic
        var CURRENT : MinecraftVersion = UNKNOWN
            private set
            get() {
                if (field == UNKNOWN) {
                    val version = Bukkit.getServer().version
                    val matcher = VERSION_PATTERN.matcher(version)
                    if (!matcher.matches() || matcher.group(1) == null)
                        throw IllegalStateException("Bukkit Minecraft version number not successfully matched: $version.")
                    val versionOnly = matcher.group(1)
                    val numbers = IntArray(3)
                    var pre: Int? = null
                    try {
                        val index = versionOnly.lastIndexOf("-pre")
                        val elements = if (index == -1)
                            versionOnly.split(Pattern.compile("\\."))
                            else versionOnly.substring(0, index).split(Pattern.compile("\\."))
                        if (elements.size == -1)
                            throw IllegalStateException("Invalid Bukkit Minecraft version number: $versionOnly")
                        for (i in 0 until Math.min(numbers.size, elements.size))
                            numbers[i] = elements[i].trim().toInt()
                        if (index != -1)
                            pre = versionOnly.substring(index + 4).toInt()
                    } catch (e: Exception) {
                        if (e is NumberFormatException)
                            throw IllegalStateException("Unable to parse Bukkit Minecraft version number: $versionOnly")
                        else
                            throw e
                    }
                    field = MinecraftVersion(numbers[0], numbers[1], numbers[2], pre)
                }
                return field
            }

        @JvmStatic private val LOOKUP_LEVEL : Map<Level, MinecraftVersion> =
                Collections.unmodifiableMap(mapOf(
                        Level.Minecraft_V1_8 to V1_8,
                        Level.Minecraft_V1_9 to V1_9,
                        Level.Minecraft_V1_10 to V1_10,
                        Level.Minecraft_V1_11 to V1_11,
                        Level.Minecraft_V1_12 to V1_12,
                        Level.Minecraft_V1_13 to V1_13,
                        Level.Minecraft_V1_14 to V1_14
                ))

        @JvmStatic
        fun fromLevel(level: Level) : MinecraftVersion {
            val value = LOOKUP_LEVEL[level]
            return value ?: UNKNOWN
        }
    }

    val isPre : Boolean
        get() = pre != null

    override val version: String
        get() = super.version + if (pre != null) "-pre$pre" else ""

    override fun compareTo(other: Version): Int {
        return if (other is MinecraftVersion)
            ComparisonChain.start()
                    .compare(major, other.major)
                    .compare(minor, other.minor)
                    .compare(build, other.build)
                    .compare(pre ?: -1, other.pre ?: -1)
                    .result
        else
            super.compareTo(other)
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (pre?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is MinecraftVersion)
            return super.equals(other) && isPre == other.isPre
        return false
    }

    override fun toString(): String {
        return "MinecraftVersion(major=$major, minor=$minor, build=$build, pre=$pre)"
    }
}
