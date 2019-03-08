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

package com.lgou2w.ldk.asm

/**
 * ## ASMClassLoader (字节码类加载器)
 *
 * @see [ofInstance]
 * @see [ofClassLoader]
 * @author lgou2w
 * @since LDK 0.1.7-rc2
 */
abstract class ASMClassLoader private constructor(
        parent: ClassLoader
) : ClassLoader(parent) {

    /**
     * * Define the given class name [name] and bytecode [byteArray] as class.
     * * 将给定的类名 [name] 和字节码 [byteArray] 定义为类.
     */
    abstract fun defineClass(name: String, byteArray: ByteArray): Class<*>

    /**
     * * Defines the class name and bytecode key-value mapping [classes] as a class collection.
     * * 将给定的类名和字节码的键值对映射 [classes] 定义为类集合.
     *
     * @see [ASMClassGenerator]
     * @see [ASMClassGenerator.generate]
     */
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

        /**
         * * Get the singleton object of the current bytecode class loader.
         * * 获取当前字节码类加载器的单例对象.
         */
        @JvmStatic
        fun ofInstance(): ASMClassLoader
                = instance

        /**
         * * Create a bytecode class loader object from the given class loader [classLoader].
         * * 从给定的类加载器 [classLoader] 创建一个字节码类加载器对象.
         */
        @JvmStatic
        fun ofClassLoader(classLoader: ClassLoader): ASMClassLoader
                = object : ASMClassLoader(classLoader) {
            override fun defineClass(name: String, byteArray: ByteArray): Class<*>
                    = defineClass(name, byteArray, 0, byteArray.size, classLoader::class.java.protectionDomain)
        }
    }
}
