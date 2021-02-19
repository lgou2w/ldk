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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ComponentBuilder {
  private final List<ChatComponent> parts;
  private int cursor;

  private ComponentBuilder() {
    this.parts = new ArrayList<>();
    this.cursor = -1;
  }

  @NotNull
  @Contract("-> new")
  public static ComponentBuilder newBuilder() {
    return new ComponentBuilder();
  }

  public int getCursor() {
    return cursor;
  }

  @NotNull
  public List<ChatComponent> getParts() {
    return parts;
  }

  @NotNull
  @Contract("-> this")
  public ComponentBuilder resetCursor() {
    cursor = parts.size() - 1;
    return this;
  }

  @NotNull
  @Contract("_ -> this")
  public ComponentBuilder setCursor(int pos) throws IndexOutOfBoundsException {
    if (this.cursor == pos || pos >= 0 && pos < parts.size()) {
      this.cursor = pos;
      return this;
    } else {
      throw new IndexOutOfBoundsException("Cursor out of bounds. (Expected between 0 + " + (parts.size() - 1) + ')');
    }
  }

  @NotNull
  @Contract("null -> fail; _ -> this")
  public ComponentBuilder append(ChatComponent... parts) throws IllegalArgumentException {
    if (parts == null) throw new NullPointerException("parts");
    if (parts.length <= 0) throw new IllegalArgumentException("No components to append.");
    this.parts.addAll(Arrays.asList(parts));
    resetCursor();
    return this;
  }

  @NotNull
  @Contract("null -> fail; _ -> this")
  public ComponentBuilder append(String text) {
    return append(new TextComponent(text));
  }

  public void remove(int pos) throws IndexOutOfBoundsException {
    if (parts.remove(pos) != null) {
      resetCursor();
    }
  }

  @NotNull
  public ChatComponent get(int pos) throws IndexOutOfBoundsException {
    return parts.get(pos);
  }

  @NotNull
  public ChatComponent getCurrent() throws IllegalStateException {
    if (cursor == -1) throw new IllegalStateException("Without any components.");
    return parts.get(cursor);
  }

  @NotNull
  @Contract("_ -> this")
  public ComponentBuilder color(@Nullable Color color) {
    getCurrent().withStyle(Style.EMPTY.withColor(color));
    return this;
  }

  @NotNull
  @Contract("_ -> this")
  public ComponentBuilder color(@Nullable Formatting formatting) {
    getCurrent().withStyle(Style.EMPTY.withColor(formatting));
    return this;
  }

  @NotNull
  @Contract("_ -> this")
  public ComponentBuilder color(int rgb) {
    getCurrent().withStyle(Style.EMPTY.withColor(rgb));
    return this;
  }

  @NotNull
  @Contract("_ -> this")
  public ComponentBuilder bold(boolean bold) {
    getCurrent().withStyle(Style.EMPTY.withBold(bold));
    return this;
  }

  @NotNull
  @Contract("_ -> this")
  public ComponentBuilder italic(boolean italic) {
    getCurrent().withStyle(Style.EMPTY.withItalic(italic));
    return this;
  }

  @NotNull
  @Contract("_ -> this")
  public ComponentBuilder underlined(boolean underlined) {
    getCurrent().withStyle(Style.EMPTY.withUnderlined(underlined));
    return this;
  }

  @NotNull
  @Contract("_ -> this")
  public ComponentBuilder strikethrough(boolean strikethrough) {
    getCurrent().withStyle(Style.EMPTY.withStrikethrough(strikethrough));
    return this;
  }

  @NotNull
  @Contract("_ -> this")
  public ComponentBuilder obfuscated(boolean obfuscated) {
    getCurrent().withStyle(Style.EMPTY.withObfuscated(obfuscated));
    return this;
  }

  @NotNull
  @Contract("_ -> this")
  public ComponentBuilder insertion(@Nullable String insertion) {
    getCurrent().withStyle(Style.EMPTY.withInsertion(insertion));
    return this;
  }

  @NotNull
  @Contract("_ -> this")
  public ComponentBuilder clickable(@Nullable ClickEvent clickEvent) {
    getCurrent().withStyle(Style.EMPTY.withClickEvent(clickEvent));
    return this;
  }

  @NotNull
  @Contract("_ -> this")
  public ComponentBuilder hoverable(@Nullable HoverEvent hoverable) {
    getCurrent().withStyle(Style.EMPTY.withHoverEvent(hoverable));
    return this;
  }

  @NotNull
  @Contract("_ -> this")
  public ComponentBuilder font(@Nullable String font) {
    getCurrent().withStyle(Style.EMPTY.withFont(font));
    return this;
  }

  /// Event extended

  @NotNull
  @Contract("null -> fail; !null -> this")
  public ComponentBuilder clickOpenURL(String url) {
    if (url == null) throw new NullPointerException("url");
    clickable(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
    return this;
  }

  @NotNull
  @Contract("null -> fail; !null -> this")
  public ComponentBuilder clickOpenFile(String file) {
    if (file == null) throw new NullPointerException("file");
    clickable(new ClickEvent(ClickEvent.Action.OPEN_FILE, file));
    return this;
  }

  @NotNull
  @Contract("null -> fail; !null -> this")
  public ComponentBuilder clickSuggestCommand(String command) {
    if (command == null) throw new NullPointerException("command");
    clickable(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
    return this;
  }

  @NotNull
  @Contract("null -> fail; !null -> this")
  public ComponentBuilder clickRunCommand(String command) {
    if (command == null) throw new NullPointerException("command");
    clickable(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
    return this;
  }

  @NotNull
  @Contract("null -> fail; !null -> this")
  public ComponentBuilder clickChangePage(String page) {
    if (page == null) throw new NullPointerException("page");
    clickable(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, page));
    return this;
  }

  @NotNull
  @Contract("null -> fail; !null -> this")
  public ComponentBuilder clickCopyToClipboard(String value) {
    if (value == null) throw new NullPointerException("value");
    clickable(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, value));
    return this;
  }

  @NotNull
  @Contract("null -> fail; !null -> this")
  public ComponentBuilder hoverShowText(ChatComponent value) {
    if (value == null) throw new NullPointerException("value");
    hoverable(new HoverEvent(HoverEvent.Action.SHOW_TEXT, value));
    return this;
  }

  @NotNull
  @Contract("null -> fail; !null -> this")
  public ComponentBuilder hoverShowItem(String itemData) {
    if (itemData == null) throw new NullPointerException("itemData");
    ChatComponent value = new ChatSerializer.NonSerializedValueComponent(itemData); // non serialized
    hoverable(new HoverEvent(HoverEvent.Action.SHOW_ITEM, value));
    return this;
  }

  @NotNull
  @Contract("null -> fail; !null -> this")
  public ComponentBuilder hoverShowEntity(String entityData) {
    if (entityData == null) throw new NullPointerException("entityData");
    ChatComponent value = new ChatSerializer.NonSerializedValueComponent(entityData); // non serialized
    hoverable(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, value));
    return this;
  }

  ///

  @NotNull
  public ChatComponent[] create() {
    return parts.toArray(new ChatComponent[0]);
  }

  @NotNull
  public TextComponent createToSingleton() {
    TextComponent component = new TextComponent("");
    for (ChatComponent part : parts)
      component.addSibling(part);
    return component;
  }
}
