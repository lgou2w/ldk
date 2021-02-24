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

import java.util.Objects;

public final class PotionEffect {
  private final PotionEffectType effect;
  private final int amplifier, duration;
  private final boolean ambient, particles, icon;

  @Contract("null, _, _, _, _, _ -> fail")
  public PotionEffect(PotionEffectType effect, int amplifier, int duration, boolean ambient, boolean particles, boolean icon) {
    this.effect = Objects.requireNonNull(effect, "effect");
    this.amplifier = amplifier;
    this.duration = duration;
    this.ambient = ambient;
    this.particles = particles;
    this.icon = icon;
  }

  @Contract("null, _, _, _, _ -> fail")
  public PotionEffect(PotionEffectType effect, int amplifier, int duration, boolean ambient, boolean particles) {
    this(effect, amplifier, duration, ambient, particles, particles);
  }

  @Contract("null, _, _, _ -> fail")
  public PotionEffect(PotionEffectType effect, int amplifier, int duration, boolean ambient) {
    this(effect, amplifier, duration, ambient, true);
  }

  @Contract("null, _, _ -> fail")
  public PotionEffect(PotionEffectType effect, int amplifier, int duration) {
    this(effect, amplifier, duration, true);
  }

  @NotNull
  public PotionEffectType getEffect() {
    return effect;
  }

  public int getAmplifier() {
    return amplifier;
  }

  public int getDuration() {
    return duration;
  }

  public boolean isAmbient() {
    return ambient;
  }

  public boolean hasParticles() {
    return particles;
  }

  public boolean hasIcon() {
    return icon;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PotionEffect that = (PotionEffect) o;
    return effect.equals(that.effect) && amplifier == that.amplifier &&
      duration == that.duration && ambient == that.ambient &&
      particles == that.particles && icon == that.icon;
  }

  @Override
  public int hashCode() {
    return Objects.hash(effect, amplifier, duration, ambient, particles, icon);
  }

  @Override
  public String toString() {
    return "PotionEffect{" +
      "effect=" + effect +
      ", amplifier=" + amplifier +
      ", duration=" + duration +
      ", ambient=" + ambient +
      ", particles=" + particles +
      ", icon=" + icon +
      '}';
  }
}
