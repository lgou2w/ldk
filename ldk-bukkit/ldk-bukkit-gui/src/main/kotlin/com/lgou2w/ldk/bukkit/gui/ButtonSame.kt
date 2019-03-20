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

package com.lgou2w.ldk.bukkit.gui

import org.bukkit.inventory.ItemStack

/**
 * ## ButtonSame (相同类型按钮)
 *
 * @see [Button]
 * @see [ButtonSameBase]
 * @author lgou2w
 */
interface ButtonSame : Button {

    /**
     * * The indexed array value of this button.
     * * 此按钮的索引数组值.
     */
    val indexes : IntArray

    /**
     * * A list of indexed item stacks for this button.
     * * 此按钮的索引物品栈列表.
     */
    val stacks : List<ItemStack?>

    /**
     * * Get the same type from the given [index].
     * * 从给定的索引 [index] 获取是否为相同类型.
     */
    fun isSame(index: Int): Boolean
}
