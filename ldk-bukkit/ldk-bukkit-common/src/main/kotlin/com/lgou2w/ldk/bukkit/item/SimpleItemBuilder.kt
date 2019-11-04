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

package com.lgou2w.ldk.bukkit.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * ## SimpleItemBuilder (简单物品构建者)
 *
 * @see [ItemBuilder]
 * @see [ItemBuilderBase]
 * @author lgou2w
 */
class SimpleItemBuilder : ItemBuilderBase {

  constructor(itemStack: ItemStack) : super(itemStack)

  @JvmOverloads
  constructor(material: Material, count: Int = 1, durability: Int = 0) : super(material, count, durability)
}
