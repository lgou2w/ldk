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

package com.lgou2w.ldk.bukkit.anvil;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class AnvilWindowInputEvent extends AnvilWindowEvent implements Cancellable {
  private String value;
  private boolean cancel = false;

  @Contract("null, _, _ -> fail; _, null, _ -> fail; _, _, null -> fail")
  AnvilWindowInputEvent(AnvilWindow anvil, Player viewer, String value) {
    super(anvil, viewer);
    this.value = Objects.requireNonNull(value, "value");
  }

  @NotNull
  public String getValue() {
    return value;
  }

  @Contract("null -> fail")
  public void setValue(String value) {
    this.value = Objects.requireNonNull(value, "value");
  }

  @Override
  public boolean isCancelled() {
    return cancel;
  }

  @Override
  public void setCancelled(boolean cancel) {
    this.cancel = cancel;
  }
}
