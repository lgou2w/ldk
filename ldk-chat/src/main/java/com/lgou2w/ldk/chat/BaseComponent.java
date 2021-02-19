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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class BaseComponent implements ChatComponent {
  @NotNull protected final List<ChatComponent> siblings = new ArrayList<>();
  @NotNull protected Style style = Style.EMPTY;

  @NotNull
  @Override
  public Style getStyle() {
    return style;
  }

  @NotNull
  @Override
  public ChatComponent setStyle(Style style) {
    if (style == null) throw new NullPointerException("style");
    this.style = style;
    return this;
  }

  @Override
  @NotNull
  public ChatComponent withStyle(Style style) {
    if (style == null) throw new NullPointerException("style");
    return setStyle(style.applyStyle(getStyle()));
  }

  @Override
  @NotNull
  public ChatComponent withStyle(Formatting... formats) {
    if (formats == null) throw new NullPointerException("formats");
    return setStyle(style.applyFormat(formats));
  }

  @NotNull
  @Override
  public List<ChatComponent> getSiblings() {
    return siblings;
  }

  @Override
  @NotNull
  public ChatComponent addSibling(ChatComponent sibling) {
    if (sibling == null) throw new NullPointerException("sibling");
    siblings.add(sibling);
    return this;
  }

  @Override
  @NotNull
  public ChatComponent addSibling(String text) {
    if (text == null) throw new NullPointerException("text");
    siblings.add(new TextComponent(text));
    return this;
  }

  @NotNull
  @Override
  public Iterator<ChatComponent> iterator() {
    List<ChatComponent> result = concat(this);
    return result.iterator();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseComponent that = (BaseComponent) o;
    return siblings.equals(that.siblings) && style.equals(that.style);
  }

  @Override
  public int hashCode() {
    return Objects.hash(siblings, style);
  }

  @Override
  public String toString() {
    return "BaseComponent{" +
      "siblings=" + siblings +
      ", style=" + style +
      '}';
  }

  static List<ChatComponent> concat(ChatComponent component) {
    List<ChatComponent> result = new ArrayList<>();
    concat(result, component);
    return result;
  }

  static void concat(List<ChatComponent> result, ChatComponent component) {
    result.add(component);
    for (ChatComponent sibling : component.getSiblings())
      concat(result, sibling);
  }

  final static class ChatComponentSerializer implements JsonSerializer<ChatComponent>, JsonDeserializer<ChatComponent> {
    @Override
    public JsonElement serialize(ChatComponent src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject json = new JsonObject();
      if (!src.getStyle().isEmpty()) {
        JsonObject jsonStyle = context.serialize(src.getStyle()).getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonStyle.entrySet()) {
          json.add(entry.getKey(), entry.getValue());
        }
      }
      if (!src.getSiblings().isEmpty()) {
        JsonArray jsonExtras = new JsonArray();
        for (ChatComponent sibling : src.getSiblings()) {
          JsonElement extra = serialize(sibling, sibling.getClass(), context);
          jsonExtras.add(extra);
        }
        json.add("extra", jsonExtras);
      }
      if (src instanceof TextComponent) {
        json.addProperty("text", ((TextComponent) src).getText());
      } else if (src instanceof TranslationComponent) {
        TranslationComponent translation = (TranslationComponent) src;
        json.addProperty("translate", translation.getKey());
        if (translation.getArgs().length > 0) {
          JsonArray jsonWiths = new JsonArray();
          for (Object arg : translation.getArgs()) {
            if (arg instanceof ChatComponent) {
              jsonWiths.add(serialize((ChatComponent) arg, arg.getClass(), context));
            } else {
              jsonWiths.add(new JsonPrimitive(arg.toString()));
            }
          }
          json.add("with", jsonWiths);
        }
      } else if (src instanceof ScoreComponent) {
        ScoreComponent score = (ScoreComponent) src;
        JsonObject jsonScore = new JsonObject();
        jsonScore.addProperty("name", score.getName());
        jsonScore.addProperty("objective", score.getObjective());
        if (score.getValue() != null)
          jsonScore.addProperty("value", score.getValue());
        json.add("score", jsonScore);
      } else if (src instanceof SelectorComponent) {
        json.addProperty("selector", ((SelectorComponent) src).getSelector());
      } else if (src instanceof KeybindComponent) {
        json.addProperty("keybind", ((KeybindComponent) src).getKeybind());
      } else if (src instanceof NBTComponent) {
        NBTComponent nbt = (NBTComponent) src;
        json.addProperty("nbt", nbt.getPath());
        json.addProperty("interpret", nbt.isInterpret());
        if (nbt instanceof NBTComponent.StorageNBTComponent) {
          json.addProperty("storage", nbt.getValue());
        } else if (nbt instanceof NBTComponent.BlockNBTComponent) {
          json.addProperty("block", nbt.getValue());
        } else if (nbt instanceof NBTComponent.EntityNBTComponent) {
          json.addProperty("entity", nbt.getValue());
        } else {
          throw new JsonParseException("Don't know how to serialize " + src + " as a Component");
        }
      }
      return json;
    }

    @Override
    public ChatComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      if (json.isJsonPrimitive()) return new TextComponent(json.getAsString());
      if (json.isJsonArray()) {
        JsonArray jsonArray = json.getAsJsonArray();
        ChatComponent component = null;
        for (JsonElement element : jsonArray) {
          ChatComponent sibling = deserialize(element, element.getClass(), context);
          if (component == null) component = sibling;
          else component.addSibling(sibling);
        }
        return component;
      }
      JsonObject jsonObject = json.getAsJsonObject();
      ChatComponent component;
      if (jsonObject.has("text")) {
        component = new TextComponent(jsonObject.get("text").getAsString());
      } else if (jsonObject.has("translate")) {
        String translate = jsonObject.get("translate").getAsString();
        if (jsonObject.has("with")) {
          JsonArray jsonWiths = jsonObject.getAsJsonArray("with");
          Object[] args = new Object[jsonWiths.size()];
          for (int i = 0; i < args.length; i++) {
            args[i] = deserialize(jsonWiths.get(i), typeOfT, context);
            if (args[i] instanceof TextComponent) {
              TextComponent textComponent = (TextComponent) args[i];
              if (textComponent.getStyle().isEmpty() && textComponent.getSiblings().isEmpty())
                args[i] = textComponent.getText();
            }
          }
          component = new TranslationComponent(translate, args);
        } else {
          component = new TranslationComponent(translate);
        }
      } else if (jsonObject.has("score")) {
        JsonObject jsonScore = jsonObject.getAsJsonObject("score");
        String name = jsonScore.has("name") ? jsonScore.get("name").getAsString() : null;
        String objective = jsonScore.has("objective") ? jsonScore.get("objective").getAsString() : null;
        if (name == null || objective == null)
          throw new JsonParseException("A score component needs a least a name and an objective.");
        String value = jsonScore.has("value") ? jsonScore.get("value").getAsString() : null;
        component = new ScoreComponent(name, objective, value);
      } else if (jsonObject.has("selector")) {
        component = new SelectorComponent(jsonObject.get("selector").getAsString());
      } else if (jsonObject.has("keybind")) {
        component = new KeybindComponent(jsonObject.get("keybind").getAsString());
      } else if (jsonObject.has("nbt")) {
        String nbt = jsonObject.get("nbt").getAsString();
        boolean interpret = jsonObject.has("interpret") && jsonObject.get("interpret").getAsBoolean();
        if (jsonObject.has("storage")) {
          String id = jsonObject.get("storage").getAsString();
          component = new NBTComponent.StorageNBTComponent(nbt, id, interpret);
        } else if (jsonObject.has("block")) {
          String position = jsonObject.get("block").getAsString();
          component = new NBTComponent.BlockNBTComponent(nbt, position, interpret);
        } else if (jsonObject.has("entity")) {
          String selector = jsonObject.get("entity").getAsString();
          component = new NBTComponent.EntityNBTComponent(nbt, selector, interpret);
        } else {
          throw new JsonParseException("Don't know how to turn " + json + " into a Component");
        }
      } else {
        throw new JsonParseException("Don't know how to parse " + json + " into a chat component.");
      }
      if (jsonObject.has("extra")) {
        JsonArray jsonExtras = jsonObject.getAsJsonArray("extra");
        if (jsonExtras.size() <= 0)
          throw new JsonParseException("Invalid empty array component.");
        for (JsonElement element : jsonExtras) {
          ChatComponent sibling = deserialize(element, typeOfT, context);
          component.addSibling(sibling);
        }
      }
      Style style = context.deserialize(json, Style.class);
      component.setStyle(style);
      return component;
    }
  }
}
