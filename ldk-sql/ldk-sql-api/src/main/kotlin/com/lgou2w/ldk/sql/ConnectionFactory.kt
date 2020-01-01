/*
 * Copyright (C) 2016-2020 The lgou2w <lgou2w@hotmail.com>
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

import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

/**
 * ## ConnectionFactory (连接工厂)
 *
 * @author lgou2w
 */
interface ConnectionFactory {

  /**
   * * The data source object for this connection factory.
   * * 此连接工厂的数据源对象.
   *
   * @see [DataSource]
   */
  val dataSource : DataSource

  /**
   * * The implementation name of this connection factory.
   * * 此连接工厂的实现名称.
   */
  val implementationName : String

  /**
   * * Initialize this connection factory.
   * * 初始化此连接工厂.
   */
  fun initialize()

  /**
   * * Shutdown this connection factory.
   * * 关闭此连接工厂.
   */
  fun shutdown()

  /**
   * * From this connection factory opens a new connection session.
   * * 从此连接工厂开启一个新的连接会话.
   *
   * @throws [SQLException] SQL
   */
  @Throws(SQLException::class)
  fun openSession(): Connection
}
