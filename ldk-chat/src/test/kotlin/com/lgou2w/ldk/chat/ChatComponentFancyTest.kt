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

package com.lgou2w.ldk.chat

import org.amshove.kluent.shouldEqual
import org.junit.Test

class ChatComponentFancyTest {

    @Test fun `ChatComponentFancy - normal`() {
        val fancy = ChatComponentFancy("Hello") // 1
            .color(ChatColor.GOLD)
            .then(" ") // 2
            .then("World") // 3
            .color(ChatColor.GREEN)
            .then("!") // 4
            .then(" <Click me>") // 2
            .color(ChatColor.AQUA)
            .withHoverEvent(ChatHoverEvent.Action.SHOW_TEXT, ChatComponentText("You found me."))
            .withClickEvent(ChatClickEvent.Action.RUN_COMMAND, "/say You found me.")
        val component = fancy.build()
        fancy.size shouldEqual 5
        fancy.clear()
        fancy.size shouldEqual 0
        component.toRaw() shouldEqual "§6Hello §aWorld!§b <Click me>"
    }

    @Test fun `ChatComponentFancy - normal - 2`() {
        val component = ChatComponentFancy("Hello")
            .color(ChatColor.RED)
            .withBold()
            .withItalic()
            .withUnderlined()
            .withStrikethrough()
            .withObfuscated()
            .withInsertion("Insertion")
            .link("https://github.com/lgou2w")
            .tooltipTexts("Go to lgou2w's github page")
            .build()
            .extras
            .first()
        component.style.color shouldEqual ChatColor.RED
        component.style.bold shouldEqual true
        component.style.italic shouldEqual true
        component.style.underlined shouldEqual true
        component.style.strikethrough shouldEqual true
        component.style.obfuscated shouldEqual true
        component.style.insertion shouldEqual "Insertion"
        component.style.clickEvent shouldEqual
                ChatClickEvent(ChatClickEvent.Action.OPEN_URL, "https://github.com/lgou2w")
        component.style.hoverEvent shouldEqual
                ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT, ChatComponentText("").append("Go to lgou2w's github page"))
        component.toRaw(true) shouldEqual "§c§l§o§m§n§kHello"
        component.toRaw(false) shouldEqual "Hello"
    }

    @Test fun `ChatComponentFancy - normal - 3`() {
        val fancy = ChatComponentFancy("n3")
            .file("/dir")
            .suggest("/say hi")
            .command("/say hi")
            .changePage(2)
            .tooltipTexts(listOf("a", "b", "c"))
            .thenNewLine()
        val fancy2 = ChatComponentFancy("n3")
            .tooltipTexts("a", "b", "c")
            .tooltipItem("{}")
            .join(fancy)
        fancy2.clear()
        fancy.clear()
    }
}
