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

abstract class NBTTagNumber<T: Number>(
        name: String,
        value: T
) : NBTBase<T>(name, value) {

    fun toDouble(): Double
            = value.toDouble()

    fun toFloat(): Float
            = value.toFloat()

    fun toLong(): Long
            = value.toLong()

    fun toInt(): Int
            = value.toInt()

    fun toChar(): Char
            = value.toChar()

    fun toShort(): Short
            = value.toShort()

    fun toByte(): Byte
            = value.toByte()

    override fun equals(other: Any?): Boolean {
        if (super.equals(other))
            return value == (other as NBTBase<*>).value
        return false
    }
}
