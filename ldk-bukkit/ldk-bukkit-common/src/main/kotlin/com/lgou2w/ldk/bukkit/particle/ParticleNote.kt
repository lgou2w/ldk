/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.bukkit.particle

/**
 * ## ParticleNote (粒子音符)
 *
 * * Sets the color of the note particle. The value is `0 ~ 24` range.
 *      * Need to set the number of `count` to `0` color to take effect.
 * * 设置音符粒子的颜色. 值为 `0 ~ 24` 范围.
 *      * 需要将 `count` 数量设置为 `0` 颜色才会生效.
 *
 * @author lgou2w
 */
data class ParticleNote(val value: Int)
