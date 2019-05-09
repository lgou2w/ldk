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
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotContain
import org.amshove.kluent.shouldNotEqual
import org.amshove.kluent.shouldThrow
import org.junit.Test

class NBTListTest {

    @Test fun `NBTTagList - type of element should be the same`() {
        val list = NBTTagList()
        list.addByte(0, 1, 2)
        invoking { list.addInt(3) } shouldThrow IllegalArgumentException::class
        list.addByte(3, 4, 5)
        list.size shouldEqual 6
        list.elementType shouldEqual NBTType.TAG_BYTE
    }

    @Test fun `NBTTagList - cloned object does not affect the other party`() {
        val list = NBTTagList("l1")
        list.addLong(0L, 1L)
        val clone = list.clone()
        clone.removeAt(1) // remove 1L
        list.size shouldEqual 2
        clone.size shouldEqual 1 // removed
        list shouldNotEqual clone
        list.name shouldEqual clone.name
        list.hashCode() shouldNotEqual clone.hashCode()
    }

    @Test fun `NBTTagList - method validation from some list`() {
        val list = NBTTagList("list")
        val el1 = NBTTagFloat(1f)
        val el2 = NBTTagFloat(2f)
        val el3 = NBTTagFloat(3f)
        list.add(el1)
        list.addAll(listOf(el2, el3))
        list.size shouldEqual 3
        list.indexOf(el1) shouldEqual 0
        list.lastIndexOf(el2) shouldEqual 1
        list.remove(el2) shouldEqual true
        list shouldContainAll listOf(el1, el3)
        list.size shouldEqual 2
        list[0] shouldEqual el1
        list[1] shouldEqual el3
        list[1] = el2
        list shouldNotContain el3
        list.add(1, el3) // 0 = el1, 1 = el3, 2 = el2
        list.size shouldEqual 3
        list[2] shouldEqual el2
        list[1] shouldEqual el3
        list[1].value shouldEqual 3f
        el3.value = 10f
        list[1].value shouldEqual 10f
        list.removeAll(arrayOf(el1, el2, el3))
        list.size shouldEqual 0
        list.isEmpty() shouldEqual true
    }

    @Test fun `NBTTagList - method validation from some list 2`() {
        val rawList = ArrayList<NBTBase<*>>(listOf(NBTTagDouble(-1.0)))
        val list = NBTTagList(rawList)
        list.addAll(0, listOf(NBTTagDouble(0.0), NBTTagDouble(1.0)))
        list.addDouble(2.0, 3.0, 4.0)
        list.size shouldEqual 6
        list.value shouldBe rawList
        list.containsAll(rawList) shouldEqual true
        list.listIterator() shouldNotEqual null
        list.listIterator(0) shouldNotEqual null
        list.subList(0, 0).size shouldEqual 0
    }

    @Test fun `NBTTagList - equals`() {
        val list1 = NBTTagList(); list1.addByte(0, 1)
        val list2 = NBTTagList(); list2.addShort(0, 1)
        val rawList = listOf(0.toShort(), 1)
        list1 shouldNotEqual list2
        list1 shouldNotEqual rawList
        list2 shouldNotEqual rawList
        list2.asElements<Short>() shouldEqual rawList
    }

    @Test fun `NBTTagList - asElements - wrapper type validation`() {
        val list = NBTTagList()
        list.addCompound(ofCompound {  }, ofCompound {  })
        list.asElements<NBTTagCompound>().size shouldEqual 2
        val list2 = NBTTagList()
        list2.addList(ofList {  }, ofList {  })
        list2.asElements<NBTTagList>().size shouldEqual 2
    }

    @Test fun `NBTTagList - toJson - If the list is empty, then json should only have parentheses`() {
        val list = NBTTagList()
        list.toJson() shouldEqual "[]"
        list.toMojangson() shouldEqual "[]"
        list.toMojangsonWithColor() shouldEqual "[]"
    }

    @Test fun `NBTTagList - toJson - equals`() {
        val list = NBTTagList()
        list.addBoolean(true, false, true, false) // 1, 0, 1, 0
        list.toJson() shouldEqual "[1,0,1,0]"
        list.toMojangson() shouldEqual "[1b,0b,1b,0b]"
        list.toMojangsonWithColor() shouldEqual // there is a space after the comma, number gold color: §6
                "[§61§cb§r, §60§cb§r, §61§cb§r, §60§cb§r]"
    }

    @Test fun `NBTTagList - adds`() {
        NBTTagList().addByteArray(byteArrayOf(0, 1), byteArrayOf(2, 3)) shouldEqual true
        NBTTagList().addIntArray(intArrayOf(0, 1), intArrayOf(2, 3)) shouldEqual true
        NBTTagList().addByte(0.toByte(), 1.toByte(), 2.toByte()) shouldEqual true
        NBTTagList().addShort(0.toShort(), 1.toShort()) shouldEqual true
        NBTTagList().addFloat(0f, 1f, 2f, 3f) shouldEqual true
        NBTTagList().addString("0", "1", "2", "3") shouldEqual true
    }

    @Test fun `NBTTagList - value set`() {
        val list = NBTTagList()
        list.value = arrayListOf(NBTTagInt(1))
        list.size shouldEqual 1
        list.elementType shouldEqual NBTType.TAG_INT
    }

    @Test fun `NBTTagList - retainAll`() {
        ofList { addInt(0, 2, 4, 6) }
            .apply { retainAll(ofList { addInt(1, 2, 3, 6) }) }
            .size shouldEqual 2 // have 2 and 6
        ofList {  }
            .apply { retainAll(ofList {  }) }
            .isEmpty() shouldEqual true
    }
}
