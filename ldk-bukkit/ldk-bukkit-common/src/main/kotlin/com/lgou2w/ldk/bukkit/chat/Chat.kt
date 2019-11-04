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

package com.lgou2w.ldk.bukkit.chat

import com.lgou2w.ldk.chat.ChatAction
import com.lgou2w.ldk.chat.ChatComponent
import com.lgou2w.ldk.chat.ChatComponentFancy
import com.lgou2w.ldk.chat.ChatComponentText
import com.lgou2w.ldk.chat.ChatSerializer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * * Convert the given chat [component] to an implementation of `NMS`.
 * * 将给定的聊天组件 [component] 转换为 `NMS` 的实现.
 *
 * @see [ChatFactory.toNMS]
 */
fun ChatSerializer.toNMS(component: ChatComponent): Any
  = ChatFactory.toNMS(component)

/**
 * * Convert the given `NMS` chat object to an implementation of the [ChatComponent] wrapper.
 * * 将给定的 `NMS` 聊天对象转换为 [ChatComponent] 包装的实现.
 *
 * @throws [IllegalArgumentException] If the chat component object [icbc] is not the expected `NMS` instance.
 * @throws [IllegalArgumentException] 如果聊天组件对象 [icbc] 不是预期的 `NMS` 实例.
 * @see [ChatFactory.fromNMS]
 */
@Throws(IllegalArgumentException::class)
fun ChatSerializer.fromNMS(icbc: Any): ChatComponent
  = ChatFactory.fromNMS(icbc)

/**
 * * Sends the given chat component to the player with the given [action].
 * * 将给定的聊天组件以给定的交互 [action] 发送给玩家.
 *
 * @see [ChatFactory.sendChat]
 */
@JvmOverloads
fun ChatComponent.send(player: Player, action: ChatAction = ChatAction.CHAT)
  = ChatFactory.sendChat(player, this, action)

/**
 * * Sends the given chat component to the player with the given [action].
 * * 将给定的聊天组件以给定的交互 [action] 发送给玩家.
 *
 * @see [ChatFactory.sendChatTo]
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTo(players: Array<Player>, action: ChatAction = ChatAction.CHAT)
  = ChatFactory.sendChatTo(players, this, action)

/**
 * * Send the given chat component to all online players with the given [action].
 * * 将给定的聊天组件以给定的交互 [action] 发送给所有在线玩家.
 *
 * @see [ChatFactory.sendChatToAll]
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendToAll(action: ChatAction = ChatAction.CHAT)
  = ChatFactory.sendChatToAll(this, action)

/**
 * * Display the given item stack [itemStack] to the fancy chat component.
 * * 将给定的物品栈 [itemStack] 显示到花式聊天组件上.
 *
 * @see [ChatComponentFancy]
 * @see [ChatComponentFancy.tooltipItem]
 * @see [ChatFactory.tooltipItem]
 */
fun ChatComponentFancy.tooltipItem(itemStack: ItemStack): ChatComponentFancy
  = ChatFactory.tooltipItem(this, itemStack)

/**
 * * Send a title chat message to the given player [player].
 * * 将给定的玩家 [player] 发送标题聊天消息.
 *
 * @param [fadeIn] Fade in time tick
 * @param [fadeIn] 淡入时间刻
 * @param [stay] Stay time tick
 * @param [stay] 停留时间刻
 *  @param [fadeOut] Fade out time tick
 * @param [fadeOut] 淡出时间刻
 */
