/*
 * Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>
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
import com.lgou2w.ldk.nbt.MojangsonParser
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
import com.lgou2w.ldk.reflect.AccessorConstructor
import com.lgou2w.ldk.reflect.AccessorField
import com.lgou2w.ldk.reflect.AccessorMethod
import com.lgou2w.ldk.reflect.DataType
import com.lgou2w.ldk.reflect.FuzzyReflect
import com.lgou2w.ldk.reflect.Visibility
import java.io.DataInput
import java.io.DataOutput

/**
 * ## NBTFactory (NBT 工厂)
 *
 * @author lgou2w
 */
object NBTFactory {

  @JvmStatic val CLASS_NBT_BASE by lazyMinecraftClass("NBTBase")
  @JvmStatic val CLASS_NBT_TAG_LIST by lazyMinecraftClass("NBTTagList")
  @JvmStatic val CLASS_NBT_TAG_COMPOUND by lazyMinecraftClass("NBTTagCompound")
  @JvmStatic val CLASS_NBT_STREAM by lazyMinecraftClass("NBTCompressedStreamTools")
  @JvmStatic val CLASS_NBT_READ_LIMITER by lazyMinecraftClass("NBTReadLimiter")
  @JvmStatic val CLASS_NBT_LONG_ARRAY by lazyMinecraftClassOrNull("NBTTagLongArray") // since Minecraft 1.12

  @Deprecated("IMPLEMENTED", replaceWith = ReplaceWith("MojangsonParser"))
  @JvmStatic val CLASS_MOJANGSON_PARSER by lazyMinecraftClass("MojangsonParser")

  // NMS.NBTList -> private byte type
  @JvmStatic val NBT_LIST_TYPE_FIELD: AccessorField<Any, Byte> by lazy {
    FuzzyReflect.of(CLASS_NBT_TAG_LIST, true)
      .useFieldMatcher()
      .withType(Byte::class.java)
      .resultAccessorAs<Any, Byte>()
  }

