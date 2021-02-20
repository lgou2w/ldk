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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatSerializer {

  private ChatSerializer() { }

  final static Gson GSON = new GsonBuilder()
    .disableHtmlEscaping()
    .registerTypeHierarchyAdapter(Style.class, new Style.StyleSerializer())
    .registerTypeHierarchyAdapter(ChatComponent.class, new BaseComponent.ChatComponentSerializer())
    .create();

  @NotNull
  @Contract("null -> fail")
  public static ChatComponent fromJson(String json) throws JsonParseException {
    if (json == null) throw new NullPointerException("json");
    try {
      JsonReader reader = new JsonReader(new StringReader(json));
      reader.setLenient(false);
      return GSON.getAdapter(ChatComponent.class).read(reader);
    } catch (IOException e) {
      throw new JsonParseException(e);
    }
  }

  @NotNull
  @Contract("null -> fail")
  public static ChatComponent fromJsonLenient(String json) throws JsonParseException {
    if (json == null) throw new NullPointerException("json");
    try {
      JsonReader reader = new JsonReader(new StringReader(json));
      reader.setLenient(true);
      return GSON.getAdapter(ChatComponent.class).read(reader);
    } catch (IOException e) {
      throw new JsonParseException(e);
    }
  }

  @NotNull
  @Contract("null -> fail")
  public static String toJson(ChatComponent component) throws JsonParseException {
    if (component == null) throw new NullPointerException("component");
    return GSON.toJson(component);
  }

  private static void toPlaintText0(
    StringBuilder builder,
    ChatComponent component,
    boolean includeFormat,
    boolean includeHexColor
  ) {
    Iterator<ChatComponent> iterator = component.iterator();
    boolean hadFormat = false;
    while (iterator.hasNext()) {
      ChatComponent element = iterator.next();
      String text = element instanceof TextComponent
        ? ((TextComponent) element).getText()
        : element instanceof SelectorComponent
        ? ((SelectorComponent) element).getSelector()
        : null;
      if (includeFormat) {
        Style style = element.getStyle();
        Color color = style.getColor();
        if ((text != null && !text.isEmpty()) || color != null) {
          if (color == null) {
            if (hadFormat) {
              builder.append(Formatting.RESET);
              hadFormat = false;
            }
          } else {
            Formatting format = Formatting.fromName(style.getColor().serialize());
            if (format != null) builder.append(format);
            else if (includeHexColor) {
              builder.append(Formatting.FORMATTING_PREFIX).append('x');
              char[] hex = color.serialize().substring(1).toCharArray();
              for (char magic : hex) builder.append(Formatting.FORMATTING_PREFIX).append(magic);
            }
            hadFormat = true;
          }
        }
        if (style.isBold()) {
          builder.append(Formatting.BOLD);
          hadFormat = true;
        }
        if (style.isItalic()) {
          builder.append(Formatting.ITALIC);
          hadFormat = true;
        }
        if (style.isStrikethrough()) {
          builder.append(Formatting.STRIKETHROUGH);
          hadFormat = true;
        }
        if (style.isUnderlined()) {
          builder.append(Formatting.UNDERLINE);
          hadFormat = true;
        }
        if (style.isObfuscated()) {
          builder.append(Formatting.OBFUSCATED);
          hadFormat = true;
        }
      }
      if (text != null)
        builder.append(text);
    }
  }

  @NotNull
  public static String toPlainText(
    @Nullable ChatComponent component,
    boolean includeFormat,
    boolean includeHexColor
  ) {
    if (component == null) return "";
    StringBuilder builder = new StringBuilder();
    toPlaintText0(builder, component, includeFormat, includeHexColor);
    return builder.toString();
  }

  @NotNull
  public static String toPlainTextWithFormatted(@Nullable ChatComponent component, boolean includeHexColor) {
    return toPlainText(component, true, includeHexColor);
  }

  @NotNull
  public static String toPlainText(@Nullable ChatComponent component) {
    return toPlainText(component, false, false);
  }

  @NotNull
  public static ChatComponent[] fromPlainText(@Nullable String value, boolean keepNewlines, boolean keepPlainURL) {
    List<ChatComponent> result = new PlainText(value, keepNewlines, keepPlainURL).result;
    return result.toArray(new ChatComponent[0]);
  }

  @NotNull
  public static ChatComponent[] fromPlainText(@Nullable String value, boolean keepNewlines) {
    return fromPlainText(value, keepNewlines, false);
  }

  @Contract("null -> null; !null -> !null")
  public static ChatComponent fromPlainText(@Nullable String value) {
    if (value == null) return null;
    if (value.isEmpty()) return new TextComponent("");
    List<ChatComponent> result = new PlainText(value, false, false).result;
    return result.get(0);
  }

  final static class PlainText {
    final List<ChatComponent> result;
    final String value;
    ChatComponent current;
    StringBuilder hex;
    Style style;
    int index;

    final static Pattern INCREMENTAL_KEEP_NEWLINES_PATTERN;
    final static Pattern INCREMENTAL_PATTERN;
    final static Style RESET;

    static {
      INCREMENTAL_KEEP_NEWLINES_PATTERN = Pattern.compile("(" + Formatting.FORMATTING_PREFIX + "[0-9a-fk-orx])|((?:(?:https?)://)?(?:[-\\w_.]{2,}\\.[a-z]{2,4}.*?(?=[.?!,;:]?(?:[" + Formatting.FORMATTING_PREFIX + " \\n]|$))))|(\\n)", Pattern.CASE_INSENSITIVE);
      INCREMENTAL_PATTERN = Pattern.compile("(" + Formatting.FORMATTING_PREFIX + "[0-9a-fk-orx])|((?:(?:https?)://)?(?:[-\\w_.]{2,}\\.[a-z]{2,4}.*?(?=[.?!,;:]?(?:[" + Formatting.FORMATTING_PREFIX + " ]|$))))", Pattern.CASE_INSENSITIVE);
      RESET = Style.EMPTY.withBold(false).withItalic(false).withUnderlined(false).withStrikethrough(false).withObfuscated(false);
    }

    PlainText(@Nullable String value, boolean keepNewlines, boolean keepPlainURL) {
      this.value = value;
      this.current = new TextComponent("");
      this.style = Style.EMPTY;

      if (value == null) {
        result = Collections.singletonList(current);
      } else {
        result = new ArrayList<ChatComponent>(){{ add(current); }};
        Matcher matcher = (keepNewlines ? INCREMENTAL_KEEP_NEWLINES_PATTERN : INCREMENTAL_PATTERN).matcher(value);
        boolean needsAdd;
        String match;
        int groupId;
        for (needsAdd = false; matcher.find(); index = matcher.end(groupId)) {
          groupId = 0;
          do {
            ++groupId;
          } while ((match = matcher.group(groupId)) == null);

          int start = matcher.start(groupId);
          if (start > index) {
            needsAdd = false;
            appendComponent(start);
          }

          switch (groupId) {
            case 1:
              char c = match.toLowerCase(Locale.ENGLISH).charAt(1);
              Formatting format = Formatting.fromCode(c);
              if (c == 'x') {
                hex = new StringBuilder("#");
              } else if (hex != null) {
                hex.append(c);
                if (hex.length() == 7) {
                  style = RESET.withColor(Color.parse(hex.toString()));
                  hex = null;
                }
              } else if (format != null && format.isFormat() && format != Formatting.RESET) {
                switch (format) {
                  case BOLD:
                    style = style.withBold(true);
                    break;
                  case ITALIC:
                    style = style.withItalic(true);
                    break;
                  case STRIKETHROUGH:
                    style = style.withStrikethrough(true);
                    break;
                  case UNDERLINE:
                    style = style.withUnderlined(true);
                    break;
                  case OBFUSCATED:
                    style = style.withObfuscated(true);
                }
              } else {
                style = RESET.withColor(format);
              }
              needsAdd = true;
              break;
            case 2:
              if (keepPlainURL) appendComponent(matcher.end(groupId));
              else {
                if (!match.startsWith("https://") && !match.startsWith("http://"))
                  match = "http://" + match;
                style = style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
                appendComponent(matcher.end(groupId));
                style = style.withClickEvent(null);
              }
              break;
            case 3:
              if (needsAdd) appendComponent(index);
              current = null;
              break;
          }
        }
        if (index < value.length() || needsAdd)
          appendComponent(value.length());
      }
    }

    void appendComponent(int index) {
      String section = value.substring(this.index, index);
      ChatComponent sibling = new TextComponent(section).setStyle(style);
      this.index = index;
      if (current == null) {
        current = new TextComponent("");
        result.add(current);
      }
      current.addSibling(sibling);
    }
  }

  // TODO: docs
  final static class NonSerializedValueComponent extends BaseComponent {
    final String value;

    public NonSerializedValueComponent(String value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;
      NonSerializedValueComponent that = (NonSerializedValueComponent) o;
      return value.equals(that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), value);
    }

    @Override
    public String toString() {
      return "NonSerializedValueComponent{" +
        "value='" + value + '\'' +
        '}';
    }
  }
}
