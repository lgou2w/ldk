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

package com.lgou2w.ldk.bukkit.reflect;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PackageCached {
  private final Map<String, Optional<Class<?>>> cached;
  private final ClassSource source;
  private final String packageName;

  @Contract("null, _ -> fail; _, null -> fail")
  public PackageCached(String packageName, ClassSource source) {
    this.packageName = Objects.requireNonNull(packageName, "packageName");
    this.source = Objects.requireNonNull(source, "source");
    this.cached = new ConcurrentHashMap<>();
  }

  @NotNull
  public Class<?> getPackageClass(@NotNull String className) throws ClassNotFoundException {
    if (cached.containsKey(className)) {
      Optional<Class<?>> result = cached.get(className);
      if (result == null || !result.isPresent())
        throw new ClassNotFoundException("Unable to find class: " + className);
      return result.get();
    }
    try {
      Class<?> clazz = source.loadClass(combine(packageName, className));
      cached.put(className, Optional.of(clazz));
      return clazz;
    } catch (ClassNotFoundException e) {
      cached.put(className, Optional.empty());
      throw e;
    }
  }

  public void setPackageClass(@NotNull String className, @Nullable Class<?> clazz) {
    if (clazz != null) cached.put(className, Optional.of(clazz));
    else cached.remove(className);
  }

  public void clear() {
    cached.clear();
  }

  private String combine(String packageName, String className) {
    if (packageName.isEmpty()) return className;
    if (className.isEmpty()) return packageName;
    return packageName + "." + className;
  }
}
