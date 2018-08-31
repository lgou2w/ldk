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

    fun Any.println() = println(this)

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
        NBTTagList(arrayListOf(NBTTagByte(0), NBTTagByte(1), NBTTagByte(2), NBTTagByte(3))).println()
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
        NBTTagList(arrayListOf(NBTTagByte(0), NBTTagByte(1), NBTTagByte(2), NBTTagByte(3))).toJson().println()
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
        NBTTagList(arrayListOf(NBTTagByte(0), NBTTagByte(1), NBTTagByte(2), NBTTagByte(3))).toMojangson().println()
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
        val a = ofList<NBTTagString> { addString("1"); addString("2") }
        val b = ofList<NBTTagString> { addString("1"); addString("2") }
        a.hashCode().println()
        b.hashCode().println()
        println(a == b)
    }
}
