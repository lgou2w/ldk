package com.lgou2w.ldk.bukkit.compatibility

import com.lgou2w.ldk.common.notNull
import org.bukkit.DyeColor

enum class DyeColors(private val pre113dyecolor: String, private val post113dyecolor: String) {

    LIGHT_GRAY("LIGHT_GRAY", "SILVER");

    private var cached: DyeColor? = null

    fun toBukkit(): DyeColor {
        return if (cached == null) {
            try {
                cached = DyeColor.valueOf(pre113dyecolor)
                cached as DyeColor
            } catch (ignore: IllegalArgumentException) {
                cached = DyeColor.valueOf(post113dyecolor)
                cached as DyeColor
            }
        } else {
            cached.notNull()
        }
    }
}
