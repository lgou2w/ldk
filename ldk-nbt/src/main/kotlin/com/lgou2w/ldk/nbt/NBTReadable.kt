/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
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
 * ## NBTReadable (可读 NBT 能力接口)
 *
 * * Implement this interface so it should have the ability to read data from `root`.
 * * 实现此接口那么其应该具备从 `root` 读取数据能力.
 *
 * @see [load]
 * @see [NBTSavable]
 * @see [NBTTagCompound]
 * @author lgou2w
 */
interface NBTReadable {

    /**
     * * Read the required data from the given [root] tag.
     * * 从给定的 [root] 标签中读取需要的数据.
     */
    fun load(root: NBTTagCompound): NBTTagCompound
}
