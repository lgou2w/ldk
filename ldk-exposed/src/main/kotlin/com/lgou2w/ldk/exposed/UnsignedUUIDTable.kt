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

package com.lgou2w.ldk.exposed

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

/**
 * ## UnsignedUUIDTable (无符号 UUID 表)
 *
 * @see [IdTable]
 * @see [UnsignedUUIDColumnType]
 * @see [unsignedUUID]
 * @see [UUID]
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
open class UnsignedUUIDTable @JvmOverloads constructor(
        name: String = "",
        columnName: String = ""
) : IdTable<UUID>(name) {
    override val id: Column<EntityID<UUID>> = unsignedUUID(columnName)
        .primaryKey()
        .clientDefault { UUID.randomUUID() }
        .entityId()
}
