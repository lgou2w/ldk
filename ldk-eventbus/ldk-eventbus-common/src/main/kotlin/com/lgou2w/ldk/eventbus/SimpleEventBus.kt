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

import com.lgou2w.ldk.common.notNull
import com.lgou2w.ldk.reflect.TypeResolver
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.function.Consumer

/**
 * @author Mouse
 * @since LDK 0.1.9
 */
class SimpleEventBus(
  private val eventExceptionHandler: EventExceptionHandler,
  private val eventListenerFactory: EventListenerFactory
) : EventBus {

  companion object {

    @JvmStatic
    fun builder() = Builder()

    class Builder internal constructor() {

      private var eventExceptionHandler : EventExceptionHandler = object : EventExceptionHandler {
        override fun handle(list: ListenerList, listener: RegisteredListener, event: Event, e: Exception) {
          throw EventException("Cannot handle event. EventType: ${event.javaClass.name}")
        }
      }
      private var eventListenerFactory : EventListenerFactory? = null

      fun eventExceptionHandler(eventExceptionHandler: EventExceptionHandler): Builder {
        this.eventExceptionHandler = eventExceptionHandler
        return this
      }

      fun eventListenerFactory(eventListenerFactory: EventListenerFactory): Builder {
        this.eventListenerFactory = eventListenerFactory
        return this
      }

      fun build(): SimpleEventBus {
        val eventListenerFactory = this.eventListenerFactory.notNull("Event listener factory has not been initialized")
        return SimpleEventBus(eventExceptionHandler, eventListenerFactory)
      }
    }
  }

  private val listenerLists : MutableMap<Class<*>, ListenerList> = HashMap()
  private val registeredListeners : MutableMap<Any, List<RegisteredListener>> = HashMap()

  override fun post(event: Event): Boolean {
    val listenerList = getListenerList(event.javaClass)
    for (listener in listenerList.listeners) try {
      listener.post(event)
    } catch (e: Exception) {
      eventExceptionHandler.handle(listenerList, listener, event, e)
    }
    return event is Cancellable && event.isCancelled
  }

  private fun getListenerList(eventType: Class<*>): ListenerList
    = listenerLists.getOrPut(eventType) { createListenerList(eventType) }

  private fun createListenerList(eventType: Class<*>): ListenerList {
    val listenerList = ListenerList(eventType)
    for ((key, value) in listenerLists.entries) {
      if (key.isAssignableFrom(eventType))
        listenerList.addParent(value)
      else if (eventType.isAssignableFrom(key))
        listenerList.addChild(value)
    }
    return listenerList
  }

  override fun register(target: Any) {
    if (registeredListeners.containsKey(target))
      return
    if (target is Class<*>)
      registerClass(target)
    else
      registerObject(target)
  }

  private fun registerObject(obj: Any) {
    val listeners = ArrayList<RegisteredListener>()
    obj.javaClass.methods.asSequence()
      .filter { !Modifier.isStatic(it.modifiers) && it.isAnnotationPresent(Listener::class.java) }
      .map { registerListener(obj, it, false) }
      .forEach { listeners.add(it) }
    registeredListeners[obj] = listeners
  }

  private fun registerClass(clazz: Class<*>) {
    val listeners = ArrayList<RegisteredListener>()
    clazz.methods.asSequence()
      .filter { Modifier.isStatic(it.modifiers) && it.isAnnotationPresent(Listener::class.java) }
      .map { registerListener(clazz, it, true) }
      .forEach { listeners.add(it) }
    registeredListeners[clazz] = listeners
  }

  private fun registerListener(owner: Any, method: Method, isStatic: Boolean): RegisteredListener {
    if (method.parameterCount != 1)
      throw EventException("The count of listener method parameter must be 1. Listener: ${method.declaringClass.name}#${method.name}")
    val eventType = method.parameterTypes[0]
    if (!Event::class.java.isAssignableFrom(eventType))
      throw EventException("The type of parameter of listener method must be Event or its sub class. Listener: ${method.declaringClass.name}#${method.name}")
    if (method.returnType != Void.TYPE)
      throw EventException("The return type of listener method must be void. Listener: ${method.declaringClass.name}#${method.name}")
    if (!Modifier.isPublic(method.modifiers))
      throw EventException("Listener method must be public. Listener: ${method.declaringClass.name}#${method.name}")
    val annotation = method.getAnnotation(Listener::class.java)
    var genericType : Type? = null
    if (GenericEvent::class.java.isAssignableFrom(eventType)) {
      val type = method.genericParameterTypes[0]
      genericType = if (type is ParameterizedType) type.actualTypeArguments[0] else null
      if (genericType is ParameterizedType)
        genericType = genericType.rawType
    }
    try {
      val eventListener = eventListenerFactory.create(eventType, owner, method, isStatic)
      val listener = RegisteredListener(eventType, owner, annotation.order, annotation.receiveCancelled, genericType, eventListener)
      getListenerList(eventType).register(listener)
      return listener
    } catch (e: Exception) {
      throw EventException("Cannot register listener. Listener: ${method.declaringClass.name}#${method.name}")
    }
  }

  override fun unregister(target: Any) {
    if (!registeredListeners.containsKey(target))
      return
    registeredListeners.remove(target)?.forEach { listener ->
      getListenerList(listener.eventType)
        .unregister(listener)
    }
  }

  override fun <T : Event> addListener(consumer: Consumer<T>) {
    addListener(Order.DEFAULT, false, consumer)
  }

  override fun <T : Event> addListener(order: Order, consumer: Consumer<T>) {
    addListener(order, false, consumer)
  }

  override fun <T : Event> addListener(order: Order, receiveCancelled: Boolean, consumer: Consumer<T>) {
    @Suppress("UNCHECKED_CAST")
    val eventType = TypeResolver.resolveRawArgument(Consumer::class.java, consumer.javaClass) as Class<T>
    addListener(order, receiveCancelled, eventType, consumer)
  }

  override fun <T : Event> addListener(order: Order, receiveCancelled: Boolean, eventType: Class<T>, consumer: Consumer<T>) {
    getListenerList(eventType).register(RegisteredListener(
      eventType,
      null,
      order,
      receiveCancelled,
      null,
      object : EventListener {
        override fun post(event: Event) {
          consumer.accept(eventType.cast(event))
        }
      }
    ))
  }

  override fun <T : GenericEvent<G>, G> addGenericListener(genericType: Class<G>, consumer: Consumer<T>) {
    addGenericListener(genericType, Order.DEFAULT, false, consumer)
  }

  override fun <T : GenericEvent<G>, G> addGenericListener(genericType: Class<G>, order: Order, consumer: Consumer<T>) {
    addGenericListener(genericType, order, false, consumer)
  }

  override fun <T : GenericEvent<G>, G> addGenericListener(genericType: Class<G>, order: Order, receiveCancelled: Boolean, consumer: Consumer<T>) {
    @Suppress("UNCHECKED_CAST")
    val eventType = TypeResolver.resolveRawArgument(Consumer::class.java, consumer.javaClass) as Class<T>
    addGenericListener(genericType, order, receiveCancelled, eventType, consumer)
  }

  override fun <T : GenericEvent<G>, G> addGenericListener(genericType: Class<G>, order: Order, receiveCancelled: Boolean, eventType: Class<T>, consumer: Consumer<T>) {
    getListenerList(eventType).register(RegisteredListener(
      eventType,
      null,
      order,
      receiveCancelled,
      genericType,
      object : EventListener {
        override fun post(event: Event) {
          consumer.accept(eventType.cast(event))
        }
      }
    ))
  }
}
