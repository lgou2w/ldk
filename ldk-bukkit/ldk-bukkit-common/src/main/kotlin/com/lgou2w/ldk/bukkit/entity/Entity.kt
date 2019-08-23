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

package com.lgou2w.ldk.bukkit.entity

import com.lgou2w.ldk.bukkit.potion.PotionEffectCustom
import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.common.notNull
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import kotlin.math.cos

/**************************************************************************
 *
 * org.bukkit.entity.Entity Extended
 *
 **************************************************************************/

/**
 * * Read NBT tag data from the given entity.
 * * 从给定的实体读取 NBT 标签数据.
 *
 * @see [EntityFactory.readTag]
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun Entity.readTag(block: Applicator<NBTTagCompound> = {}): NBTTagCompound
        = EntityFactory.readTag(this).also(block)

/**
 * * Modify the NBT tag data for the given entity.
 * * 将给定的实体进行 NBT 标签数据的修改.
 *
 * @see [EntityFactory.modifyTag]
 * @since LDK 0.1.7-rc3
 */
fun <T : Entity> T.modifyTag(block: Applicator<NBTTagCompound>): T
        = EntityFactory.modifyTag(this, block)

/**
 * * Get a list of entities within the [x], [y], [z] radius near a given entity.
 * * 获取给定实体附近 [x], [y], [z] 半径内的实体列表.
 */
fun Entity.getNearbyEntities(x: Double, y: Double, z: Double): List<Entity>
        = world.getNearbyEntities(location, x, y, z).toList()

/**
 * * Get a list of entities within the [x], [y], [z] radius near a given location.
 * * 获取给定位置附近 [x], [y], [z] 半径内的实体列表.
 *
 * @throws [NullPointerException] If the world of the location is `null`.
 * @throws [NullPointerException] 如果位置的世界是 `null` 的.
 * @since LDK 0.1.8
 */
@Throws(NullPointerException::class)
fun Location.getNearbyEntities(x: Double, y: Double, z: Double): List<Entity>
        = world.notNull("The world of location cannot be null.").getNearbyEntities(this, x, y, z).toList()

/**
 * * Get a list of entities within the [range] radius near a given entity.
 * * 获取给定实体附近 [range] 半径内的实体列表.
 */
fun Entity.getNearbyEntities(range: Double): List<Entity>
        = world.getNearbyEntities(location, range, range, range).toList()

/**
 * * Get a list of entities within the [range] radius near a given location.
 * * 获取给定位置附近 [range] 半径内的实体列表.
 *
 * @throws [NullPointerException] If the world of the location is `null`.
 * @throws [NullPointerException] 如果位置的世界是 `null` 的.
 * @since LDK 0.1.8
 */
@Throws(NullPointerException::class)
fun Location.getNearbyEntities(range: Double): List<Entity>
        = world.notNull("The world of location cannot be null.").getNearbyEntities(this, range, range, range).toList()

/**
 * * Get a list of entities of the specified type [type] within the [x], [y], [z] radius near the given entity.
 * * 获取给定实体附近 [x], [y], [z] 半径内指定类型 [type] 的实体列表.
 */
fun <T : Entity> Entity.getNearbyEntities(type: Class<T>, x: Double, y: Double, z: Double): List<T>
        = getNearbyEntities(x, y, z).filterIsInstance(type)

/**
 * * Get a list of entities of the specified type [type] within the [x], [y], [z] radius near the given location.
 * * 获取给定位置附近 [x], [y], [z] 半径内指定类型 [type] 的实体列表.
 *
 * @throws [NullPointerException] If the world of the location is `null`.
 * @throws [NullPointerException] 如果位置的世界是 `null` 的.
 * @since LDK 0.1.8
 */
@Throws(NullPointerException::class)
fun <T : Entity> Location.getNearbyEntities(type: Class<T>, x: Double, y: Double, z: Double): List<T>
        = getNearbyEntities(x, y, z).filterIsInstance(type)

/**
 * * Get a list of entities of the specified type [type] within the [range] radius near the given entity.
 * * 获取给定实体附近 [range] 半径内指定类型 [type] 的实体列表.
 */
fun <T : Entity> Entity.getNearbyEntities(type: Class<T>, range: Double): List<T>
        = getNearbyEntities(range).filterIsInstance(type)

/**
 * * Get a list of entities of the specified type [type] within the [range] radius near the given location.
 * * 获取给定位置附近 [range] 半径内指定类型 [type] 的实体列表.
 *
 * @throws [NullPointerException] If the world of the location is `null`.
 * @throws [NullPointerException] 如果位置的世界是 `null` 的.
 * @since LDK 0.1.8
 */
