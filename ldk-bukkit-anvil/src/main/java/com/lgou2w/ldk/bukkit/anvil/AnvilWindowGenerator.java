/*
 * Copyright (C) 2016-2021 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.bukkit.anvil;

import com.lgou2w.ldk.bukkit.reflect.MinecraftReflection;
import com.lgou2w.ldk.bukkit.version.BukkitVersion;
import com.lgou2w.ldk.bukkit.version.MinecraftVersion;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.LinkedHashMap;
import java.util.Map;

/** INTERNAL ONLY */
@Deprecated
@SuppressWarnings("DuplicatedCode")
final class AnvilWindowGenerator implements Opcodes {

  private AnvilWindowGenerator() { }

  public static Map<String, byte[]> generate() {
    Map<String, byte[]> classes = new LinkedHashMap<>(3);
    classes.put("com.lgou2w.ldk.bukkit.anvil.AnvilWindowImpl$ContainerImpl", generateContainerImpl());
    classes.put("com.lgou2w.ldk.bukkit.anvil.AnvilWindowImpl$TileEntityImpl", generateTileEntityImpl());
    classes.put("com.lgou2w.ldk.bukkit.anvil.AnvilWindowImpl", generateAnvilWindowImpl());
    return classes;
  }

  private final static String PACKAGE_ANVIL = "com/lgou2w/ldk/bukkit/anvil";
  private final static String NAME_ANVIL = PACKAGE_ANVIL + "/AnvilWindow";
  private final static String NAME_ANVIL_BASE = PACKAGE_ANVIL + "/AnvilWindowBase";
  private final static String NAME_ANVIL_IMPL = PACKAGE_ANVIL + "/AnvilWindowImpl";
  private final static String NAME_ANVIL_CONTAINER_IMPL = PACKAGE_ANVIL + "/AnvilWindowImpl$ContainerImpl";
  private final static String NAME_ANVIL_TILE_ENTITY_IMPL = PACKAGE_ANVIL + "/AnvilWindowImpl$TileEntityImpl";
  private final static String NAME_ANVIL_INPUT_EVENT = PACKAGE_ANVIL + "/AnvilWindowInputEvent";
  private final static String NAME_ANVIL_CLOSE_EVENT = PACKAGE_ANVIL + "/AnvilWindowCloseEvent";
  private final static String NAME_ANVIL_OPEN_EVENT = PACKAGE_ANVIL + "/AnvilWindowOpenEvent";
  private final static String DESC_ANVIL = classDesc(NAME_ANVIL);
  private final static String DESC_ANVIL_BASE = classDesc(NAME_ANVIL_BASE);
  private final static String DESC_ANVIL_IMPL = classDesc(NAME_ANVIL_IMPL);
  private final static String DESC_ANVIL_CONTAINER_IMPL = classDesc(NAME_ANVIL_CONTAINER_IMPL);
  private final static String DESC_ANVIL_TILE_ENTITY_IMPL = classDesc(NAME_ANVIL_TILE_ENTITY_IMPL);
  private final static String DESC_ANVIL_INPUT_EVENT = classDesc(NAME_ANVIL_INPUT_EVENT);
  private final static String DESC_ANVIL_CLOSE_EVENT = classDesc(NAME_ANVIL_CLOSE_EVENT);
  private final static String DESC_ANVIL_OPEN_EVENT = classDesc(NAME_ANVIL_OPEN_EVENT);

  private final static String NAME_NMS_WORLD = loadNMSClassName("World");
  private final static String NAME_NMS_ENTITY_HUMAN = loadNMSClassName("EntityHuman");
  private final static String NAME_NMS_ENTITY_PLAYER = loadNMSClassName("EntityPlayer");
  private final static String NAME_NMS_BLOCK_POSITION = loadNMSClassName("BlockPosition");
  private final static String NAME_NMS_CONTAINER_ANVIL = loadNMSClassName("ContainerAnvil");
  private final static String NAME_NMS_PLAYER_INVENTORY = loadNMSClassName("PlayerInventory");
  private final static String DESC_NMS_WORLD = classDesc(NAME_NMS_WORLD);
  private final static String DESC_NMS_ENTITY_HUMAN = classDesc(NAME_NMS_ENTITY_HUMAN);
  private final static String DESC_NMS_ENTITY_PLAYER = classDesc(NAME_NMS_ENTITY_PLAYER);
  private final static String DESC_NMS_BLOCK_POSITION = classDesc(NAME_NMS_BLOCK_POSITION);
  private final static String DESC_NMS_PLAYER_INVENTORY = classDesc(NAME_NMS_PLAYER_INVENTORY);

