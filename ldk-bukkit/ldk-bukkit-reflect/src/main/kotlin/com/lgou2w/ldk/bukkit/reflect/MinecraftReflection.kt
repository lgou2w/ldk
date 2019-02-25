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

package com.lgou2w.ldk.bukkit.reflect

import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.reflect.ClassSource
import com.lgou2w.ldk.reflect.PackageCached

object MinecraftReflection {

    @JvmStatic val PACKAGE_CRAFTBUKKIT = "org.bukkit.craftbukkit.${MinecraftBukkitVersion.CURRENT.version}"
    @JvmStatic val PACKAGE_MINECRAFT = "net.minecraft.server.${MinecraftBukkitVersion.CURRENT.version}"

    @JvmStatic private val PACKAGE_CACHED_CRAFTBUKKIT : PackageCached
            by lazy { PackageCached(PACKAGE_CRAFTBUKKIT, SOURCE) }
    @JvmStatic private val PACKAGE_CACHED_MINECRAFT : PackageCached
            by lazy { PackageCached(PACKAGE_MINECRAFT, SOURCE) }
    @JvmStatic private val  SOURCE : ClassSource
            by lazy { ClassSource.fromClassLoader() }

    @JvmStatic
    @Throws(ClassNotFoundException::class)
    fun getMinecraftClass(className: String): Class<*>
            = PACKAGE_CACHED_MINECRAFT.getPackageClass(className)

    @JvmStatic
    @Throws(ClassNotFoundException::class)
    fun getMinecraftClass(className: String, vararg aliases: String): Class<*> = try {
        getMinecraftClass(className)
    } catch (e: ClassNotFoundException) {
        var result: Class<*>? = null
        for (alias in aliases) try {
            result = getMinecraftClass(alias)
        } catch (e: ClassNotFoundException) {
        }
        if (result != null) result.also { setMinecraftClass(className, it) }
        else throw ClassNotFoundException("The class for $className and the alias ${aliases.joinToString()} was not found.", e.cause ?: e)
    }

    @JvmStatic
    fun getMinecraftClassOrNull(className: String): Class<*>? = try {
        getMinecraftClass(className)
    } catch (e: ClassNotFoundException) {
        null
    }

    @JvmStatic
    fun getMinecraftClassOrNull(className: String, vararg aliases: String): Class<*>? = try {
        getMinecraftClass(className, *aliases)
    } catch (e: ClassNotFoundException) {
        null
    }

    @JvmStatic
    fun setMinecraftClass(className: String, clazz: Class<*>?): Class<*>? {
        PACKAGE_CACHED_MINECRAFT.setPackageClass(className, clazz)
        return clazz
    }

    @JvmStatic
    @Throws(ClassNotFoundException::class)
    fun getCraftBukkitClass(className: String): Class<*>
            = PACKAGE_CACHED_CRAFTBUKKIT.getPackageClass(className)

    @JvmStatic
    fun getCraftBukkitClassOrNull(className: String): Class<*>? = try {
        getCraftBukkitClass(className)
    } catch (e: ClassNotFoundException) {
        null
    }

    @JvmStatic
    fun setCraftBukkitClass(className: String, clazz: Class<*>?): Class<*>? {
        PACKAGE_CACHED_CRAFTBUKKIT.setPackageClass(className, clazz)
        return clazz
    }

    /**
     * * Determines if the given argument is the expected instance object, otherwise throws an exception.
     * * 判断给定的参数是否为预期的实例对象, 否则抛出异常.
     *
     * @throws IllegalArgumentException If the argument is not an instance of the expected type.
     * @throws IllegalArgumentException 如果参数不是预期类型的实例.
     */
    @Throws(IllegalArgumentException::class)
    fun isExpected(instance: Any?, expected: Class<*>) {
        if (instance != null && !expected.isInstance(instance))
            throw IllegalArgumentException("The value type of the instance does not match the expected. (Expected: $expected)")
    }
}
