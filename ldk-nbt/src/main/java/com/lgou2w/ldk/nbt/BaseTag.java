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

package com.lgou2w.ldk.nbt;

import org.jetbrains.annotations.NotNull;

public abstract class BaseTag<T> implements NBT<T> {
  @NotNull protected T value;

  public BaseTag(T value) {
    if (value == null) throw new NullPointerException("value");
    this.value = value;
  }

  @NotNull
  @Override
  public T getValue() {
    return value;
  }

  @Override
  public void setValue(T value) {
    if (value == null) throw new NullPointerException("value");
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseTag<?> base = (BaseTag<?>) o;
    return value.equals(base.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public abstract String toString();

  @Override
  @NotNull
  public String toMojangson() {
    return toMojangson(false);
  }

  @Override
  @NotNull
  public String toMojangson(boolean color) {
    StringBuilder builder = new StringBuilder();
    toMojangsonBuilder(builder, color);
    return builder.toString();
  }

  protected abstract void toMojangsonBuilder(@NotNull StringBuilder builder, boolean color);

  @Override
  @NotNull
  public abstract BaseTag<T> clone();

  /// Mojangson numeric prefix, suffix and colors

  final static char SUFFIX_BYTE = 'b';
  final static char SUFFIX_SHORT = 's';
  final static char SUFFIX_LONG = 'L';
  final static char SUFFIX_FLOAT = 'f';
  final static char SUFFIX_DOUBLE = 'd';
  final static char PREFIX_BYTE_ARRAY = 'B';
  final static char PREFIX_INT_ARRAY = 'I';
  final static char PREFIX_LONG_ARRAY = 'L';
  final static char COLOR = '§';
  final static String COLOR_GOLD = COLOR + "6";
  final static String COLOR_GREEN = COLOR + "a";
  final static String COLOR_AQUA = COLOR + "b";
  final static String COLOR_RED = COLOR + "c";
  final static String COLOR_RESET = COLOR + "r";
}
