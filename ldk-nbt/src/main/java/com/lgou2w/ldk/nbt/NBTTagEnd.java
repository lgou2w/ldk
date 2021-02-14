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
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;

public final class NBTTagEnd extends NBTBase<Object> {

  private final static Object VOID = new Object();
  public final static NBTTagEnd INSTANCE = new NBTTagEnd();

  private NBTTagEnd() {
    super("", VOID);
  }

  @Override
  public @NotNull NBTType getType() {
    return NBTType.END;
  }

  @Override
  public void setValue(@Nullable Object value) { /* No change allowed */ }

  @Override
  public void read(@NotNull DataInput input) { }

  @Override
  public void write(@NotNull DataOutput output) { }

  @Override
  public String toString() {
    return "NBTTagEnd";
  }

  @Override
  @NotNull
  public String toMojangson(boolean color) {
    return "";
  }

  @Override
  protected void toMojangsonBuilder(@NotNull StringBuilder builder, boolean color) { }

  @Override
  @NotNull
  public NBTTagEnd clone() {
    return INSTANCE;
  }
}
