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

import com.lgou2w.ldk.common.Consumer
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

interface Gui : InventoryHolder, Iterable<ItemStack> {

    var parent : Gui?

    val type : GuiType

    val title : String

    val size : Int

    val buttons : List<Button>

    val buttonSize : Int

    fun hasParent() : Boolean

    var isAllowMove : Boolean

    var onOpened : ((gui: Gui, event: InventoryOpenEvent) -> Unit)?

    var onClosed : ((gui: Gui, event: InventoryCloseEvent) -> Unit)?

    /**
     * * Called when the button click is processed. Can handle non-button items.
     * * 当按钮点击处理完后被调用. 可以处理非按钮物品.
     *
     * @since LDK 0.1.7-rc5
     */
    var onClicked : ((gui: Gui, event: InventoryClickEvent) -> Unit)?

    /**************************************************************************
     *
     * Properties Holder
     *
     **************************************************************************/

    val properties : Map<String, Any>

    fun setProperty(key: String, value: Any): Any?

    fun getProperty(key: String, def: Any? = null): Any?

    fun getProperty(key: String): Any?

    fun <T> getPropertyAs(key: String, def: T? = null): T?

    fun <T> getPropertyAs(key: String): T?

    fun <T> getPropertyAsNotNull(key: String, def: T): T

    fun <T> getPropertyAsNotNull(key: String): T

    fun containsProperty(key: String): Boolean

    fun removeProperty(key: String): Any?

    fun clearProperties()

    /**************************************************************************
     *
     * API
     *
     **************************************************************************/

    fun open(human: HumanEntity)

    fun hasButton(): Boolean

    fun getButton(index: Int): Button?

    fun getButton(x: Int, y: Int): Button?

    fun isButton(index: Int): Boolean

    fun isButton(x: Int, y: Int): Boolean

    fun removeButton(button: Button): Boolean

    fun removeButton(index: Int): Boolean

    fun removeButton(x: Int, y: Int): Boolean

    fun removeButtons()

    @Throws(IllegalStateException::class)
    fun addButton(): Button

    @Throws(IllegalStateException::class)
    fun addButton(stack: ItemStack? = null): Button

    @Throws(IllegalStateException::class)
    fun addButton(onClicked: Consumer<ButtonEvent>? = null): Button

    @Throws(IllegalStateException::class)
    fun addButton(stack: ItemStack? = null, onClicked: Consumer<ButtonEvent>? = null): Button

    @Throws(IllegalArgumentException::class)
    fun setButton(index: Int): Button

    @Throws(IllegalArgumentException::class)
    fun setButton(index: Int, stack: ItemStack? = null): Button

    @Throws(IllegalArgumentException::class)
    fun setButton(index: Int, onClicked: Consumer<ButtonEvent>? = null): Button

    @Throws(IllegalArgumentException::class)
    fun setButton(index: Int, stack: ItemStack? = null, onClicked: Consumer<ButtonEvent>? = null): Button

    @Throws(IllegalArgumentException::class)
    fun setButton(x: Int, y: Int): Button

    @Throws(IllegalArgumentException::class)
    fun setButton(x: Int, y: Int, stack: ItemStack? = null): Button

    @Throws(IllegalArgumentException::class)
    fun setButton(x: Int, y: Int, onClicked: Consumer<ButtonEvent>? = null): Button

    @Throws(IllegalArgumentException::class)
    fun setButton(x: Int, y: Int, stack: ItemStack? = null, onClicked: Consumer<ButtonEvent>? = null): Button

    @Throws(IllegalArgumentException::class)
    fun setSameButton(indexes: IntArray): ButtonSame

    @Throws(IllegalArgumentException::class)
    fun setSameButton(indexes: IntArray, stack: ItemStack? = null): ButtonSame

    @Throws(IllegalArgumentException::class)
    fun setSameButton(indexes: IntArray, onClicked: Consumer<ButtonEvent>? = null): ButtonSame

    @Throws(IllegalArgumentException::class)
    fun setSameButton(indexes: IntArray, stack: ItemStack? = null, onClicked: Consumer<ButtonEvent>? = null): ButtonSame
}
