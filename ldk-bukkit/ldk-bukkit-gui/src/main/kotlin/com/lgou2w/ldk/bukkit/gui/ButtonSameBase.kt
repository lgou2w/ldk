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
import java.util.Arrays

/**
 * ## ButtonSameBase (相同类型按钮基础)
 *
 * @see [ButtonSame]
 * @author lgou2w
 */
open class ButtonSameBase(
        parent: Gui,
        protected val values: IntArray
) : ButtonBase(parent, values.first()),
        ButtonSame {

    override var stack : ItemStack?
        get() = super.stack
        set(value) { values.forEach { parent.inventory.setItem(it, value) } }

    final override val indexes : IntArray = values.clone()

    final override val stacks : List<ItemStack?>
        get() = values.map { parent.inventory.getItem(it) }

    final override fun isSame(index: Int): Boolean {
        return values.contains(index)
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is ButtonSameBase)
            return super.equals(other) && Arrays.equals(values, other.values)
        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + Arrays.hashCode(values)
        return result
    }

    override fun toString(): String {
        return "ButtonSame(indexes=${Arrays.toString(values)})"
    }
}
