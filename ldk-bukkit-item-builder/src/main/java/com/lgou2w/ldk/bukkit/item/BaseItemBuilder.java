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
import com.lgou2w.ldk.chat.ChatComponent;
import com.lgou2w.ldk.chat.ChatSerializer;
import com.lgou2w.ldk.nbt.CompoundTag;
import com.lgou2w.ldk.nbt.ListTag;
import com.lgou2w.ldk.nbt.StringTag;
import com.lgou2w.ldk.reflect.FieldAccessor;
import com.lgou2w.ldk.reflect.FuzzyReflection;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class BaseItemBuilder implements ItemBuilder {
  protected final Material type;
  protected final CompoundTag tag;
  protected int count, durability;

  @Contract("null, _, _, _ -> fail; _, _, _, null -> fail")
  protected BaseItemBuilder(Material type, int count, int durability, CompoundTag tag) {
    this.type = Objects.requireNonNull(type, "type");
    this.tag = Objects.requireNonNull(tag, "tag");
    this.count = count;
    this.durability = durability;
  }

  @Contract("null, _, _ -> fail")
  protected BaseItemBuilder(Material type, int count, int durability) {
    this(type, count, durability, new CompoundTag());
  }

  // TODO: docs
  /**
   * @see ItemBuilder#of(Material, int, int)
   */
  final static class Default extends BaseItemBuilder {
    @Contract("null, _, _ -> fail")
    Default(Material type, int count, int durability) {
      super(type, count, durability);
    }
  }

  @Override
  @NotNull
  @SuppressWarnings("deprecation")
  public ItemStack create() {
    ItemStack stack = !BukkitVersion.isV113OrLater
      ? new ItemStack(type, count, (short) durability)
      : new ItemStack(type, count);
    if (BukkitVersion.isV113OrLater && durability != 0)
      tag.setInt(TAG_DAMAGE, durability);
    ItemFactory.writeStackTag(stack, tag);
    return stack;
  }

  @Override
  public ItemBuilder count(int count) {
    this.count = count;
    return this;
  }

  @Override
  public ItemBuilder durability(int durability) {
    this.durability = durability;
    return this;
  }

  @Override
  public ItemBuilder displayName(@Nullable ChatComponent displayName) {
    if (displayName == null) {
      tag.getCompoundOrPresent(TAG_DISPLAY).remove(TAG_DISPLAY_NAME);
      return this;
    }

    String value = BukkitVersion.isV113OrLater
      ? ChatSerializer.toJson(displayName)
      : ChatSerializer.toPlainTextWithFormatted(displayName, false);
    tag
      .getCompoundOrPresent(TAG_DISPLAY)
      .setString(TAG_DISPLAY_NAME, value);
    return this;
  }

  @Override
  public ItemBuilder displayName(@Nullable String displayName) {
    if (displayName == null) {
      tag.getCompoundOrPresent(TAG_DISPLAY).remove(TAG_DISPLAY_NAME);
      return this;
    }

    String value = BukkitVersion.isV113OrLater
      ? ChatSerializer.toJson(ChatSerializer.fromPlainText(displayName))
      : displayName;
    tag
      .getCompoundOrPresent(TAG_DISPLAY)
      .setString(TAG_DISPLAY_NAME, value);
    return this;
  }

  @Override
  public ItemBuilder localizedName(@Nullable ChatComponent localizedName) {
    if (BukkitVersion.isV113OrLater) {
      displayName(localizedName);
    } else {
      localizedName(localizedName != null
        ? ChatSerializer.toPlainTextWithFormatted(localizedName, false)
        : null
      );
    }
    return this;
  }

  @Override
  public ItemBuilder localizedName(@Nullable String localizedName) {
    if (BukkitVersion.isV113OrLater) {
      displayName(localizedName != null
        ? ChatSerializer.fromPlainText(localizedName)
        : null
      );
    } else if (localizedName == null) {
      tag
        .getCompoundOrPresent(TAG_DISPLAY)
        .remove(TAG_DISPLAY_LOC_NAME);
    } else {
      tag
        .getCompoundOrPresent(TAG_DISPLAY)
        .setString(TAG_DISPLAY_LOC_NAME, localizedName);
    }
    return this;
  }

  @Override
  public ItemBuilder lore(@Nullable ChatComponent... lore) {
    if (lore == null || lore.length <= 0) {
      tag.getCompoundOrPresent(TAG_DISPLAY).remove(TAG_DISPLAY_LORE);
      return this;
    }

    String[] values = new String[lore.length];
    for (int i = 0; i < values.length; i++) {
      values[i] = BukkitVersion.isV113OrLater
        ? ChatSerializer.toJson(lore[i])
        : ChatSerializer.toPlainTextWithFormatted(lore[i], false);
    }
    tag
      .getCompoundOrPresent(TAG_DISPLAY)
      .set(TAG_DISPLAY_LORE, ListTag.of(it -> it.addString(values)));
    return this;
  }

  @Override
  public ItemBuilder lore(@Nullable String... lore) {
    if (lore == null || lore.length <= 0) {
      tag.getCompoundOrPresent(TAG_DISPLAY).remove(TAG_DISPLAY_LORE);
      return this;
    }

    String[] values = new String[lore.length];
    for (int i = 0; i < values.length; i++) {
      values[i] = BukkitVersion.isV113OrLater
        ? ChatSerializer.toJson(ChatSerializer.fromPlainText(lore[i]))
        : lore[i];
    }
    tag
      .getCompoundOrPresent(TAG_DISPLAY)
      .set(TAG_DISPLAY_LORE, ListTag.of(it -> it.addString(values)));
    return this;
  }

  @Override
  public ItemBuilder lorePrepend(@Nullable ChatComponent... lore) {
    loreInsert(0, lore);
    return this;
  }

  @Override
  public ItemBuilder lorePrepend(@Nullable String... lore) {
    loreInsert(0, lore);
    return this;
  }

  @Override
  public ItemBuilder loreAppend(@Nullable ChatComponent... lore) {
    loreInsert(Integer.MAX_VALUE, lore);
    return this;
  }

  @Override
  public ItemBuilder loreAppend(@Nullable String... lore) {
    loreInsert(Integer.MAX_VALUE, lore);
    return this;
  }

  private void loreInsert(int index, StringTag[] lore) {
    ListTag list = tag
      .getCompoundOrPresent(TAG_DISPLAY)
      .getListOrPresent(TAG_DISPLAY_LORE);
    int size = list.size();
    int pos = index <= 0 ? 0 : Math.min(index, size);
    list.addAll(pos, Arrays.asList(lore));
  }

  @Override
  public ItemBuilder loreInsert(int index, @Nullable ChatComponent... lore) {
    if (lore == null || lore.length <= 0) return this;

    StringTag[] values = new StringTag[lore.length];
    for (int i = 0; i < values.length; i++) {
      String value = BukkitVersion.isV113OrLater
        ? ChatSerializer.toJson(lore[i])
        : ChatSerializer.toPlainTextWithFormatted(lore[i], false);
      values[i] = new StringTag(value);
    }
    loreInsert(index, values);
    return this;
  }

  @Override
  public ItemBuilder loreInsert(int index, @Nullable String... lore) {
    if (lore == null || lore.length <= 0) return this;

    StringTag[] values = new StringTag[lore.length];
    for (int i = 0; i < values.length; i++) {
      String value = BukkitVersion.isV113OrLater
        ? ChatSerializer.toJson(ChatSerializer.fromPlainText(lore[i]))
        : lore[i];
      values[i] = new StringTag(value);
    }
    loreInsert(index, values);
    return this;
  }

  @Override
  public ItemBuilder customModelData(int customModelData) {
    tag.setInt(TAG_CUSTOM_MODEL_DATA, customModelData);
    return this;
  }

  @Override
  public ItemBuilder unbreakable(boolean unbreakable) {
    tag.setBoolean(TAG_UNBREAKABLE, unbreakable);
    return this;
  }

  // Bukkit 1.8 ~ Bukkit 1.12.2
  // OB.enchantments.Enchantment -> private int id
  @Nullable final static Supplier<@NotNull FieldAccessor<Enchantment, Integer>> FIELD_ENCHANTMENT_ID
    = !BukkitVersion.isV113OrLater ? null : FuzzyReflection.lazySupplier(Enchantment.class, true, fuzzy -> fuzzy
    .useFieldMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withType(int.class)
    .resultAccessorAs());

  @SuppressWarnings("ConstantConditions")
  private CompoundTag enchantmentToCompound(Enchantment enchantment, int level) {
    CompoundTag compound = new CompoundTag();
    if (BukkitVersion.isV113OrLater) {
      compound.setString(TAG_ENCHANTMENT_ID, enchantment.getNamespacedKey());
    } else {
      int id = FIELD_ENCHANTMENT_ID.get().get(enchantment);
      compound.setShort(TAG_ENCHANTMENT_ID, id);
    }
    compound.setShort(TAG_ENCHANTMENT_LEVEL, level);
    return compound;
  }

  @Override
  public ItemBuilder enchantment(@Nullable Map<@NotNull Enchantment, @NotNull Integer> enchantments) {
    if (enchantments == null || enchantments.isEmpty()) {
      tag.remove(TAG_ENCH);
      tag.remove(TAG_ENCHANTMENTS);
      return this;
    }

    List<CompoundTag> values = new ArrayList<>(enchantments.size());
    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
      Enchantment key = Objects.requireNonNull(entry.getKey(), "enchantments key");
      int value = Objects.requireNonNull(entry.getValue(), "enchantments value");
      values.add(enchantmentToCompound(key, value));
    }
    ListTag list = tag.getListOrPresent(BukkitVersion.isV113OrLater ? TAG_ENCHANTMENTS : TAG_ENCH);
    list.addCompound(values.toArray(new CompoundTag[0]));
    return this;
  }

  @Override
  public ItemBuilder enchantmentApply(Enchantment enchantment, int level) {
    Objects.requireNonNull(enchantment, "enchantment");

    ListTag list = tag.getListOrPresent(BukkitVersion.isV113OrLater ? TAG_ENCHANTMENTS : TAG_ENCH);
    CompoundTag value = enchantmentToCompound(enchantment, level);
    list.addCompound(value);
    return this;
  }

  final static String TAG_DAMAGE = "Damage";
  final static String TAG_DISPLAY = "display";
  final static String TAG_DISPLAY_NAME = "Name";
  final static String TAG_DISPLAY_LOC_NAME = "LocName";
  final static String TAG_DISPLAY_LORE = "Lore";
  final static String TAG_CUSTOM_MODEL_DATA = "CustomModelData";
  final static String TAG_UNBREAKABLE = "Unbreakable";
  final static String TAG_ENCH = "ench"; // Minecraft 1.13 Before
  final static String TAG_ENCHANTMENTS = "Enchantments"; // Since Minecraft 1.13 or later
  final static String TAG_ENCHANTMENT_ID = "id";
  final static String TAG_ENCHANTMENT_LEVEL = "lvl";
}
