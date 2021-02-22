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
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public enum TagType {

  END(0, Void.TYPE, Void.class, EndTag.class),
  BYTE(1, Byte.TYPE, Byte.class, ByteTag.class),
  SHORT(2, Short.TYPE, Short.class, ShortTag.class),
  INT(3, Integer.TYPE, Integer.class, IntTag.class),
  LONG(4, Long.TYPE, Long.class, LongTag.class),
  FLOAT(5, Float.TYPE, Float.class, FloatTag.class),
  DOUBLE(6, Double.TYPE, Double.class, DoubleTag.class),
  BYTE_ARRAY(7, byte[].class, byte[].class, ByteArrayTag.class),
  STRING(8, String.class, String.class, StringTag.class),
  LIST(9, List.class, List.class, ListTag.class),
  COMPOUND(10, Map.class, Map.class, CompoundTag.class),
  INT_ARRAY(11, int[].class, int[].class, IntArrayTag.class),
  LONG_ARRAY(12, long[].class, long[].class, LongArrayTag.class)
  ;

  private final int id;
  private final Class<?> primitive;
  private final Class<?> reference;
  private final Class<? extends BaseTag<?>> wrapped;

  TagType(int id, Class<?> primitive, Class<?> reference, Class<? extends BaseTag<?>> wrapped) {
    this.id = id;
    this.primitive = primitive;
    this.reference = reference;
    this.wrapped = wrapped;
  }

  public int getId() {
    return id;
  }

  @NotNull
  public Class<?> getPrimitive() {
    return primitive;
  }

  @NotNull
  public Class<?> getReference() {
    return reference;
  }

  @NotNull
  public Class<? extends BaseTag<?>> getWrapped() {
    return wrapped;
  }

  public boolean isNumeric() {
    return this == BYTE ||
      this == SHORT ||
      this == INT ||
      this == LONG ||
      this == FLOAT ||
      this == DOUBLE;
  }

  public boolean isListOrCompound() {
    return this == LIST || this == COMPOUND;
  }

  private final static Map<Integer, TagType> ID_MAP;
  private final static Map<Class<?>, TagType> CLASS_MAP;

  static {
    Map<Integer, TagType> idMap = new HashMap<>();
    Map<Class<?>, TagType> classMap = new HashMap<>();
    for (TagType type : TagType.values()) {
      idMap.put(type.id, type);
      classMap.put(type.primitive, type);
      classMap.put(type.reference, type);
      classMap.put(type.wrapped, type);
    }
    ID_MAP = Collections.unmodifiableMap(idMap);
    CLASS_MAP = Collections.unmodifiableMap(classMap);
  }

  @Nullable
  public static TagType fromId(int id) {
    return ID_MAP.get(id);
  }

  @Nullable
  @Contract("null -> null")
  public static TagType fromClass(Class<?> clazz) {
    return CLASS_MAP.get(clazz);
  }

  @NotNull
  @Contract("null -> fail")
  public static BaseTag<?> create(TagType type) {
    Objects.requireNonNull(type, "type");
    switch (type) {
      case END: return EndTag.INSTANCE;
      case BYTE: return new ByteTag();
      case SHORT: return new ShortTag();
      case INT: return new IntTag();
      case LONG: return new LongTag();
      case FLOAT: return new FloatTag();
      case DOUBLE: return new DoubleTag();
      case BYTE_ARRAY: return new ByteArrayTag();
      case STRING: return new StringTag();
      case LIST: return new ListTag();
      case COMPOUND: return new CompoundTag();
      case INT_ARRAY: return new IntArrayTag();
      case LONG_ARRAY: return new LongArrayTag();
      default: throw new UnsupportedOperationException();
    }
  }
}
