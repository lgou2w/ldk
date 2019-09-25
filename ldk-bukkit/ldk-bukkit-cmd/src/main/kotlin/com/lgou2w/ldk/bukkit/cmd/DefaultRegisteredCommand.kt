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

import com.lgou2w.ldk.chat.ChatColor
import com.lgou2w.ldk.common.Consumer
import com.lgou2w.ldk.reflect.DataType
import org.bukkit.command.CommandException
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import java.util.Arrays
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

/**
 * ## DefaultRegisteredCommand (默认已注册命令)
 *
 * @see [RegisteredCommand]
 * @see [DefaultCommandManager]
 * @author lgou2w
 */
class DefaultRegisteredCommand(
  override val manager: CommandManager,
  override val source: Any,
  override val parent: DefaultRegisteredCommand?,
  override val name: String,
  override val aliases: Array<out String>,
  override val permission: Array<out String>?,
  override val permissionDefault: PermissionDefault?,
  override val sorted: Int?,
  override val fallbackPrefix: String,
  override var description: String? = null,
  override var usage: String? = null,
  prefix: String? = null,
  children: Map<String, DefaultRegisteredCommand>,
  executors: Map<String, DefaultCommandExecutor>
) : RegisteredCommand {

  private val _children = ConcurrentHashMap(children)
  private val _executors = ConcurrentHashMap(executors)

  override val children : Map<String, RegisteredCommand>
    get() = Collections.unmodifiableMap(_children)
  override val executors : Map<String, CommandExecutor>
    get() = Collections.unmodifiableMap(_executors)

  override var prefix : String = prefix?.replace(COMMAND_PLACEHOLDER, name) ?: ""
    set(value) { field = value.replace(COMMAND_PLACEHOLDER, name) }

  override var feedback : CommandFeedback? = null
  override var isAllowCompletion : Boolean = true

  /**************************************************************************
   *
   * API
   *
   **************************************************************************/

  internal var isRegistered : Boolean = false

  override val rootParent : RegisteredCommand?
    get() = getRootParent(this)

  override fun registerChild(child: Any, forcibly: Boolean): Boolean {
    val command = manager.parser.parse(manager, this, child)
    return registerChild(command, forcibly)
  }

  override fun registerChild(child: RegisteredCommand, forcibly: Boolean): Boolean {
    if (child !is DefaultRegisteredCommand)
      throw IllegalArgumentException("The subcommand must be an instance of DefaultRegisteredCommand.")
    if (child.name.isBlank())
      throw IllegalArgumentException("The subcommand name cannot be blank.")
    if (child.isRegistered)
      throw IllegalArgumentException("The subcommand already has registered.")
    val existed = findChild(child.name, false)
    if (existed != null && !forcibly)
      return false
    _children[child.name] = child
    child.isRegistered = true
    CommandManagerBase
      .initialize(child, child.manager)
    return true
  }

  override fun findChild(name: String, allowAlias: Boolean): DefaultRegisteredCommand? {
    var child = _children[name]
    if (child == null && allowAlias)
      child = _children.values.find { name in it.aliases }
    return child
  }

  override fun findExecutor(name: String, allowAlias: Boolean): DefaultCommandExecutor? {
    var executor = _executors[name]
    if (executor == null && allowAlias)
      executor = _executors.values.find { name in it.aliases }
    return executor
  }

  override fun mappingDescriptions(description: String?, usage: String?) {
    this.description = description
    this.usage = usage
  }

  override fun mappingExecutorDescriptions(mapping: Map<String, String?>) {
    mapping.forEach { (name, description) ->
      val executor = _executors[name]
      if (executor != null)
        executor.description = description
    }
  }

  /**************************************************************************
   *
   * Significant
   *
   **************************************************************************/

  override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
    if (!testPermissionIfFailed(sender, permission) {
        (feedback ?: manager.globalFeedback)
          .onPermission(sender, label, this, null, args, it)
      })
      return true
    return if (args.isEmpty() || (compareWithAliases(this, label) && args.isEmpty())) {
      val childSameExecutor = findExecutor(name)
      if (childSameExecutor != null)
        invokeExecutor(childSameExecutor, sender, emptyArray()) // mapping not have arguments
      else {
        typeHelp(this, sender)
        true
      }
    } else {
      val child = findChild(args.first())
      child?.execute(sender, child.name, pollArgument(args))
        ?: invokeExecutor(null, sender, args)
    }
  }

  private fun invokeExecutor(
    executor: DefaultCommandExecutor?,
    sender: CommandSender,
    args: Array<out String>
  ): Boolean {
    val commandExecutor = executor ?: findExecutor(args.first())
    val arguments = if (args.isEmpty()) emptyArray() else pollArgument(args)
    if (commandExecutor != null && compareWithAliases(this, commandExecutor.name) && args.isNotEmpty()) {
      // mapping not have arguments
      typeHelp(this, sender)
      return true
    }
    if (commandExecutor != null) try {
      val parameterValues = ArrayList<Any?>()
      val feedback = feedback ?: manager.globalFeedback
      if (!parseExecutorArguments(commandExecutor, feedback, sender, arguments, parameterValues))
        return true
      return try {
        commandExecutor.execute(*parameterValues.toTypedArray())
        true
      } catch (e: Exception) {
        feedback.onUnhandled(sender, name, this, commandExecutor, arguments, e)
        true
      }
    } catch (e: Exception) {
      throw CommandException(e.message, e)
    } else {
      typeHelp(this, sender)
      return true
    }
  }

  private fun parseExecutorArguments(
    executor: DefaultCommandExecutor,
    feedback: CommandFeedback,
    sender: CommandSender,
    args: Array<out String>,
    parameterValues: MutableList<Any?>
  ): Boolean {
    if (!testPermissionIfFailed(sender, executor.permission) {
        feedback.onPermission(sender, name, this, executor, args, it) }) {
      return false
    }
    if ((executor.isPlayable && sender !is Player) ||
      (Player::class.java.isAssignableFrom(executor.senderType) && sender !is Player)
    ) {
      feedback.onPlayerOnly(sender, name, this, executor, args)
      return false
    }
    if (args.size < executor.min) {
      feedback.onMinimum(sender, name, this, executor, args, args.size, executor.min)
      return false
    }
    parameterValues.add(if (executor.isPlayable) sender as Player else sender)
    val vararg = executor.parameters.lastOrNull()
    val maxArguments = if (args.size <= executor.max) executor.max else args.size
    for (index in 0 until maxArguments) {
      val parameter = if (vararg != null && index >= vararg.index) vararg else executor.parameters.getOrNull(index)
      if (parameter == null || (vararg?.vararg == null && index > executor.length - 1))
        break
      val parameterType = parameter.vararg ?: parameter.type
      var transform = manager.transforms.getTransform(parameterType)
      var transformedType = parameterType
      if (transform == null || DataType.ofPrimitive(transformedType).isPrimitive) {
        transformedType = DataType.ofPrimitive(transformedType)
        transform = manager.transforms.getTransform(transformedType)
      }
      val value = args.getOrNull(index) ?: parameter.defValue
      val transformed = if (value != null) transform?.transform(value) else value
      if ((transformed == null && !parameter.canNullable) ||
        (transformed != null && !transformedType.isAssignableFrom(DataType.ofPrimitive(transformed::class.java)))
      ) {
        feedback.onTransform(sender, name, this, executor, args, parameterType, value, transformed)
        return false
      }
      parameterValues.add(transformed)
    }
    if (vararg?.vararg != null) {
      val varArguments = parameterValues.subList(vararg.index + 1, parameterValues.size).toList()
      parameterValues.removeAll(varArguments)
      parameterValues.add(ArrayList(varArguments)) // vararg
    }
    return true
  }

  override fun complete(sender: CommandSender, alias: String, args: Array<out String>): List<String>? {
    if (!isAllowCompletion || !testPermissionIfFailed(sender, permission))
      return null
    return if (args.size <= 1) {
      (_children
        .asSequence()
        .filter { testPermissionIfFailed(sender, it.value.permission) }
        .map { it.key to it.value.sorted }
        +
        _executors
          .asSequence()
          .filter { testPermissionIfFailed(sender, it.value.permission) && it.key != name }
          .map { it.key to it.value.sorted }
        )
        .filter {
          val first = args.firstOrNull()
          (first == null || it.first.startsWith(first))
        }
        .sortedBy { it.second }
        .map { it.first }
        .toMutableList()
    } else {
      val child = findChild(args.first())
      child?.complete(sender, alias, pollArgument(args))
        ?: invokeExecutorComplete(sender, args)
    }
  }

  private fun invokeExecutorComplete(sender: CommandSender, args: Array<out String>): List<String>? {
    val executor = findExecutor(args.first())
    return if (executor == null || !testPermissionIfFailed(sender, executor.permission))
      null
    else {
      if (args.lastIndex <= executor.max) {
        val parameter = executor.parameters[args.lastIndex - 1]
        val completer = manager.completes.getCompleter(parameter.type) ?: Completer.DEFAULT
        completer.onComplete(parameter, sender, args.last())
      } else {
        val vararg = executor.parameters.lastOrNull()
        if (vararg?.vararg == null)
          null
        else {
          val completer = manager.completes.getCompleter(vararg.vararg) ?: Completer.DEFAULT
          completer.onComplete(vararg, sender, args.last())
        }
      }
    }
  }

  override fun hashCode(): Int {
    var result = source.hashCode()
    result = result * 31 + name.hashCode()
    result = result * 31 + Arrays.hashCode(aliases)
    result = result * 31 + _children.hashCode()
    result = result * 31 + _executors.hashCode()
    return result
  }

  override fun equals(other: Any?): Boolean {
    if (other === this)
      return true
    if (other is DefaultRegisteredCommand)
      return source == other.source &&
        name == other.name &&
        Arrays.equals(aliases, other.aliases) &&
        _children == other._children &&
        _executors == other._executors
    return false
  }

  override fun toString(): String {
    return "DefaultRegisteredCommand(name=$name, aliases=${Arrays.toString(aliases)})"
  }

  companion object Constants {

    const val COMMAND_PLACEHOLDER = "<command>"

    private fun getRootParent(command: RegisteredCommand?): RegisteredCommand? {
      val parent = command?.parent
      return if (parent == null)
        command
      else
        getRootParent(parent)
    }

    private fun compareWithAliases(command: RegisteredCommand, source: String): Boolean {
      var result = source == command.name
      if (!result)
        result = command.aliases.contains(source)
      return result
    }

    private fun testPermissionIfFailed(
      sender: CommandSender,
      permission: Array<out String>?,
      block: Consumer<String>? = null
    ): Boolean {
      return permission == null || permission.all {
        val result = sender.hasPermission(it)
        if (!result && block != null)
          block(it)
        result
      }
    }

    private fun pollArgument(args: Array<out String>): Array<out String> {
      return if (args.size <= 1)
        emptyArray()
      else
        args.copyOfRange(1, args.size)
    }

    private fun typeHelp(command: DefaultRegisteredCommand, sender: CommandSender) {
      val root = command.rootParent ?: command
      val usage = command.usage
      if (usage == null || usage.isEmpty())
        sender.sendMessage(root.prefix + ChatColor.RED + betterDefaultTypeHelp(command, root.name))
      else
        sender.sendMessage(root.prefix + usage.replace(COMMAND_PLACEHOLDER, root.name))
    }

    private fun betterDefaultTypeHelp(command: DefaultRegisteredCommand, name: String): String {
      val chinese = command.manager.globalFeedback is SimpleChineseCommandFeedback ||
        command.feedback is SimpleChineseCommandFeedback
      return if (!chinese) "Unknown command. Type \"/$name help\" for help."
      else "未知命令. 输入 \"/$name help\" 查看帮助."
    }
  }
}
