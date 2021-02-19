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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class ListTag extends BaseTag<List<BaseTag<?>>> implements List<BaseTag<?>> {
  private @NotNull TagType elementType = TagType.END;

  @Contract("null -> fail")
  public ListTag(List<BaseTag<?>> value) {
    super(new ArrayList<>(value));
  }

  public ListTag() {
    this(new ArrayList<>());
  }

  private void checkElement(@NotNull BaseTag<?> el) {
    if (elementType == TagType.END) {
      elementType = el.getType();
    } else if (el.getType() != elementType) {
      throw new IllegalArgumentException("The type of element value that does not match, should be: " + elementType);
    }
  }

  @NotNull
  public TagType getElementType() {
    return elementType;
  }

  @Override
  @NotNull
  public TagType getType() {
    return TagType.LIST;
  }

  @Override
  public void setValue(List<BaseTag<?>> value) {
    if (value == null) throw new NullPointerException("value");
    for (BaseTag<?> el : value) checkElement(el);
    super.setValue(new ArrayList<>(value));
  }

  @Override
  public void read(@NotNull DataInput input) throws IOException {
    TagType elementType = TagType.fromId(input.readUnsignedByte());
    if (elementType == null) elementType = TagType.END;
    int length = input.readInt();
    this.elementType = elementType;
    value = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      BaseTag<?> element = TagType.create(elementType);
      element.read(input);
      value.add(element); // skip check
    }
  }

  @Override
  public void write(@NotNull DataOutput output) throws IOException {
    output.writeByte(isEmpty() ? 0 : elementType.getId());
    output.writeInt(size());
    for (BaseTag<?> el : value) el.write(output);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ListTag list = (ListTag) o;
    if (elementType != list.elementType) return false;
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), elementType);
  }

  @Override
  public String toString() {
    return "ListTag{" +
      "value=" + value +
      ", elementType=" + elementType +
      '}';
  }

  @Override
  protected void toMojangsonBuilder(@NotNull StringBuilder builder, boolean color) {
    builder.append('[');
    int len = value.size(), i = 0;
    for (BaseTag<?> element : value) {
      if (i >= 1 && i < len) builder.append(!color ? "," : ", ");
      element.toMojangsonBuilder(builder, color);
      i++;
    }
    builder.append(']');
  }

  @Override
  @NotNull
  public ListTag clone() {
    List<BaseTag<?>> newValue = new ArrayList<>(value.size());
    for (BaseTag<?> el : value) newValue.add(el.clone());
    return new ListTag(newValue);
  }

  /// Extended

  @Contract("null -> fail; !null -> this")
  public ListTag addByte(byte... values) {
    if (values == null) throw new NullPointerException("values");
    List<ByteTag> c = new ArrayList<>(values.length);
    for (byte element : values) c.add(new ByteTag(element));
    addAll(c);
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addByte(int... values) {
    if (values == null) throw new NullPointerException("values");
    List<ByteTag> c = new ArrayList<>(values.length);
    for (int element : values) c.add(new ByteTag((byte) element));
    addAll(c);
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addShort(short... values) {
    if (values == null) throw new NullPointerException("values");
    List<ShortTag> c = new ArrayList<>(values.length);
    for (short element : values) c.add(new ShortTag(element));
    addAll(c);
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addShort(int... values) {
    if (values == null) throw new NullPointerException("values");
    List<ShortTag> c = new ArrayList<>(values.length);
    for (int element : values) c.add(new ShortTag((short) element));
    addAll(c);
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addInt(int... values) {
    if (values == null) throw new NullPointerException("values");
    List<IntTag> c = new ArrayList<>(values.length);
    for (int element : values) c.add(new IntTag(element));
    addAll(c);
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addLong(long... values) {
    if (values == null) throw new NullPointerException("values");
    List<LongTag> c = new ArrayList<>(values.length);
    for (long element : values) c.add(new LongTag(element));
    addAll(c);
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addFloat(float... values) {
    if (values == null) throw new NullPointerException("values");
    List<FloatTag> c = new ArrayList<>(values.length);
    for (float element : values) c.add(new FloatTag(element));
    addAll(c);
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addDouble(double... values) {
    if (values == null) throw new NullPointerException("values");
    List<DoubleTag> c = new ArrayList<>(values.length);
    for (double element : values) c.add(new DoubleTag(element));
    addAll(c);
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addByteArray(byte[]... values) {
    if (values == null) throw new NullPointerException("values");
    List<ByteArrayTag> c = new ArrayList<>(values.length);
    for (byte[] element : values) c.add(new ByteArrayTag(element));
    addAll(c);
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addString(String... values) {
    if (values == null) throw new NullPointerException("values");
    List<StringTag> c = new ArrayList<>(values.length);
    for (String element : values) c.add(new StringTag(element));
    addAll(c);
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addList(ListTag... values) {
    if (values == null) throw new NullPointerException("values");
    addAll(Arrays.asList(values));
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addCompound(CompoundTag... values) {
    if (values == null) throw new NullPointerException("values");
    addAll(Arrays.asList(values));
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addIntArray(int[]... values) {
    if (values == null) throw new NullPointerException("values");
    List<IntArrayTag> c = new ArrayList<>(values.length);
    for (int[] element : values) c.add(new IntArrayTag(element));
    addAll(c);
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addLongArray(long[]... values) {
    if (values == null) throw new NullPointerException("values");
    List<LongArrayTag> c = new ArrayList<>(values.length);
    for (long[] element : values) c.add(new LongArrayTag(element));
    addAll(c);
    return this;
  }

  @Contract("null -> fail; !null -> this")
  public ListTag addBoolean(boolean... values) {
    if (values == null) throw new NullPointerException("values");
    List<ByteTag> c = new ArrayList<>(values.length);
    for (boolean element : values) c.add(new ByteTag((byte) (element ? 1 : 0)));
    addAll(c);
    return this;
  }

  /// List

  @NotNull
  @Override
  public Iterator<BaseTag<?>> iterator() {
    return value.iterator();
  }

  @Override
  public int size() {
    return value.size();
  }

  @Override
  public boolean contains(Object o) {
    return value.contains(o);
  }

  @Override
  public boolean containsAll(@NotNull Collection<?> c) {
    return value.containsAll(c);
  }

  @Override
  public BaseTag<?> get(int index) {
    return value.get(index);
  }

  @Override
  public int indexOf(Object o) {
    return value.indexOf(o);
  }

  @Override
  public boolean isEmpty() {
    return value.isEmpty();
  }

  @Override
  public int lastIndexOf(Object o) {
    return value.lastIndexOf(o);
  }

  @Override
  public boolean add(BaseTag<?> element) {
    checkElement(element);
    return value.add(element);
  }

  @Override
  public void add(int index, BaseTag<?> element) {
    checkElement(element);
    value.add(index, element);
  }

  @Override
  public boolean addAll(int index, @NotNull Collection<? extends BaseTag<?>> c) {
    for (BaseTag<?> el : c) checkElement(el);
    return value.addAll(index, c);
  }

  @Override
  public boolean addAll(@NotNull Collection<? extends BaseTag<?>> c) {
    for (BaseTag<?> el : c) checkElement(el);
    return value.addAll(c);
  }

  @Override
  public void clear() {
    value.clear();
  }

  @NotNull
  @Override
  public ListIterator<BaseTag<?>> listIterator() {
    return value.listIterator();
  }

  @NotNull
  @Override
  public ListIterator<BaseTag<?>> listIterator(int index) {
    return value.listIterator(index);
  }

  @Override
  public BaseTag<?> remove(int index) {
    return value.remove(index);
  }

  @Override
  public boolean remove(Object o) {
    if (o instanceof BaseTag) checkElement((BaseTag<?>) o);
    return value.remove(o);
  }

  @Override
  public boolean removeAll(@NotNull Collection<?> c) {
    for (Object el : c) {
      if (el instanceof BaseTag) {
        checkElement((BaseTag<?>) el);
      }
    }
    return value.removeAll(c);
  }

  @Override
  public boolean retainAll(@NotNull Collection<?> c) {
    for (Object el : c) {
      if (el instanceof BaseTag) {
        checkElement((BaseTag<?>) el);
      }
    }
    return value.retainAll(c);
  }

  @Override
  public BaseTag<?> set(int index, BaseTag<?> element) {
    checkElement(element);
    return value.set(index, element);
  }

  @NotNull
  @Override
  public List<BaseTag<?>> subList(int fromIndex, int toIndex) {
    return value.subList(fromIndex, toIndex);
  }

  @NotNull
  @Override
  public Object[] toArray() {
    return value.toArray();
  }

  @NotNull
  @Override
  public <T> T[] toArray(@NotNull T[] a) {
    return value.toArray(a);
  }
}
