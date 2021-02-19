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

import com.lgou2w.ldk.bukkit.nbt.NBTFactory;
import com.lgou2w.ldk.nbt.CompoundTag;
import com.lgou2w.ldk.nbt.TagType;
import com.lgou2w.ldk.reflect.ConstructorAccessor;
import com.lgou2w.ldk.reflect.FieldAccessor;
import com.lgou2w.ldk.reflect.FuzzyReflection;
import com.lgou2w.ldk.reflect.MethodAccessor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getCraftBukkitClass;
import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getCraftBukkitClassOrNull;
import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getMinecraftClass;
import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getMinecraftClassOrNull;

public final class ItemFactory {

  private ItemFactory() { }

  /// NMS Classes

  @NotNull public final static Class<?> CLASS_ITEM;
  @NotNull public final static Class<?> CLASS_ITEM_STACK;
  @NotNull public final static Class<?> CLASS_CRAFT_ITEM_STACK;
  @Nullable public final static Class<?> CLASS_MINECRAFT_KEY; // since Minecraft 1.13
  @Nullable public final static Class<?> CLASS_CRAFT_MAGIC_NUMBERS; // since Minecraft 1.13

  static {
    try {
      CLASS_ITEM = getMinecraftClass("Item");
      CLASS_ITEM_STACK = getMinecraftClass("ItemStack");
      CLASS_CRAFT_ITEM_STACK = getCraftBukkitClass("inventory.CraftItemStack");
      CLASS_MINECRAFT_KEY = getMinecraftClassOrNull("MinecraftKey");
      CLASS_CRAFT_MAGIC_NUMBERS = getCraftBukkitClassOrNull("util.CraftMagicNumbers");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Error in initializing internal static block: ", e);
    }
  }

  /// NMS Accessors

  // NMS.NBTTagCompound -> Map<String, NMS.NBTBase> value;
  final static Supplier<@NotNull FieldAccessor<Object, Object>> FIELD_NBT_TAG_COMPOUND_VALUE
    = FuzzyReflection.lazySupplier(NBTFactory.CLASS_NBT_TAG_COMPOUND, true, fuzzy -> fuzzy
    .useFieldMatcher()
    .withType(Map.class)
    .resultAccessor("Missing match: NMS.NBTTagCompound -> Field: Map<String, NMS.NBTBase> value"));

  // NMS.ItemStack -> private NMS.NBTTagCompound tag;
  // Not necessarily private, but must not be static.
  final static Supplier<@NotNull FieldAccessor<Object, Object>> FIELD_ITEM_STACK_TAG
    = FuzzyReflection.lazySupplier(CLASS_ITEM_STACK, true, fuzzy -> fuzzy
    .useFieldMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withType(NBTFactory.CLASS_NBT_TAG_COMPOUND)
    .resultAccessor("Missing match: NMS.ItemStack -> Field: NMS.NBTTagCompound tag"));

  // OBC.inventory.CraftItemStack -> private NMS.ItemStack handle;
  // Not necessarily private, but must not be static.
  final static Supplier<@NotNull FieldAccessor<Object, Object>> FIELD_CRAFT_ITEM_STACK_HANDLE
    = FuzzyReflection.lazySupplier(CLASS_CRAFT_ITEM_STACK, true, fuzzy -> fuzzy
    .useFieldMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withType(CLASS_ITEM_STACK)
    .resultAccessor("Missing match: OBC.inventory.CraftItemStack -> Field: NMS.ItemStack handle"));

  // OBC.inventory.CraftItemStack -> public static NMS.ItemStack asNMSCopy(OB.inventory.ItemStack);
  final static Supplier<@NotNull MethodAccessor<Object, Object>> METHOD_CRAFT_ITEM_STACK_AS_NMS_COPY
    = FuzzyReflection.lazySupplier(CLASS_CRAFT_ITEM_STACK, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withModifiers(Modifier.PUBLIC, Modifier.STATIC)
    .withType(CLASS_ITEM_STACK)
    .withArgs(ItemStack.class)
    .withName("asNMSCopy")
    .resultAccessor("Missing match: OBC.inventory.CraftItemStack -> Method: public static NMS.ItemStack asNMSCopy(OB.inventory.ItemStack)"));

