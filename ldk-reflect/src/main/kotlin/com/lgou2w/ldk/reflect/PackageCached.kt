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

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * ## PackageCached (包缓存器)
 *
 * @author lgou2w
 */
class PackageCached {

    private val cached: MutableMap<String, Optional<Class<*>>>
    private val source: ClassSource
    private val packageName: String

    /**
     * ## CachedPackage
     *
     * @param packageName The target package name
     * @param packageName 目标包名
     * @param source The target class source
     * @param source 目标类源
     */
    constructor(packageName: String, source: ClassSource) {
        this.cached = ConcurrentHashMap()
        this.packageName = packageName
        this.source = source
    }

    /**
     * * Get the class of the current cached package from the given class name.
     * * 从给定的类名获取当前缓存包的类.
     *
     * @param className Class name
     * @param className 类名称
     * @throws ClassNotFoundException If the class does not exist.
     * @throws ClassNotFoundException 如果类不存在.
     */
    @Throws(ClassNotFoundException::class)
    fun getPackageClass(className: String): Class<*> {
        if (cached.containsKey(className)) {
            val result = cached[className]
            if (result == null || !result.isPresent)
                throw ClassNotFoundException("Unable to find class: $className")
            return result.get()
        }
        try {
            val clazz = source.loadClass(combine(packageName, className))
            cached[className] = Optional.of(clazz)
            return clazz
        } catch (e: ClassNotFoundException) {
            cached[className] = Optional.empty()
            throw ClassNotFoundException("Unable to find class: $className", e.cause ?: e)
        }
    }

    /**
     * * Sets the class for this given class name for this cache package. Removed if [clazz] is `null`.
     * * 设置此缓存包的给定类名的类. 如果 [clazz] 为 `null` 则移除.
     *
     * @param className Class name
     * @param className 类名称
     * @param clazz Class
     * @param clazz 类
     */
    fun setPackageClass(className: String, clazz: Class<*>?) {
        if (clazz != null)
            cached[className] = Optional.of(clazz)
        else
            cached.remove(className)
    }

    /**
     * * Clear the internal cache.
     * * 清除内部缓存.
     */
    fun gc() {
        cached.clear()
    }

    private fun combine(packageName: String, className: String): String {
        if (packageName.isEmpty())
            return className
        if (className.isEmpty())
            return packageName
        return "$packageName.$className"
    }
}
