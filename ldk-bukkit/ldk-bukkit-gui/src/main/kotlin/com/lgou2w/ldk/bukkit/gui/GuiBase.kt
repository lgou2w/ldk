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
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.HumanEntity
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.generator.ChunkGenerator
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginBase
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginLoader
import org.bukkit.plugin.RegisteredListener
import java.io.File
import java.io.InputStream
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Logger

abstract class GuiBase : Gui {

    final override var parent: Gui? = null
    final override val type: GuiType
    final override val title: String
    final override val size: Int
    final override fun hasParent(): Boolean {
        return parent != null
    }

    @JvmOverloads
    @Suppress("ConvertSecondaryConstructorToPrimary", "LeakingThis")
    constructor(type: GuiType, title: String = type.title) {
        this.type = type
        this.title = title
        this.inventory = type.createInventory(this, title)
        this.size = inventory.size
        safeRegisterHandlerListener()
    }

    private val inventory : Inventory
    final override fun getInventory(): Inventory {
        return inventory
    }

    override fun open(human: HumanEntity) {
        human.openInventory(inventory)
    }

    override fun iterator(): Iterator<ItemStack> {
        return inventory.iterator()
    }

    /**************************************************************************
     *
     * Controller
     *
     **************************************************************************/

    override var isAllowMove: Boolean = true

    final override var onOpened: ((gui: Gui, event: InventoryOpenEvent) -> Unit)? = null
    final override var onClosed: ((gui: Gui, event: InventoryCloseEvent) -> Unit)? = null

    /**************************************************************************
     *
     * Button Implement
     *
     **************************************************************************/

    private val buttonList : MutableList<Button> = ArrayList()
    final override val buttons : List<Button> = ArrayList(buttonList)
    
    private fun canAdd(button: Button) {
        val sameMax = (button as? ButtonSame)?.indexes?.max()
        if (button.index < 0 || button.index + 1 > size || (sameMax != null && sameMax + 1 > size))
            throw IllegalArgumentException("Invalid button index: ${sameMax ?: button.index} (should: >= 0 || <= ${size - 1})")
        if (isButton(button.index))
            throw IllegalArgumentException("The current index ${button.index} already has a valid button.")
        var invalid = 0
        if (button is ButtonSame && button.indexes.any { invalid = it; isButton(it) })
            throw IllegalArgumentException("The same button index $invalid already has a valid button.")
    }

    private fun <T : Button> addButton0(button: T) : T {
        canAdd(button)
        synchronized (buttonList) {
            buttonList.add(button)
            return button
        }
    }

    protected open fun getButton0(index: Int) : Button? {
        var button = buttonList.find { it.index == index }
        if (button == null)
            button = buttonList.asSequence().filterIsInstance(ButtonSame::class.java).find { it.isSame(index) }
        return button
    }

    protected open fun nextAvailableIndex() : Int {
        synchronized (buttonList) {
            for (index in 0 until size)
                if (getButton0(index) == null)
                    return index
            throw IllegalStateException("Gui can't add more buttons.")
        }
    }

    override fun getButton(index: Int): Button? {
        synchronized (buttonList) {
            return getButton0(index)
        }
    }

    override fun getButton(x: Int, y: Int): Button? {
        return getButton(GuiFactory.coordinateToIndex(x, y))
    }

    override fun isButton(index: Int): Boolean {
        return getButton(index) != null
    }

    override fun isButton(x: Int, y: Int): Boolean {
        return isButton(GuiFactory.coordinateToIndex(x,  y))
    }

    override fun removeButton(index: Int): Boolean {
        synchronized (buttonList) {
            val button = getButton0(index) ?: return false
            return buttonList.remove(button).apply {
                if (this) {
                    button.stack = null
                    button.onClicked = null
                }
            }
        }
    }

    override fun removeButton(x: Int, y: Int): Boolean {
        return removeButton(GuiFactory.coordinateToIndex(x, y))
    }

    override fun addButton(): Button {
        val index = nextAvailableIndex()
        return setButton(index)
    }

    override fun addButton(stack: ItemStack?): Button {
        return addButton(stack, null)
    }

    override fun addButton(onClicked: Consumer<ButtonEvent>?): Button {
        return addButton(null, onClicked)
    }

    override fun addButton(stack: ItemStack?, onClicked: Consumer<ButtonEvent>?): Button {
        val button = addButton()
        button.stack = stack
        button.onClicked = onClicked
        return button
    }

    override fun setButton(index: Int): Button {
        val button = ButtonBase(this, index)
        return addButton0(button)
    }

    override fun setButton(index: Int, stack: ItemStack?): Button {
        return setButton(index, stack, null)
    }

    override fun setButton(index: Int, onClicked: Consumer<ButtonEvent>?): Button {
        return setButton(index, null, onClicked)
    }

