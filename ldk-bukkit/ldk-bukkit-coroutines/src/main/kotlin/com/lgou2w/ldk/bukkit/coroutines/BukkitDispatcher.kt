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

@JvmOverloads
fun Dispatchers.bukkit(plugin: Plugin, state: State = State.SYNC)
        = BukkitDispatcher(plugin, state)

@JvmOverloads
fun Plugin.launcher(
        initializeState: State = State.SYNC,
        block: SuspendApplicator<BukkitCoroutineFactory>
): Job = BukkitCoroutineFactory(this).launcher(initializeState, block)

fun Plugin.launcherAsync(block: SuspendApplicator<BukkitCoroutineFactory>): Job
        = BukkitCoroutineFactory(this).launcher(State.ASYNC, block)

suspend inline fun <T> Plugin.withBukkit(
        crossinline block: SuspendApplicatorFunction<CoroutineScope, T>
): T = withContext(Dispatchers.bukkit(this)) {
    block()
}

suspend inline fun <T> Plugin.withBukkitAsync(
        crossinline block: SuspendApplicatorFunction<CoroutineScope, T>
): Deferred<T> = GlobalScope.async(Dispatchers.bukkit(this, State.ASYNC)) {
    block()
}

class BukkitDispatcher(
        val plugin: Plugin,
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