@Throws(NullPointerException::class)
fun <T : Entity> Location.getNearbyEntities(type: Class<T>, range: Double): List<T>
        = getNearbyEntities(range).filterIsInstance(type)

/**************************************************************************
 *
 * org.bukkit.entity.Entity TargetHelper Extended
 *
 **************************************************************************/

/**
 * * Determine if the given [target] location is in front of the given location.
 * * 判断给定的目标位置 [target] 是否在给定位置前面.
 *
 * @since LDK 0.1.8
 */
fun Location.isInFront(target: Location): Boolean {
    val facing = direction
    val relative = target.subtract(this).toVector().normalize()
    return facing.dot(relative) >= .0
}

/**
 * * Determine if the given [target] entity is in front of the given entity.
 * * 判断给定的目标实体 [target] 是否在给定实体前面.
 */
fun Entity.isInFront(target: Entity): Boolean
        = location.isInFront(target.location)

/**
 * * Determine if the given [target] location is in front of the given location.
 * * 判断给定的目标位置 [target] 是否在给定位置前面.
 *
 * @since LDK 0.1.8
 */
fun Location.isInFront(target: Location, angle: Double): Boolean {
    if (angle <= .0) return false
    if (angle >= 360.0) return true
    val dotTarget = cos(angle)
    val facing = direction
    val relative = target.subtract(this).toVector().normalize()
    return facing.dot(relative) >= dotTarget
}

/**
 * * Determine if the given [target] entity is in front of the given entity.
 * * 判断给定的目标实体 [target] 是否在给定实体前面.
 */
fun Entity.isInFront(target: Entity, angle: Double): Boolean
        = location.isInFront(target.location, angle)

/**
 * * Determine if the given [target] location is in behind of the given location.
 * * 判断给定的目标位置 [target] 是否在给定位置后面.
 *
 * @since LDK 0.1.8
 */
fun Location.isBehind(target: Location): Boolean
        = !isInFront(target)

/**
 * * Determine if the given [target] entity is in behind of the given entity.
 * * 判断给定的目标实体 [target] 是否在给定实体后面.
 */
fun Entity.isBehind(target: Entity): Boolean
        = !location.isInFront(target.location)

/**
 * * Determine if the given [target] location is in behind of the given location.
 * * 判断给定的目标位置 [target] 是否在给定位置后面.
 *
 * @since LDK 0.1.8
 */
fun Location.isBehind(target: Location, angle: Double): Boolean {
    if (angle <= .0) return false
    if (angle >= 360.0) return true
    val dotTarget = cos(angle)
    val facing = direction
    val relative = subtract(target).toVector().normalize()
    return facing.dot(relative) >= dotTarget
}

/**
 * * Determine if the given [target] entity is in behind of the given entity.
 * * 判断给定的目标实体 [target] 是否在给定实体后面.
 */
fun Entity.isBehind(target: Entity, angle: Double): Boolean
        = location.isBehind(target.location, angle)

/**
 * * Get the list of entities in front of the [x], [y], [z] radius near the given location.
 * * 获取给定位置附近 [x], [y], [z] 半径内面前的实体列表.
 *
 * @throws [NullPointerException] If the world of the location is `null`.
 * @throws [NullPointerException] 如果位置的世界是 `null` 的.
 * @since LDK 0.1.8
 */
@JvmOverloads
@Throws(NullPointerException::class)
fun Location.getNearbyTargets(x: Double, y: Double, z: Double, tolerance: Double = 4.0): List<Entity>
        = getNearbyTargets(Entity::class.java, x, y, z, tolerance)

/**
 * * Get the list of entities in front of the [x], [y], [z] radius near the given entity.
 * * 获取给定实体附近 [x], [y], [z] 半径内面前的实体列表.
 */
@JvmOverloads
fun Entity.getNearbyTargets(x: Double, y: Double, z: Double, tolerance: Double = 4.0): List<Entity>
        = getNearbyTargets(Entity::class.java, x, y, z, tolerance)

/**
 * * Get the list of entities in front of the [range] radius near the given location.
 * * 获取给定位置附近 [range] 半径内面前的实体列表.
 *
 * @throws [NullPointerException] If the world of the location is `null`.
 * @throws [NullPointerException] 如果位置的世界是 `null` 的.
 * @since LDK 0.1.8
 */
