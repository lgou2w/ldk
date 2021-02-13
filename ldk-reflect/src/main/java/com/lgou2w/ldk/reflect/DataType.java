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

package com.lgou2w.ldk.reflect;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum DataType {

  VOID(Void.TYPE, Void.class),
  BYTE(Byte.TYPE, Byte.class),
  SHORT(Short.TYPE, Short.class),
  INTEGER(Integer.TYPE, Integer.class),
  LONG(Long.TYPE, Long.class),
  CHAR(Character.TYPE, Character.class),
  FLOAT(Float.TYPE, Float.class),
  DOUBLE(Double.TYPE, Double.class),
  BOOLEAN(Boolean.TYPE, Boolean.class)
  ;

  private final Class<?> primitive;
  private final Class<?> reference;

  DataType(Class<?> primitive, Class<?> reference) {
    this.primitive = primitive;
    this.reference = reference;
  }

  @NotNull
  public Class<?> getPrimitive() {
    return primitive;
  }

  @NotNull
  public Class<?> getReference() {
    return reference;
  }

  private final static Map<Class<?>, DataType> CLASS_MAP;

  static {
    Map<Class<?>, DataType> map = new HashMap<>();
    for (DataType type : DataType.values()) {
      map.put(type.primitive, type);
      map.put(type.reference, type);
    }
    CLASS_MAP = Collections.unmodifiableMap(map);
  }

  @Nullable
  @Contract("null -> null")
  public static DataType fromClass(@Nullable Class<?> clazz) {
    return CLASS_MAP.get(clazz);
  }

  @Nullable
  @Contract("null -> null; !null -> !null")
  public static Class<?> ofPrimitive(@Nullable Class<?> clazz) {
    DataType type = fromClass(clazz);
    return type != null ? type.primitive : clazz;
  }

  @Nullable
  @Contract("null -> null; !null -> !null")
  public static Class<?> ofReference(@Nullable Class<?> clazz) {
    DataType type = fromClass(clazz);
    return type != null ? type.reference : clazz;
  }

  @NotNull
  @Contract("null -> fail; !null -> !null")
  public static Class<?>[] ofPrimitive(Class<?>[] classes) {
    if (classes == null) throw new NullPointerException("classes");
    Class<?>[] result = new Class[classes.length];
    for (int i = 0; i < result.length; i++) result[i] = ofPrimitive(classes[i]);
    return result;
  }

  @NotNull
  @Contract("null -> fail; !null -> !null")
  public static Class<?>[] ofReference(Class<?>[] classes) {
    if (classes == null) throw new NullPointerException("classes");
    Class<?>[] result = new Class[classes.length];
    for (int i = 0; i < result.length; i++) result[i] = ofReference(classes[i]);
    return result;
  }

  @Contract("null, null -> true")
  public static boolean compare(@Nullable Class<?>[] left, @Nullable Class<?>[] right) {
    if (left != right && left != null && right != null && left.length == right.length) {
      for (int i = 0; i < left.length; i++) {
        if ((left[i] != null && right[i] != null ? !left[i].isAssignableFrom(right[i]) : left[i] != right[i])) {
          return false;
        }
      }
      return true;
    }
    return left == null && right == null;
  }
}
