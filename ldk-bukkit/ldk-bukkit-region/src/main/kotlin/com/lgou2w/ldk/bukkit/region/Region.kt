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

/**
 * ## Region (区域)
 *
 * @author lgou2w
 */
interface Region : ConfigurationSerializable, Iterable<RegionVectorBlock> {

    /**
     * * Get the world where this region is located.
     * * 获取此区域所在的世界.
     */
    val world : World

    /**
     * * Get the minimum point of this region.
     * * 获取此区域的最低点.
     */
    val minimumPoint : RegionVector

    /**
     * * Get the maximum point of this region.
     * * 获取此区域的最高点.
     */
    val maximumPoint : RegionVector

    /**
     * * Get the center point of this region.
     * * 获取此区域的中心点.
     */
    val center : RegionVector

    /**
     * * Get the volume of this area.
     * * 获取此区域的体积.
     */
    val area : Int

    /**
     * * Get the width of this region.
     * * 获取此区域的宽度.
     */
    val width : Int

    /**
     * * Get the height of this region.
     * * 获取此区域的高度.
     */
    val height : Int

    /**
     * * Get the length of this region.
     * * 获取此区域的长度.
     */
    val length : Int

    /**
     * * Get from the given [x], [y], [z] coordinate whether this is in this region.
     * * 从给定的 [x], [y], [z] 坐标获取是否在此区域内.
     */
    fun contains(x: Double, y: Double, z: Double): Boolean

    /**
     * * Get from the given region [vector] whether it is in this region.
     * * 从给定的区域向量 [vector] 获取是否在此区域内.
     */
    fun contains(vector: RegionVector): Boolean

    /**
     * * Get from the given [location] whether it is in this region.
     * * 从给定的位置 [location] 获取是否在此区域内.
     */
    fun contains(location: Location): Boolean

    /**
     * * Get from the given [entity] whether it is in this region.
     * * 从给定的实体 [entity] 获取是否在此区域内.
     */
    fun contains(entity: Entity): Boolean

    /**
     * * Get from the given [block] whether it is in this region.
     * * 从给定的方块 [block] 获取是否在此区域内.
     */
    fun contains(block: Block): Boolean
}

/**
 * * Convert the given location to a region vector.
 * * 将给定的位置转换为区域向量.
 */
fun Location.toRegionVector(): RegionVector
        = RegionVector(x, y, z)

/**
 * * Convert the given location to a region block vector.
 * * 将给定的位置转换为区域方块向量.
 */
fun Location.toRegionVectorBlock(): RegionVectorBlock
        = RegionVectorBlock(x, y, z)

/**
 * * Convert the given location to a region vector 2d.
 * * 将给定的位置转换为区域向量 2D.
 */
fun Location.toRegionVector2D(): RegionVector2D
        = RegionVector2D(x, z)

/**
 * * Create a cuboid region from a given world with the given vector [pos1], [pos2].
 * * 从给定的世界以给定的向量 [pos1], [pos2] 创建一个长方体区域.
 */
fun World.createRegionCuboid(pos1: RegionVector, pos2: RegionVector): RegionCuboid
        = RegionCuboid(this, pos1, pos2)

/**
 * * Create a cylinder region from a given world with a given center point [center2D] and [radius] vector.
 * * 从给定的世界以给定的中心点 [center2D] 和半径 [radius] 向量创建一个圆柱区域.
 */
fun World.createRegionCylinder(center2D: RegionVector2D, radius: RegionVector2D): RegionCylinder
        = RegionCylinder(this, center2D, radius)

/**
 * * Create a cylinder region from a given world with a given center point [center2D] and [radius] vector and height [minY], [maxY].
 * * 从给定的世界以给定的中心点 [center2D] 和半径 [radius] 向量和高度 [minY], [maxY] 创建一个圆柱区域.
 */
fun World.createRegionCylinder(center2D: RegionVector2D, radius: RegionVector2D, minY: Int, maxY: Int): RegionCylinder
        = RegionCylinder(this, center2D, radius, minY, maxY)

/**
 * * Create a ellipsoid region from a given world with a given center point [center] and [radius] vector.
 * * 从给定的世界以给定的中心点 [center] 和半径 [radius] 向量创建一个椭圆区域.
 */
fun World.createRegionEllipsoid(center: RegionVector, radius: RegionVector): RegionEllipsoid
        = RegionEllipsoid(this, center, radius)

/**
 * * Create a world border object for the given region. Note that [WorldBorder] is a rectangle.
 * * 将给定的区域创建一个世界边界对象. 注意 [WorldBorder] 是一个矩形.
 *
 * @see [WorldBorder]
 * @see [World.getWorldBorder]
 */
@JvmOverloads
fun Region.createWorldBorder(block: Applicator<WorldBorder> = {}): WorldBorder {
    val border = world.worldBorder
    border.center = center.toLocation(world)
    border.setSize(length.toDouble(), 0L)
    return border.apply(block)
}
