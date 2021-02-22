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

package com.lgou2w.ldk.chat;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class TranslationComponent extends BaseComponent {
  private final String key;
  private final Object[] args;

  @Contract("null, _ -> fail; _, null -> fail")
  public TranslationComponent(String key, Object[] args) {
    this.key = Objects.requireNonNull(key, "key");
    this.args = Objects.requireNonNull(args, "args");
  }

  @Contract("null -> fail")
  public TranslationComponent(String key) {
    this(key, new Object[0]);
  }

  @NotNull
  public String getKey() {
    return key;
  }

  @NotNull
  public Object[] getArgs() {
    return args;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    TranslationComponent that = (TranslationComponent) o;
    return key.equals(that.key) && Arrays.equals(args, that.args);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(super.hashCode(), key);
    result = 31 * result + Arrays.hashCode(args);
    return result;
  }

  @Override
  public String toString() {
    return "TranslationComponent{" +
      "key='" + key + '\'' +
      ", args=" + Arrays.toString(args) +
      ", style=" + style +
      ", siblings=" + siblings +
      '}';
  }
}
