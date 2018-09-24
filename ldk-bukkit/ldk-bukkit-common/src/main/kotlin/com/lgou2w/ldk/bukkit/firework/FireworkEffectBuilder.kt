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

package com.lgou2w.ldk.bukkit.firework

import com.lgou2w.ldk.common.Builder
import org.bukkit.Color
import java.util.*

class FireworkEffectBuilder(
        private var type: FireworkType
) : Builder<FireworkEffect> {

    private var canFlicker: Boolean = false
    private var hasTrail: Boolean = false
    private var colors: MutableList<Color> = ArrayList()
    private var fades: MutableList<Color> = ArrayList()

    fun with(type: FireworkType) : FireworkEffectBuilder {
        this.type = type
        return this
    }

    fun withFlicker() : FireworkEffectBuilder {
        this.canFlicker = true
        return this
    }

    fun withTrail() : FireworkEffectBuilder {
        this.hasTrail = true
        return this
    }

    fun withColors(vararg colors: Color) : FireworkEffectBuilder {
        this.colors.addAll(colors)
        return this
    }

    fun withFades(vararg colors: Color) : FireworkEffectBuilder {
        this.fades.addAll(colors)
        return this
    }

    override fun build(): FireworkEffect {
        return FireworkEffect(
                type,
                canFlicker,
                hasTrail,
                Collections.unmodifiableList(colors),
                Collections.unmodifiableList(fades))
    }
}
