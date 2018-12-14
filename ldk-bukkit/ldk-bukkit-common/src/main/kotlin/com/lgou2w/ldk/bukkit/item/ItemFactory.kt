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

package com.lgou2w.ldk.bukkit.item

import com.lgou2w.ldk.bukkit.nbt.NBTFactory
import com.lgou2w.ldk.bukkit.reflect.MinecraftReflection
import com.lgou2w.ldk.bukkit.reflect.lazyCraftBukkitClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClassOrNull
import com.lgou2w.ldk.bukkit.version.API
import com.lgou2w.ldk.bukkit.version.Level
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.common.isOrLater
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
import java.util.*

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
                .withVisibilities(Visibility.PRIVATE)
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

//    // NMS.Item -> public static int getId(NMS.Item)
//    @JvmStatic val METHOD_ITEM_GET_ID : AccessorMethod<Any, Int> by lazy {
//        FuzzyReflect.of(CLASS_ITEM, true)
//            .useMethodMatcher()
//            .withVisibilities(Visibility.PUBLIC, Visibility.STATIC)
//            .withName("getId")
//            .withType(Int::class.java)
//            .withParams(CLASS_ITEM)
//            .resultAccessorAs<Any, Int>()
//    }
//
//    // NMS.Item -> public static NMS.Item getById(int)
//    @JvmStatic val METHOD_ITEM_GET_BY_ID : AccessorMethod<Any, Any> by lazy {
//        FuzzyReflect.of(CLASS_ITEM, true)
//            .useMethodMatcher()
//            .withVisibilities(Visibility.PUBLIC, Visibility.STATIC)
//            .withName("getById")
//            .withType(CLASS_ITEM)
//            .withParams(Int::class.java)
//            .resultAccessor()
//    }

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

//    @JvmStatic
//    @Deprecated("Magic Number")
//    private fun getItemId(item: Any?) : Int {
//        if (item == null) return 0
//        return METHOD_ITEM_GET_ID.invoke(null, item) ?: 0
//    }
//
//    @JvmStatic
//    @Deprecated("Magic Number")
//    private fun getItemById(id: Int) : Any? {
//        return METHOD_ITEM_GET_BY_ID.invoke(null, id)
//    }

    @JvmStatic
    fun asNMSCopy(stack: ItemStack?) : Any? {
        return METHOD_AS_NMSCOPY.invoke(null, stack)
    }

    @JvmStatic
    fun asCraftMirror(origin: Any?) : ItemStack? {
        MinecraftReflection.isExpected(origin, CLASS_ITEMSTACK)
        return METHOD_AS_CRAFTMIRROR.invoke(null, origin) as? ItemStack
    }

    @JvmStatic
    fun asBukkitCopy(origin: Any?) : ItemStack? {
        MinecraftReflection.isExpected(origin, CLASS_ITEMSTACK)
        return METHOD_AS_BUKKITCOPY.invoke(null, origin)
    }

    @JvmStatic
    fun readTagSafe(itemStack: ItemStack): NBTTagCompound {
        return readTag(itemStack) ?: ofCompound(NBT.TAG)
    }

    @JvmStatic
    fun readTag(itemStack: ItemStack) : NBTTagCompound? {
        return if (CLASS_CRAFT_ITEMSTACK.isInstance(itemStack)) {
            val nmsStack = FIELD_CRAFT_ITEMSTACK_HANDLE[itemStack]
            val nmsTag = FIELD_ITEMSTACK_TAG[nmsStack] ?: return null
            NBTFactory.fromNMS(nmsTag) as? NBTTagCompound
        } else {
            val nmsStack = asNMSCopy(itemStack)
            val obcStack = asCraftMirror(nmsStack) as ItemStack
            obcStack.itemMeta = itemStack.itemMeta
            readTag(obcStack)
        }
    }

    @JvmStatic
    fun materialType(material: Material) : String {
        return if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
            METHOD_MAGIC_NUMBERS_KEY
                .notNull("OBC.CraftMagicNumbers#key(Material)")
                .invoke(null, material)
                .toString()
        } else {
            "minecraft:${material.name.toLowerCase(Locale.US)}"
        }
    }

    @JvmStatic
    fun readItem(itemStack: ItemStack) : NBTTagCompound {
        val root = ofCompound {  }
        if (MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1)) {
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
            root.putShort(NBT.TAG_DAMAGE, damage)
            root.putString(NBT.TAG_ID, "minecraft:$id")
            root.putByte(NBT.TAG_COUNT, count)
            val tag = readTag(itemStack)
            if (tag != null)
                root[NBT.TAG] = tag
        }
        return root
    }

    @JvmStatic
    fun writeTag(itemStack: ItemStack, tag: NBTTagCompound?): ItemStack {
        if (CLASS_CRAFT_ITEMSTACK.isInstance(itemStack)) {
            val nmsStack = FIELD_CRAFT_ITEMSTACK_HANDLE[itemStack]
            FIELD_ITEMSTACK_TAG[nmsStack] = if (tag == null) null else NBTFactory.toNMS(tag)
        } else {
            if (tag == null) {
                itemStack.itemMeta = null
            } else {
                val nmsStack = asNMSCopy(itemStack)
                val obcStack = asCraftMirror(nmsStack) as ItemStack
                writeTag(obcStack, tag)
                itemStack.itemMeta = obcStack.itemMeta
            }
        }
        return itemStack
    }
}
