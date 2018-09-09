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

import java.io.FileNotFoundException
import java.io.InputStream

open class ResourceProvider(
        val classLoader: ClassLoader = ResourceProvider::class.java.classLoader
) : LanguageProvider {

    override fun isValid(name: String): Boolean {
        return classLoader.getResource(name) != null
    }

    override fun load(name: String): InputStream? {
        return classLoader.getResourceAsStream(name)
               ?: throw FileNotFoundException("The resource file $name in the class loader was not found.")
    }
}
