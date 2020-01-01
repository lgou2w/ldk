/*
 * Copyright (C) 2016-2020 The lgou2w <lgou2w@hotmail.com>
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
import com.lgou2w.ldk.common.notNull
import com.lgou2w.ldk.reflect.FuzzyReflect
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.CommandException
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.command.SimpleCommandMap
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.Plugin
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level

/**
 * ## CommandManagerBase (命令管理器基础)
 *
 * @see [CommandManager]
 * @author lgou2w
 */
abstract class CommandManagerBase(
  final override val plugin: Plugin,
  override val parser: CommandParser
) : CommandManager {

  protected val mCommands = ConcurrentHashMap<String, RegisteredCommand>()
  override val commands : Map<String, RegisteredCommand>
    get() = Collections.unmodifiableMap(mCommands)

  override val transforms = Transforms()
  override val completes = Completes()
  override var globalFeedback : CommandFeedback = SimpleCommandFeedback()

  override fun registerCommand(source: Any): RegisteredCommand {
    val command = parser.parse(this, source)
    val existed = mCommands[command.name]
    if (existed != null)
      throw UnsupportedOperationException("This command '${command.name}' has already been registered.")
    return if (registerBukkitCommand(command)) {
      initialize(command, this)
      mCommands[command.name] = command
      command
    } else {
      throw UnsupportedOperationException("Internal error when registering command '${command.name}' to bukkit.")
    }
  }

  override fun unregisterCommand(command: String): Boolean {
    val existed = mCommands[command] ?: return true
    return unregisterCommand(existed)
  }

  override fun unregisterCommand(command: RegisteredCommand): Boolean {
    return if (mCommands.containsKey(command.name))
      unregisterBukkitCommand(command) && mCommands.remove(command.name, command)
    else
      true
  }

  override fun unregisterCommands(): Boolean {
    return mCommands.values.all(::unregisterCommand)
  }

  override fun getCommand(command: String): RegisteredCommand? {
    return mCommands[command]
  }

  companion object {

    @JvmStatic internal fun initialize(command: RegisteredCommand, manager: CommandManager) {
      val source = command.source
      if (source is Initializable) try {
        source.initialize(command)
      } catch (e: Exception) {
        manager.plugin.logger.log(Level.WARNING, "Command source object initialization exception:", e)
      }
      command.children.values.forEach { child ->
        initialize(child, manager)
      }
    }

    @JvmStatic private val bukkitCommandMap : CommandMap by lazy {
      FuzzyReflect.of(MinecraftReflection.getCraftBukkitClass("CraftServer"), true)
        .useFieldMatcher()
        .withType(CommandMap::class.java)
        .resultAccessorAs<Server, CommandMap>()[Bukkit.getServer()] as CommandMap
    }
    @JvmStatic private fun registerBukkitCommand(command: RegisteredCommand): Boolean {
      return try {
        val existed = bukkitCommandMap.getCommand(command.name)
        if (existed != null) {
          if (existed is PluginCommand && existed.plugin != command.manager.plugin)
            throw UnsupportedOperationException("The command '${command.name}' has been registered by another plugin and cannot be override.")
          unregisterBukkitCommand(command)
        }
        val betterFallbackPrefix = command.fallbackPrefix.let { if (it.isBlank()) command.manager.plugin.name else it }
        val result = bukkitCommandMap.register(command.name, betterFallbackPrefix, CommandProxy(command))
        if (result) // register permission default
          registerPermissionDefault(command)
        result
      } catch (e: Exception) {
        if (e is UnsupportedOperationException)
          throw e
        command.manager.plugin.logger.log(Level.SEVERE, e.message, e)
        false
      }
    }
    @JvmStatic private fun registerPermissionDefault(command: RegisteredCommand) {
      registerPermissionDefault0(command.permission, command.permissionDefault)
      command.executors.values.forEach { executor ->
        registerPermissionDefault0(executor.permission, executor.permissionDefault)
      }
      command.children.values
        .forEach(::registerPermissionDefault)
    }
    @JvmStatic private fun registerPermissionDefault0(permissions: Array<out String>?, permissionDefault: PermissionDefault?) {
      if (permissions != null && permissions.isNotEmpty() && permissionDefault != null) {
        permissions.forEach { permission ->
          val existed = Bukkit.getPluginManager().getPermission(permission)
          if (existed == null)
            Bukkit.getPluginManager().addPermission(Permission(permission, permissionDefault))
        }
      }
    }
    @JvmStatic private val simpleCommandMapKnownCommands by lazy {
      FuzzyReflect.of(SimpleCommandMap::class.java, true)
        .useFieldMatcher()
        .withType(Map::class.java)
        .withName("knownCommands")
        .resultAccessorAs<SimpleCommandMap, MutableMap<String, org.bukkit.command.Command>>()
    }
    @JvmStatic private fun unregisterBukkitCommand(command: RegisteredCommand): Boolean {
      val simpleCommandMap = bukkitCommandMap as? SimpleCommandMap ?: return false
      val knownCommands = simpleCommandMapKnownCommands[simpleCommandMap].notNull()
      val label = command.name.toLowerCase()
      val betterFallbackPrefix = command.fallbackPrefix.let { if (it.isBlank()) command.manager.plugin.name else it }
      return if (knownCommands.remove(label) != null) {
        knownCommands.remove("$betterFallbackPrefix:$label")
        command.aliases.map(String::toLowerCase).forEach {
          knownCommands.remove(it)
          knownCommands.remove("$betterFallbackPrefix:$it")
        }
        true
      } else
        false
    }

    private class CommandProxy(
      val command: RegisteredCommand
    ) : org.bukkit.command.Command(
      command.name,
      command.description ?: command.name,
      command.usage ?: "/${command.name} help",
      command.aliases.toMutableList()
    ) {
      init {
        permission = command.permission?.joinToString(";")
      }
      override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        if (!command.manager.plugin.isEnabled)
          throw CommandException("Cannot execute command '$label' in plugin ${command.manager.plugin.description.fullName} - plugin is disabled.")
        return command.execute(sender, label, args)
      }
      override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): List<String> {
        if (!command.manager.plugin.isEnabled)
          return emptyList()
        return command.complete(sender, alias, args) ?: emptyList()
      }
    }
  }
}
