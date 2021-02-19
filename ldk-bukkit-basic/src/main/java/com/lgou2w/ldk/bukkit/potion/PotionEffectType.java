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

package com.lgou2w.ldk.bukkit.potion;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum PotionEffectType {

  SPEED(1, "speed"),
  SLOWNESS(2, "slow", "slowness"),
  HASTE(3, "fast_digging", "haste"),
  MINING_FATIGUE(4, "slow_digging", "mining_fatigue"),
  STRENGTH(5, "increase_damage", "strength"),
  INSTANT_HEALTH(6, "heal", "instant_health"),
  INSTANT_DAMAGE(7, "harm", "instant_damage"),
  JUMP_BOOST(8, "jump", "jump_boost"),
  NAUSEA(9, "confusion", "nausea"),
  REGENERATION(10, "regeneration"),
  RESISTANCE(11, "damage_resistance", "resistance"),
  FIRE_RESISTANCE(12, "fire_resistance"),
  WATER_BREATHING(13, "water_breathing"),
  INVISIBILITY(14, "invisibility"),
  BLINDNESS(15, "blindness"),
  NIGHT_VISION(16, "night_vision"),
  HUNGER(17, "hunger"),
  WEAKNESS(18, "weakness"),
  POISON(19, "poison"),
  WITHER(20, "wither"),
  HEALTH_BOOST(21, "health_boost"),
  ABSORPTION(22, "absorption"),
  SATURATION(23, "saturation"),
  GLOWING(24, "glowing"),
  LEVITATION(25, "levitation"),
  LUCK(26, "luck"),
  UNLUCK(27, "unluck"),
  SLOW_FALLING(28, "slow_falling"),
  CONDUIT_POWER(29, "conduit_power"),
  DOLPHINS_GRACE(30, "dolphins_grace"),
  BAD_OMEN(31, "bad_omen"),
  HERO_OF_THE_VILLAGE(32, "hero_of_the_village")
  ;

  private final int id;
  private final String legacy, flatting;

  PotionEffectType(int id, String legacy, String flatting) {
    this.id = id;
    this.legacy = legacy;
    this.flatting = flatting;
  }

  PotionEffectType(int id, String identical) {
    this.id = id;
    this.legacy = identical;
    this.flatting = identical;
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

  public boolean isInstantEffect() {
    return this == INSTANT_HEALTH ||
      this == INSTANT_DAMAGE ||
      this == SATURATION;
  }

  @NotNull
  public org.bukkit.potion.PotionEffectType toBukkit() throws UnsupportedOperationException {
    org.bukkit.potion.PotionEffectType type = org.bukkit.potion.PotionEffectType.getByName(flatting);
    if (type == null) type = org.bukkit.potion.PotionEffectType.getByName(legacy);
    if (type == null) throw new UnsupportedOperationException(
      "Server version does not unsupported this potion effect type: " + name());
    return type;
  }

  private final static Map<Integer, PotionEffectType> ID_MAP;
  private final static Map<String, PotionEffectType> NAME_MAP;

  static {
    Map<Integer, PotionEffectType> idMap = new HashMap<>();
    Map<String, PotionEffectType> nameMap = new HashMap<>();
    for (PotionEffectType type : PotionEffectType.values()) {
      idMap.put(type.id, type);
      nameMap.put(type.legacy, type);
      nameMap.put(type.flatting, type);
    }
    ID_MAP = Collections.unmodifiableMap(idMap);
    NAME_MAP = Collections.unmodifiableMap(nameMap);
  }

  @NotNull
  @Contract("null -> fail; !null -> !null")
  public static PotionEffectType fromBukkit(org.bukkit.potion.PotionEffectType type) {
    if (type == null) throw new NullPointerException("type");
    return NAME_MAP.get(type.getName());
  }

  @Nullable
  public static PotionEffectType fromId(int id) {
    return ID_MAP.get(id);
  }

  @Nullable
  @Contract("null -> null")
  public static PotionEffectType fromName(@Nullable String name) {
    if (name == null) return null;
    return NAME_MAP.get(name.toLowerCase(Locale.ENGLISH));
  }
}