  // FIXME: Not compatible with Arclight version 1.14.4
  //   The server did not print the stack trace, which cannot be solved temporarily.

  private static byte[] generateContainerImpl() {
    ClassWriter cw = new ClassWriter(0x0);
    cw.visit(V1_8, ACC_SUPER, NAME_ANVIL_CONTAINER_IMPL, null, NAME_NMS_CONTAINER_ANVIL, null);
    cw.visitSource("AnvilWindowImpl.java", null);
    cw.visitField(ACC_PRIVATE + ACC_FINAL, "anvilWindow", DESC_ANVIL_IMPL, null, null);
    {
      MethodVisitor mv;
      Label l0, l1, l2, l3, l4;
      if (BukkitVersion.isV114OrLater) {
        mv = cw.visitMethod(0x0, "<init>", '(' + DESC_ANVIL_IMPL + DESC_NMS_PLAYER_INVENTORY + DESC_NMS_WORLD + "I)V", null, null);
        mv.visitCode();
        l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(69, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ILOAD, 4);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 3);
        {
          String name = remappingField(loadNMSClass("BlockPosition"), "ZERO");
          mv.visitFieldInsn(GETSTATIC, NAME_NMS_BLOCK_POSITION, name, DESC_NMS_BLOCK_POSITION);
        }
        {
          Class<?> world = loadNMSClass("World");
          Class<?> containerAccess = loadNMSClass("ContainerAccess");
          String name = remappingMethod(containerAccess, "at", new Class[] { world, loadNMSClass("BlockPosition") });
          String descriptor = '(' + classDesc(world) + DESC_NMS_BLOCK_POSITION + ')' + classDesc(containerAccess);
          mv.visitMethodInsn(INVOKESTATIC, className(containerAccess), name, descriptor, true);
        }
        {
          Class<?> containerAccess = loadNMSClass("ContainerAccess");
          String descriptor = "(I" + DESC_NMS_PLAYER_INVENTORY + classDesc(containerAccess) + ")V";
          mv.visitMethodInsn(INVOKESPECIAL, NAME_NMS_CONTAINER_ANVIL, "<init>", descriptor, false);
        }
      } else {
        mv = cw.visitMethod(0x0, "<init>", '(' + DESC_ANVIL_IMPL + DESC_NMS_PLAYER_INVENTORY + DESC_NMS_ENTITY_HUMAN + ")V", null, null);
        mv.visitCode();
        l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(69, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 3);
        {
          String fieldName = remappingField(loadNMSClass("Entity"), "world");
          mv.visitFieldInsn(GETFIELD, NAME_NMS_ENTITY_HUMAN, fieldName, DESC_NMS_WORLD);
        }
        {
          String fieldName = remappingField(loadNMSClass("BlockPosition"), "ZERO");
          mv.visitFieldInsn(GETSTATIC, NAME_NMS_BLOCK_POSITION, fieldName, DESC_NMS_BLOCK_POSITION);
        }
        mv.visitVarInsn(ALOAD, 3);
        {
          String descriptor = '(' + DESC_NMS_PLAYER_INVENTORY + DESC_NMS_WORLD + DESC_NMS_BLOCK_POSITION + DESC_NMS_ENTITY_HUMAN + ")V";
          mv.visitMethodInsn(INVOKESPECIAL, NAME_NMS_CONTAINER_ANVIL, "<init>", descriptor, false);
        }
      }
      l1 = new Label();
      mv.visitLabel(l1);
      mv.visitLineNumber(70, l1);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitVarInsn(ALOAD, 1);
      mv.visitFieldInsn(PUTFIELD, NAME_ANVIL_CONTAINER_IMPL, "anvilWindow", DESC_ANVIL_IMPL);
      l2 = new Label();
      mv.visitLabel(l2);
      mv.visitLineNumber(71, l2);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitInsn(ICONST_0);
      {
        String fieldName = remappingField(AnvilWindowBase.CLASS_CONTAINER, "checkReachable");
        mv.visitFieldInsn(PUTFIELD, NAME_ANVIL_CONTAINER_IMPL, fieldName, "Z");
      }
      l3 = new Label();
      mv.visitLabel(l3);
      mv.visitLineNumber(72, l3);
      mv.visitInsn(RETURN);
      l4 = new Label();
      mv.visitLabel(l4);
      mv.visitLocalVariable("this", DESC_ANVIL_CONTAINER_IMPL, null, l0, l4, 0);
      mv.visitLocalVariable("anvilWindow", DESC_ANVIL_IMPL, null, l0, l4, 1);
      mv.visitLocalVariable("inventory", DESC_NMS_PLAYER_INVENTORY, null, l0, l4, 2);

      if (BukkitVersion.isV114OrLater) {
        mv.visitLocalVariable("world", DESC_NMS_WORLD, null, l0, l4, 3);
        mv.visitLocalVariable("counter", "I", null, l0, l4, 4);
        mv.visitMaxs(5, 6);
      } else {
        mv.visitLocalVariable("human", DESC_NMS_ENTITY_HUMAN, null, l0, l4, 3);
        mv.visitMaxs(5, 5);
      }
      mv.visitEnd();
    }
    {
      MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getAnvilWindow", "()" + DESC_ANVIL_BASE, null, null);
      mv.visitCode();
      Label l0 = new Label();
      mv.visitLabel(l0);
      mv.visitLineNumber(76, l0);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitFieldInsn(GETFIELD, NAME_ANVIL_CONTAINER_IMPL, "anvilWindow", DESC_ANVIL_IMPL);
      mv.visitInsn(ARETURN);
      Label l1 = new Label();
      mv.visitLabel(l1);
      mv.visitLocalVariable("this", DESC_ANVIL_CONTAINER_IMPL, null, l0, l1, 0);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
    }
    {
      String methodName = BukkitVersion.isArclight
        ? "func_75145_c"
        : MinecraftVersion.CURRENT.compareTo(new MinecraftVersion(1, 12, 2)) >= 0
          ? "canUse"
          : "a";
      MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, methodName, '(' + DESC_NMS_ENTITY_HUMAN + ")Z", null, null);
      mv.visitCode();
      Label l0 = new Label();
      mv.visitLabel(l0);
      mv.visitLineNumber(81, l0);
      mv.visitInsn(ICONST_1);
      mv.visitInsn(IRETURN);
      Label l1 = new Label();
      mv.visitLabel(l1);
      mv.visitLocalVariable("this", DESC_ANVIL_CONTAINER_IMPL, null, l0, l1, 0);
      mv.visitLocalVariable("human", DESC_NMS_ENTITY_HUMAN, null, l0, l1, 1);
      mv.visitMaxs(1, 2);
      mv.visitEnd();
    }
    {
      String methodName = BukkitVersion.isForge ? "func_82850_a" : "a";
      MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, methodName, "(Ljava/lang/String;)V", null, null);
      mv.visitCode();
      Label l0 = new Label();
      mv.visitLabel(l0);
      mv.visitLineNumber(86, l0);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitFieldInsn(GETFIELD, NAME_ANVIL_CONTAINER_IMPL, "anvilWindow", DESC_ANVIL_IMPL);
      mv.visitVarInsn(ALOAD, 1);
      mv.visitMethodInsn(INVOKEVIRTUAL, NAME_ANVIL_IMPL, "callInputEvent", "(Ljava/lang/String;)" + DESC_ANVIL_INPUT_EVENT, false);
      mv.visitVarInsn(ASTORE, 2);
      Label l1 = new Label();
      mv.visitLabel(l1);
      mv.visitLineNumber(87, l1);
      mv.visitVarInsn(ALOAD, 2);
      Label l3 = new Label();
      mv.visitJumpInsn(IFNONNULL, l3);
      Label l2 = new Label();
      mv.visitLabel(l2);
      mv.visitLineNumber(88, l2);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitVarInsn(ALOAD, 1);
      mv.visitMethodInsn(INVOKESPECIAL, NAME_NMS_CONTAINER_ANVIL, methodName, "(Ljava/lang/String;)V", false);
      Label l5 = new Label();
      mv.visitJumpInsn(GOTO, l5);
      mv.visitLabel(l3);
      mv.visitLineNumber(89, l3);
      mv.visitFrame(F_APPEND, 1, new String[] { NAME_ANVIL_INPUT_EVENT }, 0, null);
      mv.visitVarInsn(ALOAD, 2);
      mv.visitMethodInsn(INVOKEVIRTUAL, NAME_ANVIL_INPUT_EVENT, "isCancelled", "()Z", false);
      mv.visitJumpInsn(IFNE, l5);
      Label l4 = new Label();
      mv.visitLabel(l4);
      mv.visitLineNumber(90, l4);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitVarInsn(ALOAD, 2);
      mv.visitMethodInsn(INVOKEVIRTUAL, NAME_ANVIL_INPUT_EVENT, "getValue", "()Ljava/lang/String;", false);
      mv.visitMethodInsn(INVOKESPECIAL, NAME_NMS_CONTAINER_ANVIL, methodName, "(Ljava/lang/String;)V", false);
      mv.visitLabel(l5);
      mv.visitLineNumber(91, l5);
      mv.visitFrame(F_SAME, 0, null, 0, null);
      mv.visitInsn(RETURN);
      Label l6 = new Label();
      mv.visitLabel(l6);
      mv.visitLocalVariable("this", DESC_ANVIL_CONTAINER_IMPL, null, l0, l6, 0);
      mv.visitLocalVariable("value", "Ljava/lang/String;", null, l0, l6, 1);
      mv.visitLocalVariable("event", DESC_ANVIL_INPUT_EVENT, null, l1, l6, 2);
      mv.visitMaxs(2, 3);
      mv.visitEnd();
    }
    {
      String methodName = BukkitVersion.isArclight ? "func_75134_a" : "b";
      MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, methodName, '(' + DESC_NMS_ENTITY_HUMAN + ")V", null, null);
      mv.visitCode();
      Label l0 = new Label();
      mv.visitLabel(l0);
      mv.visitLineNumber(95, l0);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitFieldInsn(GETFIELD, NAME_ANVIL_CONTAINER_IMPL, "anvilWindow", DESC_ANVIL_IMPL);
      mv.visitMethodInsn(INVOKEVIRTUAL, NAME_ANVIL_IMPL, "callCloseEvent", "()" + DESC_ANVIL_CLOSE_EVENT, false);
      mv.visitInsn(POP);
      Label l1 = new Label();
      mv.visitLabel(l1);
      mv.visitLineNumber(96, l1);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitFieldInsn(GETFIELD, NAME_ANVIL_CONTAINER_IMPL, "anvilWindow", DESC_ANVIL_IMPL);
      mv.visitMethodInsn(INVOKEVIRTUAL, NAME_ANVIL_IMPL, "release", "()V", false);
      Label l2 = new Label();
      mv.visitLabel(l2);
      mv.visitLineNumber(97, l2);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitVarInsn(ALOAD, 1);
      mv.visitMethodInsn(INVOKESPECIAL, NAME_NMS_CONTAINER_ANVIL, methodName, '(' + DESC_NMS_ENTITY_HUMAN + ")V", false);
      Label l3 = new Label();
      mv.visitLabel(l3);
      mv.visitLineNumber(98, l3);
      mv.visitInsn(RETURN);
      Label l4 = new Label();
      mv.visitLabel(l4);
      mv.visitLocalVariable("this", DESC_ANVIL_CONTAINER_IMPL, null, l0, l4, 0);
      mv.visitLocalVariable("human", DESC_NMS_ENTITY_HUMAN, null, l0, l4, 1);
      mv.visitMaxs(2, 2);
      mv.visitEnd();
    }
    cw.visitInnerClass(NAME_ANVIL_CONTAINER_IMPL, "AnvilWindowImpl", "ContainerImpl", ACC_PUBLIC);
    return cw.toByteArray();
  }

  private static byte[] generateTileEntityImpl() {
    ClassWriter cw = new ClassWriter(0x0);
    if (BukkitVersion.isV114OrLater) {
      String iTileEntityContainer = loadNMSClassName("ITileEntityContainer");
      cw.visit(V1_8, ACC_SUPER, NAME_ANVIL_TILE_ENTITY_IMPL, null, "java/lang/Object", new String[] { iTileEntityContainer });
    } else {
      String superName = BukkitVersion.CURRENT.compareTo(BukkitVersion.V1_8_R2) >= 0
        ? "BlockAnvil$TileEntityContainerAnvil"
        : "TileEntityContainerAnvil";
      cw.visit(V1_8, ACC_SUPER, NAME_ANVIL_TILE_ENTITY_IMPL, null, loadNMSClassName(superName), null);
    }
    cw.visitSource("AnvilWindowImpl.java", null);
    cw.visitField(ACC_PRIVATE + ACC_FINAL, "anvilWindow", DESC_ANVIL_IMPL, null, null);

    if (BukkitVersion.isV114OrLater) {
      {
        MethodVisitor mv = cw.visitMethod(0x0, "<init>", '(' + DESC_ANVIL_IMPL + ")V", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(51, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(52, l1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, NAME_ANVIL_TILE_ENTITY_IMPL, "anvilWindow", DESC_ANVIL_IMPL);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(53, l2);
        mv.visitInsn(RETURN);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLocalVariable("this", DESC_ANVIL_TILE_ENTITY_IMPL, null, l0, l3, 0);
        mv.visitLocalVariable("anvilWindow", DESC_ANVIL_IMPL, null, l0, l3, 1);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
      }
      {
        String methodName = remappingMethod(loadNMSClass("ITileEntityContainer"), "createMenu",
          new Class[] { int.class, loadNMSClass("PlayerInventory"), loadNMSClass("EntityHuman") });
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, methodName, "(I" + DESC_NMS_PLAYER_INVENTORY + DESC_NMS_ENTITY_HUMAN + ')' + classDesc(AnvilWindowBase.CLASS_CONTAINER), null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(58, l0);
        mv.visitTypeInsn(NEW, NAME_ANVIL_CONTAINER_IMPL);
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, NAME_ANVIL_TILE_ENTITY_IMPL, "anvilWindow", DESC_ANVIL_IMPL);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 3);
        {
          String fieldName = remappingField(loadNMSClass("Entity"), "world");
          mv.visitFieldInsn(GETFIELD, NAME_NMS_ENTITY_HUMAN, fieldName, DESC_NMS_WORLD);
        }
        mv.visitVarInsn(ILOAD, 1);
        mv.visitMethodInsn(INVOKESPECIAL, NAME_ANVIL_CONTAINER_IMPL, "<init>", '(' + DESC_ANVIL_IMPL + DESC_NMS_PLAYER_INVENTORY + DESC_NMS_WORLD + "I)V", false);
        mv.visitVarInsn(ASTORE, 4);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(59, l1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, NAME_ANVIL_TILE_ENTITY_IMPL, "anvilWindow", DESC_ANVIL_IMPL);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(INVOKEVIRTUAL, NAME_ANVIL_IMPL, "setHandle", "(Ljava/lang/Object;)V", false);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(60, l2);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, NAME_ANVIL_TILE_ENTITY_IMPL, "anvilWindow", DESC_ANVIL_IMPL);
        mv.visitMethodInsn(INVOKEVIRTUAL, NAME_ANVIL_IMPL, "callOpenEvent", "()" + DESC_ANVIL_OPEN_EVENT, false);
        mv.visitInsn(POP);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLineNumber(61, l3);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitInsn(ARETURN);
        Label l4 = new Label();
        mv.visitLabel(l4);
        mv.visitLocalVariable("this", DESC_ANVIL_TILE_ENTITY_IMPL, null, l0, l4, 0);
        mv.visitLocalVariable("counter", "I", null, l0, l4, 1);
        mv.visitLocalVariable("inventory", DESC_NMS_PLAYER_INVENTORY, null, l0, l4, 2);
        mv.visitLocalVariable("human", DESC_NMS_ENTITY_HUMAN, null, l0, l4, 3);
        mv.visitLocalVariable("container", DESC_ANVIL_CONTAINER_IMPL, null, l1, l4, 4);
        mv.visitMaxs(7, 5);
        mv.visitEnd();
      }
    } else {
      String superName = BukkitVersion.CURRENT.compareTo(BukkitVersion.V1_8_R2) >= 0
        ? "BlockAnvil$TileEntityContainerAnvil"
        : "TileEntityContainerAnvil";
      {
        MethodVisitor mv = cw.visitMethod(0x0, "<init>", '(' + DESC_ANVIL_IMPL + DESC_NMS_WORLD + ")V", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(51, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        {
          String fieldName = remappingField(loadNMSClass("BlockPosition"), "ZERO");
          mv.visitFieldInsn(GETSTATIC, NAME_NMS_BLOCK_POSITION, fieldName, DESC_NMS_BLOCK_POSITION);
        }
        {
          mv.visitMethodInsn(INVOKESPECIAL, loadNMSClassName(superName), "<init>", '(' + DESC_NMS_WORLD + DESC_NMS_BLOCK_POSITION + ")V", false);
        }
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(52, l1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, NAME_ANVIL_TILE_ENTITY_IMPL, "anvilWindow", DESC_ANVIL_IMPL);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(53, l2);
        mv.visitInsn(RETURN);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLocalVariable("this", DESC_ANVIL_TILE_ENTITY_IMPL, null, l0, l3, 0);
        mv.visitLocalVariable("anvilWindow", DESC_ANVIL_IMPL, null, l0, l3, 1);
        mv.visitLocalVariable("world", DESC_NMS_WORLD, null, l0, l3, 2);
        mv.visitMaxs(3, 3);
        mv.visitEnd();
      }
      {
        String methodName = remappingMethod(loadNMSClass(superName), "createContainer",
          new Class[] { loadNMSClass("PlayerInventory"), loadNMSClass("EntityHuman") });
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, methodName, '(' + DESC_NMS_PLAYER_INVENTORY + DESC_NMS_ENTITY_HUMAN + ')' + classDesc(AnvilWindowBase.CLASS_CONTAINER), null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(58, l0);
        mv.visitTypeInsn(NEW, NAME_ANVIL_CONTAINER_IMPL);
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, NAME_ANVIL_TILE_ENTITY_IMPL, "anvilWindow", DESC_ANVIL_IMPL);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKESPECIAL, NAME_ANVIL_CONTAINER_IMPL, "<init>", '(' + DESC_ANVIL_IMPL + DESC_NMS_PLAYER_INVENTORY + DESC_NMS_ENTITY_HUMAN + ")V", false);
        mv.visitVarInsn(ASTORE, 3);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(59, l1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, NAME_ANVIL_TILE_ENTITY_IMPL, "anvilWindow", DESC_ANVIL_IMPL);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKEVIRTUAL, NAME_ANVIL_IMPL, "setHandle", "(Ljava/lang/Object;)V", false);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(60, l2);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, NAME_ANVIL_TILE_ENTITY_IMPL, "anvilWindow", DESC_ANVIL_IMPL);
        mv.visitMethodInsn(INVOKEVIRTUAL, NAME_ANVIL_IMPL, "callOpenEvent", "()" + DESC_ANVIL_OPEN_EVENT, false);
        mv.visitInsn(POP);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLineNumber(61, l3);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitInsn(ARETURN);
        Label l4 = new Label();
        mv.visitLabel(l4);
        mv.visitLocalVariable("this", DESC_ANVIL_TILE_ENTITY_IMPL, null, l0, l4, 0);
        mv.visitLocalVariable("inventory", DESC_NMS_PLAYER_INVENTORY, null, l0, l4, 1);
        mv.visitLocalVariable("human", DESC_NMS_ENTITY_HUMAN, null, l0, l4, 2);
        mv.visitLocalVariable("container", DESC_ANVIL_CONTAINER_IMPL, null, l1, l4, 3);
        mv.visitMaxs(5, 4);
        mv.visitEnd();
      }
    }
    cw.visitInnerClass(NAME_ANVIL_TILE_ENTITY_IMPL, "AnvilWindowImpl", "TileEntityImpl", ACC_PUBLIC);
    cw.visitInnerClass(NAME_ANVIL_CONTAINER_IMPL, "AnvilWindowImpl", "ContainerImpl", ACC_PUBLIC);
    if (BukkitVersion.CURRENT.compareTo(BukkitVersion.V1_8_R2) >= 0 && !BukkitVersion.isV114OrLater)
      cw.visitInnerClass(loadNMSClassName("BlockAnvil$TileEntityContainerAnvil"), "BlockAnvil", "TileEntityContainerAnvil", ACC_PUBLIC + ACC_STATIC);
    return cw.toByteArray();
  }

  private static byte[] generateAnvilWindowImpl() {
    ClassWriter cw = new ClassWriter(0x0);
    cw.visit(V1_8, ACC_SUPER, NAME_ANVIL_IMPL, null, NAME_ANVIL_BASE, null);
    cw.visitSource("AnilWindowImpl.java", null);
    cw.visitField(ACC_PRIVATE, "handle", DESC_ANVIL_CONTAINER_IMPL, null, null);
    {
      MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lorg/bukkit/plugin/Plugin;)V", null, null);
      mv.visitCode();
      Label l0 = new Label();
      mv.visitLabel(l0);
      mv.visitLineNumber(21, l0);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitVarInsn(ALOAD, 1);
      mv.visitMethodInsn(INVOKESPECIAL, NAME_ANVIL_BASE, "<init>", "(Lorg/bukkit/plugin/Plugin;)V", false);
      Label l1 = new Label();
      mv.visitLabel(l1);
      mv.visitLineNumber(22, l1);
      mv.visitInsn(RETURN);
      Label l2 = new Label();
      mv.visitLabel(l2);
      mv.visitLocalVariable("this", DESC_ANVIL_IMPL, null, l0, l2, 0);
      mv.visitLocalVariable("plugin", "Lorg/bukkit/plugin/Plugin;", null, l0, l2, 1);
      mv.visitMaxs(2, 2);
      mv.visitEnd();
    }
    {
      MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "open", "(Lorg/bukkit/entity/Player;)" + DESC_ANVIL, null, null);
      mv.visitCode();
      Label l0 = new Label();
      mv.visitLabel(l0);
      mv.visitLineNumber(26, l0);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitVarInsn(ALOAD, 1);
      mv.visitMethodInsn(INVOKESPECIAL, NAME_ANVIL_BASE, "open", "(Lorg/bukkit/entity/Player;)" + DESC_ANVIL, false);
      Label l1 = new Label();
      mv.visitLabel(l1);
      mv.visitLineNumber(27, l1);
      mv.visitVarInsn(ALOAD, 1);
      {
        String craftPlayer = loadOBCClassName("entity.CraftPlayer");
        mv.visitTypeInsn(CHECKCAST, craftPlayer);
        mv.visitMethodInsn(INVOKEVIRTUAL, craftPlayer, "getHandle", "()" + DESC_NMS_ENTITY_PLAYER, false);
      }
      mv.visitVarInsn(ASTORE, 2);
      Label l2 = new Label();
      mv.visitLabel(l2);
      mv.visitLineNumber(28, l2);

      if (BukkitVersion.isV114OrLater) {
        String chatMessage = loadNMSClassName("ChatMessage");
        String tileInventory = loadNMSClassName("TileInventory");
        String iTileEntityContainer = loadNMSClassName("ITileEntityContainer");
        String iChatBaseComponent = loadNMSClassName("IChatBaseComponent");
        mv.visitTypeInsn(NEW, tileInventory);
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, NAME_ANVIL_TILE_ENTITY_IMPL);
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, NAME_ANVIL_TILE_ENTITY_IMPL, "<init>", '(' + DESC_ANVIL_IMPL + ")V", false);
        mv.visitTypeInsn(NEW, chatMessage);
        mv.visitInsn(DUP);
        mv.visitLdcInsn("container.repair");
        mv.visitInsn(ICONST_0);
        mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
        mv.visitMethodInsn(INVOKESPECIAL, chatMessage, "<init>", "(Ljava/lang/String;[Ljava/lang/Object;)V", false);
        mv.visitMethodInsn(INVOKESPECIAL, tileInventory, "<init>", '(' + classDesc(iTileEntityContainer) + classDesc(iChatBaseComponent) + ")V", false);
        mv.visitVarInsn(ASTORE, 3);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLineNumber(29, l3);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 3);
        {
          Class<?> iTileInventory = loadNMSClass("ITileInventory");
          String methodName = remappingMethod(loadNMSClass("EntityHuman"), "openContainer", new Class[] { iTileInventory });
          mv.visitMethodInsn(INVOKEVIRTUAL, NAME_NMS_ENTITY_HUMAN, methodName, '(' + classDesc(iTileInventory) + ")Ljava/util/OptionalInt;", false);
        }
        mv.visitInsn(POP);
        Label l4 = new Label();
        mv.visitLabel(l4);
        mv.visitLineNumber(30, l4);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(ARETURN);
        Label l5 = new Label();
        mv.visitLabel(l5);
        mv.visitLocalVariable("this", DESC_ANVIL_IMPL, null, l0, l5, 0);
        mv.visitLocalVariable("player", "Lorg/bukkit/entity/Player;", null, l0, l5, 1);
        mv.visitLocalVariable("playerHandle", DESC_NMS_ENTITY_PLAYER, null, l2, l5, 2);
        mv.visitLocalVariable("inventory", classDesc(tileInventory), null, l3, l5, 3);
        mv.visitMaxs(8, 4);
      } else {
        mv.visitTypeInsn(NEW, NAME_ANVIL_TILE_ENTITY_IMPL);
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        {
          String fieldName = remappingField(loadNMSClass("Entity"), "world");
          mv.visitFieldInsn(GETFIELD, NAME_NMS_ENTITY_PLAYER, fieldName, DESC_NMS_WORLD);
        }
        mv.visitMethodInsn(INVOKESPECIAL, NAME_ANVIL_TILE_ENTITY_IMPL, "<init>", '(' + DESC_ANVIL_IMPL + DESC_NMS_WORLD + ")V", false);
        mv.visitVarInsn(ASTORE, 3);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLineNumber(29, l3);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 3);
        {
          Class<?> iTileEntityContainer = loadNMSClass("ITileEntityContainer");
          String methodName = remappingMethod(loadNMSClass("EntityHuman"), "openTileEntity", new Class[] { iTileEntityContainer });
          mv.visitMethodInsn(INVOKEVIRTUAL, NAME_NMS_ENTITY_HUMAN, methodName, '(' + classDesc(iTileEntityContainer) + ")V", false);
        }
        Label l4 = new Label();
        mv.visitLabel(l4);
        mv.visitLineNumber(30, l4);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(ARETURN);
        Label l5 = new Label();
        mv.visitLabel(l5);
        mv.visitLocalVariable("this", DESC_ANVIL_IMPL, null, l0, l5, 0);
        mv.visitLocalVariable("player", "Lorg/bukkit/entity/Player;", null, l0, l5, 1);
        mv.visitLocalVariable("playerHandle", DESC_NMS_ENTITY_PLAYER, null, l2, l5, 2);
        mv.visitLocalVariable("tileEntity", DESC_ANVIL_TILE_ENTITY_IMPL, null, l3, l5, 3);
        mv.visitMaxs(5, 4);
      }
      mv.visitEnd();
    }
    {
      MethodVisitor mv = cw.visitMethod(ACC_PROTECTED, "getHandle", "()Ljava/lang/Object;", null, null);
      mv.visitCode();
      Label l0 = new Label();
      mv.visitLabel(l0);
      mv.visitLineNumber(34, l0);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitFieldInsn(GETFIELD, NAME_ANVIL_IMPL, "handle", DESC_ANVIL_CONTAINER_IMPL);
      mv.visitInsn(ARETURN);
      Label l1 = new Label();
      mv.visitLabel(l1);
      mv.visitLocalVariable("this", DESC_ANVIL_IMPL, null, l0, l1, 0);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
    }
    {
      MethodVisitor mv = cw.visitMethod(ACC_PROTECTED, "setHandle", "(Ljava/lang/Object;)V", null, null);
      mv.visitCode();
      Label l0 = new Label();
      mv.visitLabel(l0);
      mv.visitLineNumber(39, l0);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitVarInsn(ALOAD, 1);
      mv.visitTypeInsn(CHECKCAST, NAME_ANVIL_CONTAINER_IMPL);
      mv.visitFieldInsn(PUTFIELD, NAME_ANVIL_IMPL, "handle", DESC_ANVIL_CONTAINER_IMPL);
      Label l1 = new Label();
      mv.visitLabel(l1);
      mv.visitLineNumber(40, l1);
      mv.visitInsn(RETURN);
      Label l2 = new Label();
      mv.visitLabel(l2);
      mv.visitLocalVariable("this", DESC_ANVIL_IMPL, null, l0, l2, 0);
      mv.visitLocalVariable("handle", "Ljava/lang/Object;", null, l0, l2, 1);
      mv.visitMaxs(2, 2);
      mv.visitEnd();
    }
    {
      MethodVisitor mv = cw.visitMethod(ACC_PROTECTED, "getInventory", "()Lorg/bukkit/inventory/Inventory;", null, null);
      mv.visitCode();
      Label l0 = new Label();
      mv.visitLabel(l0);
      mv.visitLineNumber(44, l0);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitFieldInsn(GETFIELD, NAME_ANVIL_IMPL, "handle", DESC_ANVIL_CONTAINER_IMPL);
      {
        Class<?> craftInventoryView = loadOBCClass("inventory.CraftInventoryView");
        mv.visitMethodInsn(INVOKEVIRTUAL, NAME_ANVIL_CONTAINER_IMPL, "getBukkitView", "()" + classDesc(craftInventoryView), false);
        mv.visitMethodInsn(INVOKEVIRTUAL, className(craftInventoryView), "getTopInventory", "()Lorg/bukkit/inventory/Inventory;", false);
      }
      mv.visitInsn(ARETURN);
      Label l1 = new Label();
      mv.visitLabel(l1);
      mv.visitLocalVariable("this", DESC_ANVIL_IMPL, null, l0, l1, 0);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
    }
    cw.visitInnerClass(NAME_ANVIL_CONTAINER_IMPL, "AnvilWindowImpl", "ContainerImpl", ACC_PUBLIC);
    cw.visitInnerClass(NAME_ANVIL_TILE_ENTITY_IMPL, "AnvilWindowImpl", "TileEntityImpl", ACC_PUBLIC);
    return cw.toByteArray();
  }

  /// utils

  private static String className(Class<?> clazz) {
    return className(clazz.getName());
  }

  private static String className(String clazz) {
    return clazz.replace('.', '/');
  }

  private static String classDesc(Class<?> clazz) {
    return classDesc(className(clazz));
  }

  private static String classDesc(String className) {
    return 'L' + className(className) + ';';
  }

  private static Class<?> loadNMSClass(String name) {
    try {
      return MinecraftReflection.getMinecraftClass(name);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Class '" + name + "' does not exist:", e);
    }
  }

  private static String loadNMSClassName(String name) {
    return className(loadNMSClass(name));
  }

  private static Class<?> loadOBCClass(String name) {
    try {
      return MinecraftReflection.getCraftBukkitClass(name);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Class '" + name + "' does not exist:", e);
    }
  }

  @SuppressWarnings("SameParameterValue")
  private static String loadOBCClassName(String name) {
    return className(loadOBCClass(name));
  }

  private static String remappingField(Class<?> clazz, String name) {
    if (MinecraftReflection.REMAPPER == null) return name;
    return MinecraftReflection.REMAPPER.mapFieldName(clazz, name);
  }

  private static String remappingMethod(Class<?> clazz, String name, Class<?>[] parameterTypes) {
    if (MinecraftReflection.REMAPPER == null) return name;
    return MinecraftReflection.REMAPPER.mapMethodName(clazz, name, parameterTypes);
  }
}
