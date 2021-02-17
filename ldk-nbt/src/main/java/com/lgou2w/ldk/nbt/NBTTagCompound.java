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
import java.util.Set;
import java.util.function.Supplier;

public class NBTTagCompound extends NBTBase<Map<String, NBTBase<?>>> implements Map<String, NBTBase<?>> {

  @Contract("null -> fail")
  public NBTTagCompound(Map<String, NBTBase<?>> value) {
    super(new LinkedHashMap<>(value));
  }

  public NBTTagCompound() {
    super(new LinkedHashMap<>());
  }

  @Override
  @NotNull
  public NBTType getType() {
    return NBTType.COMPOUND;
  }

  @Override
  public void setValue(Map<String, NBTBase<?>> value) {
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
    for (Map.Entry<String, NBTBase<?>> entry : value.entrySet()) {
      NBTBase<?> value = entry.getValue();
      String key = entry.getKey();
      NBTStreams.write(output, NBTMetadata.of(key, value));
    }
    output.writeByte(0); // END
  }

  @Override
  public String toString() {
    return "NBTTagCompound{" +
      "value=" + value +
      '}';
  }

  @Override
  protected void toMojangsonBuilder(@NotNull StringBuilder builder, boolean color) {
    builder.append('{');
    Set<Map.Entry<String, NBTBase<?>>> entrySet = entrySet();
    int len = entrySet.size(), i = 0;

    if (!color) {
      for (Map.Entry<String, NBTBase<?>> entry : entrySet) {
        String key = entry.getKey();
        NBTBase<?> value = entry.getValue();
        if (i >= 1 && i < len) builder.append(',');
        builder.append("\"");
        builder.append(key);
        builder.append("\":");
        value.toMojangsonBuilder(builder, false);
        i++;
      }
    } else {
      for (Map.Entry<String, NBTBase<?>> entry : entrySet) {
        String key = entry.getKey();
        NBTBase<?> value = entry.getValue();
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
  public NBTTagCompound clone() {
    Map<String, NBTBase<?>> newValue = new LinkedHashMap<>(value.size());
    for (Map.Entry<String, NBTBase<?>> entry : value.entrySet())
      newValue.put(entry.getKey(), entry.getValue().clone());
    return new NBTTagCompound(newValue);
  }

  /// Extended

  @NotNull
  @Contract("null, _ -> fail; _, null -> fail; !null, !null -> this")
  public NBTTagCompound set(String key, NBTBase<?> value) {
    if (key == null) throw new NullPointerException("key");
    if (value == null) throw new NullPointerException("value");
    put(key, value);
    return this;
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public NBTTagCompound setByte(String key, byte value) {
    return set(key, new NBTTagByte(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public NBTTagCompound setByte(String key, int value) {
    return setByte(key, (byte) value);
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public NBTTagCompound setShort(String key, short value) {
    return set(key, new NBTTagShort(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public NBTTagCompound setShort(String key, int value) {
    return setShort(key, (short) value);
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public NBTTagCompound setInt(String key, int value) {
    return set(key, new NBTTagInt(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public NBTTagCompound setLong(String key, long value) {
    return set(key, new NBTTagLong(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public NBTTagCompound setFloat(String key, float value) {
    return set(key, new NBTTagFloat(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public NBTTagCompound setDouble(String key, double value) {
    return set(key, new NBTTagDouble(value));
  }

  @NotNull
  @Contract("null, _ -> fail; _, null -> fail; !null, !null -> this")
  public NBTTagCompound setByteArray(String key, byte[] value) {
    return set(key, new NBTTagByteArray(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public NBTTagCompound setString(String key, String value) {
    return set(key, new NBTTagString(value));
  }

  @NotNull
  @Contract("null, _ -> fail; _, null -> fail; !null, !null -> this")
  public NBTTagCompound setIntArray(String key, int[] value) {
    return set(key, new NBTTagIntArray(value));
  }

  @NotNull
  @Contract("null, _ -> fail; _, null -> fail; !null, !null -> this")
  public NBTTagCompound setLongArray(String key, long[] value) {
    return set(key, new NBTTagLongArray(value));
  }

  @NotNull
  @Contract("null, _ -> fail; !null, _ -> this")
  public NBTTagCompound setBoolean(String key, boolean value) {
    return setByte(key, (byte) (value ? 1 : 0));
  }

  @Nullable
  @Contract("_, _, false -> !null")
  private <T extends NBTBase<?>> T find(
    String key,
    Class<T> expected,
    boolean nullable
  ) throws NoSuchElementException, ClassCastException {
    if (key == null) throw new NullPointerException("key");
    NBTBase<?> value = this.value.get(key);
    if (nullable && value == null) return null;
    if (value == null) throw new NoSuchElementException("Key value does not exist: " + key);
    NBTType expectedType = NBTType.fromClass(expected);
    if (!expected.isInstance(value)) {
      String name = expectedType != null ? expectedType.name() : expected.getSimpleName();
      throw new ClassCastException("Key '" + key + "' of value type '" + value.getType() + "' does not match. (Expected: " + name + ")");
    }
    return expected.cast(value);
  }

  @NotNull
  private Number findNumber(@NotNull String key) throws NoSuchElementException, ClassCastException {
    NBTTagNumeric<?> value = find(key, NBTTagNumeric.class, false);
    return value.value;
  }

  @Nullable
  private Number findNumberOrNull(@NotNull String key) {
    NBTTagNumeric<?> value = find(key, NBTTagNumeric.class, true);
    return value != null ? value.value : null;
  }

  @SuppressWarnings("unchecked")
  @NotNull
  private <T, V> T findOrPresent(@NotNull NBTType type, @NotNull String key, @Nullable Supplier<V> present) {
    NBTBase<?> value = find(key, type.getWrapped(), true);
    if (value == null) {
      V pv = present != null ? present.get() : null;
      switch (type) {
        case END: throw new UnsupportedOperationException("END");
        case BYTE:
          value = new NBTTagByte((byte) (pv != null ? pv : 0));
          break;
        case SHORT:
          value = new NBTTagShort((short) (pv != null ? pv : 0));
          break;
        case INT:
          value = new NBTTagInt((int) (pv != null ? pv : 0));
          break;
        case LONG:
          value = new NBTTagLong((long) (pv != null ? pv : 0L));
          break;
        case FLOAT:
          value = new NBTTagFloat((float) (pv != null ? pv : 0f));
          break;
        case DOUBLE:
          value = new NBTTagDouble((double) (pv != null ? pv : 0d));
          break;
        case BYTE_ARRAY:
          value = new NBTTagByteArray(pv != null ? (byte[]) pv : new byte[0]);
          break;
        case STRING:
          value = new NBTTagString(pv != null ? (String) pv : "");
          break;
        case INT_ARRAY:
          value = new NBTTagIntArray(pv != null ? (int[]) pv : new int[0]);
          break;
        case LONG_ARRAY:
          value = new NBTTagLongArray(pv != null ? (long[]) pv : new long[0]);
          break;
        case LIST:
          value = pv != null ? (NBTTagList) pv : new NBTTagList();
          break;
        case COMPOUND:
          value = pv != null ? (NBTTagCompound) pv : new NBTTagCompound();
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
    Number value = findOrPresent(NBTType.BYTE, key, present);
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
    Number value = findOrPresent(NBTType.BYTE, key, present);
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
    Number value = findOrPresent(NBTType.INT, key, present);
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
    Number value = findOrPresent(NBTType.LONG, key, present);
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
    Number value = findOrPresent(NBTType.FLOAT, key, present);
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
    Number value = findOrPresent(NBTType.DOUBLE, key, present);
    return value.doubleValue();
  }

  @Contract("null -> fail")
  public double getDoubleOrPresent(String key) throws ClassCastException  {
    return getDoubleOrPresent(key, null);
  }

  @Contract("null -> fail")
  public byte @NotNull [] getByteArray(String key) throws NoSuchElementException, ClassCastException  {
    NBTTagByteArray value = find(key, NBTTagByteArray.class, false);
    return value.value;
  }

  @Contract("null -> fail")
  public byte @Nullable [] getByteArrayOrNull(String key) throws NoSuchElementException, ClassCastException  {
    NBTTagByteArray value = find(key, NBTTagByteArray.class, true);
    return value != null ? value.value : null;
  }

  @Contract("null, _ -> fail")
  public byte @NotNull [] getByteArrayOrPresent(String key, @Nullable Supplier<byte[]> present) throws ClassCastException  {
    NBTTagByteArray value = findOrPresent(NBTType.BYTE_ARRAY, key, present);
    return value.value;
  }

  @Contract("null -> fail")
  public byte @NotNull [] getByteArrayOrPresent(String key) throws ClassCastException  {
    return getByteArrayOrPresent(key, null);
  }

  @NotNull
  @Contract("null -> fail")
  public String getString(String key) throws NoSuchElementException, ClassCastException  {
    NBTTagString value = find(key, NBTTagString.class, false);
    return value.value;
  }

  @Nullable
  @Contract("null -> fail")
  public String getStringOrNull(String key) throws NoSuchElementException, ClassCastException  {
    NBTTagString value = find(key, NBTTagString.class, true);
    return value != null ? value.value : null;
  }

  @NotNull
  @Contract("null, _ -> fail")
  public String getStringOrPresent(String key, @Nullable Supplier<String> present) throws ClassCastException  {
    NBTTagString value = findOrPresent(NBTType.STRING, key, present);
    return value.value;
  }

  @NotNull
  @Contract("null -> fail")
  public String getStringOrPresent(String key) throws ClassCastException  {
    return getStringOrPresent(key, null);
  }

  @NotNull
  @Contract("null -> fail")
  public NBTTagList getList(String key) throws NoSuchElementException, ClassCastException  {
    return find(key, NBTTagList.class, false);
  }

  @Nullable
  @Contract("null -> fail")
  public NBTTagList getListOrNull(String key) throws NoSuchElementException, ClassCastException  {
    return find(key, NBTTagList.class, true);
  }

  @NotNull
  @Contract("null, _ -> fail")
  public NBTTagList getListOrPresent(String key, @Nullable Supplier<NBTTagList> present) throws ClassCastException  {
    return findOrPresent(NBTType.LIST, key, present);
  }

  @NotNull
  @Contract("null -> fail")
  public NBTTagList getListOrPresent(String key) throws ClassCastException  {
    return getListOrPresent(key, null);
  }

  @NotNull
  @Contract("null -> fail")
  public NBTTagCompound getCompound(String key) throws NoSuchElementException, ClassCastException  {
    return find(key, NBTTagCompound.class, false);
  }

  @Nullable
  @Contract("null -> fail")
  public NBTTagCompound getCompoundOrNull(String key) throws NoSuchElementException, ClassCastException  {
    return find(key, NBTTagCompound.class, true);
  }

  @NotNull
  @Contract("null, _ -> fail")
  public NBTTagCompound getCompoundOrPresent(String key, @Nullable Supplier<NBTTagCompound> present) throws ClassCastException  {
    return findOrPresent(NBTType.COMPOUND, key, present);
  }

  @NotNull
  @Contract("null -> fail")
  public NBTTagCompound getCompoundOrPresent(String key) throws ClassCastException  {
    return getCompoundOrPresent(key, null);
  }

  @Contract("null -> fail")
  public int @NotNull [] getIntArray(String key) throws NoSuchElementException, ClassCastException  {
    NBTTagIntArray value = find(key, NBTTagIntArray.class, false);
    return value.value;
  }

  @Contract("null -> fail")
  public int @Nullable [] getIntArrayOrNull(String key) throws NoSuchElementException, ClassCastException  {
    NBTTagIntArray value = find(key, NBTTagIntArray.class, true);
    return value != null ? value.value : null;
  }

  @Contract("null, _ -> fail")
  public int @NotNull [] getIntArrayOrPresent(String key, @Nullable Supplier<int[]> present) throws ClassCastException  {
    NBTTagIntArray value = findOrPresent(NBTType.INT_ARRAY, key, present);
    return value.value;
  }

  @Contract("null -> fail")
  public int @NotNull [] getIntArrayOrPresent(String key) throws ClassCastException  {
    return getIntArrayOrPresent(key, null);
  }

  @Contract("null -> fail")
  public long @NotNull [] getLongArray(String key) throws NoSuchElementException, ClassCastException  {
    NBTTagLongArray value = find(key, NBTTagLongArray.class, false);
    return value.value;
  }

  @Contract("null -> fail")
  public long @Nullable [] getLongArrayOrNull(String key) throws NoSuchElementException, ClassCastException  {
    NBTTagLongArray value = find(key, NBTTagLongArray.class, true);
    return value != null ? value.value : null;
  }

  @Contract("null, _ -> fail")
  public long @NotNull [] getLongArrayOrPresent(String key, @Nullable Supplier<long[]> present) throws ClassCastException  {
    NBTTagLongArray value = findOrPresent(NBTType.LONG_ARRAY, key, present);
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
  public boolean hasValue(@Nullable NBTBase<?> value) {
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
    if (!(value instanceof NBTBase)) return false;
    return hasValue((NBTBase<?>) value);
  }

  @Override
  @Contract("null -> null")
  public NBTBase<?> get(@Nullable Object key) {
    if (!(key instanceof String)) return null;
    return value.get(key);
  }

  @Override
  public boolean isEmpty() {
    return value.isEmpty();
  }

  @NotNull
  @Override
  public Set<Entry<String, NBTBase<?>>> entrySet() {
    return value.entrySet();
  }

  @NotNull
  @Override
  public Set<String> keySet() {
    return value.keySet();
  }

  @NotNull
  @Override
  public Collection<NBTBase<?>> values() {
    return value.values();
  }

  @Nullable
  @Override
  public NBTBase<?> put(String key, NBTBase<?> value) {
    return this.value.put(key, value);
  }

  @Override
  @Contract("null -> null")
  public NBTBase<?> remove(@Nullable Object key) {
    if (!(key instanceof String)) return null;
    return value.remove(key);
  }

  @Override
  public void putAll(@NotNull Map<? extends String, ? extends NBTBase<?>> m) {
    value.putAll(m);
  }

  @Override
  public void clear() {
    value.clear();
  }
}
