/*
 * Copyright (C) 2021 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.bukkit.reflect;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Remapper {
  protected final ClassLoader classLoader;

  Remapper(@Nullable ClassLoader classLoader) {
    this.classLoader = classLoader == null
      ? this.getClass().getClassLoader()
      : classLoader;
  }

  @NotNull
  public abstract String getServerName();

  @Nullable
  @Contract("null -> null; !null -> !null")
  public abstract String mapClassName(String name);

  @Nullable
  @Contract("null, _, _ -> fail; _, _, null -> fail; _, null, _ -> null; _, !null, _ -> !null")
  public abstract String mapMethodName(Class<?> clazz, String name, Class<?>[] parameterTypes);

  @Nullable
  @Contract("null, _ -> fail; _, null -> null; _, !null -> !null")
  public abstract String mapFieldName(Class<?> clazz, String name);
}
