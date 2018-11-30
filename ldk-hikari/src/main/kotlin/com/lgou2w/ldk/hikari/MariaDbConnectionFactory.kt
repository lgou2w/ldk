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

package com.lgou2w.ldk.hikari

import com.zaxxer.hikari.HikariConfig

@Deprecated("Will be removed in 0.1.7-rc. Please replace with ldk-sql-hikari module.", level = DeprecationLevel.WARNING)
class MariaDbConnectionFactory(
        configuration: Configuration
) : HikariConnectionFactory(configuration) {

    override val implementationName: String = "MariaDB"
    override val driverClass: String = "org.mariadb.jdbc.MariaDbDataSource"

    override fun appendProperties(config: HikariConfig, configuration: Configuration) {
        val entries = configuration.properties.entries
        if (entries.isEmpty())
            return
        val properties = entries.asSequence()
            .map { "${it.key}=${it.value}" }
            .joinToString(separator = ";")
        config.addDataSourceProperty("properties", properties)
    }
}
