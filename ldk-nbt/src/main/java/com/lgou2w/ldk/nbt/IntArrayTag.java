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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class IntArrayTag extends BaseTag<int[]> {

  @Contract("null -> fail")
  public IntArrayTag(int[] value) {
    super(value);
  }

  public IntArrayTag() {
    this(new int[0]);
  }

  @Override
  public @NotNull TagType getType() {
    return TagType.INT_ARRAY;
  }

  @Override
  @Contract("-> new")
  public int @NotNull [] getValue() {
    int[] value = super.getValue();
    int[] newValue = new int[value.length];
    System.arraycopy(value, 0, newValue, 0, newValue.length);
    return newValue;
  }

  @Override
  public void setValue(int[] value) {
    int[] newValue = new int[value.length];
    System.arraycopy(value, 0, newValue, 0, newValue.length);
    super.setValue(newValue);
  }

  @Override
  public void read(@NotNull DataInput input) throws IOException {
    int[] newValue = new int[input.readInt()];
    for (int i = 0; i < newValue.length; i++) newValue[i] = input.readInt();
    value = newValue;
  }

  @Override
  public void write(@NotNull DataOutput output) throws IOException {
    output.writeInt(value.length);
    for (int element : value) output.writeInt(element);
  }

  @Override
  public String toString() {
    return "IntArrayTag{" +
      "value=" + Arrays.toString(value) +
      '}';
  }

  @Override
  protected void toMojangsonBuilder(@NotNull StringBuilder builder, boolean includeColor) {
    if (!includeColor) {
      builder.append('[');
      builder.append(PREFIX_INT_ARRAY + ";");
      int len = value.length, i = 0;
      for (int element : value) {
        if (i >= 1 && i < len) builder.append(',');
        builder.append(element);
        i++;
      }
    } else {
      builder.append("[" + COLOR_RED);
      builder.append(PREFIX_INT_ARRAY);
      builder.append(COLOR_RESET);
      builder.append(';');
      int len = value.length, i = 0;
      for (int element : value) {
        if (i >= 1 && i < len) builder.append(", ");
        builder.append(COLOR_GOLD);
        builder.append(element);
        builder.append(COLOR_RESET);
        i++;
      }
    }
    builder.append(']');
  }

  @Override
  @NotNull
  public IntArrayTag clone() {
    int[] newValue = new int[value.length];
    System.arraycopy(value, 0, newValue, 0, newValue.length);
    return new IntArrayTag(newValue);
  }
}
