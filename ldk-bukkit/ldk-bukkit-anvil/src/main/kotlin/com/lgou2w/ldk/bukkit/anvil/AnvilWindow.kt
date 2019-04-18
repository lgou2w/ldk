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

package com.lgou2w.ldk.bukkit.anvil

import com.lgou2w.ldk.common.Consumer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

/**
 * ## AnvilWindow (铁砧窗口)
 *
 * @see [AnvilWindow.of]
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
interface AnvilWindow {

    /**
     * * The plugin object for this anvil window.
     * * 此铁砧窗口的插件对象.
     */
    val plugin : Plugin

    /**
     * * Indicates whether this anvil window allows moving item.
     * * 表示此铁砧窗口是否允许移动物品.
     */
    var isAllowMove : Boolean

    /**
     * * Indicates whether this anvil window has been opened.
     * * 表示此铁砧窗口是否已经被打开.
     */
    val isOpened : Boolean

    /**
     * * Open this anvil window to the given [player].
     * * 将此铁砧窗口打开到给定玩家 [player].
     *
     * @throws [IllegalStateException] If the anvil window has already been opened.
     * @throws [IllegalStateException] 如果铁砧窗口已经被打开.
     * @see [isOpened]
     */
    @Throws(IllegalStateException::class)
    fun open(player: Player)

    /**
     * * Called when this anvil window is opened. Set to `null` to remove the listener.
     * * 当此铁砧窗口被打开时调用. 设置为 `null` 则移除监听器.
     *
     * @see [AnvilWindowOpenEvent]
     */
    fun onOpened(block: Consumer<AnvilWindowOpenEvent>?)

    /**
     * * Called when this anvil window is closed. Set to `null` to remove the listener.
     * * 当此铁砧窗口被关闭时调用. 设置为 `null` 则移除监听器.
     *
     * @see [AnvilWindowCloseEvent]
     */
    fun onClosed(block: Consumer<AnvilWindowCloseEvent>?)

    /**
     * * Called when this anvil window is clicked. Set to `null` to remove the listener.
     * * 当此铁砧窗口被点击时调用. 设置为 `null` 则移除监听器.
     *
     * @see [AnvilWindowClickEvent]
     */
    fun onClicked(block: Consumer<AnvilWindowClickEvent>?)

    /**
     * * Called when this anvil window is inputted text. Set to `null` to remove the listener.
     * * 当此铁砧窗口被输入文本时调用. 设置为 `null` 则移除监听器.
     *
     * @see [AnvilWindowInputEvent]
     */
    fun onInputted(block: Consumer<AnvilWindowInputEvent>?)

    /**
     * * Get the item stack for this anvil window given [slot].
     * * 获取此铁砧窗口给定槽位 [slot] 的物品栈.
     *
     * @throws [IllegalStateException] If the anvil window is not yet opened.
     * @throws [IllegalStateException] 如果铁砧窗口尚未打开.
     * @see [isOpened]
     * @see [AnvilWindowSlot]
     */
    @Throws(IllegalStateException::class)
    fun getItem(slot: AnvilWindowSlot): ItemStack?

    /**
     * * Set this anvil window to give the item [stack] for the [slot].
     * * 设置此铁砧窗口给定槽位 [slot] 的物品栈 [stack].
     *
     * @throws [IllegalStateException] If the anvil window is not yet opened.
     * @throws [IllegalStateException] 如果铁砧窗口尚未打开.
     * @see [isOpened]
     * @see [AnvilWindowSlot]
     */
    @Throws(IllegalStateException::class)
    fun setItem(slot: AnvilWindowSlot, stack: ItemStack?)

    /**
     * * Clear the items in this anvil window.
     * * 清除此铁砧窗口的物品.
     */
    fun clearItems()

    companion object Factory {

        /**
         * * Create an anvil window instance object with the given [plugin].
         * * 以给定的插件 [plugin] 创建铁砧窗口实例对象.
         */
        @JvmStatic
        fun of(plugin: Plugin): AnvilWindow
                = @Suppress("DEPRECATION") AnvilWindowBase.of(plugin)
    }
}
