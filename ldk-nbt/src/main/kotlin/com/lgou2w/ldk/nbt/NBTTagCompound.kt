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
class NBTTagCompound : NBTBase<MutableMap<String, NBTBase<*>>>, MutableMap<String, NBTBase<*>> {

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

    // TODO document

    fun put(value: NBTBase<*>): NBTBase<*>? {
        return put(value.name, value)
    }

    fun putByte(key: String, value: Byte): NBTBase<*>? {
        return put(NBTTagByte(key, value))
    }

    fun putByte(key: String, value: Int): NBTBase<*>? {
        return put(NBTTagByte(key, value.toByte()))
    }

    fun putShort(key: String, value: Short): NBTBase<*>? {
        return put(NBTTagShort(key, value))
    }

    fun putShort(key: String, value: Int): NBTBase<*>? {
        return put(NBTTagShort(key, value.toShort()))
    }

    fun putInt(key: String, value: Int): NBTBase<*>? {
        return put(NBTTagInt(key, value))
    }

    fun putLong(key: String, value: Long): NBTBase<*>? {
        return put(NBTTagLong(key, value))
    }

    fun putFloat(key: String, value: Float): NBTBase<*>? {
        return put(NBTTagFloat(key, value))
    }

    fun putDouble(key: String, value: Double): NBTBase<*>? {
        return put(NBTTagDouble(key, value))
    }

    fun putByteArray(key: String, value: ByteArray): NBTBase<*>? {
        return put(NBTTagByteArray(key, value))
    }

    fun putString(key: String, value: String): NBTBase<*>? {
        return put(NBTTagString(key, value))
    }

    fun putIntArray(key: String, value: IntArray): NBTBase<*>? {
        return put(NBTTagIntArray(key, value))
    }

    fun putBoolean(key: String, value: Boolean): NBTBase<*>? {
        return putByte(key, if (value) 1 else 0)
    }

    private fun getNumber(key: String): Number {
        val tag = get(key) ?: throw NoSuchElementException("未存在的指定键: $key")
        return (tag as NBTTagNumber<*>).value
    }

    private fun getNumberOrNull(key: String): Number? {
        return (get(key) as? NBTTagNumber<*>)?.value
    }

    private fun <T> getOrDefault(type: NBTType, key: String): T {
        var tag = get(key)
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
                NBTType.TAG_LIST -> NBTTagList<NBTBase<*>>(key)
                NBTType.TAG_COMPOUND -> NBTTagCompound(key)
                NBTType.TAG_INT_ARRAY -> NBTTagIntArray(key)
                NBTType.TAG_BYTE_ARRAY -> NBTTagByteArray(key)
            }
            put(tag) // put
        }
        @Suppress("UNCHECKED_CAST")
        return if (tag.type.isWrapper()) tag as T
        else tag.value as T
    }

    fun getByte(key: String): Byte {
        return getNumber(key).toByte()
    }

    fun getByteOrNull(key: String): Byte? {
        return getNumberOrNull(key)?.toByte()
    }

    fun getByteOrDefault(key: String): Byte {
        return getOrDefault<Number>(NBTType.TAG_BYTE, key).toByte()
    }

    fun getShort(key: String): Short {
        return getNumber(key).toShort()
    }

    fun getShortOrNull(key: String): Short? {
        return getNumberOrNull(key)?.toShort()
    }

    fun getShortOrDefault(key: String): Short {
        return getOrDefault<Number>(NBTType.TAG_SHORT, key).toShort()
    }

    fun getInt(key: String): Int {
        return getNumber(key).toInt()
    }

    fun getIntOrNull(key: String): Int? {
        return getNumberOrNull(key)?.toInt()
    }

    fun getIntOrDefault(key: String): Int {
        return getOrDefault<Number>(NBTType.TAG_INT, key).toInt()
    }

    fun getLong(key: String): Long {
        return getNumber(key).toLong()
    }

    fun getLongOrNull(key: String): Long? {
        return getNumberOrNull(key)?.toLong()
    }

    fun getLongOrDefault(key: String): Long {
        return getOrDefault<Number>(NBTType.TAG_LONG, key).toLong()
    }

    fun getFloat(key: String): Float {
        return getNumber(key).toFloat()
    }

    fun getFloatOrNull(key: String): Float? {
        return getNumberOrNull(key)?.toFloat()
    }

    fun getFloatOrDefault(key: String): Float {
        return getOrDefault<Number>(NBTType.TAG_FLOAT, key).toFloat()
    }

    fun getDouble(key: String): Double {
        return getNumber(key).toDouble()
    }

    fun getDoubleOrNull(key: String): Double? {
        return getNumberOrNull(key)?.toDouble()
    }

    fun getDoubleOrDefault(key: String): Double {
        return getOrDefault<Number>(NBTType.TAG_DOUBLE, key).toDouble()
    }

    fun getByteArray(key: String): ByteArray {
        return (get(key) as NBTTagByteArray).value
    }

    fun getByteArrayOrNull(key: String): ByteArray? {
        return (get(key) as? NBTTagByteArray)?.value
    }

    fun getByteArrayOrDefault(key: String): ByteArray {
        return getOrDefault(NBTType.TAG_BYTE_ARRAY, key)
    }

    fun getString(key: String): String {
        return (get(key) as NBTTagString).value
    }

    fun getStringOrNull(key: String): String? {
        return (get(key) as? NBTTagString)?.value
    }

    fun getStringOrDefault(key: String): String {
        return getOrDefault(NBTType.TAG_STRING, key)
    }

    fun getList(key: String): NBTTagList<*> {
        return get(key) as NBTTagList<*>
    }

    fun getListOrNull(key: String): NBTTagList<*>? {
        return get(key) as? NBTTagList<*>
    }

    fun getListOrDefault(key: String): NBTTagList<*> {
        return getOrDefault(NBTType.TAG_LIST, key)
    }

    fun getCompound(key: String): NBTTagCompound {
        return get(key) as NBTTagCompound
    }

    fun getCompoundOrNull(key: String): NBTTagCompound? {
        return get(key) as? NBTTagCompound
    }

    fun getCompoundOrDefault(key: String): NBTTagCompound {
        return getOrDefault(NBTType.TAG_COMPOUND, key)
    }

    fun getIntArray(key: String): IntArray? {
        return (get(key) as? NBTTagIntArray)?.value
    }

    fun getIntArrayOrNull(key: String): IntArray {
        return (get(key) as NBTTagIntArray).value
    }

    fun getIntArrayOrDefault(key: String): IntArray {
        return getOrDefault(NBTType.TAG_INT_ARRAY, key)
    }

    fun getBoolean(key: String): Boolean {
        return getByte(key) > 0
    }

    fun getBooleanOrNull(key: String): Boolean? {
        return getByteOrNull(key).let { if (it == null) null else it > 0 }
    }

    fun getBooleanOrDefault(key: String): Boolean {
        return getByteOrDefault(key) > 0
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
