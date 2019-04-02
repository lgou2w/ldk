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

package com.lgou2w.ldk.bukkit.gui

import com.lgou2w.ldk.common.Consumer
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

/**
 * ## Gui (界面)
 *
 * @see [GuiBase]
 * @see [SimpleGui]
 * @see [PageableGui]
 * @author lgou2w
 */
interface Gui : InventoryHolder, Iterable<ItemStack> {

    /**
     * * The parent Gui object of this Gui.
     * * 此 Gui 的父 Gui 对象.
     */
    var parent : Gui?

    /**
     * * The type of this Gui.
     * * 此 Gui 的类型.
     *
     * @see [GuiType]
     */
    val type : GuiType

    /**
     * * The title of this Gui.
     * * 此 Gui 的标题.
     */
    val title : String

    /**
     * * The size of this Gui.
     * * 此 Gui 的大小.
     *
     * @see [GuiType.size]
     */
    val size : Int

    /**
     * * A list of buttons for this Gui.
     * * 此 Gui 的按钮列表.
     */
    val buttons : List<Button>

    /**
     * * The number size of buttons for this Gui.
     * * 此 Gui 的按钮数量大小.
     */
    val buttonSize : Int

    /**
     * * Get whether this Gui has a parent Gui object.
     * * 获取此 Gui 是否拥有父 Gui 对象.
     */
    fun hasParent() : Boolean

    /**
     * * Indicates whether this Gui allows moving items.
     * * 表示此 Gui 是否允许移动物品.
     */
    var isAllowMove : Boolean

    /**
     * * Indicates the callback when this Gui is opened.
     * * 表示此 Gui 被打开时的回调.
     *
     * @see [InventoryOpenEvent]
     */
    var onOpened : ((gui: Gui, event: InventoryOpenEvent) -> Unit)?

    /**
     * * Indicates the callback when this Gui is closed.
     * * 表示此 Gui 被关闭时的回调.
     *
     * @see [InventoryCloseEvent]
     */
    var onClosed : ((gui: Gui, event: InventoryCloseEvent) -> Unit)?

    /**
     * * Called when the button click is processed. Can handle non-button items.
     * * 当按钮点击处理完后被调用. 可以处理非按钮物品.
     *
     * @see [InventoryClickEvent]
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

    /**
     * * Open this Gui to the given [human].
     * * 将此 Gui 打开给定的人类 [human].
     */
    fun open(human: HumanEntity)

    /**
     * * Get this Gui has a button.
     * * 获取此 Gui 是否拥有按钮.
     */
    fun hasButton(): Boolean

    /**
     * * Get the button for the given [index]. Returns `null` if it does not exist.
     * * 获取给定索引 [index] 的按钮. 如果不存在则返回 `null`.
     */
    fun getButton(index: Int): Button?

    /**
     * * Get the button given the two-dimensional coordinate [x], [y]. Returns `null` if it does not exist.
     * * 获取给定二维坐标 [x], [y] 的按钮. 如果不存在则返回 `null`.
     */
    fun getButton(x: Int, y: Int): Button?

    /**
     * * Get the given [index] is the button.
     * * 获取给定索引 [index] 是否为按钮.
     */
    fun isButton(index: Int): Boolean

    /**
     * * Get the given two-dimensional coordinate [x], [y] whether it is a button.
     * * 获取给定二维坐标 [x], [y] 是否为按钮.
     */
    fun isButton(x: Int, y: Int): Boolean

    /**
     * * Remove the given [button] from this Gui.
     * * 将给定的按钮 [button] 从此 Gui 移除.
     */
    fun removeButton(button: Button): Boolean

    /**
     * * Remove the given [index] button from this Gui.
     * * 将给定的索引 [index] 按钮从此 Gui 移除.
     */
    fun removeButton(index: Int): Boolean

    /**
     * * Remove the given two-dimensional coordinate [x], [y] button from this Gui.
     * * 将给定的二维坐标 [x], [y] 按钮从此 Gui 移除.
     */
    fun removeButton(x: Int, y: Int): Boolean

    /**
     * * Remove all buttons of this Gui.
     * * 将此 Gui 的所有按钮移除.
     */
    fun removeButtons()

