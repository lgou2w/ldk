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

import com.lgou2w.ldk.bukkit.packet.PacketFactory;
import com.lgou2w.ldk.bukkit.version.BukkitVersion;
import com.lgou2w.ldk.reflect.ConstructorAccessor;
import com.lgou2w.ldk.reflect.FieldAccessor;
import com.lgou2w.ldk.reflect.FuzzyReflection;
import com.lgou2w.ldk.reflect.MethodAccessor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getCraftBukkitClass;
import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getCraftBukkitClassOrNull;
import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getMinecraftClass;
import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getMinecraftClassOrNull;

public final class ParticleFactory {

  private ParticleFactory() {
  }

  /// NMS Classes

  @NotNull final static Class<?> CLASS_PACKET_PLAY_OUT_WORLD_PARTICLES;
  @Nullable final static Class<?> CLASS_ENUM_PARTICLE;
  @Nullable final static Class<?> CLASS_PARTICLE;
  @Nullable final static Class<?> CLASS_PARTICLES;
  @Nullable final static Class<?> CLASS_PARTICLE_TYPE;
  @Nullable final static Class<?> CLASS_PARTICLE_PARAM;
  @Nullable final static Class<?> CLASS_PARTICLE_PARAM_ITEM;
  @Nullable final static Class<?> CLASS_PARTICLE_PARAM_BLOCK;
  @Nullable final static Class<?> CLASS_PARTICLE_PARAM_REDSTONE;

  @Nullable final static Class<?> CLASS_IBLOCK_DATA;
  @Nullable final static Class<?> CLASS_CRAFT_BLOCK_DATA;
  @Nullable final static Class<?> CLASS_CRAFT_MAGIC_NUMBERS;

  @NotNull final static Class<?> CLASS_ITEM_STACK;
  @NotNull final static Class<?> CLASS_CRAFT_ITEM_STACK;
  @NotNull final static Class<?> CLASS_MINECRAFT_KEY;

  static {
    try {
      CLASS_PACKET_PLAY_OUT_WORLD_PARTICLES = getMinecraftClass("PacketPlayOutWorldParticles");
      CLASS_PARTICLE = getMinecraftClassOrNull("Particle");
      CLASS_PARTICLES = getMinecraftClassOrNull("Particles");
      CLASS_PARTICLE_TYPE = getMinecraftClassOrNull("ParticleType");
      CLASS_PARTICLE_PARAM = getMinecraftClassOrNull("ParticleParam");
      CLASS_PARTICLE_PARAM_ITEM = getMinecraftClassOrNull("ParticleParamItem");
      CLASS_PARTICLE_PARAM_BLOCK = getMinecraftClassOrNull("ParticleParamBlock");
      CLASS_PARTICLE_PARAM_REDSTONE = getMinecraftClassOrNull("ParticleParamRedstone");
      CLASS_IBLOCK_DATA = getMinecraftClassOrNull("IBlockData");
      CLASS_CRAFT_BLOCK_DATA = getCraftBukkitClassOrNull("block.data.CraftBlockData");
      CLASS_CRAFT_MAGIC_NUMBERS = getCraftBukkitClassOrNull("util.CraftMagicNumbers");
      CLASS_ITEM_STACK = getMinecraftClass("ItemStack");
      CLASS_CRAFT_ITEM_STACK = getCraftBukkitClass("inventory.CraftItemStack");
      CLASS_MINECRAFT_KEY = getMinecraftClass("MinecraftKey");

      Class<?> classEnumParticle = null;
      try {
        classEnumParticle = getMinecraftClassOrNull("EnumParticle");
      } catch (Exception e) {
        try {
          classEnumParticle = getMinecraftClassOrNull("PacketPlayOutWorldParticles$EnumParticle");
        } catch (Exception ignore) {
        }
      } finally {
        CLASS_ENUM_PARTICLE = classEnumParticle;
      }
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Error in initializing ParticleFactory internal static block:", e);
    } catch (NullPointerException e) {
      throw new RuntimeException(e);
    }
  }

  /// NMS Accessors

