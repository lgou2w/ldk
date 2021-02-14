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

import com.lgou2w.ldk.common.ComparisonChain;
import com.lgou2w.ldk.common.Version;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MinecraftVersion extends Version implements Comparable<Version> {
  private final @Nullable Integer pre;

  public MinecraftVersion(int major, int minor, int build, @Nullable Integer pre) {
    super(major, minor, build);
    this.pre = pre;
  }

  public MinecraftVersion(int major, int minor, int build) {
    this(major, minor, build, null);
  }

  public boolean isPre() {
    return pre != null;
  }

  @Override
  @NotNull
  public String getVersionString() {
    String parent = super.getVersionString();
    return pre != null ? parent + "-pre" + pre : parent;
  }

  @Override
  public int compareTo(@NotNull Version o) {
    if (o instanceof MinecraftVersion) {
      MinecraftVersion version = (MinecraftVersion) o;
      return ComparisonChain.start()
        .compare(major, version.major)
        .compare(minor, version.minor)
        .compare(build, version.build)
        .compare(pre != null ? pre : -1, version.pre != null ? version.pre : -1)
        .result();
    } else return super.compareTo(o);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    MinecraftVersion that = (MinecraftVersion) o;
    return Objects.equals(pre, that.pre);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), pre);
  }

  @Override
  public String toString() {
    return "MinecraftVersion{" +
      "major=" + major +
      ", minor=" + minor +
      ", build=" + build +
      ", pre=" + pre +
      '}';
  }

  public final static MinecraftVersion UNKNOWN = new MinecraftVersion(-1, -1, -1, -1);
  public final static MinecraftVersion V1_8 = new MinecraftVersion(1, 8, 0);
  public final static MinecraftVersion V1_9 = new MinecraftVersion(1, 9, 0);
  public final static MinecraftVersion V1_10 = new MinecraftVersion(1, 10, 0);
  public final static MinecraftVersion V1_11 = new MinecraftVersion(1, 11, 0);
  public final static MinecraftVersion V1_12 = new MinecraftVersion(1, 12, 0);
  public final static MinecraftVersion V1_13 = new MinecraftVersion(1, 13, 0);
  public final static MinecraftVersion V1_14 = new MinecraftVersion(1, 14, 0);
  public final static MinecraftVersion V1_15 = new MinecraftVersion(1, 15, 0);
  public final static MinecraftVersion V1_16 = new MinecraftVersion(1, 16, 0);
  /** TODO: Draft */
  @Deprecated
  public final static MinecraftVersion V1_17 = new MinecraftVersion(1, 17, 0);
  public final static MinecraftVersion CURRENT;

  private final static Pattern VERSION_PATTERN = Pattern.compile(".*\\(.*MC:?\\s([a-zA-Z0-9\\-.\\s]+).*\\)");

  static {
    MinecraftVersion current = UNKNOWN;
    String minecraftVersion = Bukkit.getServer().getVersion().trim();
    Matcher matcher = VERSION_PATTERN.matcher(minecraftVersion);
    if (!matcher.matches() || matcher.group(1) == null) {
      Bukkit.getLogger().log(Level.SEVERE, "[LDK] Unable to match minecraft version: " + minecraftVersion);
    } else {
      String versionString = matcher.group(1);
      int preIndex = versionString.lastIndexOf("-pre");
      int[] numbers = { 0, 0, 0 };
      String[] elements;
      Integer pre;
      try {
        if (preIndex == -1) {
          String[] versions = versionString.split(" ");
          pre = versions.length > 2 ? Integer.parseInt(versions[2]) : null;
          elements = versions[0].split("\\.");
        } else {
          pre = Integer.parseInt(versionString.substring(preIndex + 4));
          elements = versionString.substring(0, preIndex).split("\\.");
        }
        int size = Math.min(numbers.length, elements.length);
        for (int i = 0; i < size; i++)
          numbers[i] = Integer.parseInt(elements[i].trim());
        current = new MinecraftVersion(numbers[0], numbers[1], numbers[2], pre);
      } catch (Exception e) {
        Bukkit.getLogger().log(Level.SEVERE, "[LDK] Unable to parse minecraft version: " + versionString, e);
      }
    }
    CURRENT = current;
  }
}
