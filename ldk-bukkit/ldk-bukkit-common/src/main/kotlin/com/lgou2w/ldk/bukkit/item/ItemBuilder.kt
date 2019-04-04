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

import com.lgou2w.ldk.bukkit.attribute.AttributeItemModifier
import com.lgou2w.ldk.bukkit.attribute.AttributeType
import com.lgou2w.ldk.bukkit.attribute.Operation
import com.lgou2w.ldk.bukkit.attribute.Slot
import com.lgou2w.ldk.bukkit.firework.FireworkEffect
import com.lgou2w.ldk.bukkit.firework.FireworkType
import com.lgou2w.ldk.bukkit.potion.PotionBase
import com.lgou2w.ldk.bukkit.potion.PotionEffectCustom
import com.lgou2w.ldk.bukkit.potion.PotionEffectType
import com.lgou2w.ldk.chat.ChatComponent
import com.lgou2w.ldk.common.ApplicatorFunction
import com.lgou2w.ldk.common.BiFunction
import com.lgou2w.ldk.common.Builder
import com.lgou2w.ldk.common.Predicate
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.UUID

/**
 * ## ItemBuilder (物品构建者)
 *
 * @see [Builder]
 * @see [ItemBuilderBase]
 * @see [SimpleItemBuilder]
 * @see [of]
 * @author lgou2w
 */
interface ItemBuilder : Builder<ItemStack> {

    val tag : NBTTagCompound

    /**
     * * The material type of this item stack.
     * * 此物品栈的材料类型.
     *
     * @see [org.bukkit.Material]
     * @see [org.bukkit.inventory.ItemStack.type]
     */
    val material : Material

    /**
     * * Maximum durability of this item stack material type.
     * * 此物品栈材料类型的最大耐久度.
     *
     * @see [org.bukkit.Material.getMaxDurability]
     */
    val maxDurability : Int

    /**
     * * Maximum stack size of this item stack material type.
     * * 此物品栈材料类型的最大堆叠大小.
     *
     * @see [org.bukkit.Material.getMaxStackSize]
     */
    val maxStackSize : Int

    /**
     * * Rebuilds an [ItemBuilder] object with the given material type data. using the current material type data if not specified.
     * * 以给定的材料类型数据重建一个 [ItemBuilder] 对象, 如果不指定则使用当前材料类型数据.
     */
    fun reBuilder(material: Material = this.material, count: Int = this.count, durability: Int = this.durability): ItemBuilder

    /**
     * @see [reBuilder]
     */
    fun reBuilder(material: Material = this.material, count: Int = this.count): ItemBuilder

    /**
     * @see [reBuilder]
     */
    fun reBuilder(material: Material = this.material): ItemBuilder

    /**
     * @see [reBuilder]
     */
    fun reBuilder(count: Int = this.count): ItemBuilder

    /**
     * @see [reBuilder]
     */
    fun reBuilder(count: Int = this.count, durability: Int = this.durability): ItemBuilder

    //<editor-fold desc="ItemBuilder - Generic" defaultstate="collapsed">

    /**
     * * The count of this item stack.
     * * 此物品栈的数量.
     */
    var count : Int

    fun getCount(block: (ItemBuilder, Int) -> Unit): ItemBuilder

    fun setCount(count: Int): ItemBuilder

