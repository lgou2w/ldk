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

package com.lgou2w.ldk.coroutines

import com.lgou2w.ldk.common.SuspendApplicator
import com.lgou2w.ldk.common.SuspendApplicatorFunction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * ## CoroutineFactory (协程工厂)
 *
 * @see [CoroutineFactoryBase]
 * @see [SimpleCoroutineFactory]
 * @author lgou2w
 */
interface CoroutineFactory {

    /**
     * * The dispatcher provider for this coroutine factory.
     * * 此协程工厂的调度程序提供者.
     */
    val provider : DispatcherProvider

    /**
     * * The dispatcher context for this coroutine factory.
     * * 此协程工厂的调度程序上下文.
     */
    val context : CoroutineContext

    /**
     * * Launches new coroutine without blocking current thread and returns a reference to the coroutine as a [Job].
     * * 在不阻塞当前线程的情况下启动新的协同程序, 并将协程的引用作为 [Job] 返回.
     *
     * @see [kotlinx.coroutines.launch]
     */
    fun launch(block: SuspendApplicator<CoroutineFactory>): Job

    // Operating function

    /**
     * * Calls the specified suspending block with the dispatcher context [context] of the current coroutine factory,
     *      suspends until it completes and returns the result.
     * * 使用当前协程工厂的调度程序上下文 [context] 调用指定的挂起块, 挂起直到完成然后返回结果.
     *
     * @see [kotlinx.coroutines.withContext]
     */
    suspend fun <T> with(block: SuspendApplicatorFunction<CoroutineScope, T>): T

    /**
     * * Calls the specified suspending block with the given dispatch context [ctx], suspends until it completes and returns the result.
     * * 使用给定的调度上下文 [ctx] 调用指定的挂起块, 挂起直到完成然后返回结果.
     *
     * @see [kotlinx.coroutines.withContext]
     */
    suspend fun <T> with(ctx: CoroutineContext, block: SuspendApplicatorFunction<CoroutineScope, T>): T

    /**
     * * Use the dispatch context [context] of the current coroutine factory to create a new coroutine
     *      and return its future results as an implementation of [Deferred].
     * * 使用当前协程工厂的调度程序上下文 [context] 来创建新的协同程序并将其未来结果作为 [Deferred] 的实现返回.
     *
     * @see [kotlinx.coroutines.async]
     */
    fun <T> async(block: SuspendApplicatorFunction<CoroutineScope, T>): Deferred<T>

    /**
     * * Create a new coroutine with the given dispatch context [ctx] and return its future results as an implementation of [Deferred].
     * * 使用给定的调度上下文 [ctx] 来创建新的协同程序并将其未来结果作为 [Deferred] 的实现返回.
     *
     * @see [kotlinx.coroutines.async]
     */
    fun <T> async(ctx: CoroutineContext, block: SuspendApplicatorFunction<CoroutineScope, T>): Deferred<T>
}
