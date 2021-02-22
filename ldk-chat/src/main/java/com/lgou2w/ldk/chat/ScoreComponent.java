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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ScoreComponent extends BaseComponent {
  private final String name, objective;
  @Nullable private final String value;

  @Contract("null, _, _ -> fail; _, null, _ -> fail")
  public ScoreComponent(String name, String objective, @Nullable String value) {
    this.name = Objects.requireNonNull(name, "name");
    this.objective = Objects.requireNonNull(objective, "objective");
    this.value = value;
  }

  @NotNull
  public String getName() {
    return name;
  }

  @NotNull
  public String getObjective() {
    return objective;
  }

  @Nullable
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ScoreComponent that = (ScoreComponent) o;
    return name.equals(that.name) && objective.equals(that.objective) && Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name, objective, value);
  }

  @Override
  public String toString() {
    return "ScoreComponent{" +
      "name='" + name + '\'' +
      ", objective='" + objective + '\'' +
      ", value='" + value + '\'' +
      ", style=" + style +
      ", siblings=" + siblings +
      '}';
  }
}
