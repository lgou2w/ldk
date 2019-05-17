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

package com.lgou2w.ldk.bukkit.item

import com.lgou2w.ldk.bukkit.compatibility.XMaterial
import com.lgou2w.ldk.bukkit.nbt.NBTFactory
import com.lgou2w.ldk.bukkit.reflect.MinecraftReflection
import com.lgou2w.ldk.bukkit.reflect.lazyCraftBukkitClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClassOrNull
import com.lgou2w.ldk.bukkit.version.API
import com.lgou2w.ldk.bukkit.version.Level
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.common.Applicator
import com.lgou2w.ldk.common.notNull
import com.lgou2w.ldk.nbt.NBT
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.NBTType
import com.lgou2w.ldk.nbt.ofCompound
import com.lgou2w.ldk.reflect.AccessorField
import com.lgou2w.ldk.reflect.AccessorMethod
import com.lgou2w.ldk.reflect.FuzzyReflect
import com.lgou2w.ldk.reflect.Visibility
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.Locale

/**
 * ## ItemFactory (物品工厂)
 *
 * @author lgou2w
 */
object ItemFactory {

    @JvmStatic val CLASS_ITEM by lazyMinecraftClass("Item")
    @JvmStatic val CLASS_ITEMSTACK by lazyMinecraftClass("ItemStack")
    @JvmStatic val CLASS_CRAFT_ITEMSTACK by lazyCraftBukkitClass("inventory.CraftItemStack")

    @API(Level.Minecraft_V1_13)
    @JvmStatic val CLASS_MINECRAFT_KEY by lazyMinecraftClassOrNull("MinecraftKey")
    @API(Level.Minecraft_V1_13)
    @JvmStatic val CLASS_CRAFT_MAGIC_NUMBERS by lazyCraftBukkitClass("util.CraftMagicNumbers")

    // NMS.ItemStack -> private NMS.NBTTagCompound tag
    @JvmStatic val FIELD_ITEMSTACK_TAG: AccessorField<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_ITEMSTACK, true)
                .useFieldMatcher()
