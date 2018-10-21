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

import java.io.PrintWriter
import java.io.StringWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.ConsoleHandler
import java.util.logging.Formatter
import java.util.logging.LogRecord
import java.util.logging.Logger

/**
 * ## SimpleLogger (简单记录器)
 *
 * * The following format: `[HH:mm:dd] [Thread/Level]: Message`
 *
 * @author lgou2w
 */
@Deprecated("USELESS") // TODO 0.1.6-rc
class SimpleLogger(
        name: String,
        resourceBundleName: String? = null
) : Logger(name, resourceBundleName) {

    @JvmOverloads
    constructor(
            clazz: Class<*>,
            resourceBundleName: String? = null
    ) : this(clazz.simpleName, resourceBundleName)

    init {
        addHandler(ConsoleHandler().apply {
            formatter = object: Formatter() {
                val dateFormat: DateFormat = SimpleDateFormat("HH:mm:dd")
                override fun format(record: LogRecord?): String {
                    if (record == null)
                        return ""
                    val message = formatMessage(record)
                    val time = dateFormat.format(Date(record.millis))
                    val writer = StringWriter()
                    val formatted = "[$time] [${Thread.currentThread().name}/${record.level}]: "
                    val printWriter = PrintWriter(writer)
                    printWriter.write(formatted)
                    printWriter.write(message)
                    printWriter.println()
                    if (record.thrown != null) {
                        val throwable = record.thrown
                        printWriter.write(formatted)
                        printWriter.write(throwable.toString())
                        printWriter.println()
                        throwable.stackTrace.forEach {
                            printWriter.write(formatted)
                            printWriter.write("\tat ")
                            printWriter.write(it.toString())
                            printWriter.println()
                        }
                    }
                    writer.close()
                    return writer.toString()
                }
            }
        })
    }
}
