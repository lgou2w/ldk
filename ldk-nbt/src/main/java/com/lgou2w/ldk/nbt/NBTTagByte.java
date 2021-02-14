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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTTagNumeric<Byte> {

  public NBTTagByte(byte value) {
    super(value);
  }

  public NBTTagByte() {
    this((byte) 0);
  }

  @Override
  @NotNull
  public NBTType getType() {
    return NBTType.BYTE;
  }

  @Override
  public void read(@NotNull DataInput input) throws IOException {
    value = input.readByte();
  }

  @Override
  public void write(@NotNull DataOutput output) throws IOException {
    output.writeByte(value);
  }

  @Override
  public String toString() {
    return "NBTTagByte{" +
      "value=" + value +
      '}';
  }

  @Override
  @NotNull
  public NBTTagByte clone() {
    return new NBTTagByte(value);
  }

  @Override
  protected char getMojangsonSuffix() {
    return NBTBase.SUFFIX_BYTE;
  }
}
