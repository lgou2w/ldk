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

public class StringTag extends BaseTag<String> {

  @Contract("null -> fail")
  public StringTag(String value) {
    super(value);
  }

  public StringTag() {
    this("");
  }

  @Override
  public @NotNull TagType getType() {
    return TagType.STRING;
  }

  @Override
  public void read(@NotNull DataInput input) throws IOException {
    value = input.readUTF();
  }

  @Override
  public void write(@NotNull DataOutput output) throws IOException {
    output.writeUTF(value);
  }

  @Override
  public String toString() {
    return "StringTag{" +
      "value=" + value.replace("\"", "\\\"") +
      '}';
  }

  @Override
  protected void toMojangsonBuilder(@NotNull StringBuilder builder, boolean color) {
    if (!color) {
      builder.append("\"");
      builder.append(value.replace("\"", "\\\""));
      builder.append("\"");
    } else {
      builder.append("\"" + COLOR_GREEN);
      builder.append(value.replace("\"", "\\\""));
      builder.append(COLOR_RESET + "\"");
    }
  }

  @Override
  @NotNull
  public StringTag clone() {
    return new StringTag(value);
  }
}
