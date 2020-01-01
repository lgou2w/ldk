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

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import java.lang.reflect.Method
import java.util.Collections
import java.util.WeakHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Mouse
 * @since LDK 0.1.9
 */
class ASMEventListenerFactory private constructor() : EventListenerFactory {

  private val classDefiner = SafeClassDefiner()
  private val uniqueId = AtomicInteger(1)
  private val cachedWrappers : MutableMap<Method, Class<*>> = HashMap()

  override fun create(eventType: Class<*>, owner: Any, method: Method, isStatic: Boolean): EventListener {
    val constructor = createWrapper(eventType, owner, method, isStatic).constructors[0]
    val instance = if (isStatic) constructor.newInstance() else constructor.newInstance(owner)
    return instance as EventListener
  }

  private fun createWrapper(eventType: Class<*>, owner: Any, method: Method, isStatic: Boolean): Class<*> {
    var clazz = cachedWrappers[method]
    if (clazz == null) {
      val ownerType = if (isStatic) owner as Class<*> else owner.javaClass
      val declClassName = Type.getInternalName(method.declaringClass)
      val declClassDesc = Type.getDescriptor(method.declaringClass)
      val eventName = Type.getInternalName(eventType)
      val methodName = method.name
      val methodDesc = Type.getMethodDescriptor(method)
      val wrapperName = getUniqueName(ownerType.simpleName, methodName, eventType.simpleName)
      val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES).apply {
        visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, wrapperName, null,
          "java/lang/Object", arrayOf(EVENT_LISTENER_INTERFACE_NAME)
        )
        visitSource(".dynamic", null)
        if (!isStatic) {
          visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL, "owner", declClassDesc, null, null)
            .visitEnd()
        }
        visitMethod(Opcodes.ACC_PUBLIC, "<init>",
          if (isStatic) "()V" else "($declClassDesc)V",
          if (isStatic) "()V" else "($declClassDesc)V",
          null
        ).apply {
          visitCode()
          visitVarInsn(Opcodes.ALOAD, 0)
          visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
          if (!isStatic) {
            visitVarInsn(Opcodes.ALOAD, 0)
            visitVarInsn(Opcodes.ALOAD, 1)
            visitFieldInsn(Opcodes.PUTFIELD, wrapperName, "owner", declClassDesc)
          }
          visitInsn(Opcodes.RETURN)
          visitMaxs(2, 2)
          visitEnd()
        }
        visitMethod(Opcodes.ACC_PUBLIC, "post", "($EVENT_TYPE_DESC)V", null, arrayOf("java/lang/Exception")).apply {
          visitCode()
          if (!isStatic) {
            visitVarInsn(Opcodes.ALOAD, 0)
            visitFieldInsn(Opcodes.GETFIELD, wrapperName, "owner", declClassDesc)
          }
          visitVarInsn(Opcodes.ALOAD, 1)
          visitTypeInsn(Opcodes.CHECKCAST, eventName)
          visitMethodInsn(
            if (isStatic) Opcodes.INVOKESTATIC else Opcodes.INVOKEVIRTUAL,
            declClassName, methodName, methodDesc, false
          )
          visitInsn(Opcodes.RETURN)
          visitMaxs(2, 2)
          visitEnd()
        }
        visitEnd()
      }
      val byteCode = classWriter.toByteArray()
      clazz = classDefiner.defineClass(ownerType.classLoader, wrapperName, byteCode)
      cachedWrappers[method] = clazz
    }
    return clazz
  }

  private fun getUniqueName(ownerName: String, handlerName: String, eventName: String): String
    = "ASMDynamicListener_${Integer.toHexString(System.identityHashCode(this))
  }_${ownerName}_${handlerName}_${eventName}_${uniqueId.getAndIncrement()}"

  companion object {

    @JvmStatic fun create() = ASMEventListenerFactory()

    private val EVENT_LISTENER_INTERFACE_NAME = EventListener::class.java.name.replace(".", "/")
    private val EVENT_TYPE_DESC = Type.getDescriptor(Event::class.java)

    private class SafeClassDefiner {

      companion object {
        @JvmStatic private val loaders : MutableMap<ClassLoader, GeneratedClassLoader>
          = Collections.synchronizedMap(WeakHashMap())
      }

      fun defineClass(parent: ClassLoader, name: String, data: ByteArray): Class<*> {
        val loader = loaders.getOrPut(parent) { GeneratedClassLoader(parent) }
        return synchronized (loader.getClassLoadingLock(name)) {
          if (loader.hasClass(name))
            throw IllegalStateException("$name already defined")
          val clazz = loader.define(name, data)
          assert(clazz.name == name)
          clazz
        }
      }
    }

    private class GeneratedClassLoader(
      parent: ClassLoader
    ) : ClassLoader(parent) {

      companion object {
        init {
          registerAsParallelCapable()
        }
      }

      fun define(name: String, data: ByteArray): Class<*> {
        return synchronized (getClassLoadingLock(name)) {
          assert(!hasClass(name))
          val clazz = defineClass(name, data, 0, data.size)
          resolveClass(clazz)
          clazz
        }
      }

      public override fun getClassLoadingLock(className: String?): Any {
        return super.getClassLoadingLock(className)
      }

      fun hasClass(name: String): Boolean {
        return synchronized (getClassLoadingLock(name)) {
          try {
            Class.forName(name)
            true
          } catch (e: ClassNotFoundException) {
            false
          }
        }
      }
    }
  }
}
