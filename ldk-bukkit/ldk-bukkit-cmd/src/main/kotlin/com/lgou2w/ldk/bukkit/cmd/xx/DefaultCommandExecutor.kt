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

import com.lgou2w.ldk.reflect.AccessorMethod
import java.lang.reflect.Modifier
import java.util.*

class DefaultCommandExecutor(
        reference: Any,
        name: String,
        aliases: Array<out String>,
        permission: Array<out String>?,
        isPlayable: Boolean,
        val executor: AccessorMethod<Any, Any>,
        val parameters: Array<out DefaultCommandExecutor.Parameter>
) : CommandExecutorBase(reference, name, aliases, permission, isPlayable) {

    val isStatic = Modifier.isStatic(executor.source.modifiers)
    val length = parameters.size
    val max = length
    val min = max - parameters.count { it.canNullable }

    override fun execute(vararg args: Any?): Any? {
        return if (isStatic)
            executor.invoke(null, *args)
        else
            executor.invoke(reference, *args)
    }

    override fun hashCode(): Int {
        var result = reference.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + Arrays.hashCode(aliases)
        result = 31 * result + Arrays.hashCode(permission)
        result = 31 * result + isPlayable.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is DefaultCommandExecutor)
            return reference == other.reference &&
                   name == other.name &&
                   Arrays.equals(aliases, other.aliases) &&
                   Arrays.equals(permission, other.permission) &&
                   isPlayable == other.isPlayable
        return false
    }

    override fun toString(): String {
        return "DefaultCommandExecutor(name=$name, aliases=${Arrays.toString(aliases)}, permission=${Arrays.toString(permission)}, isPlayable=$isPlayable)"
    }

    class Parameter(
            val type : Class<*>,
            val defValue: String?,
            val isNullable: Boolean
    ) {
        val canNullable : Boolean
            get() = defValue != null || isNullable
    }
}
