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

package com.lgou2w.ldk.bukkit.item;

import com.lgou2w.ldk.chat.ChatComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public interface ItemBuilder {

  @NotNull
  @Contract("null, _, _ -> fail; !null, _, _ -> new")
  static ItemBuilder of(Material type, int count, int durability) {
    Objects.requireNonNull(type, "type");
    return new BaseItemBuilder.Default(type, count, durability);
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> new")
  static ItemBuilder of(Material type, int count) {
    return of(type, count, 0);
  }

  @NotNull
  @Contract("null -> fail; !null -> new")
  static ItemBuilder of(Material type) {
    return of(type, 1, 0);
  }

  @NotNull
  @Contract("-> new")
  ItemStack create();

  @Contract("_ -> this")
  ItemBuilder count(int count);

  @Contract("_ -> this")
  ItemBuilder durability(int durability);

  @Contract("_ -> this")
  ItemBuilder displayName(@Nullable ChatComponent displayName);

  @Contract("_ -> this")
  ItemBuilder displayName(@Nullable String displayName);

  @Contract("_ -> this")
  ItemBuilder localizedName(@Nullable ChatComponent localizedName);

  @Contract("_ -> this")
  ItemBuilder localizedName(@Nullable String localizedName);

  @Contract("_ -> this")
  ItemBuilder lore(@Nullable ChatComponent... lore);

  @Contract("_ -> this")
  ItemBuilder lore(@Nullable String... lore);

  @Contract("_ -> this")
  ItemBuilder lorePrepend(@Nullable ChatComponent... lore);

  @Contract("_ -> this")
  ItemBuilder lorePrepend(@Nullable String... lore);

  @Contract("_ -> this")
  ItemBuilder loreAppend(@Nullable ChatComponent... lore);

  @Contract("_ -> this")
  ItemBuilder loreAppend(@Nullable String... lore);

  @Contract("_, _ -> this")
  ItemBuilder loreInsert(int index, @Nullable ChatComponent... lore);

  @Contract("_, _ -> this")
  ItemBuilder loreInsert(int index, @Nullable String... lore);

  @Contract("_ -> this")
  ItemBuilder customModelData(int customModelData);

  @Contract("_ -> this")
  ItemBuilder unbreakable(boolean unbreakable);

  @Contract("_ -> this")
  ItemBuilder enchantment(@Nullable Map<@NotNull Enchantment, @NotNull Integer> enchantments);

  @Contract("null, _ -> fail; _, _ -> this")
  ItemBuilder enchantmentApply(Enchantment enchantment, int level);

  // TODO: more more ...
}
