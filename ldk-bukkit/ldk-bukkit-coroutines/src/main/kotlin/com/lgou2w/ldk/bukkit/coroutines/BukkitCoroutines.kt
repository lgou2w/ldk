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

import com.lgou2w.ldk.common.Consumer
import com.lgou2w.ldk.common.Runnable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BukkitCoroutines private constructor(
        val plugin: Plugin,
        val scheduler: BukkitScheduler
) {

    companion object {

        @JvmStatic
        private fun currentState() : State
                = if (Bukkit.isPrimaryThread()) State.SYNC else State.ASYNC

        @JvmStatic
        fun coroutines(
                plugin: Plugin,
                scheduler: BukkitScheduler,
                initializeState: State = State.SYNC,
                block: suspend BukkitCoroutines.() -> Unit
        ) : CoroutinesTask {
            val coroutines = BukkitCoroutines(plugin, scheduler)
            GlobalScope.launch(Dispatchers.Unconfined) {
                try {
                    coroutines.start(initializeState)
                    coroutines.block()
                } finally {
                    coroutines.clear()
                }
            }
            return CoroutinesTaskImpl(coroutines)
        }
    }

    private var proxy: Scheduler = TaskScheduler(plugin, scheduler)

    val currentTask: BukkitTask?
        get() = proxy.currentTask

    val isRepeating: Boolean
        get() = proxy is RepeatingTaskScheduler

    internal suspend fun start(type: State) = suspendCoroutine<Unit> { continuation ->
        proxy.switchState(type) { continuation.resume(Unit) }
    }

    internal fun clear() {
        currentTask?.cancel()
    }

    suspend fun wait(tick: Long) : Long = suspendCoroutine { continuation ->
        proxy.wait(tick) { continuation.resume(it) }
    }

    suspend fun yield() : Long = suspendCoroutine { continuation ->
        proxy.yield() { continuation.resume(it) }
    }

    suspend fun switchState(type: State) : Boolean = suspendCoroutine { continuation ->
        proxy.switchState(type) { continuation.resume(it) }
    }

    suspend fun newState(type: State) : Unit = suspendCoroutine { continuation ->
        proxy.newState(type) { continuation.resume(Unit) }
    }

    suspend fun repeating(interval: Long) : Long = suspendCoroutine { continuation ->
        proxy = RepeatingTaskScheduler(plugin, scheduler, interval)
        proxy.newState(currentState()) { continuation.resume(0L) }
    }

    private class CoroutinesTaskImpl(private val coroutines: BukkitCoroutines) : CoroutinesTask {

        override val plugin: Plugin
            get() = coroutines.plugin

        override val currentTask: BukkitTask?
            get() = coroutines.currentTask

        override val isSync: Boolean
            get() = coroutines.currentTask?.isSync ?: false

        override val isAsync: Boolean
            get() = !(coroutines.currentTask?.isSync ?: true)

        override fun cancel() {
            coroutines.currentTask!!.cancel()
        }
    }

    private interface Scheduler {

        var currentTask : BukkitTask?

        fun wait(tick: Long, task: Consumer<Long>)

        fun yield(task: Consumer<Long>)

        fun switchState(state: State, task: Consumer<Boolean>)

        fun newState(state: State, task: Runnable)
    }

    private class TaskScheduler(
            val plugin: Plugin,
            val scheduler: BukkitScheduler
    ) : Scheduler {

        override var currentTask: BukkitTask? = null

        override fun wait(tick: Long, task: Consumer<Long>) {
            runTaskLater(currentState(), tick) { task(tick) }
        }

        override fun yield(task: Consumer<Long>) {
            wait(0, task)
        }

        override fun switchState(state: State, task: Consumer<Boolean>) {
            val currentState = currentState()
            if (state == currentState)
                task(false)
            else
                newState(state) { task(true) }
        }

        override fun newState(state: State, task: Runnable) {
            runTask(state, task)
        }

        private fun runTask(state: State, task: Runnable) {
            currentTask = when (state) {
                State.SYNC -> scheduler.runTask(plugin, task)
                State.ASYNC -> scheduler.runTaskAsynchronously(plugin, task)
            }
        }

        private fun runTaskLater(type: State, tick: Long, task: Runnable) {
            currentTask = when (type) {
                State.SYNC -> scheduler.runTaskLater(plugin, task, tick)
                State.ASYNC -> scheduler.runTaskLaterAsynchronously(plugin, task, tick)
            }
        }
    }

    private class RepeatingTaskScheduler(
            val plugin: Plugin,
            val scheduler: BukkitScheduler,
            val interval: Long
    ) : Scheduler {

        override var currentTask: BukkitTask? = null
        private var nextContinuation : RepetitionContinuation? = null

        override fun wait(tick: Long, task: Consumer<Long>) {
            nextContinuation = RepetitionContinuation(task, tick)
        }

        override fun yield(task: Consumer<Long>) {
            nextContinuation = RepetitionContinuation(task)
        }

        override fun switchState(state: State, task: Consumer<Boolean>) {
            val currentState = currentState()
            if (state == currentState)
                task(false)
            else
                newState(state) { task(true) }
        }

        override fun newState(state: State, task: Runnable) {
            yield() { task() }
            runTaskTimer(state)
        }

        private fun runTaskTimer(state: State) {
            currentTask?.cancel()
            val task : Runnable = { nextContinuation?.tryResume(interval) }
            currentTask = when (state) {
                State.SYNC -> scheduler.runTaskTimer(plugin, task, 0L, interval)
                State.ASYNC -> scheduler.runTaskTimerAsynchronously(plugin, task, 0L, interval)
            }
        }
    }

    private class RepetitionContinuation(val resume: Consumer<Long>, val delay: Long = 0) {

        var passedTick = 0L
        private var resumed = false

        fun tryResume(passedTick: Long) {
            if (resumed)
                throw IllegalStateException("Already resumed.")
            this.passedTick += passedTick
            if (this.passedTick >= delay) {
                resumed = true
                resume(this.passedTick)
            }
        }
    }
}
