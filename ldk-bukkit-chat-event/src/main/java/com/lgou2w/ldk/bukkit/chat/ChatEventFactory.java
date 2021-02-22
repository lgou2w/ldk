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

package com.lgou2w.ldk.bukkit.chat;

import com.google.gson.JsonElement;
import com.lgou2w.ldk.bukkit.item.ItemFactory;
import com.lgou2w.ldk.bukkit.version.BukkitVersion;
import com.lgou2w.ldk.chat.ChatComponent;
import com.lgou2w.ldk.chat.ChatSerializer;
import com.lgou2w.ldk.chat.HoverEvent;
import com.lgou2w.ldk.chat.Style;
import com.lgou2w.ldk.nbt.CompoundTag;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public final class ChatEventFactory {

  private ChatEventFactory() { }

  @NotNull
  @Contract("null -> fail")
  @SuppressWarnings("deprecation")
  public static HoverEvent makeHoverShowItem(ItemStack stack) {
    Objects.requireNonNull(stack, "stack");
    String id = ItemFactory.materialToMinecraftKey(stack.getType());
    int count = stack.getAmount(), damage = stack.getDurability();
    CompoundTag tag = ItemFactory.readStackTag(stack);
    String mojangsonTag = tag == null ? null : tag.toMojangson();
    HoverEvent.ItemStackInfo value = new HoverEvent.ItemStackInfo(id, count, damage, mojangsonTag);
    return new HoverEvent(HoverEvent.Action.SHOW_ITEM, value);
  }

  @NotNull
  @Contract("null, _, _ -> fail; _, null, _ -> fail")
  public static HoverEvent makeHoverShowEntity(EntityType type, UUID id, @Nullable ChatComponent name) {
    Objects.requireNonNull(type, "type");
    Objects.requireNonNull(id, "id");
    Function<HoverEvent.EntityInfo, JsonElement> adapter = !BukkitVersion.isV19OrLater
      ? HoverEvent.EntityInfo::legacyAdapterV18
      : !BukkitVersion.isV113OrLater
        ? HoverEvent.EntityInfo::legacyAdapterV19ToV112
        : HoverEvent.EntityInfo::legacyAdapterV113;

    String typeName = type.name().toLowerCase(Locale.ENGLISH);
    HoverEvent.EntityInfo value = new HoverEvent.EntityInfo(typeName, id, name, adapter);
    return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, value);
  }

  @NotNull
  @Contract("null, _ -> fail")
  public static HoverEvent makeHoverShowEntity(Entity entity, @Nullable ChatComponent name) {
    Objects.requireNonNull(entity, "entity");
    ChatComponent customName = entity.getCustomName() != null && name == null
      ? ChatSerializer.fromPlainText(entity.getCustomName())
      : name;
    return makeHoverShowEntity(entity.getType(), entity.getUniqueId(), customName);
  }

  @NotNull
  @Contract("null -> fail")
  public static HoverEvent makeHoverShowEntity(Entity entity) {
    return makeHoverShowEntity(entity, null);
  }

  @Nullable
  @Contract("null, _ -> null; !null, null -> fail; !null, !null -> param1")
  public static ChatComponent withHoverShowItem(ChatComponent component, ItemStack stack) {
    if (component == null) return null;
    HoverEvent hoverEvent = makeHoverShowItem(stack);
    component.withStyle(Style.EMPTY.withHoverEvent(hoverEvent));
    return component;
  }

  @Nullable
  @Contract("null, _ -> null; !null, null -> fail; !null, !null -> param1")
  public static ChatComponent withHoverShowEntity(ChatComponent component, Entity entity) {
    if (component == null) return null;
    HoverEvent hoverEvent = makeHoverShowEntity(entity);
    component.withStyle(Style.EMPTY.withHoverEvent(hoverEvent));
    return component;
  }
}
