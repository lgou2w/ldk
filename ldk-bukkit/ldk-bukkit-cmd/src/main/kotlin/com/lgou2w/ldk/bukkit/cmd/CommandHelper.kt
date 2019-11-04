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
import com.lgou2w.ldk.chat.toColor
import com.lgou2w.ldk.common.applyIfNotNull
import org.bukkit.command.CommandSender

/**
 * ## CommandHelper (命令帮助器)
 *
 * @author lgou2w
 */
object CommandHelper {

  private val PARAMETER = "${ChatColor.AQUA}"
  private val OPTIONAL_LEFT = "${ChatColor.DARK_GRAY}[$PARAMETER"
  private val OPTIONAL_RIGHT = "${ChatColor.DARK_GRAY}]"
  private val REQUIRED_LEFT = "${ChatColor.DARK_GRAY}<$PARAMETER"
  private val REQUIRED_RIGHT = "${ChatColor.DARK_GRAY}>"
  private val SLASH = "${ChatColor.GRAY}/${ChatColor.GOLD}"
  private val DASH = "${ChatColor.DARK_GRAY}-${ChatColor.GRAY}"
  private val DEF = "${ChatColor.DARK_GRAY}=${ChatColor.GREEN}"
  private val VARARG = "$PARAMETER..."
  private const val NOT_SET = "Not set."
  private const val NEWLINE = "\n"
  private const val EMPTY = ""
  private const val BLANK = " "

  @JvmStatic
  fun sendSimpleCommandTooltip(
    receiver: CommandSender,
    command: RegisteredCommand,
    index: Int,
    split: Int = 4,
    newLineDesc: Boolean = false,
    named: Boolean = true,
    sorted: Sorted = Sorted.NONE
  ) = sendSimpleCommandTooltip(receiver, command, index, split, newLineDesc, named, false, sorted)

  /**
   * @since LDK 0.1.8-rc
   */
  @JvmStatic
  fun sendSimpleCommandTooltip(
    receiver: CommandSender,
    command: RegisteredCommand,
    index: Int,
    split: Int = 4,
    newLineDesc: Boolean = false,
    named: Boolean = true,
    permission: Boolean = false,
    sorted: Sorted = Sorted.NONE
  ) {
    val tooltips = createSimpleCommandTooltip(receiver, command, index, split, newLineDesc, named, permission, sorted)
    tooltips.forEach { tooltip ->
      if (tooltip.contains(NEWLINE)) receiver.sendMessage(tooltip.split(NEWLINE).toTypedArray())
      else receiver.sendMessage(tooltip)
    }
  }

  @JvmStatic
  fun sendSimpleCommandTooltips(
    receiver: CommandSender,
    command: RegisteredCommand,
    newLineDesc: Boolean = false,
    named: Boolean = true,
    sorted: Sorted = Sorted.NONE
  ) = sendSimpleCommandTooltips(receiver, command, newLineDesc, named, false, sorted)

  /**
   * @since LDK 0.1.8-rc
   */
  @JvmStatic
  fun sendSimpleCommandTooltips(
    receiver: CommandSender,
    command: RegisteredCommand,
    newLineDesc: Boolean = false,
    named: Boolean = true,
    permission: Boolean = false,
    sorted: Sorted = Sorted.NONE
  ) {
    val tooltips = createSimpleCommandTooltips(receiver, command, newLineDesc, named, permission, sorted)
    tooltips.forEach { tooltip ->
      if (tooltip.contains(NEWLINE)) receiver.sendMessage(tooltip.split(NEWLINE).toTypedArray())
      else receiver.sendMessage(tooltip)
    }
  }

  @JvmStatic
  fun createSimpleCommandTooltip(
    command: RegisteredCommand,
    index: Int,
    split: Int = 4,
    newLineDesc: Boolean = false,
    named: Boolean = true,
    sorted: Sorted = Sorted.NONE
  ): List<String>
    = createSimpleCommandTooltip(null, command, index, split, newLineDesc, false, named, sorted)

  @JvmStatic
  private fun createSimpleCommandTooltip(
    receiver: CommandSender?,
    command: RegisteredCommand,
    index: Int,
    split: Int = 4,
    newLineDesc: Boolean = false,
    named: Boolean = true,
    permission: Boolean = false,
    sorted: Sorted = Sorted.NONE
  ): List<String> {
    val betterSplit = if (newLineDesc) split else split * 2
    val tooltips = createSimpleCommandTooltips(receiver, command, newLineDesc, named, permission, sorted).chunked(betterSplit)
    if (tooltips.isEmpty())
      return emptyList()
    return when {
      index <= 0 -> tooltips.first()
      index >= tooltips.size -> tooltips.last()
      else -> tooltips[index]
    }
  }