//                .withVisibilities(Visibility.PRIVATE) // SEE : https://github.com/lgou2w/ldk/issues/83
                .withName("tag")
                .withType(NBTFactory.CLASS_NBT_TAG_COMPOUND)
                .resultAccessor()
    }

    // OBC.inventory.CraftItemStack -> NMS.ItemStack handle
    @JvmStatic val FIELD_CRAFT_ITEMSTACK_HANDLE: AccessorField<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_CRAFT_ITEMSTACK, true)
                .useFieldMatcher()
                .withName("handle")
                .withType(CLASS_ITEMSTACK)
                .resultAccessor()
    }

    // OBC.inventory.CraftItemStack -> public static NMS.ItemStack asNMSCopy(OB.inventory.ItemStack)
    @JvmStatic val METHOD_AS_NMSCOPY: AccessorMethod<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_CRAFT_ITEMSTACK, true)
                .useMethodMatcher()
                .withVisibilities(Visibility.PUBLIC, Visibility.STATIC)
                .withName("asNMSCopy")
                .withType(CLASS_ITEMSTACK)
                .withParams(ItemStack::class.java)
                .resultAccessor()
    }

    // OBC.inventory.CraftItemStack -> public static OB.inventory.ItemStack asBukkitCopy(NMS.ItemStack)
    @JvmStatic val METHOD_AS_BUKKITCOPY: AccessorMethod<Any, ItemStack> by lazy {
        FuzzyReflect.of(CLASS_CRAFT_ITEMSTACK, true)
                .useMethodMatcher()
                .withVisibilities(Visibility.PUBLIC, Visibility.STATIC)
                .withName("asBukkitCopy")
                .withType(ItemStack::class.java)
                .withParams(CLASS_ITEMSTACK)
                .resultAccessorAs<Any, ItemStack>()
    }

    // OBC.inventory.CraftItemStack -> public static OBC.inventory.CraftItemStack asCraftMirror(NMS.ItemStack)
    @JvmStatic val METHOD_AS_CRAFTMIRROR: AccessorMethod<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_CRAFT_ITEMSTACK, true)
                .useMethodMatcher()
                .withVisibilities(Visibility.PUBLIC, Visibility.STATIC)
                .withName("asCraftMirror")
                .withType(CLASS_CRAFT_ITEMSTACK)
                .withParams(CLASS_ITEMSTACK)
                .resultAccessor()
    }

    // OBC.CraftMagicNumbers -> public static NMS.MinecraftKey key(Material)
    @API(Level.Minecraft_V1_13)
    @JvmStatic val METHOD_MAGIC_NUMBERS_KEY : AccessorMethod<Any, Any>? by lazy {
        FuzzyReflect.of(CLASS_CRAFT_MAGIC_NUMBERS, true)
            .useMethodMatcher()
            .withVisibilities(Visibility.PUBLIC, Visibility.STATIC)
            .withType(CLASS_MINECRAFT_KEY.notNull("The 1.13 version doesn't have the MinecraftKey class ???"))
            .withParams(Material::class.java)
            .resultAccessorOrNull()
    }

    // NMS.ItemStack -> public NMS.NBTTagCompound save(NMS.NBTTagCompound)
    @JvmStatic val METHOD_ITEMSTACK_SAVE : AccessorMethod<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_ITEMSTACK)
            .useMethodMatcher()
            .withVisibilities(Visibility.PUBLIC)
            .withType(NBTFactory.CLASS_NBT_TAG_COMPOUND)
            .withParams(NBTFactory.CLASS_NBT_TAG_COMPOUND)
            .withName("save")
            .resultAccessor()
    }

    /**
     * * Converts the given item [stack] to an instance object of `NMS`.
     * * 将给定的物品栈 [stack] 转换为  `NMS` 的实例对象.
     */
    @JvmStatic
    fun asNMSCopy(stack: ItemStack?): Any? {
        return METHOD_AS_NMSCOPY.invoke(null, stack)
    }

    /**
     * * Convert the given `NMS` item stack [origin] to the `CraftItemStack` object.
     * * 将给定 `NMS` 物品栈 [origin] 转换为 `CraftItemStack` 对象.
     *
     * @throws [IllegalArgumentException] If the item stack object [origin] is not the expected `NMS` instance.
     * @throws [IllegalArgumentException] 如果物品栈对象 [origin] 不是预期的 `NMS` 实例.
     */
    @JvmStatic
    fun asCraftMirror(origin: Any?): ItemStack? {
        MinecraftReflection.isExpected(origin, CLASS_ITEMSTACK)
        return METHOD_AS_CRAFTMIRROR.invoke(null, origin) as? ItemStack
    }

    /**
     * * Convert the given `NMS` item stack [origin] to the [ItemStack] object.
     * * 将给定 `NMS` 物品栈 [origin] 转换为 [ItemStack] 对象.
     *
     * @throws [IllegalArgumentException] If the item stack object [origin] is not the expected `NMS` instance.
     * @throws [IllegalArgumentException] 如果物品栈对象 [origin] 不是预期的 `NMS` 实例.
     */
    @JvmStatic
    fun asBukkitCopy(origin: Any?): ItemStack? {
        MinecraftReflection.isExpected(origin, CLASS_ITEMSTACK)
        return METHOD_AS_BUKKITCOPY.invoke(null, origin)
    }

    /**
     * * Safely read NBT tag data from a given [itemStack].
     * * 从给定的物品栈 [itemStack] 安全的读取 NBT 标签数据.
     *
     * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
     * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
     * @see [readTag]
     */
    @JvmStatic
    @Throws(UnsupportedOperationException::class)
    fun readTagSafe(itemStack: ItemStack): NBTTagCompound
            = readTag(itemStack, itemStack.type) ?: ofCompound(NBT.TAG)

    /**
     * * Read NBT tag data from a given [itemStack].
     * * 从给定的物品栈 [itemStack] 读取 NBT 标签数据.
     *
     * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
     * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
     * @see [readTagSafe]
     */
    @JvmStatic
    @Throws(UnsupportedOperationException::class)
    fun readTag(itemStack: ItemStack): NBTTagCompound?
            = readTag(itemStack, itemStack.type)

    @JvmStatic
    @Throws(UnsupportedOperationException::class)
    private fun readTag(itemStack: ItemStack, rawType: Material): NBTTagCompound? {
        return if (CLASS_CRAFT_ITEMSTACK.isInstance(itemStack)) {
            val nmsStack = FIELD_CRAFT_ITEMSTACK_HANDLE[itemStack]
                           ?: throw UnsupportedOperationException(
                                   "Illegal item type '$rawType' that does not supported.")
            val nmsTag = FIELD_ITEMSTACK_TAG[nmsStack] ?: return null
            NBTFactory.fromNMS(nmsTag) as? NBTTagCompound
        } else {
            val nmsStack = asNMSCopy(itemStack)
            val obcStack = asCraftMirror(nmsStack) as ItemStack
            obcStack.itemMeta = itemStack.itemMeta
            readTag(obcStack, itemStack.type)
        }
    }

    /**
     * * Get the Minecraft standard type name from the given [material] type.
     * * 从给定的材料类型 [material] 获取 Minecraft 标准类型名.
     */
    @JvmStatic
    fun materialType(material: Material): String {
        return if (MinecraftBukkitVersion.isV113OrLater) {
            METHOD_MAGIC_NUMBERS_KEY
                .notNull("OBC.CraftMagicNumbers#key(Material)")
                .invoke(null, material)
                .toString()
        } else {
            "minecraft:${material.name.toLowerCase(Locale.US)}"
        }
    }

    /**
     * * Read NBT data from the given item stack [itemStack].
     * * 从给定的物品栈 [itemStack] 读取 NBT 数据.
     *
     * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
     * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
     */
    @JvmStatic
    @Throws(UnsupportedOperationException::class)
    fun readItem(itemStack: ItemStack): NBTTagCompound {
        val root = ofCompound {  }
        if (MinecraftBukkitVersion.isV113OrLater) {
            // After version 1.13
            // 1.13 版本之后
            try {
                // Get by OBC CraftLegacy
                // 从 OBC 的 CraftLegacy 获取
                val id = materialType(itemStack.type)
                val count = itemStack.amount
                root.putString(NBT.TAG_ID, id)
                root.putByte(NBT.TAG_COUNT, count)
                val tag = readTag(itemStack)
                if (tag != null)
                    root[NBT.TAG] = tag
            } catch (e: NullPointerException) {
                // Get by ItemStack.save(NBTTagCompound)
                // 从 ItemStack.save(NBTTagCompound) 获取
                val nmsStack = asNMSCopy(itemStack)
                val handle = NBTFactory.createInternal(NBTType.TAG_COMPOUND)
                METHOD_ITEMSTACK_SAVE.invoke(nmsStack, handle)
                return NBTFactory.fromNMS(handle) as NBTTagCompound
            }
        } else {
            // Before version 1.13
            // 1.13 版本之前
            val id = itemStack.type.name.toLowerCase(Locale.US)
            val count = itemStack.amount
            @Suppress("DEPRECATION")
            val damage = itemStack.durability   // do not worry about it, 不用担心弃用
            root.putString(NBT.TAG_ID, "minecraft:$id")
            root.putByte(NBT.TAG_COUNT, count)
            if (damage > 0) // SEE -> https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/commits/c3749a
                root.putShort(NBT.TAG_DAMAGE, damage)
            val tag = readTag(itemStack)
            if (tag != null)
                root[NBT.TAG] = tag
        }
        return root
    }

    /**
     * * Create item stack object from the given NBT data [root].
     * * 从给定的 NBT 数据 [root] 创建物品栈对象.
     *
     * @throws [UnsupportedOperationException] If the item material is illegal.
     * @throws [UnsupportedOperationException] 如果物品材料是非法的.
     * @since LDK 0.1.8-rc
     */
    @JvmStatic
    @Throws(UnsupportedOperationException::class)
    fun createItem(root: NBTTagCompound): ItemStack {
        val id = root.getStringOrNull(NBT.TAG_ID)?.replaceFirst("minecraft:", "") // e.g.: minecraft:diamond -> diamond
                 ?: throw UnsupportedOperationException("Illegal item nbt.")
        val count = root.getByteOrNull(NBT.TAG_COUNT) ?: 1
        val tag = root.getCompoundOrNull(NBT.TAG)
        val type = if (MinecraftBukkitVersion.isV113OrLater) {
            // After version 1.13
            // 1.13 版本之后
            XMaterial.searchByType(id) ?: throw UnsupportedOperationException("Invalid item id: $id")
        } else {
            // Before version 1.13
            // 1.13 版本之前
            val damage = root.getShortOrNull(NBT.TAG_DAMAGE) ?: 0 // if not existed
            XMaterial.searchByType("$id:$damage") ?: throw UnsupportedOperationException("Invalid item: $id:$damage")
        }.toBukkit()
        return ItemStack(type, count.toInt()).apply {
            if (tag != null)
                writeTag(this, tag)
        }
    }

    /**
     * * Write the given NBT tag data [tag] to the given item stack [itemStack].
     * * 将给定的 NBT 标签数据 [tag] 写入到给定的物品栈 [itemStack] 中.
     *
     * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
     * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
     */
    @JvmStatic
    @Throws(UnsupportedOperationException::class)
    fun writeTag(itemStack: ItemStack, tag: NBTTagCompound?): ItemStack
            = writeTag(itemStack, tag, itemStack.type)

    @JvmStatic
    @Throws(UnsupportedOperationException::class)
    private fun writeTag(itemStack: ItemStack, tag: NBTTagCompound?, rawType: Material): ItemStack {
        if (CLASS_CRAFT_ITEMSTACK.isInstance(itemStack)) {
            val nmsStack = FIELD_CRAFT_ITEMSTACK_HANDLE[itemStack]
                           ?: throw UnsupportedOperationException(
                                   "Illegal item type '$rawType' that does not supported.")
            FIELD_ITEMSTACK_TAG[nmsStack] = if (tag == null) null else NBTFactory.toNMS(tag)
        } else {
            if (tag == null) {
                itemStack.itemMeta = null
            } else {
                val nmsStack = asNMSCopy(itemStack)
                val obcStack = asCraftMirror(nmsStack) as ItemStack
                writeTag(obcStack, tag, itemStack.type)
                itemStack.itemMeta = obcStack.itemMeta
            }
        }
        return itemStack
    }

    /**
     * * Modify the NBT tag data for the given item stack [itemStack].
     * * 将给定的物品栈 [itemStack] 进行 NBT 标签数据的修改.
     *
     * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
     * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    @Throws(UnsupportedOperationException::class)
    fun modifyTag(itemStack: ItemStack, applicator: Applicator<NBTTagCompound>): ItemStack {
        val tag = readTagSafe(itemStack)
        applicator(tag)
        writeTag(itemStack, tag)
        return itemStack
    }
}