    /**
     * * Add a new button to this Gui.
     * * 添加一个新的按钮到此 Gui 内.
     *
     * @throws [IllegalStateException] If can't add more buttons.
     * @throws [IllegalStateException] 如果无法添加更多的按钮.
     */
    @Throws(IllegalStateException::class)
    fun addButton(): Button

    /**
     * * Add a new button to this Gui.
     * * 添加一个新的按钮到此 Gui 内.
     *
     * @throws [IllegalStateException] If can't add more buttons.
     * @throws [IllegalStateException] 如果无法添加更多的按钮.
     */
    @Throws(IllegalStateException::class)
    fun addButton(stack: ItemStack? = null): Button

    /**
     * * Add a new button to this Gui.
     * * 添加一个新的按钮到此 Gui 内.
     *
     * @throws [IllegalStateException] If can't add more buttons.
     * @throws [IllegalStateException] 如果无法添加更多的按钮.
     */
    @Throws(IllegalStateException::class)
    fun addButton(onClicked: Consumer<ButtonEvent>? = null): Button

    /**
     * * Add a new button to this Gui.
     * * 添加一个新的按钮到此 Gui 内.
     *
     * @throws [IllegalStateException] If can't add more buttons.
     * @throws [IllegalStateException] 如果无法添加更多的按钮.
     */
    @Throws(IllegalStateException::class)
    fun addButton(stack: ItemStack? = null, onClicked: Consumer<ButtonEvent>? = null): Button

    /**
     * * Set the given [index] to the button.
     * * 将给定的索引 [index] 设置为按钮.
     *
     * @throws [IllegalArgumentException] If invalid index value.
     * @throws [IllegalArgumentException] 如果无效的索引值.
     * @throws [IllegalArgumentException] If the index is already a button.
     * @throws [IllegalArgumentException] 如果索引已经是按钮.
     */
    @Throws(IllegalArgumentException::class)
    fun setButton(index: Int): Button

    /**
     * * Set the given [index] to the button.
     * * 将给定的索引 [index] 设置为按钮.
     *
     * @throws [IllegalArgumentException] If invalid index value.
     * @throws [IllegalArgumentException] 如果无效的索引值.
     * @throws [IllegalArgumentException] If the index is already a button.
     * @throws [IllegalArgumentException] 如果索引已经是按钮.
     */
    @Throws(IllegalArgumentException::class)
    fun setButton(index: Int, stack: ItemStack? = null): Button

    /**
     * * Set the given [index] to the button.
     * * 将给定的索引 [index] 设置为按钮.
     *
     * @throws [IllegalArgumentException] If invalid index value.
     * @throws [IllegalArgumentException] 如果无效的索引值.
     * @throws [IllegalArgumentException] If the index is already a button.
     * @throws [IllegalArgumentException] 如果索引已经是按钮.
     */
    @Throws(IllegalArgumentException::class)
    fun setButton(index: Int, onClicked: Consumer<ButtonEvent>? = null): Button

    /**
     * * Set the given [index] to the button.
     * * 将给定的索引 [index] 设置为按钮.
     *
     * @throws [IllegalArgumentException] If invalid index value.
     * @throws [IllegalArgumentException] 如果无效的索引值.
     * @throws [IllegalArgumentException] If the index is already a button.
     * @throws [IllegalArgumentException] 如果索引已经是按钮.
     */
    @Throws(IllegalArgumentException::class)
    fun setButton(index: Int, stack: ItemStack? = null, onClicked: Consumer<ButtonEvent>? = null): Button

    /**
     * * Set the given two-dimensional coordinate [x], [y] to button.
     * * 将给定的二维坐标 [x], [y] 设置为按钮.
     *
     * @throws [IllegalArgumentException] If invalid coordinate value.
     * @throws [IllegalArgumentException] 如果无效的坐标值.
     * @throws [IllegalArgumentException] If the coordinate is already a button.
     * @throws [IllegalArgumentException] 如果坐标已经是按钮.
     */
    @Throws(IllegalArgumentException::class)
    fun setButton(x: Int, y: Int): Button

