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

import com.lgou2w.ldk.common.notNull

class HikariConfigurationBuilder {

    var poolName : String? = null

    var address : String? = null
        get() = field.notNull("Connection address cannot be null.")

    var database : String? = null
        get() = field.notNull("Connection database cannot be null.")

    var username : String? = null
        get() = field.notNull("Connection username cannot be null.")

    var password : String? = null
        get() = field.notNull("Connection password cannot be null.")

    var maxPoolSize : Int = 10
    var minIdleConnections : Int = 10
    var maxLifetime : Long = 1800000L // def => 30 minute
    var connectionTimeout : Long = 5000L // def => 5 second

    internal val properties : MutableMap<String, String> = LinkedHashMap()
    var property : Pair<String, Any>?
        get() = null
        set(value) {
            if (value == null)
                throw IllegalArgumentException("The property pair cannot be null.")
            properties[value.first] = value.second.toString()
        }
}
