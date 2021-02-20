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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public final class HoverEvent {
  private final Action<?> action;
  private final Object value;

  @Contract("null, _ -> fail; _, null -> fail")
  public <T> HoverEvent(Action<T> action, T value) {
    if (action == null) throw new NullPointerException("action");
    if (value == null) throw new NullPointerException("value");
    this.action = action;
    this.value = value;
  }

  @NotNull
  public Action<?> getAction() {
    return action;
  }

  @NotNull
  public <T> T getValue(Action<T> action) throws IllegalArgumentException {
    if (this.action != action) throw new IllegalArgumentException(
      "Illegal action type: " + action.name + " (Expected: " + this.action.name + ')');
    return action.cast(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HoverEvent that = (HoverEvent) o;
    return action == that.action && value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, value);
  }

  @Override
  public String toString() {
    return "HoverEvent{" +
      "action=" + action +
      ", value=" + value +
      '}';
  }

  public final static class Action<T> {
    private final String name;
    private final Function<T, JsonElement> serializer;
    private final Function<T, JsonElement> serializerToLegacy;
    private final Function<JsonElement, T> deserializer;
    private final Function<JsonElement, T> deserializerLegacy;

    public final static Action<ChatComponent> SHOW_TEXT = new Action<>(
      "show_text",
      ChatSerializer::toJsonTree,
      ChatSerializer::toJsonTree,
      ChatSerializer::fromJson,
      ChatSerializer::fromJson);

    public final static Action<EntityInfo> SHOW_ENTITY = new Action<>(
      "show_entity",
      EntityInfo::serialize,
      EntityInfo::serializeToLegacy,
      EntityInfo::deserialize,
      EntityInfo::deserializeFromLegacy
    );

    public final static Action<ItemStackInfo> SHOW_ITEM = new Action<>(
      "show_item",
      ItemStackInfo::serialize,
      ItemStackInfo::serializeToLegacy,
      ItemStackInfo::deserialize,
      ItemStackInfo::deserializeFromLegacy
    );

    private final static Map<String, Action<?>> NAME_MAP
      = Collections.unmodifiableMap(new HashMap<String, Action<?>>() {{
        put(SHOW_ITEM.name, SHOW_ITEM);
        put(SHOW_ENTITY.name, SHOW_ENTITY);
        put(SHOW_ITEM.name, SHOW_ITEM);
      }});

    @Nullable
    public static Action<?> fromName(String name) {
      return NAME_MAP.get(name);
    }

    private Action(
      String name,
      Function<T, JsonElement> serializer,
      Function<T, JsonElement> serializerToLegacy,
      Function<JsonElement, T> deserializer,
      Function<JsonElement, T> deserializerLegacy
    ) {
      this.name = name;
      this.serializer = serializer;
      this.serializerToLegacy = serializerToLegacy;
      this.deserializer = deserializer;
      this.deserializerLegacy = deserializerLegacy;
    }

    @NotNull
    public String getName() {
      return name;
    }

    @SuppressWarnings("unchecked")
    private T cast(Object value) {
      return (T) value;
    }

    JsonElement serialize(Object value) {
      return serializer.apply(cast(value));
    }

    JsonElement serializeToLegacy(Object value) {
      return serializerToLegacy.apply(cast(value));
    }

    HoverEvent deserialize(JsonElement json) {
      T value = deserializer.apply(json);
      return value == null ? null : new HoverEvent(this, value);
    }

    HoverEvent deserializeFromLegacy(JsonElement json) {
      T value = deserializerLegacy.apply(json);
      return value == null ? null : new HoverEvent(this, value);
    }

    @Override
    public String toString() {
      return name;
    }
  }

  public final static class EntityInfo {
    private final String type;
    private final UUID id;
    @Nullable private final ChatComponent name;

    @Contract("null, _, _ -> fail; _, null, _ -> fail")
    public EntityInfo(String type, UUID id, @Nullable ChatComponent name) {
      if (type == null) throw new NullPointerException("type");
      if (id == null) throw new NullPointerException("id");
      this.type = type;
      this.id = id;
      this.name = name;
    }

    @NotNull
    public String getType() {
      return type;
    }

    @NotNull
    public UUID getId() {
      return id;
    }

    @Nullable
    public ChatComponent getName() {
      return name;
    }

    @NotNull
    private JsonElement serialize() {
      JsonObject json = new JsonObject();
      json.addProperty("type", type);
      json.addProperty("id", id.toString());
      if (name != null) json.add("name", ChatSerializer.toJsonTree(name));
      return json;
    }

    @NotNull
    private JsonElement serializeToLegacy() {
      StringWriter out = new StringWriter();
      try (JsonWriter writer = new JsonWriter(out)) {
        writer.beginObject();
        writer.name("type").value(type);
        writer.name("id").value(id.toString());
        if (name != null) writer.name("name").value(ChatSerializer.toJson(name));
        writer.endObject();
      } catch (IOException ignore) {
      }
      return new JsonPrimitive(out.toString());
    }

    @Nullable
    private static EntityInfo deserialize(JsonElement json) {
      if (!json.isJsonObject()) return null;
      JsonObject object = json.getAsJsonObject();
      if (!object.has("type") || !object.has("id"))
        return null;
      String type = object.get("type").getAsString();
      UUID id = UUID.fromString(object.get("id").getAsString());
      ChatComponent name = object.has("name") ? ChatSerializer.fromJson(object.get("name")) : null;
      return new EntityInfo(type, id, name);
    }

    @Nullable
    private static EntityInfo deserializeFromLegacy(JsonElement json) {
      if (!json.isJsonPrimitive()) return null;
      String value = json.getAsString();
      String type = null, id = null, name = null;
      try (JsonReader reader = new JsonReader(new StringReader(value))) {
        reader.beginObject();
        while (reader.hasNext()) {
          String key = reader.nextName();
          switch (key) {
            case "type":
              type = reader.nextString();
              break;
            case "id":
              id = reader.nextString();
              break;
            case "name":
              name = reader.nextString();
              break;
            default:
              break;
          }
        }
        reader.endObject();
        if (type != null && id != null) {
          UUID idValue = UUID.fromString(id);
          ChatComponent nameValue = name != null ? ChatSerializer.fromJson(name) : null;
          return new EntityInfo(type, idValue, nameValue);
        }
      } catch (IOException ignore) {
      }
      return null;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      EntityInfo that = (EntityInfo) o;
      return type.equals(that.type) && id.equals(that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(type, id, name);
    }

    @Override
    public String toString() {
      return "EntityInfo{" +
        "type='" + type + '\'' +
        ", id=" + id +
        ", name=" + name +
        '}';
    }
  }

  public final static class ItemStackInfo {
    private final String id;
    private final int count;
    @Nullable private final String mojangsonTag;

    @Contract("null, _, _ -> fail")
    public ItemStackInfo(String id, int count, @Nullable String mojangsonTag) {
      if (id == null) throw new NullPointerException("id");
      this.id = id;
      this.count = count;
      this.mojangsonTag = mojangsonTag;
    }

    @NotNull
    public String getId() {
      return id;
    }

    public int getCount() {
      return count;
    }

    @Nullable
    public String getMojangsonTag() {
      return mojangsonTag;
    }

    @NotNull
    private JsonElement serialize() {
      JsonObject json = new JsonObject();
      json.addProperty("id", id);
      json.addProperty("count", count);
      if (mojangsonTag != null) json.addProperty("tag", mojangsonTag);
      return json;
    }

    @NotNull
    private JsonElement serializeToLegacy() {
      StringWriter out = new StringWriter();
      try (JsonWriter writer = new JsonWriter(out)) {
        writer.beginObject();
        writer.name("id").value(id);
        writer.name("count").value(count);
        if (mojangsonTag != null) writer.name("tag").value(mojangsonTag);
        writer.endObject();
      } catch (IOException ignore) {
      }
      return new JsonPrimitive(out.toString());
    }

    @Nullable
    private static ItemStackInfo deserialize(JsonElement json) {
      if (json.isJsonPrimitive()) {
        return new ItemStackInfo(json.getAsString(), 1, null);
      } else {
        if (!json.isJsonObject()) return null;
        JsonObject object = json.getAsJsonObject();
        if (!object.has("id")) return null;
        String id = object.get("id").getAsString();
        int count = object.has("count") ? object.get("count").getAsInt() : 1;
        String mojangsonTag = object.has("tag") ? object.get("tag").getAsString() : null;
        return new ItemStackInfo(id, count, mojangsonTag);
      }
    }

    @Nullable
    private static ItemStackInfo deserializeFromLegacy(JsonElement json) {
      if (!json.isJsonPrimitive()) return null;
      String value = json.getAsString();
      String id = null, mojangsonTag = null;
      int count = 1;
      try (JsonReader reader = new JsonReader(new StringReader(value))) {
        reader.beginObject();
        while (reader.hasNext()) {
          String key = reader.nextName();
          switch (key) {
            case "id":
              id = reader.nextString();
              break;
            case "count":
              count = reader.nextInt();
              break;
            case "tag":
              mojangsonTag = reader.nextString();
              break;
            default:
              break;
          }
        }
        reader.endObject();
        if (id != null)
          return new ItemStackInfo(id, count, mojangsonTag);
      } catch (IOException ignore) {
      }
      return null;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ItemStackInfo that = (ItemStackInfo) o;
      return count == that.count && id.equals(that.id) && Objects.equals(mojangsonTag, that.mojangsonTag);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, count, mojangsonTag);
    }

    @Override
    public String toString() {
      return "ItemStackInfo{" +
        "id='" + id + '\'' +
        ", count=" + count +
        ", tag='" + mojangsonTag + '\'' +
        '}';
    }
  }
}
