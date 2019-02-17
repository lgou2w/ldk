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

package com.lgou2w.ldk.chat

/**
 * ## ChatComponentScore (聊天组件分数)
 *
 * @see [ChatComponent]
 * @see [ChatComponentAbstract]
 * @author lgou2w
 * @constructor ChatComponentScore
 * @param name Score name.
 * @param name 分数名.
 * @param objective Score Objective.
 * @param objective 分数目标.
 * @param value Score value.
 * @param value 分数值.
 */
class ChatComponentScore(
        /**
         * * Gets or sets the score name object for this chat component score.
         * * 获取或设置此聊天组件分数的分数名对象.
         */
        var name: String,
        /**
         * * Gets or sets the score objective object for this chat component score.
         * * 获取或设置此聊天组件分数的分数目标对象.
         */
        var objective: String,
        /**
         * * Gets or sets the score value object for this chat component score.
         * * 获取或设置此聊天组件分数的分数值对象.
         */
        var value: String? = null

) : ChatComponentAbstract() {

    /**
     * @see [ChatComponentScore.name]
     */
    fun setName(name: String): ChatComponentScore {
        this.name = name
        return this
    }

    /**
     * @see [ChatComponentScore.objective]
     */
    fun setObjective(objective: String): ChatComponentScore {
        this.objective = objective
        return this
    }

    /**
     * @see [ChatComponentScore.value]
     */
    fun setValue(value: String?): ChatComponentScore {
        this.value = value
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is ChatComponentScore)
            return super.equals(other) && name == other.name && objective == other.objective && value == other.value
        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + objective.hashCode()
        return result
    }

    override fun toString(): String {
        return "ChatComponentScore(name='$name', objective='$objective', value=$value, style=$style, extras=$extras)"
    }
}
