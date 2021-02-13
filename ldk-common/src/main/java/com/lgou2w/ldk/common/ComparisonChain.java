/*
 * Copyright (C) 2009 The Guava Authors
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

/*
 * Copyright (C) 2017-2021 The lgou2w <lgou2w@hotmail.com>
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

import java.util.Comparator;

/*
 *  Modify: Guava ComparisonChain
 *  by lgou2w on 08/24/2017
 */

public abstract class ComparisonChain {

  private ComparisonChain () { }

  @NotNull
  public static ComparisonChain start() {
    return ACTIVE;
  }

  private final static ComparisonChain ACTIVE = new ComparisonChain() {

    @Override
    @NotNull
    public <T extends Comparable<T>> ComparisonChain compare(T left, T right) {
      return classify(left.compareTo(right));
    }

    @Override
    @NotNull
    public <T> ComparisonChain compare(T left, T right, @NotNull Comparator<T> comparator) {
      return classify(comparator.compare(left, right));
    }

    @Override
    @NotNull
    public ComparisonChain compare(int left, int right) {
      return classify(Integer.compare(left, right));
    }

    @Override
    @NotNull
    public ComparisonChain compare(long left, long right) {
      return classify(Long.compare(left, right));
    }

    @Override
    @NotNull
    public ComparisonChain compare(float left, float right) {
      return classify(Float.compare(left, right));
    }

    @Override
    @NotNull
    public ComparisonChain compare(double left, double right) {
      return classify(Double.compare(left, right));
    }

    @Override
    @NotNull
    public ComparisonChain compareTrueFirst(boolean left, boolean right) {
      return classify(Boolean.compare(right, left)); // reversed
    }

    @Override
    @NotNull
    public ComparisonChain compareFalseFirst(boolean left, boolean right) {
      return classify(Boolean.compare(left, right));
    }

    ComparisonChain classify(int result) {
      return result < 0 ? LESS : result > 0 ? GREATER : ACTIVE;
    }

    @Override
    public int result() {
      return 0;
    }
  };

  private final static ComparisonChain LESS = new InactiveComparisonChain(-1);
  private final static ComparisonChain GREATER = new InactiveComparisonChain(1);

  private final static class InactiveComparisonChain extends ComparisonChain {
    final int result;

    InactiveComparisonChain (int result) {
      this.result = result;
    }

    @Override
    @NotNull
    public <T extends Comparable<T>> ComparisonChain compare(T left, T right) {
      return this;
    }

    @Override
    @NotNull
    public <T> ComparisonChain compare(T left, T right, Comparator<T> comparator) {
      return this;
    }

    @Override
    @NotNull
    public ComparisonChain compare(int left, int right) {
      return this;
    }

    @Override
    @NotNull
    public ComparisonChain compare(long left, long right) {
      return this;
    }

    @Override
    @NotNull
    public ComparisonChain compare(float left, float right) {
      return this;
    }

    @Override
    @NotNull
    public ComparisonChain compare(double left, double right) {
      return this;
    }

    @Override
    @NotNull
    public ComparisonChain compareTrueFirst(boolean left, boolean right) {
      return this;
    }

    @Override
    @NotNull
    public ComparisonChain compareFalseFirst(boolean left, boolean right) {
      return this;
    }

    @Override
    public int result() {
      return this.result;
    }
  }

  @NotNull
  public abstract <T extends Comparable<T>> ComparisonChain compare(T left, T right);

  @NotNull
  public abstract <T> ComparisonChain compare(T left, T right, Comparator<T> comparator);

  @NotNull
  public abstract ComparisonChain compare(int left, int right);

  @NotNull
  public abstract ComparisonChain compare(long left, long right);

  @NotNull
  public abstract ComparisonChain compare(float left, float right);

  @NotNull
  public abstract ComparisonChain compare(double left, double right);

  @NotNull
  public abstract ComparisonChain compareTrueFirst(boolean left, boolean right);

  @NotNull
  public abstract ComparisonChain compareFalseFirst(boolean left, boolean right);

  public abstract int result();
}
