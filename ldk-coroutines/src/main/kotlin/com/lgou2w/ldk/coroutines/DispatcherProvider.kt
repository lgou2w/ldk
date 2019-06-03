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

package com.lgou2w.ldk.coroutines

import java.io.Closeable
import kotlin.coroutines.CoroutineContext

/**
 * ## DispatcherProvider (调度程序提供者)
 *
 * @see [CustomizeDispatcherProvider]
 * @see [SingleThreadDispatcherProvider]
 * @see [FixedThreadPoolDispatcherProvider]
 * @see [ScheduledThreadPoolDispatcherProvider]
 * @see [CachedThreadPoolDispatcherProvider]
 * @see [Closeable]
 * @author lgou2w
 */
interface DispatcherProvider : Closeable {

    /**
     * * The coroutine context object for this dispatcher provider.
     * * 此调度程序提供者的协程上下文对象.
     */
    val dispatcher : CoroutineContext
}
