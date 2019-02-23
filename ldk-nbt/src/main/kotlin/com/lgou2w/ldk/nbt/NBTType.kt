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

/**
 * ## NBTType (NBT 标签类型)
 *
 * * Enumeration of the tag type of the Minecraft NBT Format.
 * * 枚举 Minecraft NBT 格式的标签类型.
 *
 * @author lgou2w
 */
enum class NBTType(
        /**
         * * Tag type Id.
         * * 标签类型的 Id.
         */
        val id: Int,
        /**
         * * The primitive type of tag type.
         * * 标签类型的基本类型.
         */
        val primitive: Class<*>,
        /**
         * * The reference type of tag type.
         * * 标签类型的引用类型.
         */
        val reference: Class<*>,
        /**
         * * The wrapped type of tag type.
         * * 标签类型的包装类型.
         */
        val wrapped: Class<out NBTBase<*>>,
        /**
         * * A tag type with a specific suffix.
         * * 具有特定后缀的标签类型.
         *
         * @see [NBT.toMojangson]
         */
        val mojangsonSuffix: String? = null
) {

    /**
     * * TAG_END
     *      * Primitive = `void`
     *      * Reference = `java.lang.Void.class`
     *      * Wrapped = `NBTTagEnd.class`
     *
     * @see [NBTTagEnd]
     */
    TAG_END(0, java.lang.Void.TYPE, java.lang.Void::class.java, NBTTagEnd::class.java),
    /**
     * * TAG_BYTE
     *      * Primitive = `byte`
     *      * Reference = `java.lang.Byte.class`
     *      * Wrapped = `NBTTagByte.class`
     *      * Suffix = `b`
     *
     * @see [NBTTagByte]
     */
    TAG_BYTE(1, java.lang.Byte.TYPE, java.lang.Byte::class.java, NBTTagByte::class.java, "b"),
    /**
     * * TAG_SHORT
     *      * Primitive = `short`
     *      * Reference = `java.lang.Short.class`
     *      * Wrapped = `NBTTagShort.class`
     *      * Suffix = `s`
     *
     * @see [NBTTagShort]
     */
    TAG_SHORT(2, java.lang.Short.TYPE, java.lang.Short::class.java, NBTTagShort::class.java, "s"),
    /**
     * * TAG_INT
     *      * Primitive = `int`
     *      * Reference = `java.lang.Integer.class`
     *      * Wrapped = `NBTTagInt.class`
     *
     * @see [NBTTagInt]
     */
    TAG_INT(3, java.lang.Integer.TYPE, java.lang.Integer::class.java, NBTTagInt::class.java, ""),
    /**
     * * TAG_LONG
     *      * Primitive = `long`
     *      * Reference = `java.lang.Long.class`
     *      * Wrapped = `NBTTagLong.class`
     *      * Suffix = `L`
     *
     * @see [NBTTagLong]
     */
    TAG_LONG(4, java.lang.Long.TYPE, java.lang.Long::class.java, NBTTagLong::class.java, "L"),
    /**
     * * TAG_FLOAT
     *      * Primitive = `float`
     *      * Reference = `java.lang.Float.class`
     *      * Wrapped = `NBTTagFloat.class`
     *      * Suffix = `f`
     *
     * @see [NBTTagFloat]
     */
    TAG_FLOAT(5, java.lang.Float.TYPE, java.lang.Float::class.java, NBTTagFloat::class.java, "f"),
    /**
     * * TAG_DOUBLE
     *      * Primitive = `double`
     *      * Reference = `java.lang.Double.class`
     *      * Wrapped = `NBTTagDouble.class`
     *      * Suffix = `d`
     *
     * @see [NBTTagDouble]
     */
    TAG_DOUBLE(6, java.lang.Double.TYPE, java.lang.Double::class.java, NBTTagDouble::class.java, "d"),
    /**
     * * TAG_BYTE_ARRAY
     *      * Primitive = `byte [ ]`
     *      * Reference = `byte [ ]`
     *      * Wrapped = `NBTTagByteArray.class`
     *
     * @see [NBTTagByteArray]
     */
    TAG_BYTE_ARRAY(7, ByteArray::class.java, ByteArray::class.java, NBTTagByteArray::class.java),
    /**
     * * TAG_STRING
     *      * Primitive = `String`
     *      * Reference = `java.lang.String.class`
     *      * Wrapped = `NBTTagString.class`
     *
     * @see [NBTTagString]
     */
    TAG_STRING(8, java.lang.String::class.java, java.lang.String::class.java, NBTTagString::class.java),
    /**
     * * TAG_LIST
     *      * Primitive = `java.util.List.class`
     *      * Reference = `java.util.List.class`
     *      * Wrapped = `NBTTagList.class`
     *
     * @see [NBTTagList]
     */
    TAG_LIST(9, java.util.List::class.java, java.util.List::class.java, NBTTagList::class.java),
    /**
     * * TAG_COMPOUND
     *      * Primitive = `java.util.Map.class`
     *      * Reference = `java.util.Map.class`
     *      * Wrapped = `NBTTagCompound.class`
     *
     * @see [NBTTagCompound]
     */
    TAG_COMPOUND(10, java.util.Map::class.java, java.util.Map::class.java, NBTTagCompound::class.java),
    /**
     * * TAG_INT_ARRAY
     *      * Primitive = `int [ ]`
     *      * Reference = `int [ ]`
     *      * Wrapped = `NBTTagIntArray.class`
     *
     * @see [NBTTagIntArray]
     */
    TAG_INT_ARRAY(11, IntArray::class.java, IntArray::class.java, NBTTagIntArray::class.java),
    /**
     * * TAG_LONG_ARRAY
     *      * Primitive = `long [ ]`
     *      * Reference = `long [ ]`
     *      * Wrapped = `NBTTagLongArray.class`
     *
     * @see [NBTTagLongArray]
     * @since Minecraft 1.12
     */
    TAG_LONG_ARRAY(12, LongArray::class.java, LongArray::class.java, NBTTagLongArray::class.java),
    ;

    /**
     * * Gets whether this NBT type is a numeric type.
     * * 获取此 NBT 类型是否为数字类型.
     *
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
     * * Gets whether this NBT type is a wrapper type.
     * * 获取此 NBT 类型是否为包装类型.
     *
     * @see [NBTTagList]
     * @see [NBTTagCompound]
     */
    fun isWrapper() : Boolean
            = this == TAG_LIST || this == TAG_COMPOUND

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

        /**
         * * Find the corresponding NBT type from the given [id].
         * * 从给定的 [id] 中查找对应的 NBT 类型.
         *
         * @see [NBTType.id]
         * @param id Type id
         * @param id 类型 Id
         */
        @JvmStatic
        fun fromId(id: Int): NBTType? {
            return ID_MAP[id]
        }

        /**
         * * Find the corresponding NBT type from the given [clazz] type.
         * * 从给定的 [clazz] 类型中查找对应的 NBT 类型.
         *
         * @see [NBTType.primitive]
         * @see [NBTType.reference]
         * @see [NBTType.wrapped]
         * @param clazz Class
         * @param clazz 类
         */
        @JvmStatic
        fun fromClass(clazz: Class<*>): NBTType? {
            return CLASS_MAP[clazz]
        }

        /**
         * * Create a corresponding type of instance object with the given [type] NBT type named [name].
         * * 将给定的 [type] NBT 类型以 [name] 命名创建一个对应类型的实例对象.
         *
         * @param type NBT type
         * @param type NBT 类型
         * @param name Name
         * @param name 名称
         */
        @JvmStatic
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
                TAG_LIST -> NBTTagList(name)
                TAG_COMPOUND -> NBTTagCompound(name)
                TAG_INT_ARRAY -> NBTTagIntArray(name)
                TAG_LONG_ARRAY -> NBTTagLongArray(name)
            }
        }
    }
}
