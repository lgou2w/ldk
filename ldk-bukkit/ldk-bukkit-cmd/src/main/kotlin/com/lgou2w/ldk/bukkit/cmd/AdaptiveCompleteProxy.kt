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

import org.bukkit.command.CommandSender

class AdaptiveCompleteProxy : CompleteProxy {

    override fun getTypeCompleter(type: Class<*>): ParameterCompleter {
        return ParameterCompleter.adaptive(type)
    }

    override fun tabComplete(
            command: RegisteredCommand,
            sender: CommandSender,
            name: String,
            args: Array<out String>
    ): List<String> {
        return if (args.isEmpty() || args.size == 1) {
            command.childrenKeys.filter { child ->
                val first = args.firstOrNull()
                (first == null || child.startsWith(first))
            }
        } else {
            val child = command.getChild(args.first()) ?: return emptyList()
            if (args.lastIndex > child.max)
                return emptyList()
            val parameter = child.parameters[args.lastIndex - 1]
            val completer = getTypeCompleter(parameter.type)
            completer.onComplete(parameter, sender, args.last())
        }
    }
}
