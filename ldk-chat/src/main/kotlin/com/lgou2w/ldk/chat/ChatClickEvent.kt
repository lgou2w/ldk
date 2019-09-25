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

package com.lgou2w.ldk.chat

/**
 * ## ChatClickEvent (聊天点击事件)
 *
 * @see [ChatComponent]
 * @see [ChatStyle]
 * @see [ChatStyle.setClickEvent]
 * @author lgou2w
 * @param action Click action type.
 * @param action 点击交互类型.
 * @param value Action value.
 * @param value 交互值.
 */
data class ChatClickEvent(
  /**
   * * The action type of this chat click event.
   * * 此聊天点击事件的交互类型.
   *
   * @see [Action]
   */
  val action: Action,
  /**
   * * The action value of this chat click event.
   * * 此聊天点击事件的交互值.
   */
  val value: String
) {

  /**
   * ## Action (交互类型)
   *
   * @see [ChatClickEvent]
   * @author lgou2w
   */
  enum class Action {

    /**
     * * Chat Click Type: Open URL
     * * 聊天点击类型: 打开链接
     */
    OPEN_URL,
    /**
     * * Chat Click Type: Open File
     * * 聊天点击类型: 打开文件
     *
     * > * Opens a link to any protocol, but cannot be used in `JSON` chat for security reasons.
     *      Only exists to internally implement links for screenshots.
     *
     * > * 打开指向任何协议的链接, 但出于安全原因, 不能在 `JSON` 聊天中使用. 仅存在内部实现屏幕截图的链接.
     */
    OPEN_FILE,
    /**
     * * Chat Click Type: Suggest Command
     * * 聊天点击类型: 提示命令
     */
    SUGGEST_COMMAND,
    /**
     * * Chat Click Type: Run Command
     * * 聊天点击类型: 执行命令
     */
    RUN_COMMAND,
    /**
     * * Chat Click Type: Change Page
     * * 聊天点击类型: 改变页面
     */
    CHANGE_PAGE,
    ;
  }
}
