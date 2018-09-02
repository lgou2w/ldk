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

import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

class PropertiesAdapter : LanguageAdapter {

    override val fileExtension: String = "properties"

    override fun adaptation(input: InputStream): Map<String, String> {
        val properties = Properties()
        properties.load(InputStreamReader(input, Charsets.UTF_8))
        return properties.entries
            .associate { it.key.toString() to it.value.toString() }
    }
}
