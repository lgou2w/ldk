/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright (C) 2018-2020 The lgou2w <lgou2w@hotmail.com>
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/*
 * Modify: Apache common-collections IteratorChain
 * by lgou2w on 09/19/2018
 */

public final class IteratorChain<T> implements Iterator<T> {
  private final List<Iterator<T>> iterators;
  private @Nullable Iterator<T> current;
  private int index = 0;

  @SafeVarargs
  private IteratorChain(Iterator<T>... iterators) {
    this.iterators = Arrays.asList(iterators.clone());
  }

  private IteratorChain(Collection<Iterator<T>> iterators) {
    this.iterators = new ArrayList<>(iterators);
  }

  @NotNull
  private Iterator<T> update() {
    if (current == null) {
      current = iterators.isEmpty() ? (Iterator<T>) EMPTY : iterators.get(0);
    } else {
      while (!current.hasNext() && index < iterators.size() - 1) {
        index += 1;
        current = iterators.get(index);
      }
    }
    return current;
  }

  @Override
  public boolean hasNext() {
    return update().hasNext();
  }

  @Override
  public T next() {
    return update().next();
  }

  private final static Iterator<Object> EMPTY = new Iterator<Object>() {
    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public Object next() {
      throw new NoSuchElementException("Empty iterator");
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("Empty iterator");
    }
  };

  @NotNull
  @Contract("null -> fail; !null -> new")
  public static <T> IteratorChain<T> concat(Iterator<T>... iterators) {
    if (iterators == null) throw new NullPointerException("iterators");
    return new IteratorChain<>(iterators);
  }

  @NotNull
  @Contract("null -> fail; !null -> new")
  public static <T> IteratorChain<T> concat(Collection<Iterator<T>> iterators) {
    if (iterators == null) throw new NullPointerException("iterators");
    return new IteratorChain<>(iterators);
  }
}
