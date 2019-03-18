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

package com.lgou2w.ldk.bukkit.depend

import com.lgou2w.ldk.bukkit.region.Region
import com.lgou2w.ldk.bukkit.region.RegionCuboid
import com.lgou2w.ldk.bukkit.region.RegionCylinder
import com.lgou2w.ldk.bukkit.region.RegionEllipsoid
import com.lgou2w.ldk.bukkit.region.RegionVector2D
import com.lgou2w.ldk.bukkit.region.RegionVectorBlock
import com.sk89q.worldedit.IncompleteRegionException
import com.sk89q.worldedit.Vector
import com.sk89q.worldedit.Vector2D
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.CylinderRegion
import com.sk89q.worldedit.regions.EllipsoidRegion
import org.bukkit.World
import org.bukkit.entity.Player

/**
 * ## DependWorldEdit (WorldEdit 插件依赖)
 *
 * @see [Depend]
 * @see [DependBase]
 * @author lgou2w
 */
class DependWorldEdit : DependBase<WorldEditPlugin>(getPlugin(NAME)) {

    /**
     * * Get the WorldEdit selected region of ​​the given [player]. If not, return `null`.
     * * 获取给定玩家 [player] 的 WorldEdit 选中区域. 如果没有则返回 `null`.
     */
    fun getSelectRegion(player: Player): Region? {
        checkReference()
        val worldWrapped = player.world.toAdapter() as com.sk89q.worldedit.world.World
        val session = plugin.getSession(player)
        val selector = session.getRegionSelector(worldWrapped)
        return try {
            val region = selector.region
            val world = player.world
            when (region) {
                is CuboidRegion -> RegionCuboid(world, region.pos1.toRegionVectorBlock(), region.pos2.toRegionVectorBlock())
                is CylinderRegion -> RegionCylinder(world, region.center.toRegionVector2D(), region.radius.toRegionVector2D(), region.minimumY, region.maximumY)
                is EllipsoidRegion -> RegionEllipsoid(world, region.center.toRegionVectorBlock(), region.radius.toRegionVectorBlock())
                else -> null
            }
        } catch (e: IncompleteRegionException) {
            null
        }
    }

// No longer supported
//    fun setSelectRegion(player: Player, region: Region) {
//        checkReference()
//    }

    private fun World.toAdapter(): BukkitWorld
            = BukkitWorld(this)
    private fun Vector.toRegionVectorBlock(): RegionVectorBlock
            = RegionVectorBlock(x, y, z)
    private fun Vector.toRegionVector2D(): RegionVector2D
            = RegionVector2D(x, z)
    private fun Vector2D.toRegionVector2D(): RegionVector2D
            = RegionVector2D(x, z)

    companion object {
        const val NAME = "WorldEdit"
    }
}
