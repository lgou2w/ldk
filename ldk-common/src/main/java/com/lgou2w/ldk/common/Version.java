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

package com.lgou2w.ldk.common;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version implements Comparable<Version> {
  protected final int major, minor, build;

  public Version(int major, int minor, int build) {
    this.major = major;
    this.minor = minor;
    this.build = build;
  }

  public final int getMajor() {
    return major;
  }

  public final int getMinor() {
    return minor;
  }

  public final int getBuild() {
    return build;
  }

  @NotNull
  public String getVersionString() {
    return major + "." + minor + "." + build;
  }

  @Override
  public int compareTo(@NotNull Version o) {
    return ComparisonChain.start()
      .compare(major, o.major)
      .compare(minor, o.minor)
      .compare(build, o.build)
      .result();
  }

  @Override
  public int hashCode() {
    return Objects.hash(major, minor, build);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Version version = (Version) o;
    return major == version.major && minor == version.minor && build == version.build;
  }

  @Override
  public String toString() {
    return "Version{" +
      "major=" + major +
      ", minor=" + minor +
      ", build=" + build +
      '}';
  }

  private final static Pattern VERSION_PATTERN = Pattern.compile("^(?<major>\\d+)\\.(?<minor>\\d+)\\.(?<build>\\d+)$");

  @NotNull
  public static Version parse(@NotNull String versionString) throws IllegalArgumentException {
    Matcher matcher = VERSION_PATTERN.matcher(versionString);
    if (!matcher.matches()) throw new IllegalArgumentException("Illegal version format, must be: x.y.z");
    return new Version(
      Integer.parseInt(matcher.group("major")),
      Integer.parseInt(matcher.group("minor")),
      Integer.parseInt(matcher.group("build"))
    );
  }

  @Nullable
  @Contract("null -> null")
  public static Version parseSafely(@Nullable String versionString) {
    if (versionString == null || versionString.isEmpty()) return null;
    try {
      return parse(versionString);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