  @JvmStatic
  fun createSimpleCommandTooltips(
    command: RegisteredCommand,
    newLineDesc: Boolean = false,
    named: Boolean = true,
    sorted: Sorted = Sorted.NONE
  ): List<String>
    = createSimpleCommandTooltips(null, command, newLineDesc, false, named, sorted)

  @JvmStatic
  private fun createSimpleCommandTooltips(
    receiver: CommandSender?,
    command: RegisteredCommand,
    newLineDesc: Boolean = false,
    named: Boolean = true,
    permission: Boolean = false,
    sorted: Sorted = Sorted.NONE
  ): List<String> {
    val name = if (named) named(command) else command.name
    val children = command.children
    val executors = command.executors
    if (children.isEmpty() && executors.isEmpty())
      return emptyList()
    val childrenTooltips = children
      .asSequence()
      .filter { !permission || hasPermission(receiver, it.value.permission) }
      .map { it.value.sorted to "$SLASH$name$BLANK$PARAMETER${it.key}${description(it.value.description, newLineDesc)}" }
      .toMutableList()
    val executorsTooltips = executors
      .asSequence()
      .filter { it.key != command.name && (!permission || hasPermission(receiver, it.value.permission)) }
      .map { it.value.sorted to "$SLASH$name$BLANK$PARAMETER${it.key}${createSimpleExecutorUsage(it.value)}${description(it.value.description, newLineDesc)}" }
      .toMutableList()
    val sortedTooltips = when (sorted) {
      Sorted.NONE -> (childrenTooltips.map { it.second } + executorsTooltips.map { it.second })
      Sorted.ANNOTATION -> (childrenTooltips + executorsTooltips)
        .asSequence()
        .sortedBy { it.first }
        .map { it.second }
        .toMutableList()
      Sorted.DEFAULT -> (childrenTooltips + executorsTooltips).map { it.second }.sorted()
      Sorted.C_T_E -> childrenTooltips.map { it.second }.sorted() + executorsTooltips.map { it.second }.sorted()
      Sorted.E_T_C -> executorsTooltips.map { it.second }.sorted() + childrenTooltips.map { it.second }.sorted()
    }.toMutableList()
    whetherCommandMapping(command, executors, newLineDesc)
      .applyIfNotNull { sortedTooltips.add(0, "$SLASH$name$this") }
    return sortedTooltips
  }

  @JvmStatic
  fun createSimpleExecutorUsage(executor: CommandExecutor): String {
    val parameters = executor.parameters
    return if (parameters.isNotEmpty()) {
      BLANK + parameters.joinToString(separator = BLANK) { parameter ->
        val alias = if (parameter.vararg == null) parameter.name ?: parameter.type.simpleName
        else parameter.name ?: parameter.vararg.simpleName
        if (parameter.canNullable) {
          if (parameter.vararg == null) "$OPTIONAL_LEFT$alias$DEF${parameter.defValue}$OPTIONAL_RIGHT"
          else "$OPTIONAL_LEFT$alias$DEF${parameter.defValue}$VARARG$OPTIONAL_RIGHT"
        } else {
          if (parameter.vararg == null) "$REQUIRED_LEFT$alias$REQUIRED_RIGHT"
          else "$REQUIRED_LEFT$alias$VARARG$REQUIRED_RIGHT"
        }
      }
    } else {
      EMPTY
    }
  }

  private fun hasPermission(receiver: CommandSender?, permission: Array<out String>?): Boolean {
    return if (receiver == null || permission == null || permission.isEmpty()) true
    else permission.all(receiver::hasPermission)
  }

  private fun whetherCommandMapping(
    command: RegisteredCommand,
    executors: Map<String, CommandExecutor>,
    newLineDesc: Boolean
  ): String? {
    val mapping = executors.values.find { it.name == command.name } ?: return null
    return description(mapping.description, newLineDesc)
  }

  private fun description(description: String?, newLineDesc: Boolean): String {
    val colorDescription = description?.toColor()
    return if (newLineDesc) "$NEWLINE$BLANK$DASH$BLANK${colorDescription ?: NOT_SET}"
    else "$BLANK$DASH$BLANK${colorDescription ?: NOT_SET}"
  }

  private fun named(command: RegisteredCommand?): String {
    val name = command?.name
    return if (command?.parent != null)
      "${named(command.parent)}$BLANK$name"
    else
      name ?: EMPTY
  }

  enum class Sorted {

    NONE,
    /**
     * @since LDK 0.1.8-rc
     */
    ANNOTATION,
    DEFAULT,
    C_T_E,
    E_T_C,
    ;
  }
}
