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

package com.lgou2w.ldk.bukkit.nbt;

import com.lgou2w.ldk.nbt.BaseTag;
import com.lgou2w.ldk.nbt.ByteTag;
import com.lgou2w.ldk.nbt.ByteArrayTag;
import com.lgou2w.ldk.nbt.CompoundTag;
import com.lgou2w.ldk.nbt.DoubleTag;
import com.lgou2w.ldk.nbt.EndTag;
import com.lgou2w.ldk.nbt.FloatTag;
import com.lgou2w.ldk.nbt.IntTag;
import com.lgou2w.ldk.nbt.IntArrayTag;
import com.lgou2w.ldk.nbt.ListTag;
import com.lgou2w.ldk.nbt.LongTag;
import com.lgou2w.ldk.nbt.LongArrayTag;
import com.lgou2w.ldk.nbt.ShortTag;
import com.lgou2w.ldk.nbt.StringTag;
import com.lgou2w.ldk.nbt.TagType;
import com.lgou2w.ldk.reflect.ConstructorAccessor;
import com.lgou2w.ldk.reflect.ConstructorReflectionMatcher;
import com.lgou2w.ldk.reflect.DataType;
import com.lgou2w.ldk.reflect.FieldAccessor;
import com.lgou2w.ldk.reflect.FuzzyReflection;
import com.lgou2w.ldk.reflect.MethodAccessor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getMinecraftClass;
import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getMinecraftClassOrNull;

public final class NBTFactory {

  private NBTFactory() { }

  /// NMS Classes

  @NotNull public final static Class<?> CLASS_NBT_BASE;
  @NotNull public final static Class<?> CLASS_NBT_TAG_END;
  @NotNull public final static Class<?> CLASS_NBT_TAG_LIST;
  @NotNull public final static Class<?> CLASS_NBT_TAG_COMPOUND;
  @Nullable public final static Class<?> CLASS_NBT_TAG_LONG_ARRAY; // since Minecraft 1.12

  static {
    try {
      CLASS_NBT_BASE = getMinecraftClass("NBTBase");
      CLASS_NBT_TAG_END = getMinecraftClass("NBTTagEnd");
      CLASS_NBT_TAG_LIST = getMinecraftClass("NBTTagList");
      CLASS_NBT_TAG_COMPOUND = getMinecraftClass("NBTTagCompound");
      CLASS_NBT_TAG_LONG_ARRAY = getMinecraftClassOrNull("NBTTagLongArray");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Error in initializing NBTFactory internal static block:", e);
    }
  }

  /// NMS Accessors

  // NMS.NBTTagList -> private byte type;
  final static Supplier<@NotNull FieldAccessor<Object, Byte>> FIELD_NBT_TAG_LIST_TYPE
    = FuzzyReflection.lazySupplier(CLASS_NBT_TAG_LIST, true, fuzzy -> fuzzy
    .useFieldMatcher()
    .withoutModifiers(Modifier.FINAL, Modifier.STATIC)
    .withType(byte.class)
    .resultAccessorAs("Missing match: NMS.NBTTagList -> Field: byte type"));

  // NMS.NBTBase -> public abstract byte getTypeId();
  final static Supplier<@NotNull MethodAccessor<Object, Byte>> METHOD_NBT_BASE_GET_TYPE_ID
    = FuzzyReflection.lazySupplier(CLASS_NBT_BASE, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
    .withArgsCount(0)
    .withType(byte.class)
    .resultAccessorAs("Missing match: NMS.NBTBase -> Method: public abstract byte getTypeId()"));

  final static Map<TagType, FieldAccessor<Object, Object>> INTERNAL_FIELD_MAP = new HashMap<>();
  final static Map<TagType, ConstructorAccessor<Object>> INTERNAL_CONSTRUCTOR_MAP = new HashMap<>();
  final static Map<TagType, String> INTERNAL_CLASS_NAME_MAP = Collections.unmodifiableMap(new HashMap<TagType, String>() {{
    put(TagType.END,        "NBTTagEnd");
    put(TagType.BYTE,       "NBTTagByte");
    put(TagType.SHORT,      "NBTTagShort");
    put(TagType.INT,        "NBTTagInt");
    put(TagType.LONG,       "NBTTagLong");
    put(TagType.FLOAT,      "NBTTagFloat");
    put(TagType.DOUBLE,     "NBTTagDouble");
    put(TagType.STRING,     "NBTTagString");
    put(TagType.BYTE_ARRAY, "NBTTagByteArray");
    put(TagType.INT_ARRAY,  "NBTTagIntArray");
    put(TagType.LIST,       "NBTTagList");
    put(TagType.COMPOUND,   "NBTTagCompound");
    put(TagType.LONG_ARRAY, "NBTTagLongArray");
  }});