    fun setCountIf(count: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun increaseCount(count: Int): ItemBuilder

    fun increaseCountIf(count: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun decreaseCount(count: Int): ItemBuilder

    fun decreaseCountIf(count: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Durability" defaultstate="collapsed">

    var durability : Int

    fun getDurability(block: (ItemBuilder, Int) -> Unit): ItemBuilder

    fun setDurability(durability: Int): ItemBuilder

    fun setDurabilityIf(durability: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun increaseDurability(durability: Int): ItemBuilder

    fun increaseDurabilityIf(durability: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun decreaseDurability(durability: Int): ItemBuilder

    fun decreaseDurabilityIf(durability: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - DisplayName" defaultstate="collapsed">

    var displayName : ChatComponent?

    fun getDisplayName(block: (ItemBuilder, ChatComponent?) -> Unit): ItemBuilder

    fun setDisplayName(displayName: String?): ItemBuilder

    fun setDisplayNameIf(displayName: String?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun setDisplayName(displayName: ChatComponent?): ItemBuilder

    fun setDisplayNameIf(displayName: ChatComponent?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeDisplayName(): ItemBuilder

    fun removeDisplayName(predicate: Predicate<ChatComponent>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - LocalizedName" defaultstate="collapsed">

    var localizedName : ChatComponent?

    fun getLocalizedName(block: (ItemBuilder, ChatComponent?) -> Unit): ItemBuilder

    fun setLocalizedName(localizedName: ChatComponent?): ItemBuilder

    fun setLocalizedNameIf(localizedName: ChatComponent?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeLocalizedName(): ItemBuilder

    fun removeLocalizedName(predicate: Predicate<ChatComponent>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Lore" defaultstate="collapsed">

    var lore : List<String>?

    fun getLore(block: (ItemBuilder, List<String>?) -> Unit): ItemBuilder

    fun setLore(lore: List<String>?): ItemBuilder

    fun setLoreIf(lore: List<String>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun clearLore(): ItemBuilder

    fun addLore(vararg lore: String): ItemBuilder

    fun addLoreIf(vararg lore: String, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeLore(predicate: Predicate<String>? = null): ItemBuilder

    fun removeLoreIndexed(block: BiFunction<Int, String, Boolean>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Enchantment" defaultstate="collapsed">

    var enchantments : Map<Enchantment, Int>?

    fun getEnchantment(block: (ItemBuilder, Map<Enchantment, Int>?) -> Unit): ItemBuilder

    fun setEnchantment(enchantments : Map<Enchantment, Int>?): ItemBuilder

    fun setEnchantmentIf(enchantments: Map<Enchantment, Int>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun clearEnchantment(): ItemBuilder

    fun addEnchantment(enchantment: Pair<Enchantment, Int>): ItemBuilder

    fun addEnchantment(enchantment: Enchantment, level: Int): ItemBuilder

    fun addEnchantmentIf(enchantment: Pair<Enchantment, Int>, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun addEnchantmentIf(enchantment: Enchantment, level: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeEnchantment(enchantment: Enchantment): ItemBuilder

    fun removeEnchantment(predicate: Predicate<Pair<Enchantment, Int>>? = null): ItemBuilder

    fun removeEnchantmentIndexed(block: BiFunction<Int, Pair<Enchantment, Int>, Boolean>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - ItemFlag" defaultstate="collapsed">

    var flags : Array<out ItemFlag>?

    fun getFlag(block: (ItemBuilder, Array<out ItemFlag>?) -> Unit): ItemBuilder

    fun setFlag(flags: Array<out ItemFlag>?): ItemBuilder

    fun setFlagIf(flags: Array<out ItemFlag>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun clearFlag(): ItemBuilder

    fun addFlag(vararg flags: ItemFlag): ItemBuilder

    fun addFlagIf(vararg flags: ItemFlag, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeFlag(vararg flags: ItemFlag): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Unbreakable" defaultstate="collapsed">

    var isUnbreakable : Boolean

    fun isUnbreakable(block: (ItemBuilder, Boolean) -> Unit): ItemBuilder

    fun setUnbreakable(unbreakable: Boolean): ItemBuilder

    fun setUnbreakableIf(unbreakable: Boolean, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Attribute" defaultstate="collapsed">

    var attributes : List<AttributeItemModifier>?

    fun getAttribute(block: (ItemBuilder, List<AttributeItemModifier>?) -> Unit): ItemBuilder

    fun setAttribute(attributes: List<AttributeItemModifier>?): ItemBuilder

    fun setAttributeIf(attributes: List<AttributeItemModifier>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun clearAttribute(): ItemBuilder

    fun addAttribute(attribute: AttributeItemModifier): ItemBuilder

    fun addAttributeIf(attribute: AttributeItemModifier, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun addAttribute(type: AttributeType, operation: Operation, amount: Double, uuid: UUID = UUID.randomUUID()): ItemBuilder

    fun addAttribute(type: AttributeType, operation: Operation, slot: Slot?, amount: Double, uuid: UUID = UUID.randomUUID()): ItemBuilder

    fun addAttribute(type: AttributeType, name: String = type.value, operation: Operation, amount: Double, uuid: UUID = UUID.randomUUID()): ItemBuilder

    fun addAttribute(type: AttributeType, name: String = type.value, operation: Operation, slot: Slot?, amount: Double, uuid: UUID = UUID.randomUUID()): ItemBuilder

    fun removeAttribute(type: AttributeType): ItemBuilder

    fun removeAttribute(predicate: Predicate<AttributeItemModifier>? = null): ItemBuilder

    fun removeAttributeIndexed(block: BiFunction<Int, AttributeItemModifier, Boolean>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - CanDestroy" defaultstate="collapsed">

    var canDestroy : List<Material>?

    fun getCanDestroy(block: (ItemBuilder, List<Material>?) -> Unit): ItemBuilder

    fun setCanDestroy(types: List<Material>?): ItemBuilder

    fun setCanDestroyIf(types: List<Material>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun clearCanDestroy(): ItemBuilder

    fun addCanDestroy(vararg types: Material): ItemBuilder

    fun addCanDestroyIf(vararg types: Material, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeCanDestroy(vararg types: Material): ItemBuilder

    fun removeCanDestroy(predicate: Predicate<Material>? = null): ItemBuilder

    fun removeCanDestroyIndexed(block: BiFunction<Int, Material, Boolean>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - CanPlaceOn" defaultstate="collapsed">

    var canPlaceOn : List<Material>?

    fun getCanPlaceOn(block: (ItemBuilder, List<Material>?) -> Unit): ItemBuilder

    fun setCanPlaceOn(types: List<Material>?): ItemBuilder

    fun setCanPlaceOnIf(types: List<Material>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun clearCanPlaceOn(): ItemBuilder

    fun addCanPlaceOn(vararg types: Material): ItemBuilder

    fun addCanPlaceOnIf(vararg types: Material, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeCanPlaceOn(vararg types: Material): ItemBuilder

    fun removeCanPlaceOn(predicate: Predicate<Material>? = null): ItemBuilder

    fun removeCanPlaceOnIndexed(block: BiFunction<Int, Material, Boolean>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - RepairCost" defaultstate="collapsed">

    var repairCost : Int?

    fun getRepairCost(block: (ItemBuilder, Int?) -> Unit): ItemBuilder

    fun setRepairCost(repairCost: Int?): ItemBuilder

    fun setRepairCostIf(repairCost: Int?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeRepairCost(): ItemBuilder

    fun removeRepairCost(predicate: Predicate<Int>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - LeatherColor" defaultstate="collapsed">

    var leatherColor : Color?

    fun getLeatherColor(block: (ItemBuilder, Color?) -> Unit): ItemBuilder

    fun setLeatherColor(leatherColor: Color?): ItemBuilder

    fun setLeatherColorIf(leatherColor: Color?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeLeatherColor(): ItemBuilder

    fun removeLeatherColor(predicate: Predicate<Color>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - BookTitle" defaultstate="collapsed">

    var bookTitle : String?

    fun getBookTitle(block: (ItemBuilder, String?) -> Unit): ItemBuilder

    fun setBookTitle(title: String?): ItemBuilder

    fun setBookTitleIf(title: String?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeBookTitle(): ItemBuilder

    fun removeBookTitle(predicate: Predicate<String>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - BookAuthor" defaultstate="collapsed">

    var bookAuthor : String?

    fun getBookAuthor(block: (ItemBuilder, String?) -> Unit): ItemBuilder

    fun setBookAuthor(author: String?): ItemBuilder

    fun setBookAuthorIf(author: String?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeBookAuthor(): ItemBuilder

    fun removeBookAuthor(predicate: Predicate<String>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - BookGeneration" defaultstate="collapsed">

    var bookGeneration : Generation?

    fun getBookGeneration(block: (ItemBuilder, Generation?) -> Unit): ItemBuilder

    fun setBookGeneration(generation: Generation): ItemBuilder

    fun setBookGenerationIf(generation: Generation, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeBookGeneration(): ItemBuilder

    fun removeBookGeneration(predicate: Predicate<Generation>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - BookPages" defaultstate="collapsed">

    var bookPages : List<ChatComponent>?

    fun getBookPages(block: (ItemBuilder, List<ChatComponent>?) -> Unit): ItemBuilder

    fun getBookPage(index: Int, block: (ItemBuilder, ChatComponent?) -> Unit): ItemBuilder

    fun getBookPage(index: Int) : ChatComponent?

    fun setBookPages(pages: List<ChatComponent>?): ItemBuilder

    fun setBookPagesIf(pages: List<ChatComponent>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun setBookPage(index: Int, page: ChatComponent): ItemBuilder

    fun setBookPageIf(index: Int, page: ChatComponent, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun clearBookPages(): ItemBuilder

    fun addBookPage(vararg pages: ChatComponent): ItemBuilder

    fun addBookPage(vararg pages: ChatComponent, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeBookPage(predicate: Predicate<ChatComponent>? = null): ItemBuilder

    fun removeBookPageIndexed(block: BiFunction<Int, ChatComponent, Boolean>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - StoredEnchantment" defaultstate="collapsed">

    var storedEnchantments : Map<Enchantment, Int>?

    fun getStoredEnchantments(block: (ItemBuilder, Map<Enchantment, Int>?) -> Unit): ItemBuilder

    fun setStoredEnchantments(storedEnchantments: Map<Enchantment, Int>?): ItemBuilder

    fun setStoredEnchantmentsIf(storedEnchantments: Map<Enchantment, Int>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun clearStoredEnchantments(): ItemBuilder

    fun addStoredEnchantment(enchantment: Pair<Enchantment, Int>): ItemBuilder

    fun addStoredEnchantmentIf(enchantment: Pair<Enchantment, Int>, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun addStoredEnchantment(enchantment: Enchantment, level: Int): ItemBuilder

    fun addStoredEnchantmentIf(enchantment: Enchantment, level: Int, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeStoredEnchantment(enchantment: Enchantment): ItemBuilder

    fun removeStoredEnchantment(predicate: Predicate<Pair<Enchantment, Int>>? = null): ItemBuilder

    fun removeStoredEnchantmentIndexed(block: BiFunction<Int, Pair<Enchantment, Int>, Boolean>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - SkullOwner" defaultstate="collapsed">

    var skullOwner : String?

    fun getSkullOwner(block: (ItemBuilder, String?) -> Unit): ItemBuilder

    fun setSkullOwner(skullOwner: String?): ItemBuilder

    fun setSkullOwnerIf(skullOwner: String?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeSkullOwner(): ItemBuilder

    fun removeSkullOwner(predicate: Predicate<String>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - SkullOwner Value" defaultstate="collapsed">

    var skullOwnerValue : String?

    fun getSkullOwnerValue(block: (ItemBuilder, String?) -> Unit): ItemBuilder

    fun setSkullOwnerValue(value: String?): ItemBuilder

    fun setSkullOwnerValueIf(value: String?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun setSkullOwnerValue(value: String, name: String?, id: UUID?): ItemBuilder

    fun setSkullOwnerValue(value: String, name: String?, id: UUID?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeSkullOwnerValue(): ItemBuilder

    fun removeSkullOwnerValue(predicate: Predicate<String>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - PotionColor" defaultstate="collapsed">

    var potionColor : Color?

    fun getPotionColor(block: (ItemBuilder, Color?) -> Unit): ItemBuilder

    fun setPotionColor(color: Color): ItemBuilder

    fun setPotionColorIf(color: Color, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removePotionColor(): ItemBuilder

    fun removePotionColor(predicate: Predicate<Color>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - PotionBase" defaultstate="collapsed">

    var potionBase : PotionBase?

    fun getPotionBase(block: (ItemBuilder, PotionBase?) -> Unit): ItemBuilder

    fun setPotionBase(base: PotionBase?): ItemBuilder

    fun setPotionBaseIf(base: PotionBase?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removePotionBase(): ItemBuilder

    fun removePotionBase(predicate: Predicate<PotionBase>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - PotionCustom" defaultstate="collapsed">

    var potionCustoms : List<PotionEffectCustom>?

    fun getPotionCustoms(block: (ItemBuilder, List<PotionEffectCustom>?) -> Unit): ItemBuilder

    fun getPotionCustom(type: PotionEffectType) : PotionEffectCustom?

    fun getPotionCustom(type: PotionEffectType, block: (ItemBuilder, PotionEffectCustom?) -> Unit): ItemBuilder

    fun setPotionCustoms(customs: List<PotionEffectCustom>?): ItemBuilder

    fun setPotionCustoms(customs: List<PotionEffectCustom>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun clearPotionCustoms(): ItemBuilder

    fun addPotionCustom(effect: PotionEffectCustom): ItemBuilder

    fun addPotionCustom(effect: PotionEffectCustom, override: Boolean): ItemBuilder

    fun addPotionCustomIf(effect: PotionEffectCustom, override: Boolean = false, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removePotionCustom(type: PotionEffectType): ItemBuilder

    fun removePotionCustom(predicate: Predicate<PotionEffectCustom>? = null): ItemBuilder

    fun removePotionCustomIndexed(block: BiFunction<Int, PotionEffectCustom, Boolean>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - FireworkStar" defaultstate="collapsed">

    var fireworkStar : FireworkEffect?

    fun getFireworkStar(block: (ItemBuilder, FireworkEffect?) -> Unit): ItemBuilder

    fun setFireworkStar(effect: FireworkEffect?): ItemBuilder

    fun setFireworkStar(effect: FireworkEffect?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeFireworkStar(): ItemBuilder

    fun removeFireworkStar(predicate: Predicate<FireworkEffect>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - FireworkRocket Effect" defaultstate="collapsed">

    var fireworkRocketEffects : List<FireworkEffect>?

    fun getFireworkRocketEffects(block: (ItemBuilder, List<FireworkEffect>?) -> Unit): ItemBuilder

    fun getFireworkRocketEffect(type: FireworkType) : FireworkEffect?

    fun getFireworkRocketEffect(type: FireworkType, block: (ItemBuilder, FireworkEffect?) -> Unit): ItemBuilder

    fun setFireworkRocketEffects(effect: List<FireworkEffect>?): ItemBuilder

    fun setFireworkRocketEffectsIf(effect: List<FireworkEffect>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun clearFireworkRocketEffects(): ItemBuilder

    fun addFireworkRocketEffect(effect: FireworkEffect): ItemBuilder

    fun addFireworkRocketEffectIf(effect: FireworkEffect, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeFireworkRocketEffect(type: FireworkType): ItemBuilder

    fun removeFireworkRocketEffect(predicate: Predicate<FireworkEffect>? = null): ItemBuilder

    fun removeFireworkRocketEffectIndexed(block: BiFunction<Int, FireworkEffect, Boolean>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - FireworkRocket Flight" defaultstate="collapsed">

    var fireworkRocketFlight : Int?

    fun getFireworkRocketFlight(block: (ItemBuilder, Int?) -> Unit): ItemBuilder

    fun setFireworkRocketFlight(flight: Int?): ItemBuilder

    fun setFireworkRocketFlightIf(flight: Int?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    fun removeFireworkRocketFlight(): ItemBuilder

    fun removeFireworkRocketFlight(predicate: Predicate<Int>? = null): ItemBuilder

    //</editor-fold>

    //<editor-fold desc="ItemBuilder - Banner Pattern" defaultstate="collapsed">

    /**
     * @since LDK 0.1.8-rc
     */
    var bannerPatterns : List<Pattern>?

    /**
     * @since LDK 0.1.8-rc
     */
    fun getBannerPattern(block: (ItemBuilder, List<Pattern>?) -> Unit): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun setBannerPattern(patterns: List<Pattern>?): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun setBannerPatternIf(patterns: List<Pattern>?, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun clearBannerPatten(): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun addBannerPattern(vararg patterns: Pattern): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun addBannerPattern(color: DyeColor, type: PatternType): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun addBannerPattern(pattern: Pair<DyeColor, PatternType>): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun addBannerPattern(vararg patterns: Pair<DyeColor, PatternType>): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun addBannerPatternIf(pattern: Pattern, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun addBannerPatternIf(color: DyeColor, type: PatternType, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun addBannerPatternIf(pattern: Pair<DyeColor, PatternType>, block: ApplicatorFunction<ItemBuilder, Boolean?>): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun removeBannerPattern(vararg patterns: Pattern): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun removeBannerPattern(vararg patterns: Pair<DyeColor, PatternType>): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun removeBannerPattern(predicate: Predicate<Pattern>? = null): ItemBuilder

    /**
     * @since LDK 0.1.8-rc
     */
    fun removeBannerPatternIndexed(block: BiFunction<Int, Pattern, Boolean>? = null): ItemBuilder

    //</editor-fold>

    companion object {

        /**
         * * Create an Item Builder [ItemBuilder] object from the given item stack.
         * * 从给定的物品栈创建一个物品构建者 [ItemBuilder] 对象.
         *
         * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
         * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
         */
        @JvmStatic
        @Throws(UnsupportedOperationException::class)
        fun of(itemStack: ItemStack): ItemBuilder
                = SimpleItemBuilder(itemStack)

        /**
         * * Create an Item Builder [ItemBuilder] object from the given item material.
         * * 从给定的物品材料创建一个物品构建者 [ItemBuilder] 对象.
         *
         * @throws [UnsupportedOperationException] If the item material is illegal. e.g.: `WALL_BANNER`.
         * @throws [UnsupportedOperationException] 如果物品材料是非法的. 例如: `WALL_BANNER`.
         */
        @JvmStatic
        @Throws(UnsupportedOperationException::class)
        fun of(material: Material, count: Int = 1, durability: Int = 0): ItemBuilder
                = SimpleItemBuilder(material, count, durability)

        @JvmStatic
        fun <T : ItemBuilder> of(builder: T): T {
            return builder
        }
    }
}
