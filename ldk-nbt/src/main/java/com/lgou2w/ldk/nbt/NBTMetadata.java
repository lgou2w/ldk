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

import java.util.Objects;

public final class NBTMetadata {
  @NotNull private final String name;
  @NotNull private final BaseTag<?> value;

  private NBTMetadata(@NotNull String name, @NotNull BaseTag<?> value) {
    this.name = name;
    this.value = value;
  }

  @NotNull
  public String getName() {
    return name;
  }

  @NotNull
  public BaseTag<?> getValue() {
    return value;
  }

  public boolean isBlankName() {
    return name.isEmpty();
  }

  public boolean isEndType() {
    return this == END ||
      getValue() == EndTag.INSTANCE ||
      getValue().getType() == TagType.END;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NBTMetadata that = (NBTMetadata) o;
    return name.equals(that.name) && value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value);
  }

  @Override
  public String toString() {
    return "NBTMetadata{" +
      "name='" + name + '\'' +
      ", value=" + value +
      '}';
  }

  public final static NBTMetadata END = new NBTMetadata("", EndTag.INSTANCE);

  @Contract("null, _ -> fail; _, null -> fail")
  public static NBTMetadata of(String name, BaseTag<?> value) {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(value, "value");
    return new NBTMetadata(name, value);
  }

  @Contract("null -> fail")
  public static NBTMetadata of(BaseTag<?> value) {
    return of("", value);
  }
}
