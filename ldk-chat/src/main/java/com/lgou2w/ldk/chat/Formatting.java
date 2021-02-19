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
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public enum Formatting {
  BLACK(        "BLACK",         '0', 0x000000),
  DARK_BLUE(    "DARK_BLUE",     '1', 0x0AA000),
  DARK_GREEN(   "DARK_GREEN",    '2', 0x00AA00),
  DARK_AQUA(    "DARK_AQUA",     '3', 0x00AAAA),
  DARK_RED(     "DARK_RED",      '4', 0xAA0000),
  DARK_PUPRLE(  "DARK_PURPLE",   '5', 0xAA00AA),
  GOLD(         "GOLD",          '6', 0xFFAA00),
  GRAY(         "GRAY",          '7', 0xAAAAAA),
  DARK_GRAY(    "DARK_GRAY",     '8', 0x555555),
  BLUE(         "BLUE",          '9', 0x5555FF),
  GREEN(        "GREEN",         'a', 0x55FF55),
  AQUA(         "AQUA",          'b', 0x55FFFF),
  RED(          "RED",           'c', 0xFF5555),
  LIGHT_PURPLE( "LIGHT_PURPLE",  'd', 0xFF55FF),
  YELLOW(       "YELLOW",        'e', 0xFFFF55),
  WHITE(        "WHITE",         'f', 0xFFFFFF),

  OBFUSCATED(   "OBFUSCATED",    'k', null, true),
  BOLD(         "BOLD",          'l', null, true),
  STRIKETHROUGH("STRIKETHROUGH", 'm', null, true),
  UNDERLINE(    "UNDERLINE",     'n', null, true),
  ITALIC(       "ITALIC",        'o', null, true),
  RESET(        "RESET",         'r', null, false)
  ;

  private final String name;
  private final String toString;
  private final char code;
  private final boolean isFormat;
  @Nullable private final Integer color;

  Formatting(String name, char code, int color) {
    this(name, code, color, false);
  }

  Formatting(String name, char code, @Nullable Integer color, boolean isFormat) {
    this.name = name;
    this.code = code;
    this.color = color;
    this.isFormat = isFormat;
    this.toString = FORMATTING_PREFIX + String.valueOf(code);
  }

  @NotNull
  public String getName() {
    return name.toLowerCase(Locale.ROOT);
  }

  public char getCode() {
    return code;
  }

  public boolean isFormat() {
    return isFormat;
  }

  public boolean isColor() {
    return !isFormat && this != RESET;
  }

  @Nullable
  public Integer getColor() {
    return color;
  }

  @Override
  public String toString() {
    return toString;
  }

  final static char FORMATTING_PREFIX = '§';
  final static String FORMATTING_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
  final static Pattern STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)" + FORMATTING_PREFIX + "[0-9A-FK-OR]");
  final static Map<Character, Formatting> CODE_MAP;
  final static Map<String, Formatting> NAME_MAP;

  static {
    Map<Character, Formatting> codeMap = new HashMap<>();
    Map<String, Formatting> nameMap = new HashMap<>();
    for (Formatting formatting : Formatting.values()) {
      codeMap.put(formatting.code, formatting);
      nameMap.put(cleanName(formatting.name), formatting);
    }
    CODE_MAP = Collections.unmodifiableMap(codeMap);
    NAME_MAP = Collections.unmodifiableMap(nameMap);
  }

  static String cleanName(String name) {
    return name.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
  }

  @Nullable
  @Contract("null -> null")
  public static Formatting fromName(@Nullable String name) {
    if (name == null) return null;
    String cleanName = cleanName(name);
    return NAME_MAP.get(cleanName);
  }

  @Nullable
  public static Formatting fromCode(char code) {
    return CODE_MAP.get(code);
  }

  @Contract("null -> null; !null -> !null")
  public static String stripFormatting(@Nullable String input) {
    if (input == null) return null;
    return STRIP_FORMATTING_PATTERN.matcher(input).replaceAll("");
  }

  @Contract("_, null -> null; _, !null -> !null")
  public static String translateAlternateFormattingCodes(char altPrefixChar, @Nullable String textToTranslate) {
    if (textToTranslate == null) return null;
    char[] chars = textToTranslate.toCharArray();
    for (int i = 0; i < chars.length - 1; i++) {
      if (chars[i] == altPrefixChar && FORMATTING_CODES.indexOf(chars[i + 1]) > 1) {
        chars[i] = FORMATTING_PREFIX;
        chars[i + 1] = Character.toLowerCase(chars[i + 1]);
      }
    }
    return String.valueOf(chars);
  }
}