  static ConstructorAccessor<Object> lookupInternalConstructor(TagType type) throws ClassNotFoundException {
    if (type == TagType.END) throw new IllegalArgumentException("END");
    ConstructorAccessor<Object> accessor = INTERNAL_CONSTRUCTOR_MAP.get(type);
    if (accessor == null) {
      @NotNull String className = INTERNAL_CLASS_NAME_MAP.get(type);
      ConstructorReflectionMatcher<Object> matcher = FuzzyReflection
        .of(getMinecraftClass(className), true)
        .useConstructorMatcher();
      accessor = (
        type.isListOrCompound()
          ? matcher.withArgsCount(0)
          : matcher.withArgs(type.getPrimitive())
      ).resultAccessor("Missing match: NMS." + className + " -> Constructor(" + (!type.isListOrCompound()
        ? type.getPrimitive().getSimpleName() + ")"
        : ")"
      ));
      INTERNAL_CONSTRUCTOR_MAP.put(type, accessor);
    }
    return accessor;
  }

  static FieldAccessor<Object, Object> lookupInternalField(TagType type) throws ClassNotFoundException {
    if (type == TagType.END) throw new IllegalArgumentException("END");
    FieldAccessor<Object, Object> accessor = INTERNAL_FIELD_MAP.get(type);
    if (accessor == null) {
      @NotNull String className = INTERNAL_CLASS_NAME_MAP.get(type);
      accessor = FuzzyReflection.of(getMinecraftClass(className), true)
        .useFieldMatcher()
        .withType(type.getPrimitive())
        .resultAccessor("Missing match: NMS." + className + " -> Field: " + type.getPrimitive().getSimpleName() + " value");
      INTERNAL_FIELD_MAP.put(type, accessor);
    }
    return accessor;
  }

  // NMS.NBTTagEnd -> public final static NMS.NBTTagEnd instance; or new instance
  final static Object INSTANCE_NBT_TAG_END = lookupEndInstanceOrNew();

  static Object lookupEndInstanceOrNew() {
    FuzzyReflection reflection = FuzzyReflection.of(CLASS_NBT_TAG_END, true);
    List<FieldAccessor<Object, Object>> fields = reflection
      .useFieldMatcher()
      .withModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
      .withType(CLASS_NBT_TAG_END)
      .resultAccessors();
    if (!fields.isEmpty()) {
      return fields.get(0).get(null);
    } else {
      return reflection
        .useConstructorMatcher()
        .resultAccessor("Missing match: NMS.NBTTagEnd -> Constructor()")
        .newInstance();
    }
  }

  /// Public API

