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
import org.bukkit.plugin.*
import java.io.File
import java.io.InputStream
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Logger

abstract class GuiBase : Gui {

    override var parent: Gui? = null
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
        if (button.index < 0 || button.index + 1 > size)
            throw IllegalArgumentException("Invalid button index: ${button.index} (should: >= 0 || <= ${size - 1})")
        if (isButton(button.index))
            throw IllegalArgumentException("The current index ${button.index} already has a valid button.")
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
            button = buttonList.filterIsInstance(ButtonSame::class.java).find { it.isSame(index) }
        return button
    }

    protected open fun nextAvailableIndex() : Int {
        synchronized (buttonList) {
            for (index in 0 until size)
                if (buttonList.find { it.index == index } == null)
                    return index
            throw IllegalStateException("Gui can't add more buttons.")
        }
    }

    override fun getButton(index: Int): Button? {
        synchronized (buttonList) {
            return getButton0(index)
        }
    }

    override fun isButton(index: Int): Boolean {
        return getButton(index) != null
    }

    override fun addButton(): Button {
        val index = nextAvailableIndex()
        return setButton(index)
    }

    override fun setButton(index: Int): Button {
        val button = ButtonBase(this, index)
        return addButton0(button)
    }

    override fun setSameButton(indexes: IntArray): ButtonSame {
        val button = ButtonSameBase(this, indexes)
        return addButton0(button)
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
                        if (gui != null)
                            gui.onOpened?.invoke(gui, event)
                    }
                    is InventoryCloseEvent -> {
                        val gui = GuiFactory.fromInventory(event.inventory)
                        if (gui != null)
                            gui.onClosed?.invoke(gui, event)
                    }
                    is InventoryClickEvent -> {
                        val gui = GuiFactory.fromInventory(event.inventory)
                        val button = gui?.getButton(event.slot)
                        if (button != null) {
                            val buttonEvent = ButtonEvent(button, event.whoClicked, event.currentItem, event)
                            button.onClicked?.invoke(buttonEvent)
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
