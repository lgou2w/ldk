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

/**
 * * Schedule a task that will run on the next server `Tick`.
 * * 调度一个将在下一个服务器 `Tick` 运行的任务.
 *
 * @see [org.bukkit.scheduler.BukkitScheduler.runTask]
 */
fun Plugin.runTask(task: Runnable): BukkitTask
        = Bukkit.getScheduler().runTask(this, task)

/**
 * * Schedule a task that will run after the [delay] specified in the server `Tick`.
 * * 调度一个将在服务器 `Tick` 中指定延迟 [delay] 后运行的任务.
 *
 * @see [org.bukkit.scheduler.BukkitScheduler.runTaskLater]
 */
fun Plugin.runTaskLater(task: Runnable, delay: Long): BukkitTask
        = Bukkit.getScheduler().runTaskLater(this, task, delay)

/**
 * * Schedule a task that will run repeatedly with the specified [period] after the [delay] is specified in the server `Tick`.
 * * 调度一个将在服务器 `Tick` 中指定延迟 [delay] 后以指定周期 [period] 重复运行的任务.
 *
 * @see [org.bukkit.scheduler.BukkitScheduler.runTaskTimer]
 */
fun Plugin.runTaskTimer(task: Runnable, delay: Long, period: Long): BukkitTask
        = Bukkit.getScheduler().runTaskTimer(this, task, delay, period)

/**
 * * Schedule a task that will run in an asynchronous thread.
 * * 调度一个将在异步线程中运行的任务.
 *
 * @see [org.bukkit.scheduler.BukkitScheduler.runTaskAsynchronously]
 */
fun Plugin.runTaskAsync(task: Runnable): BukkitTask
        = Bukkit.getScheduler().runTaskAsynchronously(this, task)

/**
 * * Schedule a task that will run after the [delay] is specified in the asynchronous thread.
 * * 调度一个将在异步线程中指定延迟 [delay] 后运行的任务.
 *
 * @see [org.bukkit.scheduler.BukkitScheduler.runTaskLaterAsynchronously]
 */
fun Plugin.runTaskAsyncLater(task: Runnable, delay: Long): BukkitTask
        = Bukkit.getScheduler().runTaskLaterAsynchronously(this, task, delay)

/**
 * * Schedule a task that will run repeatedly in the specified [period] after specifying the [delay] in the asynchronous thread.
 * * 调度一个将在异步线程中指定延迟 [delay] 后以指定周期 [period] 重复运行的任务.
 *
 * @see [org.bukkit.scheduler.BukkitScheduler.runTaskTimerAsynchronously]
 */
fun Plugin.runTaskAsyncTimer(task: Runnable, delay: Long, period: Long): BukkitTask
        = Bukkit.getScheduler().runTaskTimerAsynchronously(this, task, delay, period)

/**
 * * Cancel the given task.
 * * 将给定的任务取消.
 *
 * @see [BukkitTask.cancel]
 */
fun BukkitTask?.cancelTask()
        = this?.cancel()

/**************************************************************************
 *
 * org.bukkit.plugin.Plugin CallTask Future Extended
 *
 **************************************************************************/

private fun <T> Plugin.callTaskFuture(
        callback: Callable<T>,
        delay: Long = 0L,
        async: Boolean = false
): CompletableFuture<T> {
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

/**
 * * Call the method on the server thread and return the [CompletableFuture] object.
 *      Note: that this will blocking the server thread until the task is completed.
 * * 在服务器线程调用方法并返回 [CompletableFuture] 对象.
 *      注意: 这将会阻塞服务器线程, 直到任务完成.
 */
fun <T> Plugin.callTaskFuture(callback: Callable<T>): CompletableFuture<T>
        = callTaskFuture(callback, 0L)

/**
 * * Call the method and return a [CompletableFuture] object after the server thread has given the [delay].
 *      Note: that this will blocking the server thread until the task is completed.
 * * 在服务器线程以给定的延迟 [delay] 后调用方法并返回 [CompletableFuture] 对象.
 *      注意: 这将会阻塞服务器线程, 直到任务完成.
 */
fun <T> Plugin.callTaskFutureLater(callback: Callable<T>, delay: Long): CompletableFuture<T>
        = callTaskFuture(callback, delay)

/**
 * * Call the method on an asynchronous thread and return a [CompletableFuture] object.
 * * 在异步线程调用方法并返回 [CompletableFuture] 对象.
 */
fun <T> Plugin.callTaskFutureAsync(callback: Callable<T>): CompletableFuture<T>
        = callTaskFuture(callback, 0L, true)

/**
 * * Call the method and return a [CompletableFuture] object after the asynchronous thread has given the [delay].
 * * 在异步线程以给定的延迟 [delay] 后调用方法并返回 [CompletableFuture] 对象.
 */
fun <T> Plugin.callTaskFutureAsyncLater(callback: Callable<T>, delay: Long): CompletableFuture<T>
        = callTaskFuture(callback, delay, true)

/**************************************************************************
 *
 * org.bukkit.plugin.Plugin Listener Extended
 *
 **************************************************************************/

/**
 * * Register the given event [listener] to the given plugin.
 * * 将给定的事件监听器 [listener] 注册到给定的插件中.
 *
 * @see [org.bukkit.plugin.PluginManager.registerEvents]
 */
fun Plugin.registerListener(listener: Listener)
        = Bukkit.getPluginManager().registerEvents(listener, this)

/**
 * * Unregister the given event [listener] from the given plugin.
 * * 将给定的事件监听器 [listener] 从给定的插件中注销.
 *
 * @since LDK 0.1.7-rc5
 */
fun Plugin.unregisterListener(listener: Listener)
        = HandlerList.unregisterAll(listener)

/**
 * * Unregister all event listeners from the given plugin.
 * * 将给定的插件中所有事件监听器注销.
 *
 * @since LDK 0.1.7-rc5
 */
fun Plugin.unregisterListeners()
        = HandlerList.unregisterAll(this)
