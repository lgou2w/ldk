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

package com.lgou2w.ldk.i18n

import java.io.IOException
import java.util.*

interface Language {

    /**
     * @since 0.1.7-rc2
     */
    val manager : LanguageManager

    val locale : Locale

    var formatter : Formatter?

    val size : Int

    val keys : Set<String>

    val entries : Set<Map.Entry<String, String>>

    /**
     * @since 0.1.7-rc2
     */
    @Throws(IOException::class)
    fun save()

    operator fun get(key: String) : String?

    fun get(key: String, vararg args: Any) : String?

    fun getOr(key: String, def: String?) : String?

    fun getOr(key: String, def: String?, vararg args: Any) : String?

    operator fun set(key: String, value: String)

    fun has(key: String) : Boolean

    fun clear()

    fun addAll(entries: Map<String, String>)
}
