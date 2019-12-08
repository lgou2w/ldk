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

package com.lgou2w.ldk.i18n

import com.lgou2w.ldk.common.notNull

/**
 * ## SimpleLanguageManager (简单语言管理器)
 *
 * @see [LanguageManager]
 * @see [LanguageManagerBase]
 * @author lgou2w
 */
open class SimpleLanguageManager(
  baseName: String,
  adapter: LanguageAdapter,
  provider: LanguageProvider
) : LanguageManagerBase(baseName, adapter, provider) {

  companion object {

    /**
     * @since LDK 0.2.0
     */
    @JvmStatic fun builder() = Builder()

    /**
     * @since LDK 0.2.0
     */
    class Builder internal constructor() {

      private var baseName : String? = null
      private var adapter : LanguageAdapter? = null
      private var provider : LanguageProvider? = null

      fun withBaseName(baseName: String): Builder {
        this.baseName = baseName
        return this
      }

      fun withAdapter(adapter: LanguageAdapter): Builder {
        this.adapter = adapter
        return this
      }

      fun withProvider(provider: LanguageProvider): Builder {
        this.provider = provider
        return this
      }

      fun build(): SimpleLanguageManager {
        val baseName = this.baseName.notNull("The base name cannot be null.")
        val adapter = this.adapter.notNull("The adapter cannot be null.")
        val provider = this.provider.notNull("The provider cannot be null.")
        return SimpleLanguageManager(baseName, adapter, provider)
      }
    }
  }
}