    override fun setButton(index: Int, stack: ItemStack?, onClicked: Consumer<ButtonEvent>?): Button {
        val button = setButton(index)
        button.stack = stack
        button.onClicked = onClicked
        return button
    }

    override fun setButton(x: Int, y: Int): Button {
        return setButton(GuiFactory.coordinateToIndex(x, y), null, null)
    }

    override fun setButton(x: Int, y: Int, stack: ItemStack?): Button {
        return setButton(GuiFactory.coordinateToIndex(x, y), stack, null)
    }

    override fun setButton(x: Int, y: Int, onClicked: Consumer<ButtonEvent>?): Button {
        return setButton(GuiFactory.coordinateToIndex(x, y), null, onClicked)
    }

    override fun setButton(x: Int, y: Int, stack: ItemStack?, onClicked: Consumer<ButtonEvent>?): Button {
        return setButton(GuiFactory.coordinateToIndex(x, y), stack, onClicked)
    }

    override fun setSameButton(indexes: IntArray): ButtonSame {
        val button = ButtonSameBase(this, indexes)
        return addButton0(button)
    }

    override fun setSameButton(indexes: IntArray, stack: ItemStack?): ButtonSame {
        return setSameButton(indexes, stack, null)
    }

    override fun setSameButton(indexes: IntArray, onClicked: Consumer<ButtonEvent>?): ButtonSame {
        return setSameButton(indexes, null, onClicked)
    }

    override fun setSameButton(indexes: IntArray, stack: ItemStack?, onClicked: Consumer<ButtonEvent>?): ButtonSame {
        val button = setSameButton(indexes)
        button.stack = stack
        button.onClicked = onClicked
        return button
    }

    companion object {
        @JvmStatic private val registered = AtomicBoolean(false)
        @JvmStatic private fun safeRegisterHandlerListener() {
            if (registered.getAndSet(true))
                return
            val ldk : Plugin? = Bukkit.getPluginManager().getPlugin("LDK")
            val listener = RegisteredListener(object : Listener {}, EventExecutor { _, event ->
                when (event) {
                    is InventoryOpenEvent -> {
                        val gui = GuiFactory.fromInventory(event.inventory)
                        if (gui?.onOpened != null)
                            gui.onOpened?.invoke(gui, event)
                    }
                    is InventoryCloseEvent -> {
                        val gui = GuiFactory.fromInventory(event.inventory)
                        if (gui?.onClosed != null)
                            gui.onClosed?.invoke(gui, event)
                    }
                    is InventoryClickEvent -> {
                        val gui = GuiFactory.fromInventory(event.view.topInventory)
                        if (gui != null) {
                            if (!gui.isAllowMove)
                                event.isCancelled = true
                            if (event.rawSlot < gui.size) {
                                val button = gui.getButton(event.rawSlot)
                                if (button?.onClicked != null) {
                                    val buttonEvent = ButtonEvent(button, event.whoClicked, event.currentItem, event)
                                    button.onClicked?.invoke(buttonEvent)
                                }
                            }
                        }
                    }
                }
            }, EventPriority.MONITOR, ldk ?: InternalFakePlugin(), false)
            InventoryOpenEvent.getHandlerList().register(listener)
            InventoryCloseEvent.getHandlerList().register(listener)
            InventoryClickEvent.getHandlerList().register(listener)
        }

        /**
         * * This makes it possible to implement an object of a fake plugin, but is not sure about the unknown risk.
         * * Currently in the running environment test everything is normal, and only event processing, and can not query plugin information.
         *
         * * 这样虽然能够实现一个虚假插件的对象，但是不确定未知的风险。
         * * 目前在运行环境测试一切正常，并且只有事件处理，以及无法查询到插件信息。
         *
         * @author lgou2w
         */
        private class InternalFakePlugin : PluginBase() {
            override fun getDataFolder(): File? = null
            override fun getPluginLoader(): PluginLoader? = null
            override fun getServer(): Server = Bukkit.getServer()
            override fun isEnabled(): Boolean = true
            override fun getDescription(): PluginDescriptionFile = PluginDescriptionFile("LDKGuiInternalFakePlugin", "0", "0")
            override fun getConfig(): FileConfiguration? = null
            override fun reloadConfig() { }
            override fun saveConfig() { }
            override fun saveDefaultConfig() { }
            override fun saveResource(resourcePath: String?, replace: Boolean) { }
            override fun getResource(filename: String?): InputStream? = null
            override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean = false
            override fun onTabComplete(sender: CommandSender?, command: Command?, alias: String?, args: Array<out String>?): MutableList<String> = Collections.emptyList()
            override fun isNaggable(): Boolean = false
            override fun setNaggable(canNag: Boolean) { }
            override fun getLogger(): Logger = Bukkit.getLogger()
            override fun onLoad() { }
            override fun onEnable() { }
            override fun onDisable() { }
            override fun getDefaultWorldGenerator(worldName: String?, id: String?): ChunkGenerator? = null
        }
    }
}