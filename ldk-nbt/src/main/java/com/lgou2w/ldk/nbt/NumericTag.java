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

public abstract class NumericTag<T extends Number> extends BaseTag<T> {

  public NumericTag(T value) {
    super(value);
  }

  public double doubleValu() {
    return value.doubleValue();
  }

  public float floatValue() {
    return value.floatValue();
  }

  public long longValue() {
    return value.longValue();
  }

  public int intValue() {
    return value.intValue();
  }

  public short shortValue() {
    return value.shortValue();
  }

  public byte byteValue() {
    return value.byteValue();
  }

  protected abstract char getMojangsonSuffix();

  @Override
  protected void toMojangsonBuilder(@NotNull StringBuilder builder, boolean includeColor) {
    if (!includeColor) {
      builder.append(value);
      if (getMojangsonSuffix() > 0) builder.append(getMojangsonSuffix());
    } else {
      builder.append(COLOR_GOLD);
      builder.append(value);
      builder.append(COLOR_RED);
      if (getMojangsonSuffix() > 0) builder.append(getMojangsonSuffix());
      builder.append(COLOR_RESET);
    }
  }
}
