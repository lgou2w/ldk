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

package com.lgou2w.ldk.nbt

import org.amshove.kluent.invoking
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBe
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withCause
import org.junit.Test
import java.io.DataInput
import java.io.DataOutput
import java.lang.reflect.InvocationTargetException

class NBTTagCompoundTest {

    @Test fun `NBTTagCompound - get - validation`() {
        val compound = ofCompound {  }
        compound.putByte("b", 1)
        compound.putByte("b", 1.toByte())
        compound.putBoolean("b", false)
        compound.putBoolean("b", true)
        compound.putShort("s", 1)
        compound.putShort("s", 1.toShort())
        compound.putInt("i", 1)
        compound.putLong("l", 1L)
        compound.putFloat("f", 1f)
        compound.putDouble("d", 1.0)
        compound.putByteArray("[b", byteArrayOf(1))
        compound.putString("string", "1")
        compound.put(ofList("list"))
        compound.put(ofCompound("compound"))
        compound.putIntArray("[i", intArrayOf(1))
        compound.putLongArray("[l", longArrayOf(1L))
        compound.size shouldEqual 12
        compound.getByte("b") shouldEqual 1
        compound.getBoolean("b") shouldEqual true
        compound.getShort("s") shouldEqual 1
        compound.getInt("i") shouldEqual 1
        compound.getLong("l") shouldEqual 1L
        compound.getFloat("f") shouldEqual 1f
        compound.getDouble("d") shouldEqual 1.0
        compound.getByteArray("[b") shouldContain 1
        compound.getString("string") shouldEqual "1"
        compound.getList("list") shouldEqual ofList("list")
        compound.getCompound("compound") shouldEqual ofCompound("compound")
        compound.getIntArray("[i") shouldContain 1
        compound.getLongArray("[l") shouldContain 1L
        compound.getByteOrNull("bb") shouldEqual null
        compound.getByteOrNull("b") shouldEqual 1
        compound.getBooleanOrNull("bb") shouldEqual null
        compound.getBooleanOrNull("b") shouldEqual true
        compound.getShortOrNull("ss") shouldEqual null
        compound.getShortOrNull("s") shouldEqual 1
        compound.getIntOrNull("ii") shouldEqual null
        compound.getIntOrNull("i") shouldEqual 1
        compound.getLongOrNull("ll") shouldEqual null
        compound.getLongOrNull("l") shouldEqual 1L
        compound.getFloatOrNull("ff") shouldEqual null
        compound.getFloatOrNull("f") shouldEqual 1f
        compound.getDoubleOrNull("dd") shouldEqual null
        compound.getDoubleOrNull("d") shouldEqual 1.0
        compound.getByteArrayOrNull("[bb") shouldEqual null
        compound.getByteArrayOrNull("[b")?.shouldContain(1)
        compound.getStringOrNull("sstring") shouldEqual null
        compound.getStringOrNull("string") shouldEqual "1"
        compound.getListOrNull("llist") shouldEqual null
        compound.getListOrNull("list") shouldEqual ofList("list")
        compound.getCompoundOrNull("ccompound") shouldEqual null
        compound.getCompoundOrNull("compound") shouldEqual ofCompound("compound")
        compound.getIntArrayOrNull("[ii") shouldEqual null
        compound.getIntArrayOrNull("[i")?.shouldContain(1)
        compound.getLongArrayOrNull("[ll") shouldEqual null
        compound.getLongArrayOrNull("[l")?.shouldContain(1L)
    }

    @Test fun `NBTTagCompound - getOrDefault - validation`() {
        val compound = ofCompound {  }
        compound.getByteOrDefault("b") shouldEqual 0
        compound.getBooleanOrDefault("b") shouldEqual false
        compound.getShortOrDefault("s") shouldEqual 0
        compound.getIntOrDefault("i") shouldEqual 0
        compound.getLongOrDefault("l") shouldEqual 0L
        compound.getFloatOrDefault("f") shouldEqual 0f
        compound.getDoubleOrDefault("d") shouldEqual 0.0
        compound.getByteArrayOrDefault("[b").isEmpty() shouldEqual true
        compound.getStringOrDefault("string") shouldEqual ""
        compound.getListOrDefault("list").isEmpty() shouldEqual true
        compound.getCompoundOrDefault("compound").isEmpty() shouldEqual true
        compound.getIntArrayOrDefault("[i").isEmpty() shouldEqual true
        compound.getLongArrayOrDefault("[l").isEmpty() shouldEqual true
        compound.putBoolean("b2", true)
        compound.getBoolean("b") shouldEqual false
        compound.getBooleanOrNull("b") shouldEqual false
        compound.getBooleanOrDefault("b2") shouldEqual true
    }

