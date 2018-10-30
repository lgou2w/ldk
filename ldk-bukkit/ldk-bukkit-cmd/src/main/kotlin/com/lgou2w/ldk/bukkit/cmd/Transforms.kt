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

package com.lgou2w.ldk.bukkit.cmd

import com.lgou2w.ldk.common.Function
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

open class Transforms : Iterable<Transform<*>> {

    protected val transforms : MutableMap<Class<*>, Transform<*>> = ConcurrentHashMap()

    val keys : MutableSet<Class<*>>
        get() = transforms.keys

    val values : MutableCollection<Transform<*>>
        get() = transforms.values

    val entries : MutableSet<MutableMap.MutableEntry<Class<*>, Transform<*>>>
        get() = transforms.entries

    fun <T> addTransform(type: Class<T>, transform: Transform<T>) {
        transforms[type] = transform
    }

    fun <T> addTransform(type: Class<T>, transform: Function<String, T?>) {
        addTransform(type, object : Transform<T> {
            override fun transform(parameter: String): T? {
                return transform.invoke(parameter)
            }
        })
    }

    fun removeTransform(type: Class<*>) : Transform<*>? {
        return transforms.remove(type)
    }

    fun <T> getTransform(type: Class<T>) : Transform<T>? {
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
