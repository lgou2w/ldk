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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public interface AnvilWindow {

  @NotNull
  @Contract("null -> fail; !null -> new")
  @SuppressWarnings("deprecation")
  static AnvilWindow of(Plugin plugin) {
    Objects.requireNonNull(plugin, "plugin");
    return AnvilWindowBase.newInstance(plugin);
  }

  enum Slot {
    INPUT_LEFT,
    INPUT_RIGHT,
    OUTPUT
  }

  @NotNull
  Plugin getPlugin();

  boolean isAllowMove();

  @NotNull
  @Contract("_ -> this")
  AnvilWindow setAllowMove(boolean flag);

  boolean isOpened();

  @NotNull
  @Contract("null -> fail; _ -> this")
  AnvilWindow open(Player viewer) throws IllegalStateException;

  @Nullable
  @Contract("null -> fail")
  ItemStack getItem(Slot slot) throws IllegalStateException;

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  AnvilWindow setItem(Slot slot, @Nullable ItemStack stack) throws IllegalStateException;

  @NotNull
  @Contract("_ -> this")
  AnvilWindow onOpen(@Nullable Consumer<AnvilWindowOpenEvent> listener);

  @NotNull
  @Contract("_ -> this")
  AnvilWindow onClose(@Nullable Consumer<AnvilWindowCloseEvent> listener);

  @NotNull
  @Contract("_ -> this")
  AnvilWindow onClick(@Nullable Consumer<AnvilWindowClickEvent> listener);

  @NotNull
  @Contract("_ -> this")
  AnvilWindow onInput(@Nullable Consumer<AnvilWindowInputEvent> listener);
}
