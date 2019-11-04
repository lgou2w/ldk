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

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldEqual
import org.junit.Test

class NBTTest {

  @Test fun `ofList - Two empty list should be equal`() {
    val l1 = ofList {  }
    val l2 = ofList {  }
    l1 shouldEqual l2
    l1.equals(l2) shouldEqual true
    l1.isEmpty() shouldEqual l2.isEmpty()
  }

  @Test fun `ofCompound - Two empty compound should be equal`() {
    val c1 = ofCompound {  }
    val c2 = ofCompound {  }
    c1 shouldEqual c2
    c1.equals(c2) shouldEqual true
    c1.isEmpty() shouldEqual c1.isEmpty()
  }

  @Test fun `NBTTagCompound - removeIf - If the predicate is null, the entry should be removed directly`() {
    val compound = ofCompound {
      putInt("key", 123)
      put("tag", ofCompound {  })
    }
      .removeIf<Int>("key", null)
      .removeIf<NBTTagCompound>("tag", null)
    compound.getIntOrNull("key") shouldEqual null
    compound.containsKey("key") shouldEqual false
    compound.isEmpty() shouldEqual true
  }

  @Test fun `NBTTagCompound - removeIf - If the predicate is false, the entry should not be removed`() {
    val compound = ofCompound {
      putByte("key", 1)
      put("tag", ofCompound {  })
    }
      .removeIf<Byte>("key") { it == 0.toByte() }
      .removeIf<NBTTagCompound>("tag") { it.isNotEmpty() }
    compound.getByte("key") shouldEqual 1
    compound.containsKey("key") shouldEqual true
    compound.containsKey("tag") shouldEqual true
    compound.isEmpty() shouldEqual false
  }

  @Test fun `NBTTagCompound - removeIf - If the predicate is true, the entry should be removed`() {
    val compound = ofCompound {
      putShort("key", 1)
      put("list", ofList {  })
    }
      .removeIf<Short>("key") { it == 1.toShort() }
      .removeIf<NBTTagList>("list") { it.isEmpty() }
    compound.getShortOrNull("key") shouldEqual null
    compound.isEmpty() shouldEqual true
  }

  @Test fun `NBTTagCompound - removeIf - If the entry not found or predicate is not true, compound should not change`() {
    val compound = ofCompound { putByte("404", 1) }
      .removeIf<Byte>("233", null)
      .removeIf<Byte>("404") { it == 2.toByte() }
      .removeIf<Byte>("405") { it == 1.toByte() }
    compound shouldBe compound
    compound.size shouldEqual 1
  }

  @Test fun `NBTTagList - removeIf - If the predicate is null, the list should be clear`() {
    val list1 = ofList { addByte(0, 1, 2) }.removeIf<Byte>(null)
    val list2 = ofList { addByte(0, 1, 2) }.removeIf<Byte, String>({ it.toString() }, null)
    list1.isEmpty() shouldEqual true
    list2.isEmpty() shouldEqual true
  }

  @Test fun `NBTTagList - removeIf - If the predicate is all true, the list should be clear`() {
    val list1 = ofList { addByte(0, 1, 2) }.removeIf<Byte> { it < 3 }
    val list2 = ofList { addCompound(ofCompound {  }) }.removeIf<NBTTagCompound> { it.isEmpty() }
    val list3 = ofList { addShort(0, 1, 2) }.removeIf<Short, Int>({ it.toInt() }) { it < 3 }
    list1.isEmpty() shouldEqual true
    list2.isEmpty() shouldEqual true
    list3.isEmpty() shouldEqual true
  }

  @Test fun `NBTTagList - removeIf - If the predicate is false, the list should not change`() {
    val list = ofList { addByte(0, 1, 2) }
      .removeIf<Byte> { it > 3 }
    list.size shouldEqual 3
    list.asElements<Byte>() shouldContainAll arrayOf<Byte>(0, 1, 2)
  }

  @Test fun `NBT - Constants`() {
    NBT.Constants shouldBe NBT.Constants
  }
}
