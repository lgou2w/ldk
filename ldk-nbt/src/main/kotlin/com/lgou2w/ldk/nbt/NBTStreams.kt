/*
 * Copyright (C) 2018 The lgou2w (lgou2w@hotmail.com)
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

import com.lgou2w.ldk.common.Enums
import java.io.*
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object NBTStreams {

    @Throws(IOException::class)
    fun read(input: InputStream): NBTBase<*>? {
        return read(DataInputStream(input) as DataInput)
    }

    @Throws(IOException::class)
    fun read(input: DataInput): NBTBase<*>? {
        val type = Enums.ofValuable(NBTType::class.java, input.readByte().toInt())
        if (type == null || type == NBTType.TAG_END)
            return NBTTagEnd.INSTANCE
        val name = input.readUTF()
        val tag = NBTType.createTag(type, name)
        tag.read(input)
        return tag
    }

    @Throws(IOException::class)
    fun write(output: OutputStream, nbt: NBTBase<*>) {
        write(DataOutputStream(output) as DataOutput, nbt)
    }

    @Throws(IOException::class)
    fun write(output: DataOutput, nbt: NBTBase<*>) {
        output.writeByte(nbt.typeId)
        if (nbt.type != NBTType.TAG_END) {
            output.writeUTF(nbt.name)
            nbt.write(output)
        }
    }

    @Throws(IOException::class)
    fun writeBase64(nbt: NBTBase<*>): String {
        val stream = ByteArrayOutputStream()
        write(stream, nbt)
        return Base64.getEncoder().encodeToString(stream.toByteArray())
    }

    @Throws(IOException::class)
    fun readBase64(value: String): NBTBase<*>? {
        val bytes = Base64.getDecoder().decode(value)
        val stream = ByteArrayInputStream(bytes)
        return read(stream)
    }

    @JvmOverloads
    @Throws(IOException::class)
    fun writeFile(nbt: NBTBase<*>, file: File, compress: Boolean = true) {
        val stream: FileOutputStream? = null
        var output: OutputStream? = null
        var swallow = true
        try {
            output = if (compress) GZIPOutputStream(FileOutputStream(file)) else FileOutputStream(file)
            write(output, nbt)
            swallow = false
        } finally {
            if (output != null) output.swallowClose(swallow)
            else if (stream != null) stream.swallowClose(swallow)
        }
    }

    @JvmOverloads
    @Throws(IOException::class)
    fun readFile(file: File, compress: Boolean = true): NBTBase<*>? {
        if (!file.exists() || file.isDirectory)
            return null
        val stream: FileInputStream? = null
        var input: InputStream? = null
        var swallow = true
        try {
            input = if (compress) GZIPInputStream(FileInputStream(file)) else FileInputStream(file)
            val result = read(input)
            swallow = false
            return result
        } finally {
            if (input != null) input.swallowClose(swallow)
            else if (stream != null) stream.swallowClose(swallow)
        }
    }

    @Throws(IOException::class)
    private fun Closeable?.swallowClose(swallow: Boolean) {
        if (this == null)
            return
        try {
            close()
        } catch (e: IOException) {
            if (!swallow)
                throw e
        }
    }
}
