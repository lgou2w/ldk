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

package com.lgou2w.ldk.bukkit.nbt

import com.lgou2w.ldk.bukkit.reflect.MinecraftReflection
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClassOrNull
import com.lgou2w.ldk.nbt.NBTBase
import com.lgou2w.ldk.nbt.NBTTagByte
import com.lgou2w.ldk.nbt.NBTTagByteArray
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.NBTTagDouble
import com.lgou2w.ldk.nbt.NBTTagEnd
import com.lgou2w.ldk.nbt.NBTTagFloat
import com.lgou2w.ldk.nbt.NBTTagInt
import com.lgou2w.ldk.nbt.NBTTagIntArray
import com.lgou2w.ldk.nbt.NBTTagList
import com.lgou2w.ldk.nbt.NBTTagLong
import com.lgou2w.ldk.nbt.NBTTagLongArray
import com.lgou2w.ldk.nbt.NBTTagShort
import com.lgou2w.ldk.nbt.NBTTagString
import com.lgou2w.ldk.nbt.NBTType
import com.lgou2w.ldk.reflect.AccessorField
import com.lgou2w.ldk.reflect.AccessorMethod
import com.lgou2w.ldk.reflect.FuzzyReflect
import com.lgou2w.ldk.reflect.Visibility
import java.io.DataInput
import java.io.DataOutput

object NBTFactory {

    @JvmStatic val CLASS_NBT_BASE by lazyMinecraftClass("NBTBase")
    @JvmStatic val CLASS_NBT_TAG_LIST by lazyMinecraftClass("NBTTagList")
    @JvmStatic val CLASS_NBT_TAG_COMPOUND by lazyMinecraftClass("NBTTagCompound")
    @JvmStatic val CLASS_NBT_STREAM by lazyMinecraftClass("NBTCompressedStreamTools")
    @JvmStatic val CLASS_NBT_READ_LIMITER by lazyMinecraftClass("NBTReadLimiter")
    @JvmStatic val CLASS_NBT_LONG_ARRAY by lazyMinecraftClassOrNull("NBTTagLongArray") // since Minecraft 1.12

    // NMS.NBTList -> private byte type
    @JvmStatic val NBT_LIST_TYPE_FIELD: AccessorField<Any, Byte> by lazy {
        FuzzyReflect.of(CLASS_NBT_TAG_LIST, true)
            .useFieldMatcher()
            .withType(Byte::class.java)
            .resultAccessorAs<Any, Byte>()
    }

