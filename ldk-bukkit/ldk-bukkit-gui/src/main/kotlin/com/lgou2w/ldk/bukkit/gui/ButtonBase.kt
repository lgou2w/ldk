/*
 * Copyright (C) 2018 The lgou2w (lgou2w@hotmail.com)
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

import com.lgou2w.ldk.bukkit.item.ItemBuilder
import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.common.Consumer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class ButtonBase(
        override val parent: Gui,
        override val index: Int
) : Button {

    override var stack: ItemStack?
        get() = parent.inventory.getItem(index)
        set(value) { parent.inventory.setItem(index, value) }

    final override var onClicked: Consumer<ButtonEvent>? = null

    final override fun stackModify(modifier: Applicator<ItemBuilder>) {
        val stackClone = stack?.clone()
        if (stackClone != null && stackClone.type != Material.AIR)
            stack = ItemBuilder.of(stackClone).apply(modifier).build()
    }

    override fun hashCode(): Int {
        var result = parent.hashCode()
        result = 31 * result + index.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is ButtonBase)
            return parent == other.parent && index == other.index
        return false
    }

    override fun toString(): String {
        return "Button(index=$index)"
    }
}
