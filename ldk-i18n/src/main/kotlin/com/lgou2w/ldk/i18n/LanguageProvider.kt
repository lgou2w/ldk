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
import java.io.InputStream
import java.io.OutputStream

/**
 * ## LanguageProvider (语言提供者)
 *
 * @see [ExternalProvider]
 * @see [ResourceProvider]
 * @see [ResourceExternalizableProvider]
 * @author lgou2w
 */
interface LanguageProvider {

    /**
     * * Get the language data input stream from the given [name].
     * * 从给定的名称 [name] 获取语言数据输入流.
     *
     * @throws [IOException] I/O
     */
    @Throws(IOException::class)
    fun load(name: String): InputStream?

    /**
     * * Gets whether the language data is valid from the given [name].
     * * 从给定的名称 [name] 获取语言数据是否有效.
     */
    fun isValid(name: String): Boolean

    /**
     * * Get the language data output stream from the given [name].
     * * 从给定的名称 [name] 获取语言数据输出流.
     *
     * @throws [UnsupportedOperationException] If the language provider does not support this operation.
     * @throws [UnsupportedOperationException] 如果语言提供者不支持此操作.
     * @throws [IOException] I/O
     */
    @Throws(IOException::class, UnsupportedOperationException::class)
    fun write(name: String): OutputStream {
        throw UnsupportedOperationException("Current provider does not support saving language data.")
    }
}
