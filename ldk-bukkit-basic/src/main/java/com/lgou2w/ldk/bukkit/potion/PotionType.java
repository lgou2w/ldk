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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum PotionType {

  UNCRAFTABLE("empty", false, false),
  WATER("water", false, false),
  MUNDANE("mundane", false, false),
  THICK("thick", false, false),
  AWKWARD("awkward", false, false),
  NIGHT_VISION("night_vision", false, true, PotionEffectType.NIGHT_VISION),
  INVISIBILITY("invisibility", false, true, PotionEffectType.INVISIBILITY),
  LEAPING("leaping", true, true, PotionEffectType.JUMP_BOOST),
  FIRE_RESISTANCE("fire_resistance", false, true, PotionEffectType.FIRE_RESISTANCE),
  SWIFTNESS("swiftness", true, true, PotionEffectType.SPEED),
  SLOWNESS("slowness", false, true, PotionEffectType.SLOWNESS),
  WATER_BREATHING("water_breathing", false, true, PotionEffectType.WATER_BREATHING),
  HEALING("healing", true, false, PotionEffectType.INSTANT_HEALTH),
  HARMING("harming", true, false, PotionEffectType.INSTANT_DAMAGE),
  POISON("poison", true, true, PotionEffectType.POISON),
  REGENERATION("regeneration", true, true, PotionEffectType.REGENERATION),
  STRENGTH("strength", true, true, PotionEffectType.STRENGTH),
  WEAKNESS("weakness", false, true, PotionEffectType.WEAKNESS),
  LUCK("luck", false, false, PotionEffectType.LUCK),
  TURTLE_MASTER("turtle_master", true, true, PotionEffectType.SLOWNESS), // Slowness IV and Resistance III
  SLOW_FALLING("slow_falling", false, true, PotionEffectType.SLOW_FALLING)
  ;

  private final String id;
  private final boolean upgradeable, extendable;
  private final @Nullable PotionEffectType effect;

  PotionType(String id, boolean upgradeable, boolean extendable, @Nullable PotionEffectType effect) {
    this.id = id;
    this.upgradeable = upgradeable;
    this.extendable = extendable;
    this.effect = effect;
  }

  PotionType(String id, boolean upgradable, boolean extendable) {
    this(id, upgradable, extendable, null);
  }

  @NotNull
  public String getId() {
    return id;
  }

  public boolean isUpgradeable() {
    return upgradeable;
  }

  public boolean isExtendable() {
    return extendable;
  }

  @Nullable
  public PotionEffectType getEffect() {
    return effect;
  }
}
