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

abstract class LanguageManagerBase(
        override val baseName: String,
        override val adapter: LanguageAdapter,
        override val provider: LanguageProvider
) : LanguageManager {

    protected open fun checkName(locale: Locale) : String {
        val fileExtension = adapter.fileExtension
        val name = if (locale != Locale.ROOT) "_$locale" else ""
        return "$baseName$name.$fileExtension"
    }

    override fun load(locale: Locale): Language {
        val name = checkName(locale)
        try {
            val input = provider.load(name)
            val entries = adapter.adaptation(input)
            return LanguageImpl(locale, entries)
        } catch (e: Exception) {
            throw IOException("从提供者加载语言文件时异常:", e.cause ?: e)
        }
    }

    private class LanguageImpl(
            override val locale: Locale,
            internal val maps: Map<String, String>
    ) : Language {

        override var formatter: Formatter? = null
        override val size: Int
            get() = maps.size
        override val keys: Set<String>
            get() = Collections.unmodifiableSet(maps.keys)
        override val entries: Set<Map.Entry<String, String>>
            get() = Collections.unmodifiableSet(maps.entries)

        override fun get(key: String): String?
                = getOr(key, null)
        override fun get(key: String, vararg args: Any): String?
                = getOr(key, null, *args)
        override fun getOr(key: String, def: String?): String?
                = getOr(key, def, emptyArray<String>())

        override fun getOr(key: String, def: String?, vararg args: Any): String? {
            val value = maps[key] ?: def ?: return null
            val formatter = this.formatter
            if (formatter != null && args.isNotEmpty())
                return formatter.format(locale, value, *args)
            return value
        }

        override fun toString(): String {
            return "Language(locale=${if (locale == Locale.ROOT) "ROOT" else locale.toString()}, size=$size)"
        }
    }
}
