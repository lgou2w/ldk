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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lgou2w.ldk.bukkit.packet.PacketFactory;
import com.lgou2w.ldk.bukkit.version.BukkitVersion;
import com.lgou2w.ldk.chat.ChatComponent;
import com.lgou2w.ldk.chat.ChatSerializer;
import com.lgou2w.ldk.common.Enums;
import com.lgou2w.ldk.reflect.ConstructorAccessor;
import com.lgou2w.ldk.reflect.ConstructorReflectionMatcher;
import com.lgou2w.ldk.reflect.FieldAccessor;
import com.lgou2w.ldk.reflect.FuzzyReflection;
import com.lgou2w.ldk.reflect.MethodAccessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getMinecraftClass;
import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getMinecraftClassOrNull;

public final class ChatFactory {

  private ChatFactory() { }

  /// NMS Classes

  @NotNull public final static Class<?> CLASS_ICHAT_BASE_COMPONENT;
  @NotNull public final static Class<?> CLASS_CHAT_SERIALIZER;
  @NotNull public final static Class<?> CLASS_PACKET_PLAY_OUT_CHAT;
  @Nullable public final static Class<?> CLASS_CHAT_MESSAGE_TYPE;

  // TODO: docs
  @NotNull final static Class<?> CLASS_GSON_RELOCATED;
  final static boolean GSON_RELOCATED;

  static {
    try {
      CLASS_ICHAT_BASE_COMPONENT = getMinecraftClass("IChatBaseComponent");
      CLASS_CHAT_SERIALIZER = getMinecraftClass("IChatBaseComponent$ChatSerializer", "ChatSerializer");
      CLASS_PACKET_PLAY_OUT_CHAT = getMinecraftClass("PacketPlayOutChat");
      CLASS_CHAT_MESSAGE_TYPE = getMinecraftClassOrNull("ChatMessageType");
      Class<?> gsonRelocated = null;
      try {
        gsonRelocated = Class.forName("org.bukkit.craftbukkit.libs.com.google.gson.Gson");
      } catch (ClassNotFoundException ignore) {
      } finally {
        CLASS_GSON_RELOCATED = gsonRelocated != null ? gsonRelocated : Gson.class;
        GSON_RELOCATED = gsonRelocated != Gson.class;
      }
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Error in initializing ChatFactory internal static block:", e);
    }
  }

  /// NMS Accessors

  // 1.16 before    NMS.PacketPlayOutChat -> public Constructor(NMS.IChatBaseComponent, NMS.ChatMessageType | byte)
  // 1.16 and after NMS.PacketPlayOutChat -> public Constructor(NMS.IChatBaseComponent, NMS.ChatMessageType, UUID)
  @SuppressWarnings("ConstantConditions")
  final static Supplier<@NotNull ConstructorAccessor<Object>> CONSTRUCTOR_PACKET_PLAY_OUT_CHAT
    = FuzzyReflection.lazySupplier(CLASS_PACKET_PLAY_OUT_CHAT, true, fuzzy -> {
      ConstructorReflectionMatcher<Object> matcher = fuzzy.useConstructorMatcher();
      if (!BukkitVersion.isV116OrLater) {
        return matcher
          .withArgs(CLASS_ICHAT_BASE_COMPONENT, CLASS_CHAT_MESSAGE_TYPE != null ? CLASS_CHAT_MESSAGE_TYPE : byte.class)
          .resultAccessor("Missing match: NMS.PacketPlayOutChat -> Constructor(NMS.IChatBaseComponent, " +
            CLASS_CHAT_MESSAGE_TYPE != null ? "NMS.ChatMessageType)" : "byte)");
      } else {
        return matcher
          .withArgs(CLASS_ICHAT_BASE_COMPONENT, CLASS_CHAT_MESSAGE_TYPE, UUID.class)
          .resultAccessor("Missing match: NMS.PacketPlayOutChat -> Constructor(NMS.IChatBaseComponent, NMS.ChatMessageType, UUID)");
      }
    });

  // NMS.ChatSerializer -> public static Gson GSON;
  final static Supplier<@NotNull FieldAccessor<Object, Gson>> FIELD_CHAT_SERIALIZER_GSON
    = FuzzyReflection.lazySupplier(CLASS_CHAT_SERIALIZER, true, fuzzy -> fuzzy
    .useFieldMatcher()
    .withModifiers(Modifier.STATIC)
    .withType(CLASS_GSON_RELOCATED)
    .resultAccessorAs("Missing match: NMS.ChatSerializer -> public final static Gson GSON"));

  /** @see ChatFactory#CLASS_GSON_RELOCATED */
  @Nullable final static Supplier<@NotNull MethodAccessor<Object, String>> METHOD_GSON_RELOCATED_TO_JSON
    = !GSON_RELOCATED ? null : FuzzyReflection.lazySupplier(CLASS_GSON_RELOCATED, fuzzy -> fuzzy
    .useMethodMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withModifiers(Modifier.PUBLIC)
    .withArgs(String.class, Class.class)
    .withName("toJson")
    .resultAccessorAs("Missing match: " + CLASS_GSON_RELOCATED.getCanonicalName() + " -> public <T> T toJson(String, Class<T>)"));

