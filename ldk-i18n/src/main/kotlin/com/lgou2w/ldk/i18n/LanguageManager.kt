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
import java.util.Locale

/**
 * ## LanguageManager (语言管理器)
 *
 * @see [LanguageManagerBase]
 * @see [SimpleLanguageManager]
 * @see [DynamicLanguageManager]
 * @author lgou2w
 */
interface LanguageManager {

    /**
     * * The base name of this language manager.
     * * 此语言管理器的基础名称.
     */
    val baseName : String

    /**
     * * The adapter for this language manager.
     * * 此语言管理器的适配器.
     *
     * @see [LanguageAdapter]
     */
    val adapter : LanguageAdapter

    /**
     * * The provider of this language manager.
     * * 此语言管理器的提供者.
     *
     * @see [LanguageProvider]
     */
    val provider : LanguageProvider

    /**
     * * Global formatter of this language manager.
     * * 此语言管理器的全局格式化.
     *
     * @see [Formatter]
     * @since LDK 0.1.7-rc2
     */
    var globalFormatter : Formatter?

    /**
     * * Load a language object from the given locale [locale].
     * * 从给定的本地化 [locale] 加载语言对象.
     *
     * @throws [IOException] I/O
     */
    @Throws(IOException::class)
    fun load(locale: Locale): Language

    /**
     * * Save the given language object [language].
     * * 将给定的语言对象 [language] 进行保存.
     *
     * @throws [UnsupportedOperationException] This operation is not supported by the language provider [provider].
     * @throws [UnsupportedOperationException] 如果语言提供者 [provider] 不支持此操作.
     * @throws [IOException] I/O
     */
    @Throws(IOException::class, UnsupportedOperationException::class)
    fun save(language: Language)

    /**
     * * Gets valid from the given locale [locale].
     * * 从给定的本地化 [locale] 获取是否有效.
     */
    fun isValid(locale: Locale): Boolean

    /**
     * * Gets valid from the given language object [language].
     * * 从给定的语言对象 [language] 获取是否有效.
     */
    fun isValid(language: Language): Boolean
}
