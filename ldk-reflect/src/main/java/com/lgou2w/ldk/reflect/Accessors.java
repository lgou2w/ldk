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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public final class Accessors {

  private Accessors() { }

  @NotNull
  @Contract("null -> fail; !null -> new")
  public static <T> ConstructorAccessor<T> of(Constructor<T> constructor) {
    return new ConstructorAccessorImpl<>(constructor);
  }

  @NotNull
  @Contract("null -> fail; !null -> new")
  public static <T, R> MethodAccessor<T, R> of(Method method) {
    return new MethodAccessorImpl<>(method);
  }

  @NotNull
  @Contract("null -> fail; !null -> new")
  public static <T, R> FieldAccessor<T, R> of(Field field) {
    return new FieldAccessorImpl<>(field);
  }

  private final static class ConstructorAccessorImpl<T> implements ConstructorAccessor<T> {
    final Constructor<T> source;

    @Contract("null -> fail")
    ConstructorAccessorImpl(Constructor<T> source) {
      this.source = Objects.requireNonNull(source, "source");
    }

    @Override
    @NotNull
    public Constructor<T> getSource() {
      return source;
    }

    @Override
    public @NotNull T newInstance(@Nullable Object... initArgs) throws RuntimeException {
      try {
        return source.newInstance(initArgs);
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Unable to use constructor.", e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException("An internal error has occurred.", e.getCause());
      } catch (InstantiationException e) {
        throw new RuntimeException("Unable to instantiate object.", e);
      }
    }

    @Override
    public String toString() {
      return "ConstructorAccessorImpl{" +
        "source=" + source +
        '}';
    }
  }

  private final static class MethodAccessorImpl<T, R> implements MethodAccessor<T, R> {
    final Method source;

    @Contract("null -> fail")
    MethodAccessorImpl(Method source) {
      this.source = Objects.requireNonNull(source, "source");
    }

    @Override
    @NotNull
    public Method getSource() {
      return source;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable R invoke(@Nullable T instance, @Nullable Object... args) {
      try {
        return (R) source.invoke(instance, args);
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Unable to use method.", e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException("An internal error has occurred.", e.getCause());
      }
    }

    @Override
    public String toString() {
      return "MethodAccessorImpl{" +
        "source=" + source +
        '}';
    }
  }

  private final static class FieldAccessorImpl<T, R> implements FieldAccessor<T, R> {
    final Field source;

    @Contract("null -> fail")
    FieldAccessorImpl(Field source) {
      this.source = Objects.requireNonNull(source, "source");
    }

    @Override
    @NotNull
    public Field getSource() {
      return source;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable R get(@Nullable T instance) throws RuntimeException {
      try {
        return (R) source.get(instance);
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Unable to read field: " + source, e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Unable to use field.", e);
      }
    }

    @Override
    public void set(@Nullable T instance, @Nullable R value) throws RuntimeException {
      try {
        source.set(instance, value);
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Unable to set field: The value of " + source + " is: " + value, e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Unable to use field.", e);
      }
    }

    @Override
    public String toString() {
      return "FieldAccessorImpl{" +
        "source=" + source +
        '}';
    }
  }
}
