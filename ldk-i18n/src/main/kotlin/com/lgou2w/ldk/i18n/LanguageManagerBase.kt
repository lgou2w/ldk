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
import java.io.InputStream
import java.io.OutputStream
import java.util.*

abstract class LanguageManagerBase(
        override val baseName: String,
        override val adapter: LanguageAdapter,
        override val provider: LanguageProvider
) : LanguageManager {

    override var globalFormatter: Formatter? = null

    protected open fun checkName(locale: Locale) : String {
        val fileExtension = adapter.fileExtension.toLowerCase(Locale.US)
        val name = if (locale != Locale.ROOT) "_$locale" else ""
        return "$baseName$name.$fileExtension"
    }

    protected fun loadEntries(locale: Locale) : Map<String, String> {
        val name = checkName(locale)
        var input : InputStream? = null
        try {
            input = provider.load(name)
            return if (input != null) adapter.adapt(input) else LinkedHashMap()
        } catch (e: Exception) {
            throw IOException("Exception when loading a language file from a provider:", e.cause ?: e)
        } finally {
            if (input != null) try {
                input.close()
            } catch (e: Exception) {
            }
        }
    }

    override fun load(locale: Locale): Language {
        val entries = loadEntries(locale)
        return SimpleLanguage(this, locale, entries)
    }

    final override fun save(language: Language) {
        val name = checkName(language.locale)
        var output : OutputStream? = null
        try {
            output = provider.write(name)
            val values = language.entries.associate { it.key to it.value }.toMutableMap()
            adapter.readapt(output, values)
        } catch (e: Exception) {
            throw IOException("Exception when saving a language file from the supplied adapter:", e.cause ?: e)
        } finally {
            if (output != null) try {
                output.close()
            } catch (e: Exception) {
            }
        }
    }

    override fun isValid(locale: Locale): Boolean {
        val name = checkName(locale)
        return provider.isValid(name)
    }

    override fun isValid(language: Language): Boolean {
        return isValid(language.locale)
    }
}
