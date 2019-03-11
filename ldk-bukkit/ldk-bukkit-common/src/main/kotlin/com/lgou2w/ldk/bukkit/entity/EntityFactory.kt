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

package com.lgou2w.ldk.bukkit.entity

import com.lgou2w.ldk.bukkit.nbt.NBTFactory
import com.lgou2w.ldk.bukkit.reflect.MinecraftReflection
import com.lgou2w.ldk.bukkit.reflect.lazyCraftBukkitClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClass
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.common.Applicator
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

/**
 * ## EntityFactory (实体工厂)
 *
 * @author lgou2w
 */
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

    /**
     * * Get the `NMS` object of the given [entity].
     * * 获取给定实体 [entity] 的 `NMS` 对象.
     */
    @JvmStatic
    fun asNMS(entity: Entity?): Any? {
        if (entity == null) return null
        return METHOD_GET_HANDLE.invoke(entity)
    }

    /**
     * * Get the Bukkit wrapper object for the given `NMS` entity.
     * * 获取给定 `NMS` 实体的 Bukkit 包装对象.
     *
     * @throws [IllegalArgumentException] If the entity object [nms] is not the expected `NMS` instance.
     * @throws [IllegalArgumentException] 如果实体对象 [nms] 不是预期的 `NMS` 实例.
     */
    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun asBukkit(nms: Any?): Entity? {
        if (nms == null) return null
        MinecraftReflection.isExpected(nms, CLASS_ENTITY)
        return METHOD_GET_BUKKIT_ENTITY.invoke(nms)
    }

    /**
     * @see [asBukkit]
     */
    @JvmStatic
    @Throws(IllegalArgumentException::class)
    inline fun <reified T : Entity> asBukkitEntity(nms: Any?): T? {
        val entity = asBukkit(nms) ?: return null
        return if (T::class.java.isInstance(entity)) T::class.java.cast(entity)
        else throw IllegalArgumentException(
                "The entity type ${entity::class.java.simpleName} does not match the expected ${T::class.java.simpleName}.")
    }

    /**
     * * Read NBT tag data from the given [entity].
     * * 从给定的实体 [entity] 读取 NBT 标签数据.
     */
    @JvmStatic
    fun readTag(entity: Entity): NBTTagCompound {
        val nmsEntity = asNMS(entity)
        val handle = NBTFactory.createInternal(NBTType.TAG_COMPOUND)
        val nms = METHOD_ENTITY_SAVE_TAG.invoke(nmsEntity, handle)
        return NBTFactory.fromNMS(nms) as? NBTTagCompound
        ?: ofCompound(NBT.TAG_ENTITY_TAG)
    }

    /**
     * * Write the given NBT [tag] to the given [entity].
     * * 将给定的 NBT 标签数据 [tag] 写入到给定的实体 [entity] 中.
     */
    @JvmStatic
    fun writeTag(entity: Entity, tag: NBTTagCompound) {
        val nmsEntity = asNMS(entity)
        val nms = NBTFactory.toNMS(tag)
        METHOD_ENTITY_READ_TAG.invoke(nmsEntity, nms)
    }

    /**
     * * Modify the NBT tag data for the given [entity].
     * * 将给定的实体 [entity] 进行 NBT 标签数据的修改.
     *
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    fun <T : Entity> modifyTag(entity: T, applicator: Applicator<NBTTagCompound>): T {
        val tag = readTag(entity)
        applicator(tag)
        writeTag(entity, tag)
        return entity
    }

    /**
     * * Get the item stack in main hand for the given [LivingEntity].
     * * 获取给定 [LivingEntity] 主手中的物品栈.
     */
    @JvmStatic
    fun getItemInHand(entity: LivingEntity): ItemStack? {
        return if (MinecraftBukkitVersion.isV19OrLater)
            getItemInMainHand(entity)
        else
            @Suppress("DEPRECATION")
            entity.equipment.itemInHand
    }

    /**
     * * Set the item stack in main hand for the given [LivingEntity].
     * * 设置给定 [LivingEntity] 主手中的物品栈.
     */
    @JvmStatic
    fun setItemInHand(entity: LivingEntity, stack: ItemStack?) {
        if (MinecraftBukkitVersion.isV19OrLater)
            setItemInMainHand(entity, stack)
        else
            @Suppress("DEPRECATION")
            entity.equipment.itemInHand = stack
    }

    /**
     * * Get the item stack in main hand for the given [LivingEntity].
     * * 获取给定 [LivingEntity] 主手中的物品栈.
     */
    @JvmStatic
    fun getItemInMainHand(entity: LivingEntity): ItemStack? {
        return if (MinecraftBukkitVersion.isV19OrLater)
            entity.equipment.itemInMainHand
        else
            getItemInHand(entity)
    }

    /**
     * * Set the item stack in main hand for the given [LivingEntity].
     * * 设置给定 [LivingEntity] 主手中的物品栈.
     */
    @JvmStatic
    fun setItemInMainHand(entity: LivingEntity, stack: ItemStack?) {
        if (MinecraftBukkitVersion.isV19OrLater)
            entity.equipment.itemInMainHand = stack
        else
            setItemInHand(entity, stack)
    }

    /**
     * * Get the item stack in off hand for the given [LivingEntity].
     * * 获取给定 [LivingEntity] 副手中的物品栈.
     */
    @JvmStatic
    fun getItemInOffHand(entity: LivingEntity): ItemStack? {
        return if (MinecraftBukkitVersion.isV19OrLater)
            entity.equipment.itemInOffHand
        else
            getItemInHand(entity)
    }

    /**
     * * Set the item stack in off hand for the given [LivingEntity].
     * * 设置给定 [LivingEntity] 副手中的物品栈.
     */
    @JvmStatic
    fun setItemInOffHand(entity: LivingEntity, stack: ItemStack?) {
        if (MinecraftBukkitVersion.isV19OrLater)
            entity.equipment.itemInOffHand = stack
        else
            setItemInHand(entity, stack)
    }
}