  @NotNull
  @Contract("null -> fail; !null -> !null")
  @SuppressWarnings({ "unchecked", "ConstantConditions" })
  public static BaseTag<?> from(Object nbt) throws IllegalArgumentException {
    Objects.requireNonNull(nbt, "nbt");
    if (!CLASS_NBT_BASE.isInstance(nbt))
      throw new IllegalArgumentException("Value type of the instance does not match. (Expected: " + CLASS_NBT_BASE + ")");
    int typeId = METHOD_NBT_BASE_GET_TYPE_ID.get().invoke(nbt);
    TagType type = Objects.requireNonNull(TagType.fromId(typeId), "Invalid nbt type id: " + typeId);
    FieldAccessor<Object, Object> field;
    try {
      field = lookupInternalField(type);
    } catch (ClassNotFoundException e) {
      throw new UnsupportedOperationException("Server version does not support this type: " + type);
    }
    Object value = Objects.requireNonNull(field.get(nbt), "Null value for nbt: " + nbt);
    switch (type) {
      case END: return EndTag.INSTANCE;
      case BYTE: return new ByteTag((byte) value);
      case SHORT: return new ShortTag((short) value);
      case INT: return new IntTag((int) value);
      case LONG: return new LongTag((long) value);
      case FLOAT: return new FloatTag((float) value);
      case DOUBLE: return new DoubleTag((double) value);
      case BYTE_ARRAY: return new ByteArrayTag((byte[]) value);
      case STRING: return new StringTag((String) value);
      case INT_ARRAY: return new IntArrayTag((int[]) value);
      case LONG_ARRAY: return new LongArrayTag((long[]) value);
      case LIST:
        ListTag list = new ListTag();
        List<Object> listValue = (List<Object>) value;
        for (Object element : listValue) {
          BaseTag<?> elementFrom = from(element);
          list.add(elementFrom);
        }
        return list;
      case COMPOUND:
        CompoundTag compound = new CompoundTag();
        Map<String, Object> compoundValue = (Map<String, Object>) value;
        for (Map.Entry<String, Object> entry : compoundValue.entrySet()) {
          BaseTag<?> entryValueFrom = from(entry.getValue());
          compound.set(entry.getKey(), entryValueFrom);
        }
        return compound;
      default:
        throw new UnsupportedOperationException();
    }
  }

  @NotNull
  @Contract("null -> fail; !null -> !null")
  public static Object to(BaseTag<?> nbt) throws IllegalArgumentException {
    Objects.requireNonNull(nbt, "nbt");
    switch (nbt.getType()) {
      case END: return INSTANCE_NBT_TAG_END;
      case LIST:
        ListTag list = (ListTag) nbt;
        List<Object> listValue = new ArrayList<>();
        for (BaseTag<?> element : list) {
          Object elementTo = to(element);
          listValue.add(elementTo);
        }
        return newInternal(TagType.LIST, listValue, list.getElementType());
      case COMPOUND:
        CompoundTag compound = (CompoundTag) nbt;
        Map<String, Object> compoundValue = new LinkedHashMap<>();
        for (Map.Entry<String, BaseTag<?>> entry : compound.entrySet()) {
          Object entryValueTo = to(entry.getValue());
          compoundValue.put(entry.getKey(), entryValueTo);
        }
        return newInternal(TagType.COMPOUND, compoundValue);
      case LONG_ARRAY:
        boolean supported = CLASS_NBT_TAG_LONG_ARRAY != null;
        if (supported) return newInternal(nbt.getType(), nbt.getValue());
        // Server does not support, convert to list to compatibility
        LongArrayTag longArray = (LongArrayTag) nbt;
        ListTag longList = new ListTag();
        longList.addLong(longArray.getValue());
        return to(longList);
      default:
        return newInternal(nbt.getType(), nbt.getValue());
    }
  }

  @NotNull
  @Contract("null, _ -> fail")
  public static Object newInternal(
    TagType type,
    @Nullable Object value
  ) throws IllegalArgumentException {
    return newInternal(type, value, TagType.END);
  }

  @NotNull
  @Contract("null, _, _ -> fail; _, _, null -> fail")
  public static Object newInternal(
    TagType type,
    @Nullable Object value,
    TagType listElementType
  ) throws IllegalArgumentException {
    Objects.requireNonNull(type, "type");
    Objects.requireNonNull(listElementType, "listElementType");
    if (value != null && !type.getPrimitive().isAssignableFrom(DataType.ofPrimitive(value.getClass())))
      throw new IllegalArgumentException("Value '" + value.getClass() + "' and type mismatch. (Expected: " + type.getPrimitive() + ")");
    ConstructorAccessor<Object> constructor;
    FieldAccessor<Object, Object> field;
    try {
      constructor = lookupInternalConstructor(type);
      field = lookupInternalField(type);
    } catch (ClassNotFoundException e) {
      throw new UnsupportedOperationException("Server version does not support this type: " + type);
    }
    if (type.isListOrCompound()) {
      Object inst = constructor.newInstance();
      if (value != null) field.set(inst, value);
      if (value != null && type == TagType.LIST)
        FIELD_NBT_TAG_LIST_TYPE.get().set(inst, (byte) listElementType.getId());
      return inst;
    } else {
      return constructor.newInstance(value);
    }
  }
}
