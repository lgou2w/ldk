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

interface LanguageProvider {

    @Throws(IOException::class)
    fun load(name: String): InputStream?

    fun isValid(name: String): Boolean

    @Throws(IOException::class, UnsupportedOperationException::class)
    fun write(name: String): OutputStream {
        throw UnsupportedOperationException("Current provider does not support saving language data.")
    }
}
