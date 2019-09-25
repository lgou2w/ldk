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

import org.amshove.kluent.shouldEqual
import org.junit.Test

class NBTTagNumberTest {

  @Test fun `NBTTagNumber - number type conversion`() {
    val nbt = NBTTagByte(value = 49.toByte())
    nbt.toByte() shouldEqual 49
    nbt.toChar() shouldEqual '1'
    nbt.toShort() shouldEqual 49
    nbt.toInt() shouldEqual 49
    nbt.toLong() shouldEqual 49L
    nbt.toFloat() shouldEqual 49f
    nbt.toDouble() shouldEqual 49.0
  }
}
