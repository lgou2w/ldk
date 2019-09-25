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

import com.lgou2w.ldk.nbt.NBTTagEnd.Companion.INSTANCE
import java.io.DataInput
import java.io.DataOutput

/**
 * ## NBTTagEnd (尾 NBT 标签)
 *
 * * This tag cannot create instance objects, please use the [INSTANCE] instance.
 * * 此标签不能创建实例对象, 请使用 [INSTANCE] 实例.
 *
 * @see [INSTANCE]
 * @author lgou2w
 */
class NBTTagEnd private constructor() : NBTBase<Any>("", Any()) {

  companion object {
    /**
     * * An empty `END` instance object.
     * * 空的 `END` 实例对象.
     */
    @JvmField
    val INSTANCE = NBTTagEnd()
  }

  @Suppress("UNUSED_PARAMETER")
  override var value: Any
    get() = super.value
    set(value) { }

  override val type = NBTType.TAG_END

  override fun read(input: DataInput) {
  }

  override fun write(output: DataOutput) {
  }

  override fun clone(): NBTBase<*> {
    return INSTANCE
  }

  override fun toString(): String {
    return "NBTTagEnd()"
  }

  override fun toJson(): String {
    return ""
  }

  override fun toMojangson(): String {
    return ""
  }
}
