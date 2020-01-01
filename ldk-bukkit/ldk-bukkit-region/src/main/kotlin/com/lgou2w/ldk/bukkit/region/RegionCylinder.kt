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

import com.lgou2w.ldk.common.letIfNotNull
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.configuration.serialization.ConfigurationSerialization
import kotlin.math.floor

/**
 * ## RegionCylinder (圆柱区域)
 *
 * @see [Region]
 * @see [RegionBase]
 * @see [RegionFlat]
 * @author lgou2w
 */
open class RegionCylinder : RegionBase, RegionFlat {

  companion object {

    init {
      ConfigurationSerialization.registerClass(RegionCylinder::class.java)
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun deserialize(args: Map<String, Any>): RegionCylinder {
      val world = args["world"]?.toString().letIfNotNull(Bukkit::getWorld)
        ?: throw IllegalArgumentException("Unknown world: ${args["world"]}")
      val center = RegionVector2D.deserialize(args["center2D"] as Map<String, Any>)
      val radius = RegionVector2D.deserialize(args["radius"] as Map<String, Any>)
      val minY = args["minY"]?.toString()?.toInt() ?: 0
      val maxY = args["maxY"]?.toString()?.toInt() ?: 0
      return RegionCylinder(world, center, radius, minY, maxY)
    }
  }

  /**
   * * The center point vector of this cylinder region.
   * * 此圆柱区域的中心点向量.
   */
  var center2D : RegionVector2D
  /**
   * * The radius vector of this cylinder region.
   * * 此圆柱区域的半径向量.
   */
  var radius : RegionVector2D
  /**
   * @see [minimumY]
   */
  var minY : Int = 0
  /**
   * @see [maximumY]
   */
  var maxY : Int = 0

  private var hasY : Boolean

  constructor(
    world: World,
    center2D: RegionVector2D,
    radius: RegionVector2D
  ) : this(world, center2D, radius, 0, 0) {
    this.hasY = false
  }

  constructor(
    world: World,
    center2D: RegionVector2D,
    radius: RegionVector2D,
    minY: Int,
    maxY: Int
  ) : super(world) {
    this.center2D = center2D
    this.radius = radius
    this.minY = minY
    this.maxY = maxY
    this.hasY = true
  }

  override val minimumPoint : RegionVector
    get() = (center2D - radius).toRegionVector(minY)

  override val maximumPoint : RegionVector
    get() = (center2D + radius).toRegionVector(maxY)

  override val center : RegionVector
    get() = center2D.toRegionVector((maxY + minY) / 2.0)

  override val area : Int
    get() = floor(radius.x * radius.z * Math.PI * height).toInt()

  override val width : Int
    get() = (radius.x * 2.0).toInt()

  override val height : Int
    get() = maxY - minY + 1

  override val length : Int
    get() = (radius.z * 2.0).toInt()

  override fun contains(x: Double, y: Double, z: Double): Boolean
    = contains(RegionVector(x, y, z))

  override fun contains(vector: RegionVector): Boolean {
    val blockY = vector.blockY
    if (blockY < minY || blockY > maxY)
      return false
    return ((vector.toRegionVector2D() - center2D) / radius).lengthSq() <= 1.0
  }

  override var minimumY : Int
    get() = minY
    set(value) {
      minY = value
      hasY = true
    }

  override var maximumY : Int
    get() = maxY
    set(value) {
      maxY = value
      hasY = true
    }

  fun setY(y: Int) : Boolean {
    return if (!hasY) {
      maxY = y
      minY = y
      hasY = true
      true
    } else if (y < minY) {
      minY = y
      true
    } else if (y > maxY) {
      maxY = y
      true
    } else {
      false
    }
  }

  override fun iterator(): Iterator<RegionVectorBlock>
    = RegionIteratorFlat3D(this)

  override fun asFlat(): Iterable<RegionVector2D> {
    return object : Iterable<RegionVector2D> {
      override fun iterator(): Iterator<RegionVector2D> {
        return RegionIteratorFlat(this@RegionCylinder)
      }
    }
  }

  override fun serialize(): MutableMap<String, Any> {
    val result = LinkedHashMap<String, Any>()
    result["world"] = world.name
    result["center2D"] = center2D.serialize()
    result["radius"] = radius.serialize()
    result["minY"] = minY
    result["maxY"] = maxY
    return result
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + center2D.hashCode()
    result = 31 * result + radius.hashCode()
    result = 31 * result + minY.hashCode()
    result = 31 * result + maxY.hashCode()
    result = 31 * result + hasY.hashCode()
    return result
  }

  override fun equals(other: Any?): Boolean {
    if (other === this)
      return true
    if (other is RegionCylinder)
      return super.equals(other) &&
        minY == other.minY &&
        maxY == other.maxY &&
        hasY == other.hasY &&
        center2D == other.center2D &&
        radius == other.radius
    return false
  }

  override fun toString(): String {
    return "RegionCylinder(world=${world.name}, center2D=$center2D, radius=$radius, minY=$minY, maxY=$maxY)"
  }
}
