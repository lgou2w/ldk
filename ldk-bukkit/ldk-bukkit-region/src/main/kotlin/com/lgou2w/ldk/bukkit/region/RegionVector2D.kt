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

import com.lgou2w.ldk.common.ComparisonChain
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.serialization.ConfigurationSerializable

open class RegionVector2D(
        val x: Double,
        val z: Double
) : ConfigurationSerializable,
        Comparable<RegionVector>,
        Cloneable {

    companion object {

        @JvmField val ZERO = RegionVector2D(.0, .0)

        @JvmStatic
        fun deserialize(args: Map<String, Any>): RegionVector2D {
            val x = args["x"]?.toString()?.toDouble() ?: .0
            val z = args["z"]?.toString()?.toDouble() ?: .0
            return RegionVector2D(x, z)
        }
    }

    constructor(x: Float, z: Float) : this(x.toDouble(), z.toDouble())
    constructor(x: Int, z: Int) : this(x.toDouble(), z.toDouble())
    constructor(other: RegionVector2D) : this(other.x, other.z)
    constructor() : this(.0, .0)

    override fun compareTo(other: RegionVector): Int {
        return ComparisonChain.start()
            .compare(x, other.x)
            .compare(z, other.z)
            .result
    }

    override fun serialize(): MutableMap<String, Any> {
        val result = LinkedHashMap<String, Any>()
        result["x"] = x
        result["z"] = z
        return result
    }

    override fun clone(): RegionVector2D {
        return RegionVector2D(x, z)
    }

    val blockX : Int
        get() = Math.round(x).toInt()

    val blockZ : Int
        get() = Math.round(z).toInt()

    fun setX(x: Double): RegionVector2D
            = RegionVector2D(x, z)

    fun setZ(z: Double): RegionVector2D
            = RegionVector2D(x, z)

    fun setX(x: Int): RegionVector2D
            = RegionVector2D(x.toDouble(), z)

    fun setZ(z: Int): RegionVector2D
            = RegionVector2D(x, z.toDouble())

    fun length(): Double
            = Math.sqrt(lengthSq())

    fun lengthSq(): Double
            = x * x + z * z

    fun distance(vector: RegionVector2D): Double
            = Math.sqrt(distanceSq(vector))

    fun distanceSq(vector: RegionVector2D): Double
            = Math.pow(x - vector.x, 2.0) + Math.pow(z - vector.z, 2.0)

    fun normalize(): RegionVector2D
            = div(length())

    fun toRegionVector(y: Double = .0): RegionVector
            = RegionVector(x, y, z)

    fun toRegionVector(y: Int = 0): RegionVector
            = RegionVector(x, y.toDouble(), z)

    @JvmOverloads
    fun toLocation(world: World, y: Double = .0, yaw: Float = .0f, pitch: Float = .0f): Location
            = Location(world, x, y, z, yaw, pitch)

    /** operator */

    operator fun inc(): RegionVector2D
            = RegionVector2D(x + 1.0, z + 1.0)

    operator fun dec(): RegionVector2D
            = RegionVector2D(x - 1.0, z - 1.0)

    operator fun plus(other: RegionVector2D): RegionVector2D
            = RegionVector2D(x + other.x, z + other.z)

    operator fun plus(other: Double): RegionVector2D
            = RegionVector2D(x + other, z + other)

    operator fun plus(other: Float): RegionVector2D
            = RegionVector2D(x + other, z + other)

    operator fun plus(other: Int): RegionVector2D
            = RegionVector2D(x + other, z + other)

    fun plus(x: Double, z: Double): RegionVector2D
            = RegionVector2D(this.x + x, this.z + z)

    fun plus(x: Int, z: Int): RegionVector2D
            = RegionVector2D(this.x + x, this.z + z)

    operator fun minus(other: RegionVector2D): RegionVector2D
            = RegionVector2D(x - other.x, z - other.z)

    operator fun minus(other: Double): RegionVector2D
            = RegionVector2D(x - other, z - other)

    operator fun minus(other: Float): RegionVector2D
            = RegionVector2D(x - other, z - other)

    operator fun minus(other: Int): RegionVector2D
            = RegionVector2D(x - other, z - other)

    fun minus(x: Double, z: Double): RegionVector2D
            = RegionVector2D(this.x - x, this.z - z)

    fun minus(x: Int, z: Int): RegionVector2D
            = RegionVector2D(this.x - x, this.z - z)

    operator fun times(other: RegionVector2D): RegionVector2D
            = RegionVector2D(x * other.x, z * other.z)

    operator fun times(other: Double): RegionVector2D
            = RegionVector2D(x * other, z * other)

    operator fun times(other: Float): RegionVector2D
            = RegionVector2D(x * other, z * other)

    operator fun times(other: Int): RegionVector2D
            = RegionVector2D(x * other, z * other)

    fun times(x: Double, z: Double): RegionVector2D
            = RegionVector2D(this.x * x, this.z * z)

    fun times(x: Int, z: Int): RegionVector2D
            = RegionVector2D(this.x * x, this.z * z)

    operator fun div(other: RegionVector2D): RegionVector2D
            = RegionVector2D(x / other.x, z / other.z)

    operator fun div(other: Double): RegionVector2D
            = RegionVector2D(x / other, z / other)

    operator fun div(other: Float): RegionVector2D
            = RegionVector2D(x / other, z / other)

    operator fun div(other: Int): RegionVector2D
            = RegionVector2D(x / other, z / other)

    fun div(x: Double, z: Double): RegionVector2D
            = RegionVector2D(this.x / x, this.z / z)

    fun div(x: Int, z: Int): RegionVector2D
            = RegionVector2D(this.x / x, this.z / z)

    override fun equals(other: Any?): Boolean {
        if(other === this)
            return true
        if(other is RegionVector2D)
            return x == other.x && z == other.z
        return false
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    override fun toString(): String {
        return "RegionVector2D(x=$x, z=$z)"
    }
}
