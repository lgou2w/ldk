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

/**
 * ## HikariTestSession (HikariCP 测试会话)
 *
 * @author lgou2w
 */
data class HikariTestSession(
        /**
         * * Indicates if this test session is connected.
         * * 表示此测试会话是否已连接.
         */
        val isConnected: Boolean,
        /**
         * * Indicates the connection delay for this test session.
         * * 表示此测试会话的连接延迟.
         */
        val delay: Long
)
