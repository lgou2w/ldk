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

package com.lgou2w.ldk.bukkit.coroutines

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

open class BukkitCoroutineFactory(
        val plugin: Plugin
) : CoroutineFactoryBase(CustomizeDispatcherProvider(Dispatchers.Unconfined)) {

    // 强制使用 Unconfined 调度器
    // Mandatory use the Unconfined dispatcher

    private var delegate : BukkitCoroutineTask
            = NonRepeatingBukkitCoroutineTask(plugin)

    @Deprecated("No status.", replaceWith = ReplaceWith("launcher"), level = DeprecationLevel.HIDDEN)
    override fun launch(block: CoroutineFactory.() -> Unit): Job {
        return launcher(State.SYNC) {
            block()
        }
    }

    fun launcher(initializeState: State, block: suspend BukkitCoroutineFactory.() -> Unit) : Job {
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

    suspend fun wait(tick: Long) : Long {
        return suspendCoroutine { continuation ->
            delegate.wait(tick) { value ->
                continuation.resume(value)
            }
        }
    }

    suspend fun yield() : Long {
        return suspendCoroutine { continuation ->
            delegate.yield() { value ->
                continuation.resume(value)
            }
        }
    }

    suspend fun switchState(state: State) : Boolean {
        return suspendCoroutine { continuation ->
            delegate.switchState(state) { value ->
                continuation.resume(value)
            }
        }
    }

    suspend fun newState(state: State) {
        return suspendCoroutine { continuation ->
            delegate.newState(state) {
                continuation.resume(Unit)
            }
        }
    }

    suspend fun repeating(interval: Long) : Long {
        return suspendCoroutine { continuation ->
            delegate = RepeatingBukkitCoroutineTask(plugin, interval)
            delegate.newState(State.currentState()) {
                continuation.resume(0L)
            }
        }
    }
}
