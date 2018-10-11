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

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Delay
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

class BukkitDisplatcher(
        val plugin: Plugin,
        val state: State
) : CoroutineDispatcher(), Delay {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (state != State.ASYNC && Bukkit.isPrimaryThread())
            block.run()
        else {
            when (state) {
                State.SYNC -> Bukkit.getScheduler().runTask(plugin, block)
                State.ASYNC -> Bukkit.getScheduler().runTaskAsynchronously(plugin, block)
            }
        }
    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val block : com.lgou2w.ldk.common.Runnable = { continuation.apply { resumeUndispatched(Unit) } }
        when (state) {
            State.SYNC -> Bukkit.getScheduler().runTaskLater(plugin, block, unitToTick(timeMillis))
            State.ASYNC -> Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, block, unitToTick(timeMillis))
        }
    }

    companion object {
        private fun unitToTick(timeMillis: Long): Long {
            return Math.round(timeMillis * 0.02)
        }
    }
}
