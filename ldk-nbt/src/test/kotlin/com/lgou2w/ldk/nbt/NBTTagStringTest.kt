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

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldEndWith
import org.amshove.kluent.shouldStartWith
import org.junit.Test

class NBTTagStringTest {

  @Test fun `NBTTagString - toJson - Strings always start and end with " `() {
    val nbt = NBTTagString(value = "string")
    nbt.toJson() shouldStartWith "\""
    nbt.toMojangson() shouldEndWith "\""
    nbt.toMojangsonWithColor() shouldStartWith "\""
    nbt.toJson() shouldBeEqualTo "\"string\""
  }

  @Test fun `NBTTagString - toJson - When value contains " should always be escaped`() {
    val nbt = NBTTagString(value = "\"Hi, I'm fine\"")
    nbt.toJson() shouldBeEqualTo "\"\\\"Hi, I'm fine\\\"\""
  }
}
