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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
}
