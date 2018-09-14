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
import com.lgou2w.ldk.reflect.DataType
import org.bukkit.plugin.Plugin
import java.util.*

abstract class CommandManagerBase : CommandManager {

    private val transforms : MutableMap<Class<*>, TypeTransform<*>> = HashMap()
    private val commands : MutableMap<String, RegisteredCommand> = HashMap()

    override fun registerCommand(plugin: Plugin, source: Class<*>, vararg args: Any): Boolean {
        val instance = newSourceInstance(source, *args)
        return registerCommand(plugin, instance)
    }

    override fun registerCommand(plugin: Plugin, source: Any): Boolean {
        val parsed = parseCommand(plugin, this, source)
        synchronized (commands) {
            commands[parsed.name] = parsed
            registered(parsed)
            return true
        }
    }

    override fun getCommand(command: String): RegisteredCommand? {
        synchronized (commands) {
            return commands[command]
        }
    }

    override fun addDefaultTypeTransforms() {
        TypeTransforms.addDefaultTypeTransform(this)
    }

    override fun <T> addTypeTransform(type: Class<T>, transform: Function<String, T?>) {
        return addTypeTransform(type, object: TypeTransform<T> {
            override fun transform(parameter: String): T? {
                return transform.invoke(parameter)
            }
        })
    }

    override fun <T> addTypeTransform(type: Class<T>, transform: TypeTransform<T>) {
        synchronized (transforms) {
            transforms[type] = transform
        }
    }

    override fun <T> getTypeTransform(type: Class<T>): TypeTransform<T>? {
        synchronized (transforms) {
            val transform = transforms[type]
            try {
                @Suppress("UNCHECKED_CAST")
                return transform as TypeTransform<T>
            } catch (e: ClassCastException) {
                throw IllegalArgumentException("The internal type transform and $type do not match.")
            }
        }
    }

    override fun hasTypeTransform(type: Class<*>): Boolean {
        return getTypeTransform(type) != null
    }

    protected open fun registered(registered: RegisteredCommand) {
        if (registered.source is Initializable)
            (registered.source as Initializable).initialize(this, registered)
        TODO() // Bukkit Registered
    }

    protected open fun parseCommand(
            plugin: Plugin,
            manager: CommandManager,
            source: Any
    ) : RegisteredCommand {
        TODO() // Parse Command
    }

    companion object {

        @JvmStatic
        private fun newSourceInstance(source: Class<*>, vararg args: Any) : Any = try {
            val primitives = DataType.ofPrimitive(args.map { it.javaClass }.toTypedArray())
            val constructor = source.getConstructor(*primitives)
            constructor.newInstance(*args)
        } catch (e: Exception) {
            throw IllegalArgumentException("Unable access $source constructor, Modifier 'public' and declaration?", e.cause ?: e)
        }
    }
}
