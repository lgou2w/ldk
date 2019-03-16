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

package com.lgou2w.ldk.bukkit.cmd

import com.lgou2w.ldk.reflect.Accessors
import com.lgou2w.ldk.reflect.FuzzyReflect
import org.bukkit.command.CommandSender
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.util.logging.Level

/**
 * ## DefaultCommandParser (默认命令解析器)
 *
 * @see [CommandParser]
 * @author lgou2w
 */
class DefaultCommandParser : CommandParser {

    override fun parse(manager: CommandManager, source: Any): DefaultRegisteredCommand {
        return parse(manager, null, source)
    }

    override fun parse(manager: CommandManager, parent: RegisteredCommand?, source: Any): DefaultRegisteredCommand {
        val clazz = source.javaClass
        val commandRoot = parseRoot(manager, clazz)
        val permission = clazz.getAnnotation(Permission::class.java)
        val description = clazz.getAnnotation(Description::class.java)
        val executors = parseExecutors(manager, commandRoot.value, source, clazz)
        val command = buildCommandRegistered(
                manager,
                source,
                parent as? DefaultRegisteredCommand,
                commandRoot.value,
                commandRoot.aliases,
                permission?.values,
                description,
                emptyMap(),
                executors
        )
        registerChildren(manager, command, source, clazz)
        return command
    }

    @Throws(CommandParseException::class)
    private fun parseRoot(
            @Suppress("UNUSED_PARAMETER")
            manager: CommandManager,
            clazz: Class<*>
    ): CommandRoot {
        if (Modifier.isAbstract(clazz.modifiers) || Modifier.isInterface(clazz.modifiers) || clazz.isEnum || clazz.isAnnotation)
            throw CommandParseException("The command $clazz cannot be an abstract, interface, enum, or annotation class.")
        val root = clazz.getAnnotation(CommandRoot::class.java)
               ?: throw CommandParseException("The command $clazz must be annotated by CommandRoot.")
        if (root.value.isBlank())
            throw CommandParseException("The command $clazz name is blank.")
        return root
    }

    private fun parseExecutors(
            manager: CommandManager,
            name: String,
            source: Any,
            clazz: Class<*>
    ): Map<String, DefaultCommandExecutor> {
        return FuzzyReflect.of(clazz, true)
            .useMethodMatcher()
            .withAnnotation(Command::class.java)
            .results()
            .asReversed()
            .filter { method ->
                val command = method.getAnnotation(Command::class.java)
                val parameters = method.parameters
                isValidExecutor(manager, name, clazz, command, method, parameters)
            }
            .mapNotNull { method ->
                val command = method.getAnnotation(Command::class.java)
                val permission = method.getAnnotation(Permission::class.java)
                val isPlayable = method.getAnnotation(Playable::class.java) != null
                val parameters = parseExecutorParameters(manager, method)
                val description = method.getAnnotation(CommandDescription::class.java)?.value
                if (parameters == null) null else command.value to buildCommandExecutor(
                        source,
                        command.value,
                        command.aliases,
                        permission?.values,
                        isPlayable,
                        method,
                        parameters,
                        description
                )
            }
            .associate { it }
    }

    private fun isValidExecutor(
            manager: CommandManager,
            name: String,
            clazz: Class<*>,
            command: Command,
            method: Method,
            parameters: Array<out java.lang.reflect.Parameter>
    ): Boolean {
        val alias = "${method.declaringClass.canonicalName}#${method.name}"
        // Command name cannot be blank
        if (command.value.isBlank()) {
            betterError(manager, """
                Command '${clazz.simpleName}#${method.name}' executor name is blank. filtered.
                  Correction: ?
                    @Command("${method.name}")
            """.trimIndent())
            return false
        }
        // Command executor parameters cannot be empty and the first must be of type CommandSender
        // fun sample (sender: CommandSender, ...)
        if (parameters.isNotEmpty() &&
            !CommandSender::class.java.isAssignableFrom(parameters.first().parameterizedType as Class<*>)) {
            betterError(manager, """
                Command '${command.value}' executor first parameter is not CommandSender type. filtered.
                  Correction: ?
                    fun $alias(sender: CommandSender, ...) {...}
            """.trimIndent())
            return false
        }
        // The parameter mapping can only be a CommandSender type.
        if (command.value == name && parameters.size > 1) {
            betterError(manager, """
                Command '${command.value}' executor and command mapping, parameters must only be one CommandSender. filtered.
                  Correction: ?
                    fun $alias(sender: CommandSender) {...}
            """.trimIndent())
            return false
        }
        // Variable length parameters cannot have multiple
        if (parameters.count { parameter -> parameter.getAnnotation(Vararg::class.java) != null } > 1) {
            betterError(manager, """
                Command '${command.value}' executor variable length parameters cannot have multiple. filtered.
                  Correction: ?
                    fun $alias(sender: CommandSender, ..., @Vararg parameter: List) {...}
            """.trimIndent())
            return false
        }
        return true
    }

