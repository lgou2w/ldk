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

import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.lgou2w.ldk.common.Enums
import com.lgou2w.ldk.common.notNull
import java.io.IOException
import java.io.StringReader
import java.lang.reflect.Type
import java.util.regex.Pattern

/**
 * ## ChatSerializer (聊天序列化器)
 *
 * @see [ChatComponent]
 * @author lgou2w
 */
object ChatSerializer {

    @JvmStatic
    private val GSON: Gson

    init {
        GSON = GsonBuilder()
                .registerTypeHierarchyAdapter(ChatStyle::class.java, ChatStyleSerializer())
                .registerTypeHierarchyAdapter(ChatComponent::class.java, ChatComponentSerializer())
                .create()
    }

    /**
     * * Converts the given raw `JSON` message to a [ChatComponent] object.
     * * 将给定的源 `JSON` 消息转换为 [ChatComponent] 对象.
     *
     * @see [toJson]
     * @param json Raw `JSON` message.
     * @param json 源 `JSON` 消息.
     * @throws JsonParseException If parsing failed.
     * @throws JsonParseException 如果解析时失败.
     */
    @JvmStatic
    @Throws(JsonParseException::class)
    fun fromJson(json: String): ChatComponent = try {
        val jsonReader = JsonReader(StringReader(json))
        jsonReader.isLenient = false
        GSON.getAdapter(ChatComponent::class.java).read(jsonReader)
    } catch (e: IOException) {
        throw JsonParseException(e)
    }

    /**
     * * Converts the given raw `JSON` message to a [ChatComponent] object. If `null` then the result is `null`.
     * * 将给定的源 `JSON` 消息转换为 [ChatComponent] 对象. 如果 `null` 则结果为 `null`.
     *
     * @see [toJson]
     * @param json Raw `JSON` message.
     * @param json 源 `JSON` 消息.
     * @throws JsonParseException If parsing failed.
     * @throws JsonParseException 如果解析时失败.
     */
    @JvmStatic
    @Throws(JsonParseException::class)
    fun fromJsonOrNull(json: String?): ChatComponent?
            = if (json == null) null else fromJson(json)

    /**
     * * Converts the raw source `JSON` message to [ChatComponent] object in [JsonReader.lenient] lenient mode.
     * * 将给定的源 `JSON` 消息以 [JsonReader.lenient] 宽松模式转换为 [ChatComponent] 对象.
     *
     * @see [toJson]
     * @param json Raw `JSON` message.
     * @param json 源 `JSON` 消息.
     * @throws JsonParseException If parsing failed.
     * @throws JsonParseException 如果解析时失败.
     */
    @JvmStatic
    @Throws(JsonParseException::class)
    fun fromJsonLenient(json: String): ChatComponent = try {
        val jsonReader = JsonReader(StringReader(json))
        jsonReader.isLenient = true
        GSON.getAdapter(ChatComponent::class.java).read(jsonReader)
    } catch (e: IOException) {
        throw JsonParseException(e)
    }

    /**
     * * Converts the given source `JSON` message to a [ChatComponent] object. If the parsing exception tries the lenient mode.
     * * 将给定的源 `JSON` 消息转换为 [ChatComponent] 对象. 如果解析异常尝试宽松模式.
     *
     * @see [fromJson]
     * @see [fromJsonLenient]
     * @param json Raw `JSON` message.
     * @param json 源 `JSON` 消息.
     * @throws JsonParseException If parsing failed.
     * @throws JsonParseException 如果解析时失败.
     */
    @JvmStatic
    @Throws(JsonParseException::class)
    fun fromJsonOrLenient(json: String): ChatComponent = try {
        fromJson(json)
    } catch (e: JsonParseException) {
        fromJsonLenient(json)
    }

    /**
     * * Converts the given chat component to a `JSON` raw message.
     * * 将给定的聊天组件转换为 `JSON` 源消息.
     *
     * @see [fromJson]
     * @see [fromJsonLenient]
     * @param component Chat component.
     * @param component 聊天组件.
     */
    @JvmStatic
    fun toJson(component: ChatComponent): String
            = GSON.toJson(component)

    /**
     * * Converts the given raw string to [ChatComponent] object.
     * * 将给定的源字符串转换为 [ChatComponent] 对象.
     *
     * @see [toRaw]
     * @param raw Raw string.
     * @param raw 源字符串.
     */
    @JvmStatic
    fun fromRaw(raw: String?): ChatComponent {
        if (raw == null || raw.isEmpty())
            return ChatComponentText("")
        return RawMessage(raw.toColor()).get()
    }

