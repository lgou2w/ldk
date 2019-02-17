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

import com.lgou2w.ldk.chat.ChatColor
import com.lgou2w.ldk.common.applyIfNotNull
import org.bukkit.command.CommandSender

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
    ) {
        val tooltips = createSimpleCommandTooltip(command, index, split, newLineDesc, named, sorted)
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
    ) {
        val tooltips = createSimpleCommandTooltips(command, newLineDesc, named, sorted)
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
    ): List<String> {
        val betterSplit = if (newLineDesc) split else split * 2
        val tooltips = createSimpleCommandTooltips(command, newLineDesc, named, sorted).chunked(betterSplit)
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
    ) : List<String> {
        val name = if (named) named(command) else command.name
        val children = command.children
        val executors = command.executors
        if (children.isEmpty() && executors.isEmpty())
            return emptyList()
        val childrenTooltips = children
            .map { "$SLASH$name$BLANK$PARAMETER${it.key}${description(it.value.description, newLineDesc)}" }
            .toMutableList()
        val executorsTooltips = executors
            .filter { it.key != command.name }
            .map { "$SLASH$name$BLANK$PARAMETER${it.key}${createSimpleExecutorUsage(it.value)}${description(it.value.description, newLineDesc)}" }
        val sortedTooltips = when (sorted) {
            Sorted.NONE -> childrenTooltips + executorsTooltips
            Sorted.DEFAULT -> (childrenTooltips + executorsTooltips).sorted()
            Sorted.C_T_E -> childrenTooltips.sorted() + executorsTooltips.sorted()
            Sorted.E_T_C -> executorsTooltips.sorted() + childrenTooltips.sorted()
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

    private fun whetherCommandMapping(
            command: RegisteredCommand,
            executors: Map<String, CommandExecutor>,
            newLineDesc: Boolean
    ): String? {
        val mapping = executors.values.find { it.name == command.name } ?: return null
        return description(mapping.description, newLineDesc)
    }

    private fun description(description: String?, newLineDesc: Boolean): String {
        return if (newLineDesc) "$NEWLINE$BLANK$DASH$BLANK${description ?: NOT_SET}"
        else "$BLANK$DASH$BLANK${description ?: NOT_SET}"
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
        DEFAULT,
        C_T_E,
        E_T_C,
        ;
    }
}
