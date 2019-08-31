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

import com.lgou2w.ldk.bukkit.item.ItemBuilder
import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.common.Builder
import com.lgou2w.ldk.common.Consumer
import com.lgou2w.ldk.common.letIfNotNull
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

/**
 * @since LDK 0.1.7-rc5
 */
interface GuiBuilder : Builder<Gui> {

    fun parent(parent: Gui?): GuiBuilder

    fun opened(block: ((gui: Gui, event: InventoryOpenEvent) -> Unit)?): GuiBuilder
    fun closed(block: ((gui: Gui, event: InventoryCloseEvent) -> Unit)?): GuiBuilder
    fun clicked(block: ((gui: Gui, event: InventoryClickEvent) -> Unit)?): GuiBuilder

    fun allowMove(flag: Boolean): GuiBuilder
    fun property(key: String, value: Any): GuiBuilder

    @Throws(IllegalStateException::class)
    fun button(): ButtonBuilder
    @Throws(IllegalArgumentException::class)
    fun button(index: Int): ButtonBuilder
    @Throws(IllegalArgumentException::class)
    fun button(x: Int, y: Int): ButtonBuilder
    @Throws(IllegalArgumentException::class)
    fun buttonSame(indexes: IntArray): ButtonBuilder
    @Throws(IllegalArgumentException::class)
    fun buttonSame(vararg indexRanges: IntProgression): ButtonBuilder

    companion object {

        @JvmStatic
        fun of(plugin: Plugin, type: GuiType, title: String = type.title): GuiBuilder
                = SimpleGuiBuilder(plugin, type, title)

        /**
         * @since LDK 0.1.7-rc6
         */
        @JvmStatic
        fun of(gui: Gui): GuiBuilder
                = SimpleGuiBuilder(gui)
    }
}

/**
 * @since LDK 0.1.7-rc5
 */
open class SimpleGuiBuilder(protected open val gui: Gui) : GuiBuilder {

    constructor(plugin: Plugin, type: GuiType, title: String = type.title) : this(SimpleGui(plugin, type, title))

    override fun build(): Gui = gui

    override fun parent(parent: Gui?): GuiBuilder {
        gui.parent = parent
        return this
    }

    override fun opened(block: ((gui: Gui, event: InventoryOpenEvent) -> Unit)?): GuiBuilder {
        gui.onOpened = block
        return this
    }

    override fun closed(block: ((gui: Gui, event: InventoryCloseEvent) -> Unit)?): GuiBuilder {
        gui.onClosed = block
        return this
    }

    override fun clicked(block: ((gui: Gui, event: InventoryClickEvent) -> Unit)?): GuiBuilder {
        gui.onClicked = block
        return this
    }

    override fun allowMove(flag: Boolean): GuiBuilder {
        gui.isAllowMove = flag
        return this
    }

    override fun property(key: String, value: Any): GuiBuilder {
        gui.setProperty(key, value)
        return this
    }

    protected open fun buttonBuilder(origin: GuiBuilder, button: Button): ButtonBuilder {
        return SimpleButtonBuilder(origin as SimpleGuiBuilder, button)
    }

    override fun button(): ButtonBuilder = buttonBuilder(this, gui.addButton())
    override fun button(index: Int): ButtonBuilder = buttonBuilder(this, gui.setButton(index))
    override fun button(x: Int, y: Int): ButtonBuilder = buttonBuilder(this, gui.setButton(x, y))
    override fun buttonSame(indexes: IntArray): ButtonBuilder = buttonBuilder(this, gui.setSameButton(indexes))
    override fun buttonSame(vararg indexRanges: IntProgression): ButtonBuilder {
        val indexes = indexRanges.flatMap { it.toList() }
        return buttonSame(indexes.toIntArray())
    }
}

/**
 * @since LDK 0.1.7-rc5
 */
interface ButtonBuilder : Builder<Button> {

    val button : Button

    fun origin(): GuiBuilder

    fun stack(stack: ItemStack?): ButtonBuilder
    fun stack(material: Material): ButtonBuilder
    fun stack(stack: ItemStack?, builder: Applicator<ItemBuilder>): ButtonBuilder
    fun stack(material: Material, builder: Applicator<ItemBuilder>): ButtonBuilder
    fun stackModify(modifier: Applicator<ItemBuilder>): ButtonBuilder

    fun clicked(block: Consumer<ButtonEvent>?): ButtonBuilder
    fun clickedAndCancel(block: Consumer<ButtonEvent>): ButtonBuilder
    fun clickedAndClosed(block: Consumer<ButtonEvent>): ButtonBuilder

    fun clickedGotoPrevious(): ButtonBuilder
    fun clickedGotoNext(): ButtonBuilder
}

/**
 * @since LDK 0.1.7-rc5
 */
open class SimpleButtonBuilder(
        protected open val origin: SimpleGuiBuilder,
        override val button: Button
) : ButtonBuilder {

    override fun build(): Button = button
    override fun origin(): GuiBuilder = origin

    override fun stack(stack: ItemStack?): ButtonBuilder {
        button.stack = stack
        return this
    }

    override fun stack(material: Material): ButtonBuilder {
        button.stack = ItemStack(material)
        return this
    }

    override fun stack(stack: ItemStack?, builder: Applicator<ItemBuilder>): ButtonBuilder {
        button.stack = stack.letIfNotNull { ItemBuilder.of(it).also(builder).build() }
        return this
    }

    override fun stack(material: Material, builder: Applicator<ItemBuilder>): ButtonBuilder {
        button.stack = ItemBuilder.of(material).also(builder).build()
        return this
    }

    override fun stackModify(modifier: Applicator<ItemBuilder>): ButtonBuilder {
        button.stackModify(modifier)
        return this
    }

    override fun clicked(block: Consumer<ButtonEvent>?): ButtonBuilder {
        button.onClicked = block
        return this
    }

    override fun clickedAndCancel(block: Consumer<ButtonEvent>): ButtonBuilder {
        button.onClicked = ButtonEvent.cancelThen(block)
        return this
    }

    override fun clickedAndClosed(block: Consumer<ButtonEvent>): ButtonBuilder {
        button.onClicked = ButtonEvent.cancelThenConsumeAndClose(block)
        return this
    }

    override fun clickedGotoPrevious(): ButtonBuilder {
        button.onClicked = PageableGui.previousPage()
        return this
    }

    override fun clickedGotoNext(): ButtonBuilder {
        button.onClicked = PageableGui.nextPage()
        return this
    }
}
