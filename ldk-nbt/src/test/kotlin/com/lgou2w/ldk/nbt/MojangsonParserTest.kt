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
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.junit.Test

class MojangsonParserTest {

    @Test fun `MojangsonParser - Standard test`() {
        // Standard tag
        var mojangson = "{byte:1b,short:1s,int:1,long:1L,float:1f,double:1.0,double2:1.0d,string:\"1\",yes:true,no:false,invalid:a.0f}"
        var compound = MojangsonParser.parse(mojangson)
        compound["byte"] shouldBeInstanceOf NBTTagByte::class
        compound["short"] shouldBeInstanceOf NBTTagShort::class
        compound["int"] shouldBeInstanceOf NBTTagInt::class
        compound["long"] shouldBeInstanceOf NBTTagLong::class
        compound["float"] shouldBeInstanceOf NBTTagFloat::class
        compound["double"] shouldBeInstanceOf NBTTagDouble::class
        compound["double2"] shouldBeInstanceOf NBTTagDouble::class
        compound["string"] shouldBeInstanceOf NBTTagString::class
        compound["yes"] shouldBeInstanceOf NBTTagByte::class
        compound["no"] shouldBeInstanceOf NBTTagByte::class
        compound["invalid"] shouldBeInstanceOf NBTTagString::class // invalid type always string
        // Typed array
        mojangson = "{barr:[B;1b,2b],iarr:[I;1,2],larr:[L;1L,2L]}"
        compound = MojangsonParser.parse(mojangson)
        compound["barr"] shouldBeInstanceOf NBTTagByteArray::class
        compound["iarr"] shouldBeInstanceOf NBTTagIntArray::class
        compound["larr"] shouldBeInstanceOf NBTTagLongArray::class
        // List
        mojangson = "{list:[1,2,3],list2:[1b,2b,3b]}"
        compound = MojangsonParser.parse(mojangson)
        compound["list"] shouldBeInstanceOf NBTTagList::class
        compound["list2"]?.asList()?.elementType shouldEqual NBTType.TAG_BYTE
    }

    @Test fun `MojangsonParser - Support for lenient JSON string`() {
        val mojangson = "  {  lenient   :   1b  ,  map  :  {  k   :   1.0  }  }   "
        val compound = MojangsonParser.parse(mojangson)
        compound["lenient"] shouldBeInstanceOf NBTTagByte::class
        compound["lenient"]?.value shouldEqual 1.toByte()
        compound["map"] shouldBeInstanceOf NBTTagCompound::class
        compound["map"]?.asCompound()?.get("k")?.value shouldEqual 1.0

        MojangsonParser.parseValue("[B; 1b]") shouldBeInstanceOf NBTTagByteArray::class // must  [B;
        invoking { MojangsonParser.parseValue(" [  B;   1b  ]") } shouldThrow IllegalArgumentException::class

        MojangsonParser.parseValue(" 1b ") shouldBeInstanceOf NBTTagByte::class
        invoking { MojangsonParser.parseValue("   ?  ") } shouldThrow IllegalArgumentException::class
        invoking { MojangsonParser.parse("{}abc") } shouldThrow IllegalArgumentException::class
    }

    @Test fun `MojangsonParser - Some incorrect JSON string`() {
        invoking { MojangsonParser.parse("") } shouldThrow IllegalArgumentException::class
        invoking { MojangsonParser.parse(" { foo : bar  ") } shouldThrow IllegalArgumentException::class
        invoking { MojangsonParser.parse("  [  1,  2,  ] ") } shouldThrow IllegalArgumentException::class
        invoking { MojangsonParser.parse("  {  : value  }  ") } shouldThrow IllegalArgumentException::class
        invoking { MojangsonParser.parseValue("[   ") } shouldThrow IllegalArgumentException::class
        invoking { MojangsonParser.parseValue("{   ") } shouldThrow IllegalArgumentException::class
    }

    @Test fun `MojangsonParser - Type array elements require a suffix`() {
        val byteArray = MojangsonParser.parseValue("[B; 1b, 2b, 3b ]")
        byteArray shouldBeInstanceOf NBTTagByteArray::class
        (byteArray as NBTTagByteArray).value shouldContainAll byteArrayOf(1, 2, 3)
        val intArray = MojangsonParser.parseValue("[I; 1, 2, 3]")
        intArray shouldBeInstanceOf NBTTagIntArray::class
        (intArray as NBTTagIntArray).value shouldContainAll intArrayOf(1, 2, 3)
        val longArray = MojangsonParser.parseValue("[L; 1L, 2L, 3L]")
        longArray shouldBeInstanceOf NBTTagLongArray::class
        (longArray as NBTTagLongArray).value shouldContainAll longArrayOf(1, 2, 3)

        // Unsupported float array
        invoking { MojangsonParser.parseValue("[F; 1f, 2f]") } shouldThrow IllegalArgumentException::class
    }

    @Test fun `MojangsonParser - The elements of the array and list must be of the same type`() {
        invoking { MojangsonParser.parseValue("[B; 1b, 2B, 3L ]") } shouldThrow IllegalArgumentException::class
        invoking { MojangsonParser.parseValue("[1, 2, 3b]") } shouldThrow IllegalArgumentException::class
        val list = MojangsonParser.parseValue("[1, 2, 3]").asList()
        list shouldBeInstanceOf NBTTagList::class
        list.elementType shouldEqual NBTType.TAG_INT
        list.size shouldEqual 3
        list[0].value shouldEqual 1
        list[1].value shouldEqual 2
        list[2].value shouldEqual 3
    }
}
