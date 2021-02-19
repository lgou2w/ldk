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

import java.util.Objects;

public abstract class NBTComponent extends BaseComponent {
  private final String path;
  private final String value;
  private final boolean interpret;

  @Contract("null, _, _ -> fail; _, null, _ -> fail")
  protected NBTComponent(String path, String value, boolean interpret) {
    if (path == null) throw new NullPointerException("path");
    if (value == null) throw new NullPointerException("value");
    this.path = path;
    this.value = value;
    this.interpret = interpret;
  }

  @NotNull
  public String getPath() {
    return path;
  }

  @NotNull
  public String getValue() {
    return value;
  }

  public boolean isInterpret() {
    return interpret;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    NBTComponent that = (NBTComponent) o;
    return interpret == that.interpret && path.equals(that.path) && value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), path, value, interpret);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + '{' +
      "path='" + path + '\'' +
      ", value='" + value + '\'' +
      ", interpret=" + interpret +
      ", style=" + style +
      ", siblings=" + siblings +
      '}';
  }

  public final static class StorageNBTComponent extends NBTComponent {
    @Contract("null, _, _ -> fail; _, null, _ -> fail")
    public StorageNBTComponent(String path, String id, boolean interpret) {
      super(path, id, interpret);
    }
  }

  public final static class BlockNBTComponent extends NBTComponent {
    @Contract("null, _, _ -> fail; _, null, _ -> fail")
    public BlockNBTComponent(String path, String position, boolean interpret) {
      super(path, position, interpret);
    }
  }

  public final static class EntityNBTComponent extends NBTComponent {
    @Contract("null, _, _ -> fail; _, null, _ -> fail")
    public EntityNBTComponent(String path, String selector, boolean interpret) {
      super(path, selector, interpret);
    }
  }
}
