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
import com.lgou2w.ldk.coroutines.CoroutineFactory
import com.lgou2w.ldk.coroutines.CoroutineFactoryBase
import com.lgou2w.ldk.coroutines.CustomizeDispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.bukkit.plugin.Plugin
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * ## BukkitCoroutineFactory (Bukkit 协程工厂)
 *
 * @see [CoroutineFactory]
 * @see [CoroutineFactoryBase]
 * @author lgou2w
 */
open class BukkitCoroutineFactory(
  /**
   * * The plugin object for this Bukkit coroutine factory.
   * * 此 Bukkit 协程工厂的插件对象.
   */
  val plugin: Plugin
) : CoroutineFactoryBase(CustomizeDispatcherProvider(Dispatchers.Unconfined)) {

  // 强制使用 Unconfined 调度器
  // Mandatory use the Unconfined dispatcher

  private var delegate : BukkitCoroutineTask
    = NonRepeatingBukkitCoroutineTask(plugin)

  @Deprecated("No status.", replaceWith = ReplaceWith("launcher"), level = DeprecationLevel.HIDDEN)
  override fun launch(block: SuspendApplicator<CoroutineFactory>): Job {
    return launcher(State.SYNC) {
      block()
    }
  }

  /**
   * * Launches new coroutine with given [initializeState] without blocking current thread and returns a reference to the coroutine as a [Job].
   * * 在不阻塞当前线程的情况下以给定的状态 [initializeState] 启动新的协同程序, 并将协程的引用作为 [Job] 返回.
   *
   * @see [kotlinx.coroutines.launch]
   */
  fun launcher(initializeState: State, block: SuspendApplicator<BukkitCoroutineFactory>): Job {
    return GlobalScope.launch(context) {
      try {
        launching()
        start(initializeState)
        block()
      } finally {
        clean()
      }
    }
  }

  private suspend fun start(initializeState: State) {
    return suspendCoroutine { continuation ->
      delegate.switchState(initializeState) {
        continuation.resume(Unit)
      }
    }
  }

  private fun clean() {
    delegate.currentTask?.cancel()
  }

  /**
   * * Suspend the time specified by the current coroutine context [tick].
   * * 挂起当前协程上下文指定的时间刻 [tick].
   */
  suspend fun wait(tick: Long): Long {
    return suspendCoroutine { continuation ->
      delegate.wait(tick) { value ->
        continuation.resume(value)
      }
    }
  }

  /**
   * * Restore the current coroutine context to the running state.
   * * 恢复当前协程上下文到运行状态.
   */
  suspend fun yield(): Long {
    return suspendCoroutine { continuation ->
      delegate.yield() { value ->
        continuation.resume(value)
      }
    }
  }

  /**
   * * Switch the current coroutine context to the new [state].
   * * 将当前协程上下文切换到新的状态 [state].
   */
  suspend fun switchState(state: State) : Boolean {
    return suspendCoroutine { continuation ->
      delegate.switchState(state) { value ->
        continuation.resume(value)
      }
    }
  }

  /**
   * * Forces the current coroutine context to switch to the new [state].
   * * 强制将当前协程上下文切换到新的状态.
   */
  suspend fun newState(state: State) {
    return suspendCoroutine { continuation ->
      delegate.newState(state) {
        continuation.resume(Unit)
      }
    }
  }

  /**
   * * Switch the current coroutine context to the repeat run state.
   * * 将当前协程上下文切换到重复运行状态.
   */
  suspend fun repeating(interval: Long): Long {
    return suspendCoroutine { continuation ->
      delegate = RepeatingBukkitCoroutineTask(plugin, interval)
      delegate.newState(State.currentState()) {
        continuation.resume(0L)
      }
    }
  }
}
