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

package com.lgou2w.ldk.bukkit.version;

import com.lgou2w.ldk.common.Version;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BukkitVersion extends Version implements Comparable<Version> {

  public BukkitVersion(int major, int minor, int build) {
    super(major, minor, build);
  }

  @Override
  @NotNull
  public String getVersionString() {
    return "v" + major + "_" + minor + "_R" + build;
  }

  @Override
  public String toString() {
    return "BukkitVersion{" +
      "major=" + major +
      ", minor=" + minor +
      ", build=" + build +
      '}';
  }

  public final static BukkitVersion UNKNOWN = new BukkitVersion(-1, -1, -1);
  public final static BukkitVersion V1_8_R1 = new BukkitVersion(1, 8, 1);
  public final static BukkitVersion V1_8_R2 = new BukkitVersion(1, 8, 2);
  public final static BukkitVersion V1_8_R3 = new BukkitVersion(1, 8, 3);
  public final static BukkitVersion V1_9_R1 = new BukkitVersion(1, 9, 1);
  public final static BukkitVersion V1_9_R2 = new BukkitVersion(1, 9, 2);
  public final static BukkitVersion V1_10_R1 = new BukkitVersion(1, 10, 1);
  public final static BukkitVersion V1_11_R1 = new BukkitVersion(1, 11, 1);
  public final static BukkitVersion V1_12_R1 = new BukkitVersion(1, 12, 1);
  public final static BukkitVersion V1_13_R1 = new BukkitVersion(1, 13, 1);
  public final static BukkitVersion V1_14_R1 = new BukkitVersion(1, 14, 1);
  public final static BukkitVersion V1_15_R1 = new BukkitVersion(1, 15, 1);
  public final static BukkitVersion V1_16_R1 = new BukkitVersion(1, 16, 1);
  public final static BukkitVersion V1_16_R2 = new BukkitVersion(1, 16, 2);
  public final static BukkitVersion V1_16_R3 = new BukkitVersion(1, 16, 3);
  /** TODO: Draft */
  @Deprecated
  public final static BukkitVersion V1_17_R1 = new BukkitVersion(1, 17, 1);
  public final static BukkitVersion CURRENT;
  public final static boolean isV18OrLater;
  public final static boolean isV19OrLater;
  public final static boolean isV110OrLater;
  public final static boolean isV111OrLater;
  public final static boolean isV112OrLater;
  public final static boolean isV113OrLater;
  public final static boolean isV114OrLater;
  public final static boolean isV115OrLater;
  public final static boolean isV116OrLater;
  /** TODO: Draft */
  @Deprecated
  public final static boolean isV117OrLater;

  /// Remapper
  public final static boolean isSpigot;
  public final static boolean isPaper;
  public final static boolean isArclight;
  public final static boolean isCatServer;
  public final static boolean isMohist;
  ///

  private final static Pattern VERSION_PATTERN = Pattern.compile("(?i)^v(\\d+)_(\\d+)_r(\\d+)$");

  static {
    BukkitVersion current = UNKNOWN;
    String[] versions = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
    String versionString = versions[versions.length - 1];
    Matcher matcher = VERSION_PATTERN.matcher(versionString);
    if (!matcher.matches()) {
      Bukkit.getLogger().log(Level.SEVERE, "[LDK] Unable to match bukkit version: " + versionString);
    } else {
      try {
        current = new BukkitVersion(
          Integer.parseInt(matcher.group(1)),
          Integer.parseInt(matcher.group(2)),
          Integer.parseInt(matcher.group(3))
        );
      } catch (NumberFormatException e) {
        Bukkit.getLogger().log(Level.SEVERE, "[LDK] Unable to parse bukkit version: " + versionString, e);
      }
    }
    CURRENT = current;
    isV18OrLater = CURRENT.compareTo(V1_8_R1) >= 0;
    isV19OrLater = CURRENT.compareTo(V1_9_R1) >= 0;
    isV110OrLater = CURRENT.compareTo(V1_10_R1) >= 0;
    isV111OrLater = CURRENT.compareTo(V1_11_R1) >= 0;
    isV112OrLater = CURRENT.compareTo(V1_12_R1) >= 0;
    isV113OrLater = CURRENT.compareTo(V1_13_R1) >= 0;
    isV114OrLater = CURRENT.compareTo(V1_14_R1) >= 0;
    isV115OrLater = CURRENT.compareTo(V1_15_R1) >= 0;
    isV116OrLater = CURRENT.compareTo(V1_16_R1) >= 0;
    isV117OrLater = CURRENT.compareTo(V1_17_R1) >= 0;

    /// Remapper
    isSpigot = existClasses("org.bukkit.craftbukkit." + CURRENT.getVersionString() + ".Spigot", "org.spigotmc.SpigotConfig");
    isPaper = existClasses("com.destroystokyo.paper.PaperConfig", "com.destroystokyo.paper.PaperMCConfig");
    isArclight = existClasses("io.izzel.arclight.common.ArclightMain");
    isCatServer = existClasses("catserver.server.CatServer");
    isMohist = existClasses("com.mohistmc.MohistMC");
  }

  private static boolean existClasses(String... classes) {
    for (String clazz : classes) {
      try {
        Class.forName(clazz);
        return true;
      } catch (ClassNotFoundException ignore) {
      }
    }
    return false;
  }
}
