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

package com.lgou2w.ldk.bukkit.particle;

import org.bukkit.Color;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ParticleData {

  private ParticleData() { }

  public final static class Item {
    private final Material type;
    private final int data;

    public Item(@Nullable Material type, int data) {
      this.type = type != null ? type : Material.AIR;
      this.data = data;
    }

    @NotNull
    public Material getType() {
      return type;
    }

    public int getData() {
      return data;
    }
  }

  public final static class Block {
    private final Material type;
    private final int data;

    public Block(@Nullable Material type, int data) {
      this.type = type != null ? type : Material.AIR;
      this.data = data;
    }

    @NotNull
    public Material getType() {
      return type;
    }

    public int getData() {
      return data;
    }
  }

  public final static class Dust {
    private final Color color;
    private final float size;

    public Dust(@Nullable Color color, float size) {
      this.color = color != null ? color : Color.WHITE;
      this.size = size;
    }

    @NotNull
    public Color getColor() {
      return color;
    }

    public float getSize() {
      return size;
    }
  }

  public final static class Note {
    private final int value;

    public Note(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }
}
