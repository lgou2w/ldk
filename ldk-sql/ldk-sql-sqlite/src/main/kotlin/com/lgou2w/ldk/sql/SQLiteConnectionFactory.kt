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

import com.lgou2w.ldk.common.isTrue
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.nio.file.Path
import java.sql.Connection
import java.sql.SQLException
import java.util.Properties
import java.util.concurrent.atomic.AtomicBoolean
import javax.sql.DataSource

/**
 * ## SQLiteConnectionFactory (SQLite 连接工厂)
 *
 * @see [ConnectionFactory]
 * @author lgou2w
 */
class SQLiteConnectionFactory(
  /**
   * * The data file path of this connection factory.
   * * 此连接工厂的数据文件路径.
   */
  val file: Path,
  /**
   * * The properties of this connection factory.
   * * 此连接工厂的属性.
   */
  val properties: Properties = Properties()
) : ConnectionFactory {

  private var connectionWrap : WrappedConnection? = null
  private val initialized = AtomicBoolean(false)

  override val dataSource : DataSource
    get() = throw UnsupportedOperationException("The SQLite connection factory does not support data sources.")

  override val implementationName : String = "SQLite"

  override fun initialize() {
    initializeDriver()
  }

  override fun shutdown() {
    if (connectionWrap != null) {
      connectionWrap?.shutdown()
      connectionWrap = null
    }
  }

  @Synchronized
  override fun openSession(): Connection {
    if (connectionWrap == null || connectionWrap?.isClosed.isTrue()) {
      val connection = createConnection("jdbc:sqlite:$file", properties)
      if (connection != null)
        connectionWrap = WrappedConnection.wrap(connection)
    }
    return connectionWrap
      ?: throw SQLException("Unable to get a connection.")
  }

  private fun initializeDriver() {
    if (!initialized.compareAndSet(false, true))
      return
    try {
      Class.forName("org.sqlite.JDBC")
    } catch (e: Exception) {
      initialized.set(false)
      if (e is ClassNotFoundException)
        throw RuntimeException(e)
      throw e
    }
  }

  @Throws(SQLException::class)
  private fun createConnection(url: String, properties: Properties): Connection? {
    initializeDriver()
    return try {
      org.sqlite.JDBC.createConnection(url, properties)
    } catch (e: NoSuchMethodError) {
      throw RuntimeException(e)
    } catch (e: IllegalAccessException) {
      throw RuntimeException(e)
    } catch (e: InvocationTargetException) {
      if (e.cause is SQLException)
        throw e.cause as SQLException
      throw RuntimeException(e)
    }
  }

  private interface WrappedConnection : Connection {
    fun shutdown()
    companion object {
      @JvmStatic
      fun wrap(connection: Connection): WrappedConnection {
        return Proxy.newProxyInstance(
          WrappedConnection::class.java.classLoader,
          arrayOf(WrappedConnection::class.java),
          Handler(connection)
        ) as WrappedConnection
      }
    }
  }

  private class Handler(val connection: Connection) : InvocationHandler {
    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
      if (method.name == "close")
        return null
      if (method.name == "shutdown") {
        connection.close()
        return null
      }
      return if (args == null) method.invoke(connection)
      else method.invoke(connection, *args)
    }
  }
}
