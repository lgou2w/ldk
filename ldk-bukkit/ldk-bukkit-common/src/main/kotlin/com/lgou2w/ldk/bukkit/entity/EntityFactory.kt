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

import com.lgou2w.ldk.bukkit.nbt.NBTFactory
import com.lgou2w.ldk.bukkit.reflect.MinecraftReflection
import com.lgou2w.ldk.bukkit.reflect.lazyCraftBukkitClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClass
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.common.isOrLater
import com.lgou2w.ldk.nbt.NBT
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.NBTType
import com.lgou2w.ldk.nbt.ofCompound
import com.lgou2w.ldk.reflect.AccessorMethod
import com.lgou2w.ldk.reflect.FuzzyReflect
import com.lgou2w.ldk.reflect.Visibility
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

object EntityFactory {

    @JvmStatic val CLASS_ENTITY by lazyMinecraftClass("Entity")
    @JvmStatic val CLASS_ENTITY_LIVING by lazyMinecraftClass("EntityLiving")
    @JvmStatic val CLASS_ENTITY_HUMAN by lazyMinecraftClass("EntityHuman")
    @JvmStatic val CLASS_ENTITY_PLAYER by lazyMinecraftClass("EntityPlayer")

    @JvmStatic val CLASS_CRAFT_ENTITY by lazyCraftBukkitClass("entity.CraftEntity")
    @JvmStatic val CLASS_CRAFT_ENTITY_HUMAN by lazyCraftBukkitClass("entity.CraftHumanEntity")
    @JvmStatic val CLASS_CRAFT_ENTITY_PLAYER by lazyCraftBukkitClass("entity.CraftPlayer")

    // OBC.entity.Entity -> public NMS.Entity getHandle()
    @JvmStatic val METHOD_GET_HANDLE : AccessorMethod<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_CRAFT_ENTITY, true)
            .useMethodMatcher()
            .withVisibilities(Visibility.PUBLIC)
            .withType(CLASS_ENTITY)
            .withName("getHandle")
            .resultAccessor()
    }

    // NMS.Entity -> public OBC.CraftEntity getBukkitEntity()
    @JvmStatic val METHOD_GET_BUKKIT_ENTITY : AccessorMethod<Any, Entity> by lazy {
        FuzzyReflect.of(CLASS_ENTITY, true)
            .useMethodMatcher()
            .withVisibilities(Visibility.PUBLIC)
            .withName("getBukkitEntity")
            .resultAccessorAs<Any, Entity>()
    }

    // NMS.Entity -> public NMS.NBTTagCompound save(NMS.NBTTagCompound)
    @JvmStatic val METHOD_ENTITY_SAVE_TAG : AccessorMethod<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_ENTITY, true)
            .useMethodMatcher()
            .withVisibilities(Visibility.PUBLIC)
            .withType(NBTFactory.CLASS_NBT_TAG_COMPOUND)
            .withParams(NBTFactory.CLASS_NBT_TAG_COMPOUND)
            .resultAccessor()
    }

    // NMS.Entity -> public NMS.NBTTagCompound read(NMS.NBTTagCompound)
    @JvmStatic val METHOD_ENTITY_READ_TAG : AccessorMethod<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_ENTITY, true)
            .useMethodMatcher()
            .withVisibilities(Visibility.PUBLIC)
            .withType(Void::class.java)
            .withParams(NBTFactory.CLASS_NBT_TAG_COMPOUND)
            .resultAccessor()
    }

    @JvmStatic
    fun asNMS(entity: Entity?): Any? {
        if (entity == null) return null
        return METHOD_GET_HANDLE.invoke(entity)
    }

    @JvmStatic
    fun asBukkit(nms: Any?): Entity? {
        if (nms == null) return null
        MinecraftReflection.isExpected(nms, CLASS_ENTITY)
        return METHOD_GET_BUKKIT_ENTITY.invoke(nms)
    }

    @JvmStatic
    inline fun <reified T : Entity> asBukkitEntity(nms: Any?): T? {
        val entity = asBukkit(nms) ?: return null
        return if (T::class.java.isInstance(entity)) T::class.java.cast(entity)
        else throw IllegalArgumentException(
                "The entity type ${entity::class.java.simpleName} does not match the expected ${T::class.java.simpleName}.")
    }

    @JvmStatic
    fun readTag(entity: Entity) : NBTTagCompound {
        val nmsEntity = asNMS(entity)
        val handle = NBTFactory.createInternal(NBTType.TAG_COMPOUND)
        val nms = METHOD_ENTITY_SAVE_TAG.invoke(nmsEntity, handle)
        return NBTFactory.fromNMS(nms) as? NBTTagCompound
        ?: ofCompound(NBT.TAG_ENTITY_TAG)
    }

    @JvmStatic
    fun writeTag(entity: Entity, tag: NBTTagCompound) {
        val nmsEntity = asNMS(entity)
        val nms = NBTFactory.toNMS(tag)
        METHOD_ENTITY_READ_TAG.invoke(nmsEntity, nms)
    }

    /**
     * @since 0.1.7-rc3
     */
    @JvmStatic
    fun <T : Entity> modifyTag(entity: T, applicator: Applicator<NBTTagCompound>): T {
        val tag = readTag(entity)
        applicator(tag)
        writeTag(entity, tag)
        return entity
    }

    @JvmStatic
    fun getItemInHand(entity: LivingEntity) : ItemStack? {
        return if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_9_R1))
            getItemInMainHand(entity)
        else
            @Suppress("DEPRECATION")
            entity.equipment.itemInHand
    }

    @JvmStatic
    fun setItemInHand(entity: LivingEntity, stack: ItemStack?) {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_9_R1))
            setItemInMainHand(entity, stack)
        else
            @Suppress("DEPRECATION")
            entity.equipment.itemInHand = stack
    }

    @JvmStatic
    fun getItemInMainHand(entity: LivingEntity) : ItemStack? {
        return if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_9_R1))
            entity.equipment.itemInMainHand
        else
            getItemInHand(entity)
    }

    @JvmStatic
    fun setItemInMainHand(entity: LivingEntity, stack: ItemStack?) {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_9_R1))
            entity.equipment.itemInMainHand = stack
        else
            setItemInHand(entity, stack)
    }

    @JvmStatic
    fun getItemInOffHand(entity: LivingEntity) : ItemStack? {
        return if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_9_R1))
            entity.equipment.itemInOffHand
        else
            getItemInHand(entity)
    }

    @JvmStatic
    fun setItemInOffHand(entity: LivingEntity, stack: ItemStack?) {
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_9_R1))
            entity.equipment.itemInOffHand = stack
        else
            setItemInHand(entity, stack)
    }
}
