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

package com.lgou2w.ldk.bukkit.packet;

import com.lgou2w.ldk.reflect.FieldAccessor;
import com.lgou2w.ldk.reflect.FuzzyReflection;
import com.lgou2w.ldk.reflect.MethodAccessor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getCraftBukkitClass;
import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getMinecraftClass;

public final class PacketFactory {

  private PacketFactory() { }

  /// NMS Classes

  @NotNull public final static Class<?> CLASS_PACKET;
  @NotNull public final static Class<?> CLASS_PACKET_LISTENER;
  @NotNull public final static Class<?> CLASS_NETWORK_MANAGER;
  @NotNull public final static Class<?> CLASS_PLAYER_CONNECTION;
  @NotNull public final static Class<?> CLASS_ENTITY;
  @NotNull public final static Class<?> CLASS_ENTITY_PLAYER;
  @NotNull public final static Class<?> CLASS_CRAFT_ENTITY;

  static {
    try {
      CLASS_PACKET = getMinecraftClass("Packet");
      CLASS_PACKET_LISTENER = getMinecraftClass("PacketListener");
      CLASS_NETWORK_MANAGER = getMinecraftClass("NetworkManager");
      CLASS_PLAYER_CONNECTION = getMinecraftClass("PlayerConnection");
      CLASS_ENTITY = getMinecraftClass("Entity");
      CLASS_ENTITY_PLAYER = getMinecraftClass("EntityPlayer");
      CLASS_CRAFT_ENTITY = getCraftBukkitClass("entity.CraftEntity");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Error in initializing PacketFactory internal static block:", e);
    }
  }

  /// NMS Accessors

  // OBC.entity.CraftEntity -> public NMS.Entity getHandle();
  final static Supplier<@NotNull MethodAccessor<Object, Object>> METHOD_CRAFT_ENTITY_GET_HANDLE
    = FuzzyReflection.lazySupplier(CLASS_CRAFT_ENTITY, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withModifiers(Modifier.PUBLIC)
    .withType(CLASS_ENTITY)
    .resultAccessor("Missing match: OBC.entity.CraftEntity -> Method: public NMS.Entity getHandle()"));

  // NMS.EntityPlayer -> public NMS.PlayerConnection playerConnection;
  final static Supplier<@NotNull FieldAccessor<Object, Object>> FIELD_ENTITY_PLAYER_CONNECTION
    = FuzzyReflection.lazySupplier(CLASS_ENTITY_PLAYER, true, fuzzy -> fuzzy
    .useFieldMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withType(CLASS_PLAYER_CONNECTION)
    .resultAccessor("Missing match: NMS.EntityPlayer -> Field: public NMS.PlayerConnection playerConnection"));

  // NMS.PlayerConnection -> public NMS.NetworkManager networkManager;
  final static Supplier<@NotNull FieldAccessor<Object, Object>> FIELD_PLAYER_CONNECTION_MANAGER
    = FuzzyReflection.lazySupplier(CLASS_PLAYER_CONNECTION, true, fuzzy -> fuzzy
    .useFieldMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withType(CLASS_NETWORK_MANAGER)
    .resultAccessor("Missing match: NMS.PlayerConnection -> Field: public NMS.NetworkManager networkManager"));

  // NMS.PlayerConnection -> public void sendPacket(NMS.Packet);
  final static Supplier<@NotNull MethodAccessor<Object, Object>> METHOD_PLAYER_CONNECTION_SEND_PACKET
    = FuzzyReflection.lazySupplier(CLASS_PLAYER_CONNECTION, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withArgs(CLASS_PACKET)
    .resultAccessor("Missing match: NMS.PlayerConnection -> Method: public void sendPacket(NMS.Packet)"));

