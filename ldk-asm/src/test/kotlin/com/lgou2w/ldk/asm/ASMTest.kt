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

package com.lgou2w.ldk.asm

import org.junit.Ignore
import org.junit.Test
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

class ASMTest {

    @Test
    @Ignore
    fun testASM() {
        val classWriter = ClassWriter(Opcodes.ASM6)
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "HelloWorld", null, "java/lang/Object", null)
        val constructor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
        constructor.visitCode()
        constructor.visitVarInsn(Opcodes.ALOAD, 0)
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        constructor.visitInsn(Opcodes.RETURN)
        constructor.visitMaxs(1, 1)
        constructor.visitEnd()
        val sayHello = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "sayHello", "()V", null, null)
        sayHello.visitCode()
        sayHello.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        sayHello.visitLdcInsn("HelloWorld")
        sayHello.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false)
        sayHello.visitInsn(Opcodes.RETURN)
        sayHello.visitMaxs(2, 1)
        sayHello.visitEnd()
        val main = classWriter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null)
        main.visitCode()
        main.visitTypeInsn(Opcodes.NEW, "HelloWorld")
        main.visitInsn(Opcodes.DUP)
        main.visitMethodInsn(Opcodes.INVOKESPECIAL, "HelloWorld", "<init>", "()V", false)
        main.visitVarInsn(Opcodes.ASTORE, 1)
        main.visitVarInsn(Opcodes.ALOAD, 1)
        main.visitMethodInsn(Opcodes.INVOKESPECIAL, "HelloWorld", "sayHello", "()V", false)
        main.visitInsn(Opcodes.RETURN)
        main.visitMaxs(2, 2)
        main.visitEnd()
        classWriter.visitEnd()
        val byteArray = classWriter.toByteArray()
        val clazz = ASMClassLoader.ofInstance().defineClass("HelloWorld", byteArray)
        val method = clazz.getMethod("main", Array<String>::class.java)
        method.invoke(null, emptyArray<String>())
    }
}
