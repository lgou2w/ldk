/*
 * Copyright (C) 2018 The lgou2w (lgou2w@hotmail.com)
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
import com.lgou2w.ldk.bukkit.region.RegionVector
import com.lgou2w.ldk.bukkit.region.RegionVector2D
import com.lgou2w.ldk.bukkit.region.RegionVectorBlock
import com.sk89q.worldedit.BlockVector2D
import com.sk89q.worldedit.LocalSession
import com.sk89q.worldedit.Vector
import com.sk89q.worldedit.Vector2D
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.bukkit.WorldEditPlugin
import com.sk89q.worldedit.bukkit.selections.CuboidSelection // since 7.0 removed
import com.sk89q.worldedit.bukkit.selections.CylinderSelection // since 7.0 removed
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.CylinderRegion
import com.sk89q.worldedit.regions.EllipsoidRegion
import com.sk89q.worldedit.regions.RegionSelector
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector
import com.sk89q.worldedit.regions.selector.CylinderRegionSelector
import org.bukkit.World
import org.bukkit.entity.Player

class DependWorldEdit : DependBase<WorldEditPlugin>(getPlugin(NAME)) {

    val isV7 : Boolean
        get() {
            checkReference()
            return pluginVersion.contains("7.0")
        }

    fun getSelection(player: Player) : Region? {
        checkReference()
        return if (!isV7) {
            V6().get(player)
        } else {
            V7().get(player)
        }
    }

    fun setSelection(player: Player, region: Region) {
        checkReference()
        if (!isV7) {
            V6().set(player, region)
        } else {
            V7().set(player, region)
        }
    }

    private inner class V6 {

        fun get(player: Player) : Region? {
            val selection = plugin.getSelection(player) ?: return null
            val world = selection.world ?: return null
            val region = selection.regionSelector.incompleteRegion
            return get(world, region)
        }

        fun get(world: World, region: com.sk89q.worldedit.regions.Region) : Region? {
            return if (region is CuboidRegion) {
                RegionCuboid(world, region.pos1.toRegionVectorBlock(), region.pos2.toRegionVectorBlock())
            } else if (region is CylinderRegion) {
                RegionCylinder(world, region.center.toRegionVector2D(), region.radius.toRegionVector2D(), region.minimumY, region.maximumY)
            } else if (region is EllipsoidRegion) {
                RegionEllipsoid(world, region.center.toRegionVectorBlock(), region.radius.toRegionVectorBlock())
            } else {
                null
            }
        }

        fun set(player: Player, region: Region) {
            val session = plugin.getSession(player)
            set(session, region)
        }

        fun set(session: LocalSession, region: Region) {
            val world = region.world
            val selection = if (region is RegionCuboid) {
                CuboidSelection(world, region.pos1.toVector(), region.pos2.toVector())
            } else if (region is RegionCylinder) {
                CylinderSelection(world, region.center2D.toVector(), region.radius.toVector(), region.minY, region.maxY)
            } else {
                throw IllegalArgumentException("Unsupported region types, WorldEdit only supports Cuboid and Cylinder.")
            }
            session.setRegionSelector(world.toAdapter() as com.sk89q.worldedit.world.World, selection.regionSelector)
        }
    }

    // V7.0.0
    // getSelection
    // TODO If the player has not selected any region, the acquired region is still not null, but a region with a ZERO value.

    private inner class V7 {

        fun get(player: Player) : Region? {
            val session = plugin.getSession(player)
            val selector = session.getRegionSelector(player.world.toAdapter() as com.sk89q.worldedit.world.World)
            return V6().get(player.world, selector.incompleteRegion)
        }

        fun set(player: Player, region: Region) {
            val session = plugin.getSession(player)
            val selector = if (region is RegionCuboid) {
                CuboidRegionSelector(region.world.toAdapter(), region.pos1.toVector(), region.pos2.toVector())
            } else if (region is RegionCylinder) {
                CylinderRegionSelector(region.world.toAdapter(), region.center2D.toVector(), region.radius.toVector(), region.minY, region.maxY)
            } else {
                throw IllegalArgumentException("Unsupported region types, WorldEdit only supports Cuboid and Cylinder.")
            } as RegionSelector
            session.setRegionSelector(selector.world, selector)
        }
    }

    private fun World.toAdapter() : BukkitWorld
            = BukkitWorld(this)
    private fun Vector.toRegionVectorBlock() : RegionVectorBlock
            = RegionVectorBlock(x, y, z)
    private fun Vector.toRegionVector2D() : RegionVector2D
            = RegionVector2D(x, z)
    private fun Vector2D.toRegionVector2D() : RegionVector2D
            = RegionVector2D(x, z)
    private fun RegionVector.toVector() : Vector
            = Vector(x, y, z)
    private fun RegionVector.toVector2D() : BlockVector2D
            = BlockVector2D(x, z)
    private fun RegionVector2D.toVector() : BlockVector2D
            = BlockVector2D(x, z)

    companion object {
        const val NAME = "WorldEdit"
    }
}
