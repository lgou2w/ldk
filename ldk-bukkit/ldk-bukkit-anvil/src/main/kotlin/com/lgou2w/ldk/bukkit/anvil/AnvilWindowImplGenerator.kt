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
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.bukkit.version.MinecraftVersion
import com.lgou2w.ldk.common.isOrLater
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes

/**
 * ## AnvilWindowImplGenerator (铁砧窗口实现类生成器)
 *
 * @see [ASMClassGenerator]
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
@Suppress("INTERNAL ONLY")
object AnvilWindowImplGenerator : ASMClassGenerator {

    private val VER = MinecraftBukkitVersion.CURRENT.version

    private fun anvilName(className: String) = "com/lgou2w/ldk/bukkit/anvil/$className"
    private fun anvilDescriptor(className: String) = "L${anvilName(className)};"
    private fun nmsName(className: String) = "net/minecraft/server/$VER/$className"
    private fun nmsDescriptor(className: String) = "L${nmsName(className)};"
    private fun obcName(className: String) = "org/bukkit/craftbukkit/$VER/$className"
    private fun obcDescriptor(className: String) = "L${obcName(className)};"

    // AnvilWindowImpl.java - Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>
    /*
    package com.lgou2w.ldk.bukkit.anvil;

    import net.minecraft.server.v1_13_R2.BlockAnvil;
    import net.minecraft.server.v1_13_R2.BlockPosition;
    import net.minecraft.server.v1_13_R2.Container;
    import net.minecraft.server.v1_13_R2.ContainerAnvil;
    import net.minecraft.server.v1_13_R2.EntityHuman;
    import net.minecraft.server.v1_13_R2.EntityPlayer;
    import net.minecraft.server.v1_13_R2.PlayerInventory;
    import net.minecraft.server.v1_13_R2.World;
    import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
    import org.bukkit.entity.Player;
    import org.bukkit.inventory.Inventory;
    import org.bukkit.plugin.Plugin;

    public class AnvilWindowImpl extends AnvilWindowBase {

        private ContainerImpl handle;

        public AnvilWindowImpl(Plugin plugin) {
            super(plugin);
        }

        @Override
        public void open(Player player) {
            super.open(player);
            EntityPlayer playerHandle = ((CraftPlayer) player).getHandle();
            TileEntityImpl tileEntity = new TileEntityImpl(this, playerHandle.world);
            playerHandle.openTileEntity(tileEntity);
        }

        @Override
        protected Object getHandle() {
            return handle;
        }

        @Override
        protected void setHandle(Object handle) {
            this.handle = (ContainerImpl) handle;
        }

        @Override
        protected Inventory getInventory() {
            return handle.getBukkitView().getTopInventory();
        }

        private class TileEntityImpl extends BlockAnvil.TileEntityContainerAnvil {

            private AnvilWindowImpl anvilWindow;

            TileEntityImpl(AnvilWindowImpl anvilWindow, World world) {
                super(world, BlockPosition.ZERO);
                this.anvilWindow = anvilWindow;
            }

            @Override
            public Container createContainer(PlayerInventory inventory, EntityHuman human) {
                ContainerImpl container = new ContainerImpl(anvilWindow, inventory, human);
                anvilWindow.setHandle(container);
                anvilWindow.callOpenedEvent();
                return container;
            }
        }

        private class ContainerImpl extends ContainerAnvil {

            private AnvilWindowImpl anvilWindow;

            ContainerImpl(AnvilWindowImpl anvilWindow, PlayerInventory inventory, EntityHuman human) {
                super(inventory, human.world, BlockPosition.ZERO, human);
                this.anvilWindow = anvilWindow;
            }

            public AnvilWindowBase getAnvilWindow() {
                return anvilWindow;
            }

            @Override
            public boolean canUse(EntityHuman human) {
                return true;
            }

            @Override
            public void a(String value) {
                AnvilWindowInputEvent event = anvilWindow.callInputtedEvent(value);
                if (event == null)
                    super.a(value);
                else if (!event.isCancelled())
                    super.a(event.getValue());
            }

            @Override
            public void b(EntityHuman human) {
                anvilWindow.callClosedEvent();
                anvilWindow.release();
                super.b(human);
            }
        }
    }
    */

    // Since Minecraft 1.14 Pre-Release 5
    // AnvilWindowImpl.java - Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>
    /*
    package com.lgou2w.ldk.bukkit.anvil;

    import net.minecraft.server.v1_14_R1.BlockPosition;
    import net.minecraft.server.v1_14_R1.ChatMessage;
    import net.minecraft.server.v1_14_R1.Container;
    import net.minecraft.server.v1_14_R1.ContainerAnvil;
    import net.minecraft.server.v1_14_R1.EntityHuman;
    import net.minecraft.server.v1_14_R1.EntityPlayer;
    import net.minecraft.server.v1_14_R1.ITileEntityContainer;
    import net.minecraft.server.v1_14_R1.PlayerInventory;
    import net.minecraft.server.v1_14_R1.TileInventory;
    import net.minecraft.server.v1_14_R1.World;
    import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
    import org.bukkit.entity.Player;
    import org.bukkit.inventory.Inventory;
    import org.bukkit.plugin.Plugin;

    public class AnvilWindowImpl extends AnvilWindowBase {

        private ContainerImpl handle;

        public AnvilWindowImpl(Plugin plugin) {
            super(plugin);
        }

        @Override
        public void open(Player player) {
            super.open(player);
            EntityPlayer playerHandle = ((CraftPlayer) player).getHandle();
            TileInventory inventory = new TileInventory(new TileEntityImpl(this), new ChatMessage("container.repair"));
            playerHandle.openContainer(inventory);
        }

        @Override
        protected Object getHandle() {
            return handle;
        }

        @Override
        protected void setHandle(Object handle) {
            this.handle = (ContainerImpl) handle;
        }

        @Override
        protected Inventory getInventory() {
            return handle.getBukkitView().getTopInventory();
        }

        private class TileEntityImpl implements ITileEntityContainer {

            private AnvilWindowImpl anvilWindow;

            TileEntityImpl(AnvilWindowImpl anvilWindow) {
                super();
                this.anvilWindow = anvilWindow;
            }

            @Override
            public Container createMenu(int counter, PlayerInventory inventory, EntityHuman human) {
                ContainerImpl container = new ContainerImpl(anvilWindow, inventory, human.world, counter);
                anvilWindow.setHandle(container);
                anvilWindow.callOpenedEvent();
                return container;
            }
        }

        private class ContainerImpl extends ContainerAnvil {

            private AnvilWindowImpl anvilWindow;

            ContainerImpl(AnvilWindowImpl anvilWindow, PlayerInventory inventory, World world, int counter) {
                super(counter, inventory, ContainerAccess.at(world, BlockPosition.ZERO));
                this.anvilWindow = anvilWindow;
            }

            public AnvilWindowBase getAnvilWindow() {
                return anvilWindow;
            }

            @Override
            public boolean canUse(EntityHuman human) {
                return true;
            }

            @Override
            public void a(String value) {
                AnvilWindowInputEvent event = anvilWindow.callInputtedEvent(value);
                if (event == null)
                    super.a(value);
                else if (!event.isCancelled())
                    super.a(event.getValue());
            }

            @Override
            public void b(EntityHuman human) {
                anvilWindow.callClosedEvent();
                anvilWindow.release();
                super.b(human);
            }
        }
    }
    */

    // Since 1.8.3 TileEntityContainerAnvil has become an inner class of BlockAnvil
    private val isAdapterTileEntityInner = MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_8_R2)
    private fun adapterTileEntityInner(): String {
        return if (isAdapterTileEntityInner) "BlockAnvil\$TileEntityContainerAnvil"
        else "TileEntityContainerAnvil"
    }
    private fun adapterCanUse(): String {
        return if (MinecraftVersion.CURRENT.isOrLater(MinecraftVersion(1, 12, 2)))
            "canUse" else "a" // Versions after 1.12.2 are: canUse
    }

    override fun generate(): Map<String, ByteArray> {
        val classes = LinkedHashMap<String, ByteArray>(3)
        val anvilWindowImpl = ClassWriter(0x00).apply {
            visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, anvilName("AnvilWindowImpl"), null, anvilName("AnvilWindowBase"), null)
            visitSource("AnvilWindowImpl.java", null)
            visitField(Opcodes.ACC_PRIVATE, "handle", anvilDescriptor("AnvilWindowImpl\$ContainerImpl"), null, null).visitEnd()
            visitMethod(Opcodes.ACC_PUBLIC, "<init>", "(Lorg/bukkit/plugin/Plugin;)V", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(21, l0)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitVarInsn(Opcodes.ALOAD, 1)
                visitMethodInsn(Opcodes.INVOKESPECIAL, anvilName("AnvilWindowBase"), "<init>", "(Lorg/bukkit/plugin/Plugin;)V", false)
                val l1 = Label()
                visitLabel(l1)
                visitLineNumber(22, l1)
                visitInsn(Opcodes.RETURN)
                val l2 = Label()
                visitLabel(l2)
                visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl"), null, l0, l2, 0)
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
                visitMethodInsn(Opcodes.INVOKESPECIAL, anvilName("AnvilWindowBase"), "open", "(Lorg/bukkit/entity/Player;)V", false)
                val l1 = Label()
                visitLabel(l1)
                visitLineNumber(27, l1)
                visitVarInsn(Opcodes.ALOAD, 1)
                visitTypeInsn(Opcodes.CHECKCAST, obcName("entity/CraftPlayer"))
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, obcName("entity/CraftPlayer"), "getHandle", "()${nmsDescriptor("EntityPlayer")}", false)
                visitVarInsn(Opcodes.ASTORE, 2)
                val l2 = Label()
                visitLabel(l2)
                visitLineNumber(28, l2)
                if (MinecraftBukkitVersion.isV114OrLater) {
                    // Since Minecraft 1.14 Pre-Release 5
                    visitTypeInsn(Opcodes.NEW, nmsName("TileInventory"))
                    visitInsn(Opcodes.DUP)
                    visitTypeInsn(Opcodes.NEW, anvilName("AnvilWindowImpl\$TileEntityImpl"))
                    visitInsn(Opcodes.DUP)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitMethodInsn(Opcodes.INVOKESPECIAL, anvilName("AnvilWindowImpl\$TileEntityImpl"), "<init>", "(${anvilDescriptor("AnvilWindowImpl")})V", false)
                    visitTypeInsn(Opcodes.NEW, nmsName("ChatMessage"))
                    visitInsn(Opcodes.DUP)
                    visitLdcInsn("container.repair")
                    visitInsn(Opcodes.ICONST_0)
                    visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object")
                    visitMethodInsn(Opcodes.INVOKESPECIAL, nmsName("ChatMessage"), "<init>", "(Ljava/lang/String;[Ljava/lang/Object;)V", false)
                    visitMethodInsn(Opcodes.INVOKESPECIAL, nmsName("TileInventory"), "<init>", "(${nmsDescriptor("ITileEntityContainer")}${nmsDescriptor("IChatBaseComponent")})V", false)
                    visitVarInsn(Opcodes.ASTORE, 3)
                    val l3 = Label()
                    visitLabel(l3)
                    visitLineNumber(29, l3)
                    visitVarInsn(Opcodes.ALOAD, 2)
                    visitVarInsn(Opcodes.ALOAD, 3)
                    visitMethodInsn(Opcodes.INVOKEVIRTUAL, nmsName("EntityPlayer"), "openContainer", "(${nmsDescriptor("ITileInventory")})Ljava/util/OptionalInt;", false)
                    visitInsn(Opcodes.POP)
                    val l4 = Label()
                    visitLabel(l4)
                    visitLineNumber(30, l4)
                    visitInsn(Opcodes.RETURN)
                    val l5 = Label()
                    visitLabel(l5)
                    visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl"), null, l0, l5, 0)
                    visitLocalVariable("player", "Lorg/bukkit/entity/Player;", null, l0, l5, 1)
                    visitLocalVariable("playerHandle", nmsDescriptor("EntityPlayer"), null, l2, l5, 2)
                    visitLocalVariable("inventory", nmsDescriptor("TileInventory"), null, l3, l5, 3)
                    visitMaxs(7, 4)
                } else {
                    visitTypeInsn(Opcodes.NEW, anvilName("AnvilWindowImpl\$TileEntityImpl"))
                    visitInsn(Opcodes.DUP)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitVarInsn(Opcodes.ALOAD, 2)
                    visitFieldInsn(Opcodes.GETFIELD, nmsName("EntityPlayer"), "world", nmsDescriptor("World"))
                    visitMethodInsn(Opcodes.INVOKESPECIAL, anvilName("AnvilWindowImpl\$TileEntityImpl"), "<init>", "(${anvilDescriptor("AnvilWindowImpl")}${nmsDescriptor("World")})V", false)
                    visitVarInsn(Opcodes.ASTORE, 3)
                    val l3 = Label()
                    visitLabel(l3)
                    visitLineNumber(29, l3)
                    visitVarInsn(Opcodes.ALOAD, 2)
                    visitVarInsn(Opcodes.ALOAD, 3)
                    visitMethodInsn(Opcodes.INVOKEVIRTUAL, nmsName("EntityPlayer"), "openTileEntity", "(${nmsDescriptor("ITileEntityContainer")})V", false)
                    val l4 = Label()
                    visitLabel(l4)
                    visitLineNumber(30, l4)
                    visitInsn(Opcodes.RETURN)
                    val l5 = Label()
                    visitLabel(l5)
                    visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl"), null, l0, l5, 0)
                    visitLocalVariable("player", "Lorg/bukkit/entity/Player;", null, l0, l5, 1)
                    visitLocalVariable("playerHandle", nmsDescriptor("EntityPlayer"), null, l2, l5, 2)
                    visitLocalVariable("tileEntity", anvilDescriptor("AnvilWindowImpl\$TileEntityImpl"), null, l3, l5, 3)
                    visitMaxs(4, 4)
                }
                visitEnd()
            }
            visitMethod(Opcodes.ACC_PROTECTED, "getHandle", "()Ljava/lang/Object;", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(34, l0)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitFieldInsn(Opcodes.GETFIELD, anvilName("AnvilWindowImpl"), "handle", anvilDescriptor("AnvilWindowImpl\$ContainerImpl"))
                visitInsn(Opcodes.ARETURN)
                val l1 = Label()
                visitLabel(l1)
                visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl"), null, l0, l1, 0)
                visitMaxs(1, 1)
                visitEnd()
            }
            visitMethod(Opcodes.ACC_PROTECTED, "setHandle", "(Ljava/lang/Object;)V", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(39, l0)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitVarInsn(Opcodes.ALOAD, 1)
                visitTypeInsn(Opcodes.CHECKCAST, anvilName("AnvilWindowImpl\$ContainerImpl"))
                visitFieldInsn(Opcodes.PUTFIELD, anvilName("AnvilWindowImpl"), "handle", anvilDescriptor("AnvilWindowImpl\$ContainerImpl"))
                val l1 = Label()
                visitLabel(l1)
                visitLineNumber(40, l1)
                visitInsn(Opcodes.RETURN)
                val l2 = Label()
                visitLabel(l2)
                visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl"), null, l0, l2, 0)
                visitLocalVariable("handle", "Ljava/lang/Object;", null, l0, l2, 1)
                visitMaxs(2, 2)
                visitEnd()
            }
            visitMethod(Opcodes.ACC_PROTECTED, "getInventory", "()Lorg/bukkit/inventory/Inventory;", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(44, l0)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitFieldInsn(Opcodes.GETFIELD, anvilName("AnvilWindowImpl"), "handle", anvilDescriptor("AnvilWindowImpl\$ContainerImpl"))
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, anvilName("AnvilWindowImpl\$ContainerImpl"), "getBukkitView", "()${obcDescriptor("inventory/CraftInventoryView")}", false)
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, obcName("inventory/CraftInventoryView"), "getTopInventory", "()Lorg/bukkit/inventory/Inventory;", false)
                visitInsn(Opcodes.ARETURN)
                val l1 = Label()
                visitLabel(l1)
                visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl"), null, l0, l1, 0)
                visitMaxs(1, 1)
                visitEnd()
            }
            visitInnerClass(anvilName("AnvilWindowImpl\$ContainerImpl"), "AnvilWindowImpl", "ContainerImpl", Opcodes.ACC_PUBLIC)
            visitInnerClass(anvilName("AnvilWindowImpl\$TileEntityImpl"), "AnvilWindowImpl", "TileEntityImpl", Opcodes.ACC_PUBLIC)
        }.toByteArray()
        val anvilWindowImplTileEntityImpl = ClassWriter(0x00).apply {
            if (MinecraftBukkitVersion.isV114OrLater) {
                // Since Minecraft 1.14 Pre-Release 5
                visit(Opcodes.V1_8, Opcodes.ACC_SUPER, anvilName("AnvilWindowImpl\$TileEntityImpl"), null, "java/lang/Object", arrayOf(nmsName("ITileEntityContainer")))
            } else
                visit(Opcodes.V1_8, Opcodes.ACC_SUPER, anvilName("AnvilWindowImpl\$TileEntityImpl"), null, nmsName(adapterTileEntityInner()), null)
            visitSource("AnvilWindowImpl.java", null)
            visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL, "anvilWindow", anvilDescriptor("AnvilWindowImpl"), null, null)
            if (MinecraftBukkitVersion.isV114OrLater) {
                // Since Minecraft 1.14 Pre-Release 5
                visitMethod(0x00, "<init>", "(${anvilDescriptor("AnvilWindowImpl")})V", null, null).apply {
                    visitCode()
                    val l0 = Label()
                    visitLabel(l0)
                    visitLineNumber(51, l0)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
                    val l1 = Label()
                    visitLabel(l1)
                    visitLineNumber(52, l1)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitVarInsn(Opcodes.ALOAD, 1)
                    visitFieldInsn(Opcodes.PUTFIELD, anvilName("AnvilWindowImpl\$TileEntityImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                    val l2 = Label()
                    visitLabel(l2)
                    visitLineNumber(53, l2)
                    visitInsn(Opcodes.RETURN)
                    val l3 = Label()
                    visitLabel(l3)
                    visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl\$TileEntityImpl"), null, l0, l3, 0)
                    visitLocalVariable("anvilWindow", anvilDescriptor("AnvilWindowImpl"), null, l0, l3, 1)
                    visitMaxs(2, 2)
                    visitEnd()
                }
                visitMethod(Opcodes.ACC_PUBLIC, "createMenu", "(I${nmsDescriptor("PlayerInventory")}${nmsDescriptor("EntityHuman")})${nmsDescriptor("Container")}", null, null).apply {
                    visitCode()
                    val l0 = Label()
                    visitLabel(l0)
                    visitLineNumber(58, l0)
                    visitTypeInsn(Opcodes.NEW, anvilName("AnvilWindowImpl\$ContainerImpl"))
                    visitInsn(Opcodes.DUP)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitFieldInsn(Opcodes.GETFIELD, anvilName("AnvilWindowImpl\$TileEntityImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                    visitVarInsn(Opcodes.ALOAD, 2)
                    visitVarInsn(Opcodes.ALOAD, 3)
                    visitFieldInsn(Opcodes.GETFIELD, nmsName("EntityHuman"), "world", nmsDescriptor("World"))
                    visitVarInsn(Opcodes.ILOAD, 1)
                    visitMethodInsn(Opcodes.INVOKESPECIAL, anvilName("AnvilWindowImpl\$ContainerImpl"), "<init>", "(${anvilDescriptor("AnvilWindowImpl")}${nmsDescriptor("PlayerInventory")}${nmsDescriptor("World")}I)V", false)
                    visitVarInsn(Opcodes.ASTORE, 4)
                    val l1 = Label()
                    visitLabel(l1)
                    visitLineNumber(59, l1)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitFieldInsn(Opcodes.GETFIELD, anvilName("AnvilWindowImpl\$TileEntityImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                    visitVarInsn(Opcodes.ALOAD, 4)
                    visitMethodInsn(Opcodes.INVOKEVIRTUAL, anvilName("AnvilWindowImpl"), "setHandle", "(Ljava/lang/Object;)V", false)
                    val l2 = Label()
                    visitLabel(l2)
                    visitLineNumber(60, l2)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitFieldInsn(Opcodes.GETFIELD, anvilName("AnvilWindowImpl\$TileEntityImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                    visitMethodInsn(Opcodes.INVOKEVIRTUAL, anvilName("AnvilWindowImpl"), "callOpenedEvent", "()${anvilDescriptor("AnvilWindowOpenEvent")}", false)
                    visitInsn(Opcodes.POP)
                    val l3 = Label()
                    visitLabel(l3)
                    visitLineNumber(61, l3)
                    visitVarInsn(Opcodes.ALOAD, 4)
                    visitInsn(Opcodes.ARETURN)
                    val l4 = Label()
                    visitLabel(l4)
                    visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl\$TileEntityImpl"), null, l0, l4, 0)
                    visitLocalVariable("counter", "I", null, l0, l4, 1)
                    visitLocalVariable("inventory", nmsDescriptor("PlayerInventory"), null, l0, l4, 2)
                    visitLocalVariable("human", nmsDescriptor("EntityHuman"), null, l0, l4, 3)
                    visitLocalVariable("container", anvilDescriptor("AnvilWindowImpl\$ContainerImpl"), null, l1, l4, 4)
                    visitMaxs(7, 5)
                    visitEnd()
                }
            } else {
                visitMethod(0x00, "<init>", "(${anvilDescriptor("AnvilWindowImpl")}${nmsDescriptor("World")})V", null, null).apply {
                    visitCode()
                    val l0 = Label()
                    visitLabel(l0)
                    visitLineNumber(51, l0)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitVarInsn(Opcodes.ALOAD, 2)
                    visitFieldInsn(Opcodes.GETSTATIC, nmsName("BlockPosition"), "ZERO", nmsDescriptor("BlockPosition"))
                    visitMethodInsn(Opcodes.INVOKESPECIAL, nmsName(adapterTileEntityInner()), "<init>", "(${nmsDescriptor("World")}${nmsDescriptor("BlockPosition")})V", false)
                    val l1 = Label()
                    visitLabel(l1)
                    visitLineNumber(52, l1)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitVarInsn(Opcodes.ALOAD, 1)
                    visitFieldInsn(Opcodes.PUTFIELD, anvilName("AnvilWindowImpl\$TileEntityImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                    val l2 = Label()
                    visitLabel(l2)
                    visitLineNumber(53, l2)
                    visitInsn(Opcodes.RETURN)
                    val l3 = Label()
                    visitLabel(l3)
                    visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl\$TileEntityImpl"), null, l0, l3, 0)
                    visitLocalVariable("anvilWindow", anvilDescriptor("AnvilWindowImpl"), null, l0, l3, 1)
                    visitLocalVariable("world", nmsDescriptor("World"), null, l0, l3, 2)
                    visitMaxs(3, 3)
                    visitEnd()
                }
                visitMethod(Opcodes.ACC_PUBLIC, "createContainer", "(${nmsDescriptor("PlayerInventory")}${nmsDescriptor("EntityHuman")})${nmsDescriptor("Container")}", null, null).apply {
                    visitCode()
                    val l0 = Label()
                    visitLabel(l0)
                    visitLineNumber(58, l0)
                    visitTypeInsn(Opcodes.NEW, anvilName("AnvilWindowImpl\$ContainerImpl"))
                    visitInsn(Opcodes.DUP)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitFieldInsn(Opcodes.GETFIELD, anvilName("AnvilWindowImpl\$TileEntityImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                    visitVarInsn(Opcodes.ALOAD, 1)
                    visitVarInsn(Opcodes.ALOAD, 2)
                    visitMethodInsn(Opcodes.INVOKESPECIAL, anvilName("AnvilWindowImpl\$ContainerImpl"), "<init>", "(${anvilDescriptor("AnvilWindowImpl")}${nmsDescriptor("PlayerInventory")}${nmsDescriptor("EntityHuman")})V", false)
                    visitVarInsn(Opcodes.ASTORE, 3)
                    val l1 = Label()
                    visitLabel(l1)
                    visitLineNumber(59, l1)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitFieldInsn(Opcodes.GETFIELD, anvilName("AnvilWindowImpl\$TileEntityImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                    visitVarInsn(Opcodes.ALOAD, 3)
                    visitMethodInsn(Opcodes.INVOKEVIRTUAL, anvilName("AnvilWindowImpl"), "setHandle", "(Ljava/lang/Object;)V", false)
                    val l2 = Label()
                    visitLabel(l2)
                    visitLineNumber(60, l2)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitFieldInsn(Opcodes.GETFIELD, anvilName("AnvilWindowImpl\$TileEntityImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                    visitMethodInsn(Opcodes.INVOKEVIRTUAL, anvilName("AnvilWindowImpl"), "callOpenedEvent", "()${anvilDescriptor("AnvilWindowOpenEvent")}", false)
                    visitInsn(Opcodes.POP)
                    val l3 = Label()
                    visitLabel(l3)
                    visitLineNumber(61, l3)
                    visitVarInsn(Opcodes.ALOAD, 3)
                    visitInsn(Opcodes.ARETURN)
                    val l4 = Label()
                    visitLabel(l4)
                    visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl\$TileEntityImpl"), null, l0, l4, 0)
                    visitLocalVariable("inventory", nmsDescriptor("PlayerInventory"), null, l0, l4, 1)
                    visitLocalVariable("human", nmsDescriptor("EntityHuman"), null, l0, l4, 2)
                    visitLocalVariable("container", anvilDescriptor("AnvilWindowImpl\$ContainerImpl"), null, l1, l4, 3)
                    visitMaxs(5, 4)
                    visitEnd()
                }
            }
            visitInnerClass(anvilName("AnvilWindowImpl\$TileEntityImpl"), "AnvilWindowImpl", "TileEntityImpl", Opcodes.ACC_PUBLIC)
            visitInnerClass(anvilName("AnvilWindowImpl\$ContainerImpl"), "AnvilWindowImpl", "ContainerImpl", Opcodes.ACC_PUBLIC)
            if (isAdapterTileEntityInner && !MinecraftBukkitVersion.isV114OrLater) // When it is an inner class
                visitInnerClass(nmsName("BlockAnvil\$TileEntityContainerAnvil"), "BlockAnvil", "TileEntityContainerAnvil", Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC)
        }.toByteArray()
        val anvilWindowImplContainerImpl = ClassWriter(0x00).apply {
            visit(Opcodes.V1_8, Opcodes.ACC_SUPER, anvilName("AnvilWindowImpl\$ContainerImpl"), null, nmsName("ContainerAnvil"), null)
            visitSource("AnvilWindowImpl.java", null)
            visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL, "anvilWindow", anvilDescriptor("AnvilWindowImpl"), null, null)
            if (MinecraftBukkitVersion.isV114OrLater) {
                // Since Minecraft 1.14 Pre-Release 5
                visitMethod(0x00, "<init>", "(${anvilDescriptor("AnvilWindowImpl")}${nmsDescriptor("PlayerInventory")}${nmsDescriptor("World")}I)V", null, null).apply {
                    visitCode()
                    val l0 = Label()
                    visitLabel(l0)
                    visitLineNumber(69, l0)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitVarInsn(Opcodes.ILOAD, 4)
                    visitVarInsn(Opcodes.ALOAD, 2)
                    visitVarInsn(Opcodes.ALOAD, 3)
                    visitFieldInsn(Opcodes.GETSTATIC, nmsName("BlockPosition"), "ZERO", nmsDescriptor("BlockPosition"))
                    visitMethodInsn(Opcodes.INVOKESTATIC, nmsName("ContainerAccess"), "at", "(${nmsDescriptor("World")}${nmsDescriptor("BlockPosition")})${nmsDescriptor("ContainerAccess")}", true)
                    visitMethodInsn(Opcodes.INVOKESPECIAL, nmsName("ContainerAnvil"), "<init>", "(I${nmsDescriptor("PlayerInventory")}${nmsDescriptor("ContainerAccess")})V", false)
                    val l1 = Label()
                    visitLabel(l1)
                    visitLineNumber(70, l1)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitVarInsn(Opcodes.ALOAD, 1)
                    visitFieldInsn(Opcodes.PUTFIELD, anvilName("AnvilWindowImpl\$ContainerImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                    val l2 = Label()
                    visitLabel(l2)
                    visitLineNumber(71, l2)
                    visitInsn(Opcodes.RETURN)
                    val l3 = Label()
                    visitLabel(l3)
                    visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl\$ContainerImpl"), null, l0, l3, 0)
                    visitLocalVariable("anvilWindow", anvilDescriptor("AnvilWindowImpl"), null, l0, l3, 1)
                    visitLocalVariable("inventory", nmsDescriptor("PlayerInventory"), null, l0, l3, 2)
                    visitLocalVariable("world", nmsDescriptor("World"), null, l0, l3, 3)
                    visitLocalVariable("counter", "I", null, l0, l3, 4)
                    visitMaxs(5, 6)
                    visitEnd()
                }
            } else {
                visitMethod(0x00, "<init>", "(${anvilDescriptor("AnvilWindowImpl")}${nmsDescriptor("PlayerInventory")}${nmsDescriptor("EntityHuman")})V", null, null).apply {
                    visitCode()
                    val l0 = Label()
                    visitLabel(l0)
                    visitLineNumber(69, l0)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitVarInsn(Opcodes.ALOAD, 2)
                    visitVarInsn(Opcodes.ALOAD, 3)
                    visitFieldInsn(Opcodes.GETFIELD, nmsName("EntityHuman"), "world", nmsDescriptor("World"))
                    visitFieldInsn(Opcodes.GETSTATIC, nmsName("BlockPosition"), "ZERO", nmsDescriptor("BlockPosition"))
                    visitVarInsn(Opcodes.ALOAD, 3)
                    visitMethodInsn(Opcodes.INVOKESPECIAL, nmsName("ContainerAnvil"), "<init>", "(${nmsDescriptor("PlayerInventory")}${nmsDescriptor("World")}${nmsDescriptor("BlockPosition")}${nmsDescriptor("EntityHuman")})V", false)
                    val l1 = Label()
                    visitLabel(l1)
                    visitLineNumber(70, l1)
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitVarInsn(Opcodes.ALOAD, 1)
                    visitFieldInsn(Opcodes.PUTFIELD, anvilName("AnvilWindowImpl\$ContainerImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                    val l2 = Label()
                    visitLabel(l2)
                    visitLineNumber(71, l2)
                    visitInsn(Opcodes.RETURN)
                    val l3 = Label()
                    visitLabel(l3)
                    visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl\$ContainerImpl"), null, l0, l3, 0)
                    visitLocalVariable("anvilWindow", anvilDescriptor("AnvilWindowImpl"), null, l0, l3, 1)
                    visitLocalVariable("inventory", nmsDescriptor("PlayerInventory"), null, l0, l3, 2)
                    visitLocalVariable("human", nmsDescriptor("EntityHuman"), null, l0, l3, 3)
                    visitMaxs(5, 4)
                    visitEnd()
                }
            }
            visitMethod(Opcodes.ACC_PUBLIC, "getAnvilWindow", "()${anvilDescriptor("AnvilWindowBase")}", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(75, l0)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitFieldInsn(Opcodes.GETFIELD, anvilName("AnvilWindowImpl\$ContainerImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                visitInsn(Opcodes.ARETURN)
                val l1 = Label()
                visitLabel(l1)
                visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl\$ContainerImpl"), null, l0, l1, 0)
                visitMaxs(1, 1)
                visitEnd()
            }
            visitMethod(Opcodes.ACC_PUBLIC, adapterCanUse(), "(${nmsDescriptor("EntityHuman")})Z", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(80, l0)
                visitInsn(Opcodes.ICONST_1)
                visitInsn(Opcodes.IRETURN)
                val l1 = Label()
                visitLabel(l1)
                visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl\$ContainerImpl"), null, l0, l1, 0)
                visitLocalVariable("human", nmsDescriptor("EntityHuman"), null, l0, l1, 1)
                visitMaxs(1, 2)
                visitEnd()
            }
            visitMethod(Opcodes.ACC_PUBLIC, "a", "(Ljava/lang/String;)V", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(85, l0)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitFieldInsn(Opcodes.GETFIELD, anvilName("AnvilWindowImpl\$ContainerImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                visitVarInsn(Opcodes.ALOAD, 1)
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, anvilName("AnvilWindowImpl"), "callInputtedEvent", "(Ljava/lang/String;)${anvilDescriptor("AnvilWindowInputEvent")}", false)
                visitVarInsn(Opcodes.ASTORE, 2)
                val l1 = Label()
                visitLabel(l1)
                visitLineNumber(86, l1)
                visitVarInsn(Opcodes.ALOAD, 2)
                val l3 = Label()
                visitJumpInsn(Opcodes.IFNONNULL, l3)
                val l2 = Label()
                visitLabel(l2)
                visitLineNumber(87, l2)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitVarInsn(Opcodes.ALOAD, 1)
                visitMethodInsn(Opcodes.INVOKESPECIAL, nmsName("ContainerAnvil"), "a", "(Ljava/lang/String;)V", false)
                val l5 = Label()
                visitJumpInsn(Opcodes.GOTO, l5)
                visitLabel(l3)
                visitLineNumber(88, l3)
                visitFrame(Opcodes.F_APPEND, 1, arrayOf(anvilName("AnvilWindowInputEvent")), 0, null)
                visitVarInsn(Opcodes.ALOAD, 2)
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, anvilName("AnvilWindowInputEvent"), "isCancelled", "()Z", false)
                visitJumpInsn(Opcodes.IFNE, l5)
                val l4 = Label()
                visitLabel(l4)
                visitLineNumber(89, l4)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitVarInsn(Opcodes.ALOAD, 2)
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, anvilName("AnvilWindowInputEvent"), "getValue", "()Ljava/lang/String;", false)
                visitMethodInsn(Opcodes.INVOKESPECIAL, nmsName("ContainerAnvil"), "a", "(Ljava/lang/String;)V", false)
                visitLabel(l5)
                visitLineNumber(90, l5)
                visitFrame(Opcodes.F_SAME, 0, null, 0, null)
                visitInsn(Opcodes.RETURN)
                val l6 = Label()
                visitLabel(l6)
                visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl\$ContainerImpl"), null, l0, l6, 0)
                visitLocalVariable("value", "Ljava/lang/String;", null, l0, l6, 1)
                visitLocalVariable("event", anvilDescriptor("AnvilWindowInputEvent"), null, l1, l6, 2)
                visitMaxs(2, 3)
                visitEnd()
            }
            visitMethod(Opcodes.ACC_PUBLIC, "b", "(${nmsDescriptor("EntityHuman")})V", null, null).apply {
                visitCode()
                val l0 = Label()
                visitLabel(l0)
                visitLineNumber(94, l0)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitFieldInsn(Opcodes.GETFIELD, anvilName("AnvilWindowImpl\$ContainerImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, anvilName("AnvilWindowImpl"), "callClosedEvent", "()${anvilDescriptor("AnvilWindowCloseEvent")}", false)
                visitInsn(Opcodes.POP)
                val l1 = Label()
                visitLabel(l1)
                visitLineNumber(95, l1)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitFieldInsn(Opcodes.GETFIELD, anvilName("AnvilWindowImpl\$ContainerImpl"), "anvilWindow", anvilDescriptor("AnvilWindowImpl"))
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, anvilName("AnvilWindowImpl"), "release", "()V", false)
                val l2 = Label()
                visitLabel(l2)
                visitLineNumber(96, l2)
                visitVarInsn(Opcodes.ALOAD, 0)
                visitVarInsn(Opcodes.ALOAD, 1)
                visitMethodInsn(Opcodes.INVOKESPECIAL, nmsName("ContainerAnvil"), "b", "(${nmsDescriptor("EntityHuman")})V", false)
                val l3 = Label()
                visitLabel(l3)
                visitLineNumber(97, l3)
                visitInsn(Opcodes.RETURN)
                val l4 = Label()
                visitLabel(l4)
                visitLocalVariable("this", anvilDescriptor("AnvilWindowImpl\$ContainerImpl"), null, l0, l4, 0)
                visitLocalVariable("human", nmsDescriptor("EntityHuman"), null, l0, l4, 1)
                visitMaxs(2, 2)
                visitEnd()
            }
            visitInnerClass(anvilName("AnvilWindowImpl\$ContainerImpl"), "AnvilWindowImpl", "ContainerImpl", Opcodes.ACC_PUBLIC)
        }.toByteArray()
        classes["com.lgou2w.ldk.bukkit.anvil.AnvilWindowImpl\$ContainerImpl"] = anvilWindowImplContainerImpl
        classes["com.lgou2w.ldk.bukkit.anvil.AnvilWindowImpl\$TileEntityImpl"] = anvilWindowImplTileEntityImpl
        classes["com.lgou2w.ldk.bukkit.anvil.AnvilWindowImpl"] = anvilWindowImpl
        return classes
    }
}
