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

import com.lgou2w.ldk.common.Valuable

/**
 * ## AnvilWindowSlot (铁砧窗口槽位)
 *
 * @see [Valuable]
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
enum class AnvilWindowSlot(override val value: Int) : Valuable<Int> {

    /**
     * * Indicates the left input slot of the anvil window.
     * * 表示铁砧窗口的左输入槽位.
     */
    INPUT_LEFT(0),
    /**
     * * Indicates the right input slot of the anvil window.
     * * 表示铁砧窗口的右输入槽位.
     */
    INPUT_RIGHT(1),
    /**
     * * Indicates the output slot of the anvil window.
     * * 表示铁砧窗口的输出槽位.
     */
    OUTPUT(2),
    ;
}
