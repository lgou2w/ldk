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

/**
 * ## RegionIterator (区域迭代器)
 *
 * @see [RegionVectorBlock]
 * @see [Iterator]
 * @author lgou2w
 */
class RegionIterator(
        private val region: Region
) : Iterator<RegionVectorBlock> {

    private val maxX : Int
    private val maxY : Int
    private val maxZ : Int
    private val min : RegionVector
    private var nextX : Int
    private var nextY : Int
    private var nextZ : Int

    init {
        val max = region.maximumPoint
        this.maxX = max.blockX
        this.maxY = max.blockY
        this.maxZ = max.blockZ
        this.min = region.minimumPoint
        this.nextX = min.blockX
        this.nextY = min.blockY
        this.nextZ = min.blockZ
        this.forward()
    }

    private fun forward() {
        while (hasNext() && !region.contains(RegionVector(nextX, nextY, nextZ)))
            forwardOne()
    }

    private fun forwardOne() {
        if (++nextX <= maxX)
            return
        nextX = min.blockX
        if (++nextY <= maxY)
            return
        nextY = min.blockY
        if (++nextZ <= maxZ)
            return
        nextX = 0x7FFFFFFF
    }

    override fun hasNext(): Boolean
            = nextX != 0x7FFFFFFF

    override fun next(): RegionVectorBlock {
        if (!hasNext())
            throw NoSuchElementException()
        val answer = RegionVectorBlock(nextX, nextY, nextZ)
        forwardOne()
        forward()
        return answer
    }
}
