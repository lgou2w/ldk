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

import com.lgou2w.ldk.common.IteratorChain
import com.lgou2w.ldk.common.letIfNotNull
import org.bukkit.Bukkit
import org.bukkit.World

/**
 * ## RegionCuboid (长方块区域)
 *
 * @see [Region]
 * @see [RegionBase]
 * @see [RegionFlat]
 * @author lgou2w
 */
open class RegionCuboid(
        world: World,
        /**
         * * Indicates the vector 1 of this cuboid region.
         * * 表示此长方块区域的向量 1.
         */
        var pos1: RegionVector,
        /**
         * * Indicates the vector 2 of this cuboid region.
         * * 表示此长方块区域的向量 2.
         */
        var pos2: RegionVector
) : RegionBase(world),
        RegionFlat {

    companion object {

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun deserialize(args: Map<String, Any>): RegionCuboid {
            val world = args["world"]?.toString().letIfNotNull { Bukkit.getWorld(this) }
                        ?: throw IllegalArgumentException("Unknown world: ${args["world"]}")
            val pos1 = RegionVector.deserialize(args["pos1"] as Map<String, Any>)
            val pos2 = RegionVector.deserialize(args["pos2"] as Map<String, Any>)
            return RegionCuboid(world, pos1, pos2)
        }
    }

    override val minimumPoint : RegionVector
        get() = RegionVector(Math.min(pos1.x, pos2.x), Math.min(pos1.y, pos2.y), Math.min(pos1.z, pos2.z))

    override val maximumPoint : RegionVector
        get() = RegionVector(Math.max(pos1.x, pos2.x), Math.max(pos1.y, pos2.y), Math.max(pos1.z, pos2.z))

    override fun contains(x: Double, y: Double, z: Double): Boolean {
        val min = minimumPoint
        val max = maximumPoint
        return x >= min.blockX && x <= max.blockX &&
               y >= min.blockY && y <= max.blockY &&
               z >= min.blockZ && z <= max.blockZ
    }

    override fun serialize(): MutableMap<String, Any> {
        val result = LinkedHashMap<String, Any>()
        result["world"] = world.name
        result["pos1"] = pos1.serialize()
        result["pos2"] = pos2.serialize()
        return result
    }

    override fun iterator(): Iterator<RegionVectorBlock> {
        return object : Iterator<RegionVectorBlock> {
            private val min = minimumPoint
            private val max = maximumPoint
            private var nextX = min.blockX
            private var nextY = min.blockY
            private var nextZ = min.blockZ
            override fun hasNext(): Boolean
                    = nextX != 0x7FFFFFFF
            override fun next(): RegionVectorBlock {
                if (!hasNext())
                    throw NoSuchElementException()
                val answer = RegionVectorBlock(nextX, nextY, nextZ)
                if (++nextX > max.blockX) {
                    nextX = min.blockX
                    if (++nextY > max.blockY) {
                        nextY = min.blockY
                        if (++nextZ > max.blockZ)
                            nextX = 0x7FFFFFFF
                    }
                }
                return answer
            }
        }
    }

    override val minimumY : Int
        get() = Math.min(pos1.blockY, pos2.blockY)

    override val maximumY : Int
        get() = Math.max(pos1.blockY, pos2.blockY)

    override fun asFlat(): Iterable<RegionVector2D> {
        return object : Iterable<RegionVector2D> {
            override fun iterator(): Iterator<RegionVector2D> {
                return object : Iterator<RegionVector2D> {
                    private val min = minimumPoint
                    private val max = maximumPoint
                    private var nextX = min.blockX
                    private var nextZ = min.blockZ
                    override fun hasNext(): Boolean
                            = nextX != 0x7FFFFFFF
                    override fun next(): RegionVector2D {
                        if (!hasNext())
                            throw NoSuchElementException()
                        val answer = RegionVector2D(nextX, nextZ)
                        if (++nextX > max.blockX) {
                            nextX = min.blockX
                            if (++nextZ > max.blockZ)
                                nextX = 0x7FFFFFFF
                        }
                        return answer
                    }
                }
            }
        }
    }

    /**
     * * Get the block vector of all faces of this cuboid region.
     * * 获取此长方体区域的所有面的方块向量.
     */
    val faces : Iterable<RegionVectorBlock>
        get() {
            return object : Iterable<RegionVectorBlock> {
                private val min = minimumPoint
                private val max = maximumPoint
                private val faceRegions = arrayOf(
                        RegionCuboid(world, pos1.setX(min.x), pos2.setX(min.x)),
                        RegionCuboid(world, pos1.setX(max.x), pos2.setX(max.x)),
                        RegionCuboid(world, pos1.setY(min.y), pos2.setY(min.y)),
                        RegionCuboid(world, pos1.setY(max.y), pos2.setY(max.y)),
                        RegionCuboid(world, pos1.setZ(min.z), pos2.setZ(min.z)),
                        RegionCuboid(world, pos1.setZ(max.z), pos2.setZ(max.z)))
                override fun iterator(): Iterator<RegionVectorBlock> {
                    return IteratorChain.concat(faceRegions.map { it.iterator() })
                }
            }
        }

    override fun equals(other: Any?): Boolean {
        if(other === this)
            return true
        if(other is RegionCuboid)
            return super.equals(other) && pos1 == other.pos1 && pos2 == other.pos2
        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + pos1.hashCode()
        result = 31 * result + pos2.hashCode()
        return result
    }

    override fun toString(): String {
        return "RegionCuboid(world=${world.name}, pos1=$pos1, pos2=$pos2)"
    }
}
