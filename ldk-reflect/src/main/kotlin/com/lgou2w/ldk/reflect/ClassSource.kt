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

package com.lgou2w.ldk.reflect

abstract class ClassSource {

    @Throws(ClassNotFoundException::class)
    abstract fun loadClass(name: String): Class<*>

    companion object {

        @JvmStatic
        fun fromClassLoader(): ClassSource
                = fromClassLoader(ClassSource::class.java.classLoader)

        @JvmStatic
        fun fromClassLoader(classLoader: ClassLoader): ClassSource = object: ClassSource() {
            override fun loadClass(name: String): Class<*>
                    = classLoader.loadClass(name)
        }

        @JvmStatic
        fun fromMap(map: Map<String, Class<*>>): ClassSource = object: ClassSource() {
            override fun loadClass(name: String): Class<*>
                    = map[name] ?: throw ClassNotFoundException("The specified class does not exist in this Map.")
        }
    }
}
