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

import java.io.DataInput
import java.io.DataOutput

/**
 * ## NBTTagString (字符串 NBT 标签)
 *
 * @see [String]
 * @author lgou2w
 */
class NBTTagString : NBTBase<String> {

    constructor(name: String = "", value: String = "") : super(name, value)
    constructor(value: String = "") : super("", value)
    constructor() : super("", "")

    override val type: NBTType
        get() = NBTType.TAG_STRING

    override fun read(input: DataInput) {
        value = input.readUTF()
    }

    override fun write(output: DataOutput) {
        output.writeUTF(value)
    }

    override fun equals(other: Any?): Boolean {
        if (super.equals(other))
            return value == (other as NBTBase<*>).value
        return false
    }

    override fun toString(): String {
        return "NBTTagString(value=${value.replace("\"", "\\\"")})"
    }

    override fun clone(): NBTTagString {
        return NBTTagString(name, value)
    }

    override fun toJson(): String {
        return "\"" + value.replace("\"", "\\\"") + "\""
    }

    override fun toMojangson(): String {
        return "\"" + value.replace("\"", "\\\"") + "\""
    }

    override fun toMojangsonWithColor(): String {
        return "\"§a" + value.replace("\"", "\\\"") + "§r\""
    }
}
