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

import org.bukkit.permissions.PermissionDefault

/**
 * ## CommandExecutor (命令执行器)
 *
 * @see [CommandExecutorBase]
 * @see [DefaultCommandExecutor]
 * @author lgou2w
 */
interface CommandExecutor {

    /**
     * * Method holder object reference for this executor.
     * * 此执行器的方法持有者对象引用.
     */
    val reference : Any

    /**
     * * The name of this executor.
     * * 此执行器的名称.
     */
    val name : String

    /**
     * * The aliases of this executor.
     * * 此执行器的别名.
     */
    val aliases : Array<out String>

    /**
     * * The permission of this executor.
     * * 此执行器的权限.
     */
    val permission : Array<out String>?

    /**
     * * The permission default of this executor.
     * * 此执行器的权限默认.
     *
     * @since LDK 0.1.8-rc
     */
    val permissionDefault : PermissionDefault?

    /**
     * * The sort index of this executor.
     * * 此执行器的排序索引.
     *
     * @since LDK 0.1.8-rc
     */
    val sorted : Int?

    /**
     * * Indicate whether this executor can only be executed by the [org.bukkit.entity.Player].
     * * 表示此执行器是否只能被玩家 [org.bukkit.entity.Player] 执行.
     */
    val isPlayable : Boolean

    /**
     * * An array of arguments for this executor.
     * * 此执行器的参数数组.
     */
    val parameters: Array<out Parameter>

    /**
     * * Call the execution of the given parameter [args].
     * * 将给定的参数 [args] 进行调用执行.
     */
    fun execute(vararg args: Any?) : Any?

    /**
     * * The description of this executor.
     * * 此执行器的描述.
     */
    var description : String?

    /**
     * ## Parameter (执行器参数)
     *
     * @see [CommandExecutor.parameters]
     * @author lgou2w
     */
    data class Parameter(
            /**
             * * The index of this parameter.
             * * 此参数的索引.
             */
            val index: Int,
            /**
             * * The type of this parameter.
             * * 此参数的类型.
             */
            val type : Class<*>,
            /**
             * * The name of this parameter.
             * * 此参数的名称.
             */
            val name: String?,
            /**
             * * The default value of this parameter.
             * * 此参数的默认值.
             */
            val defValue: String?,
            /**
             * * Indicate whether this parameter can be `null`.
             * * 表示此参数是否可 `null` 的.
             */
            val isNullable: Boolean,
            /**
             * * Indicate whether this parameter matches the online player name.
             * * 表示此参数是否匹配在线玩家名.
             */
            val isPlayerName: Boolean,
            /**
             * * Represents the variable length type of this parameter.
             * * 表示此参数的可变长度类型.
             */
            val vararg: Class<*>?
    ) {
        /**
         * * Indicate whether this parameter is optional.
         * * 表示此参数是否为可选的.
         */
        val canNullable : Boolean
            get() = defValue != null || isNullable
    }
}