  // After 1.13 and before 1.15 -> constructor(NMS.ParticleParam, boolean, float, float, float, float, float, float, float, int)
  @SuppressWarnings("ConstantConditions")
  @Nullable final static Supplier<@NotNull ConstructorAccessor<Object>> CONSTRUCTOR_PACKET_FRESH
    = !BukkitVersion.isV113OrLater || BukkitVersion.isV115OrLater ? null
    : FuzzyReflection.lazySupplier(CLASS_PACKET_PLAY_OUT_WORLD_PARTICLES, true, fuzzy -> fuzzy
    .useConstructorMatcher()
    .withArgs(
      CLASS_PARTICLE_PARAM, // NMS.ParticleParam
      boolean.class, // longDistance
      float.class, // x
      float.class, // y
      float.class, // z
      float.class, // offsetX
      float.class, // offsetY
      float.class, // offsetZ
      float.class, // speed
      int.class // count
    )
    .resultAccessor());

  // After 1.15 -> constructor(NMS.ParticleParam, boolean, double, double, double, float, float, float, float, int)
  @SuppressWarnings("ConstantConditions")
  @Nullable final static Supplier<@NotNull ConstructorAccessor<Object>> CONSTRUCTOR_PACKET_FRESH_V115
    = !BukkitVersion.isV115OrLater ? null
    : FuzzyReflection.lazySupplier(CLASS_PACKET_PLAY_OUT_WORLD_PARTICLES, true, fuzzy -> fuzzy
    .useConstructorMatcher()
    .withArgs(
      CLASS_PARTICLE_PARAM, // NMS.ParticleParam
      boolean.class, // longDistance
      double.class, // x
      double.class, // y
      double.class, // z
      float.class, // offsetX
      float.class, // offsetY
      float.class, // offsetZ
      float.class, // speed
      int.class // count
    )
    .resultAccessor());

  // Before 1.13 -> constructor(NMS.EnumParticle, boolean, float, float, float, float, float, float, float, int, int[])
  @SuppressWarnings("ConstantConditions")
  @Nullable final static Supplier<@NotNull ConstructorAccessor<Object>> CONSTRUCTOR_PACKET_LEGACY
    = BukkitVersion.isV113OrLater ? null
    : FuzzyReflection.lazySupplier(CLASS_PACKET_PLAY_OUT_WORLD_PARTICLES, true, fuzzy -> fuzzy
    .useConstructorMatcher()
    .withArgs(
      CLASS_ENUM_PARTICLE, // NMS.EnumParticle
      boolean.class, // longDistance
      float.class, // x
      float.class, // y
      float.class, // z
      float.class, // offsetX
      float.class, // offsetY
      float.class, // offsetZ
      float.class, // speed
      int.class, // count
      int[].class // data
    )
    .resultAccessor());

  // NMS.ParticleParamItem -> constructor(NMS.Particle, NMS.ItemStack)
  @SuppressWarnings("ConstantConditions")
  @Nullable final static Supplier<@NotNull ConstructorAccessor<Object>> CONSTRUCTOR_PARAM_ITEM
    = !BukkitVersion.isV113OrLater ? null
    : FuzzyReflection.lazySupplier(CLASS_PARTICLE_PARAM_ITEM, true, fuzzy -> fuzzy
    .useConstructorMatcher()
    .withArgs(CLASS_PARTICLE, CLASS_ITEM_STACK)
    .resultAccessor("Missing match: NMS.ParticleParamItem -> constructor(NMS.Particle, NMS.ItemStack)"));

  // NMS.ParticleParamBlock -> constructor(NMS.Particle, NMS.IBlockData)
  @SuppressWarnings("ConstantConditions")
  @Nullable final static Supplier<@NotNull ConstructorAccessor<Object>> CONSTRUCTOR_PARAM_BLOCK
    = !BukkitVersion.isV113OrLater ? null
    : FuzzyReflection.lazySupplier(CLASS_PARTICLE_PARAM_BLOCK, true, fuzzy -> fuzzy
    .useConstructorMatcher()
    .withArgs(CLASS_PARTICLE, CLASS_IBLOCK_DATA)
    .resultAccessor("Missing match: NMS.ParticleParamBlock -> constructor(NMS.Particle, NMS.IBlockData)"));

  // NMS.ParticleParamRedstone -> constructor(float, float, float, float)
  @Nullable final static Supplier<@NotNull ConstructorAccessor<Object>> CONSTRUCTOR_PARAM_REDSTONE
    = !BukkitVersion.isV113OrLater ? null
    : FuzzyReflection.lazySupplier(CLASS_PARTICLE_PARAM_REDSTONE, true, fuzzy -> fuzzy
    .useConstructorMatcher()
    .withArgs(float.class, float.class, float.class, float.class)
    .resultAccessor("Missing match: NMS.ParticleParamRedstone -> constructor(float, float, float, float)"));

