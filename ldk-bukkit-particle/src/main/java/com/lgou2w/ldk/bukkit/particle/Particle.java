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

package com.lgou2w.ldk.bukkit.particle;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public final class Particle {
  @NotNull private final String namespacedKey;
  @NotNull private final String namespace;
  @NotNull private final String key;
  // legacy Minecraft 1.12 and before
  @Nullable private final Integer id;

  @Contract("null, _, _ -> fail; _, null, _ -> fail")
  Particle(String namespace, String key, @Nullable Integer id) {
    this.namespace = Objects.requireNonNull(namespace, "namespace");
    this.key = Objects.requireNonNull(key, "key");
    this.namespacedKey = namespace + ':' + key;
    this.id = id;
  }

  @Contract("null, _ -> fail")
  Particle(String key, @Nullable Integer id) {
    this("minecraft", key, id);
  }

  @Contract("null -> fail")
  Particle(String key) {
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
    Particle particle = (Particle) o;
    return namespace.equals(particle.namespace) && key.equals(particle.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(namespace, key);
  }

  @Override
  public String toString() {
    return "Particle{" + namespacedKey + '}';
  }

  public final static Particle POOF = new Particle("poof", 0);
  public final static Particle EXPLOSION = new Particle("explosion", 1);
  public final static Particle EXPLOSION_EMITTER = new Particle("explosion_emitter", 2);
  public final static Particle FIREWORK = new Particle("firework", 3);
  public final static Particle BUBBLE = new Particle("bubble", 4);
  public final static Particle SPLASH = new Particle("splash", 5);
  public final static Particle FISHING = new Particle("fishing", 6);
  public final static Particle UNDERWATER = new Particle("underwater", 7);
  @Deprecated // Removed in Minecraft 1.13. Replaced -> UNDERWATER
  public final static Particle UNDERWATER_DEPTH = new Particle("underwater", 8);
  public final static Particle CRIT = new Particle("crit", 9);
  public final static Particle ENCHANTED_HIT = new Particle("enchanted_hit", 10);
  public final static Particle SMOKE = new Particle("smoke", 11);
  public final static Particle LARGE_SMOKE = new Particle("large_smoke", 12);
  public final static Particle EFFECT = new Particle("effect", 13);
  public final static Particle INSTANT_EFFECT = new Particle("instant_effect", 14);
  public final static Particle ENTITY_EFFECT = new Particle("entity_effect", 15);
  public final static Particle AMBIENT_ENTITY_EFFECT = new Particle("ambient_entity_effect", 16);
  public final static Particle WITCH = new Particle("witch", 17);
  public final static Particle DRIPPING_WATER = new Particle("dripping_water", 18);
  public final static Particle DRIPPING_LAVA = new Particle("dripping_lava", 19);
  public final static Particle ANGRY_VILLAGER = new Particle("angry_villager", 20);
  public final static Particle HAPPY_VILLAGER = new Particle("happy_villager", 21);
  public final static Particle MYCELIUM = new Particle("mycelium", 22);
  public final static Particle NOTE = new Particle("note", 23);
  public final static Particle PROTAL = new Particle("protal", 24);
  public final static Particle ENCHANT = new Particle("enchant", 25);
  public final static Particle FLAME = new Particle("flame", 26);
  public final static Particle LAVA = new Particle("lava", 27);
  @Deprecated // Removed in Minecraft 1.13. Replaced -> BARRIER
  public final static Particle FOOTSTEP = new Particle("barrier", 28);
  public final static Particle CLOUD = new Particle("cloud", 29);
  public final static Particle DUST = new Particle("dust", 30);
  public final static Particle ITEM_SNOWBALL = new Particle("item_snowball", 31);
  @Deprecated // Removed in Minecraft 1.13. Replaced -> ITEM_SNOWBALL
  public final static Particle ITEM_SNOW_SHOVEL = new Particle("item_snowball", 32);
  public final static Particle ITEM_SLIME = new Particle("item_slime", 33);
  public final static Particle HEART = new Particle("heart", 34);
  public final static Particle BARRIER = new Particle("barrier", 35);
  public final static Particle ITEM = new Particle("item", 36);
  public final static Particle BLOCK = new Particle("block", 37);
  @Deprecated // Removed in Minecraft 1.13. Replaced -> BLOCK
  public final static Particle BLOCK_DUST = new Particle("block", 38);
  public final static Particle RAIN = new Particle("rain", 39);
  @Deprecated // Removed in Minecraft 1.13. Replaced -> ITEM
  public final static Particle ITEM_TAKE = new Particle("item", 40);
  public final static Particle ELDER_GUARDIAN = new Particle("elder_guardian", 41);

  // Minecraft 1.9 or later
  public final static Particle DRAGON_BREATH = new Particle("dragon_breath", 42);
  public final static Particle END_ROD = new Particle("end_rod", 43);
  public final static Particle DAMAGE_INDICATOR = new Particle("damage_indicator", 44);
  public final static Particle SWEEP_ATTACK = new Particle("sweep_attack", 45);

  // Minecraft 1.10 or later
  public final static Particle FALLING_DUST = new Particle("falling_dust", 46);

  // Minecraft 1.11 or later
  public final static Particle TOTEM_OF_UNDYING = new Particle("totem_of_undying", 47);
  public final static Particle SPIT = new Particle("spit", 48);

  // Minecraft 1.13 or later. No longer support ID!
  public final static Particle SQUID_INK = new Particle("squid_ink");
  public final static Particle BUBBLE_POP = new Particle("bubble_pop");
  public final static Particle CURRENT_DOWN = new Particle("current_down");
  public final static Particle BUBBLE_COLUMN_UP = new Particle("bubble_column_up");
  public final static Particle NAUTILUS = new Particle("nautilus");
  public final static Particle DOLPHIN = new Particle("dolphin");

  // Minecraft 1.14 or later
  public final static Particle SNEEZE = new Particle("sneeze");
  public final static Particle CAMPFIRE_COSY_SMOKE = new Particle("campfire_cosy_smoke");
  public final static Particle CAMPFIRE_SIGNAL_SMOKE = new Particle("campfire_signal_smoke");
  public final static Particle COMPOSTER = new Particle("composter");
  public final static Particle FLASH = new Particle("flash");
  public final static Particle FALLING_LAVA = new Particle("falling_lava");
  public final static Particle LANDING_LAVA = new Particle("landing_lava");
  public final static Particle FALLING_WATER = new Particle("falling_water");

  // Minecraft 1.15 or later
  public final static Particle DRIPPING_HONEY = new Particle("dripping_honey");
  public final static Particle FALLING_HONEY = new Particle("falling_honey");
  public final static Particle LANDING_HONEY = new Particle("landing_honey");
  public final static Particle FALLING_NECTAR = new Particle("falling_nectar");

  // Minecraft 1.16 or later
  public final static Particle SOUL_FIRE_FLAME = new Particle("soul_fire_flame");
  public final static Particle ASH = new Particle("ash");
  public final static Particle CRIMSON_SPORE = new Particle("crimson_spore");
  public final static Particle WARPED_SPORE = new Particle("warped_spore");
  public final static Particle SOUL = new Particle("soul");
  public final static Particle DRIPPING_OBSIDIAN_TEAR = new Particle("dripping_obsidian_tear");
  public final static Particle FALLING_OBSIDIAN_TEAR = new Particle("falling_obsidian_tear");
  public final static Particle LANDING_OBSIDIAN_TEAR = new Particle("landing_obsidian_tear");
  public final static Particle REVERSE_PORTAL = new Particle("reverse_portal");
  public final static Particle WHITE_ASH = new Particle("white_ash");

  // Instance methods

  public void display(@NotNull Location center, double range, float offsetX, float offsetY, float offsetZ, float speed, int count, @Nullable Object data) {
    ParticleFactory.sendParticle(this, center, range, offsetX, offsetY, offsetZ, speed, count, data);
  }

  public void display(@NotNull Location center, double range, float offsetX, float offsetY, float offsetZ, float speed, @Nullable Object data) {
    display(center, range, offsetX, offsetY, offsetZ, speed, 1, data);
  }

  public void display(@NotNull Location center, double range, float offsetX, float offsetY, float offsetZ, float speed, int count) {
    display(center, range, offsetX, offsetY, offsetZ, speed, count, null);
  }

  public void display(@NotNull Location center, double range, float offsetX, float offsetY, float offsetZ, float speed) {
    display(center, range, offsetX, offsetY, offsetZ, speed, 1, null);
  }

  public void display(@NotNull Location center, double range, float offsetX, float offsetY, float offsetZ, @Nullable Object data) {
    display(center, range, offsetX, offsetY, offsetZ, 0f, 1, data);
  }

  public void display(@NotNull Location center, double range, float offsetX, float offsetY, float offsetZ) {
    display(center, range, offsetX, offsetY, offsetZ, 0f, 1, null);
  }

  public void display(@NotNull Location center, double range, @Nullable Object data) {
    display(center, range, 0f, 0f, 0f, 0f, 1, data);
  }

  public void display(@NotNull Location center, double range) {
    display(center, range, 0f, 0f, 0f, 0f, 1, null);
  }

  public void display(@NotNull Location center, @Nullable Object data) {
    display(center, 32d, 0f, 0f, 0f, 0f, 1, data);
  }

  public void display(@NotNull Location center) {
    display(center, 32d, 0f, 0f, 0f, 0f, 1, null);
  }

  public void display(@Nullable Player sender, @NotNull Location center, float offsetX, float offsetY, float offsetZ, float speed, int count, @Nullable Object data) {
    ParticleFactory.sendParticle(this, sender, center, offsetX, offsetY, offsetZ, speed, count, data);
  }

  public void display(@Nullable Player sender, @NotNull Location center, float offsetX, float offsetY, float offsetZ, float speed, @Nullable Object data) {
    display(sender, center, offsetX, offsetY, offsetZ, speed, 1, data);
  }

  public void display(@Nullable Player sender, @NotNull Location center, float offsetX, float offsetY, float offsetZ, float speed, int count) {
    display(sender, center, offsetX, offsetY, offsetZ, speed, count, null);
  }

  public void display(@Nullable Player sender, @NotNull Location center, float offsetX, float offsetY, float offsetZ, float speed) {
    display(sender, center, offsetX, offsetY, offsetZ, speed, 1, null);
  }

  public void display(@Nullable Player sender, @NotNull Location center, float offsetX, float offsetY, float offsetZ, @Nullable Object data) {
    display(sender, center, offsetX, offsetY, offsetZ, 0f, 1, data);
  }

  public void display(@Nullable Player sender, @NotNull Location center, float offsetX, float offsetY, float offsetZ) {
    display(sender, center, offsetX, offsetY, offsetZ, 0f, 1, null);
  }

  public void display(@Nullable Player sender, @NotNull Location center, @Nullable Object data) {
    display(sender, center, 0f, 0f, 0f, 0f, 1, data);
  }

  public void display(@Nullable Player sender, @NotNull Location center) {
    display(sender, center, 0f, 0f, 0f, 0f, 1, null);
  }

  public void display(@NotNull List<Player> receivers, @NotNull Location center, float offsetX, float offsetY, float offsetZ, float speed, int count, @Nullable Object data) {
    ParticleFactory.sendParticle(this, receivers, center, offsetX, offsetY, offsetZ, speed, count, data);
  }

  public void display(@NotNull List<Player> receivers, @NotNull Location center, float offsetX, float offsetY, float offsetZ, float speed, @Nullable Object data) {
    display(receivers, center, offsetX, offsetY, offsetZ, speed, 1, data);
  }

  public void display(@NotNull List<Player> receivers, @NotNull Location center, float offsetX, float offsetY, float offsetZ, float speed, int count) {
    display(receivers, center, offsetX, offsetY, offsetZ, speed, count, null);
  }

  public void display(@NotNull List<Player> receivers, @NotNull Location center, float offsetX, float offsetY, float offsetZ, float speed) {
    display(receivers, center, offsetX, offsetY, offsetZ, speed, 1, null);
  }

  public void display(@NotNull List<Player> receivers, @NotNull Location center, float offsetX, float offsetY, float offsetZ, @Nullable Object data) {
    display(receivers, center, offsetX, offsetY, offsetZ, 0f, 1, data);
  }

  public void display(@NotNull List<Player> receivers, @NotNull Location center, float offsetX, float offsetY, float offsetZ) {
    display(receivers, center, offsetX, offsetY, offsetZ, 0f, 1, null);
  }

  public void display(@NotNull List<Player> receivers, @NotNull Location center, @Nullable Object data) {
    display(receivers, center, 0f, 0f, 0f, 0f, 1, data);
  }

  public void display(@NotNull List<Player> receivers, @NotNull Location center) {
    display(receivers, center, 0f, 0f, 0f, 0f, 1, null);
  }
}
