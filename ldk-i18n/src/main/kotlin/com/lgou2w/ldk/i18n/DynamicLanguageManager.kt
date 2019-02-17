/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
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
import java.util.Collections
import java.util.Locale

open class DynamicLanguageManager(
        baseName: String,
        adapter: LanguageAdapter,
        provider: LanguageProvider
) : LanguageManagerBase(baseName, adapter, provider) {

    private val languages : MutableList<Language> = Collections.synchronizedList(ArrayList())

    override fun load(locale: Locale): Language {
        val loaded = languages.find { it.locale == locale }
        if (loaded != null)
            return loaded
        val language = super.load(locale)
        languages.add(language)
        return language
    }

    @Throws(IOException::class)
    fun loadAll(vararg locales: Locale) {
        for (locale in locales)
            load(locale)
    }

    @Throws(IOException::class)
    fun reload(locale: Locale) {
        val loaded = languages.find { it.locale == locale }
        if (loaded == null) {
            load(locale)
        } else {
            val entries = super.loadEntries(locale)
            loaded.clear()
            loaded.addAll(entries)
        }
    }

    var dynamic : Language? = null
        private set

    /**
     * @throws NullPointerException If not switched.
     */
    val dynamicUnsafe : Language
        get() = dynamic ?: throw NullPointerException("The current dynamic language has not been switched.")

    fun isLoaded(locale: Locale) : Boolean {
        return languages.find { it.locale == locale } != null
    }

    @Throws(IOException::class)
    fun switch(locale: Locale) {
        val loaded = load(locale)
        dynamic = loaded
    }

    fun clearLoaded() {
        languages.clear()
    }
}
