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

import com.lgou2w.ldk.common.Consumer
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

interface Gui : InventoryHolder, Iterable<ItemStack> {

    var parent : Gui?

    val type : GuiType

    val title : String

    val size : Int

    val buttons: List<Button>

    fun hasParent() : Boolean

    var isAllowMove : Boolean

    var onOpened: ((gui: Gui, event: InventoryOpenEvent) -> Unit)?

    var onClosed: ((gui: Gui, event: InventoryCloseEvent) -> Unit)?

    fun open(human: HumanEntity)

    fun hasButton() : Boolean

    fun getButton(index: Int) : Button?

    fun getButton(x: Int, y: Int) : Button?

    fun isButton(index: Int) : Boolean

    fun isButton(x: Int, y: Int) : Boolean

    fun removeButton(button: Button) : Boolean

    fun removeButton(index: Int) : Boolean

    fun removeButton(x: Int, y: Int) : Boolean

    fun removeButtons()

    fun addButton() : Button

    fun addButton(stack: ItemStack? = null) : Button

    fun addButton(onClicked: Consumer<ButtonEvent>? = null) : Button

    fun addButton(stack: ItemStack? = null, onClicked: Consumer<ButtonEvent>? = null) : Button

    fun setButton(index: Int) : Button

    fun setButton(index: Int, stack: ItemStack? = null) : Button

    fun setButton(index: Int, onClicked: Consumer<ButtonEvent>? = null) : Button

    fun setButton(index: Int, stack: ItemStack? = null, onClicked: Consumer<ButtonEvent>? = null) : Button

    fun setButton(x: Int, y: Int) : Button

    fun setButton(x: Int, y: Int, stack: ItemStack? = null) : Button

    fun setButton(x: Int, y: Int, onClicked: Consumer<ButtonEvent>? = null) : Button

    fun setButton(x: Int, y: Int, stack: ItemStack? = null, onClicked: Consumer<ButtonEvent>? = null) : Button

    fun setSameButton(indexes: IntArray) : ButtonSame

    fun setSameButton(indexes: IntArray, stack: ItemStack? = null) : ButtonSame

    fun setSameButton(indexes: IntArray, onClicked: Consumer<ButtonEvent>? = null) : ButtonSame

    fun setSameButton(indexes: IntArray, stack: ItemStack? = null, onClicked: Consumer<ButtonEvent>? = null) : ButtonSame
}
