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

class RegionIteratorFlat(
        private val region: Region
) : Iterator<RegionVector2D> {

    private val y : Int
    private val minX : Int
    private val maxX : Int
    private val maxZ : Int
    private var nextX : Int
    private var nextZ : Int

    init {
        val min = region.minimumPoint
        val max = region.maximumPoint
        this.y = min.blockY
        this.minX = min.blockX
        this.maxX = max.blockX
        this.maxZ = max.blockZ
        this.nextX = minX
        this.nextZ = min.blockZ
        this.forward()
    }

    private fun forward() {
        while (hasNext() && !region.contains(RegionVector(nextX, y, nextZ)))
            forwardOne()
    }

    private fun forwardOne() {
        if (++nextX <= maxX)
            return
        nextX = minX
        if (++nextZ <= maxZ)
            return
        nextX = 0x7FFFFFFF
    }

    override fun hasNext(): Boolean
            = nextX != 0x7FFFFFFF

    override fun next(): RegionVector2D {
        if (!hasNext())
            throw NoSuchElementException()
        val answer = RegionVector2D(nextX, nextZ)
        forwardOne()
        forward()
        return answer
    }
}
