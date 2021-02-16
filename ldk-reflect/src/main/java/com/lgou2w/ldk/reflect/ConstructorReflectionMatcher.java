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
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ConstructorReflectionMatcher<T> extends ReflectionMatcher<Constructor<T>> {

  public ConstructorReflectionMatcher(@NotNull Collection<Constructor<T>> values, boolean declared) {
    super(values, declared);
  }

  @Override
  public ConstructorReflectionMatcher<T> with(@NotNull Predicate<Constructor<T>> condition) {
    return (ConstructorReflectionMatcher<T>) super.with(condition);
  }

  @Override
  public <U> ConstructorReflectionMatcher<T> with(@NotNull Supplier<U> initialize, @NotNull BiFunction<Constructor<T>, U, Boolean> condition) {
    return (ConstructorReflectionMatcher<T>) super.with(initialize, condition);
  }

  @Override
  public ConstructorReflectionMatcher<T> withModifiers(int... modifiers) {
    return (ConstructorReflectionMatcher<T>) super.withModifiers(modifiers);
  }

  @Override
  public ConstructorReflectionMatcher<T> withoutModifiers(int... modifiers) {
    return (ConstructorReflectionMatcher<T>) super.withoutModifiers(modifiers);
  }

  @Override
  public ConstructorReflectionMatcher<T> withName(@NotNull String regex) {
    return this; // Constructor not have name
  }

  @Override
  public <A extends Annotation> ConstructorReflectionMatcher<T> withAnnotation(@NotNull Class<A> clazz) {
    return (ConstructorReflectionMatcher<T>) super.withAnnotation(clazz);
  }

  @Override
  public <A extends Annotation> ConstructorReflectionMatcher<T> withAnnotationIf(@NotNull Class<A> clazz, @NotNull Predicate<@Nullable A> condition) {
    return (ConstructorReflectionMatcher<T>) super.withAnnotationIf(clazz, condition);
  }

  @Override
  public ConstructorReflectionMatcher<T> withType(@NotNull Class<?> clazz) {
    return this; // Constructor not have return type
  }

  @Override
  public ConstructorReflectionMatcher<T> withArgs(@NotNull Class<?>... args) {
    Class<?>[] primitiveTypes = DataType.ofPrimitive(args);
    return with(it -> DataType.compare(it.getParameterTypes(), primitiveTypes));
  }

  @Override
  public ConstructorReflectionMatcher<T> withArgsCount(int count) {
    return with(it -> it.getParameterTypes().length == count);
  }

  @Override
  @NotNull
  public List<ConstructorAccessor<T>> resultAccessors() {
    List<Constructor<T>> pureResults = results();
    List<ConstructorAccessor<T>> results = new ArrayList<>(pureResults.size());
    for (Constructor<T> element : pureResults) results.add(Accessors.of(element));
    return results;
  }

  @Override
  @NotNull
  public ConstructorAccessor<T> resultAccessor(@Nullable String message) throws NoSuchElementException {
    return resultAccessorAs(message);
  }

  @Override
  @NotNull
  public ConstructorAccessor<T> resultAccessor() throws NoSuchElementException {
    return resultAccessor(ERROR_EMPTY_RESULTS);
  }

  @Override
  @Nullable
  public ConstructorAccessor<T> resultAccessorOrNull() {
    Constructor<T> result = resultOrNull();
    return result != null ? Accessors.of(result) : null;
  }

  @NotNull
  public <R> ConstructorAccessor<R> resultAccessorAs(@Nullable String message) throws NoSuchElementException {
    Constructor<R> result = (Constructor<R>) result(message);
    return Accessors.of(result);
  }

  @NotNull
  public <R> ConstructorAccessor<R> resultAccessorAs() throws NoSuchElementException {
    return resultAccessorAs(ERROR_EMPTY_RESULTS);
  }
}
