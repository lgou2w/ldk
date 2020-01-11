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
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

class NBTTagEndTest {

  @Test fun `NBTTagEnd - clone should always be a singleton instance`() {
    NBTTagEnd.INSTANCE.clone() shouldBe NBTTagEnd.INSTANCE
    NBTTagEnd.INSTANCE.type shouldBeEqualTo NBTType.TAG_END
  }

  @Test fun `NBTTagEnd - toJson should always be a empty string`() {
    val inst = NBTTagEnd.INSTANCE
    inst.toJson() shouldBeEqualTo ""
    inst.toMojangson() shouldBeEqualTo ""
    inst.toMojangsonWithColor() shouldBeEqualTo ""
  }

  @Test fun `NBTTagEnd - value set should be useless`() {
    val newAny = Any() // new instance
    NBTTagEnd.INSTANCE.value = newAny // useless
    NBTTagEnd.INSTANCE.value shouldNotBeEqualTo newAny // !newAny
    NBTTagEnd.INSTANCE.value shouldBeEqualTo NBTTagEnd.INSTANCE.value // ok
  }

  @Test fun `NBTTagEnd - read and write should be useless`() {
    val data = byteArrayOf(0, 1, 0, 1, 0, 1, 0)
    val baos = ByteArrayOutputStream()
    val input = DataInputStream(ByteArrayInputStream(data))
    val output = DataOutputStream(baos)
    NBTTagEnd.INSTANCE.read(input)
    NBTTagEnd.INSTANCE.write(output)
    input.available() shouldBeEqualTo data.size
    baos.size() shouldBeEqualTo 0
  }
}
