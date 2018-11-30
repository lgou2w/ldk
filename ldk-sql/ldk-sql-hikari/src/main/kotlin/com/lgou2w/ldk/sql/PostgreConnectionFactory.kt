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

package com.lgou2w.ldk.sql

import com.zaxxer.hikari.HikariConfig

class PostgreConnectionFactory(
        configuration: HikariConfiguration
) : HikariConnectionFactory(configuration) {

    override val implementationName: String = "PostgreSQL"
    override val driverClass: String = "org.postgresql.ds.PGSimpleDataSource"

    override fun appendConfiguration(config: HikariConfig) {
        val address = configuration.address.split(":")
        val host = address[0]
        val port = if (address.size > 1) address[1] else "5432"
        config.dataSourceClassName = driverClass
        config.addDataSourceProperty("serverName", host)
        config.addDataSourceProperty("portNumber", port)
        config.addDataSourceProperty("databaseName", configuration.database)
        config.addDataSourceProperty("username", configuration.username)
        config.addDataSourceProperty("password", configuration.password)
    }
}
