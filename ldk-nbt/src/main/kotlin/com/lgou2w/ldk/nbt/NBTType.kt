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

import com.lgou2w.ldk.common.Valuable

enum class NBTType(
        val id: Int,
        val primitive: Class<*>,
        val reference: Class<*>,
        val wrapped: Class<*>,
        val mojangsonSuffix: String? = null
) : Valuable<Int> {

    TAG_END(0, java.lang.Void.TYPE, java.lang.Void::class.java, NBTTagEnd::class.java),
    TAG_BYTE(1, java.lang.Byte.TYPE, java.lang.Byte::class.java, NBTTagByte::class.java, "b"),
    TAG_SHORT(2, java.lang.Short.TYPE, java.lang.Short::class.java, NBTTagShort::class.java, "s"),
    TAG_INT(3, java.lang.Integer.TYPE, java.lang.Integer::class.java, NBTTagInt::class.java, ""),
    TAG_LONG(4, java.lang.Long.TYPE, java.lang.Long::class.java, NBTTagLong::class.java, "L"),
    TAG_FLOAT(5, java.lang.Float.TYPE, java.lang.Float::class.java, NBTTagFloat::class.java, "f"),
    TAG_DOUBLE(6, java.lang.Double.TYPE, java.lang.Double::class.java, NBTTagDouble::class.java, "d"),
    TAG_BYTE_ARRAY(7, ByteArray::class.java, ByteArray::class.java, NBTTagByteArray::class.java),
    TAG_STRING(8, java.lang.String::class.java, java.lang.String::class.java, NBTTagString::class.java),
    TAG_LIST(9, java.util.List::class.java, java.util.List::class.java, NBTTagList::class.java),
    TAG_COMPOUND(10, java.util.Map::class.java, java.util.Map::class.java, NBTTagCompound::class.java),
    TAG_INT_ARRAY(11, IntArray::class.java, IntArray::class.java, NBTTagIntArray::class.java),
    ;

    /**
     * @see [NBTTagByte]
     * @see [NBTTagShort]
     * @see [NBTTagInt]
     * @see [NBTTagLong]
     * @see [NBTTagFloat]
     * @see [NBTTagDouble]
     */
    fun isNumber(): Boolean
            = this == TAG_BYTE ||
            this == TAG_SHORT ||
            this == TAG_INT ||
            this == TAG_LONG ||
            this == TAG_FLOAT ||
            this == TAG_DOUBLE

    /**
     * @see [NBTTagList]
     * @see [NBTTagCompound]
     */
    fun isWrapper() : Boolean
            = this == TAG_LIST || this == TAG_COMPOUND

    override val value: Int
        get() = id

    companion object {

        private val ID_MAP: MutableMap<Int, NBTType> = HashMap()
        private val CLASS_MAP: MutableMap<Class<*>, NBTType> = HashMap()

        init {
            values().forEach {
                ID_MAP[it.id] = it
                CLASS_MAP[it.primitive] = it
                CLASS_MAP[it.reference] = it
                CLASS_MAP[it.wrapped] = it
            }
        }

        fun fromId(id: Int): NBTType? {
            return ID_MAP[id]
        }

        fun fromClass(clazz: Class<*>): NBTType? {
            return CLASS_MAP[clazz]
        }

        @JvmOverloads
        fun createTag(type: NBTType, name: String = ""): NBTBase<*> {
            return when (type) {
                TAG_END -> NBTTagEnd.INSTANCE
                TAG_BYTE -> NBTTagByte(name)
                TAG_SHORT -> NBTTagShort(name)
                TAG_INT -> NBTTagInt(name)
                TAG_LONG -> NBTTagLong(name)
                TAG_FLOAT -> NBTTagFloat(name)
                TAG_DOUBLE -> NBTTagDouble(name)
                TAG_BYTE_ARRAY -> NBTTagByteArray(name)
                TAG_STRING -> NBTTagString(name = name)
                TAG_LIST -> NBTTagList<NBTBase<*>>(name)
                TAG_COMPOUND -> NBTTagCompound(name)
                TAG_INT_ARRAY -> NBTTagIntArray(name)
            }
        }
    }
}
