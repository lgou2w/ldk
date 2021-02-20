/*
 * Copyright (C) 2016-2021 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.chat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Objects;

public final class Style {
  @Nullable private final Color color;
  @Nullable private final Boolean bold, italic, underlined, strikethrough, obfuscated;
  @Nullable private final ClickEvent clickEvent;
  @Nullable private final HoverEvent hoverEvent;
  @Nullable private final String insertion, font;

  public final static Style EMPTY = new Style(null, null, null, null, null, null, null, null, null, null);

  private Style(
    @Nullable Color color,
    @Nullable Boolean bold,
    @Nullable Boolean italic,
    @Nullable Boolean underlined,
    @Nullable Boolean strikethrough,
    @Nullable Boolean obfuscated,
    @Nullable ClickEvent clickEvent,
    @Nullable HoverEvent hoverEvent,
    @Nullable String insertion,
    @Nullable String font
  ) {
    this.color = color;
    this.bold = bold;
    this.italic = italic;
    this.underlined = underlined;
    this.strikethrough = strikethrough;
    this.obfuscated = obfuscated;
    this.clickEvent = clickEvent;
    this.hoverEvent = hoverEvent;
    this.insertion = insertion;
    this.font = font;
  }

  @Nullable
  public Color getColor() {
    return color;
  }

  public boolean isBold() {
    return bold == Boolean.TRUE;
  }

  public boolean isItalic() {
    return italic == Boolean.TRUE;
  }

  public boolean isUnderlined() {
    return underlined == Boolean.TRUE;
  }

  public boolean isStrikethrough() {
    return strikethrough == Boolean.TRUE;
  }

  public boolean isObfuscated() {
    return obfuscated == Boolean.TRUE;
  }

  public boolean isEmpty() {
    return equals(EMPTY);
  }

  @Nullable
  public ClickEvent getClickEvent() {
    return clickEvent;
  }

  @Nullable
  public HoverEvent getHoverEvent() {
    return hoverEvent;
  }

  @Nullable
  public String getInsertion() {
    return insertion;
  }

  @Nullable
  public String getFont() {
    return font;
  }

  @NotNull
  @Contract("_ -> new")
  public Style withColor(@Nullable Color color) {
    return new Style(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
  }

  @NotNull
  @Contract("_ -> new")
  public Style withColor(@Nullable Formatting formatting) {
    return withColor(formatting != null ? Color.fromFormatting(formatting) : null);
  }

  @NotNull
  @Contract("_ -> new")
  public Style withColor(int rgb) {
    return withColor(Color.fromRGB(rgb));
  }

  @NotNull
  @Contract("_ -> new")
  public Style withBold(@Nullable Boolean bold) {
    return new Style(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
  }

  @NotNull
  @Contract("_ -> new")
  public Style withItalic(@Nullable Boolean italic) {
    return new Style(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
  }

  @NotNull
  @Contract("_ -> new")
  public Style withUnderlined(@Nullable Boolean underlined) {
    return new Style(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
  }

  @NotNull
  @Contract("_ -> new")
  public Style withStrikethrough(@Nullable Boolean strikethrough) {
    return new Style(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
  }

  @NotNull
  @Contract("_ -> new")
  public Style withObfuscated(@Nullable Boolean obfuscated) {
    return new Style(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
  }

  @NotNull
  @Contract("_ -> new")
  public Style withClickEvent(@Nullable ClickEvent clickEvent) {
    return new Style(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
  }

  @NotNull
  @Contract("_ -> new")
  public Style withHoverEvent(@Nullable HoverEvent hoverEvent) {
    return new Style(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
  }

  @NotNull
  @Contract("_ -> new")
  public Style withInsertion(@Nullable String insertion) {
    return new Style(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
  }

  @NotNull
  @Contract("_ -> new")
  public Style withFont(@Nullable String font) {
    return new Style(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
  }

  @NotNull
  @Contract("null -> fail; _ -> new")
  public Style applyFormat(Formatting... formats) {
    if (formats == null) throw new NullPointerException("formats");
    Color color = this.color;
    Boolean bold = this.bold;
    Boolean italic = this.italic;
    Boolean underlined = this.underlined;
    Boolean strikethrough = this.strikethrough;
    Boolean obfuscated = this.obfuscated;
    for (Formatting formatting : formats) {
      switch (formatting) {
        case BOLD:
          bold = true;
          break;
        case ITALIC:
          italic = true;
          break;
        case UNDERLINE:
          underlined = true;
          break;
        case STRIKETHROUGH:
          strikethrough = true;
          break;
        case OBFUSCATED:
          obfuscated = true;
          break;
        case RESET:
          return EMPTY;
        default:
          color = Color.fromFormatting(formatting);
          break;
      }
    }
    return new Style(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
  }

  @NotNull
  @Contract("null -> fail")
  public Style applyStyle(Style other) {
    if (other == null) throw new NullPointerException("other");
    if (this == EMPTY) return other;
    if (other == EMPTY) return this;
    return new Style(
      color != null ? color : other.color,
      bold != null ? bold : other.bold,
      italic != null ? italic : other.italic,
      underlined != null ? underlined : other.underlined,
      strikethrough != null ? strikethrough : other.strikethrough,
      obfuscated != null ? obfuscated : other.obfuscated,
      clickEvent != null ? clickEvent : other.clickEvent,
      hoverEvent != null ? hoverEvent : other.hoverEvent,
      insertion != null ? insertion : other.insertion,
      font != null ? font : other.font
    );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Style style = (Style) o;
    return Objects.equals(color, style.color) && Objects.equals(bold, style.bold) &&
      Objects.equals(italic, style.italic) && Objects.equals(underlined, style.underlined) &&
      Objects.equals(strikethrough, style.strikethrough) && Objects.equals(obfuscated, style.obfuscated) &&
      Objects.equals(clickEvent, style.clickEvent) && Objects.equals(hoverEvent, style.hoverEvent) &&
      Objects.equals(insertion, style.insertion) && Objects.equals(font, style.font);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
  }

  @Override
  public String toString() {
    return "Style{" +
      "color=" + color +
      ", bold=" + bold +
      ", italic=" + italic +
      ", underlined=" + underlined +
      ", strikethrough=" + strikethrough +
      ", obfuscated=" + obfuscated +
      ", clickEvent=" + clickEvent +
      ", hoverEvent=" + hoverEvent +
      ", insertion='" + insertion + '\'' +
      ", font='" + font + '\'' +
      '}';
  }

  final static class StyleSerializer implements JsonSerializer<Style>, JsonDeserializer<Style> {
    @Override
    public JsonElement serialize(Style src, Type typeOfSrc, JsonSerializationContext context) {
      if (src.isEmpty()) return null;
      JsonObject json = new JsonObject();
      if (src.color != null)
        json.addProperty("color", src.color.serialize());
      if (src.bold != null)
        json.addProperty("bold", src.bold);
      if (src.italic != null)
        json.addProperty("italic", src.italic);
      if (src.underlined != null)
        json.addProperty("underlined", src.underlined);
      if (src.strikethrough != null)
        json.addProperty("strikethrough", src.strikethrough);
      if (src.obfuscated != null)
        json.addProperty("obfuscated", src.obfuscated);
      if (src.insertion != null)
        json.addProperty("insertion", src.insertion);
      if (src.clickEvent != null) {
        JsonObject jsonClickEvent = new JsonObject();
        jsonClickEvent.addProperty("action", src.clickEvent.getAction().name().toLowerCase(Locale.ROOT));
        jsonClickEvent.addProperty("value", src.clickEvent.getValue());
        json.add("clickEvent", jsonClickEvent);
      }
      if (src.hoverEvent != null) {
        JsonObject jsonHoverEvent = new JsonObject();
        jsonHoverEvent.addProperty("action", src.hoverEvent.getAction().name().toLowerCase(Locale.ROOT));
        ChatComponent value = src.hoverEvent.getValue();
        if (value instanceof ChatSerializer.NonSerializedValueComponent) {
          jsonHoverEvent.addProperty("value", ((ChatSerializer.NonSerializedValueComponent) value).value);
        } else {
          jsonHoverEvent.add("value", context.serialize(value));
        }
        json.add("hoverEvent", jsonHoverEvent);
      }
      if (src.font != null)
        json.addProperty("font", src.font);
      return json;
    }

    @Override
    public Style deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      if (!json.isJsonObject()) return null;
      JsonObject object = json.getAsJsonObject();
      Color color = object.has("color") ? Color.parse(object.get("color").getAsString()) : null;
      Boolean bold = object.has("bold") ? object.get("bold").getAsBoolean() : null;
      Boolean italic = object.has("italic") ? object.get("italic").getAsBoolean() : null;
      Boolean underlined = object.has("underlined") ? object.get("underlined").getAsBoolean() : null;
      Boolean strikethrough = object.has("strikethrough") ? object.get("strikethrough").getAsBoolean() : null;
      Boolean obfuscated = object.has("obfuscated") ? object.get("obfuscated").getAsBoolean() : null;
      String insertion = object.has("insertion") ? object.get("insertion").getAsString() : null;
      ClickEvent clickEvent = null;
      if (object.has("clickEvent")) {
        JsonObject jsonClickEvent = object.getAsJsonObject("clickEvent");
        ClickEvent.Action action = ClickEvent.Action.fromName(jsonClickEvent.get("action").getAsString());
        String value = jsonClickEvent.get("value").getAsString();
        if (action != null && value != null) {
          clickEvent = new ClickEvent(action, value);
        }
      }
      HoverEvent hoverEvent = null;
      if (object.has("hoverEvent")) {
        JsonObject jsonHoverEvent = object.getAsJsonObject("hoverEvent");
        HoverEvent.Action action = HoverEvent.Action.fromName(jsonHoverEvent.get("action").getAsString());
        if (action != null && jsonHoverEvent.has("value")) {
          JsonElement value = jsonHoverEvent.get("value");
          ChatComponent valueComponent = value.isJsonObject()
            ? context.deserialize(value, ChatComponent.class)
            : new ChatSerializer.NonSerializedValueComponent(value.getAsString());
          hoverEvent = new HoverEvent(action, valueComponent);
        } else if (action != null && jsonHoverEvent.has("contents")) {
          JsonElement contents = jsonHoverEvent.get("contents");
          ChatComponent value;
          switch (action) {
            case SHOW_TEXT:
              value = context.deserialize(contents, ChatComponent.class);
              hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, value);
              break;
            case SHOW_ITEM:
              JsonObject jsonItem = contents.getAsJsonObject();
              if (!jsonItem.has("id") || !jsonItem.has("count"))
                throw new JsonParseException("A show item value needs a least a 'id' and an 'count' properties.");
              String itemId = jsonItem.get("id").getAsString();
              int itemCount = jsonItem.get("count").getAsInt();
              String itemTag = jsonItem.has("tag") ? jsonItem.get("tag").getAsString() : null;
              StringBuilder itemBuilder = new StringBuilder()
                .append("{\"id\":\"")
                .append(itemId)
                .append('"')
                .append(",\"Count\":")
                .append(itemCount);
              if (itemTag != null) {
                itemBuilder
                  .append(",\"tag\":")
                  .append(itemTag); // not escape double quote
              }
              itemBuilder.append('}');
              value = new ChatSerializer.NonSerializedValueComponent(itemBuilder.toString());
              hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, value);
              break;
            case SHOW_ENTITY:
              JsonObject jsonEntity = contents.getAsJsonObject();
              if (!jsonEntity.has("id"))
                throw new JsonParseException("A show entity value needs a least a 'id' property.");
              String entityId = jsonEntity.get("id").getAsString();
              String entityType = jsonEntity.has("type") ? jsonEntity.get("type").getAsString() : null;
              JsonObject entityName = jsonEntity.has("name") ? jsonEntity.get("name").getAsJsonObject() : null;
              StringBuilder entityBuilder = new StringBuilder()
                .append("{\"id\":\"")
                .append(entityId)
                .append('"');
              if (entityType != null) {
                entityBuilder
                  .append(",\"type\":\"")
                  .append(entityType)
                  .append('"');
              }
              if (entityName != null) {
                entityBuilder
                  .append(",\"name\":\"") // need escape double quote
                  .append(entityName.toString().replace("\"", "\\\""))
                  .append("\"");
              }
              entityBuilder.append('}');
              value = new ChatSerializer.NonSerializedValueComponent(entityBuilder.toString());
              hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY, value);
              break;
            default:
              break;
          }
        }
      }
      String font = object.has("font") ? object.get("font").getAsString() : null;
      return new Style(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
    }
  }
}
