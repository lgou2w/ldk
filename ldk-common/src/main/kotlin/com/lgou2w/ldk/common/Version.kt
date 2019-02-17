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

package com.lgou2w.ldk.common

import java.util.regex.Pattern

/**
 * ## Version (版本)
 *
 * * A version class with major, minor, and build numbers.
 * * 一个具有主要、次要以及构建号的版本类.
 *
 * @see [Comparable]
 * @author lgou2w
 */
open class Version(
        /**
         * * The major number of the version.
         * * 版本的主要号.
         */
        val major: Int,
        /**
         * * The minor number of the version.
         * * 版本的次要号.
         */
        val minor: Int,
        /**
         * * The build number of the version.
         * * 版本的构建号.
         */
        val build: Int
) : Comparable<Version> {

    /**
     * * The string value of the version number. E.g: `1.0.0`
     * * 版本号的字符串值. 例如: `1.0.0`
     */
    open val version: String
        get() = "$major.$minor.$build"

    override fun compareTo(other: Version): Int {
        return ComparisonChain.start()
                .compare(major, other.major)
                .compare(minor, other.minor)
                .compare(build, other.build)
                .result
    }

    override fun hashCode(): Int {
        var result = major.hashCode()
        result = 31 * result + minor.hashCode()
        result = 31 * result + build.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is Version)
            return major == other.major && minor == other.minor && build == other.build
        return false
    }

    override fun toString(): String {
        return "Version(major=$major, minor=$minor, build=$build)"
    }

    companion object {

        @JvmStatic
        private val VERSION_PATTERN = Pattern.compile("^(?<major>\\d+)\\.(?<minor>\\d+)\\.(?<build>\\d+)$")

        /**
         * @since LDK 0.1.7-rc6
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun parse(versionOnly: String): Version {
            val matcher = VERSION_PATTERN.matcher(versionOnly)
            if (!matcher.matches())
                throw IllegalArgumentException("Illegal version format, must be: x.y.z")
            return Version(
                    matcher.group("major").toInt(),
                    matcher.group("minor").toInt(),
                    matcher.group("build").toInt()
            )
        }

        /**
         * @since LDK 0.1.7-rc6
         */
        @JvmStatic
        fun parseSafely(versionOnly: String?): Version? {
            if (versionOnly == null || versionOnly.isBlank()) return null
            return try {
                parse(versionOnly)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}
