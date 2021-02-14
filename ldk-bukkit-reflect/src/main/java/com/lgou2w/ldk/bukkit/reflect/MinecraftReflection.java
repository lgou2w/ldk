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

import com.lgou2w.ldk.bukkit.version.BukkitVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public final class MinecraftReflection {

  private MinecraftReflection() { }

  public final static String PACKAGE_CRAFTBUKKIT;
  public final static String PACKAGE_MINECRAFT;

  private final static ClassSource SOURCE;
  private final static PackageCached CACHED_CRAFTBUKKIT;
  private final static PackageCached CACHED_MINECRAFT;

  static {
    PACKAGE_CRAFTBUKKIT = "org.bukkit.craftbukkit." + BukkitVersion.CURRENT.getVersionString();
    PACKAGE_MINECRAFT = "net.minecraft.server." + BukkitVersion.CURRENT.getVersionString();
    SOURCE = ClassSource.fromClassLoader(MinecraftReflection.class.getClassLoader());
    CACHED_CRAFTBUKKIT = new PackageCached(PACKAGE_CRAFTBUKKIT, SOURCE);
    CACHED_MINECRAFT = new PackageCached(PACKAGE_MINECRAFT, SOURCE);
  }

  @NotNull
  public static Class<?> getMinecraftClass(@NotNull String className) throws ClassNotFoundException {
    return CACHED_MINECRAFT.getPackageClass(className);
  }

// TODO：待定是否公开此方法
//
//  @Nullable
//  @Contract("_, null -> null; _, !null -> !null")
//  public static Class<?> setMinecraftClass(@NotNull String className, @Nullable Class<?> clazz) {
//    CACHED_MINECRAFT.setPackageClass(className, clazz);
//    return clazz;
//  }

  @NotNull
  public static Class<?> getMinecraftClass(
    @NotNull String className,
    @NotNull String... aliases
  ) throws ClassNotFoundException {
    try {
      return getMinecraftClass(className);
    } catch (ClassNotFoundException e) {
      Class<?> result = null;
      for (String alias : aliases) {
        try {
          result = getMinecraftClass(alias);
        } catch (ClassNotFoundException ignored) {
        }
      }
      if (result == null)
        throw new ClassNotFoundException("The class for " + className + " and the alias " + Arrays.toString(aliases) + " was not found.", e);
      CACHED_MINECRAFT.setPackageClass(className, result);
      return result;
    }
  }

  @Nullable
  public static Class<?> getMinecraftClassOrNull(@NotNull String className) {
    try {
      return getMinecraftClass(className);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  @Nullable
  public static Class<?> getMinecraftClassOrNull(
    @NotNull String className,
    @NotNull String... aliases
  ) {
    try {
      return getMinecraftClass(className, aliases);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  @NotNull
  public static Class<?> getCraftBukkitClass(@NotNull String className) throws ClassNotFoundException {
    return CACHED_CRAFTBUKKIT.getPackageClass(className);
  }

  @Nullable
  public static Class<?> getCraftBukkitClassOrNull(@NotNull String className) {
    try {
      return getCraftBukkitClass(className);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }
}
