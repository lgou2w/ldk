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

import com.lgou2w.ldk.bukkit.attribute.AttributeItemModifier
import com.lgou2w.ldk.bukkit.attribute.AttributeType
import com.lgou2w.ldk.bukkit.attribute.Operation
import com.lgou2w.ldk.bukkit.attribute.Slot
import com.lgou2w.ldk.bukkit.potion.PotionBase
import com.lgou2w.ldk.bukkit.potion.PotionEffectCustom
import com.lgou2w.ldk.bukkit.potion.PotionEffectType
import com.lgou2w.ldk.chat.ChatComponent
import com.lgou2w.ldk.common.Builder
import com.lgou2w.ldk.common.Predicate
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

interface ItemBuilder : Builder<ItemStack> {

    val tag: NBTTagCompound

    //<editor-fold desc="ItemBuilder - Durability" defaultstate="collapsed">

    var durability : Int

    fun getDurability(block: (ItemBuilder, Int) -> Unit) : ItemBuilder

    fun setDurability(durability: Int) : ItemBuilder

    fun increaseDurability(durability: Int) : ItemBuilder

    fun decreaseDurability(durability: Int) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - DisplayName" defaultstate="collapsed">

    var displayName : ChatComponent?

    fun getDisplayName(block: (ItemBuilder, ChatComponent?) -> Unit) : ItemBuilder

    fun setDisplayName(displayName: ChatComponent?) : ItemBuilder

    fun removeDisplayName() : ItemBuilder

