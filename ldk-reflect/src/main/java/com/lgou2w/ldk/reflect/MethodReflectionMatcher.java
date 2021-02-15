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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MethodReflectionMatcher extends ReflectionMatcher<Method> {

  public MethodReflectionMatcher(@NotNull Collection<Method> values, boolean declared) {
    super(values, declared);
  }

  @Override
  public MethodReflectionMatcher with(@NotNull Predicate<Method> condition) {
    return (MethodReflectionMatcher) super.with(condition);
  }

  @Override
  public <U> MethodReflectionMatcher with(@NotNull Supplier<U> initialize, @NotNull BiFunction<Method, U, Boolean> condition) {
    return (MethodReflectionMatcher) super.with(initialize, condition);
  }

  @Override
  public MethodReflectionMatcher withModifiers(int... modifiers) {
    return (MethodReflectionMatcher) super.withModifiers(modifiers);
  }

  @Override
  public MethodReflectionMatcher withName(@NotNull String regex) {
    return (MethodReflectionMatcher) super.withName(regex);
  }

  @Override
  public <A extends Annotation> MethodReflectionMatcher withAnnotation(@NotNull Class<A> clazz) {
    return (MethodReflectionMatcher) super.withAnnotation(clazz);
  }

  @Override
  public <A extends Annotation> MethodReflectionMatcher withAnnotationIf(@NotNull Class<A> clazz, @NotNull Predicate<@Nullable A> condition) {
    return (MethodReflectionMatcher) super.withAnnotationIf(clazz, condition);
  }

  @Override
  public MethodReflectionMatcher withType(@NotNull Class<?> clazz) {
    Class<?> primitiveType = DataType.ofPrimitive(clazz);
    return with(it -> primitiveType.isAssignableFrom(it.getReturnType()));
  }

  @Override
  public MethodReflectionMatcher withArgs(@NotNull Class<?>... args) {
    Class<?>[] primitiveTypes = DataType.ofPrimitive(args);
    return with(it -> DataType.compare(it.getParameterTypes(), primitiveTypes));
  }

  @Override
  public MethodReflectionMatcher withArgsCount(int count) {
    return with(it -> it.getParameterTypes().length == count);
  }

  @Override
  @NotNull
  public List<MethodAccessor<Object, Object>> resultAccessors() {
    List<Method> pureResults = results();
    List<MethodAccessor<Object, Object>> results = new ArrayList<>(pureResults.size());
    for (Method element : pureResults) results.add(Accessors.of(element));
    return results;
  }

  @Override
  @NotNull
  public MethodAccessor<Object, Object> resultAccessor(@Nullable String message) throws NoSuchElementException {
    return resultAccessorAs(message);
  }

  @Override
  @NotNull
  public MethodAccessor<Object, Object> resultAccessor() throws NoSuchElementException {
    return resultAccessorAs(ERROR_EMPTY_RESULTS);
  }

  @Override
  @Nullable
  public MethodAccessor<Object, Object> resultAccessorOrNull() {
    Method result = resultOrNull();
    return result != null ? Accessors.of(result) : null;
  }

  @NotNull
  public <T, R> MethodAccessor<T, R> resultAccessorAs(@Nullable String message) throws NoSuchElementException {
    return Accessors.of(result(message));
  }

  @NotNull
  public <T, R> MethodAccessor<T, R> resultAccessorAs() throws NoSuchElementException {
    return resultAccessorAs(ERROR_EMPTY_RESULTS);
  }
}
