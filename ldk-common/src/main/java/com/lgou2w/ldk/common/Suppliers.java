/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

/*
 * Copyright (C) 2021 The lgou2w <lgou2w@hotmail.com>
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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/*
 *  Modify: Guava Suppliers
 *  by lgou2w on 02/18/2021
 *  see: https://github.com/google/guava/blob/master/guava/src/com/google/common/base/Suppliers.java
 */

/**
 * @author Guava Authors
 * @author Laurence Gonsalves
 * @author Harry Heymann
 */
public final class Suppliers {

  private Suppliers() { }

  @NotNull
  @Contract("null -> fail")
  public static <@Nullable T> Supplier<T> memoize(Supplier<T> delegate) {
    if (delegate instanceof NonSerializableMemoizingSupplier ||
      delegate instanceof MemoizingSupplier) {
      return delegate;
    }
    return delegate instanceof Serializable
      ? new MemoizingSupplier<>(delegate)
      : new NonSerializableMemoizingSupplier<>(delegate);
  }

  @NotNull
  @Contract("null, _, _ -> fail; _, _, null -> fail")
  public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit) {
    return new ExpiringMemoizingSupplier<>(delegate, duration, unit);
  }

  @NotNull
  public static <@Nullable T> Supplier<T> ofInstance(@Nullable T instance) {
    return new SupplizerOfInstance<>(instance);
  }

  @NotNull
  @Contract("null -> fail")
  public static <@Nullable T> Supplier<T> synchronizedSupplier(Supplier<T> delegate) {
    return new ThreadSafeSupplier<>(delegate);
  }

  final static class MemoizingSupplier<@Nullable T> implements Supplier<T>, Serializable {
    final Supplier<T> delegate;
    transient volatile boolean initialized;
    @Nullable transient T value;

    private final static long serialVersionUID = 0L;

    @Contract("null -> fail")
    MemoizingSupplier(Supplier<T> delegate) {
      this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public T get() {
      if (!initialized) {
        synchronized (this) {
          if (!initialized) {
            T t = delegate.get();
            value = t;
            initialized = true;
            return t;
          }
        }
      }
      return value;
    }

    @Override
    public String toString() {
      return "Suppliers.memoize{" +
        (initialized ? "<supplier that returned " + value + ">" : delegate) +
        "}";
    }
  }

  final static class NonSerializableMemoizingSupplier<@Nullable T> implements Supplier<T> {
    volatile Supplier<T> delegate;
    volatile boolean initialized;
    @Nullable volatile T value;

    @Contract("null -> fail")
    NonSerializableMemoizingSupplier(Supplier<T> delegate) {
      this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public T get() {
      if (!initialized) {
        synchronized (this) {
          if (!initialized) {
            T t = delegate.get();
            value = t;
            initialized = true;
            // Release the delegate to GC.
            delegate = null;
            return t;
          }
        }
      }
      return value;
    }

    @Override
    public String toString() {
      return "Suppliers.memoize{" +
        (delegate == null ? "<supplier that returned " + value + ">" : delegate) +
        "}";
    }
  }

  final static class ExpiringMemoizingSupplier<@Nullable T> implements Supplier<T>, Serializable {
    final Supplier<T> delegate;
    final long durationNanos;
    transient volatile long expirationNaos;
    @Nullable transient volatile T value;

    private final static long serialVersionUID = 0L;

    @Contract("null, _, _ -> fail; _, _, null -> fail")
    ExpiringMemoizingSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
      this.delegate = Objects.requireNonNull(delegate, "delegate");
      this.durationNanos = Objects.requireNonNull(unit, "unit").toNanos(duration);
      if (durationNanos <= 0)
        throw new IllegalArgumentException("Duration (" + duration + " " + unit + ") must be > 0");
    }

    @Override
    public T get() {
      long nanos = expirationNaos;
      long now = System.nanoTime();
      if (nanos == 0 || now - nanos >= 0) {
        synchronized (this) {
          if (nanos == expirationNaos) {
            T t = delegate.get();
            value = t;
            nanos = now + durationNanos;
            expirationNaos = nanos == 0 ? 1 : nanos;
            return t;
          }
        }
      }
      return value;
    }

    @Override
    public String toString() {
      return "Suppliers.memoizeWithExpiration{" +
        delegate + ", " +
        durationNanos + ", NANOS}";
    }
  }

  final static class SupplizerOfInstance<@Nullable T> implements Supplier<T>, Serializable {
    @Nullable final T instance;

    private final static long serialVersionUID = 0L;

    SupplizerOfInstance(@Nullable T instance) {
      this.instance = instance;
    }

    @Override
    public T get() {
      return instance;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof SupplizerOfInstance) {
        SupplizerOfInstance<?> that = (SupplizerOfInstance<?>) o;
        return Objects.equals(instance, that.instance);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(instance);
    }

    @Override
    public String toString() {
      return "Suppliers.ofInstance{" + instance + "}";
    }
  }

  final static class ThreadSafeSupplier<@Nullable T> implements Supplier<T>, Serializable {
    final Supplier<T> delegate;

    private final static long serialVersionUID = 0L;

    @Contract("null -> fail")
    ThreadSafeSupplier(Supplier<T> delegate) {
      this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public T get() {
      synchronized (delegate) {
        return delegate.get();
      }
    }

    @Override
    public String toString() {
      return "Suppliers.synchronizedSupplier{" + delegate + "}";
    }
  }
}
