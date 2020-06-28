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
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.Test
import java.io.DataInput
import java.io.DataOutput

class NBTBaseTest {

  @Suppress("ReplaceCallWithBinaryOperator")
  @Test fun `NBTBase - equals`() {
    val n1 = NBTTagString()
    val n2 = NBTTagInt()
    n1.equals(n2) shouldBeEqualTo false
    n1.name shouldBeEqualTo n2.name
    n1.value shouldNotBeEqualTo n2.value
  }

  class MyNbt : NBTBase<Any?>("mynbt", null) {
    override fun clone(): NBTBase<*> = throw UnsupportedOperationException()
    override val type: NBTType get() = throw UnsupportedOperationException()
    override val typeId: Int = 233
    override fun read(input: DataInput) { }
    override fun write(output: DataOutput) { }
    override fun toString(): String = ""
  }

  @Test fun `NBTBase - hashCode - The hash value should not be zero`() {
    NBTTagString().hashCode() shouldNotBeEqualTo 0
    NBTTagInt().hashCode() shouldNotBeEqualTo 0
    MyNbt().hashCode() shouldNotBeEqualTo 0
  }

  @Test fun `NBTBase - as - Non-wrapper class should throw exception`() {
    invoking { NBTTagString().asCompound() } shouldThrow ClassCastException::class
    invoking { NBTTagInt().asList() } shouldThrow ClassCastException::class
    ofList {  }.asList().isEmpty() shouldBeEqualTo true
    ofCompound {  }.asCompound().isEmpty() shouldBeEqualTo true
  }

  @Test fun `NBTBase - clone - The cloned object address should be different`() {
    val n1 = NBTTagByte()
    val n2 = NBTTagShort()
    val n3 = NBTTagInt()
    val n4 = NBTTagLong()
    val n5 = NBTTagFloat()
    val n6 = NBTTagDouble()
    val n7 = NBTTagByteArray()
    val n8 = NBTTagString()
    val n9 = NBTTagList()
    val n10 = NBTTagCompound()
    val n11 = NBTTagIntArray()
    val n12 = NBTTagLongArray()
    n1 shouldNotBe n1.clone()
    n2 shouldNotBe n2.clone()
    n3 shouldNotBe n3.clone()
    n4 shouldNotBe n4.clone()
    n5 shouldNotBe n5.clone()
    n6 shouldNotBe n6.clone()
    n7 shouldNotBe n7.clone()
    n8 shouldNotBe n8.clone()
    n9 shouldNotBe n9.clone()
    n10 shouldNotBe n10.clone()
    n11 shouldNotBe n11.clone()
    n12 shouldNotBe n12.clone()
  }
}
