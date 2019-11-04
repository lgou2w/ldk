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
import org.bukkit.inventory.ItemStack

/**
 * ## Button (按钮)
 *
 * @see [Gui]
 * @see [ButtonBase]
 * @see [ButtonSame]
 * @see [ButtonSameBase]
 * @author lgou2w
 */
interface Button {

  /**
   * * The Gui object for this button.
   * * 此按钮的 Gui 对象.
   */
  val parent : Gui

  /**
   * * The index value of this button.
   * * 此按钮的索引值.
   */
  val index : Int

  /**
   * * An item stack object representing this button.
   * * 表示此按钮的物品栈对象.
   */
  var stack : ItemStack?

  /**
   * * Indicates the callback after this button is clicked.
   * * 表示此按钮被点击后的回调.
   */
  var onClicked : Consumer<ButtonEvent>?

  /**
   * * If the [stack] is not `null`, modify the [stack] with the given item builder [modifier].
   * * 如果此物品栈 [stack] 不为 `null`. 则用给定的物品构建者修改器 [modifier] 修改此物品栈.
   */
  fun stackModify(modifier: Applicator<ItemBuilder>)
}
