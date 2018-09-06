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
import com.lgou2w.ldk.bukkit.reflect.lazyCraftBukkitClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClass
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.common.isOrLater
import com.lgou2w.ldk.nbt.NBT
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.ofCompound
import com.lgou2w.ldk.reflect.AccessorField
import com.lgou2w.ldk.reflect.AccessorMethod
import com.lgou2w.ldk.reflect.FuzzyReflect
import com.lgou2w.ldk.reflect.Visibility
import org.bukkit.inventory.ItemStack
import java.util.*

object ItemFactory {

    @JvmStatic val CLASS_ITEM by lazyMinecraftClass("Item")
    @JvmStatic val CLASS_ITEMSTACK by lazyMinecraftClass("ItemStack")
    @JvmStatic val CLASS_CRAFT_ITEMSTACK by lazyCraftBukkitClass("inventory.CraftItemStack")

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

    fun readTagSafe(itemStack: ItemStack): NBTTagCompound {
        return readTag(itemStack) ?: ofCompound(NBT.TAG)
    }

    fun readTag(itemStack: ItemStack): NBTTagCompound? {
        return if (CLASS_CRAFT_ITEMSTACK.isInstance(itemStack)) {
            val nmsStack = FIELD_CRAFT_ITEMSTACK_HANDLE[itemStack]
            val nmsTag = FIELD_ITEMSTACK_TAG[nmsStack]
            NBTFactory.fromNMS(nmsTag) as? NBTTagCompound
        } else {
            val nmsStack = METHOD_AS_NMSCOPY.invoke(null, itemStack)
            val obcStack = METHOD_AS_CRAFTMIRROR.invoke(null, nmsStack) as ItemStack
            obcStack.itemMeta = itemStack.itemMeta
            readTag(obcStack)
        }
    }

    fun readItem(itemStack: ItemStack) : NBTTagCompound {
        val tag = readTag(itemStack)
        val root = ofCompound {  }
        val id = itemStack.type.name.toLowerCase(Locale.US) // TODO
        val count = itemStack.amount
        val damage = itemStack.durability
        root.putString(NBT.TAG_ID, "minecraft:$id")
        root.putByte(NBT.TAG_COUNT, count)
        if (!MinecraftBukkitVersion.CURRENT.isOrLater(MinecraftBukkitVersion.V1_13_R1))
            root.putShort(NBT.TAG_DAMAGE, damage)
        else
            tag?.putShort(NBT.TAG_DAMAGE, damage)
        if (tag != null)
            root[NBT.TAG] = tag
        return root
    }

    fun writeTag(itemStack: ItemStack, tag: NBTTagCompound?): ItemStack {
        if (CLASS_CRAFT_ITEMSTACK.isInstance(itemStack)) {
            val nmsStack = FIELD_CRAFT_ITEMSTACK_HANDLE[itemStack]
            FIELD_ITEMSTACK_TAG[nmsStack] = NBTFactory.toNMS(tag)
        } else {
            if (tag == null) {
                itemStack.itemMeta = null
            } else {
                val nmsStack = METHOD_AS_NMSCOPY.invoke(null, itemStack)
                val obcStack = METHOD_AS_CRAFTMIRROR.invoke(null, nmsStack) as ItemStack
                writeTag(obcStack, tag)
                itemStack.itemMeta = obcStack.itemMeta
            }
        }
        return itemStack
    }
}
