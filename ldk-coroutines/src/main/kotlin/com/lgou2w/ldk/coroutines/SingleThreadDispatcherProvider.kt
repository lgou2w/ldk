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

package com.lgou2w.ldk.coroutines

import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

/**
 * ## SingleThreadDispatcherProvider (单线程调度程序提供者)
 *
 * @see [DispatcherProvider]
 * @see [Executors.newSingleThreadExecutor]
 * @author lgou2w
 */
class SingleThreadDispatcherProvider(
  private val threadName: String
) : DispatcherProvider {

  private val createPoolThread : (Runnable) -> Thread = { r ->
    Thread(r, threadName)
  }

  override val dispatcher : ExecutorCoroutineDispatcher
    = Executors.newSingleThreadExecutor(createPoolThread).asCoroutineDispatcher()

  override fun close() {
    dispatcher.close()
  }
}
