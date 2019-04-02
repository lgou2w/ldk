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

package com.lgou2w.ldk.bukkit.cmd

import com.lgou2w.ldk.common.Function
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

/**
 * ## Transforms (转变器)
 *
 * @see [Iterable]
 * @see [Transform]
 * @author lgou2w
 */
open class Transforms : Iterable<Transform<*>> {

    protected val transforms : MutableMap<Class<*>, Transform<*>> = ConcurrentHashMap()

    /**
     * @see [Map.keys]
     */
    val keys : MutableSet<Class<*>>
        get() = transforms.keys

    /**
     * @see [Map.values]
     */
    val values : MutableCollection<Transform<*>>
        get() = transforms.values

    /**
     * @see [Map.entries]
     */
    val entries : MutableSet<MutableMap.MutableEntry<Class<*>, Transform<*>>>
        get() = transforms.entries

    /**
     * * Add a transform of the given type [type].
     * * 添加给定类型 [type] 的转变器.
     */
    fun <T> addTransform(type: Class<T>, transform: Transform<T>) {
        transforms[type] = transform
    }

    /**
     * * Add a transform of the given type [type].
     * * 添加给定类型 [type] 的转变器.
     */
    fun <T> addTransform(type: Class<T>, transform: Function<String, T?>) {
        addTransform(type, object : Transform<T> {
            override fun transform(parameter: String): T? {
                return transform.invoke(parameter)
            }
        })
    }

    /**
     * * Remove the transform of the given type [type].
     * * 移除给定类型 [type] 的转变器.
     */
    fun removeTransform(type: Class<*>): Transform<*>? {
        return transforms.remove(type)
    }

    /**
     * * Get the transform of the given type [type].
     * * 获取给定类型 [type] 的转变器.
     *
     * @throws [IllegalArgumentException] If the expected type does not match.
     * @throws [IllegalArgumentException] 如果预期类型不符合.
     */
    @Throws(IllegalArgumentException::class)
    fun <T> getTransform(type: Class<T>): Transform<T>? {
        val transform = transforms[type] ?: return null
        try {
            @Suppress("UNCHECKED_CAST")
            return transform as Transform<T>
        } catch (e: ClassCastException) {
            throw IllegalArgumentException("The internal type transform and $type do not match.")
        }
    }

    override fun iterator(): Iterator<Transform<*>> {
        return transforms.values.iterator()
    }

    /**
     * * Add the default type transform.
     * * 添加默认的类型转变器.
     *
     * @see [Transform.TRANSFORM_BYTE]
     * @see [Transform.TRANSFORM_SHORT]
     * @see [Transform.TRANSFORM_INT]
     * @see [Transform.TRANSFORM_LONG]
     * @see [Transform.TRANSFORM_FLOAT]
     * @see [Transform.TRANSFORM_DOUBLE]
     * @see [Transform.TRANSFORM_NUMBER]
     * @see [Transform.TRANSFORM_BOOLEAN]
     * @see [Transform.TRANSFORM_STRING]
     * @see [Transform.TRANSFORM_PLAYER]
     * @see [Transform.TRANSFORM_CONSOLE]
     */
    fun addDefaultTransforms() {
        addTransform(Byte::class.java, Transform.TRANSFORM_BYTE)
        addTransform(Short::class.java, Transform.TRANSFORM_SHORT)
        addTransform(Int::class.java, Transform.TRANSFORM_INT)
        addTransform(Long::class.java, Transform.TRANSFORM_LONG)
        addTransform(Float::class.java, Transform.TRANSFORM_FLOAT)
        addTransform(Double::class.java, Transform.TRANSFORM_DOUBLE)
        addTransform(Number::class.java, Transform.TRANSFORM_NUMBER)
        addTransform(Boolean::class.java, Transform.TRANSFORM_BOOLEAN)
        addTransform(String::class.java, Transform.TRANSFORM_STRING)
        addTransform(Player::class.java, Transform.TRANSFORM_PLAYER)
        addTransform(ConsoleCommandSender::class.java, Transform.TRANSFORM_CONSOLE)
    }
}
