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

package com.lgou2w.ldk.sql

import com.zaxxer.hikari.HikariConfig

/**
 * ## MySQLConnectionFactory (MySQL 连接工厂)
 *
 * @see [ConnectionFactory]
 * @see [HikariConnectionFactory]
 * @author lgou2w
 */
class MySQLConnectionFactory(
  configuration: HikariConfiguration
) : HikariConnectionFactory(configuration) {

  override val implementationName : String = "MySQL"
  override val driverClass : String = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource"

  override fun appendProperties(config: HikariConfig, configuration: HikariConfiguration) {
    config.addDataSourceProperty("cachePrepStmts", "true")
    config.addDataSourceProperty("alwaysSendSetIsolation", "false")
    config.addDataSourceProperty("cacheServerConfiguration", "true")
    config.addDataSourceProperty("elideSetAutoCommits", "true")
    config.addDataSourceProperty("useLocalSessionState", "true")
    config.addDataSourceProperty("useServerPrepStmts", "true")
    config.addDataSourceProperty("prepStmtCacheSize", "250")
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
    config.addDataSourceProperty("cacheCallableStmts", "true")
    super.appendProperties(config, configuration)
  }
}