  // OBC.inventory.CraftItemStack -> public static OBC.inventory.CraftItemStack asCraftMirror(NMS.ItemStack);
  final static Supplier<@NotNull MethodAccessor<Object, Object>> METHOD_CRAFT_ITEM_STACK_AS_CRAFT_MIRROR
    = FuzzyReflection.lazySupplier(CLASS_CRAFT_ITEM_STACK, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withModifiers(Modifier.PUBLIC, Modifier.STATIC)
    .withType(CLASS_CRAFT_ITEM_STACK)
    .withArgs(CLASS_ITEM_STACK)
    .withName("asCraftMirror")
    .resultAccessor("Missing match: OBC.inventory.CraftItemStack -> Method: public static OB.inventory.ItemStack asBukkitCopy(NMS.ItemStack)"));

  // OBC.inventory.CraftItemStack -> public static OB.inventory.ItemStack asBukkitCopy(NMS.ItemStack);
  final static Supplier<@NotNull MethodAccessor<Object, ItemStack>> METHOD_CRAFT_ITEM_STACK_AS_BUKKIT_COPY
    = FuzzyReflection.lazySupplier(CLASS_CRAFT_ITEM_STACK, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withModifiers(Modifier.PUBLIC, Modifier.STATIC)
    .withType(ItemStack.class)
    .withArgs(CLASS_ITEM_STACK)
    .withName("asBukkitCopy")
    .resultAccessorAs("Missing match: OBC.inventory.CraftItemStack -> Method: public static OB.inventory.ItemStack asBukkitCopy(NMS.ItemStack)"));

  // This method does not necessarily exist, it should be used with the constructor.
  // NMS.ItemStack -> public static NMS.ItemStack create(NMS.NBTTagCompound);
  final static Supplier<@Nullable MethodAccessor<Object, Object>> METHOD_ITEM_STACK_CREATE
    = FuzzyReflection.lazySupplier(CLASS_ITEM_STACK, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withModifiers(Modifier.PUBLIC, Modifier.STATIC)
    .withType(CLASS_ITEM_STACK)
    .withArgs(NBTFactory.CLASS_NBT_TAG_COMPOUND)
    .resultAccessorOrNull());

  // NMS.ItemStack -> public NMS.ItemStack(NMS.NBTTagCompound);
  final static Supplier<@Nullable ConstructorAccessor<Object>> CONSTRUCTOR_ITEM_STACK
    = FuzzyReflection.lazySupplier(CLASS_ITEM_STACK, true, fuzzy -> fuzzy
    .useConstructorMatcher()
    .withArgs(NBTFactory.CLASS_NBT_TAG_COMPOUND)
    .resultAccessorOrNull());

  // NMS.ItemStack -> public NMS.NBTTagCompound save(NMS.NBTTagCompound);
  final static Supplier<@NotNull MethodAccessor<Object, Object>> METHOD_ITEM_STACK_SAVE
    = FuzzyReflection.lazySupplier(CLASS_ITEM_STACK, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withModifiers(Modifier.PUBLIC)
    .withType(NBTFactory.CLASS_NBT_TAG_COMPOUND)
    .withArgs(NBTFactory.CLASS_NBT_TAG_COMPOUND)
    .resultAccessor("Missing match: NMS.ItemStack -> Method: public NMS.NBTTagCompound save(NMS.NBTTagCompound)"));

  // OBC.util.CraftMagicNumbers -> public static NMS.MinecraftKey key(OB.Material);
  @Nullable final static Supplier<@Nullable MethodAccessor<Object, Object>> METHOD_CRAFT_MAGIC_NUMBERS_KEY
    = CLASS_CRAFT_MAGIC_NUMBERS == null || CLASS_MINECRAFT_KEY == null ? null
    : FuzzyReflection.lazySupplier(CLASS_CRAFT_MAGIC_NUMBERS, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withModifiers(Modifier.PUBLIC, Modifier.STATIC)
    .withType(CLASS_MINECRAFT_KEY)
    .withArgs(Material.class)
    .resultAccessorOrNull());

  @Nullable
  @Contract("null -> null; !null -> !null")
  public static Object asNMSCopy(ItemStack stack) {
    if (stack == null) return null;
    return METHOD_CRAFT_ITEM_STACK_AS_NMS_COPY.get().invoke(null, stack);
  }

  @Nullable
  @Contract("null -> fail; !null -> !null")
  public static Object asCraftMirror(Object origin) throws IllegalArgumentException {
    if (!CLASS_ITEM_STACK.isInstance(origin))
      throw new IllegalArgumentException("Object is not an instance of NMS.ItemStack. Current: " + origin);
    return METHOD_CRAFT_ITEM_STACK_AS_CRAFT_MIRROR.get().invoke(null, origin);
  }