  // Before 1.13 -> NMS.EnumParticle -> public static NMS.EnumParticle getById(int)
  @SuppressWarnings("ConstantConditions")
  @Nullable final static Supplier<@NotNull MethodAccessor<Object, Object>> METHOD_ENUM_PARTICLE_GET_BY_ID
    = BukkitVersion.isV113OrLater ? null
    : FuzzyReflection.lazySupplier(CLASS_ENUM_PARTICLE, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withModifiers(Modifier.STATIC)
    .withType(CLASS_ENUM_PARTICLE)
    .withArgs(int.class)
    .resultAccessor("Missing match: NMS.EnumParticle -> Method: public static NMS.EnumParticle getById(int)"));

  // After 1.13 -> OBC.util.CraftMagicNumbers -> public static NMS.IBlockData getBlock(OB.Material, byte)
  @SuppressWarnings("ConstantConditions")
  @Nullable final static Supplier<MethodAccessor<Object, Object>> METHOD_CRAFT_MAGIC_NUMBERS_GET_BLOCK
    = !BukkitVersion.isV113OrLater ? null
    : FuzzyReflection.lazySupplier(CLASS_CRAFT_MAGIC_NUMBERS, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withModifiers(Modifier.PUBLIC, Modifier.STATIC)
    .withType(CLASS_IBLOCK_DATA)
    .withArgs(Material.class, byte.class)
    .resultAccessor("Missing match: OBC.util.CraftMagicNumbers -> Method: public static NMS.IBlockData getBlock(OB.Material, byte)"));

  // After 1.13 -> OBC.block.data.CraftBlockData -> private NMS.IBlockData state
  @SuppressWarnings("ConstantConditions")
  @Nullable final static Supplier<FieldAccessor<Object, Object>> FIELD_CRAFT_BLOCK_DATA_STATE
    = !BukkitVersion.isV113OrLater ? null
    : FuzzyReflection.lazySupplier(CLASS_CRAFT_BLOCK_DATA, true, fuzzy -> fuzzy
    .useFieldMatcher()
    .withType(CLASS_IBLOCK_DATA)
    .resultAccessor("Missing match: OBC.block.data.CraftBlockData -> Field: private NMS.IBlockData state"));

  // OBC.inventory.CraftItemStack -> public static NMS.ItemStack asNMSCopy(OB.inventory.ItemStack);
  @NotNull final static Supplier<@NotNull MethodAccessor<Object, Object>> METHOD_CRAFT_ITEM_STACK_AS_NMS_COPY
    = FuzzyReflection.lazySupplier(CLASS_CRAFT_ITEM_STACK, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withModifiers(Modifier.PUBLIC, Modifier.STATIC)
    .withType(CLASS_ITEM_STACK)
    .withArgs(ItemStack.class)
    .withName("asNMSCopy")
    .resultAccessor("Missing match: OBC.inventory.CraftItemStack -> Method: public static NMS.ItemStack asNMSCopy(OB.inventory.ItemStack)"));

  // NMS.ParticleType -> public String namespacedKey()
  @Nullable final static Supplier<MethodAccessor<Object, String>> METHOD_PARTICLE_TYPE_NAMESPACED_KEY
    = !BukkitVersion.isV113OrLater ? null
    : FuzzyReflection.lazySupplier(CLASS_PARTICLE_TYPE, fuzzy -> fuzzy
    .useMethodMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withType(String.class)
    .withArgsCount(0)
    .resultAccessorAs("Missing match: NMS.ParticleType -> Method: public String namespacedKey()"));

  /// Internal

  private final static Map<String, Object> INTERNAL_MAPPINGS = new HashMap<>();