  // NMS.Packet -> public void process(NMS.PacketListener);
  final static Supplier<@NotNull MethodAccessor<Object, Object>> METHOD_PACKET_PROCESS
    = FuzzyReflection.lazySupplier(CLASS_PACKET, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withArgs(CLASS_PACKET_LISTENER)
    .resultAccessor("Missing match: NMS.Packet -> Method: public void process(NMS.PacketListener)"));

  @NotNull
  @SuppressWarnings("ConstantConditions")
  static Object getPlayerHandle(@NotNull Player player) {
    return METHOD_CRAFT_ENTITY_GET_HANDLE.get().invoke(player);
  }

  static void validatePacket(Object packet) {
    if (!CLASS_PACKET.isInstance(packet))
      throw new IllegalArgumentException("Value type of the instance does not match. (Expected: " + CLASS_PACKET + ")");
  }

  static void sendPacket0(Object packet, Player receiver) {
    Object handle = getPlayerHandle(receiver);
    Object connection = FIELD_ENTITY_PLAYER_CONNECTION.get().get(handle);
    METHOD_PLAYER_CONNECTION_SEND_PACKET.get().invoke(connection, packet);
  }

  static void processPacket0(Object packet, Player sender) {
    Object handle = getPlayerHandle(sender);
    Object connection = FIELD_ENTITY_PLAYER_CONNECTION.get().get(handle);
    METHOD_PACKET_PROCESS.get().invoke(packet, connection);
  }

  @Contract("null, _ -> fail")
  static Player[] nearbyPlayers(Location center, double range) {
    if (center == null) throw new NullPointerException("center");
    World world = center.getWorld();
    if (world == null)
      throw new IllegalArgumentException("The world at this location is a null.");

    List<Player> result = new ArrayList<>();
    double squared = range < 1.0 ? 1.0 : range * range;
    for (Player player : world.getPlayers()) {
      if (player.getLocation().distanceSquared(center) <= squared)
        result.add(player);
    }
    return result.toArray(new Player[0]);
  }

  /// Public API

  @Contract("null, _ -> fail; _, null -> fail")
  public static void sendPacket(Object packet, Player receiver) {
    validatePacket(packet);
    if (receiver == null) throw new NullPointerException("receiver");
    sendPacket0(packet, receiver);
  }

  @Contract("null, _ -> fail; _, null -> fail")
  public static void sendPacket(Object packet, Player[] receivers) {
    validatePacket(packet);
    if (receivers == null) throw new NullPointerException("receivers");
    for (Player receiver : receivers)
      sendPacket0(packet, receiver);
  }

  @Contract("null -> fail")
  public static void sendPacketToAll(Object packet) {
    validatePacket(packet);
    sendPacket(packet, Bukkit.getOnlinePlayers().toArray(new Player[0]));
  }

  @Contract("null, _, _ -> fail; _, null, _ -> fail")
  public static void sendPacketToNearby(Object packet, Location center, double range) {
    validatePacket(packet);
    Player[] players = nearbyPlayers(center, range);
    sendPacket(packet, players);
  }

  @Contract("null, _ -> fail; _, null -> fail")
  public static void processPacket(Object packet, Player sender) {
    validatePacket(packet);
    if (sender == null) throw new NullPointerException("sender");
    processPacket0(packet, sender);
  }

  @Contract("null, _ -> fail; _, null -> fail")
  public static void processPacket(Object packet, Player[] senders) {
    validatePacket(packet);
    if (senders == null) throw new NullPointerException("senders");
    for (Player sender : senders)
      processPacket0(packet, sender);
  }

  @Contract("null -> fail")
  public static void processPacketToAll(Object packet) {
    validatePacket(packet);
    processPacket(packet, Bukkit.getOnlinePlayers().toArray(new Player[0]));
  }

  @Contract("null, _, _ -> fail; _, null, _ -> fail")
  public static void processPacketToNearby(Object packet, Location center, double range) {
    validatePacket(packet);
    Player[] players = nearbyPlayers(center, range);
    processPacket(packet, players);
  }
}
