/*
* MIT License
*
* Copyright (c) 2018 Mouse
*
* https://github.com/Mouse0w0/EventBus
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

/*
 * Copyright (C) 2019-2020 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.eventbus

/*
 *  Modify: Mouse EventBus Java -> Kotlin
 *  by lgou2w on 09/04/2019
 */

import java.util.function.Consumer

/**
 * @author Mouse
 * @since LDK 0.1.9
 */
interface EventBus {

  fun post(event: Event): Boolean

  fun register(target: Any)

  fun unregister(target: Any)

  fun <T : Event> addListener(consumer: Consumer<T>)

  fun <T : Event> addListener(order: Order, consumer: Consumer<T>)

  fun <T : Event> addListener(order: Order, receiveCancelled: Boolean, consumer: Consumer<T>)

  fun <T : Event> addListener(order: Order, receiveCancelled: Boolean, eventType: Class<T>, consumer: Consumer<T>)

  fun <T, G> addGenericListener(genericType: Class<G>, consumer: Consumer<T>) where T : GenericEvent<G>

  fun <T, G> addGenericListener(genericType: Class<G>, order: Order, consumer: Consumer<T>) where T : GenericEvent<G>

  fun <T, G> addGenericListener(genericType: Class<G>, order: Order, receiveCancelled: Boolean, consumer: Consumer<T>) where T : GenericEvent<G>

  fun <T, G> addGenericListener(genericType: Class<G>, order: Order, receiveCancelled: Boolean, eventType: Class<T>, consumer: Consumer<T>) where T : GenericEvent<G>
}