  @Nullable
  @SuppressWarnings("ConstantConditions")
  private static Object mappingInternal(Particle particle) {
    String namespacedKey = particle.getNamespacedKey();
    Object mapping = INTERNAL_MAPPINGS.get(namespacedKey);
    if (mapping == Boolean.FALSE) return null;
    if (mapping == null) {
      List<FieldAccessor<Object, Object>> fieldParticles = FuzzyReflection.of(CLASS_PARTICLES, true)
        .useFieldMatcher()
        .withType(CLASS_PARTICLE)
        .resultAccessors();

      boolean blockOrFallingDust = false;
      for (FieldAccessor<Object, Object> field : fieldParticles) {
        Object origin = field.get(null);
        if (CLASS_PARTICLE_TYPE.isInstance(origin)) {
          String originNamespacedKey = METHOD_PARTICLE_TYPE_NAMESPACED_KEY.get().invoke(origin);
          INTERNAL_MAPPINGS.put(originNamespacedKey, origin);
          continue;
        }
        Type paramType = field.getSource().getGenericType() instanceof ParameterizedType
          ? ((ParameterizedType) field.getSource().getGenericType()).getActualTypeArguments()[0]
          : null;
        if (!(paramType instanceof Class)) continue;
        Class<?> paramTypeClass = (Class<?>) paramType;
        if (CLASS_PARTICLE_PARAM_ITEM.isAssignableFrom(paramTypeClass)) {
          INTERNAL_MAPPINGS.put(Particle.ITEM.getNamespacedKey(), origin);
        } else if (CLASS_PARTICLE_PARAM_REDSTONE.isAssignableFrom(paramTypeClass)) {
          INTERNAL_MAPPINGS.put(Particle.DUST.getNamespacedKey(), origin);
        } else if (CLASS_PARTICLE_PARAM_BLOCK.isAssignableFrom(paramTypeClass)) {
          if (!blockOrFallingDust) {
            INTERNAL_MAPPINGS.put(Particle.BLOCK.getNamespacedKey(), origin);
            blockOrFallingDust = true;
          } else {
            INTERNAL_MAPPINGS.put(Particle.FALLING_DUST.getNamespacedKey(), origin);
          }
        }
      }
      mapping = INTERNAL_MAPPINGS.get(namespacedKey);
    }
    if (mapping == null) INTERNAL_MAPPINGS.put(namespacedKey, Boolean.FALSE);
    return mapping;
  }

  @SuppressWarnings("deprecation")
  private static int switchParticleParam(Particle particle) {
    if (particle == Particle.ITEM || particle == Particle.ITEM_TAKE) return 0;
    if (particle == Particle.BLOCK || particle == Particle.BLOCK_DUST || particle == Particle.FALLING_DUST) return 1;
    if (particle == Particle.DUST) return 2;
    if (particle == Particle.NOTE) return 3;
    return -1;
  }

  @SuppressWarnings({ "ConstantConditions", "DuplicatedCode" })
  private static Object newPacketFresh(
    @NotNull Particle particle,
    double x,
    double y,
    double z,
    float offsetX,
    float offsetY,
    float offsetZ,
    float speed,
    int count,
    boolean longDistance,
    @Nullable Object data
  ) {
    Object particleType = mappingInternal(particle);
    if (particleType == null)
      throw new UnsupportedOperationException("Unsupported particle type: " + particle);

    Object particleParam;
    float overrideOffsetX = offsetX;
    switch (switchParticleParam(particle)) {
      case 0: // Item
        ItemStack stack;
        if (data == null) stack = new ItemStack(Material.AIR);
        else if (data instanceof ItemStack) stack = (ItemStack) data;
        else if (data instanceof Material) stack = new ItemStack((Material) data);
        else if (data instanceof ParticleData.Item) stack = new ItemStack(((ParticleData.Item) data).getType());
        else throw new IllegalArgumentException("Particle 'ITEM' data can only be Material | ItemStack | ParticleData.Item");
        Object origin = METHOD_CRAFT_ITEM_STACK_AS_NMS_COPY.get().invoke(null, stack);
        particleParam = CONSTRUCTOR_PARAM_ITEM.get().newInstance(particleType, origin);
        break;
      case 1: // Block
        Object blockData;
        if (data == null) blockData = METHOD_CRAFT_MAGIC_NUMBERS_GET_BLOCK.get().invoke(null, Material.AIR, (byte) 0);
        else if (data instanceof Block) blockData = FIELD_CRAFT_BLOCK_DATA_STATE.get().get(data);
        else if (data instanceof Material) blockData = METHOD_CRAFT_MAGIC_NUMBERS_GET_BLOCK.get().invoke(null, data, (byte) 0);
        else if (data instanceof ParticleData.Block) {
          ParticleData.Block block = (ParticleData.Block) data;
          blockData = METHOD_CRAFT_MAGIC_NUMBERS_GET_BLOCK.get().invoke(null, block.getType(), (byte) block.getData());
        }
        else throw new IllegalArgumentException("Particle 'BLOCK', 'BLOCK_DUST', 'FALLING_DUST' data can only be Material | Block | ParticleData.Block");
        particleParam = CONSTRUCTOR_PARAM_BLOCK.get().newInstance(particleType, blockData);
        break;
      case 2: // Redstone
        Color color;
        float size = 1f;
        if (data == null) color = Color.WHITE;
        else if (data instanceof Color) color = (Color) data;
        else if (data instanceof ParticleData.Dust) {
          ParticleData.Dust dust = (ParticleData.Dust) data;
          color = dust.getColor();
          size = dust.getSize();
        }
        else throw new IllegalArgumentException("Particle 'DUST' data can only be Color | ParticleData.Dust");
        particleParam = CONSTRUCTOR_PARAM_REDSTONE.get().newInstance(particleType,
          color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, size);
        break;
      case 3: // Note
        if (data instanceof Integer || data instanceof ParticleData.Note) {
          int value = data instanceof Integer ? (int) data : ((ParticleData.Note) data).getValue();
          overrideOffsetX = value >= 0 && value <= 24 ? value / 24f : offsetX;
        }
        particleParam = particleType;
        break;
      default:
        particleParam = particleType;
        break;
    }
    return BukkitVersion.isV115OrLater
      ? CONSTRUCTOR_PACKET_FRESH_V115.get().newInstance(particleParam, longDistance, x, y, z,
          overrideOffsetX, offsetY, offsetZ, speed, count)
      : CONSTRUCTOR_PACKET_FRESH.get().newInstance(particleParam, longDistance, (float) x, (float) y, (float) z,
          overrideOffsetX, offsetY, offsetZ, speed, count);
  }

