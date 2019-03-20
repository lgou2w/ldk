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

package com.lgou2w.ldk.coroutines

import com.lgou2w.ldk.common.SuspendApplicator
import com.lgou2w.ldk.common.SuspendApplicatorFunction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * ## CoroutineFactoryBase (协程工厂基础)
 *
 * @see [CoroutineFactory]
 * @see [SimpleCoroutineFactory]
 * @author lgou2w
 */
abstract class CoroutineFactoryBase(
        final override val provider: DispatcherProvider
) : CoroutineFactory {

    override val context : CoroutineContext
        get() = provider.dispatcher

    override fun launch(block: SuspendApplicator<CoroutineFactory>): Job {
        return GlobalScope.launch(context) {
            launching()
            block()
        }
    }

    /**
     * * Called after the suspend block of the [launch] coroutine is executed after startup.
     * * 在启动后 [launch] 协同程序的挂起块执行之前被调用.
     */
    protected open suspend fun launching() {
    }

    override suspend fun <T> with(
            block: SuspendApplicatorFunction<CoroutineScope, T>
    ): T = with(context, block)

    override suspend fun <T> with(
            ctx: CoroutineContext,
            block: SuspendApplicatorFunction<CoroutineScope, T>
    ): T = withContext(ctx) {
        block()
    }

    override fun <T> async(
            block: SuspendApplicatorFunction<CoroutineScope, T>
    ): Deferred<T> = async(context, block)

    override fun <T> async(
            ctx: CoroutineContext,
            block: SuspendApplicatorFunction<CoroutineScope, T>
    ): Deferred<T> = GlobalScope.async(ctx) {
        block()
    }
}