  @Nullable
  @Contract("null -> fail; !null -> !null")
  public static ItemStack asBukkitCopy(Object origin) throws IllegalArgumentException {
    if (!CLASS_ITEM_STACK.isInstance(origin))
      throw new IllegalArgumentException("Object is not an instance of NMS.ItemStack. Current: " + origin);
    return METHOD_CRAFT_ITEM_STACK_AS_BUKKIT_COPY.get().invoke(null, origin);
  }

  @NotNull
  @Contract("null -> fail; !null -> !null")
  @SuppressWarnings("ConstantConditions")
  public static String materialKey(Material material) {
    if (material == null) throw new NullPointerException("material");
    if (METHOD_CRAFT_MAGIC_NUMBERS_KEY != null &&
      METHOD_CRAFT_MAGIC_NUMBERS_KEY.get() != null) {
      Object minecraftKey = METHOD_CRAFT_MAGIC_NUMBERS_KEY.get().invoke(null, material);
      return minecraftKey.toString(); // such as: minecraft:diamond
    } else {
      String name = material.name().toLowerCase(Locale.ENGLISH);
      return !name.startsWith("minecraft:")
        ? "minecraft:" + name
        : name;
    }
  }

  @Nullable
  @Contract("null -> null")
  public static CompoundTag readStack(@Nullable ItemStack stack) {
    if (stack == null) return null;
    if (CLASS_CRAFT_ITEM_STACK.isInstance(stack)) {
      Object handle = FIELD_CRAFT_ITEM_STACK_HANDLE.get().get(stack);
      if (handle == null) {
        // Why the handle is null? Because the item material type is invalid, such as WALL_BANNER.
        // Return an null tag or throw an exception, need confirmation.
        // TODO: ItemFactory.readStack -> handle null problem
        //throw new UnsupportedOperationException("Illegal item stack material type: " + stack.getType());
        return null;
      }
      Object internal = NBTFactory.newInternal(TagType.COMPOUND, null);
      METHOD_ITEM_STACK_SAVE.get().invoke(handle, internal);
      return (CompoundTag) NBTFactory.from(internal);
    } else {
      Object origin = asNMSCopy(stack);
      ItemStack obcStack = (ItemStack) asCraftMirror(origin);
      obcStack.setItemMeta(stack.getItemMeta()); // Copy item meta
      return readStack(obcStack);
    }
  }

  @Nullable
  @Contract("null -> null")
  public static CompoundTag readStackTag(ItemStack stack) {
    if (stack == null) return null;
    if (CLASS_CRAFT_ITEM_STACK.isInstance(stack)) {
      Object handle = FIELD_CRAFT_ITEM_STACK_HANDLE.get().get(stack);
      if (handle == null) {
        // Why the handle is null? Because the item material type is invalid, such as WALL_BANNER.
        // Return an null tag or throw an exception, need confirmation.
        // TODO: ItemFactory.readTag -> handle null problem
        return null;
      }
      Object tag = FIELD_ITEM_STACK_TAG.get().get(handle);
      if (tag == null) {
        // If it is a pure vanilla server, such as spigot, paper, the value of this field must not be null.
        // The invoke save method is needed when the server is forge combined.
        // Tested:
        //   - Arclight-1.14    : pass
        //   - Arclight-1.15    : pass
        //   - Arclight-1.16    : null
        //   - CatServer-1.12.2 : pass
        //   - Magma-1.12       : pass
        //   - Magma-1.15       : pass
        //   - Mohist-1.12      : pass
        //   - Mohist-1.16      : null
        // Possible patch changes made in forge 1.16? Maybe
        Object internal = NBTFactory.newInternal(TagType.COMPOUND, null);
        METHOD_ITEM_STACK_SAVE.get().invoke(handle, internal);
        CompoundTag compound = (CompoundTag) NBTFactory.from(internal);
        return compound.getCompoundOrNull("tag"); // get tag only
      } else {
        return (CompoundTag) NBTFactory.from(tag);
      }
    } else {
      Object origin = asNMSCopy(stack);
      ItemStack obcStack = (ItemStack) asCraftMirror(origin);
      obcStack.setItemMeta(stack.getItemMeta()); // Copy item meta
      return readStackTag(obcStack);
    }
  }

  @NotNull
  public static CompoundTag readStackTagOrPresent(ItemStack stack, @Nullable Supplier<CompoundTag> present) {
    CompoundTag compound = readStackTag(stack);
    if (compound == null && present != null) compound = present.get();
    return compound != null ? compound : new CompoundTag();
  }