@JvmOverloads
@Throws(NullPointerException::class)
fun Location.getNearbyTargets(range: Double, tolerance: Double = 4.0): List<Entity>
        = getNearbyTargets(Entity::class.java, range, range, range, tolerance)

/**
 * * Get the list of entities in front of the [range] radius near the given entity.
 * * 获取给定实体附近 [range] 半径内面前的实体列表.
 */
@JvmOverloads
fun Entity.getNearbyTargets(range: Double, tolerance: Double = 4.0): List<Entity>
        = getNearbyTargets(Entity::class.java, range, range, range, tolerance)

/**
 * * Get the list of specified [type] entities in front of the [x], [y], [z] radius near the given location.
 * * 获取给定位置附近 [x], [y], [z] 半径内面前的指定类型 [type] 实体列表.
 *
 * @throws [NullPointerException] If the world of the location is `null`.
 * @throws [NullPointerException] 如果位置的世界是 `null` 的.
 * @since LDK 0.1.8
 */
@JvmOverloads
@Throws(NullPointerException::class)
fun <T : Entity> Location.getNearbyTargets(type: Class<T>, x: Double, y: Double, z: Double, tolerance: Double = 4.0): List<T> {
    val facing = direction
    val fLengthSq = facing.lengthSquared()
    return getNearbyEntities(x, y, z)
        .asSequence()
        .filter { type.isInstance(it) && isInFront(it.location) }
        .filter {
            val relative = it.location.subtract(this).toVector()
            val dot = relative.dot(facing)
            val rLengthSq = relative.lengthSquared()
            val cosSquared = dot * dot / (rLengthSq * fLengthSq)
            val sinSquared = 1.0 - cosSquared
            val dSquared = rLengthSq * sinSquared
            dSquared < tolerance
        }
        .map(type::cast)
        .toList()
}

/**
 * * Get the list of specified [type] entities in front of the [x], [y], [z] radius near the given entity.
 * * 获取给定实体附近 [x], [y], [z] 半径内面前的指定类型 [type] 实体列表.
 */
@JvmOverloads
fun <T : Entity> Entity.getNearbyTargets(type: Class<T>, x: Double, y: Double, z: Double, tolerance: Double = 4.0): List<T>
        = location.getNearbyTargets(type, x, y, z, tolerance)

/**
 * * Get the list of specified [type] entities in front of the [range] radius near the given location.
 * * 获取给定位置附近 [range] 半径内面前的指定类型 [type] 实体列表.
 *
 * @throws [NullPointerException] If the world of the location is `null`.
 * @throws [NullPointerException] 如果位置的世界是 `null` 的.
 * @since LDK 0.1.8
 */
@JvmOverloads
@Throws(NullPointerException::class)
fun <T : Entity> Location.getNearbyTargets(type: Class<T>, range: Double, tolerance: Double = 4.0): List<T>
        = getNearbyTargets(type, range, range, range, tolerance)

/**
 * * Get the list of specified [type] entities in front of the [range] radius near the given entity.
 * * 获取给定实体附近 [range] 半径内面前的指定类型 [type] 实体列表.
 */
@JvmOverloads
fun <T : Entity> Entity.getNearbyTargets(type: Class<T>, range: Double, tolerance: Double = 4.0): List<T>
        = getNearbyTargets(type, range, range, range, tolerance)

/**
 * * Get the nearest entity in front of the [x], [y], [z] radius near the given location.
 * * 获取给定位置附近 [x], [y], [z] 半径内最近的实体.
 *
 * @throws [NullPointerException] If the world of the location is `null`.
 * @throws [NullPointerException] 如果位置的世界是 `null` 的.
 * @since LDK 0.1.8
 */
@JvmOverloads
@Throws(NullPointerException::class)
fun Location.getNearbyTarget(x: Double, y: Double, z: Double, tolerance: Double = 4.0): Entity?
        = getNearbyTarget(Entity::class.java, x, y, z, tolerance)

/**
 * * Get the nearest entity in front of the [x], [y], [z] radius near the given entity.
 * * 获取给定实体附近 [x], [y], [z] 半径内最近的实体.
 */
@JvmOverloads
fun Entity.getNearbyTarget(x: Double, y: Double, z: Double, tolerance: Double = 4.0): Entity?
        = getNearbyTarget(Entity::class.java, x, y, z, tolerance)

/**
 * * Get the nearest entity in front of the [range] radius near the given location.
 * * 获取给定位置附近 [range] 半径内最近的实体.
 *
 * @throws [NullPointerException] If the world of the location is `null`.
 * @throws [NullPointerException] 如果位置的世界是 `null` 的.
 * @since LDK 0.1.8
 */
