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

package com.lgou2w.ldk.bukkit.entity

import com.lgou2w.ldk.bukkit.potion.PotionEffectCustom
import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

/**************************************************************************
 *
 * org.bukkit.entity.Entity Extended
 *
 **************************************************************************/

/**
 * @since 0.1.7-rc3
 */
@JvmOverloads
fun Entity.readTag(block: Applicator<NBTTagCompound> = {}): NBTTagCompound
        = EntityFactory.readTag(this).also(block)

/**
 * @since 0.1.7-rc3
 */
fun <T : Entity> T.modifyTag(block: Applicator<NBTTagCompound>): T
        = EntityFactory.modifyTag(this, block)

fun Entity.getNearbyEntities(x: Double, y: Double, z: Double) : List<Entity>
        = world.getNearbyEntities(location, x, y, z).toList()

fun Entity.getNearbyEntities(range: Double) : List<Entity>
        = world.getNearbyEntities(location, range, range, range).toList()

fun <T : Entity> Entity.getNearbyEntities(type: Class<T>, x: Double, y: Double, z: Double) : List<T>
        = getNearbyEntities(x, y, z).filterIsInstance(type)

fun <T : Entity> Entity.getNearbyEntities(type: Class<T>, range: Double) : List<T>
        = getNearbyEntities(range).filterIsInstance(type)

/**************************************************************************
 *
 * org.bukkit.entity.Entity TargetHelper Extended
 *
 **************************************************************************/

fun Entity.isInFront(target: Entity) : Boolean {
    val facing = location.direction
    val relative = target.location.subtract(location).toVector().normalize()
    return facing.dot(relative) >= .0
}

fun Entity.isInFront(target: Entity, angle: Double) : Boolean {
    if (angle <= .0) return false
    if (angle >= 360.0) return true
    val dotTarget = Math.cos(angle)
    val facing = location.direction
    val relative = target.location.subtract(location).toVector().normalize()
    return facing.dot(relative) >= dotTarget
}

fun Entity.isBehind(target: Entity) : Boolean
        = !isInFront(target)

fun Entity.isBehind(target: Entity, angle: Double) : Boolean {
    if (angle <= .0) return false
    if (angle >= 360.0) return true
    val dotTarget = Math.cos(angle)
    val facing = location.direction
    val relative = location.subtract(target.location).toVector().normalize()
    return facing.dot(relative) >= dotTarget
}

@JvmOverloads
fun Entity.getNearbyTargets(x: Double, y: Double, z: Double, tolerance: Double = 4.0) : List<Entity>
        = getNearbyTargets(Entity::class.java, x, y, z, tolerance)

@JvmOverloads
fun Entity.getNearbyTargets(range: Double, tolerance: Double = 4.0) : List<Entity>
        = getNearbyTargets(Entity::class.java, range, range, range, tolerance)

@JvmOverloads
fun <T : Entity> Entity.getNearbyTargets(type: Class<T>, x: Double, y: Double, z: Double, tolerance: Double = 4.0) : List<T> {
    val facing = location.direction
    val fLengthSq = facing.lengthSquared()
    return getNearbyEntities(x, y, z)
        .asSequence()
        .filter { type.isInstance(it) && isInFront(it) }
        .filter {
            val relative = it.location.subtract(location).toVector()
            val dot = relative.dot(facing)
            val rLengthSq = relative.lengthSquared()
            val cosSquared = dot * dot / (rLengthSq * fLengthSq)
            val sinSquared = 1.0 - cosSquared
            val dSquared = rLengthSq * sinSquared
            dSquared < tolerance
        }
        .map { type.cast(it) }
        .toList()
}

@JvmOverloads
fun <T : Entity> Entity.getNearbyTargets(type: Class<T>, range: Double, tolerance: Double = 4.0) : List<T>
        = getNearbyTargets(type, range, range, range, tolerance)

@JvmOverloads
fun Entity.getNearbyTarget(x: Double, y: Double, z: Double, tolerance: Double = 4.0) : Entity?
        = getNearbyTarget(Entity::class.java, x, y, z, tolerance)

@JvmOverloads
fun Entity.getNearbyTarget(range: Double, tolerance: Double = 4.0) : Entity?
        = getNearbyTarget(Entity::class.java, range, range, range, tolerance)

@JvmOverloads
fun <T : Entity> Entity.getNearbyTarget(type: Class<T>, x: Double, y: Double, z: Double, tolerance: Double = 4.0) : T? {
    val targets = getNearbyTargets(type, x, y, z, tolerance)
    return when {
        targets.isEmpty() -> null
        targets.size == 1 -> targets.first()
        else -> {
            var target = targets.first()
            var minDistance = target.location.distanceSquared(location)
            for (alternate in targets) {
                val distance = alternate.location.distanceSquared(location)
                if (distance < minDistance) {
                    minDistance = distance
                    target = alternate
                }
            }
            target
        }
    }
}

@JvmOverloads
fun <T : Entity> Entity.getNearbyTarget(type: Class<T>, range: Double, tolerance: Double = 4.0) : T?
        = getNearbyTarget(type, range, range, range, tolerance)

/**************************************************************************
 *
 * org.bukkit.entity.LivingEntity Extended
 *
 **************************************************************************/

var LivingEntity.itemInHand: ItemStack?
    get() = EntityFactory.getItemInHand(this)
    set(value) { EntityFactory.setItemInHand(this, value) }

var LivingEntity.itemInMainHand: ItemStack?
    get() = EntityFactory.getItemInMainHand(this)
    set(value) { EntityFactory.setItemInMainHand(this, value) }

var LivingEntity.itemInOffHand: ItemStack?
    get() = EntityFactory.getItemInOffHand(this)
    set(value) { EntityFactory.setItemInOffHand(this, value) }

/**
 * @since 0.1.7-rc3
 */
@JvmOverloads
fun LivingEntity.applyToEffect(effectCustom: PotionEffectCustom, force: Boolean = false): Boolean
        = effectCustom.applyToEntity(this, force)
