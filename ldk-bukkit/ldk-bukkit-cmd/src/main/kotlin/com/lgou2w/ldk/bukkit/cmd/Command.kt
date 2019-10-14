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

import org.bukkit.permissions.PermissionDefault
import kotlin.reflect.KClass

/**
 * ## CommandRoot (命令根)
 *
 * @see [Initializable]
 * @see [StandardCommand]
 * @author lgou2w
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandRoot(
  /**
   * * The command name of this command root.
   * * 此命令根的命令名称.
   */
  val value: String,
  /**
   * * The command alias for this command root.
   * * 此命令根的命令别名.
   */
  val aliases: Array<String> = []
)

/**
 * ## Description (命令根描述)
 *
 * @see [CommandRoot]
 * @author lgou2w
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Description(
  /**
   * * This command root command prefix message.
   * * 此命令根的命令前缀消息.
   */
  val prefix: String = "",
  /**
   * * The command description information of this command root.
   * * 此命令根的命令描述信息.
   */
  val description: String = "",
  /**
   * * The command usage of this command root.
   * * 此命令根的命令用法.
   */
  val usage: String = "",
  /**
   * * The command fallback prefix for this command root.
   * * 此命令根的命令后备前缀.
   */
  val fallbackPrefix: String = ""
)

/**
 * ## Command (命令执行器)
 *
 * @author lgou2w
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Command(
  /**
   * * The command name of this command executor.
   * * 此命令执行器的命令名称.
   */
  val value: String,
  /**
   * * The command alias for this command executor.
   * * 此命令执行器的命令别名.
   */
  val aliases: Array<String> = []
)

/**
 * ## CommandDescription (命令执行器描述)
 *
 * @author lgou2w
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandDescription(
  /**
   * * The command description of this command executor.
   * * 此命令执行器的命令描述.
   */
  val value: String
)

/**
 * ## Permission (命令权限)
 *
 * @author lgou2w
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Permission(
  /**
   * * The permissions of this command root or command executor.
   * * 此命令根或命令执行器的权限.
   */
  vararg val values: String
)

/**
 * ## PermissionDefaultValue (命令权限默认值)
 *
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PermissionDefaultValue(
  /**
   * * The permission default value of this command root or command executor.
   * * 此命令根或命令执行器的权限默认值.
   */
  val value: PermissionDefault
)

/**
 * ## Sorted (排序)
 *
 * @see [CommandHelper.Sorted.ANNOTATION]
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Sorted(
  /**
   * * The sort index of this command root or command executor.
   * * 此命令根或命令执行器的排序索引.
   */
  val value: Int
)

// Removed since LDK 0.1.9
///**
// * @see [PlayerOnly]
// */
//@Target(AnnotationTarget.FUNCTION)
//@Retention(AnnotationRetention.RUNTIME)
//@Deprecated("RENAME", replaceWith = ReplaceWith("PlayerOnly"))
//annotation class Playable

/**
 * ## PlayerOnly (仅限玩家)
 *
 * * Indicates that this command executor can only be executed by the [org.bukkit.entity.Player] player.
 * * 表示这个命令执行器只能由玩家 [org.bukkit.entity.Player] 执行.
 *
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PlayerOnly

/**
 * ## Parameter (命令参数)
 *
 * @author lgou2w
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Parameter(
  /**
   * * The custom name value for this command executor parameter.
   * * 此命令执行器参数的自定义名称值.
   */
  val value: String
)

/**
 * ## Optional (可选参数)
 *
 * * Indicates that this command executor parameter is an optional default value.
 * * 表示此命令执行器参数是一个可选的具有默认值的.
 *
 * @author lgou2w
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Optional(
  /**
   * * Indicates the default value of this command executor parameter.
   * * 表示此命令执行器参数的默认值.
   */
  val def: String = ""
)

/**
 * ## Nullable (可空参数)
 *
 * * Indicates that this command executor parameter is optional to accept `null`.
 * * 表示此命令执行器参数是可选接受 `null` 的.
 *
 * @author lgou2w
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Nullable

/**
 * ## Playername (玩家名)
 *
 * @author lgou2w
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Playername

/**
 * ## Vararg (可变长度参数)
 *
 * @author lgou2w
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Vararg(
  /**
   * * Indicates the final expected type of this command executor variable length parameter.
   * * 表示此命令执行器可变长度参数的最终预期类型.
   */
  val value: KClass<*>
)
