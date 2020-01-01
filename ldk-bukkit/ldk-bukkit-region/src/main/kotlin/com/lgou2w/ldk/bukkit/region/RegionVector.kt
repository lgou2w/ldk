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

import com.lgou2w.ldk.common.ComparisonChain
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.ConfigurationSerialization
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt

/**
 * ## RegionVector (区域向量)
 *
 * @author lgou2w
 */
open class RegionVector(
  val x: Double,
  val y: Double,
  val z: Double
) : ConfigurationSerializable,
  Comparable<RegionVector>,
  Cloneable {

  companion object {

    init {
      ConfigurationSerialization.registerClass(RegionVector::class.java)
    }

    @JvmField val ZERO = RegionVector(.0, .0, .0)

    @JvmStatic
    fun deserialize(args: Map<String, Any>): RegionVector {
      val x = args["x"]?.toString()?.toDoubleOrNull() ?: .0
      val y = args["y"]?.toString()?.toDoubleOrNull() ?: .0
      val z = args["z"]?.toString()?.toDoubleOrNull() ?: .0
      return RegionVector(x, y, z)
    }
  }

  constructor(x: Float, y: Float, z: Float) : this(x.toDouble(), y.toDouble(), z.toDouble())
  constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())
  constructor(other: RegionVector) : this(other.x, other.y, other.z)
  constructor() : this(.0, .0, .0)

  override fun compareTo(other: RegionVector): Int {
    return ComparisonChain.start()
      .compare(x, other.x)
      .compare(y, other.y)
      .compare(z, other.z)
      .result
  }

  override fun serialize(): MutableMap<String, Any> {
    val result = LinkedHashMap<String, Any>()
    result["x"] = x
    result["y"] = y
    result["z"] = z
    return result
  }

  override fun clone(): RegionVector {
    return RegionVector(x, y, z)
  }

  val blockX : Int
    get() = x.roundToLong().toInt()

  val blockY : Int
    get() = y.roundToLong().toInt()

  val blockZ : Int
    get() = z.roundToLong().toInt()

  fun setX(x: Double): RegionVector
    = RegionVector(x, y, z)

  fun setY(y: Double): RegionVector
    = RegionVector(x, y, z)

  fun setZ(z: Double): RegionVector
    = RegionVector(x, y, z)

  fun setX(x: Int): RegionVector
    = RegionVector(x.toDouble(), y, z)

  fun setY(y: Int): RegionVector
    = RegionVector(x, y.toDouble(), z)

  fun setZ(z: Int): RegionVector
    = RegionVector(x, y, z.toDouble())

  fun length(): Double
    = sqrt(lengthSq())

  fun lengthSq(): Double
    = x * x + y * y + z * z

  fun distance(vector: RegionVector): Double
    = sqrt(distanceSq(vector))

  fun distanceSq(vector: RegionVector): Double
    = (x - vector.x).pow(2.0) + (y - vector.y).pow(2.0) + (z - vector.z).pow(2.0)

  fun normalize(): RegionVector
    = div(length())

  fun toRegionVector2D(): RegionVector2D
    = RegionVector2D(x, z)

  @JvmOverloads
  fun toLocation(world: World, yaw: Float = .0f, pitch: Float = .0f): Location
    = Location(world, x, y, z, yaw, pitch)

  operator fun inc(): RegionVector
    = RegionVector(x + 1.0, y + 1.0, z + 1.0)

  operator fun dec(): RegionVector
    = RegionVector(x - 1.0, y - 1.0, z - 1.0)

  operator fun plus(other: RegionVector): RegionVector
    = RegionVector(x + other.x, y + other.y, z + other.z)

  operator fun plus(other: Double): RegionVector
    = RegionVector(x + other, y + other, z + other)

  operator fun plus(other: Float): RegionVector
    = RegionVector(x + other, y + other, z + other)

  operator fun plus(other: Int): RegionVector
    = RegionVector(x + other, y + other, z + other)

  fun plus(x: Double, y: Double, z: Double): RegionVector
    = RegionVector(this.x + x, this.y + y, this.z + z)

  fun plus(x: Int, y: Int, z: Int): RegionVector
    = RegionVector(this.x + x, this.y + y, this.z + z)

  operator fun minus(other: RegionVector): RegionVector
    = RegionVector(x - other.x, y - other.y, z - other.z)

  operator fun minus(other: Double): RegionVector
    = RegionVector(x - other, y - other, z - other)

  operator fun minus(other: Float): RegionVector
    = RegionVector(x - other, y - other, z - other)

  operator fun minus(other: Int): RegionVector
    = RegionVector(x - other, y - other, z - other)

  fun minus(x: Double, y: Double, z: Double): RegionVector
    = RegionVector(this.x - x, this.y - y, this.z - z)

  fun minus(x: Int, y: Int, z: Int): RegionVector
    = RegionVector(this.x - x, this.y - y, this.z - z)

  operator fun times(other: RegionVector): RegionVector
    = RegionVector(x * other.x, y * other.y, z * other.z)

  operator fun times(other: Double): RegionVector
    = RegionVector(x * other, y * other, z * other)

  operator fun times(other: Float): RegionVector
    = RegionVector(x * other, y * other, z * other)

  operator fun times(other: Int): RegionVector
    = RegionVector(x * other, y * other, z * other)

  fun times(x: Double, y: Double, z: Double): RegionVector
    = RegionVector(this.x * x, this.y * y, this.z * z)

  fun times(x: Int, y: Int, z: Int): RegionVector
    = RegionVector(this.x * x, this.y * y, this.z * z)

  operator fun div(other: RegionVector): RegionVector
    = RegionVector(x / other.x, y / other.y, z / other.z)

  operator fun div(other: Double): RegionVector
    = RegionVector(x / other, y / other, z / other)

  operator fun div(other: Float): RegionVector
    = RegionVector(x / other, y / other, z / other)

  operator fun div(other: Int): RegionVector
    = RegionVector(x / other, y / other, z / other)

  fun div(x: Double, y: Double, z: Double): RegionVector
    = RegionVector(this.x / x, this.y / y, this.z / z)

  fun div(x: Int, y: Int, z: Int): RegionVector
    = RegionVector(this.x / x, this.y / y, this.z / z)

  override fun hashCode(): Int {
    var result = x.hashCode()
    result = 31 * result + y.hashCode()
    result = 31 * result + z.hashCode()
    return result
  }

  override fun equals(other: Any?): Boolean {
    if (other === this)
      return true
    if (other is RegionVector)
      return x == other.x && y == other.y && z == other.z
    return false
  }

  override fun toString(): String {
    return "RegionVector(x=$x, y=$y, z=$z)"
  }
}
