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

package com.lgou2w.ldk.nbt;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CompoundTag extends BaseTag<Map<String, BaseTag<?>>> implements Map<String, BaseTag<?>> {

  @Contract("null -> fail")
  public CompoundTag(Map<String, BaseTag<?>> value) {
    super(new LinkedHashMap<>(value));
  }

  public CompoundTag() {
    super(new LinkedHashMap<>());
  }

  @NotNull
  @Contract("_ -> new")
  public static CompoundTag of(Consumer<CompoundTag> initializer) {
    CompoundTag tag = new CompoundTag();
    if (initializer != null)
      initializer.accept(tag);
    return tag;
  }

  @Override
  @NotNull
  public TagType getType() {
    return TagType.COMPOUND;
  }

  @Override
  public void setValue(Map<String, BaseTag<?>> value) {
    super.setValue(new LinkedHashMap<>(value));
  }

  @Override
  public void read(@NotNull DataInput input) throws IOException {
    NBTMetadata metadata;
    while (!(metadata = NBTStreams.read(input)).isEndType()) {
      value.put(metadata.getName(), metadata.getValue());
    }
  }

  @Override
  public void write(@NotNull DataOutput output) throws IOException {
    for (Map.Entry<String, BaseTag<?>> entry : value.entrySet()) {
      BaseTag<?> value = entry.getValue();
      String key = entry.getKey();
      NBTStreams.write(output, NBTMetadata.of(key, value));
    }
    output.writeByte(0); // END
  }

  @Override
  public String toString() {
    return "CompoundTag{" +
      "value=" + value +
      '}';
  }

  @Override
  protected void toMojangsonBuilder(@NotNull StringBuilder builder, boolean includeColor) {
    builder.append('{');
    Set<Map.Entry<String, BaseTag<?>>> entrySet = entrySet();
    int len = entrySet.size(), i = 0;

    if (!includeColor) {
      for (Map.Entry<String, BaseTag<?>> entry : entrySet) {
        String key = entry.getKey();
        BaseTag<?> value = entry.getValue();
        if (i >= 1 && i < len) builder.append(',');
        builder.append("\"");
        builder.append(key);
        builder.append("\":");
        value.toMojangsonBuilder(builder, false);
        i++;
      }
    } else {
      for (Map.Entry<String, BaseTag<?>> entry : entrySet) {
        String key = entry.getKey();
        BaseTag<?> value = entry.getValue();
        if (i >= 1 && i < len) builder.append(", ");
        builder.append("\"");
        builder.append(COLOR_AQUA);
        builder.append(key);
        builder.append(COLOR_RESET);
        builder.append("\": ");
        value.toMojangsonBuilder(builder, true);
        i++;
      }
    }
    builder.append('}');
  }

  @Override
  @NotNull
  public CompoundTag clone() {
    Map<String, BaseTag<?>> newValue = new LinkedHashMap<>(value.size());
    for (Map.Entry<String, BaseTag<?>> entry : value.entrySet())
      newValue.put(entry.getKey(), entry.getValue().clone());
    return new CompoundTag(newValue);
  }

  /// Extended

  @NotNull
  @Contract("null, _ -> fail; _, null -> fail; !null, !null -> this")
  public CompoundTag set(String key, BaseTag<?> value) {
    Objects.requireNonNull(key, "key");
    Objects.requireNonNull(value, "value");
    put(key, value);
    return this;
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public CompoundTag setByte(String key, byte value) {
    return set(key, new ByteTag(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public CompoundTag setByte(String key, int value) {
    return setByte(key, (byte) value);
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public CompoundTag setShort(String key, short value) {
    return set(key, new ShortTag(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public CompoundTag setShort(String key, int value) {
    return setShort(key, (short) value);
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public CompoundTag setInt(String key, int value) {
    return set(key, new IntTag(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public CompoundTag setLong(String key, long value) {
    return set(key, new LongTag(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public CompoundTag setFloat(String key, float value) {
    return set(key, new FloatTag(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public CompoundTag setDouble(String key, double value) {
    return set(key, new DoubleTag(value));
  }

  @NotNull
  @Contract("null, _ -> fail; _, null -> fail; !null, !null -> this")
  public CompoundTag setByteArray(String key, byte[] value) {
    return set(key, new ByteArrayTag(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public CompoundTag setString(String key, String value) {
    return set(key, new StringTag(value));
  }

  @NotNull
  @Contract("null, _ -> fail; _, null -> fail; !null, !null -> this")
  public CompoundTag setIntArray(String key, int[] value) {
    return set(key, new IntArrayTag(value));
  }

  @NotNull
  @Contract("null, _ -> fail; _, null -> fail; !null, !null -> this")
  public CompoundTag setLongArray(String key, long[] value) {
    return set(key, new LongArrayTag(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public CompoundTag setBoolean(String key, boolean value) {
    return setByte(key, (byte) (value ? 1 : 0));
  }

  @Nullable
  @Contract("_, _, false -> !null")
  private <T extends BaseTag<?>> T find(
    String key,
    Class<T> expected,
    boolean nullable
  ) throws NoSuchElementException, ClassCastException {
    Objects.requireNonNull(key, "key");
    BaseTag<?> value = this.value.get(key);
    if (nullable && value == null) return null;
    if (value == null) throw new NoSuchElementException("Key value does not exist: " + key);
    TagType expectedType = TagType.fromClass(expected);
    if (!expected.isInstance(value)) {
      String name = expectedType != null ? expectedType.name() : expected.getSimpleName();
      throw new ClassCastException("Key '" + key + "' of value type '" + value.getType() + "' does not match. (Expected: " + name + ")");
    }
    return expected.cast(value);
  }

  @NotNull
  private Number findNumber(@NotNull String key) throws NoSuchElementException, ClassCastException {
    NumericTag<?> value = find(key, NumericTag.class, false);
    return value.value;
  }

  @Nullable
  private Number findNumberOrNull(@NotNull String key) {
    NumericTag<?> value = find(key, NumericTag.class, true);
    return value != null ? value.value : null;
  }

  @SuppressWarnings("unchecked")
  @NotNull
  private <T, V> T findOrPresent(@NotNull TagType type, @NotNull String key, @Nullable Supplier<V> present) {
    BaseTag<?> value = find(key, type.getWrapped(), true);
    if (value == null) {
      V pv = present != null ? present.get() : null;
      switch (type) {
        case END: throw new UnsupportedOperationException("END");
        case BYTE:
          value = new ByteTag((byte) (pv != null ? pv : 0));
          break;
        case SHORT:
          value = new ShortTag((short) (pv != null ? pv : 0));
          break;
        case INT:
          value = new IntTag((int) (pv != null ? pv : 0));
          break;
        case LONG:
          value = new LongTag((long) (pv != null ? pv : 0L));
          break;
        case FLOAT:
          value = new FloatTag((float) (pv != null ? pv : 0f));
          break;
        case DOUBLE:
          value = new DoubleTag((double) (pv != null ? pv : 0d));
          break;
        case BYTE_ARRAY:
          value = new ByteArrayTag(pv != null ? (byte[]) pv : new byte[0]);
          break;
        case STRING:
          value = new StringTag(pv != null ? (String) pv : "");
          break;
        case INT_ARRAY:
          value = new IntArrayTag(pv != null ? (int[]) pv : new int[0]);
          break;
        case LONG_ARRAY:
          value = new LongArrayTag(pv != null ? (long[]) pv : new long[0]);
          break;
        case LIST:
          value = pv != null ? (ListTag) pv : new ListTag();
          break;
        case COMPOUND:
          value = pv != null ? (CompoundTag) pv : new CompoundTag();
          break;
      }
      put(key, value);
    }
    return (T) (value.getType().isListOrCompound() ? value : value.value);
  }

  @Contract("null -> fail")
  public byte getByte(String key) throws NoSuchElementException, ClassCastException {
    return findNumber(key).byteValue();
  }

  @Nullable
  @Contract("null -> fail")
  public Byte getByteOrNull(String key) throws NoSuchElementException, ClassCastException  {
    Number value = findNumberOrNull(key);
    return value != null ? value.byteValue() : null;
  }

  @Contract("null, _ -> fail")
  public byte getByteOrPresent(String key, @Nullable Supplier<Byte> present) throws ClassCastException  {
    Number value = findOrPresent(TagType.BYTE, key, present);
    return value.byteValue();
  }

  @Contract("null -> fail")
  public byte getByteOrPresent(String key) throws ClassCastException  {
    return getByteOrPresent(key, null);
  }

  @Contract("null -> fail")
  public short getShort(String key) throws NoSuchElementException, ClassCastException  {
    return findNumber(key).shortValue();
  }

  @Nullable
  @Contract("null -> fail")
  public Short getShortOrNull(String key) throws NoSuchElementException, ClassCastException  {
    Number value = findNumberOrNull(key);
    return value != null ? value.shortValue() : null;
  }

  @Contract("null, _ -> fail")
  public short getShortOrPresent(String key, @Nullable Supplier<Short> present) throws ClassCastException  {
    Number value = findOrPresent(TagType.BYTE, key, present);
    return value.byteValue();
  }

  @Contract("null -> fail")
  public short getShortOrPresent(String key) throws ClassCastException  {
    return getShortOrPresent(key, null);
  }

  @Contract("null -> fail")
  public int getInt(String key) throws NoSuchElementException, ClassCastException  {
    return findNumber(key).shortValue();
  }

  @Nullable
  @Contract("null -> fail")
  public Integer getIntOrNull(String key) throws NoSuchElementException, ClassCastException  {
    Number value = findNumberOrNull(key);
    return value != null ? value.intValue() : null;
  }

  @Contract("null, _ -> fail")
  public int getIntOrPresent(String key, @Nullable Supplier<Integer> present) throws ClassCastException  {
    Number value = findOrPresent(TagType.INT, key, present);
    return value.intValue();
  }

  @Contract("null -> fail")
  public int getIntOrPresent(String key) throws ClassCastException  {
    return getIntOrPresent(key, null);
  }

  @Contract("null -> fail")
  public long getLong(String key) throws NoSuchElementException, ClassCastException  {
    return findNumber(key).longValue();
  }

  @Nullable
  @Contract("null -> fail")
  public Long getLongOrNull(String key) throws NoSuchElementException, ClassCastException  {
    Number value = findNumberOrNull(key);
    return value != null ? value.longValue() : null;
  }

  @Contract("null, _ -> fail")
  public long getLongOrPresent(String key, @Nullable Supplier<Long> present) throws ClassCastException  {
    Number value = findOrPresent(TagType.LONG, key, present);
    return value.longValue();
  }

  @Contract("null -> fail")
  public long getLongOrPresent(String key) throws ClassCastException  {
    return getLongOrPresent(key, null);
  }

  @Contract("null -> fail")
  public float getFloat(String key) throws NoSuchElementException, ClassCastException  {
    return findNumber(key).floatValue();
  }

  @Nullable
  @Contract("null -> fail")
  public Float getFloatOrNull(String key) throws NoSuchElementException, ClassCastException  {
    Number value = findNumberOrNull(key);
    return value != null ? value.floatValue() : null;
  }

  @Contract("null, _ -> fail")
  public float getFloatOrPresent(String key, @Nullable Supplier<Float> present) throws ClassCastException  {
    Number value = findOrPresent(TagType.FLOAT, key, present);
    return value.floatValue();
  }

  @Contract("null -> fail")
  public float getFloatOrPresent(String key) throws ClassCastException  {
    return getFloatOrPresent(key, null);
  }

  @Contract("null -> fail")
  public double getDouble(String key) throws NoSuchElementException, ClassCastException  {
    return findNumber(key).doubleValue();
  }

  @Nullable
  @Contract("null -> fail")
  public Double getDoubleOrNull(String key) throws NoSuchElementException, ClassCastException  {
    Number value = findNumberOrNull(key);
    return value != null ? value.doubleValue() : null;
  }

  @Contract("null, _ -> fail")
  public double getDoubleOrPresent(String key, @Nullable Supplier<Double> present) throws ClassCastException  {
    Number value = findOrPresent(TagType.DOUBLE, key, present);
    return value.doubleValue();
  }

  @Contract("null -> fail")
  public double getDoubleOrPresent(String key) throws ClassCastException  {
    return getDoubleOrPresent(key, null);
  }

  @Contract("null -> fail")
  public byte @NotNull [] getByteArray(String key) throws NoSuchElementException, ClassCastException  {
    ByteArrayTag value = find(key, ByteArrayTag.class, false);
    return value.value;
  }

  @Contract("null -> fail")
  public byte @Nullable [] getByteArrayOrNull(String key) throws NoSuchElementException, ClassCastException  {
    ByteArrayTag value = find(key, ByteArrayTag.class, true);
    return value != null ? value.value : null;
  }

  @Contract("null, _ -> fail")
  public byte @NotNull [] getByteArrayOrPresent(String key, @Nullable Supplier<byte[]> present) throws ClassCastException  {
    ByteArrayTag value = findOrPresent(TagType.BYTE_ARRAY, key, present);
    return value.value;
  }

  @Contract("null -> fail")
  public byte @NotNull [] getByteArrayOrPresent(String key) throws ClassCastException  {
    return getByteArrayOrPresent(key, null);
  }

  @NotNull
  @Contract("null -> fail")
  public String getString(String key) throws NoSuchElementException, ClassCastException  {
    StringTag value = find(key, StringTag.class, false);
    return value.value;
  }

  @Nullable
  @Contract("null -> fail")
  public String getStringOrNull(String key) throws NoSuchElementException, ClassCastException  {
    StringTag value = find(key, StringTag.class, true);
    return value != null ? value.value : null;
  }

  @NotNull
  @Contract("null, _ -> fail")
  public String getStringOrPresent(String key, @Nullable Supplier<String> present) throws ClassCastException  {
    StringTag value = findOrPresent(TagType.STRING, key, present);
    return value.value;
  }

  @NotNull
  @Contract("null -> fail")
  public String getStringOrPresent(String key) throws ClassCastException  {
    return getStringOrPresent(key, null);
  }

  @NotNull
  @Contract("null -> fail")
  public ListTag getList(String key) throws NoSuchElementException, ClassCastException  {
    return find(key, ListTag.class, false);
  }

  @Nullable
  @Contract("null -> fail")
  public ListTag getListOrNull(String key) throws NoSuchElementException, ClassCastException  {
    return find(key, ListTag.class, true);
  }

  @NotNull
  @Contract("null, _ -> fail")
  public ListTag getListOrPresent(String key, @Nullable Supplier<ListTag> present) throws ClassCastException  {
    return findOrPresent(TagType.LIST, key, present);
  }

  @NotNull
  @Contract("null -> fail")
  public ListTag getListOrPresent(String key) throws ClassCastException  {
    return getListOrPresent(key, null);
  }

  @NotNull
  @Contract("null -> fail")
  public CompoundTag getCompound(String key) throws NoSuchElementException, ClassCastException  {
    return find(key, CompoundTag.class, false);
  }

  @Nullable
  @Contract("null -> fail")
  public CompoundTag getCompoundOrNull(String key) throws NoSuchElementException, ClassCastException  {
    return find(key, CompoundTag.class, true);
  }

  @NotNull
  @Contract("null, _ -> fail")
  public CompoundTag getCompoundOrPresent(String key, @Nullable Supplier<CompoundTag> present) throws ClassCastException  {
    return findOrPresent(TagType.COMPOUND, key, present);
  }

  @NotNull
  @Contract("null -> fail")
  public CompoundTag getCompoundOrPresent(String key) throws ClassCastException  {
    return getCompoundOrPresent(key, null);
  }

  @Contract("null -> fail")
  public int @NotNull [] getIntArray(String key) throws NoSuchElementException, ClassCastException  {
    IntArrayTag value = find(key, IntArrayTag.class, false);
    return value.value;
  }

  @Contract("null -> fail")
  public int @Nullable [] getIntArrayOrNull(String key) throws NoSuchElementException, ClassCastException  {
    IntArrayTag value = find(key, IntArrayTag.class, true);
    return value != null ? value.value : null;
  }

  @Contract("null, _ -> fail")
  public int @NotNull [] getIntArrayOrPresent(String key, @Nullable Supplier<int[]> present) throws ClassCastException  {
    IntArrayTag value = findOrPresent(TagType.INT_ARRAY, key, present);
    return value.value;
  }

  @Contract("null -> fail")
  public int @NotNull [] getIntArrayOrPresent(String key) throws ClassCastException  {
    return getIntArrayOrPresent(key, null);
  }

  @Contract("null -> fail")
  public long @NotNull [] getLongArray(String key) throws NoSuchElementException, ClassCastException  {
    LongArrayTag value = find(key, LongArrayTag.class, false);
    return value.value;
  }

  @Contract("null -> fail")
  public long @Nullable [] getLongArrayOrNull(String key) throws NoSuchElementException, ClassCastException  {
    LongArrayTag value = find(key, LongArrayTag.class, true);
    return value != null ? value.value : null;
  }

  @Contract("null, _ -> fail")
  public long @NotNull [] getLongArrayOrPresent(String key, @Nullable Supplier<long[]> present) throws ClassCastException  {
    LongArrayTag value = findOrPresent(TagType.LONG_ARRAY, key, present);
    return value.value;
  }

  @Contract("null -> fail")
  public long @NotNull [] getLongArrayOrPresent(String key) throws ClassCastException  {
    return getLongArrayOrPresent(key, null);
  }

  @Contract("null -> fail")
  public boolean getBoolean(String key) throws NoSuchElementException, ClassCastException  {
    return getByte(key) == 1;
  }

  @Nullable
  @Contract("null -> fail")
  public Boolean getBooleanOrNull(String key) throws NoSuchElementException, ClassCastException  {
    Byte value = getByteOrNull(key);
    return value != null ? value != 0 : null; // if not zero, true
  }

  @Contract("null, _ -> fail")
  public boolean getBooleanOrPresent(String key, @Nullable Supplier<Boolean> present) throws ClassCastException  {
    return getByteOrPresent(key, () -> {
      Boolean pv = present != null ? present.get() : null;
      return (byte) (pv == null ? 0 : 1);
    }) != 0; // if not zero, true
  }

  @Contract("null -> fail")
  public boolean getBooleanOrPresent(String key) throws ClassCastException  {
    return getByteOrPresent(key) != 0; // if not zero, true
  }

  @Contract("null -> false")
  public boolean hasKey(@Nullable String key) {
    if (key == null) return false;
    return value.containsKey(key);
  }

  @Contract("null -> false")
  public boolean hasValue(@Nullable BaseTag<?> value) {
    if (value == null) return false;
    return this.value.containsValue(value);
  }

  /// Map

  @Override
  public int size() {
    return value.size();
  }

  @Override
  @Contract("null -> false")
  public boolean containsKey(@Nullable Object key) {
    if (!(key instanceof String)) return false;
    return hasKey((String) key);
  }

  @Override
  @Contract("null -> false")
  public boolean containsValue(@Nullable Object value) {
    if (!(value instanceof BaseTag)) return false;
    return hasValue((BaseTag<?>) value);
  }

  @Override
  @Contract("null -> null")
  public BaseTag<?> get(@Nullable Object key) {
    if (!(key instanceof String)) return null;
    return value.get(key);
  }

  @Override
  public boolean isEmpty() {
    return value.isEmpty();
  }

  @NotNull
  @Override
  public Set<Entry<String, BaseTag<?>>> entrySet() {
    return value.entrySet();
  }

  @NotNull
  @Override
  public Set<String> keySet() {
    return value.keySet();
  }

  @NotNull
  @Override
  public Collection<BaseTag<?>> values() {
    return value.values();
  }

  @Nullable
  @Override
  public BaseTag<?> put(String key, BaseTag<?> value) {
    return this.value.put(key, value);
  }

  @Override
  @Contract("null -> null")
  public BaseTag<?> remove(@Nullable Object key) {
    if (!(key instanceof String)) return null;
    return value.remove(key);
  }

  @Override
  public void putAll(@NotNull Map<? extends String, ? extends BaseTag<?>> m) {
    value.putAll(m);
  }

  @Override
  public void clear() {
    value.clear();
  }
}
