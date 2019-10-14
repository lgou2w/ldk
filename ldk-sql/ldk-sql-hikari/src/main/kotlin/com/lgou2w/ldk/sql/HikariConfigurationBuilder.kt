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

import com.lgou2w.ldk.common.notNull

/**
 * ## HikariConfigurationBuilder (HikariCP 配置构建者)
 *
 * @see [HikariConfiguration]
 * @see [buildConfiguration]
 * @author lgou2w
 */
class HikariConfigurationBuilder {

  /**
   * * The connection pool name for this configuration.
   * * 此配置的连接池名称.
   */
  var poolName : String? = null

  /**
   * * The connection address for this configuration.
   * * 此配置的连接地址.
   */
  var address : String? = null
    get() = field.notNull("Connection address cannot be null.")

  /**
   * * The connection database for this configuration.
   * * 此配置的连接数据库.
   */
  var database : String? = null
    get() = field.notNull("Connection database cannot be null.")

  /**
   * * The connection username for this configuration.
   * * 此配置的连接用户名.
   */
  var username : String? = null
    get() = field.notNull("Connection username cannot be null.")

  /**
   * * The connection password for this configuration.
   * * 此配置的连接密码.
   */
  var password : String? = null
    get() = field.notNull("Connection password cannot be null.")

  /**
   * * The maximum size of the connection pool for this configuration. (Default: 10)
   * * 此配置的连接池最大大小. (默认: 10)
   */
  var maxPoolSize : Int = 10

  /**
   * * The minimum size of idle connections for this configuration connection pool. (Default: 10)
   * * 此配置的连接池最小空闲连接数. (默认: 10)
   */
  var minIdleConnections : Int = 10

  /**
   * * The maximum lifetime of the connection for this configuration. (Millisecond, default: 30 minutes)
   * * 此配置的连接最大存活时间. (毫秒, 默认: 30 分钟)
   */
  var maxLifetime : Long = 1800000L // def => 30 minute

  /**
   * * Connection timeout for this configuration. (Millisecond, default: 5 seconds)
   * * 此配置的连接超时时间. (毫秒, 默认: 5 秒)
   */
  var connectionTimeout : Long = 5000L // def => 5 second

  internal val properties : MutableMap<String, String> = LinkedHashMap()

  /**
   * * Connection custom properties for this configuration. e.g.: `property = "useSSL" to true`
   * * 此配置的连接自定义属性. 例如: `property = "useSSL" to true`
   */
  var property : Pair<String, Any>?
    get() = null
    set(value) {
      if (value == null)
        throw IllegalArgumentException("The property pair cannot be null.")
      properties[value.first] = value.second.toString()
    }
}
