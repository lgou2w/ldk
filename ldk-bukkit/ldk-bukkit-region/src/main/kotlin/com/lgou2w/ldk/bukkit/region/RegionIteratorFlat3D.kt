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

class RegionIteratorFlat3D(
        region: RegionFlat,
        private val flatIterator: Iterator<RegionVector2D>
) : Iterator<RegionVectorBlock> {

    private val minY: Int = region.minimumY
    private val maxY: Int = region.maximumY
    private var next2D: RegionVector2D? = if (flatIterator.hasNext()) flatIterator.next() else null
    private var nextY: Int = minY

    constructor(region: RegionFlat) : this(region, region.asFlat().iterator())

    override fun hasNext(): Boolean
            = next2D != null

    override fun next(): RegionVectorBlock {
        if (!hasNext())
            throw NoSuchElementException()
        val current = RegionVectorBlock(next2D!!.blockX, nextY, next2D!!.blockZ)
        when {
            nextY < maxY -> nextY += 1
            flatIterator.hasNext() -> {
                next2D = flatIterator.next()
                nextY = minY
            }
            else -> next2D = null
        }
        return current
    }
}