    /**
     * * Set the given two-dimensional coordinate [x], [y] to button.
     * * 将给定的二维坐标 [x], [y] 设置为按钮.
     *
     * @throws [IllegalArgumentException] If invalid coordinate value.
     * @throws [IllegalArgumentException] 如果无效的坐标值.
     * @throws [IllegalArgumentException] If the coordinate is already a button.
     * @throws [IllegalArgumentException] 如果坐标已经是按钮.
     */
    @Throws(IllegalArgumentException::class)
    fun setButton(x: Int, y: Int, stack: ItemStack? = null): Button

    /**
     * * Set the given two-dimensional coordinate [x], [y] to button.
     * * 将给定的二维坐标 [x], [y] 设置为按钮.
     *
     * @throws [IllegalArgumentException] If invalid coordinate value.
     * @throws [IllegalArgumentException] 如果无效的坐标值.
     * @throws [IllegalArgumentException] If the coordinate is already a button.
     * @throws [IllegalArgumentException] 如果坐标已经是按钮.
     */
    @Throws(IllegalArgumentException::class)
    fun setButton(x: Int, y: Int, onClicked: Consumer<ButtonEvent>? = null): Button

    /**
     * * Set the given two-dimensional coordinate [x], [y] to button.
     * * 将给定的二维坐标 [x], [y] 设置为按钮.
     *
     * @throws [IllegalArgumentException] If invalid coordinate value.
     * @throws [IllegalArgumentException] 如果无效的坐标值.
     * @throws [IllegalArgumentException] If the coordinate is already a button.
     * @throws [IllegalArgumentException] 如果坐标已经是按钮.
     */
    @Throws(IllegalArgumentException::class)
    fun setButton(x: Int, y: Int, stack: ItemStack? = null, onClicked: Consumer<ButtonEvent>? = null): Button

    /**
     * * Set the given index array [indexes] to the same type of button.
     * * 将给定的索引数组 [indexes] 设置为相同类型按钮.
     *
     * @throws [IllegalArgumentException] If there is an invalid index value.
     * @throws [IllegalArgumentException] 如果存在无效的索引值.
     * @throws [IllegalArgumentException] If the index has a valid button.
     * @throws [IllegalArgumentException] 如果索引存在有效按钮.
     */
    @Throws(IllegalArgumentException::class)
    fun setSameButton(indexes: IntArray): ButtonSame

    /**
     * * Set the given index array [indexes] to the same type of button.
     * * 将给定的索引数组 [indexes] 设置为相同类型按钮.
     *
     * @throws [IllegalArgumentException] If there is an invalid index value.
     * @throws [IllegalArgumentException] 如果存在无效的索引值.
     * @throws [IllegalArgumentException] If the index has a valid button.
     * @throws [IllegalArgumentException] 如果索引存在有效按钮.
     */
    @Throws(IllegalArgumentException::class)
    fun setSameButton(indexes: IntArray, stack: ItemStack? = null): ButtonSame

    /**
     * * Set the given index array [indexes] to the same type of button.
     * * 将给定的索引数组 [indexes] 设置为相同类型按钮.
     *
     * @throws [IllegalArgumentException] If there is an invalid index value.
     * @throws [IllegalArgumentException] 如果存在无效的索引值.
     * @throws [IllegalArgumentException] If the index has a valid button.
     * @throws [IllegalArgumentException] 如果索引存在有效按钮.
     */
    @Throws(IllegalArgumentException::class)
    fun setSameButton(indexes: IntArray, onClicked: Consumer<ButtonEvent>? = null): ButtonSame

    /**
     * * Set the given index array [indexes] to the same type of button.
     * * 将给定的索引数组 [indexes] 设置为相同类型按钮.
     *
     * @throws [IllegalArgumentException] If there is an invalid index value.
     * @throws [IllegalArgumentException] 如果存在无效的索引值.
     * @throws [IllegalArgumentException] If the index has a valid button.
     * @throws [IllegalArgumentException] 如果索引存在有效按钮.
     */
    @Throws(IllegalArgumentException::class)
    fun setSameButton(indexes: IntArray, stack: ItemStack? = null, onClicked: Consumer<ButtonEvent>? = null): ButtonSame
}
