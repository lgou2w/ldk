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

/**
 * ## NBTTagNumber (数字 NBT 标签)
 *
 * @see [NBTTagByte]
 * @see [NBTTagShort]
 * @see [NBTTagInt]
 * @see [NBTTagLong]
 * @see [NBTTagFloat]
 * @see [NBTTagDouble]
 * @author lgou2w
 */
abstract class NBTTagNumber<T: Number>(
        name: String,
        value: T
) : NBTBase<T>(name, value) {

    /**
     * @see [Number.toDouble]
     */
    fun toDouble(): Double
            = value.toDouble()

    /**
     * @see [Number.toFloat]
     */
    fun toFloat(): Float
            = value.toFloat()

    /**
     * @see [Number.toLong]
     */
    fun toLong(): Long
            = value.toLong()

    /**
     * @see [Number.toInt]
     */
    fun toInt(): Int
            = value.toInt()

    /**
     * @see [Number.toChar]
     */
    fun toChar(): Char
            = value.toChar()

    /**
     * @see [Number.toShort]
     */
    fun toShort(): Short
            = value.toShort()

    /**
     * @see [Number.toByte]
     */
    fun toByte(): Byte
            = value.toByte()

    override fun equals(other: Any?): Boolean {
        if (super.equals(other))
            return value == (other as NBTBase<*>).value
        return false
    }
}
