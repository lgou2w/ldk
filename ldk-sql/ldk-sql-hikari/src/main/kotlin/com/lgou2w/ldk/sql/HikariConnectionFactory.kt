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

package com.lgou2w.ldk.sql

import com.lgou2w.ldk.common.notNull
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.SQLException

abstract class HikariConnectionFactory(
        protected val configuration: HikariConfiguration
) : ConnectionFactory {

    private var hikari : HikariDataSource? = null

    override val dataSource : HikariDataSource
        get() = hikari.notNull("Connection factory has not been initialized.")

    /**
     * * implementation override
     */
    protected abstract val driverClass : String

    protected open fun appendProperties(config: HikariConfig, configuration: HikariConfiguration) {
        configuration.properties.forEach {
            config.addDataSourceProperty(it.key, it.value)
        }
    }

    protected open fun appendConfiguration(config: HikariConfig) {
        val address = configuration.address.split(":")
        val host = address[0]
        val port = if (address.size > 1) address[1] else "3306"
        config.dataSourceClassName = driverClass
        config.addDataSourceProperty("serverName", host)
        config.addDataSourceProperty("port", port)
        config.addDataSourceProperty("databaseName", configuration.database)
        config.username = configuration.username
        config.password = configuration.password
    }

    override fun initialize() {
        val config = HikariConfig()
        config.poolName = configuration.poolName
        appendConfiguration(config)
        appendProperties(config, configuration)
        config.maximumPoolSize = configuration.maxPoolSize
        config.minimumIdle = configuration.minIdleConnections
        config.maxLifetime = configuration.maxLifetime
        config.connectionTimeout = configuration.connectionTimeout
        config.initializationFailTimeout = -1L
        hikari = HikariDataSource(config)
    }

    override fun shutdown() {
        if (hikari != null) {
            hikari?.close()
            hikari = null
        }
    }

    fun testSession(): HikariTestSession {
        var success = true
        val start = System.currentTimeMillis()
        try {
            val session = hikari!!.connection
            try {
                val statement = session.createStatement()
                statement.execute("SELECT 1")
            } finally {
            }
        } catch (e: SQLException) {
            success = false
        }
        val end = System.currentTimeMillis() - start
        return HikariTestSession(success, if (success) end else -1L)
    }

    override fun openSession(): Connection {
        return hikari?.connection
               ?: throw SQLException("Unable to get a connection from the pool.")
    }

    override fun toString(): String {
        return "ConnectionFactory(implementation=$implementationName, driver=$driverClass)"
    }
}
