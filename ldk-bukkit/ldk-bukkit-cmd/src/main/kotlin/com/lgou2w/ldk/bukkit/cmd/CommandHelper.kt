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

import com.lgou2w.ldk.chat.ChatColor
import com.lgou2w.ldk.common.applyIfNotNull

object CommandHelper {

    private val OPTIONAL_LEFT = "${ChatColor.DARK_GRAY}[${ChatColor.GREEN}"
    private val OPTIONAL_RIGHT = "${ChatColor.DARK_GRAY}]"
    private val REQUIRED_LEFT = "${ChatColor.DARK_GRAY}<${ChatColor.GREEN}"
    private val REQUIRED_RIGHT = "${ChatColor.DARK_GRAY}>"
    private val SLASH = "${ChatColor.GOLD}/"
    private val DASH = "${ChatColor.DARK_GRAY}-${ChatColor.GRAY}"
    private const val NEWLINE = "\n"
    private const val EMPTY = ""
    private const val BLANK = " "

    @JvmStatic
    fun createSimpleCommandTooltip(
            command: RegisteredCommand,
            index: Int,
            split: Int = 4,
            newLineDesc: Boolean = false,
            sorted: Boolean = true
    ): List<String> {
        val betterSplit = if (newLineDesc) split else split * 2
        val tooltips = createSimpleCommandTooltips(command, newLineDesc, sorted).chunked(betterSplit)
        if (tooltips.isEmpty()) return emptyList()
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
            sorted: Boolean = true
    ) : List<String> {
        val name = named(command)
        val children = command.children
        val executors = command.executors
        if (children.isEmpty() && executors.isEmpty())
            return emptyList()
        val values = children
            .map { "$SLASH$name$BLANK${it.key}${description(it.value.description, newLineDesc)}" }
            .toMutableList()
        values += executors
            .filter { it.key != command.name }
            .map { "$SLASH$name$BLANK${it.key}$BLANK${createSimpleExecutorUsage(it.value)}${description(it.value.description, newLineDesc)}" }
        whetherCommandMapping(command, executors, newLineDesc).applyIfNotNull { values.add(0, "$SLASH$name$BLANK$this") }
        return if (sorted) values.sorted() else values
    }

    @JvmStatic
    fun createSimpleExecutorUsage(executor: CommandExecutor): String {
        val parameters = executor.parameters
        return if (parameters.isNotEmpty()) {
            parameters.joinToString(separator = BLANK) { parameter ->
                val alias = if (parameter.vararg == null) parameter.name ?: parameter.type.simpleName
                    else parameter.name ?: parameter.vararg.simpleName
                if (parameter.canNullable) {
                    if (parameter.vararg == null) "$OPTIONAL_LEFT$alias=${parameter.defValue}$OPTIONAL_RIGHT"
                    else "$OPTIONAL_LEFT$alias=${parameter.defValue}...$OPTIONAL_RIGHT"
                } else {
                    if (parameter.vararg == null) "$REQUIRED_LEFT$alias$REQUIRED_RIGHT"
                    else "$REQUIRED_LEFT$alias...$REQUIRED_RIGHT"
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
        return if (newLineDesc) "$NEWLINE$BLANK$DASH$BLANK${description ?: "Not set."}" // TODO temporary
        else "$BLANK$DASH$BLANK${description ?: "Not set."}"
    }

    private fun named(command: RegisteredCommand?): String {
        val name = command?.name
        return if (command?.parent != null)
            "${named(command.parent)}$BLANK$name"
        else
            name ?: EMPTY
    }
}
