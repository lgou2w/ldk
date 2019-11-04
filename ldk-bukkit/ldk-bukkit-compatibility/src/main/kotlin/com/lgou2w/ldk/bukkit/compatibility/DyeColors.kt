/*
 * Copyright (C) 2018 25 (https://github.com/25)
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

/*
 * Copyright (C) 2019 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.bukkit.compatibility

import com.lgou2w.ldk.common.notNull
import org.bukkit.DyeColor

/**
 * ## DyeColors (染料颜色)
 *
 * @see [DyeColor]
 * @author 25, lgou2w
 * @since LDK 0.1.8-rc
 */
enum class DyeColors(private val alias: String? = null) {

  BLACK,
  BLUE,
  BROWN,
  CYAN,
  GRAY,
  GREEN,
  LIGHT_BLUE,
  LIGHT_GRAY("SILVER"), // Before 1.12: Silver, After 1.13: Light gray
  LIME,
  MAGENTA,
  ORANGE,
  PINK,
  PURPLE,
  RED,
  WHITE,
  YELLOW,
  ;

  private var valid : DyeColor? = null

  /**
   * * Convert this dye color compatible enumeration to the dye color of Bukkit.
   * * 将此染料颜色兼容枚举转换为 Bukkit 的染料颜色.
   *
   * @see [DyeColor]
   */
  fun toBukkit(): DyeColor {
    return if (valid == null) {
      valid = try {
        DyeColor.valueOf(alias ?: name)
      } catch (e: IllegalArgumentException) {
        DyeColor.valueOf(name)
      }
      valid.notNull()
    } else
      valid.notNull()
  }
}