    /**
     * * Converts the given raw string to [ChatComponent] object. If `null` then the result is `null`.
     * * 将给定的源字符串转换为 [ChatComponent] 对象. 如果 `null` 则结果为 `null`.
     *
     * @see [toRaw]
     * @param raw Raw string.
     * @param raw 源字符串.
     */
    @JvmStatic
    fun fromRawOrNull(raw: String?): ChatComponent?
            = if (raw == null) null else fromRaw(raw)

    private class RawMessage(val raw: String) {

        private val pattern = Pattern.compile("(§[0-9a-fk-or])", Pattern.CASE_INSENSITIVE)
        private var currentComponent: ChatComponent? = null
        private var style: ChatStyle? = null
        private var currentIndex: Int = 0

        init {
            val matcher = pattern.matcher(raw)
            var match: String? = null
            var groupId: Int
            while (matcher.find()) {
                groupId = 0
                do {
                    ++groupId
                } while (matcher.group(groupId).apply { match = this } == null)
                append(matcher.start(groupId))
                when (groupId) {
                    1 -> {
                        val color = Enums.ofValuable(ChatColor::class.java, match?.toLowerCase()?.get(1))
                                    ?: ChatColor.WHITE
                        when {
                            color == ChatColor.RESET -> style = ChatStyle()
                            color.isFormat -> when (color) {
                                ChatColor.OBFUSCATED -> style?.setObfuscated(true)
                                ChatColor.BOLD -> style?.setBold(true)
                                ChatColor.STRIKETHROUGH -> style?.setStrikethrough(true)
                                ChatColor.UNDERLINE -> style?.setUnderlined(true)
                                ChatColor.ITALIC -> style?.setItalic(true)
                                else -> throw AssertionError("Invalid color formatter: ${color.code}.")
                            }
                            else -> style = ChatStyle().setColor(color)
                        }
                    }
                }
                currentIndex = matcher.end(groupId)
            }
            if (currentIndex < raw.length)
                append(raw.length)
        }

        private fun append(index: Int) {
            if (index > currentIndex) {
                val extra = ChatComponentText(raw.substring(currentIndex, index)).setStyle(style)
                currentIndex = index
                style = style?.clone()
                if (currentComponent == null)
                    currentComponent = ChatComponentText("")
                currentComponent?.append(extra)
            }
        }

        internal fun get(): ChatComponent
                = currentComponent ?: ChatComponentText("")
    }

    /**
     * * Converts the given chat component to a raw string object.
     * * 将给定的聊天组件转换为源字符串对象.
     *
     * @see [fromRaw]
     * @see [fromRawOrNull]
     * @param component Chat component.
     * @param component 聊天组件.
     * @param color Whether it has a color.
     * @param color 是否拥有颜色.
     */
    @JvmStatic
    @JvmOverloads
    fun toRaw(component: ChatComponent, color: Boolean = true): String {
        val builder = StringBuilder()
        toRaw0(component, color, builder)
        return builder.toString()
    }

    @JvmStatic
    private fun toRaw0(component: ChatComponent, color: Boolean = true, builder: StringBuilder) {
        if (color) {
            val chatStyle = component.style
            if (chatStyle.color != null)
                appendColor(builder, chatStyle.color.notNull())
            if (chatStyle.bold != null)
                appendColor(builder, ChatColor.BOLD)
            if (chatStyle.italic != null)
                appendColor(builder, ChatColor.ITALIC)
            if (chatStyle.strikethrough != null)
                appendColor(builder, ChatColor.STRIKETHROUGH)
            if (chatStyle.underlined != null)
                appendColor(builder, ChatColor.UNDERLINE)
            if (chatStyle.obfuscated != null)
                appendColor(builder, ChatColor.OBFUSCATED)
        }
        if (component is ChatComponentText)
            builder.append(component.text)
        component.extras.forEach { toRaw0(it, color, builder) }
    }

    @JvmStatic
    private fun appendColor(builder: StringBuilder, color: ChatColor) {
        builder.append(color.toString())
    }

