/*
 * Copyright (C) 2016-2020 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.bukkit.region

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity

/**
 * ## RegionBase (区域基础)
 *
 * @see [Region]
 * @author lgou2w
 */
abstract class RegionBase(
  final override val world: World
) : Region {

  override val center : RegionVector
    get() = (minimumPoint + maximumPoint) / 2

  override val area : Int
    get() {
      val min = minimumPoint
      val max = maximumPoint
      return ((max.x - min.x + 1.0) * (max.y - min.y + 1.0) * (max.z - min.z + 1.0)).toInt()
    }

  override val width : Int
    get() = (maximumPoint.x - minimumPoint.x + 1.0).toInt()

  override val height : Int
    get() = (maximumPoint.y - minimumPoint.y + 1.0).toInt()

  override val length : Int
    get() = (maximumPoint.z - minimumPoint.z + 1.0).toInt()

  override fun contains(location: Location): Boolean
    = if (location.world != world) false
  else contains(location.x, location.y, location.z)

  override fun contains(entity: Entity): Boolean
    = contains(entity.location)

  override fun contains(block: Block): Boolean
    = contains(block.location)

  override fun contains(vector: RegionVector): Boolean
    = contains(vector.x, vector.y, vector.z)

  override fun iterator(): Iterator<RegionVectorBlock>
    = RegionIterator(this)
}
