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
import com.lgou2w.ldk.bukkit.version.MinecraftVersion;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public final class MinecraftReflection {

  private MinecraftReflection() { }

  public final static String PACKAGE_CRAFTBUKKIT;
  public final static String PACKAGE_MINECRAFT;

  // TODO: 是否公开一些字段
  private final static ClassLoader LOADER;
  private final static ClassSource SOURCE;
  private final static PackageCached CACHED_CRAFTBUKKIT;
  private final static PackageCached CACHED_MINECRAFT;

  /// Remapper
  @Nullable private final static Remapper REMAPPER;

  static {
    PACKAGE_CRAFTBUKKIT = "org.bukkit.craftbukkit." + BukkitVersion.CURRENT.getVersionString();
    PACKAGE_MINECRAFT = "net.minecraft.server." + BukkitVersion.CURRENT.getVersionString();
    LOADER = MinecraftReflection.class.getClassLoader();
    SOURCE = ClassSource.fromClassLoader(LOADER);
    CACHED_CRAFTBUKKIT = new PackageCached(PACKAGE_CRAFTBUKKIT, SOURCE);
    CACHED_MINECRAFT = new PackageCached(PACKAGE_MINECRAFT, SOURCE);

    /// Remapper
    Remapper remapper = null;
    if (BukkitVersion.isArclight) {
      remapper = new ArclightRemapper(LOADER);
    } else if (BukkitVersion.isCatServer) {
      remapper = new CatServerRemapper(LOADER);
    } else if (BukkitVersion.isMohist) {
      remapper = new MohistRemapper(LOADER);
    } else if (BukkitVersion.isMagma) {
      remapper = new MagmaRemapper(LOADER);
    }
    if (remapper != null) {
      Bukkit.getLogger().info("[LDK] --- MinecraftReflection ---");
      Bukkit.getLogger().info("[LDK] Server: " + remapper.getServerName() + "-" + MinecraftVersion.CURRENT.getVersionString());
      Bukkit.getLogger().info("[LDK] Using remapper: " + remapper.toString());
    }
    REMAPPER = remapper;
  }

// TODO: 待定是否公开此方法
//
//  @Nullable
//  public static Remapper getRemapper() {
//    return REMAPPER;
//  }

  @NotNull
  @Contract("null -> fail")
  public static Class<?> getMinecraftClass(String className) throws ClassNotFoundException {
    if (className == null) throw new NullPointerException("className");
    if (REMAPPER != null) className = REMAPPER.mapClassName(className); // remapper
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
  @Contract("null, _ -> fail; _, null -> fail")
  public static Class<?> getMinecraftClass(
    String className,
    String... aliases
  ) throws ClassNotFoundException {
    if (className == null) throw new NullPointerException("className");
    if (aliases == null) throw new NullPointerException("aliases");
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
  @Contract("null -> fail")
  public static Class<?> getMinecraftClassOrNull(String className) {
    try {
      return getMinecraftClass(className);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  @Nullable
  @Contract("null, _ -> fail; _, null -> fail")
  public static Class<?> getMinecraftClassOrNull(
    String className,
    String... aliases
  ) {
    try {
      return getMinecraftClass(className, aliases);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  @NotNull
  @Contract("null -> fail")
  public static Class<?> getCraftBukkitClass(String className) throws ClassNotFoundException {
    if (className == null) throw new NullPointerException("className");
    return CACHED_CRAFTBUKKIT.getPackageClass(className);
  }

  @Nullable
  @Contract("null -> fail")
  public static Class<?> getCraftBukkitClassOrNull(String className) {
    if (className == null) throw new NullPointerException("className");
    try {
      return getCraftBukkitClass(className);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }
}
