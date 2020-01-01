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

import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.configuration.serialization.ConfigurationSerialization

/**
 * ## RegionVectorBlock (区域方块向量)
 *
 * @see [RegionVector]
 * @author lgou2w
 */
open class RegionVectorBlock : RegionVector {

  companion object {

    init {
      ConfigurationSerialization.registerClass(RegionVectorBlock::class.java)
    }

    @JvmField val ZERO = RegionVectorBlock(.0, .0, .0)

    @JvmStatic
    fun deserialize(args: Map<String, Any>): RegionVectorBlock {
      val x = args["x"]?.toString()?.toDouble() ?: .0
      val y = args["y"]?.toString()?.toDouble() ?: .0
      val z = args["z"]?.toString()?.toDouble() ?: .0
      return RegionVectorBlock(x, y, z)
    }
  }

  constructor(x: Double, y: Double, z: Double) : super(x, y, z)
  constructor(x: Float, y: Float, z: Float) : super(x, y, z)
  constructor(x: Int, y: Int, z: Int) : super(x, y, z)
  constructor(other: RegionVector) : super(other)
  constructor() : super()

  override fun clone(): RegionVectorBlock {
    return RegionVectorBlock(x, y, z)
  }

  fun getBlock(world: World): Block
    = world.getBlockAt(blockX, blockX, blockZ)

  override fun toString(): String {
    return "RegionVectorBlock(x=$blockX, y=$blockY, z=$blockZ)"
  }
}
