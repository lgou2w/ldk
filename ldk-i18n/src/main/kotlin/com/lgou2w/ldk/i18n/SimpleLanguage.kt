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

import java.util.*
import java.util.concurrent.ConcurrentHashMap

open class SimpleLanguage(
        override val manager: LanguageManager,
        override val locale: Locale,
        maps: Map<String, String>
) : Language {

    protected val languages = ConcurrentHashMap(maps)

    override var formatter: Formatter? = null
    override val size: Int
        get() = languages.size
    override val keys: Set<String>
        get() = languages.keys
    override val entries: Set<Map.Entry<String, String>>
        get() = languages.entries

    override fun save() {
        manager.save(this)
    }

    override fun get(key: String): String?
            = getOr(key, null)
    override fun get(key: String, vararg args: Any?): String?
            = getOr(key, null, *args)
    override fun getOr(key: String, def: String?): String?
            = getOr(key, def, emptyArray<String>())

    override fun getOr(key: String, def: String?, vararg args: Any?): String? {
        val value = languages[key] ?: def ?: return null
        val formatter = this.formatter ?: manager.globalFormatter
        if (formatter != null && args.isNotEmpty())
            return formatter.format(locale, value, *args)
        return value
    }

    override fun set(key: String, value: String) {
        languages[key] = value
    }

    override fun has(key: String): Boolean {
        return languages.containsKey(key)
    }

    override fun clear() {
        languages.clear()
    }

    override fun addAll(entries: Map<String, String>) {
        languages.putAll(entries)
    }

    override fun hashCode(): Int {
        var result = locale.hashCode()
        result = 31 * result + manager.hashCode()
        result = 31 * result + languages.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is SimpleLanguage)
            return manager == other.manager && locale == other.locale && languages == other.languages
        return false
    }

    override fun toString(): String {
        return "Language(locale=${if (locale == Locale.ROOT) "ROOT" else locale.toString()}, size=$size)"
    }
}
