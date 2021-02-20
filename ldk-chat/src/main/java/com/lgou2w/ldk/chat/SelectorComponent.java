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

package com.lgou2w.ldk.chat;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SelectorComponent extends BaseComponent {
  private final String selector;

  @Contract("null -> fail")
  public SelectorComponent(String selector) {
    if (selector == null) throw new NullPointerException("selector");
    this.selector = selector;
  }

  @NotNull
  public String getSelector() {
    return selector;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    SelectorComponent that = (SelectorComponent) o;
    return selector.equals(that.selector);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), selector);
  }

  @Override
  public String toString() {
    return "SelectorComponent{" +
      "selector='" + selector + '\'' +
      ", style=" + style +
      ", siblings=" + siblings +
      '}';
  }
}
