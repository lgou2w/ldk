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

import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.common.Consumer
import com.lgou2w.ldk.common.Predicate

open class PageableGui(
        type: GuiType,
        title: String = type.title
) : GuiBase(type, title) {

    var next : PageableGui? = null

    @JvmOverloads
    fun addPage(type: GuiType, title: String = type.title, initializer: Applicator<PageableGui> = {}) : PageableGui {
        val next = PageableGui(type, title)
        next.parent = this
        this.next = next
        return next.also(initializer)
    }

    @JvmOverloads
    fun removePage(completed: Applicator<PageableGui>? = null)
            = removePageIf({ true }, completed)

    @JvmOverloads
    fun removePageIf(predicate: Predicate<PageableGui>? = null, completed: Applicator<PageableGui>? = null) {
        val next = this.next
        if (next != null && predicate != null && !predicate(next))
            return
        if (next != null && completed != null)
            completed(next)
        next?.parent = null
        this.next = null
    }

    companion object {

        @JvmStatic
        fun previousPage() : Consumer<ButtonEvent> {
            return ButtonEvent.cancelThen { event ->
                val parent = event.button.parent
                parent.parent?.open(event.clicker)
            }
        }

        @JvmStatic
        fun nextPage() : Consumer<ButtonEvent> {
            return ButtonEvent.cancelThen { event ->
                val parent = event.button.parent as? PageableGui
                parent?.next?.open(event.clicker)
            }
        }
    }
}
