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

interface CommandExecutor {

    val reference : Any

    val name : String

    val aliases : Array<out String>

    val permission : Array<out String>?

    val isPlayable : Boolean

    val parameters: Array<out Parameter>

    fun execute(vararg args: Any?) : Any?

    data class Parameter(
            val type : Class<*>,
            val name: String?,
            val defValue: String?,
            val isNullable: Boolean
    ) {
        val canNullable : Boolean
            get() = defValue != null || isNullable
    }
}
