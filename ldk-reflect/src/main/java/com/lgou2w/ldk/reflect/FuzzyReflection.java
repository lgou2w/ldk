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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public final class FuzzyReflection {
  private final Class<?> source;
  private final boolean declared;

  private FuzzyReflection(Class<?> source, boolean declared) {
    this.source = source;
    this.declared = declared;
  }

  @NotNull
  public Class<?> getSource() {
    return source;
  }

  public boolean isDeclared() {
    return declared;
  }

  @Contract("-> new")
  public FuzzyReflection useDeclared() {
    return new FuzzyReflection(source, true);
  }

  @Contract("-> new")
  public ConstructorReflectionMatcher<Object> useConstructorMatcher() {
    Set<Constructor<?>> constructorSet = getConstructors();
    List<Constructor<Object>> constructors = new ArrayList<>();
    for (Constructor<?> constructor : constructorSet) {
      constructors.add((Constructor<Object>) constructor);
    }
    return new ConstructorReflectionMatcher<>(constructors, declared);
  }

  @Contract("-> new")
  public MethodReflectionMatcher useMethodMatcher() {
    return new MethodReflectionMatcher(getMethods(), declared);
  }

  @Contract("-> new")
  public FieldReflectionMatcher useFieldMatcher() {
    return new FieldReflectionMatcher(getFields(), declared);
  }

  @NotNull
  public Set<Constructor<?>> getConstructors() {
    return declared
      ? union(source.getDeclaredConstructors(), source.getConstructors())
      : union(source.getConstructors());
  }

  @NotNull
  public Set<Method> getMethods() {
    return declared
      ? union(source.getDeclaredMethods(), source.getMethods())
      : union(source.getMethods());
  }

  @NotNull
  public Set<Field> getFields() {
    return declared
      ? union(source.getDeclaredFields(), source.getFields())
      : union(source.getFields());
  }

  @SafeVarargs
  private static <T> Set<T> union(T[]... array) {
    Set<T> result = new LinkedHashSet<>();
    for (T[] elements : array) result.addAll(Arrays.asList(elements));
    return result;
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> new")
  public static FuzzyReflection of(Class<?> source, boolean declared) {
    if (source == null) throw new NullPointerException("source");
    return new FuzzyReflection(source, declared);
  }

  @NotNull
  @Contract("null -> fail; !null -> new")
  public static FuzzyReflection of(Class<?> source) {
    return of(source, false);
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> new")
  public static FuzzyReflection of(Object source, boolean declared) {
    if (source == null) throw new NullPointerException("source");
    return new FuzzyReflection(source.getClass(), declared);
  }

  @NotNull
  @Contract("null -> fail; !null -> new")
  public static FuzzyReflection of(Object source) {
    return of(source, false);
  }

  @NotNull
  @Contract("null, _, _, -> fail; _, _, null -> fail")
  public static <T extends Accessor<?>> Supplier<T> lazySupplier(
    Class<?> source,
    boolean declared,
    Function<FuzzyReflection, T> delegate
  ) {
    return new LazyNonSerializableMemoizingSupplier<>(source, declared, delegate);
  }

  @NotNull
  @Contract("null, _, -> fail; _, null -> fail")
  public static <T extends Accessor<?>> Supplier<T> lazySupplier(
    Class<?> source,
    Function<FuzzyReflection, T> delegate
  ) {
    return lazySupplier(source, false, delegate);
  }

  @NotNull
  @Contract("null, _, _, -> fail; _, _, null -> fail")
  public static <T extends Accessor<?>> Supplier<T> lazySupplier(
    Object source,
    boolean declared,
    Function<FuzzyReflection, @Nullable T> delegate
  ) {
    if (source == null) throw new NullPointerException("source");
    return lazySupplier(source.getClass(), declared, delegate);
  }

  @NotNull
  @Contract("null, _, -> fail; _, null -> fail")
  public static <T extends Accessor<?>> Supplier<T> lazySupplier(
    Object source,
    Function<FuzzyReflection, @Nullable T> delegate
  ) {
    return lazySupplier(source, false, delegate);
  }

  final static class LazyNonSerializableMemoizingSupplier<@Nullable T extends Accessor<?>> implements Supplier<T> {
    volatile Function<FuzzyReflection, T> delegate;
    volatile FuzzyReflection fuzzy;
    volatile boolean initialized;
    @Nullable volatile T value;

    @Contract("null, _, _, -> fail; _, _, null -> fail")
    LazyNonSerializableMemoizingSupplier(
      Class<?> source,
      boolean declared,
      Function<FuzzyReflection, T> delegate
    ) {
      if (source == null) throw new NullPointerException("source");
      if (delegate == null) throw new NullPointerException("delegate");
      this.fuzzy = new FuzzyReflection(source, declared);
      this.delegate = delegate;
    }

    @Override
    public T get() {
      if (!initialized) {
        synchronized (this) {
          if (!initialized) {
            T t = delegate.apply(fuzzy);
            value = t;
            initialized = true;
            // Release to gc.
            delegate = null;
            fuzzy = null;
            return t;
          }
        }
      }
      return value;
    }

    @Override
    public String toString() {
      return "FuzzyReflection.lazySupplier{" +
        (delegate == null ? "<supplier that returned " + value + ">" : delegate) +
        "}";
    }
  }
}
