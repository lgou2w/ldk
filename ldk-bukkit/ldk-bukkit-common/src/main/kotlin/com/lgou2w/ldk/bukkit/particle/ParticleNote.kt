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

package com.lgou2w.ldk.bukkit.particle

/**
 * ## ParticleNote (粒子音符)
 *
 * * Used only in versions prior to 1.13, set the color of the note particle. The value is `0 ~ 24` range.
 * * 仅在 1.13 之前版本使用, 设置音符粒子的颜色. 值为 `0 ~ 24` 范围.
 *
 * @author lgou2w
 */
data class ParticleNote(val value: Int) {
}
