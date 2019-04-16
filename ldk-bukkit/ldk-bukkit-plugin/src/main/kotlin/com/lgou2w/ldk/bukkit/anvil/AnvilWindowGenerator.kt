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

package com.lgou2w.ldk.bukkit.anvil

import com.lgou2w.ldk.asm.ASMClassGenerator
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes

// FIXME: DEBUG CODE
class AnvilWindowGenerator : ASMClassGenerator {

    override fun generate(): Map<String, ByteArray> {
        val classes = LinkedHashMap<String, ByteArray>(3)
        val anvilWindowImpl = ClassWriter(0x00).apply {
            visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl", null, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowBase", null)
            visitSource("AnvilWindowImpl.java", null)
            visitField(Opcodes.ACC_PRIVATE, "handle", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer;", null, null).visitEnd()
            visitMethod(Opcodes.ACC_PUBLIC, "<init>", "(Lorg/bukkit/plugin/Plugin;)V", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(21, l0)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitVarInsn(Opcodes.ALOAD, 1)
                visitMethodInsn(Opcodes.INVOKESPECIAL, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowBase", "<init>", "(Lorg/bukkit/plugin/Plugin;)V", false)
                val l1 = Label()
                visitLabel(l1)
                visitLineNumber(22, l1)
                visitInsn(Opcodes.RETURN)
                val l2 = Label()
                visitLabel(l2)
                visitLocalVariable("this", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;", null, l0, l2, 0)
                visitLocalVariable("plugin", "Lorg/bukkit/plugin/Plugin;", null, l0, l2, 1)
                visitMaxs(2, 2)
                visitEnd()
            }
            visitMethod(Opcodes.ACC_PUBLIC, "open", "(Lorg/bukkit/entity/Player;)V", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(26, l0)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitVarInsn(Opcodes.ALOAD, 1)
                visitMethodInsn(Opcodes.INVOKESPECIAL, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowBase", "open", "(Lorg/bukkit/entity/Player;)V", false)
                val l1 = Label()
                visitLabel(l1)
                visitLineNumber(27, l1)
                visitVarInsn(Opcodes.ALOAD, 1)
                visitTypeInsn(Opcodes.CHECKCAST, "org/bukkit/craftbukkit/v1_13_R2/entity/CraftPlayer")
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/bukkit/craftbukkit/v1_13_R2/entity/CraftPlayer", "getHandle", "()Lnet/minecraft/server/v1_13_R2/EntityPlayer;", false)
                visitVarInsn(Opcodes.ASTORE, 2)
                val l2 = Label()
                visitLabel(l2)
                visitLineNumber(28, l2)
                visitTypeInsn(Opcodes.NEW, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowTileEntity")
                visitInsn(Opcodes.DUP)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitVarInsn(Opcodes.ALOAD, 2)
                visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/server/v1_13_R2/EntityPlayer", "world", "Lnet/minecraft/server/v1_13_R2/World;")
                visitMethodInsn(Opcodes.INVOKESPECIAL, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowTileEntity", "<init>", "(Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;Lnet/minecraft/server/v1_13_R2/World;)V", false)
                visitVarInsn(Opcodes.ASTORE, 3)
                val l3 = Label()
                visitLabel(l3)
                visitLineNumber(29, l3)
                visitVarInsn(Opcodes.ALOAD, 2)
                visitVarInsn(Opcodes.ALOAD, 3)
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/server/v1_13_R2/EntityPlayer", "openTileEntity", "(Lnet/minecraft/server/v1_13_R2/ITileEntityContainer;)V", false)
                val l4 = Label()
                visitLabel(l4)
                visitLineNumber(30, l4)
                visitInsn(Opcodes.RETURN)
                val l5 = Label()
                visitLabel(l5)
                visitLocalVariable("this", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;", null, l0, l5, 0)
                visitLocalVariable("player", "Lorg/bukkit/entity/Player;", null, l0, l5, 1)
                visitLocalVariable("playerHandle", "Lnet/minecraft/server/v1_13_R2/EntityPlayer;", null, l2, l5, 2)
                visitLocalVariable("anvilWindowTileEntity", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowTileEntity;", null, l3, l5, 3)
                visitMaxs(4, 4)
                visitEnd()
            }
            visitMethod(Opcodes.ACC_PUBLIC, "getInventory", "()Lorg/bukkit/inventory/Inventory;", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(34, l0)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitFieldInsn(Opcodes.GETFIELD, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl", "handle", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer;")
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer", "getBukkitView", "()Lorg/bukkit/craftbukkit/v1_13_R2/inventory/CraftInventoryView;", false)
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/bukkit/craftbukkit/v1_13_R2/inventory/CraftInventoryView", "getTopInventory", "()Lorg/bukkit/inventory/Inventory;", false)
                visitInsn(Opcodes.ARETURN)
                val l1 = Label()
                visitLabel(l1)
                visitLocalVariable("this", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;", null, l0, l1, 0)
                visitMaxs(1, 1)
                visitEnd()
            }
            visitMethod(Opcodes.ACC_STATIC + Opcodes.ACC_SYNTHETIC, "access$002", "(Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer;)Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer;", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(16, l0)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitVarInsn(Opcodes.ALOAD, 1)
                visitInsn(Opcodes.DUP_X1)
                visitFieldInsn(Opcodes.PUTFIELD, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl", "handle", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer;")
                visitInsn(Opcodes.ARETURN)
                val l1 = Label()
                visitLabel(l1)
                visitLocalVariable("x0", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;", null, l0, l1, 0)
                visitLocalVariable("x1", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer;", null, l0, l1, 1)
                visitMaxs(3, 2)
                visitEnd()
            }
            visitInnerClass("com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer", "AnvilWindowImpl", "AnvilWindowContainer", Opcodes.ACC_PRIVATE)
            visitInnerClass("com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowTileEntity", "AnvilWindowImpl", "AnvilWindowTileEntity", Opcodes.ACC_PRIVATE)
        }
            .toByteArray()

        val anvilWindowTileEntity = ClassWriter(0x00).apply {
            visit(Opcodes.V1_8, Opcodes.ACC_SUPER, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowTileEntity", null, "net/minecraft/server/v1_13_R2/BlockAnvil\$TileEntityContainerAnvil", null)
            visitSource("AnvilWindowImpl.java", null)
            visitField(Opcodes.ACC_FINAL + Opcodes.ACC_SYNTHETIC, "this$0", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;", null, null).visitEnd()
            visitMethod(0x00, "<init>", "(Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;Lnet/minecraft/server/v1_13_R2/World;)V", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(39, l0)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitVarInsn(Opcodes.ALOAD, 1)
                visitFieldInsn(Opcodes.PUTFIELD, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowTileEntity", "this$0", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;")
                val l1 = Label()
                visitLabel(l1)
                visitLineNumber(40, l1)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitVarInsn(Opcodes.ALOAD, 2)
                visitFieldInsn(Opcodes.GETSTATIC, "net/minecraft/server/v1_13_R2/BlockPosition", "ZERO", "Lnet/minecraft/server/v1_13_R2/BlockPosition;")
                visitMethodInsn(Opcodes.INVOKESPECIAL, "net/minecraft/server/v1_13_R2/BlockAnvil\$TileEntityContainerAnvil", "<init>", "(Lnet/minecraft/server/v1_13_R2/World;Lnet/minecraft/server/v1_13_R2/BlockPosition;)V", false)
                val l2 = Label()
                visitLabel(l2)
                visitInsn(Opcodes.RETURN)
                val l3 = Label()
                visitLabel(l3)
                visitLocalVariable("this", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowTileEntity;", null, l0, l3, 0)
                visitLocalVariable("world", "Lnet/minecraft/server/v1_13_R2/World;", null, l0, l3, 2)
                visitMaxs(3, 3)
                visitEnd()
            }
            visitMethod(Opcodes.ACC_PUBLIC, "createContainer", "(Lnet/minecraft/server/v1_13_R2/PlayerInventory;Lnet/minecraft/server/v1_13_R2/EntityHuman;)Lnet/minecraft/server/v1_13_R2/Container;", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(45, l0)
                visitTypeInsn(Opcodes.NEW, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer")
                visitInsn(Opcodes.DUP)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitFieldInsn(Opcodes.GETFIELD, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowTileEntity", "this$0", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;")
                visitVarInsn(Opcodes.ALOAD, 1)
                visitVarInsn(Opcodes.ALOAD, 2)
                visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/server/v1_13_R2/EntityHuman", "world", "Lnet/minecraft/server/v1_13_R2/World;")
                visitFieldInsn(Opcodes.GETSTATIC, "net/minecraft/server/v1_13_R2/BlockPosition", "ZERO", "Lnet/minecraft/server/v1_13_R2/BlockPosition;")
                visitVarInsn(Opcodes.ALOAD, 2)
                visitMethodInsn(Opcodes.INVOKESPECIAL, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer", "<init>", "(Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;Lnet/minecraft/server/v1_13_R2/PlayerInventory;Lnet/minecraft/server/v1_13_R2/World;Lnet/minecraft/server/v1_13_R2/BlockPosition;Lnet/minecraft/server/v1_13_R2/EntityHuman;)V", false)
                visitVarInsn(Opcodes.ASTORE, 3)
                val l1 = Label()
                visitLabel(l1)
                visitLineNumber(46, l1)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitFieldInsn(Opcodes.GETFIELD, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowTileEntity", "this$0", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;")
                visitVarInsn(Opcodes.ALOAD, 3)
                visitMethodInsn(Opcodes.INVOKESTATIC, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl", "access$002", "(Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer;)Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer;", false)
                visitInsn(Opcodes.POP)
                val l2 = Label()
                visitLabel(l2)
                visitLineNumber(47, l2)
                visitVarInsn(Opcodes.ALOAD, 3)
                visitInsn(Opcodes.ARETURN)
                val l3 = Label()
                visitLabel(l3)
                visitLocalVariable("this", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowTileEntity;", null, l0, l3, 0)
                visitLocalVariable("inventory", "Lnet/minecraft/server/v1_13_R2/PlayerInventory;", null, l0, l3, 1)
                visitLocalVariable("human", "Lnet/minecraft/server/v1_13_R2/EntityHuman;", null, l0, l3, 2)
                visitLocalVariable("container", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer;", null, l1, l3, 3)
                visitMaxs(7, 4)
                visitEnd()
            }
            visitInnerClass("com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowTileEntity", "AnvilWindowImpl", "AnvilWindowTileEntity", Opcodes.ACC_PRIVATE)
            visitInnerClass("com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer", "AnvilWindowImpl", "AnvilWindowContainer", Opcodes.ACC_PRIVATE)
            visitInnerClass("net/minecraft/server/v1_13_R2/BlockAnvil\$TileEntityContainerAnvil", "BlockAnvil", "TileEntityContainerAnvil", Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC)
        }
            .toByteArray()

        val anvilWindowContainer = ClassWriter(0x00).apply {
            visit(Opcodes.V1_8, Opcodes.ACC_SUPER, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer", null, "net/minecraft/server/v1_13_R2/ContainerAnvil", null)
            visitSource("AnvilWindowImpl.java", null)
            visitField(Opcodes.ACC_FINAL + Opcodes.ACC_SYNTHETIC, "this$0", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;", null, null).visitEnd()
            visitMethod(0x00, "<init>", "(Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;Lnet/minecraft/server/v1_13_R2/PlayerInventory;Lnet/minecraft/server/v1_13_R2/World;Lnet/minecraft/server/v1_13_R2/BlockPosition;Lnet/minecraft/server/v1_13_R2/EntityHuman;)V", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(53, l0)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitVarInsn(Opcodes.ALOAD, 1)
                visitFieldInsn(Opcodes.PUTFIELD, "com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer", "this$0", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl;")
                val l1 = Label()
                visitLabel(l1)
                visitLineNumber(54, l1)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitVarInsn(Opcodes.ALOAD, 2)
                visitVarInsn(Opcodes.ALOAD, 3)
                visitVarInsn(Opcodes.ALOAD, 4)
                visitVarInsn(Opcodes.ALOAD, 5)
                visitMethodInsn(Opcodes.INVOKESPECIAL, "net/minecraft/server/v1_13_R2/ContainerAnvil", "<init>", "(Lnet/minecraft/server/v1_13_R2/PlayerInventory;Lnet/minecraft/server/v1_13_R2/World;Lnet/minecraft/server/v1_13_R2/BlockPosition;Lnet/minecraft/server/v1_13_R2/EntityHuman;)V", false)
                val l3 = Label()
                visitLabel(l3)
                visitLineNumber(55, l3)
                visitInsn(Opcodes.RETURN)
                val l4 = Label()
                visitLabel(l4)
                visitLocalVariable("this", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer;", null, l0, l4, 0)
                visitLocalVariable("playerInventory", "Lnet/minecraft/server/v1_13_R2/PlayerInventory;", null, l0, l4, 2)
                visitLocalVariable("world", "Lnet/minecraft/server/v1_13_R2/World;", null, l0, l4, 3)
                visitLocalVariable("blockPosition", "Lnet/minecraft/server/v1_13_R2/BlockPosition;", null, l0, l4, 4)
                visitLocalVariable("entityHuman", "Lnet/minecraft/server/v1_13_R2/EntityHuman;", null, l0, l4, 5)
                visitMaxs(5, 6)
                visitEnd()
            }
            visitMethod(Opcodes.ACC_PUBLIC, "canUse", "(Lnet/minecraft/server/v1_13_R2/EntityHuman;)Z", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(59, l0)
                visitInsn(Opcodes.ICONST_1)
                visitInsn(Opcodes.IRETURN)
                val l1 = Label()
                visitLabel(l1)
                visitLocalVariable("this", "Lcom/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer;", null, l0, l1, 0)
                visitLocalVariable("entityHuman", "Lnet/minecraft/server/v1_13_R2/EntityHuman;", null, l0, l1, 1)
                visitMaxs(1, 2)
                visitEnd()
            }
            visitInnerClass("com/lgou2w/ldk/bukkit/anvil/AnvilWindowImpl\$AnvilWindowContainer", "AnvilWindowImpl", "AnvilWindowContainer", Opcodes.ACC_PRIVATE)
        }
            .toByteArray()
        classes["com.lgou2w.ldk.bukkit.anvil.AnvilWindowImpl\$AnvilWindowContainer"] = anvilWindowContainer
        classes["com.lgou2w.ldk.bukkit.anvil.AnvilWindowImpl\$AnvilWindowTileEntity"] = anvilWindowTileEntity
        classes["com.lgou2w.ldk.bukkit.anvil.AnvilWindowImpl"] = anvilWindowImpl
        return classes
    }
}