    private class ChatStyleSerializer : JsonDeserializer<ChatStyle>, JsonSerializer<ChatStyle> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ChatStyle? {
            if (json.isJsonObject) {
                val jsonObject: JsonObject = json.asJsonObject ?: return null
                val chatStyle = ChatStyle()
                if (jsonObject.has("color"))
                    chatStyle.color = Enums.ofName(ChatColor::class.java, jsonObject.get("color").asString.toUpperCase(), ChatColor.WHITE)
                if (jsonObject.has("bold"))
                    chatStyle.bold = jsonObject.get("bold").asBoolean
                if (jsonObject.has("italic"))
                    chatStyle.italic = jsonObject.get("italic").asBoolean
                if (jsonObject.has("underlined"))
                    chatStyle.underlined = jsonObject.get("underlined").asBoolean
                if (jsonObject.has("strikethrough"))
                    chatStyle.strikethrough = jsonObject.get("strikethrough").asBoolean
                if (jsonObject.has("obfuscated"))
                    chatStyle.obfuscated = jsonObject.get("obfuscated").asBoolean
                if (jsonObject.has("insertion"))
                    chatStyle.insertion = jsonObject.get("insertion").asString
                if (jsonObject.has("clickEvent")) {
                    val jsonObjectClickEvent = jsonObject.getAsJsonObject("clickEvent") ?: null
                    if (jsonObjectClickEvent != null) {
                        val action = Enums.ofName(ChatClickEvent.Action::class.java, jsonObjectClickEvent.get("action").asString.toUpperCase())
                        val value = jsonObjectClickEvent.get("value").asString ?: null
                        if (action != null && value != null)
                            chatStyle.clickEvent = ChatClickEvent(action, value)
                    }
                }
                if (jsonObject.has("hoverEvent")) {
                    val jsonObjectHoverEvent = jsonObject.getAsJsonObject("hoverEvent") ?: null
                    if (jsonObjectHoverEvent != null) {
                        val action = Enums.ofName(ChatHoverEvent.Action::class.java, jsonObjectHoverEvent.get("action").asString.toUpperCase())
                        val value = jsonObjectHoverEvent.get("value") ?: null
                        if (action != null && value != null)
                            chatStyle.hoverEvent = ChatHoverEvent(action, context.deserialize(value, ChatComponent::class.java))
                    }
                }
                return chatStyle
            }
            return null
        }

