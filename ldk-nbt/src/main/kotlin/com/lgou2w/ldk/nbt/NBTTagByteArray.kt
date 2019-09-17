/*
 * Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>
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

import java.io.DataInput
import java.io.DataOutput
import java.util.Arrays

/**
 * ## NBTTagByteArray (字节数组 NBT 标签)
 *
 * @see [ByteArray]
 * @author lgou2w
 */
class NBTTagByteArray : NBTBase<ByteArray> {

    @JvmOverloads
    constructor(name: String = "", value: ByteArray = ByteArray(0)) : super(name, value)
    constructor(value: ByteArray = ByteArray(0)) : super("", value)

    override val type = NBTType.TAG_BYTE_ARRAY

    override var value : ByteArray
        get() {
            val value = ByteArray(value0.size)
            for ((i, el) in value0.withIndex())
                value[i] = el
            return value
        }
        set(value) {
            val value0 = ByteArray(value.size)
            for ((i, el) in value.withIndex())
                value0[i] = el
            super.value0 = value0
        }

    override fun read(input: DataInput) {
        val value = ByteArray(input.readInt())
        input.readFully(value)
        super.value0 = value
    }

    override fun write(output: DataOutput) {
        output.writeInt(value0.size)
        output.write(value)
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other) && Arrays.equals((other as NBTTagByteArray).value0, value0)
    }

    override fun toString(): String {
        return "NBTTagByteArray(value=${value0.joinToString(",", "[", "]")})"
    }

    override fun clone(): NBTTagByteArray {
        return NBTTagByteArray(name, value) // clone
    }

    override fun toJson(): String {
        return value0.joinToString(",", "[", "]")
    }

    override fun toMojangson(): String {
        return value0.joinToString("B,", "[B; ", "B]")
    }

    override fun toMojangsonWithColor(): String {
        return value0.joinToString("§cB§r, §6", "[§cB§r; §6", "§cB§r]")
    }
}
