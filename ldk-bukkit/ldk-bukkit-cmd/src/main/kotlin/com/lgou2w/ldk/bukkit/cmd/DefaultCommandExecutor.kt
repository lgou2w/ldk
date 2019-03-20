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

import com.lgou2w.ldk.reflect.AccessorMethod
import java.lang.reflect.Modifier
import java.util.Arrays

/**
 * ## DefaultCommandExecutor (默认命令执行器)
 *
 * @see [CommandExecutorBase]
 * @author lgou2w
 */
class DefaultCommandExecutor(
        reference: Any,
        name: String,
        aliases: Array<out String>,
        permission: Array<out String>?,
        isPlayable: Boolean,
        parameters: Array<out CommandExecutor.Parameter>,
        /**
         * * The method accessor object for this executor.
         * * 此执行器的方法访问器对象.
         *
         * @see [AccessorMethod]
         */
        val executor: AccessorMethod<Any, Any>,
        override var description: String?
) : CommandExecutorBase(reference, name, aliases, permission, isPlayable, parameters) {

    /**
     * * Indicate whether this executor method is static.
     * * 表示此执行器的方法是否为静态的.
     */
    val isStatic = Modifier.isStatic(executor.source.modifiers)

    override fun execute(vararg args: Any?): Any? {
        return if (isStatic)
            executor.invoke(null, *args)
        else
            executor.invoke(reference, *args)
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + executor.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is DefaultCommandExecutor)
            return super.equals(other) && executor == other.executor
        return false
    }

    override fun toString(): String {
        return "DefaultCommandExecutor(name=$name, aliases=${Arrays.toString(aliases)}, isPlayable=$isPlayable)"
    }
}
