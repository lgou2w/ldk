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

import com.lgou2w.ldk.bukkit.version.BukkitVersion;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum Enchantment {

  PROTECTION(0, "protection_environmental", "protection"),
  FIRE_PROTECTION(1, "protection_fire", "fire_protection"),
  FEATHER_FALLING(2, "protection_fall", "feather_falling"),
  BLAST_PROTECTION(3, "protection_explosions", "blast_protection"),
  PROJECTILE_PROTECTION(4, "protection_projectile", "projectile_protection"),
  RESPIRATION(5, "oxygen", "respiration"),
  AQUA_AFFINITY(6, "water_worker", "aqua_affinity"),
  THORNS(7, "thorns"),
  DEPTH_STRIDER(8, "depth_strider"),
  FROST_WALKER(9, "frost_walker"),
  BINDING_CURSE(10, "binding_curse"),
  SHARPNESS(16, "damage_all", "sharpness"),
  SMITE(17, "damage_undead", "smite"),
  BANE_OF_ARTHROPODS(18, "damage_arthropods", "bane_of_arthropods"),
  KNOCKBACK(19, "knockback"),
  FIRE_ASPECT(20, "fire_aspect"),
  LOOTING(21, "loot_bonus_mobs", "looting"),
  SWEEPING(22, "sweeping_edge", "sweeping"),
  EFFICIENCY(32, "dig_speed", "efficiency"),
  SILK_TOUCH(33, "silk_touch"),
  UNBREAKING(34, "durability", "unbreaking"),
  FORTUNE(35, "loot_bonus_blocks", "fortune"),
  POWER(48, "arrow_damage", "power"),
  PUNCH(49, "arrow_knockback", "punch"),
  FLAME(50, "arrow_fire", "flame"),
  INFINITE(51, "arrow_infinite", "infinite"),
  LUCK_OF_THE_SEA(61, "luck", "luck_of_the_sea"),
  LURE(62, "lure"),
  LOYALTY(65, "loyalty"),
  IMPALING(66, "impaling"),
  RIPTIDE(67, "riptide"),
  CHANNELING(68, "channeling"),
  MENDING(70, "mending"),
  VANISHING_CURSE(71, "vanishing_curse"),

  ///
  /// From here on, these enchantment id are all maintained by LDK, since Minecraft 1.13 has completely removed the Id.
  /// 从这里开始, 这些附魔的 Id 都由 LDK 自行维护, 因为自从 Minecraft 1.13 已经完全移除 Id 了.
  ///

  MULTISHOT(200, "multishot"),
  QUICK_CHARGE(201, "quick_charge"),
  PIERCING(202, "piercing"),
  SOUL_SPEED(203, "soul_speed"),
  ;

  private final int id;
  private final String legacy, flatting;

  Enchantment(int id, String legacy, String flatting) {
    this.id = id;
    this.legacy = legacy;
    this.flatting = flatting;
  }

  Enchantment(int id, String identical) {
    this(id, identical, identical);
  }

  public int getId() {
    return id;
  }

  @NotNull
  public String getLegacy() {
    return legacy;
  }

  @NotNull
  public String getFlatting() {
    return flatting;
  }

  @SuppressWarnings("deprecation")
  @NotNull
  public org.bukkit.enchantments.Enchantment toBukkit() throws UnsupportedOperationException {
    org.bukkit.enchantments.Enchantment enchantment;
    if (BukkitVersion.isV113OrLater) {
      enchantment = org.bukkit.enchantments.Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft(flatting));
    } else {
      enchantment = org.bukkit.enchantments.Enchantment.getByName(legacy);
    }
    if (enchantment == null) throw new UnsupportedOperationException(
      "Server version does not unsupported this enchantment: " + name());
    return enchantment;
  }

  private final static Map<Integer, Enchantment> ID_MAP;
  private final static Map<String, Enchantment> NAME_MAP;

  static {
    Map<Integer, Enchantment> idMap = new HashMap<>();
    Map<String, Enchantment> nameMap = new HashMap<>();
    for (Enchantment enchantment : Enchantment.values()) {
      idMap.put(enchantment.id, enchantment);
      nameMap.put(enchantment.legacy, enchantment);
      nameMap.put(enchantment.flatting, enchantment);
    }
    ID_MAP = Collections.unmodifiableMap(idMap);
    NAME_MAP = Collections.unmodifiableMap(nameMap);
  }

  @SuppressWarnings("deprecation")
  @NotNull
  @Contract("null -> fail; !null -> !null")
  public static Enchantment fromBukkit(org.bukkit.enchantments.Enchantment enchantment) {
    if (enchantment == null) throw new NullPointerException("enchantment");
    if (BukkitVersion.isV113OrLater) {
      return NAME_MAP.get(enchantment.getKey().getKey());
    } else {
      return NAME_MAP.get(enchantment.getName());
    }
  }

  @Nullable
  public static Enchantment fromId(int id) {
    return ID_MAP.get(id);
  }

  @Nullable
  @Contract("null -> null")
  public static Enchantment fromName(@Nullable String name) {
    if (name == null) return null;
    return NAME_MAP.get(name.toLowerCase(Locale.ENGLISH));
  }
}
