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

import com.lgou2w.ldk.common.ComparisonChain;
import com.lgou2w.ldk.common.Valuable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

public final class Potion implements Valuable<String>, Comparable<Potion> {
  private final PotionType type;
  private final boolean upgraded, extended;

  @Contract("null, _, _ -> fail")
  public Potion(PotionType type, boolean upgraded, boolean extended) throws IllegalArgumentException {
    if (type == null) throw new NullPointerException("type");
    this.type = type;
    if (!(!upgraded || type.isUpgradeable())) throw new IllegalArgumentException("Potion Type is not upgradable.");
    if (!(!extended || type.isExtendable())) throw new IllegalArgumentException("Potion Type is not extendable.");
    if (!(!extended || !upgraded)) throw new IllegalArgumentException("Potion cannot be both extended and upgraded.");
    this.upgraded = upgraded;
    this.extended = extended;
  }

  @NotNull
  public PotionType getType() {
    return type;
  }

  public boolean isUpgraded() {
    return upgraded;
  }

  public boolean isExtended() {
    return extended;
  }

  @Override
  public String getValue() {
    return upgraded
      ? PREFIX_UPGRADED + type.getId()
      : extended
        ? PREFIX_EXTENDED + type.getId()
        : type.getId();
  }

  @Override
  public int compareTo(@NotNull Potion o) {
    return ComparisonChain.start()
      .compare(type, o.type)
      .compare(upgraded, o.upgraded)
      .compare(extended, o.extended)
      .result();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Potion potion = (Potion) o;
    return type == potion.type && upgraded == potion.upgraded && extended == potion.extended;
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, upgraded, extended);
  }

  @Override
  public String toString() {
    return "Potion{" +
      "type=" + type +
      ", upgraded=" + upgraded +
      ", extended=" + extended +
      '}';
  }

  /// See: https://minecraft.gamepedia.com/Potion

  final static String PREFIX_UPGRADED = "strong_";
  final static String PREFIX_EXTENDED = "long_";

  @NotNull
  @Contract("null -> fail")
  public static Potion parse(String value) throws IllegalArgumentException {
    if (value == null) throw new NullPointerException("value");
    String id = value.replace(PREFIX_UPGRADED, "").replace(PREFIX_EXTENDED, "");
    PotionType type = PotionType.valueOf(id.toUpperCase(Locale.ROOT));
    boolean upgradable = value.startsWith(PREFIX_UPGRADED);
    boolean extendable = value.startsWith(PREFIX_EXTENDED);
    return new Potion(type, upgradable, extendable);
  }
}
