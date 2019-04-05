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

import java.io.IOException
import java.util.Collections
import java.util.Locale

/**
 * ## DynamicLanguageManager (动态语言管理器)
 *
 * @see [LanguageManager]
 * @see [LanguageManagerBase]
 * @author lgou2w
 */
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

    /**
     * * Load all languages ​​from the given locale array [locales].
     * * 从给定的本地数组 [locales] 加载所有语言.
     *
     * @throws [IOException] I/O
     */
    @Throws(IOException::class)
    fun loadAll(vararg locales: Locale) {
        for (locale in locales)
            load(locale)
    }

    /**
     * * Reload the given [locale].
     * * 将给定的本地 [locale] 进行重新加载.
     *
     * @throws [IOException] I/O
     */
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

    /**
     * * The dynamic language object of this dynamic language manager.
     * * 此动态语言管理器的动态语言对象.
     *
     * @see [dynamicUnsafe]
     * @see [switch]
     */
    var dynamic : Language? = null
        private set

    /**
     * @see [dynamic]
     * @see [switch]
     * @throws NullPointerException If not switched.
     * @throws [NullPointerException] 如果尚未切换.
     */
    val dynamicUnsafe : Language
        get() = dynamic ?: throw NullPointerException("The current dynamic language has not been switched.")

    /**
     * * Gets whether it has been loaded from the given [locale].
     * * 从给定的本地 [locale] 获取是否已经加载.
     */
    fun isLoaded(locale: Locale): Boolean {
        return languages.find { it.locale == locale } != null
    }

    /**
     * * Switch the current dynamic language [dynamic] to the given [locale].
     * * 将当前动态语言 [dynamic] 切换为给定的本地 [locale].
     *
     * @throws [IOException] I/O
     */
    @Throws(IOException::class)
    fun switch(locale: Locale) {
        val loaded = load(locale)
        dynamic = loaded
    }

    /**
     * * Clear the currently loaded language.
     * * 清除当前已加载的语言.
     */
    fun clearLoaded() {
        languages.clear()
    }
}
