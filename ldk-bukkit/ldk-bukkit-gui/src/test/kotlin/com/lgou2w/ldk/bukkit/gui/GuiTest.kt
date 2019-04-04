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
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class GuiTest {

    @Mock lateinit var gui : Gui
    lateinit var buttons : MutableList<Button>

    @Before
    fun init() {
        buttons = ArrayList()
        PowerMockito.`when`(gui.buttons).thenReturn(buttons)
        PowerMockito.`when`(gui.setButton(Mockito.anyInt())).thenAnswer {
            val index = it.arguments.first() as Int
            gui.setButton(index, onClicked = null)
        }
        PowerMockito.`when`(gui.setButton(Mockito.anyInt(), Mockito.any<Consumer<ButtonEvent>>())).thenAnswer {
            val index = it.arguments.first() as Int
            val onClicked = it.arguments.getOrNull(1) as? Consumer<ButtonEvent>
            val button = mockButton(gui, index, onClicked)
            buttons.add(button)
            button
        }
    }

    @Test
    fun testSetButtons() {
        gui.setButton(1)
        gui.setButton(3)
        gui.setButton(5)
        Assert.assertEquals(3, buttons.size)
        Assert.assertEquals(3, buttons[1].index)
        Assert.assertEquals(gui, buttons[1].parent)
    }

    @Test
    fun testButtonClick() {
        gui.setButton(1) { println("Click button 1") }
        gui.setButton(2) { println("Click button 2") }
        gui.mockGuiClickRequest(1)
        gui.mockGuiClickRequest(2)
        Assert.assertEquals(2, buttons.size)
    }

    private fun mockButton(parent: Gui?, index: Int, onClicked: Consumer<ButtonEvent>? = null): Button {
        val button = PowerMockito.mock(Button::class.java)
        PowerMockito.`when`(button.parent).thenReturn(parent)
        PowerMockito.`when`(button.index).thenReturn(index)
        PowerMockito.`when`(button.onClicked).thenReturn(onClicked)
        return button
    }

    private fun Gui.mockGuiClickRequest(index: Int) {
        val button = buttons.find { it.index == index } ?: return
        val clickerMock = PowerMockito.mock(HumanEntity::class.java)
        val sourceMock = PowerMockito.mock(InventoryClickEvent::class.java)
        val buttonEvent = ButtonEvent(button, clickerMock, null, sourceMock)
        button.onClicked?.invoke(buttonEvent)
    }
}
