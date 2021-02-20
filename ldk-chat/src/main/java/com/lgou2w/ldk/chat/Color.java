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

package com.lgou2w.ldk.chat;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Color {
  private final int rgb;
  @Nullable private final String name;

  private Color(int rgb, @Nullable String name) {
    this.rgb = rgb;
    this.name = name;
  }

  private Color(int rgb) {
    this.rgb = rgb;
    this.name = null;
  }

  public int getValue() {
    return rgb;
  }

  @NotNull
  public String serialize() {
    return name != null ? name : formatValue();
  }

  @NotNull
  public String formatValue() {
    return String.format("#%06X", rgb);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Color color = (Color) o;
    return rgb == color.rgb && Objects.equals(name, color.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rgb, name);
  }

  @Override
  public String toString() {
    return "Color{" + serialize() + '}';
  }

  final static Map<Formatting, Color> FORMATTING_MAP;
  final static Map<String, Color> NAME_MAP;

  static {
    Map<Formatting, Color> formattingMap = new HashMap<>();
    Map<String, Color> nameMap = new HashMap<>();
    for (Formatting formatting : Formatting.values()) {
      if (!formatting.isColor()) continue;
      @SuppressWarnings("ConstantConditions")
      Color color = new Color(formatting.getColor(), formatting.getName());
      formattingMap.put(formatting, color);
    }
    for (Map.Entry<Formatting, Color> entry : formattingMap.entrySet()) {
      Color value = entry.getValue();
      nameMap.put(value.name, value);
    }
    FORMATTING_MAP = Collections.unmodifiableMap(formattingMap);
    NAME_MAP = Collections.unmodifiableMap(nameMap);
  }

  @Nullable
  @Contract("null -> null")
  public static Color fromFormatting(@Nullable Formatting formatting) {
    return FORMATTING_MAP.get(formatting);
  }

  @NotNull
  @Contract("_ -> new")
  public static Color fromRGB(int rgb) {
    return new Color(rgb);
  }

  @Nullable
  @Contract("null -> null")
  public static Color parse(String str) {
    if (str == null) return null;
    if (str.startsWith("#")) {
      try {
        int rgb = Integer.parseInt(str.substring(1), 16);
        return new Color(rgb);
      } catch (NumberFormatException e) {
        return null;
      }
    } else {
      return NAME_MAP.get(str);
    }
  }
}
