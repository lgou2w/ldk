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
 * ## NBTTagFloat (单精度浮点 NBT 标签)
 *
 * @see [Float]
 * @author lgou2w
 */
class NBTTagFloat : NBTTagNumber<Float> {

    @JvmOverloads
    constructor(name: String = "", value: Float = 0f) : super(name, value)
    constructor(value: Float = 0f) : super("", value)

    override val type: NBTType
        get() = NBTType.TAG_FLOAT

    override fun read(input: DataInput) {
        value = input.readFloat()
    }

    override fun write(output: DataOutput) {
        output.writeFloat(value)
    }

    override fun clone(): NBTTagFloat {
        return NBTTagFloat(name, value)
    }

    override fun toString(): String {
        return "NBTTagFloat(value=$value)"
    }
}