    private fun parseExecutorParameters(
            manager: CommandManager,
            method: Method
    ): Array<out CommandExecutor.Parameter>? {
        val parameters = method.parameters
        val processLength = parameters.size - 1
        return parameters.filterIndexed { index, _ -> index != 0 }.mapIndexed { index, parameter ->
            val type = parameter.type as Class<*>
            val name = parameter.getAnnotation(Parameter::class.java)?.value
            val optional = parameter.getAnnotation(Optional::class.java)
            val nullable = parameter.getAnnotation(Nullable::class.java)
            val playerName = parameter.getAnnotation(Playername::class.java)
            val vararg = parameter.getAnnotation(Vararg::class.java)?.value?.java
            val alias = "${method.declaringClass.canonicalName}#${method.name}"
            val param = name ?: parameter.name
            if (optional != null && nullable != null) {
                betterError(manager, """
                    The parameter '$param' Optional or nullable annotations can only have one, filtered.
                      Correction: ?
                        $alias(@Optional $param: ${type.name}) or
                        $alias(@Nullable $param: ${type.name})
                """.trimIndent())
                return null
            }
            if (vararg != null) {
                if (index + 1 != processLength) {
                    betterError(manager, """
                        The variable length parameter '$param' can only be in the last position of the actuator, filtered.
                          Correction: ?
                            fun $alias(..., $param: List<${vararg.name}>)
                    """)
                    return null
                }
                if (type != List::class.java) {
                    betterError(manager, """
                        The variable length parameter '$alias' must be a List type, filtered.
                          Correction: ?
                            fun $alias(..., $param: List<${vararg.name}>)
                    """.trimIndent())
                    return null
                }
                val genericParameter = method.genericParameterTypes[index + 1] as ParameterizedType
                val transformedType = genericParameter.actualTypeArguments.first() as Class<*>
                if (transformedType != vararg) {
                    betterError(manager, """
                        The expected type of the variable length parameter '$param' does not match the List type, filtered.
                          Correction: ?
                            fun $alias(..., $param: List<${vararg.name}>)
                    """.trimIndent())
                    return null
                }
            }
            if (playerName != null && type != String::class.java) {
                betterError(manager, """
                        The parameter '$param' matches the player name, but the type is not a string.
                          Correction: ?
                            fun $alias(..., @Playername $param: String)
                    """.trimIndent())
                return null
            }
            CommandExecutor.Parameter(
                    index,
                    type,
                    name,
                    optional?.def,
                    nullable != null,
                    type == String::class.java && playerName != null,
                    vararg
            )
        }
            .toTypedArray()
    }

    private fun registerChildren(
            manager: CommandManager,
            parent: DefaultRegisteredCommand?,
            @Suppress("UNUSED_PARAMETER")
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
                    betterError(manager, """
                        Unable to access constructor of the $child, confirm the declaration and 'public' modifier. filtered
                          Correction: ?
                            class ${child.name} (...) or
                            class ${child.name} { public constructor(...) }
                    """.trimIndent(), e)
                null to null
            }
            if (root != null && instance != null) {
                val childPermission = child.getAnnotation(Permission::class.java)
                val childDescription = child.getAnnotation(Description::class.java)
                val childExecutors = parseExecutors(manager, root.value, instance, child)
                val childCommand = buildCommandRegistered(
                        manager,
                        instance,
                        parent,
                        root.value,
                        root.aliases,
                        childPermission?.values,
                        childDescription,
                        emptyMap(),
                        childExecutors
                )
                parent?.registerChild(childCommand, true)
                registerChildren(manager, childCommand, instance, child)
            }
        }
    }

    private fun betterError(manager: CommandManager, message: String, e: Exception? = null) {
        manager.plugin.logger.warning("-------- Command parsing warning -----")
        message.split("\n").forEach { manager.plugin.logger.warning(it) }
        if (e != null)
            manager.plugin.logger.log(Level.SEVERE, e.message, e)
    }

    private fun buildCommandRegistered(
            manager: CommandManager,
            source: Any,
            parent: DefaultRegisteredCommand?,
            name: String,
            aliases: Array<out String>,
            permission: Array<out String>?,
            description: Description?,
            children: Map<String, DefaultRegisteredCommand>,
            executors: Map<String, DefaultCommandExecutor>
    ): DefaultRegisteredCommand {
        return DefaultRegisteredCommand(
                manager,
                source,
                parent,
                name,
                aliases,
                permission,
                description?.fallbackPrefix ?: "",
                description?.description,
                description?.usage,
                description?.prefix,
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
            parameters: Array<out CommandExecutor.Parameter>,
            description: String?
    ): DefaultCommandExecutor {
        return DefaultCommandExecutor(
                source,
                name,
                aliases,
                permission,
                isPlayable,
                parameters,
                Accessors.ofMethod(method),
                description
        )
    }
}
