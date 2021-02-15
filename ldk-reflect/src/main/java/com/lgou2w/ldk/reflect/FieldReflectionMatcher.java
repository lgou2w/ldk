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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FieldReflectionMatcher extends ReflectionMatcher<Field> {

  public FieldReflectionMatcher(@NotNull Collection<Field> values, boolean declared) {
    super(values, declared);
  }

  @Override
  public FieldReflectionMatcher with(@NotNull Predicate<Field> condition) {
    return (FieldReflectionMatcher) super.with(condition);
  }

  @Override
  public <U> FieldReflectionMatcher with(@NotNull Supplier<U> initialize, @NotNull BiFunction<Field, U, Boolean> condition) {
    return (FieldReflectionMatcher) super.with(initialize, condition);
  }

  @Override
  public FieldReflectionMatcher withModifiers(int... modifiers) {
    return (FieldReflectionMatcher) super.withModifiers(modifiers);
  }

  @Override
  public FieldReflectionMatcher withName(@NotNull String regex) {
    return (FieldReflectionMatcher) super.withName(regex);
  }

  @Override
  public <A extends Annotation> FieldReflectionMatcher withAnnotation(@NotNull Class<A> clazz) {
    return (FieldReflectionMatcher) super.withAnnotation(clazz);
  }

  @Override
  public <A extends Annotation> FieldReflectionMatcher withAnnotationIf(@NotNull Class<A> clazz, @NotNull Predicate<@Nullable A> condition) {
    return (FieldReflectionMatcher) super.withAnnotationIf(clazz, condition);
  }

  @Override
  public FieldReflectionMatcher withType(@NotNull Class<?> clazz) {
    Class<?> primitiveType = DataType.ofPrimitive(clazz);
    return with(it -> primitiveType.isAssignableFrom(it.getType()));
  }

  @NotNull
  public FieldReflectionMatcher withParameterizedType(
    @Nullable Class<?> rawType,
    @NotNull Class<?>... actualTypeArgs
  ) {
    Class<?> primitiveRawType = DataType.ofPrimitive(rawType);
    Class<?>[] primitiveActualTypeArgs = DataType.ofPrimitive(actualTypeArgs);
    int subActualTypeArgsSize = primitiveActualTypeArgs.length;
    return with(it -> {
      ParameterizedType parameterizedType = it.getGenericType() instanceof ParameterizedType
        ? (ParameterizedType) it.getGenericType()
        : null;
      Type parameterizedRawType = parameterizedType != null ? parameterizedType.getRawType() : null;
      if (!(parameterizedRawType instanceof Class<?>)) return false;

      List<Class<?>> parameterizedActualTypeArgs = new ArrayList<>();
      for (Type type : parameterizedType.getActualTypeArguments()) {
        if (type instanceof Class) {
          parameterizedActualTypeArgs.add((Class<?>) type);
        }
      }
      if (parameterizedActualTypeArgs.size() > subActualTypeArgsSize) {
        parameterizedActualTypeArgs = parameterizedActualTypeArgs.subList(0, subActualTypeArgsSize);
      }
      return (primitiveRawType == null || primitiveRawType.isAssignableFrom((Class<?>) parameterizedRawType)) &&
        DataType.compare(primitiveActualTypeArgs, parameterizedActualTypeArgs.toArray(new Class[0]));
    });
  }

  @Override
  public FieldReflectionMatcher withArgs(@NotNull Class<?>... args) {
    return this; // Field not have args
  }

  @Override
  public FieldReflectionMatcher withArgsCount(int count) {
    return this; // Field not have args
  }

  @Override
  @NotNull
  public List<FieldAccessor<Object, Object>> resultAccessors() {
    List<Field> pureResults = results();
    List<FieldAccessor<Object, Object>> results = new ArrayList<>(pureResults.size());
    for (Field element : pureResults) results.add(Accessors.of(element));
    return results;
  }

  @Override
  @NotNull
  public FieldAccessor<Object, Object> resultAccessor(@Nullable String message) throws NoSuchElementException {
    return resultAccessorAs(message);
  }

  @Override
  @NotNull
  public FieldAccessor<Object, Object> resultAccessor() throws NoSuchElementException {
    return resultAccessorAs(ERROR_EMPTY_RESULTS);
  }

  @Override
  @Nullable
  public FieldAccessor<Object, Object> resultAccessorOrNull() {
    Field result = resultOrNull();
    return result != null ? Accessors.of(result) : null;
  }

  @NotNull
  public <T, R> FieldAccessor<T, R> resultAccessorAs(@Nullable String message) throws NoSuchElementException {
    return Accessors.of(result(message));
  }

  @NotNull
  public <T, R> FieldAccessor<T, R> resultAccessorAs() throws NoSuchElementException {
    return resultAccessorAs(ERROR_EMPTY_RESULTS);
  }
}
