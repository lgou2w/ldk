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

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public abstract class ReflectionMatcher<T extends AccessibleObject & Member> {
  protected final boolean declared;
  @NotNull protected List<T> values;

  @Contract("null, _ -> fail")
  public ReflectionMatcher(Collection<T> values, boolean declared) {
    Objects.requireNonNull(values, "values");
    this.values = new ArrayList<>(values);
    this.declared = declared;
  }

  public ReflectionMatcher<T> with(@NotNull Predicate<T> condition) {
    if (values.isEmpty()) return this;
    List<T> newValues = new ArrayList<>();
    for (T element : values) {
      if (condition.test(element)) {
        newValues.add(element);
      }
    }
    values = newValues;
    return this;
  }

  public <U> ReflectionMatcher<T> with(
    @NotNull Supplier<U> initialize,
    @NotNull BiFunction<T, U, Boolean> condition
  ) {
    if (values.isEmpty()) return this;
    U initializeValue = initialize.get();
    List<T> newValues = new ArrayList<>();
    for (T element : values) {
      if (condition.apply(element, initializeValue)) {
        newValues.add(element);
      }
    }
    values = newValues;
    return this;
  }

  @Contract("_, null -> fail")
  private ReflectionMatcher<T> withModifiers(boolean reverse, int... modifiers) {
    Objects.requireNonNull(modifiers, "modifiers");
    if (modifiers.length <= 0) return this;
    return with(it -> {
      int mod = it.getModifiers();
      boolean result = true;
      for (int modifier : modifiers)
        if (!(result = (reverse == ((mod & modifier) == 0)))) break;
      return result;
    });
  }

  @Contract("null -> fail")
  public ReflectionMatcher<T> withModifiers(int... modifiers) {
    return withModifiers(false, modifiers);
  }

  @Contract("null -> fail")
  public ReflectionMatcher<T> withoutModifiers(int... modifiers) {
    return withModifiers(true, modifiers);
  }

  public ReflectionMatcher<T> withName(@NotNull String regex) {
    Pattern pattern = Pattern.compile(regex);
    return with(it -> pattern.matcher(it.getName()).matches());
  }

  public <A extends Annotation> ReflectionMatcher<T> withAnnotation(@NotNull Class<A> clazz) {
    return with(it -> it.getAnnotation(clazz) != null);
  }

  public <A extends Annotation> ReflectionMatcher<T> withAnnotationIf(
    @NotNull Class<A> clazz,
    @NotNull Predicate<@Nullable A> condition
  ) {
    return with(it -> condition.test(it.getAnnotation(clazz)));
  }

  public abstract ReflectionMatcher<T> withType(@NotNull Class<?> clazz);

  public abstract ReflectionMatcher<T> withArgs(@NotNull Class<?>... args);

  public abstract ReflectionMatcher<T> withArgsCount(int count);

  @NotNull
  public List<T> results() {
    List<T> results = new ArrayList<>(values.size());
    for (T element : values) {
      if (declared && !element.isAccessible()) element.setAccessible(true);
      results.add(element);
    }
    return results;
  }

  @NotNull
  public T result(@Nullable String message) throws NoSuchElementException {
    List<T> results = results();
    if (results.isEmpty()) throw new NoSuchElementException(message);
    return results.get(0);
  }

  @NotNull
  public T result() throws NoSuchElementException {
    return result(ERROR_EMPTY_RESULTS);
  }

  @Nullable
  public T resultOrNull() {
    List<T> results = results();
    return !results.isEmpty() ? results.get(0) : null;
  }

  @NotNull
  public abstract List<? extends Accessor<T>> resultAccessors();

  @NotNull
  public abstract Accessor<T> resultAccessor(@Nullable String message) throws NoSuchElementException;

  @NotNull
  public abstract Accessor<T> resultAccessor() throws NoSuchElementException;

  @Nullable
  public abstract Accessor<T> resultAccessorOrNull();

  final static String ERROR_EMPTY_RESULTS = "Empty results";
}
