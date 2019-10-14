/*
 * Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>
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
import com.lgou2w.ldk.common.isTrue
import com.lgou2w.ldk.common.lazyAnyOrNullClass
import com.lgou2w.ldk.common.letIfNotNull
import com.lgou2w.ldk.common.notNull
import com.lgou2w.ldk.reflect.AccessorMethod
import com.lgou2w.ldk.reflect.FuzzyReflect
import com.lgou2w.ldk.reflect.StructureModifier
import com.sk89q.worldedit.IncompleteRegionException
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
        is CuboidRegion -> {
          val pos1 = REGION_METHOD_ACCESSOR(region, "getPos1").invoke(region).notNull()
          val pos2 = REGION_METHOD_ACCESSOR(region, "getPos2").invoke(region).notNull()
          RegionCuboid(world, toRegionVectorBlock(pos1), toRegionVectorBlock(pos2))
        }
        is CylinderRegion -> {
          val center = REGION_METHOD_ACCESSOR(region, "getCenter").invoke(region).notNull()
          val radius = REGION_METHOD_ACCESSOR(region, "getRadius").invoke(region).notNull()
          RegionCylinder(world, toRegionVector2D(center), toRegionVector2D(radius), region.minimumY, region.maximumY)
        }
        is EllipsoidRegion -> {
          val center = REGION_METHOD_ACCESSOR(region, "getCenter").invoke(region).notNull()
          val radius = REGION_METHOD_ACCESSOR(region, "getRadius").invoke(region).notNull()
          RegionEllipsoid(world, toRegionVectorBlock(center), toRegionVectorBlock(radius))
        }
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

  companion object {

    const val NAME = "WorldEdit"

    ///
    /// Compatibility layer
    ///

    @JvmStatic private val CLASS_LEGACY_VECTOR by lazyAnyOrNullClass {
      try {
        DependWorldEdit::class.java.classLoader.loadClass("com.sk89q.worldedit.Vector") // Before in 7.0.0
      } catch (e: ClassNotFoundException) {
        null
      }
    }
    @JvmStatic private val CLASS_LEGACY_VECTOR_2D by lazyAnyOrNullClass {
      try {
        DependWorldEdit::class.java.classLoader.loadClass("com.sk89q.worldedit.Vector2D") // Before in 7.0.0
      } catch (e: ClassNotFoundException) {
        null
      }
    }
    @JvmStatic private val STRUCTURE_LEGACY_VECTOR by lazy {
      CLASS_LEGACY_VECTOR.letIfNotNull { StructureModifier.of<Any>(it) }
    }
    @JvmStatic private val STRUCTURE_LEGACY_VECTOR_2D by lazy {
      CLASS_LEGACY_VECTOR_2D.letIfNotNull { StructureModifier.of<Any>(it) }
    }

    @JvmStatic private val REGION_METHOD_ACCESSORS : MutableMap<String, AccessorMethod<Any, Any>> = HashMap()
    @JvmStatic private val REGION_METHOD_ACCESSOR : (Any, String) -> AccessorMethod<Any, Any> = { obj, name ->
      val wrapName = "${obj::class.java.simpleName}#$name"
      var accessor = REGION_METHOD_ACCESSORS[wrapName]
      if (accessor == null) {
        accessor = FuzzyReflect.of(obj, false)
          .useMethodMatcher()
          .withName(name)
          .resultAccessor()
        REGION_METHOD_ACCESSORS[wrapName] = accessor
      }
      accessor
    }

    @JvmStatic private fun World.toAdapter() = BukkitWorld(this)

    @JvmStatic private fun toRegionVectorBlock(obj: Any): RegionVectorBlock {
      return if (CLASS_LEGACY_VECTOR?.isInstance(obj).isTrue()) {
        val structure = STRUCTURE_LEGACY_VECTOR.notNull().withTarget(obj).withType(Double::class.java)
        val x = structure.read(0) ?: .0
        val y = structure.read(1) ?: .0
        val z = structure.read(2) ?: .0
        RegionVectorBlock(x, y, z)
      } else {
        return if (obj is com.sk89q.worldedit.math.BlockVector3)
          RegionVectorBlock(obj.blockX, obj.blockY, obj.blockZ)
        else if (obj is com.sk89q.worldedit.math.Vector3)
          RegionVectorBlock(obj.x, obj.y, obj.z)
        else
          throw UnsupportedOperationException("Incompatible vector type: ${obj::class.java.canonicalName}")
      }
    }
    @JvmStatic private fun toRegionVector2D(obj: Any): RegionVector2D {
      return if (CLASS_LEGACY_VECTOR?.isInstance(obj).isTrue()) {
        toRegionVectorBlock(obj).toRegionVector2D()
      } else if (CLASS_LEGACY_VECTOR_2D?.isInstance(obj).isTrue()) {
        val structure = STRUCTURE_LEGACY_VECTOR_2D.notNull().withTarget(obj).withType(Double::class.java)
        val x = structure.read(0) ?: .0
        val z = structure.read(1) ?: .0
        RegionVector2D(x, z)
      } else {
        return if (obj is com.sk89q.worldedit.math.Vector3)
          RegionVector2D(obj.x, obj.z)
        else if (obj is com.sk89q.worldedit.math.Vector2)
          RegionVector2D(obj.x, obj.z)
        else
          throw UnsupportedOperationException("Incompatible vector type: ${obj::class.java.canonicalName}")
      }
    }
  }
}
