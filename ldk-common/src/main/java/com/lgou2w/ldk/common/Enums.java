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

package com.lgou2w.ldk.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

public final class Enums {

  private Enums() { }

  @Nullable
  public static <T extends Enum<T>> T of(
    @NotNull Class<T> clazz,
    @NotNull Predicate<T> condition,
    @Nullable T defaultValue
  ) {
    for (T constant : clazz.getEnumConstants()) {
      if (condition.test(constant)) {
        return constant;
      }
    }
    return defaultValue;
  }

  @Nullable
  public static <T extends Enum<T>> T of(@NotNull Class<T> clazz, @NotNull Predicate<T> condition) {
    return of(clazz, condition, null);
  }

  @Nullable
  public static <T extends Enum<T>> T ofName(
    @NotNull Class<T> clazz,
    @NotNull String name,
    @Nullable T defaultValue
  ) {
    return of(clazz, it -> it.name().equals(name), defaultValue);
  }

  @Nullable
  public static <T extends Enum<T>> T ofName(
    @NotNull Class<T> clazz,
    @NotNull String name
  ) {
    return ofName(clazz, name, null);
  }

  @Nullable
  public static <T extends Enum<T>> T ofOrdinal(
    @NotNull Class<T> clazz,
    int ordinal,
    @Nullable T defaultValue
  ) {
    return of(clazz, it -> it.ordinal() == ordinal, defaultValue);
  }

  @Nullable
  public static <T extends Enum<T>> T ofOrdinal(
    @NotNull Class<T> clazz,
    int ordinal
  ) {
    return ofOrdinal(clazz, ordinal, null);
  }

  @Nullable
  public static <V, T extends Enum<T> & Valuable<V>> T ofValuable(
    @NotNull Class<T> clazz,
    @Nullable V value,
    @Nullable T defaultValue
  ) {
    return of(clazz, it -> Objects.equals(it.getValue(), value), defaultValue);
  }

  @Nullable
  public static <V, T extends Enum<T> & Valuable<V>> T ofValuable(
    @NotNull Class<T> clazz,
    @Nullable V value
  ) {
    return ofValuable(clazz, value, null);
  }

  @Nullable
  public static <T> Enum<?> from(
    @NotNull Class<T> clazz,
    @NotNull Predicate<Enum<?>> condition,
    @Nullable Enum<?> defaultValue
  ) throws IllegalArgumentException {
    if (!clazz.isEnum()) throw new IllegalArgumentException("The parameter class " + clazz + " is not an enum.");
    for (T constant : clazz.getEnumConstants()) {
      if (condition.test((Enum<?>) constant)) {
        return (Enum<?>) constant;
      }
    }
    return defaultValue;
  }

  @Nullable
  public static <T> Enum<?> from(
    @NotNull Class<T> clazz,
    @NotNull Predicate<Enum<?>> condition
  ) throws IllegalArgumentException {
    return from(clazz, condition, null);
  }

  @Nullable
  public static <T> Enum<?> fromName(
    @NotNull Class<T> clazz,
    @NotNull String name,
    @Nullable Enum<?> defaultValue
  ) throws IllegalArgumentException {
    return from(clazz, it -> it.name().equals(name), defaultValue);
  }

  @Nullable
  public static <T> Enum<?> fromName(
    @NotNull Class<T> clazz,
    @NotNull String name
  ) throws IllegalArgumentException {
    return fromName(clazz, name, null);
  }

  @Nullable
  public static <T> Enum<?> fromOrdinal(
    @NotNull Class<T> clazz,
    int ordinal,
    @Nullable Enum<?> defaultValue
  ) throws IllegalArgumentException {
    return from(clazz, it -> it.ordinal() == ordinal, defaultValue);
  }

  @Nullable
  public static <T> Enum<?> fromOrdinal(
    @NotNull Class<T> clazz,
    int ordinal
  ) throws IllegalArgumentException {
    return fromOrdinal(clazz, ordinal, null);
  }

  @Nullable
  public static <V, T extends Valuable<V>> Enum<?> fromValuable(
    @NotNull Class<T> clazz,
    @Nullable V value,
    @Nullable Enum<?> defaultValue
  ) throws IllegalArgumentException {
    if (!clazz.isEnum()) throw new IllegalArgumentException("The parameter class " + clazz + " is not an enum.");
    for (T constant : clazz.getEnumConstants()) {
      if (Objects.equals(constant.getValue(), value)) {
        return (Enum<?>) constant;
      }
    }
    return defaultValue;
  }

  @Nullable
  public static <V, T extends Valuable<V>> Enum<?> fromValuable(
    @NotNull Class<T> clazz,
    @Nullable V value
  ) throws IllegalArgumentException {
    return fromValuable(clazz, value, null);
  }
}
