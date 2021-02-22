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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class NBTStreams {

  private NBTStreams() { }

  @NotNull
  @Contract("null -> fail")
  public static NBTMetadata read(InputStream input) throws IOException {
    Objects.requireNonNull(input, "input");
    return read((DataInput) new DataInputStream(input));
  }

  @NotNull
  @Contract("null -> fail")
  public static NBTMetadata read(DataInput input) throws IOException {
    Objects.requireNonNull(input, "input");
    TagType type = TagType.fromId(input.readUnsignedByte());
    if (type == null || type == TagType.END) return NBTMetadata.END;
    String name = input.readUTF();
    BaseTag<?> base = TagType.create(type);
    base.read(input);
    return NBTMetadata.of(name, base);
  }

  @Contract("null, _ -> fail; _, null -> fail")
  public static void write(OutputStream output, NBTMetadata metadata) throws IOException {
    Objects.requireNonNull(output, "output");
    write((DataOutput) new DataOutputStream(output), metadata);
  }

  @Contract("null, _ -> fail; _, null -> fail;")
  public static void write(DataOutput output, NBTMetadata metadata) throws IOException {
    Objects.requireNonNull(output, "output");
    Objects.requireNonNull(metadata, "metadata");
    BaseTag<?> value = metadata.getValue();
    output.writeByte(value.getType().getId());
    if (!metadata.isEndType()) {
      output.writeUTF(metadata.getName());
      value.write(output);
    }
  }

  @NotNull
  @Contract("null -> fail")
  public static NBTMetadata readBase64(String encoded) throws IllegalArgumentException, IOException {
    Objects.requireNonNull(encoded, "encoded");
    byte[] bytes = Base64.getDecoder().decode(encoded);
    return read(new ByteArrayInputStream(bytes));
  }

  @NotNull
  @Contract("null -> fail")
  public static String writeBase64(NBTMetadata metadata) throws IOException {
    Objects.requireNonNull(metadata, "metadata");
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    write(output, metadata);
    return Base64.getEncoder().encodeToString(output.toByteArray());
  }

  @NotNull
  @Contract("null, _ -> fail")
  public static NBTMetadata readFile(File file, boolean decompress) throws IOException {
    Objects.requireNonNull(file, "file");
    if (!file.exists() || file.isDirectory()) throw new FileNotFoundException(
      "File does not exist or is a directory: " + file.getAbsolutePath());
    try (InputStream input = decompress
      ? new GZIPInputStream(new FileInputStream(file))
      : new FileInputStream(file)) {
      return read(input);
    }
  }

  @Contract("null, _, _ -> fail; _, null, _ -> fail")
  public static void writeFile(NBTMetadata metadata, File file, boolean compress) throws IOException {
    Objects.requireNonNull(metadata, "metadata");
    Objects.requireNonNull(file, "file");
    try (OutputStream output = compress
      ? new GZIPOutputStream(new FileOutputStream(file))
      : new FileOutputStream(file)) {
      write(output, metadata);
    }
  }
}
