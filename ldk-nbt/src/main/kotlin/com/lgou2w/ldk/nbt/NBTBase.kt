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

abstract class NBTBase<T>(
        override val name: String,
        protected var value0: T
) : NBT<T> {

    val typeId: Int
        get() = type.id

    override var value: T
        get() = value0
        set(value) { value0 = value }

    override fun hashCode(): Int {
        return typeId xor (value0?.hashCode() ?: 0)
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is NBTBase<*>)
            return other.name == name && other.type == type
        return false
    }

    override fun toJson(): String {
        return value0.toString()
    }

    override fun toMojangson(): String {
        return value0.toString() + type.mojangsonSuffix
    }

    abstract override fun toString(): String

    public abstract override fun clone(): NBTBase<*>
}
