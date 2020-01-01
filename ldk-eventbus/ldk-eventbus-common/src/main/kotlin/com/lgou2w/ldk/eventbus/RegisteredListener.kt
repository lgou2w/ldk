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

import com.lgou2w.ldk.common.Predicate
import java.lang.reflect.Type

/**
 * @author Mouse
 * @since LDK 0.1.9
 */
class RegisteredListener {

  val eventType : Class<*>
  val owner : Any?
  val order : Order
  val genericType : Type?
  val eventListener : EventListener

  private val filter : Predicate<Event>

  constructor(eventType: Class<*>, owner: Any?, order: Order, receiveCancelled: Boolean, genericType: Type?, eventListener: EventListener) {
    this.eventType = eventType
    this.owner = owner
    this.order = order
    this.genericType = genericType
    this.eventListener = eventListener
    this.filter = createFilter(receiveCancelled, genericType != null)
  }

  private fun createFilter(receiveCancelled: Boolean, isGeneric: Boolean): Predicate<Event> {
    if (receiveCancelled) {
      if (isGeneric)
        return this::checkGeneric
      else
        return { true }
    } else {
      if (isGeneric)
        return { event -> checkCancel(event) && checkGeneric(event) }
      else
        return this::checkCancel
    }
  }

  private fun checkCancel(event: Event): Boolean
    = event !is Cancellable || !event.isCancelled

  private fun checkGeneric(event: Event): Boolean
    = (event as GenericEvent<*>).genericType == genericType

  @Throws(Exception::class)
  fun post(event: Event) {
    if (filter(event))
      eventListener.post(event)
  }

}
