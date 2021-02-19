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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class ClickEvent {
  private final Action action;
  private final String value;

  @Contract("null, _ -> fail; _, null -> fail")
  public ClickEvent(Action action, String value) {
    if (action == null) throw new NullPointerException("action");
    if (value == null) throw new NullPointerException("value");
    this.action = action;
    this.value = value;
  }

  @NotNull
  public Action getAction() {
    return action;
  }

  @NotNull
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ClickEvent that = (ClickEvent) o;
    return action == that.action && value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, value);
  }

  @Override
  public String toString() {
    return "ClickEvent{" +
      "action=" + action +
      ", value='" + value + '\'' +
      '}';
  }

  public enum Action {
    OPEN_URL,
    OPEN_FILE,
    SUGGEST_COMMAND,
    RUN_COMMAND,
    CHANGE_PAGE,
    COPY_TO_CLIPBOARD
    ;

    final static Map<String, Action> NAME_MAP;

    static {
      Map<String, Action> nameMap = new HashMap<>();
      for (Action action : Action.values()) {
        nameMap.put(action.name().toLowerCase(Locale.ROOT), action);
      }
      NAME_MAP = Collections.unmodifiableMap(nameMap);
    }

    @Nullable
    @Contract("null -> null")
    public static Action fromName(String name) {
      if (name == null) return null;
      return NAME_MAP.get(name.toLowerCase(Locale.ROOT));
    }
  }
}
