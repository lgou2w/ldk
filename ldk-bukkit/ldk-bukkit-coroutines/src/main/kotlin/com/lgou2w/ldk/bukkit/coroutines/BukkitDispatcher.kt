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

package com.lgou2w.ldk.bukkit.coroutines

import com.lgou2w.ldk.common.SuspendApplicator
import com.lgou2w.ldk.common.SuspendApplicatorFunction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

/**
 * * Create a Bukkit coroutine dispatcher from the given [plugin] with the given [state].
 * * 从给定的插件 [plugin] 以给定的状态 [state] 创建一个 Bukkit 协程调度程序.
 */
@JvmOverloads
fun Dispatchers.bukkit(plugin: Plugin, state: State = State.SYNC)
  = BukkitDispatcher(plugin, state)

/**
 * * Launches new coroutine with the plugin in the given [initializeState], and return the reference to the coroutine as [Job].
 * * 以插件用给定的状态启动新的协同程序, 并将协程的引用作为 [Job] 返回.
 */
@JvmOverloads
fun Plugin.launcher(
  initializeState: State = State.SYNC,
  block: SuspendApplicator<BukkitCoroutineFactory>
): Job = BukkitCoroutineFactory(this).launcher(initializeState, block)

/**
 * * Launches new coroutine with the plugin in the given [initializeState], and return the reference to the coroutine as [Job].
 * * 以插件用给定的状态启动新的协同程序, 并将协程的引用作为 [Job] 返回.
 *
 * @since LDK 0.2.0
 */
@JvmOverloads
fun Plugin.launcher(
  name: String,
  initializeState: State = State.SYNC,
  block: SuspendApplicator<BukkitCoroutineFactory>
): Job = BukkitCoroutineFactory(name, this).launcher(initializeState, block)

/**
 * * Launches new coroutine with the plugin in asynchronous state, and return the reference to the coroutine as [Job].
 * * 以插件用异步状态启动新的协同程序, 并将协程的引用作为 [Job] 返回.
 */
fun Plugin.launcherAsync(block: SuspendApplicator<BukkitCoroutineFactory>): Job
  = BukkitCoroutineFactory(this).launcher(State.ASYNC, block)

/**
 * * Launches new coroutine with the plugin in asynchronous state, and return the reference to the coroutine as [Job].
 * * 以插件用异步状态启动新的协同程序, 并将协程的引用作为 [Job] 返回.
 *
 * @since LDK 0.2.0
 */
fun Plugin.launcherAsync(name: String, block: SuspendApplicator<BukkitCoroutineFactory>): Job
  = BukkitCoroutineFactory(name, this).launcher(State.ASYNC, block)

/**
 * * Use the Bukkit synchronization dispatcher context [Dispatchers.bukkit] to call the specified suspending block,
 *      suspend until completion and return the result.
 * * 使用 Bukkit 同步调度程序上下文 [Dispatchers.bukkit] 调用指定的挂起块, 挂起直到完成然后返回结果.
 */
suspend inline fun <T> Plugin.withBukkit(
  crossinline block: SuspendApplicatorFunction<CoroutineScope, T>
): T = withContext(Dispatchers.bukkit(this)) {
  block()
}

/**
 * * Use the Bukkit asynchronous dispatcher context to create a new coroutine and return its future results as an implementation of [Deferred].
 * * 使用 Bukkit 异步调度上下文来创建新的协同程序并将其未来结果作为 [Deferred] 的实现返回.
 */
suspend inline fun <T> Plugin.withBukkitAsync(
  crossinline block: SuspendApplicatorFunction<CoroutineScope, T>
): Deferred<T> = GlobalScope.async(Dispatchers.bukkit(this, State.ASYNC)) {
  block()
}

/**
 * ## BukkitDispatcher (Bukkit 协程调度程序)
 *
 * @see [CoroutineDispatcher]
 * @author lgou2w
 */
class BukkitDispatcher(
  /**
   * * The plugin object of this coroutine dispatcher.
   * * 此协程调度程序的插件对象.
   */
  val plugin: Plugin,
  /**
   * * The state object of this coroutine dispatcher.
   * * 此协程调度程序的状态对象.
   */
  val state: State
) : CoroutineDispatcher() {

  override fun dispatch(
    context: CoroutineContext,
    block: Runnable
  ) {
    if (state != State.ASYNC && Bukkit.isPrimaryThread())
      block.run()
    else {
      when (state) {
        State.SYNC -> Bukkit.getScheduler().runTask(plugin, block)
        State.ASYNC -> Bukkit.getScheduler().runTaskAsynchronously(plugin, block)
      }
    }
  }
}
