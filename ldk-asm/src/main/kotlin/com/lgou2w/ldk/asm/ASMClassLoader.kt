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

package com.lgou2w.ldk.asm

/**
 * ## ASMClassLoader (字节码类加载器)
 *
 * @see [ofInstance]
 * @see [ofClassLoader]
 * @author lgou2w
 * @since 0.1.7-rc2
 */
abstract class ASMClassLoader private constructor(
        parent: ClassLoader
) : ClassLoader(parent) {

    abstract fun defineClass(name: String, byteArray: ByteArray): Class<*>

    fun defineClasses(classes: Map<String, ByteArray>): List<Class<*>>
            = classes.map { defineClass(it.key, it.value) }

    companion object {

        @JvmStatic
        private val instance by lazy {
            object : ASMClassLoader(ASMClassLoader::class.java.classLoader) {
                override fun defineClass(name: String, byteArray: ByteArray): Class<*>
                        = defineClass(name, byteArray, 0, byteArray.size, ASMClassLoader::class.java.protectionDomain)
            }
        }

        @JvmStatic
        fun ofInstance(): ASMClassLoader
                = instance

        @JvmStatic
        fun ofClassLoader(classLoader: ClassLoader): ASMClassLoader
                = object : ASMClassLoader(classLoader) {
            override fun defineClass(name: String, byteArray: ByteArray): Class<*>
                    = defineClass(name, byteArray, 0, byteArray.size, classLoader::class.java.protectionDomain)
        }
    }
}
