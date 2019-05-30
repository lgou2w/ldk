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

import org.bukkit.plugin.Plugin

/**
 * ## SimpleGui (简单界面)
 *
 * @see [Gui]
 * @see [GuiBase]
 * @author lgou2w
 */
class SimpleGui : GuiBase {

    /**
     * * Since LDK 0.1.8-rc2, Gui added the plugin field, this constructor is deprecated and throw an exception.
     *      * This constructor will be completely removed in LDK 0.1.9 version.
     * * 自从 LDK 0.1.8-rc2, Gui 增加了 `plugin` 字段, 此构造弃用且抛出异常.
     *      * 此构造将在 LDK 0.1.9 版本完全移除.
     */
    @Deprecated("Unsupported")
    @Throws(UnsupportedOperationException::class)
    constructor(type: GuiType, title: String = type.title) : super(type, title)

    /**
     * @since LDK 0.1.8-rc2
     */
    constructor(plugin: Plugin, type: GuiType, title: String = type.title) : super(plugin, type, title)
}