  /** @see ChatFactory#CLASS_GSON_RELOCATED */
  @Nullable final static Supplier<@NotNull MethodAccessor<Object, String>> METHOD_GSON_RELOCATED_FROM_JSON
    = !GSON_RELOCATED ? null : FuzzyReflection.lazySupplier(CLASS_GSON_RELOCATED, fuzzy -> fuzzy
    .useMethodMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withModifiers(Modifier.PUBLIC)
    .withArgs(String.class, Class.class)
    .withName("fromJson")
    .resultAccessorAs("Missing match: " + CLASS_GSON_RELOCATED.getCanonicalName() + " -> public <T> T fromJson(String, Class<T>)"));

  /// Public API

  @NotNull
  @Contract("null -> fail; !null -> !null")
  @SuppressWarnings({ "ConstantConditions", "CastCanBeRemovedNarrowingVariableType" })
  public static ChatComponent from(Object chat) {
    Objects.requireNonNull(chat, "chat");
    if (!CLASS_ICHAT_BASE_COMPONENT.isInstance(chat))
      throw new IllegalArgumentException("Value type of the instance does not match. (Expected: " + CLASS_ICHAT_BASE_COMPONENT + ")");
    Object gson = FIELD_CHAT_SERIALIZER_GSON.get().get(null);
    if (!GSON_RELOCATED) {
      JsonElement json = ((Gson) gson).toJsonTree(chat, CLASS_ICHAT_BASE_COMPONENT);
      return ChatSerializer.fromJson(json);
    } else {
      String json = METHOD_GSON_RELOCATED_TO_JSON.get().invoke(gson, chat, CLASS_ICHAT_BASE_COMPONENT);
      return ChatSerializer.fromJson(json);
    }
  }

  @NotNull
  @Contract("null -> fail; !null -> !null")
  @SuppressWarnings({ "ConstantConditions", "CastCanBeRemovedNarrowingVariableType" })
  public static Object to(ChatComponent chat) {
    Objects.requireNonNull(chat, "chat");
    Object gson = FIELD_CHAT_SERIALIZER_GSON.get().get(null);
    if (!GSON_RELOCATED) {
      JsonElement json = ChatSerializer.toJsonTree(chat);
      return ((Gson) gson).fromJson(json, CLASS_ICHAT_BASE_COMPONENT);
    } else {
      String json = ChatSerializer.toJson(chat);
      return METHOD_GSON_RELOCATED_FROM_JSON.get().invoke(gson, json, CLASS_ICHAT_BASE_COMPONENT);
    }
  }

  // TODO: 是否公开此方法
  @NotNull
  @Contract("null, _, _ -> fail")
  public static Object newPacketChat(
    ChatComponent chat,
    @Nullable ChatType type,
    @Nullable UUID sender
  ) {
    Objects.requireNonNull(chat, "chat");
    if (type == null) type = ChatType.CHAT;

    Object origin = to(chat);
    Object messageType = CLASS_CHAT_MESSAGE_TYPE != null
      ? Enums.fromOrdinal(CLASS_CHAT_MESSAGE_TYPE, type.ordinal())
      : (byte) type.ordinal(); // Must be forced to byte type
    if (!BukkitVersion.isV116OrLater) {
      return CONSTRUCTOR_PACKET_PLAY_OUT_CHAT.get().newInstance(origin, messageType);
    } else {
      if (sender == null) sender = new UUID(0L, 0L); // zero is always display message
      return CONSTRUCTOR_PACKET_PLAY_OUT_CHAT.get().newInstance(origin, messageType, sender);
    }
  }

  @Contract("null, _, _, _ -> fail; _, null, _, _ -> fail")
  public static void sendChat(
    Player receiver,
    ChatComponent chat,
    @Nullable ChatType type,
    @Nullable UUID sender
  ) {
    Objects.requireNonNull(receiver, "receiver");
    Object packet = newPacketChat(chat, type, sender);
    PacketFactory.sendPacket(packet, receiver);
  }

  @Contract("null, _, _ -> fail; _, null, _ -> fail")
  public static void sendChat(Player receiver, ChatComponent chat, @Nullable ChatType type) {
    sendChat(receiver, chat, type, null);
  }

  @Contract("null, _ -> fail; _, null -> fail")
  public static void sendChat(Player receiver, ChatComponent chat) {
    sendChat(receiver, chat, null, null);
  }

  @Contract("null, _, _, _ -> fail; _, null, _, _ -> fail")
  public static void sendChat(Player[] receivers, ChatComponent chat, @Nullable ChatType type, @Nullable UUID sender) {
    Objects.requireNonNull(receivers, "receivers");
    Object packet = newPacketChat(chat, type, sender);
    PacketFactory.sendPacket(packet, receivers);
  }

  @Contract("null, _, _ -> fail; _, null, _ -> fail")
  public static void sendChat(Player[] receivers, ChatComponent chat, @Nullable ChatType type) {
    sendChat(receivers, chat, type, null);
  }

  @Contract("null, _ -> fail; _, null -> fail")
  public static void sendChat(Player[] receivers, ChatComponent chat) {
    sendChat(receivers, chat, null, null);
  }

  @Contract("null, _, _ -> fail")
  public static void sendChatToAll(ChatComponent chat, @Nullable ChatType type, @Nullable UUID sender) {
    Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
    if (players.length <= 0) return; // No players online, skip packet create.
    Object packet = newPacketChat(chat, type, sender);
    PacketFactory.sendPacket(packet, players);
  }

  @Contract("null, _ -> fail")
  public static void sendChatToAll(ChatComponent chat, @Nullable ChatType type) {
    sendChatToAll(chat, type, null);
  }

  @Contract("null -> fail")
  public static void sendChatToAll(ChatComponent chat) {
    sendChatToAll(chat, null, null);
  }
}
