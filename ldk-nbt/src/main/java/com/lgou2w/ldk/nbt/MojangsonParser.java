/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT license.
 */

/*
 * Copyright (C) 2019-2021 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.nbt;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/*
 *  Modify: Mojang brigadier MojangsonParser and StringReader
 *  by lgou2w on 09/16/2019
 */

public final class MojangsonParser {
  private final @NotNull StringReader reader;

  private MojangsonParser(@NotNull StringReader reader) {
    this.reader = reader;
  }

  @NotNull
  @Contract("null -> fail; !null -> !null")
  public static BaseTag<?> parse(String mojangson) throws IllegalArgumentException {
    if (mojangson == null) throw new NullPointerException("mojangson");
    StringReader reader = new StringReader(mojangson);
    return new MojangsonParser(reader).readValue();
  }

  @NotNull
  @Contract("null -> fail; !null -> !null")
  public static CompoundTag parseCompound(String mojangson) throws IllegalArgumentException {
    if (mojangson == null) throw new NullPointerException("mojangson");
    StringReader reader = new StringReader(mojangson);
    return new MojangsonParser(reader).readSingleStruct();
  }

  @NotNull
  private CompoundTag readSingleStruct() {
    CompoundTag compound = readStruct();
    reader.skipWhitespace();
    if (reader.canRead()) throw new IllegalArgumentException("Unexpected trailing data.");
    return compound;
  }

  @NotNull
  private String readKey() {
    reader.skipWhitespace();
    if (!reader.canRead()) throw new IllegalArgumentException("Expected key.");
    return reader.readString();
  }

  @NotNull
  private BaseTag<?> readTypedValue() {
    reader.skipWhitespace();
    int start = reader.cursor;
    if (reader.peek() == StringReader.SYNTAX_DOUBLE_QUOTE)
      return new StringTag(reader.readQuotedString());
    String str = reader.readUnquotedString();
    if (str.isEmpty()) {
      reader.cursor = start;
      throw new IllegalArgumentException("Expected value.");
    }
    return parseString(str);
  }

