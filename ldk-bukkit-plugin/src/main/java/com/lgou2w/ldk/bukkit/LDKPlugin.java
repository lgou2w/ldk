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

package com.lgou2w.ldk.bukkit;

import com.lgou2w.ldk.bukkit.version.BukkitVersion;
import com.lgou2w.ldk.bukkit.version.MinecraftVersion;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class LDKPlugin extends JavaPlugin {
  public final static String NAME = "LDK";
  public final static String PREFIX = "[" + NAME + "]";
  public final static int BSTATS_ID = 3254;

  @Override
  public void onLoad() {
  }

  @Override
  public void onEnable() {
    if (!BukkitVersion.isV18OrLater) {
      getLogger().severe("-------- A multi-module lgou2w development kit -----");
      getLogger().severe("  Very sorry. Although can load LDK, it is not compatible");
      getLogger().severe("  with 1.7 and previous versions from the beginning of design.");
      getLogger().severe("-------------");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }
    getLogger().info("A multi-module lgou2w development kit");
    getLogger().info("Open source: https://github.com/lgou2w/ldk");
    getLogger().log(Level.INFO, "Minecraft version: {0} Impl version: {1}", new Object[] {
      MinecraftVersion.CURRENT.getVersionString(),
      BukkitVersion.CURRENT.getVersionString()
    });
    setupMetrics();
  }

  @Override
  public void onDisable() {
  }

  private void setupMetrics() {
    try {
      Metrics metrics = new Metrics(this, BSTATS_ID);
      metrics.addCustomChart(new AdvancedPie("plugin_dependents", () -> {
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        int hard = 0, soft = 0;
        for (Plugin plugin : plugins) {
          PluginDescriptionFile description = plugin.getDescription();
          if (description.getDepend().contains(NAME)) hard++;
          if (description.getSoftDepend().contains(NAME)) soft++;
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("Hard", hard);
        map.put("Soft", soft);
        return map;
      }));
    } catch (Exception e) {
      getLogger().log(Level.WARNING, "Metrics stats service load failed:", e);
    }
  }
}
