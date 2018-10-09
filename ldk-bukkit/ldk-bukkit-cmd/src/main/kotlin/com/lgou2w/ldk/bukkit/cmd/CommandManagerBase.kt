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

import com.lgou2w.ldk.bukkit.reflect.MinecraftReflection
import com.lgou2w.ldk.common.Function
import com.lgou2w.ldk.reflect.AccessorMethod
import com.lgou2w.ldk.reflect.Accessors
import com.lgou2w.ldk.reflect.DataType
import com.lgou2w.ldk.reflect.FuzzyReflect
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import java.lang.reflect.Modifier
import java.util.*

abstract class CommandManagerBase(
        final override val plugin: Plugin
) : CommandManager {

    private val transforms : MutableMap<Class<*>, TypeTransform<*>> = HashMap()
    private val completes : MutableMap<Class<*>, TypeCompleter> = HashMap()
    private val commands : MutableMap<String, RegisteredCommand> = HashMap()

    override var globalFeedback: CommandFeedback = DefaultCommandFeedback()

    override fun registerCommand(source: Class<*>, vararg args: Any): Boolean {
        val instance = newSourceInstance(source, *args)
        return registerCommand(instance)
    }

    override fun registerCommand(source: Any): Boolean {
        val parsed = parseCommand(source)
        synchronized (commands) {
            val registered = commands[parsed.name]
            if (registered != null)
                throw IllegalArgumentException("This command ${parsed.name} has already been registered.")
            registerBukkitCommand(parsed)
            if (parsed.source is Initializable)
                (parsed.source as Initializable).initialize(this, parsed)
            registered(parsed)
            commands[parsed.name] = parsed
            return true
        }
    }

    override fun getCommand(command: String): RegisteredCommand? {
        synchronized (commands) {
            return commands[command]
        }
    }

    override fun getCommand(source: Class<*>): RegisteredCommand? {
        synchronized (commands) {
            return commands.values.find { it.source.javaClass == source }
        }
    }

    override fun parseChildProvider(parsed: RegisteredCommand, provider: ChildProvider): RegisteredCommand.Child? {
        return parseCommandChild(parsed, provider)
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
            val transform = transforms[type] ?: return null
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

    override fun addDefaultTypeCompletes() {
        TypeCompletes.addDefaultTypeCompletes(this)
    }

    override fun addTypeCompleter(type: Class<*>, completer: (
            parameter: RegisteredCommand.ChildParameter,
            sender: CommandSender,
            value: String) -> List<String>
    ) {
        return addTypeCompleter(type, object : TypeCompleter {
            override fun onComplete(parameter: RegisteredCommand.ChildParameter, sender: CommandSender, value: String): List<String> {
                return completer(parameter, sender, value)
            }
        })
    }

    override fun addTypeCompleter(type: Class<*>, completer: TypeCompleter) {
        synchronized (completes) {
            completes[type] = completer
        }
    }

    override fun getTypeCompleter(type: Class<*>): TypeCompleter? {
        synchronized (completes) {
            return completes[type]
        }
    }

    override fun hasTypeCompleter(type: Class<*>): Boolean {
        return getTypeCompleter(type) != null
    }

    protected open fun registered(registered: RegisteredCommand) {
    }

    @Throws(RuntimeException::class)
    protected open fun parseCommand(
            source: Any
    ) : RegisteredCommand {
        val root = firstParse(source)
        val children = parseCommandChildren(source)
        val parsed = parsedCommand(root, source, children)
        children.values.forEach { child -> child.registered = parsed }
        return parsed
    }

    protected open fun parseCommandChild(parsed: RegisteredCommand, source: ChildProvider) : RegisteredCommandBase.ChildBase? {
        val children = parseCommandChildren(source)
        val child = children.values.firstOrNull() ?: return null
        child.registered = parsed
        return child
    }

    protected open fun parseCommandChildren(source: Any) : MutableMap<String, RegisteredCommandBase.ChildBase> {
        return FuzzyReflect.of(source, true)
            .useMethodMatcher()
            .withAnnotation(Command::class.java)
            .results()
            .asSequence()
            .filter { method ->
                val command = method.getAnnotation(Command::class.java)
                val parameters = method.parameters
                val firstMatched = parameters.isNotEmpty() && CommandSender::class.java.isAssignableFrom(parameters.first().parameterizedType as Class<*>)
                if (!firstMatched)
                    plugin.logger.warning("The sub command '${command.value}' function first parameter is not CommandSender type, filtered.")
                firstMatched
            }.associate { method ->
                val command = method.getAnnotation(Command::class.java)
                val permission = method.getAnnotation(Permission::class.java)
                val isPlayable = method.getAnnotation(Playable::class.java) != null
                val parameters = method.parameters.filterIndexed { index, _ -> index != 0 }.mapNotNull { parameter ->
                    val type = parameter.parameterizedType as Class<*>
                    val optional = parameter.getAnnotation(Optional::class.java)
                    val nullable = parameter.getAnnotation(Nullable::class.java)
                    if (optional != null && nullable != null) {
                        plugin.logger.warning("Optional or nullable annotations can only have one, filtered.")
                        null
                    } else {
                        RegisteredCommand.ChildParameter(type, optional, nullable != null)
                    }
                }.toTypedArray()
                val accessor = Accessors.ofMethod<Any, Any>(method)
                val child = parsedCommandChild(source, command, permission, isPlayable, parameters, accessor)
                parameters.forEach { parameter -> parameter.child = child }
                command.value to child
            }.toMutableMap()
    }

    protected open fun parsedCommand(
            root: CommandRoot,
            source: Any,
            children: MutableMap<String, RegisteredCommandBase.ChildBase>
    ) : RegisteredCommandBase {
        @Suppress("UNCHECKED_CAST")
        val parsedChildren = children as MutableMap<String, RegisteredCommand.Child>
        return RegisteredCommandBase(this, root, source, parsedChildren)
    }

    protected open fun parsedCommandChild(
            source: Any,
            command: Command,
            permission: Permission?,
            isPlayable: Boolean,
            parameters: Array<out RegisteredCommand.ChildParameter>,
            accessor: AccessorMethod<Any, Any>
    ) : RegisteredCommandBase.ChildBase {
        if (source is ChildProvider)
            return RegisteredCommandBase.ChildBaseProvider(source, command, permission, isPlayable, parameters, accessor)
        return RegisteredCommandBase.ChildBase(command, permission, isPlayable, parameters, accessor)
    }

    companion object {

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        private fun firstParse(source: Any) : CommandRoot {
            val clazz = source.javaClass
            if (Modifier.isAbstract(clazz.modifiers) || Modifier.isInterface(clazz.modifiers) || clazz.isEnum || clazz.isAnnotation)
                throw IllegalArgumentException("The command $clazz cannot be an abstract, interface, enum, or annotation class.")
            return clazz.getAnnotation(CommandRoot::class.java)
                   ?: throw IllegalArgumentException("The command $clazz must be annotated by CommandRoot.")
        }

        @JvmStatic
        private fun newSourceInstance(source: Class<*>, vararg args: Any) : Any = try {
            val primitives = DataType.ofPrimitive(args.map { it.javaClass }.toTypedArray())
            val constructor = source.getConstructor(*primitives)
            constructor.newInstance(*args)
        } catch (e: Exception) {
            throw IllegalArgumentException("Unable access $source constructor, Modifier 'public' and declaration?", e.cause ?: e)
        }

        @JvmStatic private val bukkitCommandMap : CommandMap by lazy {
            FuzzyReflect.of(MinecraftReflection.getCraftBukkitClass("CraftServer"), true)
                .useFieldMatcher()
                .withType(CommandMap::class.java)
                .resultAccessorAs<Server, CommandMap>()[Bukkit.getServer()] as CommandMap
        }
        @Throws(IllegalStateException::class)
        @JvmStatic private fun registerBukkitCommand(registered: RegisteredCommand) {
            try {
                bukkitCommandMap.register(registered.name, registered.fallbackPrefix, registered.proxy)
            } catch (e: Exception) {
                throw IllegalStateException("Internal error when registering to bukkit.", e.cause ?: e)
            }
        }
    }
}