@JvmOverloads
@Throws(NullPointerException::class)
fun Location.getNearbyTarget(range: Double, tolerance: Double = 4.0): Entity?
        = getNearbyTarget(Entity::class.java, range, range, range, tolerance)

/**
 * * Get the nearest entity in front of the [range] radius near the given entity.
 * * 获取给定实体附近 [range] 半径内最近的实体.
 */
@JvmOverloads
fun Entity.getNearbyTarget(range: Double, tolerance: Double = 4.0): Entity?
        = getNearbyTarget(Entity::class.java, range, range, range, tolerance)

/**
 * * Get the nearest of specified [type] entity in front of the [x], [y], [z] radius near the given location.
 * * 获取给定位置附近 [x], [y], [z] 半径内最近的指定类型 [type] 实体.
 *
 * @throws [NullPointerException] If the world of the location is `null`.
 * @throws [NullPointerException] 如果位置的世界是 `null` 的.
 * @since LDK 0.1.8
 */
@JvmOverloads
@Throws(NullPointerException::class)
fun <T : Entity> Location.getNearbyTarget(type: Class<T>, x: Double, y: Double, z: Double, tolerance: Double = 4.0): T? {
    val targets = getNearbyTargets(type, x, y, z, tolerance)
    return when {
        targets.isEmpty() -> null
        targets.size == 1 -> targets.first()
        else -> {
            var target = targets.first()
            var minDistance = target.location.distanceSquared(this)
            for (alternate in targets) {
                val distance = alternate.location.distanceSquared(this)
                if (distance < minDistance) {
                    minDistance = distance
                    target = alternate
                }
            }
            target
        }
    }
}

/**
 * * Get the nearest of specified [type] entity in front of the [x], [y], [z] radius near the given entity.
 * * 获取给定实体附近 [x], [y], [z] 半径内最近的指定类型 [type] 实体.
 */
@JvmOverloads
fun <T : Entity> Entity.getNearbyTarget(type: Class<T>, x: Double, y: Double, z: Double, tolerance: Double = 4.0): T?
        = location.getNearbyTarget(type, x, y, z, tolerance)

/**
 * * Get the nearest of specified [type] entity in front of the [range] radius near the given location.
 * * 获取给定位置附近 [range] 半径内最近的指定类型 [type] 实体.
 *
 * @throws [NullPointerException] If the world of the location is `null`.
 * @throws [NullPointerException] 如果位置的世界是 `null` 的.
 * @since LDK 0.1.8
 */
@JvmOverloads
@Throws(NullPointerException::class)
fun <T : Entity> Location.getNearbyTarget(type: Class<T>, range: Double, tolerance: Double = 4.0): T?
        = getNearbyTarget(type, range, range, range, tolerance)

/**
 * * Get the nearest of specified [type] entity in front of the [range] radius near the given entity.
 * * 获取给定实体附近 [range] 半径内最近的指定类型 [type] 实体.
 */
@JvmOverloads
fun <T : Entity> Entity.getNearbyTarget(type: Class<T>, range: Double, tolerance: Double = 4.0): T?
        = getNearbyTarget(type, range, range, range, tolerance)

/**************************************************************************
 *
 * org.bukkit.entity.LivingEntity Extended
 *
 **************************************************************************/

/**
 * * Indicate the item stack in main hand of the given [LivingEntity].
 * * 表示此 [LivingEntity] 主手中的物品栈.
 */
var LivingEntity.itemInHand: ItemStack?
    get() = EntityFactory.getItemInHand(this)
    set(value) { EntityFactory.setItemInHand(this, value) }

/**
 * * Indicate the item stack in main hand of the given [LivingEntity].
 * * 表示此 [LivingEntity] 主手中的物品栈.
 */
var LivingEntity.itemInMainHand: ItemStack?
    get() = EntityFactory.getItemInMainHand(this)
    set(value) { EntityFactory.setItemInMainHand(this, value) }

/**
 * * Indicate the item stack in off hand of the given [LivingEntity].
 * * 表示此 [LivingEntity] 副手中的物品栈.
 */
var LivingEntity.itemInOffHand: ItemStack?
    get() = EntityFactory.getItemInOffHand(this)
    set(value) { EntityFactory.setItemInOffHand(this, value) }

/**
 * @see [PotionEffectCustom.applyToEntity]
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun LivingEntity.applyToEffect(effectCustom: PotionEffectCustom, force: Boolean = false): Boolean
        = effectCustom.applyToEntity(this, force)
