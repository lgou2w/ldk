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

package com.lgou2w.ldk.common

import java.io.File
import java.io.IOException
import java.io.Serializable
import java.util.regex.Pattern

data class JavaVersion(
        val binary: File,
        val version: String,
        val platform: Platform
) : Serializable {

    val parsedVersion: Int = parseVersion(version)

    companion object Constants {

        private val REGEX = Pattern.compile("version \"(?<version>(.*?))\"")

        @JvmField val UNKNOWN = -1
        @JvmField val JAVA_5 = 50
        @JvmField val JAVA_6 = 60
        @JvmField val JAVA_7 = 70
        @JvmField val JAVA_8 = 80
        @JvmField val JAVA_9 = 90
        @JvmField val JAVA_X = 100

        private fun parseVersion(version: String): Int {
            return when {
                version.startsWith("10") || version.startsWith("X") -> JAVA_X
                version.contains("1.9.") || version.startsWith("9") -> JAVA_9
                version.contains("1.8") -> JAVA_8
                version.contains("1.7") -> JAVA_7
                version.contains("1.6") -> JAVA_6
                version.contains("1.5") -> JAVA_5
                else -> UNKNOWN
            }
        }

        fun fromExecutable(executable: File) : JavaVersion {
            var actualFile = executable
            var platform = Platform.BIT_32
            var version: String? = null
            if ("javaw" == actualFile.nameWithoutExtension)
                actualFile = File(actualFile.absoluteFile.parentFile, "java")
            try {
                val process = ProcessBuilder(actualFile.absolutePath, "-version").start()
                process.waitFor()
                process.errorStream.bufferedReader().lines().forEach {
                    val matcher = REGEX.matcher(it)
                    if (matcher.find())
                        version = matcher.group("version")
                    if (it.contains("64-Bit"))
                        platform = Platform.BIT_64
                }
            } catch (e: InterruptedException) {
                throw IOException("扫描 Java 版本时中断.", e)
            }
            if (version == null)
                throw IOException("未匹配到 Java 版本号.")
            if (parseVersion(version!!) == UNKNOWN)
                throw IOException("无法识别的 Java 版本号: $version.")
            return JavaVersion(actualFile, version!!, platform)
        }

        fun fromJavaHome(home: File): JavaVersion {
            return fromExecutable(getJavaFile(home))
        }

        private fun getJavaFile(home: File): File {
            val path = File(home, "bin")
            val javaw = File(path, "javaw.exe")
            return if (OperatingSystem.CURRENT == OperatingSystem.WINDOWS && javaw.isFile)
                javaw
            else
                File(path, "java")
        }

        @JvmField
        val RUNTIME = JavaVersion(
                getJavaFile(File(System.getProperty("java.home"))),
                System.getProperty("java.version"),
                Platform.CURRENT)
    }
}
