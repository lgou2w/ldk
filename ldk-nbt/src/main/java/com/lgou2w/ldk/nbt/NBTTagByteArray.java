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

public class NBTTagByteArray extends NBTBase<byte[]> {

  @Contract("null, _ -> fail; _, null -> fail")
  public NBTTagByteArray(String name, byte[] value) {
    super(name, value);
  }

  @Contract("null -> fail")
  public NBTTagByteArray(String name) {
    super(name, new byte[0]);
  }

  @Contract("null -> fail")
  public NBTTagByteArray(byte[] value) {
    super("", value);
  }

  @Override
  public @NotNull NBTType getType() {
    return NBTType.BYTE_ARRAY;
  }

  @Override
  @Contract("-> new")
  public byte @NotNull [] getValue() {
    byte[] value = super.getValue();
    byte[] newValue = new byte[value.length];
    System.arraycopy(value, 0, newValue, 0, newValue.length);
    return newValue;
  }

  @Override
  public void setValue(byte[] value) {
    byte[] newValue = new byte[value.length];
    System.arraycopy(value, 0, newValue, 0, newValue.length);
    super.setValue(newValue);
  }

  @Override
  public void read(@NotNull DataInput input) throws IOException {
    byte[] newValue = new byte[input.readInt()];
    input.readFully(newValue);
    value = newValue;
  }

  @Override
  public void write(@NotNull DataOutput output) throws IOException {
    output.writeInt(value.length);
    output.write(value);
  }

  @Override
  public String toString() {
    return "NBTTagByteArray{" +
      "value=" + Arrays.toString(value) +
      '}';
  }

  @Override
  protected void toMojangsonBuilder(@NotNull StringBuilder builder, boolean color) {
    if (!color) {
      builder.append('[');
      builder.append(PREFIX_BYTE_ARRAY + ";");
      int len = value.length, i = 0;
      for (byte element : value) {
        if (i >= 1 && i < len) builder.append(',');
        builder.append(element);
        builder.append(SUFFIX_BYTE);
        i++;
      }
    } else {
      builder.append("[" + COLOR_RED);
      builder.append(PREFIX_BYTE_ARRAY);
      builder.append(COLOR_RESET);
      builder.append(';');
      int len = value.length, i = 0;
      for (byte element : value) {
        if (i >= 1 && i < len) builder.append(", ");
        builder.append(COLOR_GOLD);
        builder.append(element);
        builder.append(COLOR_RED);
        builder.append(SUFFIX_BYTE);
        builder.append(COLOR_RESET);
        i++;
      }
    }
    builder.append(']');
  }

  @Override
  @NotNull
  public NBTTagByteArray clone() {
    byte[] newValue = new byte[value.length];
    System.arraycopy(value, 0, newValue, 0, newValue.length);
    return new NBTTagByteArray(name, newValue);
  }
}