  @SuppressWarnings({ "ConstantConditions", "deprecation", "DuplicatedCode" })
  private static Object newPacketLegacy(
    @NotNull Particle particle,
    double x,
    double y,
    double z,
    float offsetX,
    float offsetY,
    float offsetZ,
    float speed,
    int count,
    boolean longDistance,
    @Nullable Object data
  ) {
    Object particleType = particle.getId() < 0 ? null : METHOD_ENUM_PARTICLE_GET_BY_ID.get().invoke(null, particle.getId());
    if (particleType == null)
      throw new UnsupportedOperationException("Unsupported particle type: " + particle);

    int[] particleData;
    float overrideOffsetX = offsetX,
      overrideOffsetY = offsetY,
      overrideOffsetZ = offsetZ;
    switch (switchParticleParam(particle)) {
      case 0: // Item
        Material itemType;
        int itemData = 0;
        if (data == null) itemType = Material.AIR;
        else if (data instanceof ItemStack) {
          ItemStack stack = (ItemStack) data;
          itemType = stack.getType();
          itemData = stack.getData().getData();
        }
        else if (data instanceof Material) itemType = (Material) data;
        else if (data instanceof ParticleData.Item) {
          ParticleData.Item item = (ParticleData.Item) data;
          itemType = item.getType();
          itemData = item.getData();
        }
        else throw new IllegalArgumentException("Particle 'ITEM' data can only be Material | ItemStack | ParticleData.Item");
        particleData = new int[] { itemType.getId(), itemData };
        break;
      case 1: // Block
        Material blockType;
        int blockData = 0;
        if (data == null) blockType = Material.AIR;
        else if (data instanceof Block) {
          Block block = (Block) data;
          blockType = block.getType();
          blockData = block.getData();
        }
        else if (data instanceof Material) blockType = (Material) data;
        else if (data instanceof ParticleData.Block) {
          ParticleData.Block block = (ParticleData.Block) data;
          blockType = block.getType();
          blockData = block.getData();
        }
        else throw new IllegalArgumentException("Particle 'BLOCK', 'BLOCK_DUST', 'FALLING_DUST' data can only be Material | Block | ParticleData.Block");
        particleData = new int[] { blockType.getId() | (blockData << 12) };
        break;
      case 2: // Redstone
        Color color;
        if (data == null) color = Color.WHITE;
        else if (data instanceof Color) color = (Color) data;
        else if (data instanceof ParticleData.Dust) color = ((ParticleData.Dust) data).getColor();
        else throw new IllegalArgumentException("Particle 'DUST' data can only be Color | ParticleData.Dust");
        particleData = new int[0];
        overrideOffsetX = color.getRed() / 255f;
        overrideOffsetY = color.getGreen() / 255f;
        overrideOffsetZ = color.getBlue() / 255f;
        break;
      case 3: // Note
        if (data instanceof Integer || data instanceof ParticleData.Note) {
          int value = data instanceof Integer ? (int) data : ((ParticleData.Note) data).getValue();
          overrideOffsetX = value >= 0 && value <= 24 ? value / 24f : offsetX;
        }
        particleData = new int[0];
        break;
      default:
        particleData = new int[0];
        break;
    }
    return CONSTRUCTOR_PACKET_LEGACY.get().newInstance(particleType, longDistance,
      (float) x, (float) y, (float) z, overrideOffsetX, overrideOffsetY, overrideOffsetZ,
      speed, count, particleData);
  }

