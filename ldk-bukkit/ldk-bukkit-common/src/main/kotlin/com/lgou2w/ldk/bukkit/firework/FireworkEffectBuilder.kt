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

package com.lgou2w.ldk.bukkit.firework

import com.lgou2w.ldk.bukkit.randomColor
import com.lgou2w.ldk.common.Builder
import org.bukkit.Color
import java.util.ArrayList
import java.util.Collections
import kotlin.random.Random

/**
 * ## FireworkEffectBuilder (烟花效果构建者)
 *
 * @see [FireworkEffect]
 * @see [Builder]
 * @author lgou2w
 */
class FireworkEffectBuilder(
  private var type: FireworkType
) : Builder<FireworkEffect> {

  private var canFlicker : Boolean = false
  private var hasTrail : Boolean = false
  private val colors : MutableList<Color> = ArrayList()
  private val fades : MutableList<Color> = ArrayList()

  /**
   * * With a given [type] of firework.
   * * 具有给定的烟花类型 [type].
   */
  fun with(type: FireworkType): FireworkEffectBuilder {
    this.type = type
    return this
  }

  /**
   * * With a flicker effect.
   * * 具有闪烁效果.
   */
  fun withFlicker(): FireworkEffectBuilder {
    this.canFlicker = true
    return this
  }

  /**
   * * With a random flicker effect.
   * * 具有随机闪烁效果.
   *
   * @since LDK 0.1.8
   */
  fun withFlickerRandom(): FireworkEffectBuilder {
    this.canFlicker = Random.nextBoolean()
    return this
  }

  /**
   * * With a trail effect.
   * * 具有尾迹效果.
   */
  fun withTrail(): FireworkEffectBuilder {
    this.hasTrail = true
    return this
  }

  /**
   * * With a random trail effect.
   * * 具有随机尾迹效果.
   *
   * @since LDK 0.1.8
   */
  fun withTrailRandom(): FireworkEffectBuilder {
    this.hasTrail = Random.nextBoolean()
    return this
  }

  /**
   * * With the given [colors].
   * * 具有给定的颜色 [colors].
   */
  fun withColors(vararg colors: Color): FireworkEffectBuilder {
    this.colors.addAll(colors)
    return this
  }

  /**
   * * With a random color.
   * * 具有随机的颜色.
   *
   * @since LDK 0.1.8
   */
  fun withColorRandom(): FireworkEffectBuilder {
    this.colors.add(randomColor())
    return this
  }

  /**
   * * With the given fade [colors].
   * * 具有给定的淡化颜色 [colors].
   */
  fun withFades(vararg colors: Color): FireworkEffectBuilder {
    this.fades.addAll(colors)
    return this
  }

  /**
   * * With a random fade color.
   * * 具有随机的淡化颜色.
   *
   * @since LDK 0.1.8
   */
  fun withFadeRandom(): FireworkEffectBuilder {
    this.fades.add(randomColor())
    return this
  }

  override fun build(): FireworkEffect {
    return FireworkEffect(
      type, canFlicker, hasTrail,
      Collections.unmodifiableList(colors),
      Collections.unmodifiableList(fades)
    )
  }
}
