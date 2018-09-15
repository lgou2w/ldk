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

package com.lgou2w.ldk.bukkit.cmd

import com.lgou2w.ldk.common.Function
import org.bukkit.Bukkit
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

object TypeTransforms {

    // Uncertain, so should force retention accuracy
    @JvmField val TRANSFORM_NUMBER : Function<String, Number?> = { it.toDoubleOrNull() }

    // Primitive
    @JvmField val TRANSFORM_BYTE : Function<String, Byte?> = { it.toByteOrNull() }
    @JvmField val TRANSFORM_SHORT : Function<String, Short?> = { it.toShortOrNull() }
    @JvmField val TRANSFORM_INT : Function<String, Int?> = { it.toIntOrNull() }
    @JvmField val TRANSFORM_LONG : Function<String, Long?> = { it.toLongOrNull() }
    @JvmField val TRANSFORM_FLOAT : Function<String, Float?> = { it.toFloatOrNull() }
    @JvmField val TRANSFORM_DOUBLE : Function<String, Double?> = { it.toDoubleOrNull() }
    @JvmField val TRANSFORM_BOOLEAN : Function<String, Boolean?> = {
        try {
            it.toBoolean()
        } catch (e: Exception) {
            null
        }
    }

    // Specially
    @JvmField val TRANSFORM_STRING : Function<String, String?> = { it }
    @JvmField val TRANSFORM_PLAYER : Function<String, Player?> = { Bukkit.getPlayer(it) }
    @JvmField val TRANSFORM_CONSOLE : Function<String, ConsoleCommandSender?> = { Bukkit.getConsoleSender() }

    @JvmStatic
    fun addDefaultTypeTransform(manager: CommandManager) {
        manager.addTypeTransform(Byte::class.java, TRANSFORM_BYTE)
        manager.addTypeTransform(Short::class.java, TRANSFORM_SHORT)
        manager.addTypeTransform(Int::class.java, TRANSFORM_INT)
        manager.addTypeTransform(Long::class.java, TRANSFORM_LONG)
        manager.addTypeTransform(Float::class.java, TRANSFORM_FLOAT)
        manager.addTypeTransform(Double::class.java, TRANSFORM_DOUBLE)
        manager.addTypeTransform(Number::class.java, TRANSFORM_NUMBER)
        manager.addTypeTransform(Boolean::class.java, TRANSFORM_BOOLEAN)
        manager.addTypeTransform(String::class.java, TRANSFORM_STRING)
        manager.addTypeTransform(Player::class.java, TRANSFORM_PLAYER)
        manager.addTypeTransform(ConsoleCommandSender::class.java, TRANSFORM_CONSOLE)
    }
}
