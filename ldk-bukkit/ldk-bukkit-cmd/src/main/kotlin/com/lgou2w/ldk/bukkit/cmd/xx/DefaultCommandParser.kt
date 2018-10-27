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

package com.lgou2w.ldk.bukkit.cmd.xx

import com.lgou2w.ldk.reflect.Accessors
import com.lgou2w.ldk.reflect.FuzzyReflect
import org.bukkit.command.CommandSender
import java.lang.reflect.Method
import java.lang.reflect.Modifier

class DefaultCommandParser : CommandParser {

    override fun parse(manager: CommandManager, source: Any): DefaultRegisteredCommand {
        val clazz = source.javaClass
        val commandRoot = parseRoot(manager, clazz)
        val permission = clazz.getAnnotation(Permission::class.java)
        val executors = parseExecutors(manager, source, clazz)
        val command = buildCommandRegistered(
                manager,
                source,
                null,
                commandRoot.value,
                commandRoot.aliases,
                permission?.values,
                commandRoot.prefix,
                emptyMap(),
                executors
        )
        registerChildren(manager, command, source, clazz)
        return command
    }

    @Throws(CommandParseException::class)
    private fun parseRoot(manager: CommandManager, clazz: Class<*>) : CommandRoot {
        if (Modifier.isAbstract(clazz.modifiers) || Modifier.isInterface(clazz.modifiers) || clazz.isEnum || clazz.isAnnotation)
            throw CommandParseException("The command $clazz cannot be an abstract, interface, enum, or annotation class.")
        return clazz.getAnnotation(CommandRoot::class.java)
               ?: throw CommandParseException("The command $clazz must be annotated by CommandRoot.")
    }

    private fun parseExecutors(manager: CommandManager, source: Any, clazz: Class<*>) : Map<String, DefaultCommandExecutor> {
        return FuzzyReflect.of(clazz, true)
            .useMethodMatcher()
            .withAnnotation(Command::class.java)
            .results()
            .asReversed()
            .filter { method ->
                val command = method.getAnnotation(Command::class.java)
                val parameters = method.parameters
                val firstMatched = parameters.isNotEmpty() &&
                                   CommandSender::class.java.isAssignableFrom(parameters.first().parameterizedType as Class<*>)
                if (!firstMatched)
                    manager.plugin.logger.warning("The sub command '${command.value}' function first parameter is not CommandSender type, filtered.")
                firstMatched
            }
            .associate { method ->
                val command = method.getAnnotation(Command::class.java)
                val permission = method.getAnnotation(Permission::class.java)
                val isPlayable = method.getAnnotation(Playable::class.java) != null
                val parameters = parseExecutorParameters(manager, method)
                command.value to buildCommandExecutor(
                        source,
                        command.value,
                        command.aliases,
                        permission?.values,
                        isPlayable,
                        method,
                        parameters
                )
            }
    }

    private fun parseExecutorParameters(manager: CommandManager, method: Method) : Array<out DefaultCommandExecutor.Parameter> {
        return method.parameters.filterIndexed { index, _ -> index != 0 }.mapNotNull { parameter ->
            val type = parameter.parameterizedType as Class<*>
            val optional = parameter.getAnnotation(Optional::class.java)
            val nullable = parameter.getAnnotation(Nullable::class.java)
            if (optional != null && nullable != null) {
                manager.plugin.logger.warning("Optional or nullable annotations can only have one, skipped.")
                null
            } else {
                DefaultCommandExecutor.Parameter(type, optional?.def, nullable != null)
            }
        }.toTypedArray()
    }

    private fun registerChildren(
            manager: CommandManager,
            parent: DefaultRegisteredCommand?,
            source: Any,
            clazz: Class<*>
    ) {
        clazz.declaredClasses.forEach { child ->
            val (root, instance) = try {
                val root = parseRoot(manager, child)
                val instance = child.newInstance()
                root to instance
            } catch (e: Exception) {
                if (e !is CommandParseException)
                    manager.plugin.logger.warning("Unable to access constructor of the $child, confirm the declaration and 'public' modifier. skipped")
                null to null
            }
            if (root != null && instance != null) {
                val childPermission = clazz.getAnnotation(Permission::class.java)
                val childExecutors = parseExecutors(manager, instance, child)
                val childCommand = buildCommandRegistered(
                        manager,
                        instance,
                        parent,
                        root.value,
                        root.aliases,
                        childPermission?.values,
                        root.prefix,
                        emptyMap(),
                        childExecutors
                )
                parent?.registerChild(childCommand, true)
                registerChildren(manager, childCommand, instance, child)
            }
        }
    }

    private fun buildCommandRegistered(
            manager: CommandManager,
            source: Any,
            parent: DefaultRegisteredCommand?,
            name: String,
            aliases: Array<out String>,
            permission: Array<out String>?,
            prefix: String,
            children: Map<String, DefaultRegisteredCommand>,
            executors: Map<String, DefaultCommandExecutor>
    ) : DefaultRegisteredCommand {
        return DefaultRegisteredCommand(
                manager,
                source,
                parent,
                name,
                aliases,
                permission,
                prefix,
                children,
                executors
        )
    }

    private fun buildCommandExecutor(
            source: Any,
            name: String,
            aliases: Array<out String>,
            permission: Array<out String>?,
            isPlayable: Boolean,
            method: Method,
            parameters: Array<out DefaultCommandExecutor.Parameter>
    ) : DefaultCommandExecutor {
        return DefaultCommandExecutor(
                source,
                name,
                aliases,
                permission,
                isPlayable,
                Accessors.ofMethod(method),
                parameters
        )
    }
}
