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

package com.lgou2w.ldk.bukkit.reflect

import com.lgou2w.ldk.common.lazyAnyClass
import com.lgou2w.ldk.common.lazyAnyOrNullClass

/**
 * * Lazy loading of the `NMS` class delegate for the given [className].
 * * 延迟加载给定类名 [className] 的 `NMS` 类委托.
 */
fun lazyMinecraftClass(className: String)
        = lazyAnyClass { MinecraftReflection.getMinecraftClass(className) }

/**
 * * Lazy loading of the given [className] and the [aliases] of the `NMS` class delegate.
 * * 延迟加载给定类名 [className] 和别名 [aliases] 的 `NMS` 类委托.
 */
fun lazyMinecraftClass(className: String, vararg aliases: String)
        = lazyAnyClass { MinecraftReflection.getMinecraftClass(className, *aliases) }

/**
 * * Lazy loading of the `NMS` class or `null` delegate for the given [className].
 * * 延迟加载给定类名 [className] 的 `NMS` 类或 `null` 委托.
 */
fun lazyMinecraftClassOrNull(className: String)
        = lazyAnyOrNullClass { MinecraftReflection.getMinecraftClassOrNull(className) }

/**
 * * Lazy loading of the given [className] and the [aliases] of the `NMS` class or the `null` delegate.
 * * 延迟加载给定类名 [className] 和别名 [aliases] 的 `NMS` 类或 `null` 委托.
 */
fun lazyMinecraftClassOrNull(className: String, vararg aliases: String)
        = lazyAnyOrNullClass { MinecraftReflection.getMinecraftClassOrNull(className, *aliases) }

/**
 * * Lazy loading of the `CraftBukkit` class delegate for the given [className].
 * * 延迟加载给定类名 [className] 的 `CraftBukkit` 类委托.
 */
fun lazyCraftBukkitClass(className: String)
        = lazyAnyClass { MinecraftReflection.getCraftBukkitClass(className) }

/**
 * * Lazy loading of the `CraftBukkit` class or `null` delegate for the given [className].
 * * 延迟加载给定类名 [className] 的 `CraftBukkit` 类或 `null` 委托.
 */
fun lazyCraftBukkitClassOrNull(className: String)
        = lazyAnyOrNullClass { MinecraftReflection.getCraftBukkitClassOrNull(className) }
