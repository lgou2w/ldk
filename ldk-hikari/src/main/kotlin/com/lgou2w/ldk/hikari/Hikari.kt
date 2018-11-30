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

import java.util.*

@Deprecated("Will be removed in 0.1.7-rc. Please replace with ldk-sql-hikari module.", level = DeprecationLevel.WARNING)
fun buildConfiguration(block: ConfigurationBuilder.() -> Unit) : Configuration {
    val builder = ConfigurationBuilder().also(block)
    return Configuration(
            builder.poolName,
            builder.address!!,
            builder.database!!,
            builder.username!!,
            builder.password!!,
            builder.maxPoolSize,
            builder.minIdleConnections,
            builder.maxLifetime,
            builder.connectionTimeout,
            Collections.unmodifiableMap(builder.properties)
    )
}
