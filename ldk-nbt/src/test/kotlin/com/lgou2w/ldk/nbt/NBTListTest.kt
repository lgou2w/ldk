/*
 * Copyright (C) 2016-2020 The lgou2w <lgou2w@hotmail.com>
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
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotContain
import org.amshove.kluent.shouldThrow
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

class NBTListTest {

  @Test fun `NBTTagList - type of element should be the same`() {
    val list = NBTTagList()
    list.addByte(0, 1, 2)
    invoking { list.addInt(3) } shouldThrow IllegalArgumentException::class
    list.addByte(3, 4, 5)
    list.size shouldBeEqualTo 6
    list.elementType shouldBeEqualTo NBTType.TAG_BYTE
  }

  @Test fun `NBTTagList - cloned object does not affect the other party`() {
    val list = NBTTagList("l1")
    list.addLong(0L, 1L)
    val clone = list.clone()
    clone.removeAt(1) // remove 1L
    list.size shouldBeEqualTo 2
    clone.size shouldBeEqualTo 1 // removed
    list shouldNotBeEqualTo clone
    list.name shouldBeEqualTo clone.name
    list.hashCode() shouldNotBeEqualTo clone.hashCode()
  }

  @Test fun `NBTTagList - method validation from some list`() {
    val list = NBTTagList("list")
    val el1 = NBTTagFloat(1f)
    val el2 = NBTTagFloat(2f)
    val el3 = NBTTagFloat(3f)
    list.add(el1)
    list.addAll(listOf(el2, el3))
    list.size shouldBeEqualTo 3
    list.indexOf(el1) shouldBeEqualTo 0
    list.lastIndexOf(el2) shouldBeEqualTo 1
    list.remove(el2) shouldBeEqualTo true
    list shouldContainAll listOf(el1, el3)
    list.size shouldBeEqualTo 2
    list[0] shouldBeEqualTo el1
    list[1] shouldBeEqualTo el3
    list[1] = el2
    list shouldNotContain el3
    list.add(1, el3) // 0 = el1, 1 = el3, 2 = el2
    list.size shouldBeEqualTo 3
    list[2] shouldBeEqualTo el2
    list[1] shouldBeEqualTo el3
    list[1].value shouldBeEqualTo 3f
    el3.value = 10f
    list[1].value shouldBeEqualTo 10f
    list.removeAll(arrayOf(el1, el2, el3))
    list.size shouldBeEqualTo 0
    list.isEmpty() shouldBeEqualTo true
  }

  @Test fun `NBTTagList - method validation from some list 2`() {
    val rawList = ArrayList<NBTBase<*>>(listOf(NBTTagDouble(-1.0)))
    val list = NBTTagList(rawList)
    list.addAll(0, listOf(NBTTagDouble(0.0), NBTTagDouble(1.0)))
    list.addDouble(2.0, 3.0, 4.0)
    list.size shouldBeEqualTo 6
    list.value shouldBe rawList
    list.containsAll(rawList) shouldBeEqualTo true
    list.listIterator() shouldNotBeEqualTo null
    list.listIterator(0) shouldNotBeEqualTo null
    list.subList(0, 0).size shouldBeEqualTo 0
  }

  @Test fun `NBTTagList - equals`() {
    val list1 = NBTTagList(); list1.addByte(0, 1)
    val list2 = NBTTagList(); list2.addShort(0, 1)
    val rawList = listOf(0.toShort(), 1)
    list1 shouldNotBeEqualTo list2
    list1 shouldNotBeEqualTo rawList
    list2 shouldNotBeEqualTo rawList
    list2.asElements<Short>() shouldBeEqualTo rawList
  }

  @Test fun `NBTTagList - asElements - wrapper type validation`() {
    val list = NBTTagList()
    list.addCompound(ofCompound {  }, ofCompound {  })
    list.asElements<NBTTagCompound>().size shouldBeEqualTo 2
    val list2 = NBTTagList()
    list2.addList(ofList {  }, ofList {  })
    list2.asElements<NBTTagList>().size shouldBeEqualTo 2
  }

  @Test fun `NBTTagList - toJson - If the list is empty, then json should only have parentheses`() {
    val list = NBTTagList()
    list.toJson() shouldBeEqualTo "[]"
    list.toMojangson() shouldBeEqualTo "[]"
    list.toMojangsonWithColor() shouldBeEqualTo "[]"
  }

  @Test fun `NBTTagList - toJson - equals`() {
    val list = NBTTagList()
    list.addBoolean(true, false, true, false) // 1, 0, 1, 0
    list.toJson() shouldBeEqualTo "[1,0,1,0]"
    list.toMojangson() shouldBeEqualTo "[1b,0b,1b,0b]"
    list.toMojangsonWithColor() shouldBeEqualTo // there is a space after the comma, number gold color: §6
      "[§61§cb§r, §60§cb§r, §61§cb§r, §60§cb§r]"
  }

  @Test fun `NBTTagList - adds`() {
    NBTTagList().addByteArray(byteArrayOf(0, 1), byteArrayOf(2, 3)) shouldBeEqualTo true
    NBTTagList().addIntArray(intArrayOf(0, 1), intArrayOf(2, 3)) shouldBeEqualTo true
    NBTTagList().addByte(0.toByte(), 1.toByte(), 2.toByte()) shouldBeEqualTo true
    NBTTagList().addShort(0.toShort(), 1.toShort()) shouldBeEqualTo true
    NBTTagList().addFloat(0f, 1f, 2f, 3f) shouldBeEqualTo true
    NBTTagList().addString("0", "1", "2", "3") shouldBeEqualTo true
  }

  @Test fun `NBTTagList - value set`() {
    val list = NBTTagList()
    list.value = arrayListOf(NBTTagInt(1))
    list.size shouldBeEqualTo 1
    list.elementType shouldBeEqualTo NBTType.TAG_INT
  }

  @Test fun `NBTTagList - retainAll`() {
    ofList { addInt(0, 2, 4, 6) }
      .apply { retainAll(ofList { addInt(1, 2, 3, 6) }) }
      .size shouldBeEqualTo 2 // have 2 and 6
    ofList {  }
      .apply { retainAll(ofList {  }) }
      .isEmpty() shouldBeEqualTo true
  }

  @Test fun `NBTTagList - write - If the empty list should be 5 bytes`() {
    val list = ofList {  }
    val baos = ByteArrayOutputStream()
    list.write(DataOutputStream(baos))
    val bytes = baos.toByteArray()
    bytes.size shouldBeEqualTo 5
    bytes[0] shouldBeEqualTo 0 // elementType : TAG_END
    bytes.sum() shouldBeEqualTo 0 // empty list
  }

  @Test fun `NBTTagList - read - 5 bytes of zero value should be empty list`() {
    val bytes = byteArrayOf(127, 0, 0, 0, 0) // If the first byte is invalid, then TAG_END
    val list = ofList {  }
    list.read(DataInputStream(ByteArrayInputStream(bytes)))
    list.size shouldBeEqualTo 0
    list.isEmpty() shouldBeEqualTo true
  }
}
