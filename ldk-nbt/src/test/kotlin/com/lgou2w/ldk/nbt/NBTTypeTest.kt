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

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Test

class NBTTypeTest {

  @Test fun `NBTType - isNumber - byte, short, int, long, float, double should be number type`() {
    NBTType.TAG_BYTE.isNumber() &&
      NBTType.TAG_SHORT.isNumber() &&
      NBTType.TAG_INT.isNumber() &&
      NBTType.TAG_FLOAT.isNumber() &&
      NBTType.TAG_DOUBLE.isNumber() shouldBeEqualTo true
    NBTType.TAG_LIST.isNumber() &&
      NBTType.TAG_COMPOUND.isNumber() shouldBeEqualTo false
  }

  @Test fun `NBTType - fromId - zero id should be TAG_END type`() {
    val type = NBTType.fromId(0)
    type shouldBeEqualTo NBTType.TAG_END
    type?.id shouldBeEqualTo 0
    type?.primitive shouldBeEqualTo java.lang.Void.TYPE
    type?.reference shouldBeEqualTo java.lang.Void::class.java
    type?.wrapped shouldBeEqualTo NBTTagEnd::class.java
    type?.mojangsonSuffix shouldBe null
  }

  @Test fun `NBTType - createTag - TAG_END should be a singleton instance`() {
    val inst = NBTType.createTag(NBTType.TAG_END)
    inst shouldBe NBTTagEnd.INSTANCE
  }

  @Test fun `NBTType - createTag - other types should be normal instances`() {
    NBTType.createTag(NBTType.TAG_BYTE) shouldBeInstanceOf NBTTagByte::class
    NBTType.createTag(NBTType.TAG_SHORT) shouldBeInstanceOf NBTTagShort::class
    NBTType.createTag(NBTType.TAG_INT) shouldBeInstanceOf NBTTagInt::class
    NBTType.createTag(NBTType.TAG_LONG) shouldBeInstanceOf NBTTagLong::class
    NBTType.createTag(NBTType.TAG_FLOAT) shouldBeInstanceOf NBTTagFloat::class
    NBTType.createTag(NBTType.TAG_DOUBLE) shouldBeInstanceOf NBTTagDouble::class
    NBTType.createTag(NBTType.TAG_BYTE_ARRAY) shouldBeInstanceOf NBTTagByteArray::class
    NBTType.createTag(NBTType.TAG_STRING) shouldBeInstanceOf NBTTagString::class
    NBTType.createTag(NBTType.TAG_LIST) shouldBeInstanceOf NBTTagList::class
    NBTType.createTag(NBTType.TAG_COMPOUND) shouldBeInstanceOf NBTTagCompound::class
    NBTType.createTag(NBTType.TAG_INT_ARRAY) shouldBeInstanceOf NBTTagIntArray::class
    NBTType.createTag(NBTType.TAG_LONG_ARRAY) shouldBeInstanceOf NBTTagLongArray::class
  }
}