    fun removeDisplayName(predicate: Predicate<ChatComponent>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - LocalizedName" defaultstate="collapsed">

    var localizedName : ChatComponent?

    fun getLocalizedName(block: (ItemBuilder, ChatComponent?) -> Unit) : ItemBuilder

    fun setLocalizedName(localizedName: ChatComponent?) : ItemBuilder

    fun removeLocalizedName() : ItemBuilder

    fun removeLocalizedName(predicate: Predicate<ChatComponent>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Lore" defaultstate="collapsed">

    var lore : List<String>?

    fun getLore(block: (ItemBuilder, List<String>?) -> Unit) : ItemBuilder

    fun setLore(lore: List<String>?) : ItemBuilder

    fun clearLore() : ItemBuilder

    fun addLore(vararg lore: String) : ItemBuilder

    fun removeLore(predicate: Predicate<String>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Enchantment" defaultstate="collapsed">

    var enchantments : Map<Enchantment, Int>?

    fun getEnchantment(block: (ItemBuilder, Map<Enchantment, Int>?) -> Unit) : ItemBuilder

    fun setEnchantment(enchantments : Map<Enchantment, Int>?) : ItemBuilder

    fun clearEnchantment() : ItemBuilder

    fun addEnchantment(enchantment: Enchantment, level: Int) : ItemBuilder

    fun removeEnchantment(enchantment: Enchantment) : ItemBuilder

    fun removeEnchantment(predicate: Predicate<Pair<Enchantment, Int>>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - ItemFlag" defaultstate="collapsed">

    var flags : Array<out ItemFlag>?

    fun getFlag(block: (ItemBuilder, Array<out ItemFlag>?) -> Unit) : ItemBuilder

    fun setFlag(flags: Array<out ItemFlag>?) : ItemBuilder

    fun clearFlag() : ItemBuilder

    fun addFlag(vararg flags: ItemFlag) : ItemBuilder

    fun removeFlag(vararg flags: ItemFlag) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Unbreakable" defaultstate="collapsed">

    var isUnbreakable : Boolean

    fun isUnbreakable(block: (ItemBuilder, Boolean) -> Unit) : ItemBuilder

    fun setUnbreakable(unbreakable: Boolean) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Attribute" defaultstate="collapsed">

    var attributes : List<AttributeItemModifier>?

    fun getAttribute(block: (ItemBuilder, List<AttributeItemModifier>?) -> Unit) : ItemBuilder

    fun setAttribute(attributes: List<AttributeItemModifier>?) : ItemBuilder

    fun clearAttribute() : ItemBuilder

    fun addAttribute(attribute: AttributeItemModifier) : ItemBuilder

    fun addAttribute(type: AttributeType, operation: Operation, amount: Double, uuid: UUID = UUID.randomUUID()): ItemBuilder

    fun addAttribute(type: AttributeType, operation: Operation, slot: Slot?, amount: Double, uuid: UUID = UUID.randomUUID()): ItemBuilder

    fun addAttribute(type: AttributeType, name: String = type.value, operation: Operation, amount: Double, uuid: UUID = UUID.randomUUID()): ItemBuilder

    fun addAttribute(type: AttributeType, name: String = type.value, operation: Operation, slot: Slot?, amount: Double, uuid: UUID = UUID.randomUUID()): ItemBuilder

    fun removeAttribute(type: AttributeType) : ItemBuilder

    fun removeAttribute(predicate: Predicate<AttributeItemModifier>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - CanDestroy" defaultstate="collapsed">

    var canDestroy : List<Material>?

    fun getCanDestroy(block: (ItemBuilder, List<Material>?) -> Unit) : ItemBuilder

    fun setCanDestroy(types: List<Material>?) : ItemBuilder

    fun clearCanDestroy() : ItemBuilder

    fun addCanDestroy(vararg types: Material) : ItemBuilder

    fun removeCanDestroy(vararg types: Material) : ItemBuilder

    fun removeCanDestroy(predicate: Predicate<Material>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - CanPlaceOn" defaultstate="collapsed">

    var canPlaceOn : List<Material>?

    fun getCanPlaceOn(block: (ItemBuilder, List<Material>?) -> Unit) : ItemBuilder

    fun setCanPlaceOn(types: List<Material>?) : ItemBuilder

    fun clearCanPlaceOn() : ItemBuilder

    fun addCanPlaceOn(vararg types: Material) : ItemBuilder

    fun removeCanPlaceOn(vararg types: Material) : ItemBuilder

    fun removeCanPlaceOn(predicate: Predicate<Material>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - RepairCost" defaultstate="collapsed">

    var repairCost : Int?

    fun getRepairCost(block: (ItemBuilder, Int?) -> Unit) : ItemBuilder

    fun setRepairCost(repairCost: Int?) : ItemBuilder

    fun removeRepairCost() : ItemBuilder

    fun removeRepairCost(predicate: Predicate<Int>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - LeatherColor" defaultstate="collapsed">

    var leatherColor : Color?

    fun getLeatherColor(block: (ItemBuilder, Color?) -> Unit) : ItemBuilder

    fun setLeatherColor(leatherColor: Color?) : ItemBuilder

    fun removeLeatherColor() : ItemBuilder

    fun removeLeatherColor(predicate: Predicate<Color>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - BookTitle" defaultstate="collapsed">

    var bookTitle : String?

    fun getBookTitle(block: (ItemBuilder, String?) -> Unit) : ItemBuilder

    fun setBookTitle(title: String?) : ItemBuilder

    fun removeBookTitle() : ItemBuilder

    fun removeBookTitle(predicate: Predicate<String>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - BookAuthor" defaultstate="collapsed">

    var bookAuthor : String?

    fun getBookAuthor(block: (ItemBuilder, String?) -> Unit) : ItemBuilder

    fun setBookAuthor(author: String?) : ItemBuilder

    fun removeBookAuthor() : ItemBuilder

    fun removeBookAuthor(predicate: Predicate<String>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - BookGeneration" defaultstate="collapsed">

    var bookGeneration : Generation?

    fun getBookGeneration(block: (ItemBuilder, Generation?) -> Unit) : ItemBuilder

    fun setBookGeneration(generation: Generation) : ItemBuilder

    fun removeBookGeneration() : ItemBuilder

    fun removeBookGeneration(predicate: Predicate<Generation>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - BookPages" defaultstate="collapsed">

    var bookPages : List<ChatComponent>?

    fun getBookPages(block: (ItemBuilder, List<ChatComponent>?) -> Unit) : ItemBuilder

    fun getBookPage(index: Int, block: (ItemBuilder, ChatComponent?) -> Unit) : ItemBuilder

    fun getBookPage(index: Int) : ChatComponent?

    fun setBookPages(pages: List<ChatComponent>?) : ItemBuilder

    fun setBookPage(index: Int, page: ChatComponent) : ItemBuilder

    fun clearBookPages() : ItemBuilder

    fun addBookPage(vararg pages: ChatComponent) : ItemBuilder

    fun removeBookPage(predicate: Predicate<ChatComponent>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - StoredEnchantment" defaultstate="collapsed">

    var storedEnchantments : Map<Enchantment, Int>?

    fun getStoredEnchantments(block: (ItemBuilder, Map<Enchantment, Int>?) -> Unit) : ItemBuilder

    fun setStoredEnchantments(storedEnchantments: Map<Enchantment, Int>?) : ItemBuilder

    fun clearStoredEnchantments() : ItemBuilder

    fun addStoredEnchantment(enchantment: Enchantment, level: Int) : ItemBuilder

    fun removeStoredEnchantment(enchantment: Enchantment) : ItemBuilder

    fun removeStoredEnchantment(predicate: Predicate<Pair<Enchantment, Int>>?) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - SkullOwner" defaultstate="collapsed">

    var skullOwner : String?

    fun getSkullOwner(block: (ItemBuilder, String?) -> Unit) : ItemBuilder

    fun setSkullOwner(skullOwner: String?) : ItemBuilder

    fun removeSkullOwner() : ItemBuilder

    fun removeSkullOwner(predicate: Predicate<String>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - SkullOwner Value" defaultstate="collapsed">

    var skullOwnerValue : String?

    fun getSkullOwnerValue(block: (ItemBuilder, String?) -> Unit) : ItemBuilder

    fun setSkullOwnerValue(value: String?) : ItemBuilder

    fun setSkullOwnerValue(value: String, name: String?, id: UUID?) : ItemBuilder

    fun removeSkullOwnerValue() : ItemBuilder

    fun removeSkullOwnerValue(predicate: Predicate<String>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - PotionColor" defaultstate="collapsed">

    var potionColor : Color?

    fun getPotionColor(block: (ItemBuilder, Color?) -> Unit) : ItemBuilder

    fun setPotionColor(color: Color) : ItemBuilder

    fun removePotionColor() : ItemBuilder

    fun removePotionColor(predicate: Predicate<Color>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - PotionBase" defaultstate="collapsed">

    var potionBase : PotionBase?

    fun getPotionBase(block: (ItemBuilder, PotionBase?) -> Unit) : ItemBuilder

    fun setPotionBase(base: PotionBase?) : ItemBuilder

    fun removePotionBase() : ItemBuilder

    fun removePotionBase(predicate: Predicate<PotionBase>? = null) : ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - PotionCustom" defaultstate="collapsed">

    var potionCustoms : List<PotionEffectCustom>?

    fun getPotionCustoms(block: (ItemBuilder, List<PotionEffectCustom>?) -> Unit) : ItemBuilder

    fun getPotionCustom(type: PotionEffectType) : PotionEffectCustom?

    fun getPotionCustom(type: PotionEffectType, block: (ItemBuilder, PotionEffectCustom?) -> Unit) : ItemBuilder

    fun setPotionCustoms(customs: List<PotionEffectCustom>?) : ItemBuilder

    fun clearPotionCustoms() : ItemBuilder

    fun addPotionCustom(effect: PotionEffectCustom) : ItemBuilder

    fun addPotionCustom(effect: PotionEffectCustom, override: Boolean) : ItemBuilder

    fun removePotionCustom(type: PotionEffectType) : ItemBuilder

    fun removePotionCustom(predicate: Predicate<PotionEffectCustom>? = null) : ItemBuilder

    //</editor-fold>

    // TODO More...

    companion object {

        fun of(itemStack: ItemStack) : ItemBuilder
                = SimpleItemBuilder(itemStack)

        @JvmOverloads
        fun of(material: Material, count: Int = 1, durability: Int = 0) : ItemBuilder
                = SimpleItemBuilder(material, count, durability)

        fun <T: ItemBuilder> of(builder: T) : T {
            return builder
        }
    }
}
