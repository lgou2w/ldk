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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AnvilWindowEvent {
  private final AnvilWindow anvil;
  private final Player viewer;

  @Contract("null, _ -> fail; _, null -> fail")
  AnvilWindowEvent(AnvilWindow anvil, Player viewer) {
    this.anvil = Objects.requireNonNull(anvil, "anvil");
    this.viewer = Objects.requireNonNull(viewer, "viewer");
  }

  @NotNull
  public AnvilWindow getAnvil() {
    return anvil;
  }

  @NotNull
  public Player getViewer() {
    return viewer;
  }
}
