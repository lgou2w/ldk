/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.nbt

import java.io.*
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * ## NBTStreams (NBT 流工具)
 *
 * * NBT I/O tool classes that read and write to streams, files, or Base64.
 * * 读取和写入到流、文件或 Base64 的 NBT I/O 工具类.
 *
 * @author lgou2w
 */
object NBTStreams {

    /**
     * * Read the NBT tag from the given [input] input stream.
     * * 从给定的 [input] 输入流中读取 NBT 标签.
     *
     * @param input Input stream
     * @param input 输入流
     * @throws IOException I/O
     */
    @JvmStatic
    @Throws(IOException::class)
    fun read(input: InputStream): NBTBase<*>? {
        return read(DataInputStream(input) as DataInput)
    }

    /**
     * * Read the NBT tag from the given [input] data input.
     * * 从给定的 [input] 数据输入中读取 NBT 标签.
     *
     * @param input Data input
     * @param input 数据输入
     * @throws IOException I/O
     */
    @JvmStatic
    @Throws(IOException::class)
    fun read(input: DataInput): NBTBase<*>? {
        val type = NBTType.fromId(input.readByte().toInt())
        if (type == null || type == NBTType.TAG_END)
            return NBTTagEnd.INSTANCE
        val name = input.readUTF()
        val tag = NBTType.createTag(type, name)
        tag.read(input)
        return tag
    }

    /**
     * * Write the given [nbt] NBT tag to the [output] output stream.
     * * 将给定 [nbt] NBT 标签写入到 [output] 输出流中.
     *
     * @param output Output stream
     * @param output 输出流
     * @param nbt NBT
     * @throws IOException I/O
     */
    @JvmStatic
    @Throws(IOException::class)
    fun write(output: OutputStream, nbt: NBTBase<*>) {
        write(DataOutputStream(output) as DataOutput, nbt)
    }

    /**
     * * Write the given [nbt] NBT tag to the [output] data output.
     * * 将给定 [nbt] NBT 标签写入到 [output] 数据输出中.
     *
     * @param output Data output
     * @param output 输入输出
     * @param nbt NBT
     * @throws IOException I/O
     */
    @JvmStatic
    @Throws(IOException::class)
    fun write(output: DataOutput, nbt: NBTBase<*>) {
        output.writeByte(nbt.typeId)
        if (nbt.type != NBTType.TAG_END) {
            output.writeUTF(nbt.name)
            nbt.write(output)
        }
    }

    /**
     * * Write the given [nbt] NBT tag in `Base64` format.
     * * 将给定的 [nbt] NBT 标签以 `Base64` 格式写出.
     *
     * @param nbt NBT
     * @throws IOException I/O
     */
    @JvmStatic
    @Throws(IOException::class)
    fun writeBase64(nbt: NBTBase<*>): String {
        val stream = ByteArrayOutputStream()
        write(stream, nbt)
        return Base64.getEncoder().encodeToString(stream.toByteArray())
    }

    /**
     * * Read the NBT tag from the given `Base64` [value].
     * * 从给定的 `Base64` 值 [value] 中读取 NBT 标签.
     *
     * @param value Base64 value
     * @throws IOException I/O
     */
    @JvmStatic
    @Throws(IOException::class)
    fun readBase64(value: String): NBTBase<*>? {
        val bytes = Base64.getDecoder().decode(value)
        val stream = ByteArrayInputStream(bytes)
        return read(stream)
    }

    /**
     * * Write the given [nbt] NBT tag in file.
     * * 将给定的 [nbt] NBT 标签以文件写出.
     *
     * @param nbt NBT
     * @param file The file to be written
     * @param file 要写出的文件
     * @param compress Whether to compress data with `GZip`
     * @param compress 是否使用 `GZip` 压缩数据
     * @throws IOException I/O
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    fun writeFile(nbt: NBTBase<*>, file: File, compress: Boolean = true) {
        var output: OutputStream? = null
        try {
            output = if (compress) GZIPOutputStream(FileOutputStream(file)) else FileOutputStream(file)
            write(output, nbt)
        } finally {
            output?.close()
        }
    }

    /**
     * * Read the NBT tag from the given [file].
     * * 从给定的文件 [file] 中读取 NBT 标签.
     *
     * @param file The file to be read
     * @param file 要读取的文件
     * @param decompress Whether to use `GZip` to decompress data
     * @param decompress 是否使用 `GZip` 解压缩数据
     * @throws IOException I/O
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    fun readFile(file: File, decompress: Boolean = true): NBTBase<*>? {
        if (!file.exists() || file.isDirectory)
            return null
        var input: InputStream? = null
        try {
            input = if (decompress) GZIPInputStream(FileInputStream(file)) else FileInputStream(file)
            return read(input)
        } finally {
            input?.close()
        }
    }
}
