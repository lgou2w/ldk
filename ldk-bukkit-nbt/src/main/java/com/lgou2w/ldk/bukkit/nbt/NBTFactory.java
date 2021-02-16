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

import com.lgou2w.ldk.nbt.NBTBase;
import com.lgou2w.ldk.nbt.NBTTagByte;
import com.lgou2w.ldk.nbt.NBTTagByteArray;
import com.lgou2w.ldk.nbt.NBTTagCompound;
import com.lgou2w.ldk.nbt.NBTTagDouble;
import com.lgou2w.ldk.nbt.NBTTagEnd;
import com.lgou2w.ldk.nbt.NBTTagFloat;
import com.lgou2w.ldk.nbt.NBTTagInt;
import com.lgou2w.ldk.nbt.NBTTagIntArray;
import com.lgou2w.ldk.nbt.NBTTagList;
import com.lgou2w.ldk.nbt.NBTTagLong;
import com.lgou2w.ldk.nbt.NBTTagLongArray;
import com.lgou2w.ldk.nbt.NBTTagShort;
import com.lgou2w.ldk.nbt.NBTTagString;
import com.lgou2w.ldk.nbt.NBTType;
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
      throw new RuntimeException("Error in initializing internal static block: ", e);
    }
  }

  /// NMS Accessors

  // NMS.NBTTagList -> private byte type;
  @NotNull final static FieldAccessor<Object, Byte> FIELD_NBT_TAG_LIST_TYPE
    = FuzzyReflection.of(CLASS_NBT_TAG_LIST, true)
    .useFieldMatcher()
    .withoutModifiers(Modifier.FINAL, Modifier.STATIC)
    .withType(byte.class)
    .resultAccessorAs();

  // NMS.NBTBase -> public abstract byte getTypeId();
  @NotNull final static MethodAccessor<Object, Byte> METHOD_NBT_BASE_GET_TYPE_ID
    = FuzzyReflection.of(CLASS_NBT_BASE, true)
    .useMethodMatcher()
    .withModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
    .withArgsCount(0)
    .withType(byte.class)
    .resultAccessorAs();

  final static Map<NBTType, FieldAccessor<Object, Object>> INTERNAL_FIELD_MAP = new HashMap<>();
  final static Map<NBTType, ConstructorAccessor<Object>> INTERNAL_CONSTRUCTOR_MAP = new HashMap<>();
  final static Map<NBTType, String> INTERNAL_CLASS_NAME_MAP = Collections.unmodifiableMap(new HashMap<NBTType, String>() {{
    put(NBTType.END,        "NBTTagEnd");
    put(NBTType.BYTE,       "NBTTagByte");
    put(NBTType.SHORT,      "NBTTagShort");
    put(NBTType.INT,        "NBTTagInt");
    put(NBTType.LONG,       "NBTTagLong");
    put(NBTType.FLOAT,      "NBTTagFloat");
    put(NBTType.DOUBLE,     "NBTTagDouble");
    put(NBTType.STRING,     "NBTTagString");
    put(NBTType.BYTE_ARRAY, "NBTTagByteArray");
    put(NBTType.INT_ARRAY,  "NBTTagIntArray");
    put(NBTType.LIST,       "NBTTagList");
    put(NBTType.COMPOUND,   "NBTTagCompound");
    put(NBTType.LONG_ARRAY, "NBTTagLongArray");
  }});

  static ConstructorAccessor<Object> lookupInternalConstructor(NBTType type) throws ClassNotFoundException {
    if (type == NBTType.END) throw new IllegalArgumentException("END");
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
      ).resultAccessor();
      INTERNAL_CONSTRUCTOR_MAP.put(type, accessor);
    }
    return accessor;
  }

  static FieldAccessor<Object, Object> lookupInternalField(NBTType type) throws ClassNotFoundException {
    if (type == NBTType.END) throw new IllegalArgumentException("END");
    FieldAccessor<Object, Object> accessor = INTERNAL_FIELD_MAP.get(type);
    if (accessor == null) {
      @NotNull String className = INTERNAL_CLASS_NAME_MAP.get(type);
      accessor = FuzzyReflection.of(getMinecraftClass(className), true)
        .useFieldMatcher()
        .withType(type.getPrimitive())
        .resultAccessor();
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
        .resultAccessor()
        .newInstance();
    }
  }

  /// Public API

  @NotNull
  @Contract("null -> fail; !null -> !null")
  public static NBTBase<?> from(Object nbt) throws IllegalArgumentException {
    if (nbt == null) throw new NullPointerException("nbt");
    if (!CLASS_NBT_BASE.isInstance(nbt))
      throw new IllegalArgumentException("Value type of the instance does not match. (Expected: " + CLASS_NBT_BASE + ")");
    @SuppressWarnings("ConstantConditions")
    int typeId = METHOD_NBT_BASE_GET_TYPE_ID.invoke(nbt);
    NBTType type = Objects.requireNonNull(NBTType.fromId(typeId), "Invalid nbt type id: " + typeId);
    @NotNull FieldAccessor<Object, Object> field;
    try {
      field = lookupInternalField(type);
    } catch (ClassNotFoundException e) {
      throw new UnsupportedOperationException("Server version does not support this type: " + type);
    }
    Object value = Objects.requireNonNull(field.get(nbt), "Null value for nbt: " + nbt);
    switch (type) {
      case END: return NBTTagEnd.INSTANCE;
      case BYTE: return new NBTTagByte((byte) value);
      case SHORT: return new NBTTagShort((short) value);
      case INT: return new NBTTagInt((int) value);
      case LONG: return new NBTTagLong((long) value);
      case FLOAT: return new NBTTagFloat((float) value);
      case DOUBLE: return new NBTTagDouble((double) value);
      case BYTE_ARRAY: return new NBTTagByteArray((byte[]) value);
      case STRING: return new NBTTagString((String) value);
      case INT_ARRAY: return new NBTTagIntArray((int[]) value);
      case LONG_ARRAY: return new NBTTagLongArray((long[]) value);
      case LIST:
        NBTTagList list = new NBTTagList();
        List<Object> listValue = (List<Object>) value;
        for (Object element : listValue) {
          NBTBase<?> elementFrom = from(element);
          list.add(elementFrom);
        }
        return list;
      case COMPOUND:
        NBTTagCompound compound = new NBTTagCompound();
        Map<String, Object> compoundValue = (Map<String, Object>) value;
        for (Map.Entry<String, Object> entry : compoundValue.entrySet()) {
          NBTBase<?> entryValueFrom = from(entry.getValue());
          compound.set(entry.getKey(), entryValueFrom);
        }
        return compound;
      default:
        throw new UnsupportedOperationException();
    }
  }

  @NotNull
  @Contract("null -> fail; !null -> !null")
  public static Object to(NBTBase<?> nbt) throws IllegalArgumentException {
    if (nbt == null) throw new NullPointerException("nbt");
    switch (nbt.getType()) {
      case END: return INSTANCE_NBT_TAG_END;
      case LIST:
        NBTTagList list = (NBTTagList) nbt;
        List<Object> listValue = new ArrayList<>();
        for (NBTBase<?> element : list) {
          Object elementTo = to(element);
          listValue.add(elementTo);
        }
        return newInternal(NBTType.LIST, listValue, list.getElementType());
      case COMPOUND:
        NBTTagCompound compound = (NBTTagCompound) nbt;
        Map<String, Object> compoundValue = new LinkedHashMap<>();
        for (Map.Entry<String, NBTBase<?>> entry : compound.entrySet()) {
          Object entryValueTo = to(entry.getValue());
          compoundValue.put(entry.getKey(), entryValueTo);
        }
        return newInternal(NBTType.COMPOUND, compoundValue);
      case LONG_ARRAY:
        boolean supported = CLASS_NBT_TAG_LONG_ARRAY != null;
        if (supported) return newInternal(nbt.getType(), nbt.getValue());
        // Server does not support, convert to list to compatibility
        NBTTagLongArray longArray = (NBTTagLongArray) nbt;
        NBTTagList longList = new NBTTagList();
        longList.addLong(longArray.getValue());
        return to(longList);
      default:
        return newInternal(nbt.getType(), nbt.getValue());
    }
  }

  @NotNull
  @Contract("null, _ -> fail")
  public static Object newInternal(
    NBTType type,
    @Nullable Object value
  ) throws IllegalArgumentException {
    return newInternal(type, value, NBTType.END);
  }

  @NotNull
  @Contract("null, _, _ -> fail; _, _, null -> fail")
  public static Object newInternal(
    NBTType type,
    @Nullable Object value,
    NBTType listElementType
  ) throws IllegalArgumentException {
    if (type == null) throw new NullPointerException("type");
    if (listElementType == null) throw new NullPointerException("listElementType");
    if (value != null && !type.getPrimitive().isAssignableFrom(DataType.ofPrimitive(value.getClass())))
      throw new IllegalArgumentException("Value '" + value.getClass() + "' and type mismatch. (Expected: " + type.getPrimitive() + ")");
    @NotNull ConstructorAccessor<Object> constructor;
    @NotNull FieldAccessor<Object, Object> field;
    try {
      constructor = lookupInternalConstructor(type);
      field = lookupInternalField(type);
    } catch (ClassNotFoundException e) {
      throw new UnsupportedOperationException("Server version does not support this type: " + type);
    }
    if (type.isListOrCompound()) {
      Object inst = constructor.newInstance();
      if (value != null) field.set(inst, value);
      if (value != null && type == NBTType.LIST) FIELD_NBT_TAG_LIST_TYPE.set(inst, (byte) listElementType.getId());
      return inst;
    } else {
      return constructor.newInstance(value);
    }
  }
}
