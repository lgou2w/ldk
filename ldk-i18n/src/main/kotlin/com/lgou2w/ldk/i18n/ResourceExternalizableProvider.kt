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

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * ## ResourceExternalizableProvider (资源可外部语言提供者)
 *
 * @see [LanguageProvider]
 * @see [ResourceProvider]
 * @author lgou2w
 */
class ResourceExternalizableProvider @JvmOverloads constructor(
        /**
         * * A directory of resource externalizable language provider.
         * * 此资源可外部语言提供者的目录.
         */
        val directory : File,
        classLoader: ClassLoader = ResourceExternalizableProvider::class.java.classLoader
) : ResourceProvider(classLoader) {

    override fun load(name: String): InputStream? {
        val external = File(directory, name)
        if (!external.exists()) {
            val internal = super.isValid(name)
            if (internal) {
                val input = super.load(name)!!
                val output = FileOutputStream(external)
                val buffer = ByteArray(1024)
                var length : Int
                while (input.read(buffer).apply { length = this } != -1)
                    output.write(buffer, 0, length)
                output.flush()
                output.close()
                input.close()
            } else {
                if (external.parentFile?.exists() != true)
                    external.parentFile?.mkdirs()
                return null
            }
        }
        return FileInputStream(external)
    }

    override fun isValid(name: String): Boolean {
        return File(directory, name).exists()
    }

    override fun write(name: String): OutputStream {
        val file = File(directory, name)
        return FileOutputStream(file)
    }
}
