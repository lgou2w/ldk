/*
 * Copyright (C) 2016-2020 The lgou2w <lgou2w@hotmail.com>
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

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.stream.JsonReader
import com.lgou2w.ldk.common.Enums
import com.lgou2w.ldk.common.isTrue
import com.lgou2w.ldk.common.notNull
import java.io.IOException
import java.io.StringReader
import java.lang.reflect.Type
import java.util.Collections
import java.util.regex.Pattern

/**
 * ## ChatSerializer (聊天序列化器)
 *
 * @see [ChatComponent]
 * @author lgou2w
 */
object ChatSerializer {

  @JvmStatic
  private val GSON : Gson

  /**
   * RGB hex color is a feature of minecraft 1.16, this function can convert
   * the color rgb that supports the old version to a unified color type name
   *
   * e.g.: #f55 | #ff5555 -> red
   */
  @JvmStatic
  private val RGB_FORMATTING_TABLE : Map<Int, String>

  init {
    GSON = GsonBuilder()
      .registerTypeHierarchyAdapter(ChatStyle::class.java, ChatStyleSerializer())
      .registerTypeHierarchyAdapter(ChatComponent::class.java, ChatComponentSerializer())
      .create()
    RGB_FORMATTING_TABLE = Collections.unmodifiableMap(
      ChatColor
        .values()
        .filter { it.rgb != null }
        .associate { it.rgb!! to it.name.toLowerCase() })
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

    companion object {
      private val PATTERN = Pattern.compile("(§[0-9a-fk-or])", Pattern.CASE_INSENSITIVE)
    }

    private var currentComponent : ChatComponent? = null
    private var style : ChatStyle = ChatStyle()
    private var currentIndex : Int = 0

    init {
      val matcher = PATTERN.matcher(raw)
      while (matcher.find()) {
        val match : String = matcher.group(1)
        append(matcher.start(1))
        val code = match.notNull().toLowerCase()[1].toString()
        val color = Color.ofSafely(code)
        style = when {
          ChatColor.RESET == color -> ChatStyle()
          ChatColor.BOLD == color -> style.setBold(true)
          ChatColor.ITALIC == color -> style.setItalic(true)
          ChatColor.UNDERLINE == color -> style.setUnderlined(true)
          ChatColor.STRIKETHROUGH == color -> style.setStrikethrough(true)
          ChatColor.OBFUSCATED == color -> style.setObfuscated(true)
          else -> style.setColor(color)
        }
        currentIndex = matcher.end(1)
      }
      if (currentIndex < raw.length)
        append(raw.length)
    }

    private fun append(index: Int) {
      if (index > currentIndex) {
        val extra = ChatComponentText(raw.substring(currentIndex, index))
        extra.setStyle(style)
        currentIndex = index
        style = style.clone()
        if (currentComponent == null)
          currentComponent = ChatComponentText("")
        currentComponent.notNull().append(extra)
      }
    }

    fun get(): ChatComponent
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
   */
  @JvmStatic
  fun toRaw(component: ChatComponent)
    = toRaw(component, true)

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
  fun toRaw(component: ChatComponent, color: Boolean): String {
    val builder = StringBuilder()
    toRaw0(component, color, builder)
    return builder.toString()
  }

  private fun toRaw0(component: ChatComponent, color: Boolean, builder: StringBuilder) {
    if (color) {
      val chatStyle = component.style
      val chatColor = chatStyle.color
      if (chatColor is ChatColor)
        appendColor(builder, chatColor)
      if (chatStyle.bold.isTrue())
        appendColor(builder, ChatColor.BOLD)
      if (chatStyle.italic.isTrue())
        appendColor(builder, ChatColor.ITALIC)
      if (chatStyle.strikethrough.isTrue())
        appendColor(builder, ChatColor.STRIKETHROUGH)
      if (chatStyle.underlined.isTrue())
        appendColor(builder, ChatColor.UNDERLINE)
      if (chatStyle.obfuscated.isTrue())
        appendColor(builder, ChatColor.OBFUSCATED)
    }
    if (component is ChatComponentText)
      builder.append(component.text)
    component.extras.forEach { toRaw0(it, color, builder) }
  }

  private fun appendColor(builder: StringBuilder, color: ChatColor) {
    builder.append(color.toString())
  }

  private fun rgbToHex(rgb: Int): String {
    val r = rgb.shr(16).and(0xFF).toString(16)
    val g = rgb.shr(8).and(0xFF).toString(16)
    val b = rgb.and(0xFF).toString(16)
    return buildString {
      append("#")
      append(if (r.length == 1) "0$r" else r)
      append(if (g.length == 1) "0$g" else g)
      append(if (b.length == 1) "0$b" else b)
    }
  }

  private class ChatStyleSerializer : JsonDeserializer<ChatStyle>, JsonSerializer<ChatStyle> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ChatStyle? {
      if (!json.isJsonObject)
        return null
      val jsonObject : JsonObject = json.asJsonObject
      val style = ChatStyle()

      val color = if (jsonObject.has("color")) Color.ofSafely(jsonObject.get("color").asString) else null
      val bold = if (jsonObject.has("bold")) jsonObject.get("bold").asBoolean else null
      val italic = if (jsonObject.has("italic")) jsonObject.get("italic").asBoolean else null
      val underlined = if (jsonObject.has("underlined")) jsonObject.get("underlined").asBoolean else null
      val strikethrough = if (jsonObject.has("strikethrough")) jsonObject.get("strikethrough").asBoolean else null
      val obfuscated = if (jsonObject.has("obfuscated")) jsonObject.get("obfuscated").asBoolean else null
      val insertion = if (jsonObject.has("insertion")) jsonObject.get("obfuscated").asString else null
      val font = if (jsonObject.has("font")) jsonObject.get("font").asString else null

      var clickEvent: ChatClickEvent? = null
      if (jsonObject.has("clickEvent")) {
        val jsonObjectClickEvent = jsonObject.get("clickEvent") as? JsonObject
        if (jsonObjectClickEvent != null) {
          val action = Enums.ofName(ChatClickEvent.Action::class.java, jsonObjectClickEvent.get("action").asString.toUpperCase())
          val value = jsonObjectClickEvent.get("value")?.asString
          if (action != null && value != null)
            clickEvent = ChatClickEvent(action, value)
        }
      }
      var hoverEvent: ChatHoverEvent? = null
      if (jsonObject.has("hoverEvent")) {
        val jsonObjectHoverEvent = jsonObject.get("hoverEvent") as? JsonObject
        if (jsonObjectHoverEvent != null) {
          val action = Enums.ofName(ChatHoverEvent.Action::class.java, jsonObjectHoverEvent.get("action").asString.toUpperCase())
          val value = jsonObjectHoverEvent.get("value") ?: null
          if (action != null && value != null)
            hoverEvent = ChatHoverEvent(action, context.deserialize(value, ChatComponent::class.java))
        }
      }
      style.color = color
      style.bold = bold
      style.italic = italic
      style.underlined = underlined
      style.strikethrough = strikethrough
      style.obfuscated = obfuscated
      style.clickEvent = clickEvent
      style.hoverEvent = hoverEvent
      style.insertion = insertion
      style.font = font
      return style
    }

    override fun serialize(src: ChatStyle, typeOfSrc: Type, context: JsonSerializationContext): JsonElement? {
      if (src.isEmpty())
        return null
      val jsonObject = JsonObject()
      val rgb = src.color?.rgb
      if (rgb != null)
        jsonObject.addProperty("color", RGB_FORMATTING_TABLE[rgb] ?: rgbToHex(rgb))
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
        val clickEvent = src.clickEvent.notNull()
        val jsonObjectClickEvent = JsonObject()
        jsonObjectClickEvent.addProperty("action", clickEvent.action.name.toLowerCase())
        jsonObjectClickEvent.addProperty("value", clickEvent.value)
        jsonObject.add("clickEvent", jsonObjectClickEvent)
      }
      if (src.hoverEvent != null) {
        val hoverEvent = src.hoverEvent.notNull()
        val jsonObjectHoverEvent = JsonObject()
        jsonObjectHoverEvent.addProperty("action", hoverEvent.action.name.toLowerCase())
        if (hoverEvent.value is ChatComponentRaw)
          jsonObjectHoverEvent.addProperty("value", hoverEvent.value.raw)
        else
          jsonObjectHoverEvent.add("value", context.serialize(hoverEvent.value))
        jsonObject.add("hoverEvent", jsonObjectHoverEvent)
      }
      if (src.font != null)
        jsonObject.addProperty("font", src.font)
      return jsonObject
    }
  }

  private class ChatComponentSerializer : JsonDeserializer<ChatComponent>, JsonSerializer<ChatComponent> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ChatComponent? {
      if (json.isJsonPrimitive)
        return ChatComponentText(json.asString)
      if (json.isJsonArray) {
        var component: ChatComponent? = null
        val jsonArray = json.asJsonArray
        jsonArray.forEach {
          val component1 = deserialize(it, it::class.java, context)
          if (component == null)
            component = component1
          else if (component1 != null)
            component.notNull().append(component1)
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
          withs.indices.forEach {
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
          throw JsonParseException("A score component needs a least a name and an objective.")
        component = ChatComponentScore(jsonObjectScore.get("name").asString, jsonObjectScore.get("objective").asString)
        if (jsonObjectScore.has("value"))
          component.value = jsonObjectScore.get("value").asString
      } else if (jsonObject.has("selector")) {
        component = ChatComponentSelector(jsonObject.get("selector").asString)
      } else if (jsonObject.has("keybind")) {
        component = ChatComponentKeybind(jsonObject.get("keybind").asString)
      } else if (jsonObject.has("nbt")) {
        val nbt = jsonObject.get("nbt").asString
        val interpret = if (jsonObject.has("interpret")) jsonObject.get("interpret").asBoolean else null
        component = when {
          jsonObject.has("block") -> ChatComponentNBTBlock(nbt, interpret, jsonObject.get("block").asString)
          jsonObject.has("entity") -> ChatComponentNBTEntity(nbt, interpret, jsonObject.get("entity").asString)
          jsonObject.has("storage") -> ChatComponentNBTStorage(nbt, interpret, jsonObject.get("storage").asString)
          else -> throw JsonParseException("Don't know how to turn $json into a Component")
        }
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
        val jsonObjectStyle = context.serialize(src.style).asJsonObject
        jsonObjectStyle.entrySet().forEach { jsonObject.add(it.key, it.value) }
      }
      if (src.extras.isNotEmpty()) {
        val jsonArray = JsonArray()
        src.extras.forEach { jsonArray.add(serialize(it, it::class.java, context)) }
        jsonObject.add("extra", jsonArray)
      }
      if (src is ChatComponentText) {
        jsonObject.addProperty("text", src.text)
      } else if (src is ChatComponentTranslation) {
        jsonObject.addProperty("translate", src.key)
        if (src.withs.isNotEmpty()) {
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
      } else if (src is ChatComponentNBT) {
        jsonObject.addProperty("nbt", src.nbt)
        if (src.interpret != null)
          jsonObject.addProperty("interpret", src.interpret)
        when (src) {
          is ChatComponentNBTBlock -> jsonObject.addProperty("block", src.path)
          is ChatComponentNBTEntity -> jsonObject.addProperty("entity", src.path)
          is ChatComponentNBTStorage -> jsonObject.addProperty("storage", src.path)
          else -> throw JsonParseException("Don't know how to serialize $src as a Component")
        }
      } else {
        throw JsonParseException("Don't know how to serialize $src as a Component")
      }
      return jsonObject
    }
  }

  /**
   * 仅用在 Tooltip ItemStack 上
   */
  internal data class ChatComponentRaw(val raw: String) : ChatComponentAbstract()
}