  // See: https://github.com/lgou2w/ldk/issues/104
  // NMS.NBTBase -> public static NBTBase createTag(byte)
  @Deprecated("Since Minecraft 1.15 was removed!")
  @JvmStatic val METHOD_NBT_CREATE: AccessorMethod<Any, Any>? by lazy {
    FuzzyReflect.of(CLASS_NBT_BASE, true)
      .useMethodMatcher()
      .withVisibilities(Visibility.STATIC)
      .withName("createTag")
      .withType(CLASS_NBT_BASE)
      .withParams(Byte::class.java)
      .resultAccessorOrNull()
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

  // NMS.MojangsonParser -> public static NMS.NBTTagCompound parse(String)
  @Deprecated("IMPLEMENTED", replaceWith = ReplaceWith("MojangsonParser.parse"))
  @JvmStatic val METHOD_MOJANGSON_PARSER : AccessorMethod<Any, Any> by lazy {
    FuzzyReflect.of(CLASS_MOJANGSON_PARSER, true)
      .useMethodMatcher()
      .withVisibilities(Visibility.PUBLIC, Visibility.STATIC)
      .withParams(String::class.java)
      .withType(CLASS_NBT_TAG_COMPOUND)
      .resultAccessor()
  }

  /**
   * * Converts the given `NMS` NBT object [nms] to an implementation of the [NBTBase] wrapper.
   * * 将给定的 `NMS` NBT 对象 [nms] 转换为 [NBTBase] 包装的实现.
   *
   * @throws [IllegalArgumentException] If the NBT object [nms] is not the expected `NMS` instance.
   * @throws [IllegalArgumentException] 如果 NBT 对象 [nms] 不是预期的 `NMS` 实例.
   */
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
      NBTType.TAG_STRING -> NBTTagString(value = fieldValue as String)
      NBTType.TAG_INT_ARRAY -> NBTTagIntArray(fieldValue as IntArray)
      NBTType.TAG_LONG_ARRAY -> NBTTagLongArray(fieldValue as LongArray)
      else -> throw UnsupportedOperationException()
    }
  }

  /**
   * * Converts the given [NBTBase] object [nbt] to an implementation of `NMS`.
   * * 将给定的 [NBTBase] 对象 [nbt] 转换为 `NMS` 的实现.
   */
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
        createInternal(NBTType.TAG_LIST, value, nbt.elementType)
      }
      else -> if (nbt is NBTTagLongArray && CLASS_NBT_LONG_ARRAY == null) {
        // 最大化兼容性
        // 因为 LongArray 标签在 1.12 之后版本
        // 所以如果不支持那么转换为 NBTTagList<Long>
        val longList = NBTTagList(nbt.name)
        nbt.value.forEach { longList.add(NBTTagLong(it)) }
        toNMS(longList)
      } else
        createInternal(nbt.type, nbt.value)
    }
  }

  /**
   * * Create an implementation instance object of `NMS` from the given NBT [type] and [value].
   * * 从给定的 NBT 类型 [type] 和值 [value] 创建一个 `NMS` 的实现实例对象.
   *
   * @throws [UnsupportedOperationException] If the server version does not support this [type].
   * @throws [UnsupportedOperationException] 如果服务器版本不支持此 [type] 类型.
   * @throws [IllegalArgumentException] If the [value] does not match the expected [type].
   * @throws [IllegalArgumentException] 如果 [value] 值和预期 [type] 类型不符合.
   */
  @JvmStatic
  @Throws(UnsupportedOperationException::class, IllegalArgumentException::class)
  fun createInternal(type: NBTType, value: Any? = null): Any
    = createInternal(type, value, NBTType.TAG_END)

  /**
   * * Create an implementation instance object of `NMS` from the given NBT [type] and [value].
   * * 从给定的 NBT 类型 [type] 和值 [value] 创建一个 `NMS` 的实现实例对象.
   *
   * @throws [UnsupportedOperationException] If the server version does not support this [type].
   * @throws [UnsupportedOperationException] 如果服务器版本不支持此 [type] 类型.
   * @throws [IllegalArgumentException] If the [value] does not match the expected [type].
   * @throws [IllegalArgumentException] 如果 [value] 值和预期 [type] 类型不符合.
   * @since LDK 0.2.0
   */
  @JvmStatic
  @Throws(UnsupportedOperationException::class, IllegalArgumentException::class)
  fun createInternal(type: NBTType, value: Any? = null, tagListElementType: NBTType): Any {
    if (value != null && !type.primitive.isAssignableFrom(DataType.ofPrimitive(value::class.java)))
      throw IllegalArgumentException("Value '${value::class.java}' and type mismatch. (Expected: ${type.primitive})")
    val constructor = try {
      NBT_TYPE_CONSTRUCTOR(type)
    } catch (e: ClassNotFoundException) {
      throw UnsupportedOperationException("The server version does not support this type: $type", e)
    }
    return if (type.isWrapper()) {
      val inst = constructor.newInstance() // List and Compound
      if (value != null)
        NBT_TYPE_FIELD(type)[inst] = value
      if (value != null && type == NBTType.TAG_LIST)
        NBT_LIST_TYPE_FIELD[inst] = tagListElementType.id.toByte()
      inst
    } else {
      constructor.newInstance(value)
    }
  }

  /**
   * * Parse the given NBT string in the `Mojang` Gson format to the [NBTTagCompound] object.
   * * 将给定的 `Mojang` Gson 格式的 NBT 字符串解析为 [NBTTagCompound] 对象.
   *
   * @see [NBTTagCompound]
   * @see [NBTTagCompound.toMojangson]
   * @see [MojangsonParser.parse]
   * @since LDK 0.1.8-rc
   */
  @JvmStatic
  @Deprecated("IMPLEMENTED", replaceWith = ReplaceWith("MojangsonParser.parse"))
  fun fromMojangson(mojangson: String?): NBTTagCompound? {
    if (mojangson == null || mojangson.isBlank()) return null
    return try {
      MojangsonParser.parse(mojangson)
    } catch (e: Exception) {
      null
    }
  }

  private val NBT_END_INSTANCE : Any by lazy {
    FuzzyReflect.of(MinecraftReflection.getMinecraftClass("NBTTagEnd"), true)
      .useConstructorMatcher()
      .resultAccessor()
      .newInstance()
  }

  private fun NBTType.toNMSClassName() = when (this) {
    NBTType.TAG_END        -> "NBTTagEnd"
    NBTType.TAG_BYTE       -> "NBTTagByte"
    NBTType.TAG_SHORT      -> "NBTTagShort"
    NBTType.TAG_INT        -> "NBTTagInt"
    NBTType.TAG_LONG       -> "NBTTagLong"
    NBTType.TAG_FLOAT      -> "NBTTagFloat"
    NBTType.TAG_DOUBLE     -> "NBTTagDouble"
    NBTType.TAG_STRING     -> "NBTTagString"
    NBTType.TAG_BYTE_ARRAY -> "NBTTagByteArray"
    NBTType.TAG_INT_ARRAY  -> "NBTTagIntArray"
    NBTType.TAG_LIST       -> "NBTTagList"
    NBTType.TAG_COMPOUND   -> "NBTTagCompound"
    NBTType.TAG_LONG_ARRAY -> "NBTTagLongArray" // since Minecraft 1.12
  }

  private val NBT_TYPE_CONSTRUCTOR_ACCESSORS : MutableMap<NBTType, AccessorConstructor<Any>> = HashMap()
  private val NBT_TYPE_CONSTRUCTOR : (NBTType) -> AccessorConstructor<Any> = { type ->
    var accessor = NBT_TYPE_CONSTRUCTOR_ACCESSORS[type]
    if (accessor == null) {
      if (type == NBTType.TAG_END)
        throw IllegalArgumentException("TAG_END")
      val className = type.toNMSClassName()
      accessor = FuzzyReflect.of(MinecraftReflection.getMinecraftClass(className), true)
        .useConstructorMatcher()
        .apply {
          when {
            type.isWrapper()
                 -> withParamsCount(0)
            else -> withParams(type.primitive)
          }
        }
        .resultAccessor()
    }
    accessor
  }

  private val NBT_TYPE_FIELD_ACCESSORS : MutableMap<NBTType, AccessorField<Any, Any>> = HashMap()
  private val NBT_TYPE_FIELD : (NBTType) -> AccessorField<Any, Any> = { type ->
    var accessor = NBT_TYPE_FIELD_ACCESSORS[type]
    if (accessor == null) {
      if (type == NBTType.TAG_END)
        throw IllegalArgumentException("Type TAG_END has no value member field.")
      val className = type.toNMSClassName()
      accessor = FuzzyReflect
        .of(MinecraftReflection.getMinecraftClass(className), true)
        .useFieldMatcher()
        .withType(type.primitive)
        .resultAccessor()
      NBT_TYPE_FIELD_ACCESSORS[type] = accessor
    }
    accessor
  }
}
