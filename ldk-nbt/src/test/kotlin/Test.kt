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

import com.lgou2w.ldk.nbt.*
import org.junit.Test
import java.io.ByteArrayOutputStream

class Test {

    private fun Any.println() = println(this)

    @Test
    fun test_NBTTo() {
        NBTTagByte().println()
        NBTTagShort().println()
        NBTTagInt().println()
        NBTTagLong().println()
        NBTTagFloat().println()
        NBTTagDouble().println()
        NBTTagString().println()
        NBTTagByteArray(byteArrayOf(0, 1, 2, 3)).println()
        NBTTagIntArray(intArrayOf(0, 1, 2, 3)).println()
        NBTTagLongArray(longArrayOf(0, 1, 2, 3)).println()
        NBTTagList(arrayListOf(NBTTagByte(0), NBTTagByte(1), NBTTagByte(2), NBTTagByte(3)) as MutableList<NBTBase<*>>).println()
        NBTTagCompound(mutableMapOf("0" to NBTTagByte(0) as NBTBase<*>, "1" to NBTTagByte(1) as NBTBase<*>)).println()
    }

    @Test
    fun test_NBTToJson() {
        NBTTagByte().toJson().println()
        NBTTagShort().toJson().println()
        NBTTagInt().toJson().println()
        NBTTagLong().toJson().println()
        NBTTagFloat().toJson().println()
        NBTTagDouble().toJson().println()
        NBTTagString().toJson().println()
        NBTTagByteArray(byteArrayOf(0, 1, 2, 3)).toJson().println()
        NBTTagIntArray(intArrayOf(0, 1, 2, 3)).toJson().println()
        NBTTagLongArray(longArrayOf(0, 1, 2, 3)).toJson().println()
        NBTTagList(arrayListOf(NBTTagByte(0), NBTTagByte(1), NBTTagByte(2), NBTTagByte(3)) as MutableList<NBTBase<*>>).toJson().println()
        NBTTagCompound(mutableMapOf("0" to NBTTagByte(0) as NBTBase<*>, "1" to NBTTagByte(1) as NBTBase<*>)).toJson().println()
    }

    @Test
    fun test_NBTToMojangson() {
        NBTTagByte().toMojangson().println()
        NBTTagShort().toMojangson().println()
        NBTTagInt().toMojangson().println()
        NBTTagLong().toMojangson().println()
        NBTTagFloat().toMojangson().println()
        NBTTagDouble().toMojangson().println()
        NBTTagString().toMojangson().println()
        NBTTagByteArray(byteArrayOf(0, 1, 2, 3)).toMojangson().println()
        NBTTagIntArray(intArrayOf(0, 1, 2, 3)).toMojangson().println()
        NBTTagLongArray(longArrayOf(0, 1, 2, 3)).toMojangson().println()
        NBTTagList(arrayListOf(NBTTagByte(0), NBTTagByte(1), NBTTagByte(2), NBTTagByte(3)) as MutableList<NBTBase<*>>).toMojangson().println()
        NBTTagCompound(mutableMapOf("0" to NBTTagByte(0) as NBTBase<*>, "1" to NBTTagByte(1) as NBTBase<*>)).toMojangson().println()
    }

    @Test
    fun test_NBTStreams() {
        val compound = ofCompound {  }
        compound.putString("id", "minecraft:diamond")
        compound.putByte("Count", 2)
        compound.putShort("Damage", 0)
        compound.toMojangson().println()
        NBTStreams.writeBase64(compound).println()
        NBTStreams.write(ByteArrayOutputStream(), compound)
    }

    @Test
    fun test_NBTOf() {
        val a = ofList { addString("1"); addString("2") }
        val b = ofList { addString("1"); addString("2") }
        a.hashCode().println()
        b.hashCode().println()
        println(a == b)
    }

    @Test(expected = ClassCastException::class)
    fun test_NBTConflict() {
        val tag = ofCompound {  }
        tag.putString("value", "122345688")
        tag.getInt("value").println()
    }

    @Test
    fun test_NBTClone() {
        val tag = NBTTagIntArray(intArrayOf(1, 2, 3, 4))
        val tag2 = tag.clone()
        tag2.value = intArrayOf(4, 3, 2, 1)
        tag.println()
        tag2.println()
    }

    @Test
    fun test_NBT0908() {
        ofCompound {
            put("display", ofCompound {
                putString("Name", "&aIron Sword")
                put("Lore", ofList {
                    addString("1")
                    addString("1")
                    addString("1")
                })
            })
            putByte("Unbreakable", 1)
        }.println()
    }

    @Test
    fun test_NBTListValue() {
        val list = ofList { addString("1", "2", "3") }
        val list2 = list.clone()
        val iterator = list.value.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().value == "2")
                iterator.remove()
        }
        println(list)
        println(list2)
    }

    @Test
    fun test_NBTCompoundValue() {
        val compound = ofCompound { putString("a", "1"); putString("b", "1") }
        val compound2 = compound.clone()
        compound.values.removeIf { it.value == "1" }
        println(compound)
        println(compound2)
    }

    @Test
    fun test_NBTWithColor() {
        NBTTagByteArray(byteArrayOf(1, 2, 3, 4, 5)).toMojangsonWithColor().println()
        NBTTagString("minecraft:iron_sword").toMojangsonWithColor().println()
        NBTTagList().apply {
            addString("1", "2", "3", "4", "5")
        }.toMojangsonWithColor().println()
        NBTTagCompound().apply {
            putByte("byte", 1)
            putString("string", "minecraft:iron")
            put("list", NBTTagList().apply { addString("1", "2", "3", "4", "5") })
        }.toMojangsonWithColor().println()
    }
}
