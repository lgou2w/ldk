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

package com.lgou2w.ldk.bukkit.chat

import com.google.gson.Gson
import com.lgou2w.ldk.bukkit.item.ItemFactory
import com.lgou2w.ldk.bukkit.packet.PacketFactory
import com.lgou2w.ldk.bukkit.reflect.MinecraftReflection
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClassOrNull
import com.lgou2w.ldk.chat.ChatAction
import com.lgou2w.ldk.chat.ChatComponent
import com.lgou2w.ldk.chat.ChatComponentFancy
import com.lgou2w.ldk.chat.ChatSerializer
import com.lgou2w.ldk.common.Enums
import com.lgou2w.ldk.reflect.AccessorConstructor
import com.lgou2w.ldk.reflect.AccessorField
import com.lgou2w.ldk.reflect.FuzzyReflect
import com.lgou2w.ldk.reflect.Visibility
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object ChatFactory {

    @JvmStatic val CLASS_ICHAT_BASE_COMPONENT by lazyMinecraftClass("IChatBaseComponent")
    @JvmStatic val CLASS_CHAT_SERIALIZER by lazyMinecraftClass("IChatBaseComponent\$ChatSerializer", "ChatSerializer")

    @JvmStatic private val CLASS_PACKET_OUT_CHAT by lazyMinecraftClass("PacketPlayOutChat")
    @JvmStatic private val CLASS_CHAT_MESSAGE_TYPE by lazyMinecraftClassOrNull("ChatMessageType")

    // NMS.PacketPlayOutChat -> public constructor(NMS.IChatBaseComponent, ChatMessageType | Byte)
    @JvmStatic private val CONSTRUCTOR_PACKET_OUT_CHAT : AccessorConstructor<Any> by lazy {
        FuzzyReflect.of(CLASS_PACKET_OUT_CHAT, true)
            .useConstructorMatcher()
            .withParams(CLASS_ICHAT_BASE_COMPONENT, CLASS_CHAT_MESSAGE_TYPE ?: Byte::class.java)
            .resultAccessor()
    }

    // NMS.ChatSerializer -> public final static Gson GSON
    @JvmStatic val FIELD_CHAT_SERIALIZER_GSON : AccessorField<Any, Gson> by lazy {
        FuzzyReflect.of(CLASS_CHAT_SERIALIZER, true)
            .useFieldMatcher()
            .withVisibilities(Visibility.STATIC)
            .withType(Gson::class.java)
            .resultAccessorAs<Any, Gson>()
    }

    @JvmStatic
    fun toNMS(component: ChatComponent): Any {
        val gson = FIELD_CHAT_SERIALIZER_GSON[null]!!
        val json = component.toJson()
        return gson.fromJson<Any>(json, CLASS_ICHAT_BASE_COMPONENT)
    }

    @JvmStatic
    fun fromNMS(icbc: Any): ChatComponent {
        MinecraftReflection.isExpected(icbc, CLASS_ICHAT_BASE_COMPONENT)
        val gson = FIELD_CHAT_SERIALIZER_GSON[null]!!
        val json = gson.toJson(icbc, CLASS_ICHAT_BASE_COMPONENT)
        return ChatSerializer.fromJsonOrLenient(json)
    }

    @JvmStatic
    @JvmOverloads
    fun sendToPlayer(player: Player, component: ChatComponent, action: ChatAction = ChatAction.CHAT) {
        val value : Any? =
                if (CLASS_CHAT_MESSAGE_TYPE != null) Enums.fromOrigin(CLASS_CHAT_MESSAGE_TYPE!!, action.ordinal)
                else action.id
        val icbc = toNMS(component)
        val packet = CONSTRUCTOR_PACKET_OUT_CHAT.newInstance(icbc, value)
        PacketFactory.sendPacket(player, packet)
    }

    @JvmStatic
    fun tooltipItem(fancy: ChatComponentFancy, itemStack: ItemStack) : ChatComponentFancy {
        val mojangson = ItemFactory.readItem(itemStack).toMojangson()
        return fancy.tooltipItem(mojangson)
    }
}
