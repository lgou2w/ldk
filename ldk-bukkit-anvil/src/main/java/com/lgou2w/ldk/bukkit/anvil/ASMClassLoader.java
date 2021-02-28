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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** INTERNAL ONLY */
@Deprecated
final class ASMClassLoader extends ClassLoader {

  public final static ASMClassLoader INSTANCE = new ASMClassLoader();

  ASMClassLoader() {
    super(ASMClassLoader.class.getClassLoader());
  }

  @NotNull
  public Class<?> defineClass(@NotNull String name, byte @NotNull [] byteCodes) {
    return defineClass(name, byteCodes, 0, byteCodes.length, ASMClassLoader.class.getProtectionDomain());
  }

  @NotNull
  public List<Class<?>> defineClasses(@NotNull Map<String, byte[]> map) {
    List<Class<?>> result = new ArrayList<>(map.size());
    for (Map.Entry<String, byte[]> entry : map.entrySet()) {
      byte[] byteCodes = entry.getValue();
      Class<?> clazz = defineClass(entry.getKey(), byteCodes, 0, byteCodes.length, ASMClassLoader.class.getProtectionDomain());
      result.add(clazz);
    }
    return result;
  }
}
