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

import java.io.DataInput
import java.io.DataOutput

/**
 * ## NBTTagCompound (复合 NBT 标签)
 *
 * @see [Map]
 * @author lgou2w
 */
open class NBTTagCompound : NBTBase<MutableMap<String, NBTBase<*>>>, MutableMap<String, NBTBase<*>> {

    @JvmOverloads
    constructor(name: String, value: MutableMap<String, NBTBase<*>> = LinkedHashMap()) : super(name, value)
    constructor(value: MutableMap<String, NBTBase<*>> = LinkedHashMap()) : super("", value)

    override val type: NBTType
        get() = NBTType.TAG_COMPOUND

    override var value: MutableMap<String, NBTBase<*>>
        get() = HashMap(super.value0)
        set(value) { super.value0 = HashMap(value) }

    override fun read(input: DataInput) {
        var tag: NBTBase<*>? = null
        while (NBTStreams.read(input).apply { tag = this } != NBTTagEnd.INSTANCE) {
            val value = tag!!
            put(value.name, value)
        }
    }

    override fun write(output: DataOutput) {
        for (value in values)
            NBTStreams.write(output, value)
        output.writeByte(0)
    }

    override fun clone(): NBTTagCompound {
        val value = value0.mapValues { it.value.clone() }.toMutableMap()    // clone
        return NBTTagCompound(name, value)
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other) && entries == (other as NBTTagCompound).entries
    }

    override fun toString(): String {
        return "NBTTagCompound(${toJson()})"
    }

    override fun toJson(): String {
        return buildString {
            append("{")
            for ((key, value) in entries) {
                if (length > 1)
                    append(",")
                append("\"")
                append(key)
                append("\"")
                append(":")
                append(value.toJson())
            }
            append("}")
        }
    }

    override fun toMojangson(): String {
        return buildString {
            append("{")
            for ((key, value) in entries) {
                if (length > 1)
                    append(",")
                append("\"")
                append(key)
                append("\"")
                append(":")
                append(value.toMojangson())
            }
            append("}")
        }
    }

    ///
    //

    /**
     * * Add the given [value] NBT tag to this compound tag.
     * * 将给定的 [value] NBT 标签添加到此复合标签内.
     */
    fun put(value: NBTBase<*>): NBTBase<*>? {
        return put(value.name, value)
    }

    /**
     * * Add the given [value] byte value to [key] in this compound tag.
     * * 将给定的 [value] 字节值以 [key] 添加到此复合标签内.
     */
    fun putByte(key: String, value: Byte): NBTBase<*>? {
        return put(NBTTagByte(key, value))
    }

    /**
     * * Add the given [value] byte value to [key] in this compound tag.
     * * 将给定的 [value] 字节值以 [key] 添加到此复合标签内.
     */
    fun putByte(key: String, value: Int): NBTBase<*>? {
        return put(NBTTagByte(key, value.toByte()))
    }

    /**
     * * Add the given [value] short value to [key] in this compound tag.
     * * 将给定的 [value] 短整数值以 [key] 添加到此复合标签内.
     */
    fun putShort(key: String, value: Short): NBTBase<*>? {
        return put(NBTTagShort(key, value))
    }

    /**
     * * Add the given [value] short value to [key] in this compound tag.
     * * 将给定的 [value] 短整数值以 [key] 添加到此复合标签内.
     */
    fun putShort(key: String, value: Int): NBTBase<*>? {
        return put(NBTTagShort(key, value.toShort()))
    }

    /**
     * * Add the given [value] int value to [key] in this compound tag.
     * * 将给定的 [value] 整数值以 [key] 添加到此复合标签内.
     */
    fun putInt(key: String, value: Int): NBTBase<*>? {
        return put(NBTTagInt(key, value))
    }

    /**
     * * Add the given [value] long value to [key] in this compound tag.
     * * 将给定的 [value] 长整数值以 [key] 添加到此复合标签内.
     */
    fun putLong(key: String, value: Long): NBTBase<*>? {
        return put(NBTTagLong(key, value))
    }

    /**
     * * Add the given [value] float value to [key] in this compound tag.
     * * 将给定的 [value] 单精度浮点值以 [key] 添加到此复合标签内.
     */
    fun putFloat(key: String, value: Float): NBTBase<*>? {
        return put(NBTTagFloat(key, value))
    }

    /**
     * * Add the given [value] double value to [key] in this compound tag.
     * * 将给定的 [value] 双精度浮点值以 [key] 添加到此复合标签内.
     */
    fun putDouble(key: String, value: Double): NBTBase<*>? {
        return put(NBTTagDouble(key, value))
    }

    /**
     * * Add the given [value] byte array value to [key] in this compound tag.
     * * 将给定的 [value] 字节数组值以 [key] 添加到此复合标签内.
     */
    fun putByteArray(key: String, value: ByteArray): NBTBase<*>? {
        return put(NBTTagByteArray(key, value))
    }

    /**
     * * Add the given [value] string value to [key] in this compound tag.
     * * 将给定的 [value] 字符串值以 [key] 添加到此复合标签内.
     */
    fun putString(key: String, value: String): NBTBase<*>? {
        return put(NBTTagString(key, value))
    }

    /**
     * * Add the given [value] int array value to [key] in this compound tag.
     * * 将给定的 [value] 整数数组值以 [key] 添加到此复合标签内.
     */
    fun putIntArray(key: String, value: IntArray): NBTBase<*>? {
        return put(NBTTagIntArray(key, value))
    }

    /**
     * * Add the given [value] boolean value to [key] in this compound tag.
     * * 将给定的 [value] 布尔值以 [key] 添加到此复合标签内.
     */
    fun putBoolean(key: String, value: Boolean): NBTBase<*>? {
        return putByte(key, if (value) 1 else 0)
    }

    /**
     * @throws NoSuchElementException
     * @throws ClassCastException
     */
    private fun getAndCheck(key: String, expected: Class<out NBTBase<*>>, nullable: Boolean) : NBTBase<*>? {
        val tag = get(key)
        if (nullable && tag == null)
            return null
        if (tag == null)
            throw NoSuchElementException("未存在的指定键: $key")
        val expectedType = NBTType.fromClass(expected)
        if (!expected.isInstance(tag))
            throw ClassCastException("键 $key 的标签值类型 ${tag.type} 和预期不符合. (预期: ${expectedType ?: expected.simpleName})")
        return tag
    }

    private fun getNumber(key: String): Number {
        val tag = getAndCheck(key, NBTTagNumber::class.java, false)
        return (tag as NBTTagNumber<*>).value
    }

    private fun getNumberOrNull(key: String): Number? {
        return (getAndCheck(key, NBTTagNumber::class.java, true) as? NBTTagNumber<*>)?.value
    }

    private fun <T> getOrDefault(type: NBTType, key: String): T {
        var tag = getAndCheck(key, type.wrapped, true)
        if (tag == null) {
            tag = when (type) {
                NBTType.TAG_END -> throw UnsupportedOperationException("TAG_END")
                NBTType.TAG_BYTE -> NBTTagByte(key)
                NBTType.TAG_SHORT -> NBTTagShort(key)
                NBTType.TAG_INT -> NBTTagInt(key)
                NBTType.TAG_LONG -> NBTTagLong(key)
                NBTType.TAG_FLOAT -> NBTTagFloat(key)
                NBTType.TAG_DOUBLE -> NBTTagDouble(key)
                NBTType.TAG_STRING -> NBTTagString(key)
                NBTType.TAG_LIST -> NBTTagList(key)
                NBTType.TAG_COMPOUND -> NBTTagCompound(key)
                NBTType.TAG_INT_ARRAY -> NBTTagIntArray(key)
                NBTType.TAG_BYTE_ARRAY -> NBTTagByteArray(key)
                NBTType.TAG_LONG_ARRAY -> NBTTagLongArray(key)
            }
            put(tag) // put
        }
        @Suppress("UNCHECKED_CAST")
        return if (tag.type.isWrapper()) tag as T
        else tag.value as T
    }

    /**
     * * Get the byte value from the given [key] key.
     * * 从给定的 [key] 键中获取字节值.
     *
     * @throws NoSuchElementException If the [key] key does not exist.
     * @throws NoSuchElementException 如果 [key] 键不存在.
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getByte(key: String): Byte {
        return getNumber(key).toByte()
    }

    /**
     * * Get the byte value from the given [key] key.
     * * 从给定的 [key] 键中获取字节值.
     *
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getByteOrNull(key: String): Byte? {
        return getNumberOrNull(key)?.toByte()
    }

    /**
     * * Get the byte value from the given [key] key. if it doesn't exist, add the default and get it.
     * * 从给定的 [key] 键中获取字节值, 如果不存在则添加默认并获取.
     *
     * @throws ClassCastException If the value of the [key] key exists and does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值存在并且和预期不符合.
     */
    fun getByteOrDefault(key: String): Byte {
        return getOrDefault<Number>(NBTType.TAG_BYTE, key).toByte()
    }

    /**
     * * Get the short value from the given [key] key.
     * * 从给定的 [key] 键中获取短整数值.
     *
     * @throws NoSuchElementException If the [key] key does not exist.
     * @throws NoSuchElementException 如果 [key] 键不存在.
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getShort(key: String): Short {
        return getNumber(key).toShort()
    }

    /**
     * * Get the short value from the given [key] key.
     * * 从给定的 [key] 键中获取短整数值.
     *
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getShortOrNull(key: String): Short? {
        return getNumberOrNull(key)?.toShort()
    }

    /**
     * * Get the short value from the given [key] key. if it doesn't exist, add the default and get it.
     * * 从给定的 [key] 键中获取短整数值, 如果不存在则添加默认并获取.
     *
     * @throws ClassCastException If the value of the [key] key exists and does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值存在并且和预期不符合.
     */
    fun getShortOrDefault(key: String): Short {
        return getOrDefault<Number>(NBTType.TAG_SHORT, key).toShort()
    }

    /**
     * * Get the int value from the given [key] key.
     * * 从给定的 [key] 键中获取整数值.
     *
     * @throws NoSuchElementException If the [key] key does not exist.
     * @throws NoSuchElementException 如果 [key] 键不存在.
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getInt(key: String): Int {
        return getNumber(key).toInt()
    }

    /**
     * * Get the int value from the given [key] key.
     * * 从给定的 [key] 键中获取整数值.
     *
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getIntOrNull(key: String): Int? {
        return getNumberOrNull(key)?.toInt()
    }

    /**
     * * Get the int value from the given [key] key. if it doesn't exist, add the default and get it.
     * * 从给定的 [key] 键中获取整数值, 如果不存在则添加默认并获取.
     *
     * @throws ClassCastException If the value of the [key] key exists and does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值存在并且和预期不符合.
     */
    fun getIntOrDefault(key: String): Int {
        return getOrDefault<Number>(NBTType.TAG_INT, key).toInt()
    }

    /**
     * * Get the long value from the given [key] key.
     * * 从给定的 [key] 键中获取长整数值.
     *
     * @throws NoSuchElementException If the [key] key does not exist.
     * @throws NoSuchElementException 如果 [key] 键不存在.
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getLong(key: String): Long {
        return getNumber(key).toLong()
    }

    /**
     * * Get the long value from the given [key] key.
     * * 从给定的 [key] 键中获取长整数值.
     *
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getLongOrNull(key: String): Long? {
        return getNumberOrNull(key)?.toLong()
    }

    /**
     * * Get the long value from the given [key] key. if it doesn't exist, add the default and get it.
     * * 从给定的 [key] 键中获取长整数值, 如果不存在则添加默认并获取.
     *
     * @throws ClassCastException If the value of the [key] key exists and does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值存在并且和预期不符合.
     */
    fun getLongOrDefault(key: String): Long {
        return getOrDefault<Number>(NBTType.TAG_LONG, key).toLong()
    }

    /**
     * * Get the float value from the given [key] key.
     * * 从给定的 [key] 键中获取单精度浮点值.
     *
     * @throws NoSuchElementException If the [key] key does not exist.
     * @throws NoSuchElementException 如果 [key] 键不存在.
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getFloat(key: String): Float {
        return getNumber(key).toFloat()
    }

    /**
     * * Get the float value from the given [key] key.
     * * 从给定的 [key] 键中获取单精度浮点值.
     *
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getFloatOrNull(key: String): Float? {
        return getNumberOrNull(key)?.toFloat()
    }

    /**
     * * Get the float value from the given [key] key. if it doesn't exist, add the default and get it.
     * * 从给定的 [key] 键中获取单精度浮点值, 如果不存在则添加默认并获取.
     *
     * @throws ClassCastException If the value of the [key] key exists and does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值存在并且和预期不符合.
     */
    fun getFloatOrDefault(key: String): Float {
        return getOrDefault<Number>(NBTType.TAG_FLOAT, key).toFloat()
    }

    /**
     * * Get the double value from the given [key] key.
     * * 从给定的 [key] 键中获取双精度浮点值.
     *
     * @throws NoSuchElementException If the [key] key does not exist.
     * @throws NoSuchElementException 如果 [key] 键不存在.
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getDouble(key: String): Double {
        return getNumber(key).toDouble()
    }


    /**
     * * Get the double value from the given [key] key.
     * * 从给定的 [key] 键中获取双精度浮点值.
     *
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getDoubleOrNull(key: String): Double? {
        return getNumberOrNull(key)?.toDouble()
    }

    /**
     * * Get the double value from the given [key] key. if it doesn't exist, add the default and get it.
     * * 从给定的 [key] 键中获取双精度浮点值, 如果不存在则添加默认并获取.
     *
     * @throws ClassCastException If the value of the [key] key exists and does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值存在并且和预期不符合.
     */
    fun getDoubleOrDefault(key: String): Double {
        return getOrDefault<Number>(NBTType.TAG_DOUBLE, key).toDouble()
    }

    /**
     * * Get the byte array value from the given [key] key.
     * * 从给定的 [key] 键中获取字节数组值.
     *
     * @throws NoSuchElementException If the [key] key does not exist.
     * @throws NoSuchElementException 如果 [key] 键不存在.
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getByteArray(key: String): ByteArray {
        return (getAndCheck(key, NBTTagByteArray::class.java, false) as NBTTagByteArray).value
    }

    /**
     * * Get the byte array value from the given [key] key.
     * * 从给定的 [key] 键中获取字节数组值.
     *
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getByteArrayOrNull(key: String): ByteArray? {
        return (getAndCheck(key, NBTTagByteArray::class.java, true) as? NBTTagByteArray)?.value
    }

    /**
     * * Get the byte array value from the given [key] key. if it doesn't exist, add the default and get it.
     * * 从给定的 [key] 键中获取字节数组值, 如果不存在则添加默认并获取.
     *
     * @throws ClassCastException If the value of the [key] key exists and does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值存在并且和预期不符合.
     */
    fun getByteArrayOrDefault(key: String): ByteArray {
        return getOrDefault(NBTType.TAG_BYTE_ARRAY, key)
    }

    /**
     * * Get the string value from the given [key] key.
     * * 从给定的 [key] 键中获取字符串值.
     *
     * @throws NoSuchElementException If the [key] key does not exist.
     * @throws NoSuchElementException 如果 [key] 键不存在.
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getString(key: String): String {
        return (getAndCheck(key, NBTTagString::class.java, false) as NBTTagString).value
    }

    /**
     * * Get the string value from the given [key] key.
     * * 从给定的 [key] 键中获取字符串值.
     *
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getStringOrNull(key: String): String? {
        return (getAndCheck(key, NBTTagString::class.java, true) as? NBTTagString)?.value
    }

    /**
     * * Get the string value from the given [key] key. if it doesn't exist, add the default and get it.
     * * 从给定的 [key] 键中获取字符串值, 如果不存在则添加默认并获取.
     *
     * @throws ClassCastException If the value of the [key] key exists and does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值存在并且和预期不符合.
     */
    fun getStringOrDefault(key: String): String {
        return getOrDefault(NBTType.TAG_STRING, key)
    }

    /**
     * * Get the list tag value from the given [key] key.
     * * 从给定的 [key] 键中获取集合标签值.
     *
     * @throws NoSuchElementException If the [key] key does not exist.
     * @throws NoSuchElementException 如果 [key] 键不存在.
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getList(key: String): NBTTagList {
        return getAndCheck(key, NBTTagList::class.java, false) as NBTTagList
    }

    /**
     * * Get the list tag value from the given [key] key.
     * * 从给定的 [key] 键中获取集合标签值.
     *
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getListOrNull(key: String): NBTTagList? {
        return getAndCheck(key, NBTTagList::class.java, true) as? NBTTagList
    }

    /**
     * * Get the list tag value from the given [key] key. if it doesn't exist, add the default and get it.
     * * 从给定的 [key] 键中获取集合标签值, 如果不存在则添加默认并获取.
     *
     * @throws ClassCastException If the value of the [key] key exists and does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值存在并且和预期不符合.
     */
    fun getListOrDefault(key: String): NBTTagList {
        return getOrDefault(NBTType.TAG_LIST, key)
    }

    /**
     * * Get the compound tag value from the given [key] key.
     * * 从给定的 [key] 键中获取复合标签值.
     *
     * @throws NoSuchElementException If the [key] key does not exist.
     * @throws NoSuchElementException 如果 [key] 键不存在.
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getCompound(key: String): NBTTagCompound {
        return getAndCheck(key, NBTTagCompound::class.java, false) as NBTTagCompound
    }

    /**
     * * Get the compound tag value from the given [key] key.
     * * 从给定的 [key] 键中获取复合标签值.
     *
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getCompoundOrNull(key: String): NBTTagCompound? {
        return getAndCheck(key, NBTTagCompound::class.java, true) as? NBTTagCompound
    }

    /**
     * * Get the compound tag value from the given [key] key. if it doesn't exist, add the default and get it.
     * * 从给定的 [key] 键中获取复合标签值, 如果不存在则添加默认并获取.
     *
     * @throws ClassCastException If the value of the [key] key exists and does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值存在并且和预期不符合.
     */
    fun getCompoundOrDefault(key: String): NBTTagCompound {
        return getOrDefault(NBTType.TAG_COMPOUND, key)
    }

    /**
     * * Get the int array value from the given [key] key.
     * * 从给定的 [key] 键中获取整数数组值.
     *
     * @throws NoSuchElementException If the [key] key does not exist.
     * @throws NoSuchElementException 如果 [key] 键不存在.
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getIntArray(key: String): IntArray {
        return (getAndCheck(key, NBTTagIntArray::class.java, false) as NBTTagIntArray).value
    }

    /**
     * * Get the int array value from the given [key] key.
     * * 从给定的 [key] 键中获取整数数组值.
     *
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getIntArrayOrNull(key: String): IntArray? {
        return (getAndCheck(key, NBTTagIntArray::class.java, true) as? NBTTagIntArray)?.value
    }

    /**
     * * Get the int array value from the given [key] key. if it doesn't exist, add the default and get it.
     * * 从给定的 [key] 键中获取整数数组值, 如果不存在则添加默认并获取.
     *
     * @throws ClassCastException If the value of the [key] key exists and does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值存在并且和预期不符合.
     */
    fun getIntArrayOrDefault(key: String): IntArray {
        return getOrDefault(NBTType.TAG_INT_ARRAY, key)
    }

    /**
     * * Get the boolean value from the given [key] key.
     * * 从给定的 [key] 键中获取布尔值.
     *
     * @throws NoSuchElementException If the [key] key does not exist.
     * @throws NoSuchElementException 如果 [key] 键不存在.
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getBoolean(key: String): Boolean {
        return getByte(key) > 0
    }

    /**
     * * Get the boolean value from the given [key] key.
     * * 从给定的 [key] 键中获取布尔值.
     *
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getBooleanOrNull(key: String): Boolean? {
        return getByteOrNull(key).let { if (it == null) null else it > 0 }
    }

    /**
     * * Get the boolean value from the given [key] key. if it doesn't exist, add the default and get it.
     * * 从给定的 [key] 键中获取布尔值, 如果不存在则添加默认并获取.
     *
     * @throws ClassCastException If the value of the [key] key exists and does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值存在并且和预期不符合.
     */
    fun getBooleanOrDefault(key: String): Boolean {
        return getByteOrDefault(key) > 0
    }

    /**
     * * Get the long array value from the given [key] key.
     * * 从给定的 [key] 键中获取整数数组值.
     *
     * @throws NoSuchElementException If the [key] key does not exist.
     * @throws NoSuchElementException 如果 [key] 键不存在.
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getLongArray(key: String): LongArray {
        return (getAndCheck(key, NBTTagLongArray::class.java, false) as NBTTagLongArray).value
    }

    /**
     * * Get the long array value from the given [key] key.
     * * 从给定的 [key] 键中获取整数数组值.
     *
     * @throws ClassCastException If the value of the [key] key does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值和预期不符合.
     */
    fun getLongArrayOrNull(key: String): LongArray? {
        return (getAndCheck(key, NBTTagLongArray::class.java, true) as? NBTTagLongArray)?.value
    }

    /**
     * * Get the long array value from the given [key] key. if it doesn't exist, add the default and get it.
     * * 从给定的 [key] 键中获取整数数组值, 如果不存在则添加默认并获取.
     *
     * @throws ClassCastException If the value of the [key] key exists and does not match the expected.
     * @throws ClassCastException 如果 [key] 键的值存在并且和预期不符合.
     */
    fun getLongArrayOrDefault(key: String): LongArray {
        return getOrDefault(NBTType.TAG_LONG_ARRAY, key)
    }

    //
    ///

    override val size: Int
        get() = value0.size

    override fun containsKey(key: String): Boolean {
        return value0.containsKey(key)
    }

    override fun containsValue(value: NBTBase<*>): Boolean {
        return value0.containsValue(value)
    }

    override fun get(key: String): NBTBase<*>? {
        return value0[key]
    }

    override fun isEmpty(): Boolean {
        return value0.isEmpty()
    }

    override val entries: MutableSet<MutableMap.MutableEntry<String, NBTBase<*>>>
        get() = value0.entries

    override val keys: MutableSet<String>
        get() = value0.keys

    override val values: MutableCollection<NBTBase<*>>
        get() = value0.values

    override fun clear() {
        value0.clear()
    }

    override fun put(key: String, value: NBTBase<*>): NBTBase<*>? {
        return value0.put(key, value)
    }

    override fun putAll(from: Map<out String, NBTBase<*>>) {
        return value0.putAll(from)
    }

    override fun remove(key: String): NBTBase<*>? {
        return value0.remove(key)
    }
}