@JvmOverloads
fun ChatComponent.sendTitle(player: Player, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
  = ChatFactory.sendTitle(player, this, fadeIn, stay, fadeOut)

/**
 * * Send a title chat message to the given player [players].
 * * 将给定的玩家 [players] 发送标题聊天消息.
 *
 * @param [fadeIn] Fade in time tick
 * @param [fadeIn] 淡入时间刻
 * @param [stay] Stay time tick
 * @param [stay] 停留时间刻
 *  @param [fadeOut] Fade out time tick
 * @param [fadeOut] 淡出时间刻
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTitleTo(players: Array<Player>, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
  = ChatFactory.sendTitleTo(players, this, fadeIn, stay, fadeOut)

/**
 * * Send a title chat message to all online players.
 * * 将在线的所有玩家发送标题聊天消息.
 *
 * @param [fadeIn] Fade in time tick
 * @param [fadeIn] 淡入时间刻
 * @param [stay] Stay time tick
 * @param [stay] 停留时间刻
 *  @param [fadeOut] Fade out time tick
 * @param [fadeOut] 淡出时间刻
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTitleToAll(fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
  = ChatFactory.sendTitleToAll(this, fadeIn, stay, fadeOut)

/**
 * * Send a title chat message to the given player [player].
 * * 将给定的玩家 [player] 发送标题聊天消息.
 *
 * @param [subTitle] Sub title
 * @param [subTitle] 子标题
 * @param [fadeIn] Fade in time tick
 * @param [fadeIn] 淡入时间刻
 * @param [stay] Stay time tick
 * @param [stay] 停留时间刻
 *  @param [fadeOut] Fade out time tick
 * @param [fadeOut] 淡出时间刻
 */
@JvmOverloads
fun ChatComponent.sendTitle(player: Player, subTitle: ChatComponent?, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
  = ChatFactory.sendTitle(player, this, subTitle, fadeIn, stay, fadeOut)

/**
 * * Send a title chat message to the given player [players].
 * * 将给定的玩家 [players] 发送标题聊天消息.
 *
 * @param [subTitle] Sub title
 * @param [subTitle] 子标题
 * @param [fadeIn] Fade in time tick
 * @param [fadeIn] 淡入时间刻
 * @param [stay] Stay time tick
 * @param [stay] 停留时间刻
 *  @param [fadeOut] Fade out time tick
 * @param [fadeOut] 淡出时间刻
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTitleTo(players: Array<Player>, subTitle: ChatComponent?, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
  = ChatFactory.sendTitleTo(players, this, subTitle, fadeIn, stay, fadeOut)

/**
 * * Send a title chat message to all online players.
 * * 将在线的所有玩家发送标题聊天消息.
 *
 * @param [subTitle] Sub title
 * @param [subTitle] 子标题
 * @param [fadeIn] Fade in time tick
 * @param [fadeIn] 淡入时间刻
 * @param [stay] Stay time tick
 * @param [stay] 停留时间刻
 *  @param [fadeOut] Fade out time tick
 * @param [fadeOut] 淡出时间刻
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTitleToAll(subTitle: ChatComponent?, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
  = ChatFactory.sendTitleToAll(this, subTitle, fadeIn, stay, fadeOut)

/**
 * * Send a sub title chat message to the given player [player].
 * * 将给定的玩家 [player] 发送子标题聊天消息.
 *
 * @param [fadeIn] Fade in time tick
 * @param [fadeIn] 淡入时间刻
 * @param [stay] Stay time tick
 * @param [stay] 停留时间刻
 *  @param [fadeOut] Fade out time tick
 * @param [fadeOut] 淡出时间刻
 */
@JvmOverloads
fun ChatComponent.sendTitleSub(player: Player, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
  = ChatFactory.sendTitle(player, ChatComponentText(""), this, fadeIn, stay, fadeOut)

/**
 * * Send a sub title chat message to the given player [players].
 * * 将给定的玩家 [players] 发送子标题聊天消息.
 *
 * @param [fadeIn] Fade in time tick
 * @param [fadeIn] 淡入时间刻
 * @param [stay] Stay time tick
 * @param [stay] 停留时间刻
 *  @param [fadeOut] Fade out time tick
 * @param [fadeOut] 淡出时间刻
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTitleSubTo(players: Array<Player>, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
  = ChatFactory.sendTitleTo(players, ChatComponentText(""), this, fadeIn, stay, fadeOut)

/**
 * * Send a sub title chat message to all online players.
 * * 将在线的所有玩家发送子标题聊天消息.
 *
 * @param [fadeIn] Fade in time tick
 * @param [fadeIn] 淡入时间刻
 * @param [stay] Stay time tick
 * @param [stay] 停留时间刻
 *  @param [fadeOut] Fade out time tick
 * @param [fadeOut] 淡出时间刻
 * @since LDK 0.1.7-rc3
 */
@JvmOverloads
fun ChatComponent.sendTitleSubToAll(fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
  = ChatFactory.sendTitleToAll(ChatComponentText(""), this, fadeIn, stay, fadeOut)
