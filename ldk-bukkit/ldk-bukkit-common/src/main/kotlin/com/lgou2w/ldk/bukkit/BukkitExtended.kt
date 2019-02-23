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

package com.lgou2w.ldk.bukkit

import com.lgou2w.ldk.common.Callable
import com.lgou2w.ldk.common.Runnable
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import java.util.concurrent.CompletableFuture
import java.util.concurrent.FutureTask

/**************************************************************************
 *
 * org.bukkit.plugin.Plugin RunTask Extended
 *
 **************************************************************************/

fun Plugin.runTask(task: Runnable) : BukkitTask
        = Bukkit.getScheduler().runTask(this, task)

fun Plugin.runTaskLater(task: Runnable, delay: Long) : BukkitTask
        = Bukkit.getScheduler().runTaskLater(this, task, delay)

fun Plugin.runTaskTimer(task: Runnable, delay: Long, period: Long) : BukkitTask
        = Bukkit.getScheduler().runTaskTimer(this, task, delay, period)

fun Plugin.runTaskAsync(task: Runnable) : BukkitTask
        = Bukkit.getScheduler().runTaskAsynchronously(this, task)

fun Plugin.runTaskAsyncLater(task: Runnable, delay: Long) : BukkitTask
        = Bukkit.getScheduler().runTaskLaterAsynchronously(this, task, delay)

fun Plugin.runTaskAsyncTimer(task: Runnable, delay: Long, period: Long) : BukkitTask
        = Bukkit.getScheduler().runTaskTimerAsynchronously(this, task, delay, period)

fun BukkitTask?.cancelTask()
        = this?.cancel()

/**************************************************************************
 *
 * org.bukkit.plugin.Plugin CallTask Future Extended
 *
 **************************************************************************/

private fun <T> Plugin.callTaskFuture(callback: Callable<T>, delay: Long = 0L, async: Boolean = false) : CompletableFuture<T> {
    val future = CompletableFuture<T>()
    val futureTask = FutureTask(callback)
    val task : Runnable = {
        try {
            futureTask.run()
            future.complete(futureTask.get())
        } catch (e: Exception) {
            future.completeExceptionally(e)
        }
    }
    if (delay <= 0) {
        if (async) runTaskAsync(task)
        else {
            // SEE -> https://github.com/lgou2w/ldk/issues/32
            // blocking server threads, Until the task is completed
            if (Bukkit.isPrimaryThread())
                task.invoke()
            else
                runTask(task)
        }
    } else {
        // blocking server threads after waiting for a delay, Until the task is completed
        if (async) runTaskAsyncLater(task, delay)
        else runTaskLater(task, delay)
    }
    return future
}

fun <T> Plugin.callTaskFuture(callback: Callable<T>) : CompletableFuture<T>
        = callTaskFuture(callback, 0L)

fun <T> Plugin.callTaskFutureLater(callback: Callable<T>, delay: Long) : CompletableFuture<T>
        = callTaskFuture(callback, delay)

fun <T> Plugin.callTaskFutureAsync(callback: Callable<T>) : CompletableFuture<T>
        = callTaskFuture(callback, 0L, true)

fun <T> Plugin.callTaskFutureAsyncLater(callback: Callable<T>, delay: Long) : CompletableFuture<T>
        = callTaskFuture(callback, delay, true)

/**************************************************************************
 *
 * org.bukkit.plugin.Plugin Listener Extended
 *
 **************************************************************************/

fun Plugin.registerListener(listener: Listener)
        = Bukkit.getPluginManager().registerEvents(listener, this)

/**
 * @since LDK 0.1.7-rc5
 */
fun Plugin.unregisterListener(listener: Listener)
        = HandlerList.unregisterAll(listener)

/**
 * @since LDK 0.1.7-rc5
 */
fun Plugin.unregisterListeners()
        = HandlerList.unregisterAll(this)
