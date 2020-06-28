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

package com.lgou2w.ldk.chat

abstract class ChatComponentNBT(
  nbt: String,
  var interpret: Boolean? = null,
  var path: String? = null
) : ChatComponentAbstract() {

  var nbt = nbt
    @JvmName("getNBT") get
    @JvmName("setNBT") set

  override fun equals(other: Any?): Boolean {
    if (other === this)
      return true
    if (other is ChatComponentNBT)
      return super.equals(other) &&
        nbt == other.nbt &&
        interpret == other.interpret &&
        path == other.path
    return false
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + nbt.hashCode()
    result = 31 * result + interpret.hashCode()
    result = 31 * result + path.hashCode()
    return result
  }

  override fun toString(): String {
    return "ChatComponentNBT(nbt='$nbt', interpret=$interpret, path='$path' style=$style, extras=$extras)"
  }
}

class ChatComponentNBTBlock(
  nbt: String,
  interpret: Boolean? = null,
  path: String? = null
) : ChatComponentNBT(nbt, interpret, path)

class ChatComponentNBTEntity(
  nbt: String,
  interpret: Boolean? = null,
  path: String? = null
) : ChatComponentNBT(nbt, interpret, path)

class ChatComponentNBTStorage(
  nbt: String,
  interpret: Boolean? = null,
  path: String? = null
) : ChatComponentNBT(nbt, interpret, path)
