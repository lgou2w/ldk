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

public class KeybindComponent extends BaseComponent {
  private final String keybind;

  @Contract("null -> fail")
  public KeybindComponent(String keybind) {
    this.keybind = Objects.requireNonNull(keybind, "keybind");
  }

  @NotNull
  public String getKeybind() {
    return keybind;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    KeybindComponent that = (KeybindComponent) o;
    return keybind.equals(that.keybind);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), keybind);
  }

  @Override
  public String toString() {
    return "KeybindComponent{" +
      "keybind='" + keybind + '\'' +
      ", style=" + style +
      ", siblings=" + siblings +
      '}';
  }
}
