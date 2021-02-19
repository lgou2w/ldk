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

public class LongTag extends NumericTag<Long> {

  public LongTag(long value) {
    super(value);
  }

  public LongTag() {
    this(0L);
  }

  @Override
  @NotNull
  public TagType getType() {
    return TagType.LONG;
  }

  @Override
  public void read(@NotNull DataInput input) throws IOException {
    value = input.readLong();
  }

  @Override
  public void write(@NotNull DataOutput output) throws IOException {
    output.writeLong(value);
  }

  @Override
  public String toString() {
    return "LongTag{" +
      "value=" + value +
      '}';
  }

  @Override
  @NotNull
  public LongTag clone() {
    return new LongTag(value);
  }

  @Override
  protected char getMojangsonSuffix() {
    return BaseTag.SUFFIX_LONG;
  }
}
