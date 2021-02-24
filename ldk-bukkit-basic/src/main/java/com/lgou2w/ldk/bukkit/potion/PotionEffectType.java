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

import java.util.Objects;

public final class PotionEffectType {
  @NotNull private final String namespacedKey;
  @NotNull private final String namespace, key;
  // legacy Minecraft 1.12 and before
  @Nullable private final Integer id;

  @Contract("null, _, _ -> fail; _, null, _ -> fail")
  PotionEffectType(String namespace, String key, @Nullable Integer id) {
    this.namespace = Objects.requireNonNull(namespace, "namespace");
    this.key = Objects.requireNonNull(key, "key");
    this.namespacedKey = namespace + ':' + key;
    this.id = id;
  }

  @Contract("null, _ -> fail")
  PotionEffectType(String key, @Nullable Integer id) {
    this("minecraft", key, id);
  }

  @Contract("null -> fail")
  PotionEffectType(String key) {
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PotionEffectType that = (PotionEffectType) o;
    return namespace.equals(that.namespace) && key.equals(that.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(namespace, key);
  }

  @Override
  public String toString() {
    return "PotionEffectType{" + namespacedKey + '}';
  }

  public final static PotionEffectType SPEED = new PotionEffectType("speed", 1);
  public final static PotionEffectType SLOWNESS = new PotionEffectType("slowness", 2);
  public final static PotionEffectType HASTE = new PotionEffectType("haste", 3);
  public final static PotionEffectType MINING_FATIGUE = new PotionEffectType("mining_fatigue", 4);
  public final static PotionEffectType STRENGTH = new PotionEffectType("strength", 5);
  public final static PotionEffectType INSTANT_HEALTH = new PotionEffectType("instant_health", 6);
  public final static PotionEffectType INSTANT_DAMAGE = new PotionEffectType("instant_damage", 7);
  public final static PotionEffectType JUMP_BOOST = new PotionEffectType("jump_boost", 8);
  public final static PotionEffectType NAUSEA = new PotionEffectType("nausea", 9);
  public final static PotionEffectType REGENERATION = new PotionEffectType("regeneration", 10);
  public final static PotionEffectType RESISTANCE = new PotionEffectType("resistance", 11);
  public final static PotionEffectType FIRE_RESISTANCE = new PotionEffectType("fire_resistance", 12);
  public final static PotionEffectType WATER_BREATHING = new PotionEffectType("water_breathing", 13);
  public final static PotionEffectType INVISIBILITY = new PotionEffectType("invisibility", 14);
  public final static PotionEffectType BLINDNESS = new PotionEffectType("blindness", 15);
  public final static PotionEffectType NIGHT_VISION = new PotionEffectType("night_vision", 16);
  public final static PotionEffectType HUNGER = new PotionEffectType("hunger", 17);
  public final static PotionEffectType WEAKNESS = new PotionEffectType("weakness", 18);
  public final static PotionEffectType POISON = new PotionEffectType("poison", 19);
  public final static PotionEffectType WITHER = new PotionEffectType("wither", 20);
  public final static PotionEffectType HEALTH_BOOST = new PotionEffectType("health_boost", 21);
  public final static PotionEffectType ABSORPTION = new PotionEffectType("absorption", 22);
  public final static PotionEffectType SATURATION = new PotionEffectType("saturation", 23);
  public final static PotionEffectType GLOWING = new PotionEffectType("glowing", 24);
  public final static PotionEffectType LEVITATION = new PotionEffectType("levitation", 25);
  public final static PotionEffectType LUCK = new PotionEffectType("luck", 26);
  public final static PotionEffectType UNLUCK = new PotionEffectType("unluck", 27);

  public final static PotionEffectType SLOW_FALLING = new PotionEffectType("slow_falling", 28);
  public final static PotionEffectType CONDUIT_POWER = new PotionEffectType("conduit_power", 29);
  public final static PotionEffectType DOLPHINS_GRACE = new PotionEffectType("dolphins_grace", 30);
  public final static PotionEffectType BAD_OMEN = new PotionEffectType("bad_omen", 31);
  public final static PotionEffectType HERO_OF_THE_VILLAGE = new PotionEffectType("hero_of_the_village", 32);
}
