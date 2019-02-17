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

package com.lgou2w.ldk.bukkit.region

import com.lgou2w.ldk.common.Applicator
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldBorder
import org.bukkit.block.Block
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Entity

interface Region : ConfigurationSerializable, Iterable<RegionVectorBlock> {

    val world : World

    val minimumPoint : RegionVector

    val maximumPoint : RegionVector

    val center : RegionVector

    val area : Int

    val width : Int

    val height : Int

    val length : Int

    fun contains(x: Double, y: Double, z: Double) : Boolean

    fun contains(vector: RegionVector) : Boolean

    fun contains(location: Location) : Boolean

    fun contains(entity: Entity) : Boolean

    fun contains(block: Block) : Boolean
}

fun Location.toRegionVector() : RegionVector
        = RegionVector(x, y, z)

fun Location.toRegionVectorBlock() : RegionVectorBlock
        = RegionVectorBlock(x, y, z)

fun Location.toRegionVector2D() : RegionVector2D
        = RegionVector2D(x, z)

fun World.createRegionCuboid(pos1: RegionVector, pos2: RegionVector) : RegionCuboid
        = RegionCuboid(this, pos1, pos2)

fun World.createRegionCylinder(center2D: RegionVector2D, radius: RegionVector2D) : RegionCylinder
        = RegionCylinder(this, center2D, radius)

fun World.createRegionCylinder(center2D: RegionVector2D, radius: RegionVector2D, minY: Int, maxY: Int) : RegionCylinder
        = RegionCylinder(this, center2D, radius, minY, maxY)

fun World.createRegionEllipsoid(center: RegionVector, radius: RegionVector) : RegionEllipsoid
        = RegionEllipsoid(this, center, radius)

@JvmOverloads
fun Region.createWorldBorder(block: Applicator<WorldBorder> = {}) : WorldBorder {
    val border = world.worldBorder
    border.center = center.toLocation(world)
    border.setSize(length.toDouble(), 0L)
    return border.apply(block)
}