    @Test fun `NBTTagCompound - method validation from some map`() {
        val entryValue1= NBTTagString(value = "value")
        val compound = ofCompound {  }
        compound.value = hashMapOf("key" to entryValue1)
        compound.containsValue(entryValue1) shouldEqual true
        compound.keys shouldContain "key"
        compound.values shouldContain entryValue1
        compound.value.size shouldEqual 1
        compound.containsKey("key") shouldEqual true
        compound["key"] shouldEqual entryValue1
        compound.remove("key", entryValue1) shouldEqual true
        compound.clear()
        compound.isEmpty() shouldEqual true
        compound.equals(null) shouldEqual false
        compound.putAll(mapOf("key" to entryValue1))
        val compoundCloned = compound.clone()
        compound shouldEqual compoundCloned
        compound shouldNotBe compoundCloned
        compound.clear()
        compound.equals(compoundCloned) shouldEqual false
    }

    @Test fun `NBTTagCompound - get - If the entry does not exist, should be throw exception`() {
        val compound = ofCompound {  }
        invoking { compound.getInt("int") } shouldThrow NoSuchElementException::class
    }

    @Test fun `NBTTagCompound - get - If the entry type not match, should be throw exception`() {
        val compound = ofCompound { putInt("int", 1) }
        compound.getInt("int") shouldEqual 1
        invoking { compound.getString("int") } shouldThrow ClassCastException::class
    }

    @Test fun `NBTTagCompound - toJson`() {
        val emptyCompound = ofCompound {  }
        val compound = ofCompound {
            putByte("byte", 1)
            putString("str", "value")
        }
        compound.toJson() shouldEqual "{\"byte\":1,\"str\":\"value\"}"
        compound.toMojangson() shouldEqual "{\"byte\":1b,\"str\":\"value\"}"
        compound.toMojangsonWithColor() shouldEqual "{§bbyte§r: §61§cb§r, §bstr§r: \"§avalue§r\"}"
        emptyCompound.toJson() shouldEqual "{}"
        emptyCompound.toMojangson() shouldEqual "{}"
        emptyCompound.toMojangsonWithColor() shouldEqual "{}"
    }

    @Test fun `NBTTagCompound - lookup - If the path is null or blank, should return null`() {
        val compound = ofCompound {  }
        compound.lookup(null) shouldEqual null
        compound.lookup("") shouldEqual null
        compound.lookupValue<String>(null) shouldEqual null
        compound.lookupValue<String>("") shouldEqual null
        compound.lookup("abc") shouldEqual null
    }

    @Test fun `NBTTagCompound - lookup`() {
        val compound = ofCompound {
            put(ofCompound("data") {
                putString("id", "diamond")
                put(ofList("flags") {
                    addByte(0, 1, 3, 6)
                })
                putInt("counts", 10)
            })
        }
        compound.lookup("dt") shouldEqual null
        compound.lookup("dt.id") shouldEqual null
        compound.lookup("data")?.type shouldEqual NBTType.TAG_COMPOUND
        compound.lookupValue<String>("data.id") shouldEqual "diamond"
        compound.lookupValue<Byte>("data.flags[2]") shouldEqual 3
        compound.lookupValue<NBTTagList>("data.flags")?.elementType shouldEqual NBTType.TAG_BYTE
        compound.lookupValue<String>("data.404 not found") shouldEqual null
        compound.lookup("data.counts[0]") shouldEqual null // should TAG_INT
    }

    @Test fun `NBTTagCompound - getOrDefault - TAG_END should be throw exception`() {
        val getOrDefaultMethod = NBTTagCompound::class.java
                                     .getDeclaredMethod("getOrDefault", NBTType::class.java, String::class.java) ?: return
        getOrDefaultMethod.isAccessible = true
        invoking {
            getOrDefaultMethod.invoke(ofCompound { }, NBTType.TAG_END, "END")
        } shouldThrow InvocationTargetException::class withCause UnsupportedOperationException::class
    }

    class MyNBT : NBTBase<Any>("my", Any()) {
        override fun toString(): String = throw UnsupportedOperationException()
        override fun clone(): NBTBase<*> = throw UnsupportedOperationException()
        override val type : NBTType get() = throw UnsupportedOperationException()
        override fun read(input: DataInput) { }
        override fun write(output: DataOutput) { }
    }

    @Test fun `NBTTagCompound - getAndCheck - Expected type mismatch, should throw exception`() {
        val getAndCheckMethod = NBTTagCompound::class.java
                                     .getDeclaredMethod("getAndCheck", String::class.java, Class::class.java, Boolean::class.java) ?: return
        getAndCheckMethod.isAccessible = true
        invoking {
            getAndCheckMethod.invoke(ofCompound {
                putInt("404", 1) // TAG_INT
            }, "404", MyNBT::class.java, false)
        } shouldThrow InvocationTargetException::class withCause ClassCastException::class
    }
}