        override fun serialize(src: ChatStyle, typeOfSrc: Type, context: JsonSerializationContext): JsonElement? {
            if (src.isEmpty())
                return null
            val jsonObject = JsonObject()
            if (src.color != null)
                jsonObject.addProperty("color", src.color?.name?.toLowerCase())
            if (src.bold != null)
                jsonObject.addProperty("bold", src.bold)
            if (src.italic != null)
                jsonObject.addProperty("italic", src.italic)
            if (src.underlined != null)
                jsonObject.addProperty("underlined", src.underlined)
            if (src.strikethrough != null)
                jsonObject.addProperty("strikethrough", src.strikethrough)
            if (src.obfuscated != null)
                jsonObject.addProperty("obfuscated", src.obfuscated)
            if (src.insertion != null)
                jsonObject.add("insertion", context.serialize(src.insertion))
            if (src.clickEvent != null) {
                val jsonObjectClickEvent = JsonObject()
                jsonObjectClickEvent.addProperty("action", src.clickEvent?.action.toString().toLowerCase())
                jsonObjectClickEvent.addProperty("value", src.clickEvent?.value)
                jsonObject.add("clickEvent", jsonObjectClickEvent)
            }
            if (src.hoverEvent != null) {
                val jsonObjectHoverEvent = JsonObject()
                jsonObjectHoverEvent.addProperty("action", src.hoverEvent?.action.toString().toLowerCase())
                if (src.hoverEvent?.value is ChatComponentRaw)
                    jsonObjectHoverEvent.addProperty("value", (src.hoverEvent?.value as ChatComponentRaw).raw)
                else
                    jsonObjectHoverEvent.add("value", context.serialize(src.hoverEvent?.value))
                jsonObject.add("hoverEvent", jsonObjectHoverEvent)
            }
            return jsonObject
        }
    }

    private class ChatComponentSerializer : JsonDeserializer<ChatComponent>, JsonSerializer<ChatComponent> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ChatComponent? {
            if (json.isJsonPrimitive)
                return ChatComponentText(json.asString)
            if (!json.isJsonObject && json.isJsonArray) {
                var component: ChatComponent? = null
                val jsonArray = json.asJsonArray
                jsonArray.forEach {
                    val component1 = deserialize(it, it::class.java, context)
                    if (component == null)
                        component = component1
                    else if (component1 != null)
                        component?.append(component1)
                }
                return component
            }
            val component: ChatComponent?
            val jsonObject = json.asJsonObject
            if (jsonObject.has("text")) {
                component = ChatComponentText(jsonObject.get("text").asString)
            } else if (jsonObject.has("translate")) {
                val translate = jsonObject.get("translate").asString
                if (jsonObject.has("with")) {
                    val jsonArray = jsonObject.getAsJsonArray("with")
                    val withs = arrayOfNulls<Any>(jsonArray.size())
                    (0 until withs.size).forEach {
                        withs[it] = deserialize(jsonArray[it], typeOfT, context)
                        if (withs[it] is ChatComponentText) {
                            val componentText = withs[it] as ChatComponentText
                            if (componentText.style.isEmpty() && componentText.extras.isEmpty())
                                withs[it] = componentText.text
                        }
                    }
                    component = ChatComponentTranslation(translate).addWiths(withs.filterNotNull().toTypedArray())
                } else {
                    component = ChatComponentTranslation(translate)
                }
            } else if (jsonObject.has("score")) {
                val jsonObjectScore = jsonObject.getAsJsonObject("score")
                if (!jsonObjectScore.has("name") || !jsonObjectScore.has("objective"))
                    throw JsonParseException("The score chat component has at least one name or objective attribute.")
                component = ChatComponentScore(jsonObjectScore.get("name").asString, jsonObjectScore.get("objective").asString)
                if (jsonObjectScore.has("value"))
                    component.setValue(jsonObjectScore.get("value").asString)
            } else if (jsonObject.has("selector")) {
                component = ChatComponentSelector(jsonObject.get("selector").asString)
            } else if (jsonObject.has("keybind")) {
                component = ChatComponentKeybind(jsonObject.get("keybind").asString)
            } else {
                throw JsonParseException("Don't know how to parse $json into a chat component.")
            }
            if (jsonObject.has("extra")) {
                val jsonArray = jsonObject.getAsJsonArray("extra")
                if (jsonArray.size() <= 0)
                    throw JsonParseException("Invalid empty array component.")
                (0 until jsonArray.size()).forEach {
                    val component1 = deserialize(jsonArray[it], typeOfT, context)
                    if (component1 != null)
                        component.append(component1)
                }
            }
            component.setStyle(context.deserialize(json, ChatStyle::class.java))
            return component
        }

        override fun serialize(src: ChatComponent, typeOfSrc: Type, context: JsonSerializationContext): JsonElement? {
            val jsonObject = JsonObject()
            if (!src.style.isEmpty()) {
                val jsonElement = context.serialize(src.style)
                if (jsonElement.isJsonObject) {
                    val jsonObjectStyle = jsonElement.asJsonObject
                    jsonObjectStyle.entrySet().forEach { jsonObject.add(it.key, it.value) }
                }
            }
            if (!src.extras.isEmpty()) {
                val jsonArray = JsonArray()
                src.extras.forEach { jsonArray.add(serialize(it, it::class.java, context)) }
                jsonObject.add("extra", jsonArray)
            }
            if (src is ChatComponentText) {
                jsonObject.addProperty("text", src.text)
            } else if (src is ChatComponentTranslation) {
                jsonObject.addProperty("translate", src.key)
                if (!src.withs.isEmpty()) {
                    val jsonArray = JsonArray()
                    src.withs.forEach {
                        if (it is ChatComponent)
                            jsonArray.add(serialize(it, it::class.java, context))
                        else
                            jsonArray.add(JsonPrimitive(it.toString()))
                    }
                    jsonObject.add("with", jsonArray)
                }
            } else if (src is ChatComponentScore) {
                val jsonObjectScore = JsonObject()
                jsonObjectScore.addProperty("name", src.name)
                jsonObjectScore.addProperty("objective", src.objective)
                if (src.value != null) jsonObjectScore.addProperty("value", src.value)
                jsonObject.add("score", jsonObjectScore)
            } else if (src is ChatComponentSelector) {
                jsonObject.addProperty("selector", src.selector)
            } else if (src is ChatComponentKeybind) {
                jsonObject.addProperty("keybind", src.keybind)
            } else {
                throw JsonParseException("Don't know how to parse $src into a chat component.")
            }
            return jsonObject
        }
    }

    /**
     * 仅用在 Tooltip ItemStack 上
     */
    internal data class ChatComponentRaw(var raw: String) : ChatComponentAbstract()
}
