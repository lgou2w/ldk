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

/**
 * ## NBTSavable (可存 NBT 能力接口)
 *
 * * Implement this interface so it should have the ability to store data to `root`.
 * * 实现此接口那么其应该具备将数据存储到 `root` 能力.
 *
 * @see [save]
 * @see [NBTReadable]
 * @see [NBTTagCompound]
 * @author lgou2w
 */
interface NBTSavable {

  /**
   * * Store the data in the given `root` tag.
   * * 将数据存储到给定的 `root` 标签中.
   */
  fun save(root: NBTTagCompound): NBTTagCompound
}
