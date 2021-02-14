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

public class NBTTagList extends NBTBase<List<NBTBase<?>>> implements List<NBTBase<?>> {
  private @NotNull NBTType elementType = NBTType.END;

  @Contract("null, _ -> fail; _, null -> fail")
  public NBTTagList(String name, List<NBTBase<?>> value) {
    super(name, new ArrayList<>(value));
    for (NBTBase<?> el : value) checkElement(el);
  }

  @Contract("null -> fail")
  public NBTTagList(String name) {
    this(name, new ArrayList<>());
  }

  @Contract("null -> fail")
  public NBTTagList(List<NBTBase<?>> value) {
    this("", value);
  }

  public NBTTagList() {
    this("", new ArrayList<>());
  }

  private void checkElement(@NotNull NBTBase<?> el) {
    if (elementType == NBTType.END) {
      elementType = el.getType();
    } else if (el.getType() != elementType) {
      throw new IllegalArgumentException("The type of element value that does not match, should be: " + elementType);
    }
  }

  @NotNull
  public NBTType getElementType() {
    return elementType;
  }

  @Override
  @NotNull
  public NBTType getType() {
    return NBTType.LIST;
  }

  @Override
  public void setValue(List<NBTBase<?>> value) {
    if (value == null) throw new NullPointerException("value");
    for (NBTBase<?> el : value) checkElement(el);
    super.setValue(new ArrayList<>(value));
  }

