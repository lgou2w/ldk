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

package com.lgou2w.ldk.fx.coroutines

import com.lgou2w.ldk.common.SuspendApplicatorFunction
import com.lgou2w.ldk.coroutines.CoroutineFactoryBase
import com.lgou2w.ldk.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FxCoroutineFactory : CoroutineFactoryBase(FxDispatcherProvider.INSTANCE)
class FxDispatcherProvider private constructor() : DispatcherProvider {

    override val dispatcher : CoroutineContext
        get() = Dispatchers.JavaFx

    companion object {
        @JvmField
        val INSTANCE = FxDispatcherProvider()
    }
}

suspend inline fun <T> withFx(crossinline block: SuspendApplicatorFunction<CoroutineScope, T>): T {
    return withContext(Dispatchers.JavaFx) {
        block()
    }
}