    // NMS.NBTBase -> public static NBTBase createTag(byte)
    @JvmStatic val METHOD_NBT_CREATE: AccessorMethod<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_NBT_BASE, true)
                .useMethodMatcher()
                .withVisibilities(Visibility.STATIC)
                .withName("createTag")
                .withType(CLASS_NBT_BASE)
                .withParams(Byte::class.java)
                .resultAccessor()
    }

    // NMS.NBTBase -> public abstract byte getTypeId()
    @JvmStatic val METHOD_NBT_GET_TYPE_ID: AccessorMethod<Any, Byte> by lazy {
        FuzzyReflect.of(CLASS_NBT_BASE, true)
                .useMethodMatcher()
                .withVisibilities(Visibility.PUBLIC, Visibility.ABSTRACT)
                .withName("getTypeId")
                .withType(Byte::class.java)
                .resultAccessorAs<Any, Byte>()
    }

    // NMS.NBTCompressedStreamTools -> public static NMS.NBTBase read(DataInput, Int, NMS.NBTReadLimiter)
    @JvmStatic val MEHTOD_NBT_READ: AccessorMethod<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_NBT_STREAM, true)
                .useMethodMatcher()
                .withVisibilities(Visibility.STATIC)
                .withType(CLASS_NBT_BASE)
                .withParams(DataInput::class.java, Int::class.java, CLASS_NBT_READ_LIMITER)
                .resultAccessor()
    }


    // NMS.NBTCompressedStreamTools -> public static void write(NMS.NBTBase, DataOutput)
    @JvmStatic val METHOD_NBT_WRITE: AccessorMethod<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_NBT_STREAM, true)
                .useMethodMatcher()
                .withVisibilities(Visibility.STATIC)
                .withType(Void::class.java)
                .withParams(CLASS_NBT_BASE, DataOutput::class.java)
                .resultAccessor()
    }

    @JvmStatic
    fun fromNMS(nms: Any?): NBTBase<*>? {
        if (nms == null)
            return null
        MinecraftReflection.isExpected(nms, CLASS_NBT_BASE)
        val type = NBTType.fromId(METHOD_NBT_GET_TYPE_ID.invoke(nms)!!.toInt())!!
        val fieldValue = NBT_TYPE_FIELD(type)[nms]
        @Suppress("UNCHECKED_CAST")
        return when (type) {
            NBTType.TAG_COMPOUND -> {
                val compound = NBTTagCompound()
                val value = fieldValue as Map<String, Any>
                value.entries.forEach { compound[it.key] = fromNMS(it.value)!! }
                compound
            }
            NBTType.TAG_LIST -> {
                val list = NBTTagList()
                val value = fieldValue as List<Any>
                value.forEach { list.add(fromNMS(it)!!) }
                list
            }
            NBTType.TAG_END -> NBTTagEnd.INSTANCE
            NBTType.TAG_BYTE -> NBTTagByte(fieldValue as Byte)
            NBTType.TAG_SHORT -> NBTTagShort(fieldValue as Short)
            NBTType.TAG_INT -> NBTTagInt(fieldValue as Int)
            NBTType.TAG_LONG -> NBTTagLong(fieldValue as Long)
            NBTType.TAG_FLOAT -> NBTTagFloat(fieldValue as Float)
            NBTType.TAG_DOUBLE -> NBTTagDouble(fieldValue as Double)
            NBTType.TAG_BYTE_ARRAY -> NBTTagByteArray(fieldValue as ByteArray)
            NBTType.TAG_STRING -> NBTTagString(fieldValue as String)
            NBTType.TAG_INT_ARRAY -> NBTTagIntArray(fieldValue as IntArray)
            NBTType.TAG_LONG_ARRAY -> NBTTagLongArray(fieldValue as LongArray)
            else -> throw UnsupportedOperationException()
        }
    }

    @JvmStatic
    fun toNMS(nbt: NBTBase<*>?): Any? {
        if (nbt == null)
            return null
        return when (nbt.type) {
            NBTType.TAG_END -> NBT_END_INSTANCE
            NBTType.TAG_COMPOUND -> {
                val value = (nbt as NBTTagCompound).entries.associate { it.key to toNMS(it.value) }
                createInternal(NBTType.TAG_COMPOUND, value)
            }
            NBTType.TAG_LIST -> {
                val value = (nbt as NBTTagList).map { toNMS(it) }
                val handle = createInternal(NBTType.TAG_LIST, value)
                NBT_LIST_TYPE_FIELD[handle] = nbt.elementType.id.toByte()
                handle
            }
            else -> if (nbt is NBTTagLongArray && CLASS_NBT_LONG_ARRAY == null) {
                // 更大化兼容性
                // 因为 LongArray 标签在 1.12 之后版本
                // 所以如果不支持那么转换为 NBTTagList<Long>
                val longList = NBTTagList(nbt.name)
                nbt.value.forEach { longList.add(NBTTagLong(it)) }
                toNMS(longList)
            } else
                createInternal(nbt.type, nbt.value)
        }
    }

    @JvmStatic
    fun createInternal(type: NBTType, value: Any? = null): Any {
        val instance = METHOD_NBT_CREATE.invoke(null, type.id.toByte())
        val valueAccessor = NBT_TYPE_FIELD(type)
        if (value != null)
            valueAccessor[instance] = value
        return instance as Any
    }

    private val NBT_END_INSTANCE : Any by lazy {
        FuzzyReflect.of(MinecraftReflection.getMinecraftClass("NBTTagEnd"), true)
                .useConstructorMatcher()
                .resultAccessor()
                .newInstance()
    }

    private val NBT_TYPE_FIELD_ACCESSORS : MutableMap<NBTType, AccessorField<Any, Any>> = HashMap()
    private val NBT_TYPE_FIELD : (NBTType) -> AccessorField<Any, Any> = { type ->
        var accessor = NBT_TYPE_FIELD_ACCESSORS[type]
        if (accessor == null) {
            if (type == NBTType.TAG_END)
                throw IllegalArgumentException("Type TAG_END has no value member field.")
            accessor = FuzzyReflect.of(MinecraftReflection.getMinecraftClass(when (type) {
                NBTType.TAG_END -> "NBTTagEnd"
                NBTType.TAG_BYTE -> "NBTTagByte"
                NBTType.TAG_SHORT -> "NBTTagShort"
                NBTType.TAG_INT -> "NBTTagInt"
                NBTType.TAG_LONG -> "NBTTagLong"
                NBTType.TAG_FLOAT -> "NBTTagFloat"
                NBTType.TAG_DOUBLE -> "NBTTagDouble"
                NBTType.TAG_STRING -> "NBTTagString"
                NBTType.TAG_BYTE_ARRAY -> "NBTTagByteArray"
                NBTType.TAG_INT_ARRAY -> "NBTTagIntArray"
                NBTType.TAG_LIST -> "NBTTagList"
                NBTType.TAG_COMPOUND -> "NBTTagCompound"
                NBTType.TAG_LONG_ARRAY -> "NBTTagLongArray" // since Minecraft 1.12
            }), true)
                    .useFieldMatcher()
                    .withType(type.primitive)
                    .resultAccessor()
            NBT_TYPE_FIELD_ACCESSORS[type] = accessor
        }
        accessor
    }
}
