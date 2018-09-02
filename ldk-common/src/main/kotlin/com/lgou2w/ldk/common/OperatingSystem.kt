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
import java.lang.management.ManagementFactory
import java.nio.charset.Charset
import java.util.*
import javax.management.JMException
import javax.management.ObjectName

/**
 * ## OperatingSystem (操作系统)
 *
 * * Enumeration of operating system types, Windows, Linux, OS X
 * * 操作系统的类型枚举, Windows、Linux、OS X
 *
 * @see [OperatingSystem.CURRENT]
 * @see [Valuable]
 * @author lgou2w
 */
enum class OperatingSystem(
        /**
         * * The type name of the operating system.
         * * 操作系统的类型名称.
         */
        val type: String
) : Valuable<String> {

    WINDOWS ("windows"),
    LINUX ("linux"),
    OSX ("osx"),
    UNKNOWN ("universal"),
    ;

    override val value : String
        get() = type

    companion object {

        /**
         * * Indicates the operating system type of the current platform.
         * * 表示当前平台的操作系统类型.
         */
        @JvmField
        val CURRENT : OperatingSystem

        /**
         * * Indicates the total physical memory size of the operating system of the current platform. (MB)
         * * 表示当前平台的操作系统总物理内存大小. (MB)
         */
        @JvmStatic
        val TOTAL_MEMORY : Int by lazy {
            val beanServer = ManagementFactory.getPlatformMBeanServer()
            try {
                val attribute = beanServer.getAttribute(ObjectName("java.lang", "type", "OperatingSystem"), "TotalPhysicalMemorySize")
                (attribute.toString().toLong() / 1024 / 1024).toInt()
            } catch (e: JMException) {
                1024
            }
        }

//        @JvmStatic
//        val SUGGESTED_MEMORY : Int by lazy {
//            (Math.round(1.0 * TOTAL_MEMORY / 4.0 / 128.0) * 128).toInt()
//        }

        /**
         * * Indicates the operating system architecture of the current platform.
         * * 表示当前平台的操作系统架构.
         *
         * @see [Platform]
         */
        @JvmField
        val ARCHITECTURE: String

        /**
         * @see [java.io.File.pathSeparator]
         */
        @JvmField
        val PATH_SEPARATOR = File.pathSeparator

        /**
         * @see [java.io.File.separator]
         */
        @JvmField
        val FILE_SEPARATOR = File.separator

        /**
         * @see [java.lang.System.lineSeparator]
         */
        @JvmField
        val LINE_SEPARATOR = System.lineSeparator()

        /**
         * * Indicates the operating system encoding of the current platform.
         * * 表示当前平台的操作系统编码.
         */
        @JvmField
        val ENCODING = System.getProperty("sun.jnu.encoding", Charset.defaultCharset().name())

        /**
         * * Indicates the operating system version of the current platform.
         * * 表示当前平台的操作系统版本.
         */
        @JvmField
        val VERSION = System.getProperty("os.version")

        init {
            val name = System.getProperty("os.type").toLowerCase(Locale.US)
            CURRENT = when {
                name.contains("win") -> WINDOWS
                name.contains("mac") -> OSX
                name.contains(Regex("solaris|linux|unix|sunos")) -> LINUX
                else -> UNKNOWN
            }
            ARCHITECTURE =
                    System.getProperty("sun.arch.data.model") ?:
                    System.getProperty("os.arch")
        }

//        @JvmStatic
//        fun setClipboard(content: String) {
//            val clipboard = ClipboardContent()
//            clipboard.putString(content)
//            Clipboard.getSystemClipboard().setContent(clipboard)
//        }

        /**
         * * gc -> runFinalization -> gc
         *
         * @see [System.gc]
         * @see [System.runFinalization]
         */
        @JvmStatic
        fun gc() {
            System.gc()
            System.runFinalization()
            System.gc()
        }

        /**
         * * Create a File directory object from the run directory for the given [directory].
         * * 将给定的 [directory] 目录从运行目录创建一个 File 目录对象.
         */
        @JvmStatic
        fun getWorkingDir(directory: String) : File {
            val home = System.getProperty("user.home", ".")
            return when (CURRENT) {
                LINUX -> File(home, ".$directory/")
                WINDOWS -> File(System.getProperty("APPDATA") ?: home, ".$directory/")
                OSX -> File(home, "Library/Application Support/$directory")
                UNKNOWN -> File(home, "$directory/")
            }
        }
    }
}