  private final static Pattern PATTERN_DOUBLE_NOSUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", Pattern.CASE_INSENSITIVE);
  private final static Pattern PATTERN_DOUBLE = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", Pattern.CASE_INSENSITIVE);
  private final static Pattern PATTERN_FLOAT = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", Pattern.CASE_INSENSITIVE);
  private final static Pattern PATTERN_BYTE = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", Pattern.CASE_INSENSITIVE);
  private final static Pattern PATTERN_SHORT = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", Pattern.CASE_INSENSITIVE);
  private final static Pattern PATTERN_LONG = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", Pattern.CASE_INSENSITIVE);
  private final static Pattern PATTERN_INT = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)", Pattern.CASE_INSENSITIVE);

  @NotNull
  private BaseTag<?> parseString(String str) {
    try {
      if (PATTERN_FLOAT.matcher(str).matches())
        return new FloatTag(Float.parseFloat(str.substring(0, str.length() - 1)));
      if (PATTERN_BYTE.matcher(str).matches())
        return new ByteTag(Byte.parseByte(str.substring(0, str.length() - 1)));
      if (PATTERN_SHORT.matcher(str).matches())
        return new ShortTag(Short.parseShort(str.substring(0, str.length() - 1)));
      if (PATTERN_LONG.matcher(str).matches())
        return new LongTag(Long.parseLong(str.substring(0, str.length() - 1)));
      if (PATTERN_INT.matcher(str).matches())
        return new IntTag(Integer.parseInt(str));
      if (PATTERN_DOUBLE.matcher(str).matches())
        return new DoubleTag(Double.parseDouble(str.substring(0, str.length() - 1)));
      if (PATTERN_DOUBLE_NOSUFFIX.matcher(str).matches())
        return new DoubleTag(Double.parseDouble(str));
      if (str.equals("true"))
        return new ByteTag((byte) 1);
      if (str.equals("false"))
        return new ByteTag((byte) 0);
    } catch (NumberFormatException ignore) {
    }
    return new StringTag(str);
  }

  @NotNull
  private BaseTag<?> readValue() {
    reader.skipWhitespace();
    if (!reader.canRead()) throw new IllegalArgumentException("Expected value.");
    char c = reader.peek();
    if (c == '{') return readStruct();
    else if (c == '[') return readArrayOrList();
    else return readTypedValue();
  }

  @NotNull
  private BaseTag<?> readArrayOrList() {
    if (reader.canRead(3) &&
      reader.peek(1) != StringReader.SYNTAX_DOUBLE_QUOTE &&
      reader.peek(2) == ';') return readArray();
    else return readList();
  }

  @NotNull
  private CompoundTag readStruct() {
    expect('{');
    reader.skipWhitespace();
    CompoundTag compound = new CompoundTag();
    while (reader.canRead() && reader.peek() != '}') {
      int start = reader.cursor;
      String key = readKey();
      if (key.isEmpty()) {
        reader.cursor = start;
        throw new IllegalArgumentException("Expected key.");
      }
      expect(':');
      compound.put(key, readValue());
      if (!hasElementSeparator()) break;
      if (!reader.canRead()) throw new IllegalArgumentException("Expected key.");
    }
    expect('}');
    return compound;
  }

  @NotNull
  private BaseTag<?> readList() {
    expect('[');
    reader.skipWhitespace();
    if (!reader.canRead()) throw new IllegalArgumentException("Expected value.");
    ListTag list = new ListTag();
    TagType elementType = TagType.END;
    while (reader.peek() != ']') {
      int start = reader.cursor;
      BaseTag<?> element = readValue();
      if (elementType == TagType.END) elementType = element.getType();
      else if (element.getType() != elementType) {
        reader.cursor = start;
        throw new IllegalArgumentException("Can't insert '" + element.getType() + "' type into '" + elementType + "' element type list.");
      }
      list.add(element);
      if (!hasElementSeparator()) break;
      if (!reader.canRead()) throw new IllegalArgumentException("Expected value.");
    }
    expect(']');
    return list;
  }

  @NotNull
  private BaseTag<?> readArray() {
    expect('[');
    int start = reader.cursor;
    char c = reader.read();
    reader.read();
    reader.skipWhitespace();
    if (!reader.canRead()) throw new IllegalArgumentException("Expected value.");
    if (c == BaseTag.PREFIX_BYTE_ARRAY) {
      Number[] elements = readArrayElements(TagType.BYTE_ARRAY, TagType.BYTE);
      byte[] unboxes = new byte[elements.length];
      for (int i = 0; i < unboxes.length; i++) unboxes[i] = elements[i].byteValue();
      return new ByteArrayTag(unboxes);
    } else if (c == BaseTag.PREFIX_INT_ARRAY) {
      Number[] elements = readArrayElements(TagType.INT_ARRAY, TagType.INT);
      int[] unboxes = new int[elements.length];
      for (int i = 0; i < unboxes.length; i++) unboxes[i] = elements[i].intValue();
      return new IntArrayTag(unboxes);
    } else if (c == BaseTag.PREFIX_LONG_ARRAY) {
      Number[] elements = readArrayElements(TagType.LONG_ARRAY, TagType.LONG);
      long[] unboxes = new long[elements.length];
      for (int i = 0; i < unboxes.length; i++) unboxes[i] = elements[i].longValue();
      return new LongArrayTag(unboxes);
    } else {
      reader.cursor = start;
      throw new IllegalArgumentException("Invalid array type: " + c);
    }
  }

  @NotNull
  private Number[] readArrayElements(TagType arrayType, TagType elementType) {
    List<Number> elements = new ArrayList<>();
    while (true) {
      if (reader.peek() != ']') {
        int start = this.reader.cursor;
        @SuppressWarnings("unchecked")
        NumericTag<Number> base = (NumericTag<Number>) readValue();
        if (base.getType() != elementType) {
          reader.cursor = start;
          throw new IllegalArgumentException("Can't insert '" + base.getType() + "' type into '" + arrayType + "' type array.");
        }
        elements.add(base.value);
        if (hasElementSeparator()) {
          if (!reader.canRead()) throw new IllegalArgumentException("Expected value.");
          continue;
        }
      }
      expect(']');
      return elements.toArray(new Number[0]);
    }
  }

  private boolean hasElementSeparator() {
    reader.skipWhitespace();
    if (reader.canRead() && reader.peek() == ',') {
      reader.skip();
      reader.skipWhitespace();
      return true;
    } else {
      return false;
    }
  }

  private void expect(char c) throws IllegalArgumentException {
    reader.skipWhitespace();
    reader.expect(c);
  }

  private final static class StringReader {
    @NotNull final String str;
    final int strLen;
    int cursor = 0;

    StringReader(@NotNull String str) {
      this.str = str;
      this.strLen = str.length();
    }

    final static char SYNTAX_ESCAPE = '\\';
    final static char SYNTAX_DOUBLE_QUOTE = '"';
    final static char SYNTAX_SINGLE_QUOTE = '\'';

    static boolean isQuotedStringStart(char c) {
      return c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE;
    }

    static boolean isAllowedInUnquotedString(char c) {
      return (c >= '0' && c <= '9') ||
        (c >= 'A' && c <= 'Z') ||
        (c >= 'a' && c <= 'z') ||
        c == '_' || c == '-' || c == '.' || c =='+';
    }

    boolean canRead(int len) {
      return cursor + len <= strLen;
    }

    boolean canRead() {
      return canRead(1);
    }

    char peek() {
      return str.charAt(cursor);
    }

    char peek(int offset) {
      return str.charAt(cursor + offset);
    }

    char read() {
      return str.charAt(cursor++);
    }

    void skip() {
      cursor++;
    }

    void skipWhitespace() {
      while (canRead() && Character.isWhitespace(peek())) skip();
    }

    @NotNull
    String readUnquotedString() {
      int start = cursor;
      while (canRead() && isAllowedInUnquotedString(peek())) skip();
      return str.substring(start, cursor);
    }

    @NotNull
    String readQuotedString() throws IllegalArgumentException {
      if (!canRead()) return "";
      char next = peek();
      if (!isQuotedStringStart(next)) throw new IllegalArgumentException("Expected quote to start a string.");
      skip();
      return readStringUntil(next);
    }

    @NotNull
    String readStringUntil(char terminator) throws IllegalArgumentException {
      StringBuilder result = new StringBuilder();
      boolean escaped = false;
      while (canRead()) {
        char c = read();
        if (escaped) {
          if (c == terminator || c == SYNTAX_ESCAPE) {
            result.append(c);
            escaped = false;
          } else {
            cursor -= 1;
            throw new IllegalArgumentException("Invalid escape sequence '" + c + "' in quoted string.");
          }
        } else if (c == SYNTAX_ESCAPE) {
          escaped = true;
        } else if (c == terminator) {
          return result.toString();
        } else {
          result.append(c);
        }
      }
      throw new IllegalArgumentException("Unclosed quoted string.");
    }

    @NotNull
    String readString() throws IllegalArgumentException {
      if (!canRead()) return "";
      char next = peek();
      if (isQuotedStringStart(next)) {
        skip();
        return readStringUntil(next);
      }
      return readUnquotedString();
    }

    void expect(char c) throws IllegalArgumentException {
      if (canRead() && peek() == c) {
        skip();
      } else {
        throw new IllegalArgumentException("Expected: " + c);
      }
    }
  }
}
