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

public enum NBTType {

  END(0, Void.TYPE, Void.class, NBTTagEnd.class),
  BYTE(1, Byte.TYPE, Byte.class, NBTTagByte.class),
  SHORT(2, Short.TYPE, Short.class, NBTTagShort.class),
  INT(3, Integer.TYPE, Integer.class, NBTTagInt.class),
  LONG(4, Long.TYPE, Long.class, NBTTagLong.class),
  FLOAT(5, Float.TYPE, Float.class, NBTTagFloat.class),
  DOUBLE(6, Double.TYPE, Double.class, NBTTagDouble.class),
  BYTE_ARRAY(7, byte[].class, byte[].class, NBTTagByteArray.class),
  STRING(8, String.class, String.class, NBTTagString.class),
  LIST(9, List.class, List.class, NBTTagList.class),
  COMPOUND(10, Map.class, Map.class, NBTTagCompound.class),
  INT_ARRAY(11, int[].class, int[].class, NBTTagIntArray.class),
  LONG_ARRAY(12, long[].class, long[].class, NBTTagLongArray.class)
  ;

  private final int id;
  private final Class<?> primitive;
  private final Class<?> reference;
  private final Class<? extends NBTBase<?>> wrapped;

  NBTType(int id, Class<?> primitive, Class<?> reference, Class<? extends NBTBase<?>> wrapped) {
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
  public Class<? extends NBTBase<?>> getWrapped() {
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

  private final static Map<Integer, NBTType> ID_MAP;
  private final static Map<Class<?>, NBTType> CLASS_MAP;

  static {
    Map<Integer, NBTType> idMap = new HashMap<>();
    Map<Class<?>, NBTType> classMap = new HashMap<>();
    for (NBTType type : NBTType.values()) {
      idMap.put(type.id, type);
      classMap.put(type.primitive, type);
      classMap.put(type.reference, type);
      classMap.put(type.wrapped, type);
    }
    ID_MAP = Collections.unmodifiableMap(idMap);
    CLASS_MAP = Collections.unmodifiableMap(classMap);
  }

  @Nullable
  public static NBTType fromId(int id) {
    return ID_MAP.get(id);
  }

  @Nullable
  @Contract("null -> null")
  public static NBTType fromClass(Class<?> clazz) {
    return CLASS_MAP.get(clazz);
  }

  @NotNull
  @Contract("null -> fail")
  public static NBTBase<?> create(NBTType type) {
    if (type == null) throw new NullPointerException("type");
    switch (type) {
      case END: return NBTTagEnd.INSTANCE;
      case BYTE: return new NBTTagByte();
      case SHORT: return new NBTTagShort();
      case INT: return new NBTTagInt();
      case LONG: return new NBTTagLong();
      case FLOAT: return new NBTTagFloat();
      case DOUBLE: return new NBTTagDouble();
      case BYTE_ARRAY: return new NBTTagByteArray();
      case STRING: return new NBTTagString();
      case LIST: return new NBTTagList();
      case COMPOUND: return new NBTTagCompound();
      case INT_ARRAY: return new NBTTagIntArray();
      case LONG_ARRAY: return new NBTTagLongArray();
      default: throw new UnsupportedOperationException();
    }
  }
}
