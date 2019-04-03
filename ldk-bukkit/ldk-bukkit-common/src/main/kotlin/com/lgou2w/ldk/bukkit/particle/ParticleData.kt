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

package com.lgou2w.ldk.bukkit.particle

import org.bukkit.Material

/**
 * ## ParticleData (粒子数据)
 *
 * @see [Particle.ITEM]
 * @see [Particle.BLOCK]
 * @see [Particle.BLOCKDUST]
 * @see [Particle.FALLING_DUST]
 * @author lgou2w
 */
data class ParticleData(
        /**
         * * The material type of this particle data.
         * * 此粒子数据的材料类型.
         */
        val type: Material,
        /**
         * * The material data of this particle data.
         * * 此粒子数据的材料数据.
         */
        val data: Int
)