  @NotNull
  public static CompoundTag readStackTagOrPresent(ItemStack stack) {
    return readStackTagOrPresent(stack, null);
  }

  @NotNull
  @Contract("null -> !null")
  @SuppressWarnings("ConstantConditions")
  public static ItemStack createStack(@Nullable CompoundTag item) {
    if (item == null) return new ItemStack(Material.AIR);
    if (METHOD_ITEM_STACK_CREATE.get() == null && CONSTRUCTOR_ITEM_STACK.get() == null) {
      // Unless the server is a specially modified version that cannot be reflected to the member structure.
      // It is only used to avoid the null pointer problem below.
      throw new UnsupportedOperationException(
        "Unsupported server, Missing match: \n" +
          "\tNMS.ItemStack -> Constructor(NMS.NBTTagCompound)\n" +
          "\tNMS.ItemStack -> Method: public static NMS.ItemStack create(NMS.NBTTagCompound)");
    }
    Object internal = NBTFactory.to(item);
    Object origin = METHOD_ITEM_STACK_CREATE.get() != null
      ? METHOD_ITEM_STACK_CREATE.get().invoke(null, internal)
      : CONSTRUCTOR_ITEM_STACK.get().newInstance(internal);
    // If the item id is an invalid material type, then origin is null.
    // Return an 'AIR' item stack.
    if (origin == null) return new ItemStack(Material.AIR);
    return (ItemStack) asCraftMirror(origin); // Instance of OBC.inventory.CraftItemStack
  }

  @SuppressWarnings({ "unchecked", "ConstantConditions" })
  public static void writeStackTag(@Nullable ItemStack stack, @Nullable CompoundTag tag) {
    if (stack == null) return;
    if (METHOD_ITEM_STACK_CREATE.get() == null && CONSTRUCTOR_ITEM_STACK.get() == null) {
      // Unless the server is a specially modified version that cannot be reflected to the member structure.
      // It is only used to avoid the null pointer problem below.
      throw new UnsupportedOperationException(
        "Unsupported server, Missing match: \n" +
        "\tNMS.ItemStack -> Constructor(NMS.NBTTagCompound)\n" +
        "\tNMS.ItemStack -> Method: public static NMS.ItemStack create(NMS.NBTTagCompound)");
    }

    if (CLASS_ITEM_STACK.isInstance(stack)) {
      Object handle = FIELD_CRAFT_ITEM_STACK_HANDLE.get().get(stack);
      if (handle == null) {
        // Why the handle is null? Because the item material type is invalid, such as WALL_BANNER.
        // Return an null tag or throw an exception, need confirmation.
        // TODO: ItemFactory.writeTag -> handle null problem
        return;
      }
      Object internal = tag != null ? NBTFactory.to(tag) : tag;
      FIELD_ITEM_STACK_TAG.get().set(handle, internal);
    } else if (tag != null) {
      // In order to avoid the problem that some special Forge servers
      // only modify the origin field and do not take effect.
      // The method used here is to first save a copy of the NBT data of the entire item stack,
      // then overwrite the 'tag' value and finally use this data to create a new item stack.
      Object internal = NBTFactory.to(tag);
      Object origin = asNMSCopy(stack);
      Object saved = NBTFactory.newInternal(TagType.COMPOUND, null);
      METHOD_ITEM_STACK_SAVE.get().invoke(origin, saved);

      Map<String, Object> value = (Map<String, Object>) FIELD_NBT_TAG_COMPOUND_VALUE.get().get(saved);
      value.put("Damage", tag.getShortOrNull("Damage")); // Compatible with the old version
      value.put("tag", internal);

      Object newOrigin = METHOD_ITEM_STACK_CREATE.get() != null
        ? METHOD_ITEM_STACK_CREATE.get().invoke(null, saved)
        : CONSTRUCTOR_ITEM_STACK.get().newInstance(saved);
      ItemStack obcStack = (ItemStack) asCraftMirror(newOrigin);
      stack.setItemMeta(obcStack.getItemMeta());
    } else {
      stack.setItemMeta(null);
    }
  }

  @Contract("null, _ -> fail; _, null -> fail")
  public static void modifyStackTag(ItemStack stack, Consumer<CompoundTag> modifier) {
    if (stack == null) throw new NullPointerException("stack");
    if (modifier == null) throw new NullPointerException("modifier");
    CompoundTag tag = readStackTagOrPresent(stack);
    modifier.accept(tag);
    writeStackTag(stack, tag);
  }
}
