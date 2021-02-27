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

package com.lgou2w.ldk.bukkit.internal;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

/** INTERNAL ONLY */
@Deprecated
public final class FakePlugin extends PluginBase {
  private final PluginDescriptionFile description;

  public FakePlugin(@NotNull String name) {
    this.description = new PluginDescriptionFile(name, "0", "0");
  }

  @NotNull
  @Override
  public File getDataFolder() {
    throw new UnsupportedOperationException();
  }

  @NotNull
  @Override
  public PluginLoader getPluginLoader() {
    throw new UnsupportedOperationException();
  }

  @NotNull
  @Override
  public Server getServer() {
    return Bukkit.getServer();
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @NotNull
  @Override
  public PluginDescriptionFile getDescription() {
    return description;
  }

  @NotNull
  @Override
  public FileConfiguration getConfig() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void reloadConfig() { }

  @Override
  public void saveConfig() { }

  @Override
  public void saveDefaultConfig() { }

  @Override
  public void saveResource(@NotNull String resourcePath, boolean replace) { }

  @Nullable
  @Override
  public InputStream getResource(@NotNull String filename) {
    return null;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    return false;
  }

  @Nullable
  @Override
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    return null;
  }

  @Override
  public boolean isNaggable() {
    return false;
  }

  @Override
  public void setNaggable(boolean canNag) { }

  @NotNull
  @Override
  public Logger getLogger() {
    return Bukkit.getLogger();
  }

  @Override
  public void onLoad() { }

  @Override
  public void onEnable() { }

  @Override
  public void onDisable() { }

  @Nullable
  @Override
  public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
    return null;
  }
}
