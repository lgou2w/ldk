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

import com.lgou2w.ldk.common.Consumer
import com.lgou2w.ldk.common.Runnable
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

internal interface BukkitCoroutineTask {

    var currentTask : BukkitTask?

    fun wait(tick: Long, task: Consumer<Long>)

    fun yield(task: Consumer<Long>)

    fun switchState(state: State, task: Consumer<Boolean>)

    fun newState(state: State, task: Runnable)
}

internal class NonRepeatingBukkitCoroutineTask(
        private val plugin: Plugin
) : BukkitCoroutineTask {

    override var currentTask: BukkitTask? = null

    override fun wait(tick: Long, task: Consumer<Long>) {
        runTaskLater(State.currentState(), tick) { task(tick) }
    }

    override fun yield(task: Consumer<Long>) {
        wait(0, task)
    }

    override fun switchState(state: State, task: Consumer<Boolean>) {
        val currentState = State.currentState()
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
            State.SYNC -> Bukkit.getScheduler().runTask(plugin, task)
            State.ASYNC -> Bukkit.getScheduler().runTaskAsynchronously(plugin, task)
        }
    }

    private fun runTaskLater(type: State, tick: Long, task: Runnable) {
        currentTask = when (type) {
            State.SYNC -> Bukkit.getScheduler().runTaskLater(plugin, task, tick)
            State.ASYNC -> Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, tick)
        }
    }
}

internal class RepeatingBukkitCoroutineTask(
        private val plugin: Plugin,
        private val interval: Long
) : BukkitCoroutineTask {

    override var currentTask: BukkitTask? = null
    private var nextContinuation : RepetitionContinuation? = null

    override fun wait(tick: Long, task: Consumer<Long>) {
        nextContinuation = RepetitionContinuation(task, tick)
    }

    override fun yield(task: Consumer<Long>) {
        nextContinuation = RepetitionContinuation(task)
    }

    override fun switchState(state: State, task: Consumer<Boolean>) {
        val currentState = State.currentState()
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
            State.SYNC -> Bukkit.getScheduler().runTaskTimer(plugin, task, 0L, interval)
            State.ASYNC -> Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, 0L, interval)
        }
    }

    private class RepetitionContinuation(val resume: Consumer<Long>, val delay: Long = 0) {

        var passedTick = 0L
        private var resumed = false

        fun tryResume(tick: Long) {
            if (resumed)
                throw IllegalStateException("Already resumed.")
            passedTick += tick
            if (passedTick >= delay) {
                resumed = true
                resume(passedTick)
            }
        }
    }
}

