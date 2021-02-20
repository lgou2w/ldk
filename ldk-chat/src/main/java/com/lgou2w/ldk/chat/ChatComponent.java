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

import java.util.List;

public interface ChatComponent extends Iterable<ChatComponent> {

  @NotNull
  Style getStyle();

  @NotNull
  @Contract("null -> fail; !null -> this")
  ChatComponent setStyle(Style style);

  @NotNull
  @Contract("null -> fail; !null -> this")
  ChatComponent withStyle(Style style);

  @NotNull
  @Contract("null -> fail; !null -> this")
  ChatComponent withStyle(Formatting... formats);

  @NotNull
  List<ChatComponent> getSiblings();

  @NotNull
  @Contract("null -> fail; !null -> this")
  ChatComponent addSibling(ChatComponent sibling);

  @NotNull
  @Contract("null -> fail; !null -> this")
  ChatComponent addSibling(String text);

  @NotNull
  @Contract("-> new")
  static ComponentBuilder builder() {
    return ComponentBuilder.newBuilder();
  }
}
