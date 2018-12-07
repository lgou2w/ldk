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

package com.lgou2w.ldk.bukkit.rx

import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import java.util.concurrent.TimeUnit

class BukkitRxScheduler private constructor(
        private val plugin: Plugin,
        private val isAsync: Boolean
) : Scheduler() {

    companion object {

        @JvmStatic
        fun of(plugin: Plugin)
                = BukkitRxScheduler(plugin, false)

        @JvmStatic
        fun ofAsync(plugin: Plugin): BukkitRxScheduler
                = BukkitRxScheduler(plugin, true)

        @JvmStatic
        private fun scheduleOnBukkit(plugin: Plugin, command: Runnable, isAsync: Boolean, delay: Long = 0L, period: Long = -1): BukkitTask {
            val scheduler = Bukkit.getScheduler()
            return when (isAsync) {
                true-> {
                    if (period != -1L)
                        scheduler.runTaskTimerAsynchronously(plugin, command, delay, period)
                    else
                        scheduler.runTaskLaterAsynchronously(plugin, command, delay)
                }
                false -> {
                    if (period != -1L)
                        scheduler.runTaskTimer(plugin, command, delay, period)
                    else
                        scheduler.runTaskLater(plugin, command, delay)
                }
            }
        }

        private fun unitToTick(delay: Long, unit: TimeUnit): Long {
            val millis = unit.toMillis(delay)
            return Math.round(millis * 0.02)
        }
    }

    override fun createWorker(): Worker {
        return BukkitWorker()
    }

    private inner class BukkitWorker : Worker() {

        private val compositeDisposable = CompositeDisposable()

        override fun isDisposed(): Boolean {
            return compositeDisposable.isDisposed
        }

        override fun schedule(run: Runnable): Disposable {
            val task = scheduleOnBukkit(plugin, run, isAsync)
            val disposable = BukkitDisposableTask(task)
            compositeDisposable.add(disposable)
            return disposable
        }

        override fun schedule(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            val task = scheduleOnBukkit(plugin, run, isAsync, unitToTick(delay, unit))
            val disposable = BukkitDisposableTask(task)
            compositeDisposable.add(disposable)
            return disposable
        }

        override fun schedulePeriodically(run: Runnable, initialDelay: Long, period: Long, unit: TimeUnit): Disposable {
            val task = scheduleOnBukkit(plugin, run, isAsync, unitToTick(initialDelay, unit), unitToTick(period, unit))
            val disposable = BukkitDisposableTask(task)
            compositeDisposable.add(disposable)
            return disposable
        }

        override fun dispose() {
            compositeDisposable.dispose()
        }

        private inner class BukkitDisposableTask(
                private val task: BukkitTask
        ) : Disposable {

            private var isDisposed = false

            override fun isDisposed(): Boolean {
                return isDisposed && !task.owner.server.scheduler.isCurrentlyRunning(task.taskId)
            }

            override fun dispose() {
                isDisposed = true
                task.cancel()
            }
        }
    }
}
