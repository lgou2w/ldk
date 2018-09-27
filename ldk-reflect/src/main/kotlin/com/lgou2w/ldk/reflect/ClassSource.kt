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

/**
 * ## ClassSource (类源)
 *
 * @see [fromClassLoader]
 * @see [fromMap]
 * @author lgou2w
 */
abstract class ClassSource {

    /**
     * * Load the class from the full name of the given class.
     * * 从给定类的全名称加载该类.
     *
     * @param name Class full name
     * @param name 类全名称
     * @throws ClassNotFoundException If the class does not exist.
     * @throws ClassNotFoundException 如果类未存在.
     */
    @Throws(ClassNotFoundException::class)
    abstract fun loadClass(name: String): Class<*>

    companion object {

        /**
         * * Create a class source loaded from the current class loader.
         * * 创建一个从当前类加载器中加载的类源.
         */
        @JvmStatic
        fun fromClassLoader(): ClassSource
                = fromClassLoader(ClassSource::class.java.classLoader)

        /**
         * * Create a class source loaded from the given class loader [classLoader].
         * * 创建一个从给定类加载器 [classLoader] 中加载的类源.
         *
         * @param classLoader Class loader
         * @param classLoader 类加载器
         */
        @JvmStatic
        fun fromClassLoader(classLoader: ClassLoader): ClassSource = object: ClassSource() {
            override fun loadClass(name: String): Class<*>
                    = classLoader.loadClass(name)
        }

        /**
         * * Create a class source obtained from the given key-value pair Map.
         * * 创建一个从给定键值对 Map 中获取的类源.
         *
         * @param map key-value pair Map
         * @param map 键值对 Map
         */
        @JvmStatic
        fun fromMap(map: Map<String, Class<*>>): ClassSource = object: ClassSource() {
            override fun loadClass(name: String): Class<*>
                    = map[name] ?: throw ClassNotFoundException("The specified class does not exist in this Map.")
        }
    }
}
