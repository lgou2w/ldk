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

import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.common.notNull
import com.lgou2w.ldk.nbt.NBT
import com.lgou2w.ldk.nbt.NBTTagCompound
import com.lgou2w.ldk.nbt.ofCompound
import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType

/**
 * @since LDK 0.1.8-rc
 */
data class IllegalPattern(
        val rawColor: Int,
        val rawType: String
) : Pattern(
        DyeColor.getByColor(Color.fromRGB(0x45, 0x52, 0x52)).notNull(), // ERR
        PatternType.BASE
) {
    private val msg = "The banner pattern is not compatible with the server. (rawColor=$rawColor, rawType=$rawType)"
    override fun getColor(): DyeColor = throw IllegalStateException(msg)
    override fun getPattern(): PatternType = throw IllegalStateException(msg)
}

/**
 * @since LDK 0.1.8-rc
 */
fun Pattern?.isIllegal(): Boolean = this != null && IllegalPattern::class.java.isInstance(this)

/**
 * @since LDK 0.1.8-rc
 */
fun Pattern.toCompound(): NBTTagCompound {
    @Suppress("DEPRECATION") // Notice if the function is removed
    val colorValue = if (MinecraftBukkitVersion.isV113OrLater) color.woolData else color.dyeData
    val compound = ofCompound {  }
    compound.putInt(NBT.TAG_BANNER_COLOR, colorValue.toInt())
    compound.putString(NBT.TAG_BANNER_PATTERN, pattern.identifier)
    return compound
}

/**
 * @since LDK 0.1.8-rc
 * @return [Pattern] or [IllegalPattern]
 */
fun NBTTagCompound.toBannerPattern(): Pattern {
    val rawColor = getInt(NBT.TAG_BANNER_COLOR)
    val rawType = getString(NBT.TAG_BANNER_PATTERN)
    val type : PatternType? = PatternType.getByIdentifier(rawType)
    @Suppress("DEPRECATION") // Notice if the function is removed
    val color : DyeColor? = if (MinecraftBukkitVersion.isV113OrLater) DyeColor.getByWoolData(rawColor.toByte())
    else DyeColor.getByDyeData(rawColor.toByte())
    return if (type == null || color == null) IllegalPattern(rawColor, rawType)
    else Pattern(color, type)
}
