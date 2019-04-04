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
import com.lgou2w.ldk.common.Consumer
import com.lgou2w.ldk.common.notNull
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

/**
 * @since LDK 0.1.7-rc5
 */
interface PageableGuiBuilder : GuiBuilder {

    override fun build(): PageableGui

    override fun parent(parent: Gui?): PageableGuiBuilder

    override fun opened(block: ((gui: Gui, event: InventoryOpenEvent) -> Unit)?): PageableGuiBuilder
    override fun closed(block: ((gui: Gui, event: InventoryCloseEvent) -> Unit)?): PageableGuiBuilder
    override fun clicked(block: ((gui: Gui, event: InventoryClickEvent) -> Unit)?): PageableGuiBuilder

    override fun allowMove(flag: Boolean): PageableGuiBuilder
    override fun property(key: String, value: Any): PageableGuiBuilder

    override fun button(): PageableButtonBuilder
    override fun button(index: Int): PageableButtonBuilder
    override fun button(x: Int, y: Int): PageableButtonBuilder
    override fun buttonSame(indexes: IntArray): PageableButtonBuilder
    override fun buttonSame(vararg indexRanges: IntProgression): PageableButtonBuilder

    /**************************************************************************
     *
     * Extended
     *
     **************************************************************************/

    fun previousPage(): PageableGuiBuilder?
    @Throws(NullPointerException::class)
    fun previousPageUnsafe(): PageableGuiBuilder

    fun nextPage(type: GuiType, title: String = type.title): PageableGuiBuilder

    companion object {

        @JvmStatic
        fun of(type: GuiType, title: String = type.title): PageableGuiBuilder
                = SimplePageableGuiBuilder(null, PageableGui(type, title))

        /**
         * @since LDK 0.1.7-rc5
         */
        @JvmStatic
        fun of(gui: PageableGui): PageableGuiBuilder
                = SimplePageableGuiBuilder(null, gui)
    }
}

/**
 * @since LDK 0.1.7-rc5
 */
open class SimplePageableGuiBuilder(
        private val previous: PageableGuiBuilder?,
        next: PageableGui
) : SimpleGuiBuilder(next),
        PageableGuiBuilder {

    override val gui: PageableGui
        get() = super.gui as PageableGui

    override fun build(): PageableGui {
        return super.build() as PageableGui
    }

    override fun parent(parent: Gui?): PageableGuiBuilder {
        return super.parent(parent) as PageableGuiBuilder
    }

    override fun opened(block: ((gui: Gui, event: InventoryOpenEvent) -> Unit)?): PageableGuiBuilder {
        return super.opened(block) as PageableGuiBuilder
    }

    override fun closed(block: ((gui: Gui, event: InventoryCloseEvent) -> Unit)?): PageableGuiBuilder {
        return super.closed(block) as PageableGuiBuilder
    }

    override fun clicked(block: ((gui: Gui, event: InventoryClickEvent) -> Unit)?): PageableGuiBuilder {
        return super.clicked(block) as PageableGuiBuilder
    }

    override fun allowMove(flag: Boolean): PageableGuiBuilder {
        return super.allowMove(flag) as PageableGuiBuilder
    }

    override fun property(key: String, value: Any): PageableGuiBuilder {
        return super.property(key, value) as PageableGuiBuilder
    }

    override fun buttonBuilder(origin: GuiBuilder, button: Button): PageableButtonBuilder {
        return SimplePageableButtonBuilder(origin as SimplePageableGuiBuilder, button)
    }

    override fun button(): PageableButtonBuilder {
        return super.button() as PageableButtonBuilder
    }

    override fun button(index: Int): PageableButtonBuilder {
        return super.button(index) as PageableButtonBuilder
    }

    override fun button(x: Int, y: Int): PageableButtonBuilder {
        return super.button(x, y) as PageableButtonBuilder
    }

    override fun buttonSame(indexes: IntArray): PageableButtonBuilder {
        return super.buttonSame(indexes) as PageableButtonBuilder
    }

    override fun buttonSame(vararg indexRanges: IntProgression): PageableButtonBuilder {
        return super.buttonSame(*indexRanges) as PageableButtonBuilder
    }

    override fun previousPage(): PageableGuiBuilder? {
        return previous
    }

    override fun previousPageUnsafe(): PageableGuiBuilder {
        return previous.notNull("This pageable gui has no parent.")
    }

    override fun nextPage(type: GuiType, title: String): PageableGuiBuilder {
        val next = gui.setPage(type, title)
        return SimplePageableGuiBuilder(this, next)
    }
}

interface PageableButtonBuilder : ButtonBuilder {

    override fun origin(): PageableGuiBuilder

    override fun stack(stack: ItemStack?): PageableButtonBuilder
    override fun stack(material: Material): PageableButtonBuilder
    override fun stack(stack: ItemStack?, builder: Applicator<ItemBuilder>): PageableButtonBuilder
    override fun stack(material: Material, builder: Applicator<ItemBuilder>): PageableButtonBuilder
    override fun stackModify(modifier: Applicator<ItemBuilder>): PageableButtonBuilder

    override fun clicked(block: Consumer<ButtonEvent>?): PageableButtonBuilder
    override fun clickedAndCancel(block: Consumer<ButtonEvent>): PageableButtonBuilder
    override fun clickedAndClosed(block: Consumer<ButtonEvent>): PageableButtonBuilder

    override fun clickedGotoPrevious(): PageableButtonBuilder
    override fun clickedGotoNext(): PageableButtonBuilder
}

/**
 * @since LDK 0.1.7-rc5
 */
open class SimplePageableButtonBuilder(
        origin: SimplePageableGuiBuilder,
        button: Button
) : SimpleButtonBuilder(origin, button),
        PageableButtonBuilder {

    override fun origin(): PageableGuiBuilder {
        return super.origin() as PageableGuiBuilder
    }

    override fun stack(stack: ItemStack?): PageableButtonBuilder {
        return super.stack(stack) as PageableButtonBuilder
    }

    override fun stack(material: Material): PageableButtonBuilder {
        return super.stack(material) as PageableButtonBuilder
    }

    override fun stack(material: Material, builder: Applicator<ItemBuilder>): PageableButtonBuilder {
        return super.stack(material, builder) as PageableButtonBuilder
    }

    override fun stack(stack: ItemStack?, builder: Applicator<ItemBuilder>): PageableButtonBuilder {
        return super.stack(stack, builder) as PageableButtonBuilder
    }

    override fun stackModify(modifier: Applicator<ItemBuilder>): PageableButtonBuilder {
        return super.stackModify(modifier) as PageableButtonBuilder
    }

    override fun clicked(block: Consumer<ButtonEvent>?): PageableButtonBuilder {
        return super.clicked(block) as PageableButtonBuilder
    }

    override fun clickedAndCancel(block: Consumer<ButtonEvent>): PageableButtonBuilder {
        return super.clickedAndCancel(block) as PageableButtonBuilder
    }

    override fun clickedAndClosed(block: Consumer<ButtonEvent>): PageableButtonBuilder {
        return super.clickedAndClosed(block) as PageableButtonBuilder
    }

    override fun clickedGotoNext(): PageableButtonBuilder {
        return super.clickedGotoNext() as PageableButtonBuilder
    }

    override fun clickedGotoPrevious(): PageableButtonBuilder {
        return super.clickedGotoPrevious() as PageableButtonBuilder
    }
}