  @Override
  public void read(@NotNull DataInput input) throws IOException {
    NBTType elementType = NBTType.fromId(input.readUnsignedByte());
    if (elementType == null) elementType = NBTType.END;
    int length = input.readInt();
    this.elementType = elementType;
    value = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      NBTBase<?> element = NBTType.create(elementType);
      element.read(input);
      value.add(element); // skip check
    }
  }

  @Override
  public void write(@NotNull DataOutput output) throws IOException {
    output.writeByte(isEmpty() ? 0 : elementType.getId());
    output.writeInt(size());
    for (NBTBase<?> el : value) el.write(output);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NBTTagList list = (NBTTagList) o;
    if (elementType != list.elementType) return false;
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), elementType);
  }

  @Override
  public String toString() {
    return "NBTTagList{" +
      "value=" + value +
      ", elementType=" + elementType +
      '}';
  }

  @Override
  protected void toMojangsonBuilder(@NotNull StringBuilder builder, boolean color) {
    builder.append('[');
    int len = value.size(), i = 0;
    for (NBTBase<?> element : value) {
      if (i >= 1 && i < len) builder.append(!color ? "," : ", ");
      element.toMojangsonBuilder(builder, color);
      i++;
    }
    builder.append(']');
  }

  @Override
  @NotNull
  public NBTTagList clone() {
    List<NBTBase<?>> newValue = new ArrayList<>(value.size());
    for (NBTBase<?> el : value) newValue.add(el.clone());
    return new NBTTagList(name, newValue);
  }

  /// Extended

  @Contract("null -> fail")
  public boolean addByte(byte... values) {
    if (values == null) throw new NullPointerException("values");
    List<NBTTagByte> c = new ArrayList<>(values.length);
    for (byte element : values) c.add(new NBTTagByte(element));
    return addAll(c);
  }

  @Contract("null -> fail")
  public boolean addByte(int... values) {
    if (values == null) throw new NullPointerException("values");
    List<NBTTagByte> c = new ArrayList<>(values.length);
    for (int element : values) c.add(new NBTTagByte((byte) element));
    return addAll(c);
  }

  @Contract("null -> fail")
  public boolean addShort(short... values) {
    if (values == null) throw new NullPointerException("values");
    List<NBTTagShort> c = new ArrayList<>(values.length);
    for (short element : values) c.add(new NBTTagShort(element));
    return addAll(c);
  }

  @Contract("null -> fail")
  public boolean addShort(int... values) {
    if (values == null) throw new NullPointerException("values");
    List<NBTTagShort> c = new ArrayList<>(values.length);
    for (int element : values) c.add(new NBTTagShort((short) element));
    return addAll(c);
  }

  @Contract("null -> fail")
  public boolean addInt(int... values) {
    if (values == null) throw new NullPointerException("values");
    List<NBTTagInt> c = new ArrayList<>(values.length);
    for (int element : values) c.add(new NBTTagInt(element));
    return addAll(c);
  }

  @Contract("null -> fail")
  public boolean addLong(long... values) {
    if (values == null) throw new NullPointerException("values");
    List<NBTTagLong> c = new ArrayList<>(values.length);
    for (long element : values) c.add(new NBTTagLong(element));
    return addAll(c);
  }

  @Contract("null -> fail")
  public boolean addFloat(float... values) {
    if (values == null) throw new NullPointerException("values");
    List<NBTTagFloat> c = new ArrayList<>(values.length);
    for (float element : values) c.add(new NBTTagFloat(element));
    return addAll(c);
  }

  @Contract("null -> fail")
  public boolean addDouble(double... values) {
    if (values == null) throw new NullPointerException("values");
    List<NBTTagDouble> c = new ArrayList<>(values.length);
    for (double element : values) c.add(new NBTTagDouble(element));
    return addAll(c);
  }

  @Contract("null -> fail")
  public boolean addByteArray(byte[]... values) {
    if (values == null) throw new NullPointerException("values");
    List<NBTTagByteArray> c = new ArrayList<>(values.length);
    for (byte[] element : values) c.add(new NBTTagByteArray(element));
    return addAll(c);
  }

  @Contract("null -> fail")
  public boolean addString(String... values) {
    if (values == null) throw new NullPointerException("values");
    List<NBTTagString> c = new ArrayList<>(values.length);
    for (String element : values) c.add(new NBTTagString("", element));
    return addAll(c);
  }

  @Contract("null -> fail")
  public boolean addList(NBTTagList... values) {
    return addAll(Arrays.asList(values));
  }

  @Contract("null -> fail")
  public boolean addCompound(NBTTagCompound... values) {
    return addAll(Arrays.asList(values));
  }

  @Contract("null -> fail")
  public boolean addIntArray(int[]... values) {
    if (values == null) throw new NullPointerException("values");
    List<NBTTagIntArray> c = new ArrayList<>(values.length);
    for (int[] element : values) c.add(new NBTTagIntArray(element));
    return addAll(c);
  }

  @Contract("null -> fail")
  public boolean addLongArray(long[]... values) {
    if (values == null) throw new NullPointerException("values");
    List<NBTTagLongArray> c = new ArrayList<>(values.length);
    for (long[] element : values) c.add(new NBTTagLongArray(element));
    return addAll(c);
  }

  @Contract("null -> fail")
  public boolean addBoolean(boolean... values) {
    if (values == null) throw new NullPointerException("values");
    List<NBTTagByte> c = new ArrayList<>(values.length);
    for (boolean element : values) c.add(new NBTTagByte((byte) (element ? 1 : 0)));
    return addAll(c);
  }

  /// List

  @NotNull
  @Override
  public Iterator<NBTBase<?>> iterator() {
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
  public NBTBase<?> get(int index) {
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
  public boolean add(NBTBase<?> element) {
    checkElement(element);
    return value.add(element);
  }

  @Override
  public void add(int index, NBTBase<?> element) {
    checkElement(element);
    value.add(index, element);
  }

  @Override
  public boolean addAll(int index, @NotNull Collection<? extends NBTBase<?>> c) {
    for (NBTBase<?> el : c) checkElement(el);
    return value.addAll(index, c);
  }

  @Override
  public boolean addAll(@NotNull Collection<? extends NBTBase<?>> c) {
    for (NBTBase<?> el : c) checkElement(el);
    return value.addAll(c);
  }

  @Override
  public void clear() {
    value.clear();
  }

  @NotNull
  @Override
  public ListIterator<NBTBase<?>> listIterator() {
    return value.listIterator();
  }

  @NotNull
  @Override
  public ListIterator<NBTBase<?>> listIterator(int index) {
    return value.listIterator(index);
  }

  @Override
  public NBTBase<?> remove(int index) {
    return value.remove(index);
  }

  @Override
  public boolean remove(Object o) {
    if (o instanceof NBTBase) checkElement((NBTBase<?>) o);
    return value.remove(o);
  }

  @Override
  public boolean removeAll(@NotNull Collection<?> c) {
    for (Object el : c) {
      if (el instanceof NBTBase) {
        checkElement((NBTBase<?>) el);
      }
    }
    return value.removeAll(c);
  }

  @Override
  public boolean retainAll(@NotNull Collection<?> c) {
    for (Object el : c) {
      if (el instanceof NBTBase) {
        checkElement((NBTBase<?>) el);
      }
    }
    return value.retainAll(c);
  }

  @Override
  public NBTBase<?> set(int index, NBTBase<?> element) {
    checkElement(element);
    return value.set(index, element);
  }

  @NotNull
  @Override
  public List<NBTBase<?>> subList(int fromIndex, int toIndex) {
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
