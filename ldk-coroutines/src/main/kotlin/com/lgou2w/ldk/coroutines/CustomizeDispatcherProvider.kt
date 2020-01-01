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

import java.io.Closeable
import kotlin.coroutines.CoroutineContext

/**
 * ## CustomizeDispatcherProvider (自定义调度程序提供者)
 *
 * @see [DispatcherProvider]
 * @author lgou2w
 */
open class CustomizeDispatcherProvider(
  override val dispatcher: CoroutineContext
) : DispatcherProvider {
  override fun close() {
    (dispatcher as? Closeable)?.close()
  }
}
