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

import com.lgou2w.ldk.common.notNull

/**
 * ## SimpleCoroutineFactory (简单协程工厂)
 *
 * @see [CoroutineFactory]
 * @see [CoroutineFactoryBase]
 * @author lgou2w
 */
class SimpleCoroutineFactory : CoroutineFactoryBase {

  constructor(provider: DispatcherProvider) : super(provider)

  /**
   * @since LDK 0.2.0
   */
  constructor(name: String, provider: DispatcherProvider) : super(name, provider)

  companion object {

    /**
     * @since LDK 0.2.0
     */
    @JvmStatic fun builder() = Builder()

    /**
     * @since LDK 0.2.0
     */
    class Builder internal constructor() {

      private var name : String? = null
      private var provider : DispatcherProvider? = null

      fun withName(name: String?): Builder {
        this.name = name
        return this
      }

      fun withProvider(provider: DispatcherProvider): Builder {
        this.provider = provider
        return this
      }

      fun build(): SimpleCoroutineFactory {
        val name = this.name
        val provider = this.provider.notNull("Dispatcher provider cannot be null.")
        return if (name == null) SimpleCoroutineFactory(provider)
        else SimpleCoroutineFactory(name, provider)
      }
    }
  }
}
