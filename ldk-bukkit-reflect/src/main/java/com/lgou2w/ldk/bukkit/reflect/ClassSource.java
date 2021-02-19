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

import java.util.Map;

public abstract class ClassSource {

  @NotNull
  public abstract Class<?> loadClass(@NotNull String name) throws ClassNotFoundException;

  @NotNull
  @Contract("null -> fail; !null -> new")
  public static ClassSource fromClassLoader(ClassLoader classLoader) {
    if (classLoader == null) throw new NullPointerException("classLoader");
    return new ClassSource() {
      private final ClassLoader loader = classLoader;

      @Override
      public @NotNull Class<?> loadClass(@NotNull String name) throws ClassNotFoundException {
        return loader.loadClass(name);
      }
    };
  }

  @NotNull
  public static ClassSource fromClassLoader() {
    return fromClassLoader(ClassSource.class.getClassLoader());
  }

  @NotNull
  @Contract("null -> fail; !null -> new")
  public static ClassSource fromMap(Map<String, Class<?>> map) {
    if (map == null) throw new NullPointerException("map");
    return new ClassSource() {
      private final Map<String, Class<?>> cache = map;

      @Override
      @NotNull
      public Class<?> loadClass(@NotNull String name) throws ClassNotFoundException {
        Class<?> clazz = cache.get(name);
        if (clazz == null) throw new ClassNotFoundException("The specified class does not exist in map.");
        return clazz;
      }
    };
  }
}
