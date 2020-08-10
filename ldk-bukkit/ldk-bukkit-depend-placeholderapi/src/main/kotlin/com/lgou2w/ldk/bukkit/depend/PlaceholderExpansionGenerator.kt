/*
 * Copyright (C) 2016-2020 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.bukkit.depend

import com.lgou2w.ldk.asm.ASMClassGenerator
import org.bukkit.Bukkit
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes

@Deprecated("INTERNAL_ONLY")
internal object PlaceholderExpansionGenerator : ASMClassGenerator {

  // PlaceholderExpansionProxy.java - Copyright (C) 2016-2020 The lgou2w <lgou2w@hotmail.com>
  /*
  package com.lgou2w.ldk.bukkit.depend;

  import org.bukkit.entity.Player;

  import java.util.List;

  public class PlaceholderExpansionProxy extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private final PlaceholderExpansion proxy;

    public PlaceholderExpansionProxy(PlaceholderExpansion proxy) {
      this.proxy = proxy;
    }

    @Override
    public String getIdentifier() {
      return proxy.getIdentifier();
    }

    @Override
    public String getRequiredPlugin() {
      return proxy.getRequiredPlugin();
    }

    @Override
    public String getAuthor() {
      return proxy.getAuthor();
    }

    @Override
    public String getVersion() {
      return proxy.getVersion();
    }

    @Override
    public List<String> getPlaceholders() {
      return proxy.getPlaceholders();
    }

    @Override
    public boolean persist() {
      return proxy.isPersist();
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
      return proxy.onPlaceholderRequest(p, params);
    }

    @Override
    public boolean register() {
      return proxy.register();
    }

    @Override
    public boolean canRegister() {
      return proxy.canRegister();
    }

    @Override
    public boolean isRegistered() {
      return proxy.isRegistered();
    }

    @Override
    public String getPlugin() {
      return proxy.getRequiredPlugin();
    }
  }
  */

  private const val NAME_M_PE = "me/clip/placeholderapi/expansion/PlaceholderExpansion"
  private const val NAME_PEP = "com/lgou2w/ldk/bukkit/depend/PlaceholderExpansionProxy"
  private const val NAME_PE = "com/lgou2w/ldk/bukkit/depend/PlaceholderExpansion"
  private const val DESC_PEP = "L$NAME_PEP;"
  private const val DESC_PE = "L$NAME_PE;"

  private fun shouldDefineRegister (): Boolean {
    val version = Bukkit.getPluginManager().getPlugin(DependPlaceholderAPI.NAME)?.description?.version
    return version != null && version < "2.10.7"
  }

  override fun generate(): Map<String, ByteArray> {
    val classes = LinkedHashMap<String, ByteArray>(1)
    val placeholderExpansionProxy = ClassWriter(0x00).apply {
      visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, NAME_PEP, null, NAME_M_PE, null)
      visitSource("PlaceholderExpansionProxy.java", null)
      visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL, "proxy", DESC_PE, null, null).visitEnd()
      visitMethod(Opcodes.ACC_PUBLIC, "<init>", "($DESC_PE)V", null, null).apply {
        visitCode()
        val l0 = Label()
        visitLabel(l0)
        visitLineNumber(27, l0)
        visitVarInsn(Opcodes.ALOAD, 0)
        visitMethodInsn(Opcodes.INVOKESPECIAL, NAME_M_PE, "<init>", "()V", false)
        val l1 = Label()
        visitLabel(l1)
        visitLineNumber(28, l1)
        visitVarInsn(Opcodes.ALOAD, 0)
        visitVarInsn(Opcodes.ALOAD, 1)
        visitFieldInsn(Opcodes.PUTFIELD, NAME_PEP, "proxy", DESC_PE)
        val l2 = Label()
        visitLabel(l2)
        visitLineNumber(29, l2)
        visitInsn(Opcodes.RETURN)
        val l3 = Label()
        visitLabel(l3)
        visitLocalVariable("this", DESC_PEP, null, l0, l3, 0)
        visitLocalVariable("proxy", DESC_PE, null, l0, l3, 1)
        visitMaxs(2, 2)
        visitEnd()
      }
      visitMethod(Opcodes.ACC_PUBLIC, "getIdentifier", "()Ljava/lang/String;", null, null).apply {
        visitCode()
        val l0 = Label()
        visitLabel(l0)
        visitLineNumber(33, l0)
        visitVarInsn(Opcodes.ALOAD, 0)
        visitFieldInsn(Opcodes.GETFIELD, NAME_PEP, "proxy", DESC_PE)
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, NAME_PE, "getIdentifier", "()Ljava/lang/String;", false)
        visitInsn(Opcodes.ARETURN)
        val l1 = Label()
        visitLabel(l1)
        visitLocalVariable("this", DESC_PEP, null, l0, l1, 0)
        visitMaxs(1, 1)
        visitEnd()
      }
      visitMethod(Opcodes.ACC_PUBLIC, "getRequiredPlugin", "()Ljava/lang/String;", null, null).apply {
        visitCode()
        val l0 = Label()
        visitLabel(l0)
        visitLineNumber(38, l0)
        visitVarInsn(Opcodes.ALOAD, 0)
        visitFieldInsn(Opcodes.GETFIELD, NAME_PEP, "proxy", DESC_PE)
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, NAME_PE, "getRequiredPlugin", "()Ljava/lang/String;", false)
        visitInsn(Opcodes.ARETURN)
        val l1 = Label()
        visitLabel(l1)
        visitLocalVariable("this", DESC_PEP, null, l0, l1, 0)
        visitMaxs(1, 1)
        visitEnd()
      }
      visitMethod(Opcodes.ACC_PUBLIC, "getAuthor", "()Ljava/lang/String;", null, null).apply {
        visitCode()
        val l0 = Label()
        visitLabel(l0)
        visitLineNumber(43, l0)
        visitVarInsn(Opcodes.ALOAD, 0)
        visitFieldInsn(Opcodes.GETFIELD, NAME_PEP, "proxy", DESC_PE)
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, NAME_PE, "getAuthor", "()Ljava/lang/String;", false)
        visitInsn(Opcodes.ARETURN)
        val l1 = Label()
        visitLabel(l1)
        visitLocalVariable("this", DESC_PEP, null, l0, l1, 0)
        visitMaxs(1, 1)
        visitEnd()
      }
      visitMethod(Opcodes.ACC_PUBLIC, "getVersion", "()Ljava/lang/String;", null, null).apply {
        visitCode()
        val l0 = Label()
        visitLabel(l0)
        visitLineNumber(48, l0)
        visitVarInsn(Opcodes.ALOAD, 0)
        visitFieldInsn(Opcodes.GETFIELD, NAME_PEP, "proxy", DESC_PE)
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, NAME_PE, "getVersion", "()Ljava/lang/String;", false)
        visitInsn(Opcodes.ARETURN)
        val l1 = Label()
        visitLabel(l1)
        visitLocalVariable("this", DESC_PEP, null, l0, l1, 0)
        visitMaxs(1, 1)
        visitEnd()
      }
      visitMethod(Opcodes.ACC_PUBLIC, "getPlaceholders", "()Ljava/util/List;", "()Ljava/util/List<Ljava/lang/String;>;", null).apply {
        visitCode()
        val l0 = Label()
        visitLabel(l0)
        visitLineNumber(53, l0)
        visitVarInsn(Opcodes.ALOAD, 0)
        visitFieldInsn(Opcodes.GETFIELD, NAME_PEP, "proxy", DESC_PE)
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, NAME_PE, "getPlaceholders", "()Ljava/util/List;", false)
        visitInsn(Opcodes.ARETURN)
        val l1 = Label()
        visitLabel(l1)
        visitLocalVariable("this", DESC_PEP, null, l0, l1, 0)
        visitMaxs(1, 1)
        visitEnd()
      }
      visitMethod(Opcodes.ACC_PUBLIC, "persist", "()Z", null, null).apply {
        visitCode()
        val l0 = Label()
        visitLabel(l0)
        visitLineNumber(58, l0)
        visitVarInsn(Opcodes.ALOAD, 0)
        visitFieldInsn(Opcodes.GETFIELD, NAME_PEP, "proxy", DESC_PE)
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, NAME_PE, "isPersist", "()Z", false)
        visitInsn(Opcodes.IRETURN)
        val l1 = Label()
        visitLabel(l1)
        visitLocalVariable("this", DESC_PEP, null, l0, l1, 0)
        visitMaxs(1, 1)
        visitEnd()
      }
      visitMethod(Opcodes.ACC_PUBLIC, "onPlaceholderRequest", "(Lorg/bukkit/entity/Player;Ljava/lang/String;)Ljava/lang/String;", null, null).apply {
        visitCode()
        val l0 = Label()
        visitLabel(l0)
        visitLineNumber(63, l0)
        visitVarInsn(Opcodes.ALOAD, 0)
        visitFieldInsn(Opcodes.GETFIELD, NAME_PEP, "proxy", DESC_PE)
        visitVarInsn(Opcodes.ALOAD, 1)
        visitVarInsn(Opcodes.ALOAD, 2)
        visitMethodInsn(Opcodes.INVOKEVIRTUAL, NAME_PE, "onPlaceholderRequest", "(Lorg/bukkit/entity/Player;Ljava/lang/String;)Ljava/lang/String;", false)
        visitInsn(Opcodes.ARETURN)
        val l1 = Label()
        visitLabel(l1)
        visitLocalVariable("this", DESC_PEP, null, l0, l1, 0)
        visitLocalVariable("p", "Lorg/bukkit/entity/Player;", null, l0, l1, 1)
        visitLocalVariable("params", "Ljava/lang/String;", null, l0, l1, 2)
        visitMaxs(3, 3)
        visitEnd()
      }

      // Before PlaceholderAPI 2.10.7
      // See: https://github.com/PlaceholderAPI/PlaceholderAPI/releases/tag/2.10.7
      if (shouldDefineRegister()) {
        visitMethod(Opcodes.ACC_PUBLIC, "register", "()Z", null, null).apply {
          visitCode()
          val l0 = Label()
          visitLabel(l0)
          visitLineNumber(68, l0)
          visitVarInsn(Opcodes.ALOAD, 0)
          visitFieldInsn(Opcodes.GETFIELD, NAME_PEP, "proxy", DESC_PE)
          visitMethodInsn(Opcodes.INVOKEVIRTUAL, NAME_PE, "register", "()Z", false)
          visitInsn(Opcodes.IRETURN)
          val l1 = Label()
          visitLabel(l1)
          visitLocalVariable("this", DESC_PEP, null, l0, l1, 0)
          visitMaxs(1, 1)
          visitEnd()
        }
        visitMethod(Opcodes.ACC_PUBLIC, "canRegister", "()Z", null, null).apply {
          visitCode()
          val l0 = Label()
          visitLabel(l0)
          visitLineNumber(73, l0)
          visitVarInsn(Opcodes.ALOAD, 0)
          visitFieldInsn(Opcodes.GETFIELD, NAME_PEP, "proxy", DESC_PE)
          visitMethodInsn(Opcodes.INVOKEVIRTUAL, NAME_PE, "canRegister", "()Z", false)
          visitInsn(Opcodes.IRETURN)
          val l1 = Label()
          visitLabel(l1)
          visitLocalVariable("this", DESC_PEP, null, l0, l1, 0)
          visitMaxs(1, 1)
          visitEnd()
        }
        visitMethod(Opcodes.ACC_PUBLIC, "isRegistered", "()Z", null, null).apply {
          visitCode()
          val l0 = Label()
          visitLabel(l0)
          visitLineNumber(78, l0)
          visitVarInsn(Opcodes.ALOAD, 0)
          visitFieldInsn(Opcodes.GETFIELD, NAME_PEP, "proxy", DESC_PE)
          visitMethodInsn(Opcodes.INVOKEVIRTUAL, NAME_PE, "isRegistered", "()Z", false)
          visitInsn(Opcodes.IRETURN)
          val l1 = Label()
          visitLabel(l1)
          visitLocalVariable("this", DESC_PEP, null, l0, l1, 0)
          visitMaxs(1, 1)
          visitEnd()
        }
        visitMethod(Opcodes.ACC_PUBLIC, "getPlugin", "()Ljava/lang/String;", null, null).apply {
          visitCode()
          val l0 = Label()
          visitLabel(l0)
          visitLineNumber(83, l0)
          visitVarInsn(Opcodes.ALOAD, 0)
          visitFieldInsn(Opcodes.GETFIELD, NAME_PEP, "proxy", DESC_PE)
          visitMethodInsn(Opcodes.INVOKEVIRTUAL, NAME_PE, "getRequiredPlugin", "()Ljava/lang/String;", false)
          visitInsn(Opcodes.ARETURN)
          val l1 = Label()
          visitLabel(l1)
          visitLocalVariable("this", DESC_PEP, null, l0, l1, 0)
          visitMaxs(1, 1)
          visitEnd()
        }
      }

    }.toByteArray()
    classes["com.lgou2w.ldk.bukkit.depend.PlaceholderExpansionProxy"] = placeholderExpansionProxy
    return classes
  }
}
