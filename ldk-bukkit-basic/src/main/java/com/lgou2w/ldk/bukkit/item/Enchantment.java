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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Enchantment {
  @NotNull private final String namespacedKey;
  @NotNull private final String namespace;
  @NotNull private final String key;
  // legacy Minecraft 1.12 and before
  @Nullable private final Integer id;

  @Contract("null, _, _ -> fail; _, null, _ -> fail")
  Enchantment(String namespace, String key, @Nullable Integer id) {
    this.namespace = Objects.requireNonNull(namespace, "namespace");
    this.key = Objects.requireNonNull(key, "key");
    this.namespacedKey = namespace + ':' + key;
    this.id = id;
  }

  @Contract("null, _ -> fail")
  Enchantment(String key, @Nullable Integer id) {
    this("minecraft", key, id);
  }

  @Contract("null -> fail")
  Enchantment(String key) {
    this("minecraft", key, null);
  }

  @NotNull
  public String getNamespace() {
    return namespace;
  }

  @NotNull
  public String getKey() {
    return key;
  }

  @NotNull
  public String getNamespacedKey() {
    return namespacedKey;
  }

  @Deprecated
  public int getId() {
    return id != null ? id : -1;
  }

  @Override
  public String toString() {
    return namespacedKey;
  }

  public final static Enchantment PROTECTION = new Enchantment("protection", 0);
  public final static Enchantment FIRE_PROTECTION = new Enchantment("fire_protection", 1);
  public final static Enchantment FEATHER_FALLING = new Enchantment("feather_falling", 2);
  public final static Enchantment BLAST_PROTECTION = new Enchantment("blast_protection", 3);
  public final static Enchantment PROJECTILE_PROTECTION = new Enchantment("projectile_protection", 4);
  public final static Enchantment RESPIRATION = new Enchantment("respiration", 5);
  public final static Enchantment AQUA_AFFINITY = new Enchantment("aqua_affinity", 6);
  public final static Enchantment THORNS = new Enchantment("thorns", 7);
  public final static Enchantment DEPTH_STRIDER = new Enchantment("depth_strider", 8);
  public final static Enchantment FROST_WALKER = new Enchantment("frost_walker", 9);
  public final static Enchantment BINDING_CURSE = new Enchantment("binding_curse", 10);

  public final static Enchantment SHARPNESS = new Enchantment("sharpness", 16);
  public final static Enchantment SMITE = new Enchantment("smite", 17);
  public final static Enchantment BANE_OF_ARTHROPODS = new Enchantment("bane_of_arthropods", 18);
  public final static Enchantment KNOCKBACK = new Enchantment("knockback", 19);
  public final static Enchantment FIRE_ASPECT = new Enchantment("fire_aspect", 20);
  public final static Enchantment LOOTING = new Enchantment("looting", 21);
  public final static Enchantment SWEEPING = new Enchantment("sweeping", 22);

  public final static Enchantment EFFICIENCY = new Enchantment("efficiency", 32);
  public final static Enchantment SILK_TOUCH = new Enchantment("silk_touch", 33);
  public final static Enchantment UNBREAKING = new Enchantment("unbreaking", 34);
  public final static Enchantment FORTUNE = new Enchantment("fortune", 35);
  public final static Enchantment MENDING = new Enchantment("mending", 36);
  public final static Enchantment VANISHING_CURSE = new Enchantment("vanishing_curse", 37);

  public final static Enchantment POWER = new Enchantment("power", 48);
  public final static Enchantment PUNCH = new Enchantment("punch", 49);
  public final static Enchantment FLAME = new Enchantment("flame", 50);
  public final static Enchantment INFINITE = new Enchantment("infinite", 51);
  public final static Enchantment LUCK_OF_THE_SEA = new Enchantment("luck_of_the_sea", 61);
  public final static Enchantment LURE = new Enchantment("lure", 62);

  public final static Enchantment LOYALTY = new Enchantment("loyalty");
  public final static Enchantment IMPALING = new Enchantment("impaling");
  public final static Enchantment RIPTIDE = new Enchantment("riptide");
  public final static Enchantment CHANNELING = new Enchantment("channeling");

  public final static Enchantment MULTISHOT = new Enchantment("multishot");
  public final static Enchantment QUICK_CHARGE = new Enchantment("quick_charge");
  public final static Enchantment PIERCING = new Enchantment("piercing");
  public final static Enchantment SOUL_SPEED = new Enchantment("soul_speed");
}
