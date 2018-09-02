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

import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import java.io.File
import java.lang.management.ManagementFactory
import java.nio.charset.Charset
import java.util.*
import javax.management.JMException
import javax.management.ObjectName

enum class OperatingSystem(
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

        @JvmField
        val CURRENT : OperatingSystem

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

        @JvmStatic
        val SUGGESTED_MEMORY : Int by lazy {
            (Math.round(1.0 * TOTAL_MEMORY / 4.0 / 128.0) * 128).toInt()
        }

        @JvmField
        val ARCHITECTURE: String

        @JvmField
        val PATH_SEPARATOR = File.pathSeparator

        @JvmField
        val FILE_SEPARATOR = File.separator

        @JvmField
        val LINE_SEPARATOR = System.lineSeparator()

        @JvmField
        val ENCODING = System.getProperty("sun.jnu.encoding", Charset.defaultCharset().name())

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

        @JvmStatic
        fun setClipboard(content: String) {
            val clipboard = ClipboardContent()
            clipboard.putString(content)
            Clipboard.getSystemClipboard().setContent(clipboard)
        }

        @JvmStatic
        fun gc() {
            System.gc()
            System.runFinalization()
            System.gc()
        }

        @JvmStatic
        fun getWorkingDir(folder: String) : File {
            val home = System.getProperty("user.home", ".")
            return when (CURRENT) {
                LINUX -> File(home, ".$folder/")
                WINDOWS -> File(System.getProperty("APPDATA") ?: home, ".$folder/")
                OSX -> File(home, "Library/Application Support/$folder")
                UNKNOWN -> File(home, "$folder/")
            }
        }
    }
}
