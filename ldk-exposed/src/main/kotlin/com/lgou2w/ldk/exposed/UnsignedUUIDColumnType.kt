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

package com.lgou2w.ldk.exposed

import org.jetbrains.exposed.sql.ColumnType
import java.util.UUID

/**
 * ## UnsignedUUIDColumnType (无符号 UUID 列类型)
 *
 * @see [ColumnType]
 * @see [UUID]
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
class UnsignedUUIDColumnType @JvmOverloads constructor(
        nullable: Boolean = false
) : ColumnType(nullable) {

    override fun sqlType(): String = "VARCHAR(32)" // unsigned uuid length is 32
    override fun notNullValueToDB(value: Any): Any = uuidToString(valueToUUID(value))
    override fun valueFromDB(value: Any): Any = valueToUUID(value)

    companion object {
        private fun valueToUUID(value: Any): UUID {
            return when (value) {
                is UUID -> value
                is String -> stringToUUID(value)
                else -> error("Unexpected value of type UUID: ${value.javaClass.canonicalName}")
            }
        }
        private fun uuidToString(value: UUID): String
                = value.toString().replace("-", "")
        private fun stringToUUID(value: String): UUID
                = UUID.fromString(
                value.replace(Regex("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})"), "\$1-\$2-\$3-\$4-\$5"))
    }
}
