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
import org.amshove.kluent.shouldNotEqual
import org.junit.Test

class NBTTagLongArrayTest {

    @Test fun `NBTTagLongArray - toJson`() {
        val nbt = NBTTagLongArray(value = longArrayOf(0L, 1L, 1L))
        nbt.toJson() shouldEqual "[0,1,1]"
        nbt.toMojangson() shouldEqual "[0L,1L,1L]"
        nbt.toMojangsonWithColor() shouldEqual "[§60§cL§r, §61§cL§r, §61§cL§r]"
    }

    @Test fun `NBTTagLongArray - toJson - Empty values should only be parentheses`() {
        val nbt = NBTTagLongArray(value = longArrayOf())
        nbt.toJson() shouldEqual "[]"
    }

    @Test fun `NBTTagLongArray - equals`() {
        val nbt1 = NBTTagLongArray(name = "[L")
        val nbt2 = NBTTagLongArray(name = "[L")
        nbt1.value = longArrayOf(0L, 1L)
        nbt2.value = longArrayOf(1L, 0L)
        nbt1.value shouldNotEqual nbt2.value
        nbt1.value.size shouldEqual nbt2.value.size
        nbt1.equals(nbt2) shouldEqual false
        nbt2.value = nbt1.value // array clone, not reference
        nbt1.equals(nbt2) shouldEqual true
        nbt2.value = longArrayOf(1L, 0L) // Does not affect the value of nbt1
        nbt1.equals(nbt2) shouldEqual false
    }
}
