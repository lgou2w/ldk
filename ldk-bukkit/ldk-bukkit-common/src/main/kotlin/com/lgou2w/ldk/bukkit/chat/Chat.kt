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

package com.lgou2w.ldk.bukkit.chat

import com.lgou2w.ldk.chat.ChatAction
import com.lgou2w.ldk.chat.ChatComponent
import com.lgou2w.ldk.chat.ChatComponentFancy
import com.lgou2w.ldk.chat.ChatComponentText
import com.lgou2w.ldk.chat.ChatSerializer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun ChatSerializer.toNMS(component: ChatComponent) : Any
        = ChatFactory.toNMS(component)

fun ChatSerializer.fromNMS(icbc: Any) : ChatComponent
        = ChatFactory.fromNMS(icbc)

@JvmOverloads
fun ChatComponent.send(player: Player, action: ChatAction = ChatAction.CHAT)
        = ChatFactory.sendChat(player, this, action)

/**
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTo(players: Array<Player>, action: ChatAction = ChatAction.CHAT)
        = ChatFactory.sendChatTo(players, this, action)

/**
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendToAll(action: ChatAction = ChatAction.CHAT)
        = ChatFactory.sendChatToAll(this, action)

fun ChatComponentFancy.tooltipItem(itemStack: ItemStack) : ChatComponentFancy
        = ChatFactory.tooltipItem(this, itemStack)

@JvmOverloads
fun ChatComponent.sendTitle(player: Player, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
        = ChatFactory.sendTitle(player, this, fadeIn, stay, fadeOut)

/**
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTitleTo(players: Array<Player>, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
        = ChatFactory.sendTitleTo(players, this, fadeIn, stay, fadeOut)

/**
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTitleToAll(fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
        = ChatFactory.sendTitleToAll(this, fadeIn, stay, fadeOut)

@JvmOverloads
fun ChatComponent.sendTitle(player: Player, subTitle: ChatComponent?, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
        = ChatFactory.sendTitle(player, this, subTitle, fadeIn, stay, fadeOut)

/**
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTitleTo(players: Array<Player>, subTitle: ChatComponent?, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
        = ChatFactory.sendTitleTo(players, this, subTitle, fadeIn, stay, fadeOut)

/**
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTitleToAll(subTitle: ChatComponent?, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
        = ChatFactory.sendTitleToAll(this, subTitle, fadeIn, stay, fadeOut)

@JvmOverloads
fun ChatComponent.sendTitleSub(player: Player, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
        = ChatFactory.sendTitle(player, ChatComponentText(""), this, fadeIn, stay, fadeOut)

/**
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTitleSubTo(players: Array<Player>, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
        = ChatFactory.sendTitleTo(players, ChatComponentText(""), this, fadeIn, stay, fadeOut)

/**
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTitleSubToAll(fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
        = ChatFactory.sendTitleToAll(ChatComponentText(""), this, fadeIn, stay, fadeOut)