  /// Public API

  public static Object newPacket(
    @NotNull Particle particle,
    double x,
    double y,
    double z,
    float offsetX,
    float offsetY,
    float offsetZ,
    float speed,
    int count,
    boolean longDistance,
    @Nullable Object data
  ) {
    Objects.requireNonNull(particle, "particle");
    return BukkitVersion.isV113OrLater
      ? newPacketFresh(particle, x, y, z, offsetX, offsetY, offsetZ, speed, count, longDistance, data)
      : newPacketLegacy(particle, x, y, z, offsetX, offsetY, offsetZ, speed, count, longDistance, data);
  }

  public static void sendParticle(
    @NotNull Particle particle,
    @NotNull Location center,
    double range,
    float offsetX,
    float offsetY,
    float offsetZ,
    float speed,
    int count,
    @Nullable Object data
  ) {
    Objects.requireNonNull(particle, "particle");
    Objects.requireNonNull(center, "center");
    Object packet = newPacket(particle, center.getX(), center.getY(), center.getZ(),
      offsetX, offsetY, offsetZ, speed, count, range > 256d, data);
    PacketFactory.sendPacketToNearby(packet, center, range);
  }

  public static void sendParticle(
    @NotNull Particle particle,
    @Nullable Player sender,
    @NotNull Location center,
    float offsetX,
    float offsetY,
    float offsetZ,
    float speed,
    int count,
    @Nullable Object data
  ) {
    World world = Objects.requireNonNull(center, "center").getWorld();
    List<Player> receivers = Objects.requireNonNull(world, "world").getPlayers();
    sendParticle(particle, receivers, (receiver) -> sender == null || sender.canSee(receiver),
      center, offsetX, offsetY, offsetZ, speed, count, data);
  }

  public static void sendParticle(
    @NotNull Particle particle,
    @NotNull List<Player> receivers,
    @NotNull Location center,
    float offsetX,
    float offsetY,
    float offsetZ,
    float speed,
    int count,
    @Nullable Object data
  ) {
    World world = Objects.requireNonNull(center.getWorld(), "center.world");
    sendParticle(particle, receivers, (receiver) -> receiver.getWorld().equals(world),
      center, offsetX, offsetY, offsetZ, speed, count, data);
  }

  private static void sendParticle(
    @NotNull Particle particle,
    @NotNull List<Player> receivers,
    @NotNull Predicate<Player> filter,
    @NotNull Location center,
    float offsetX,
    float offsetY,
    float offsetZ,
    float speed,
    int count,
    @Nullable Object data
  ) {
    Objects.requireNonNull(particle, "particle");
    Objects.requireNonNull(receivers, "receivers");
    Objects.requireNonNull(filter, "filter");
    Objects.requireNonNull(center, "center");

    boolean longDistance = false;
    List<Player> filteredReceivers = new ArrayList<>();
    for (Player receiver : receivers) {
      if (!filter.test(receiver)) continue;
      if (!longDistance && receiver.getLocation().distanceSquared(center) >= 256d)
        longDistance = true;
      filteredReceivers.add(receiver);
    }
    if (filteredReceivers.isEmpty())
      return; // skip empty receivers

    Object packet = newPacket(particle, center.getX(), center.getY(), center.getZ(),
      offsetX, offsetY, offsetZ, speed, count, longDistance, data);
    PacketFactory.sendPacket(packet, filteredReceivers.toArray(new Player[0]));
  }
}
