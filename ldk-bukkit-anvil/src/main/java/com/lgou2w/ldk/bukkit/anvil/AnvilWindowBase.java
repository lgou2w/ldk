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

package com.lgou2w.ldk.bukkit.anvil;

import com.lgou2w.ldk.bukkit.internal.FakePlugin;
import com.lgou2w.ldk.reflect.ConstructorAccessor;
import com.lgou2w.ldk.reflect.FieldAccessor;
import com.lgou2w.ldk.reflect.FuzzyReflection;
import com.lgou2w.ldk.reflect.MethodAccessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getCraftBukkitClass;
import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getMinecraftClass;
import static com.lgou2w.ldk.bukkit.reflect.MinecraftReflection.getMinecraftClassOrNull;

/** INTERNAL ONLY */
@Deprecated
@SuppressWarnings("DeprecatedIsStillUsed")
public abstract class AnvilWindowBase implements AnvilWindow {
  private final Plugin plugin;
  private boolean allowMove = true;
  private Consumer<AnvilWindowOpenEvent> openListener;
  private Consumer<AnvilWindowCloseEvent> closeListener;
  private Consumer<AnvilWindowClickEvent> clickListener;
  private Consumer<AnvilWindowInputEvent> inputListener;

  @Contract("null -> fail")
  protected AnvilWindowBase(Plugin plugin) {
    this.plugin = Objects.requireNonNull(plugin, "plugin");
  }

  @NotNull
  @Override
  public Plugin getPlugin() {
    return plugin;
  }

  @Override
  public boolean isAllowMove() {
    return allowMove;
  }

  @NotNull
  @Override
  public AnvilWindow setAllowMove(boolean flag) {
    this.allowMove = flag;
    return this;
  }

  @Override
  public boolean isOpened() {
    return getHandle() != null;
  }

  @NotNull
  @Override
  public AnvilWindow open(Player viewer) throws IllegalStateException {
    if (isOpened()) throw new IllegalStateException("Anvil window is already opened.");
    safelyRegistryListenerHandler();
    return this;
  }

  @Nullable
  @Override
  public ItemStack getItem(Slot slot) throws IllegalStateException {
    if (!isOpened()) throw new IllegalStateException("Anvil window is not opened yet.");
    return getInventory().getItem(slot.ordinal());
  }

  @NotNull
  @Override
  public AnvilWindow setItem(Slot slot, @Nullable ItemStack stack) throws IllegalStateException {
    if (!isOpened()) throw new IllegalStateException("Anvil window is not opened yet.");
    getInventory().setItem(slot.ordinal(), stack);
    return this;
  }

  @NotNull
  @Override
  public AnvilWindow onOpen(@Nullable Consumer<AnvilWindowOpenEvent> listener) {
    this.openListener = listener;
    return this;
  }

  @NotNull
  @Override
  public AnvilWindow onClose(@Nullable Consumer<AnvilWindowCloseEvent> listener) {
    this.closeListener = listener;
    return this;
  }

  @NotNull
  @Override
  public AnvilWindow onClick(@Nullable Consumer<AnvilWindowClickEvent> listener) {
    this.clickListener = listener;
    return this;
  }

  @NotNull
  @Override
  public AnvilWindow onInput(@Nullable Consumer<AnvilWindowInputEvent> listener) {
    this.inputListener = listener;
    return this;
  }

  /// WARNING: These methods are only called in ASM, do not use!

  @Nullable
  protected abstract Object getHandle();

  @SuppressWarnings("SameParameterValue")
  protected abstract void setHandle(@Nullable Object handle);

  protected abstract Inventory getInventory();

  // ASM ONLY
  @Deprecated
  public void release() {
    this.openListener = null;
    this.closeListener = null;
    this.clickListener = null;
    this.inputListener = null;
    this.setHandle(null);
  }

  // ASM ONLY
  @Deprecated
  @Nullable
  public AnvilWindowOpenEvent callOpenEvent() {
    if (openListener == null) return null;
    Player viewer = getContainerAnvilViewer(getHandle());
    AnvilWindowOpenEvent event = new AnvilWindowOpenEvent(this, viewer);
    try {
      openListener.accept(event);
    } catch (Throwable t) {
      t.printStackTrace();
      event = null;
    }
    return event;
  }

  // ASM ONLY
  @Deprecated
  @Nullable
  public AnvilWindowCloseEvent callCloseEvent() {
    if (closeListener == null) return null;
    Player viewer = getContainerAnvilViewer(getHandle());
    AnvilWindowCloseEvent event = new AnvilWindowCloseEvent(this, viewer);
    try {
      closeListener.accept(event);
    } catch (Throwable t) {
      t.printStackTrace();
      event = null;
    }
    return event;
  }

  // ASM ONLY
  @Deprecated
  @Nullable
  public AnvilWindowClickEvent callClickEvent(@Nullable AnvilWindow.Slot slot, @Nullable ItemStack stack) {
    if (clickListener == null || slot == null) return null;
    Player viewer = getContainerAnvilViewer(getHandle());
    AnvilWindowClickEvent event = new AnvilWindowClickEvent(this, viewer, slot, stack);
    try {
      clickListener.accept(event);
    } catch (Throwable t) {
      t.printStackTrace();
      event = null;
    }
    return event;
  }

  // ASM ONLY
  @Deprecated
  @Nullable
  public AnvilWindowInputEvent callInputEvent(@Nullable String value) {
    if (inputListener == null || value == null) return null;
    Player viewer = getContainerAnvilViewer(getHandle());
    AnvilWindowInputEvent event = new AnvilWindowInputEvent(this, viewer, value);
    try {
      inputListener.accept(event);
    } catch (Throwable t) {
      t.printStackTrace();
      event = null;
    }
    return event;
  }

  /// Internal

  private final static String NAME_LDK = "LDK";
  private final static String NAME_FAKE = "LDKAnvilInternalFakePlugin";
  private final static AtomicBoolean REGISTERED = new AtomicBoolean(false);

  private static void safelyRegistryListenerHandler() {
    if (!REGISTERED.compareAndSet(false, true)) return;

    Plugin plugin = Bukkit.getPluginManager().getPlugin(NAME_LDK);
    if (plugin == null) plugin = new FakePlugin(NAME_FAKE);
    RegisteredListener listener = new RegisteredListener(new Listener() {}, (owner, evt) -> {
      if (evt instanceof InventoryClickEvent) {
        InventoryClickEvent target = (InventoryClickEvent) evt;
        AnvilWindowBase anvil = getInventoryAnvilWindow(target.getView());
        if (anvil == null) return;

        if ((target.getInventory().getType() != InventoryType.ANVIL ||
          !target.getInventory().equals(anvil.getInventory())) && !anvil.isAllowMove()) {
          target.setCancelled(true);
          target.setResult(Event.Result.DENY);
        } else {
          AnvilWindow.Slot slot;
          switch (target.getRawSlot()) {
            case 0: slot = Slot.INPUT_LEFT; break;
            case 1: slot = Slot.INPUT_RIGHT; break;
            case 2: slot = Slot.OUTPUT; break;
            default: slot = null; break;
          }
          AnvilWindowClickEvent clickEvent = anvil.callClickEvent(slot, target.getCurrentItem());
          if ((clickEvent != null && clickEvent.isCancelled()) || !anvil.isAllowMove()) {
            target.setCancelled(true);
            target.setResult(Event.Result.DENY);
          }
        }
      } else if (evt instanceof PluginDisableEvent) {
        Plugin target = ((PluginDisableEvent) evt).getPlugin();
        for (Player player : Bukkit.getOnlinePlayers()) {
          AnvilWindow anvil = getInventoryAnvilWindow(player.getOpenInventory());
          if (anvil != null && (target.getName().equals(NAME_LDK) || target.equals(anvil.getPlugin())))
            player.closeInventory();
        }
      }
    }, EventPriority.MONITOR, plugin, false);
    InventoryClickEvent.getHandlerList().register(listener);
    PluginDisableEvent.getHandlerList().register(listener);
  }

  @NotNull final static Class<?> CLASS_CONTAINER;
  @NotNull final static Class<?> CLASS_CONTAINER_ANVIL;
  @Nullable final static Class<?> CLASS_CONTAINER_ABSTRACT;
  @NotNull final static Class<?> CLASS_CRAFT_INVENTORY_VIEW;
  @NotNull final static Class<?> CLASS_ENTITY;
  @NotNull final static Class<?> CLASS_ENTITY_HUMAN;

  @NotNull final static List<Class<?>> ASM_CLASSES;
  @NotNull final static Class<?> ASM_CLASS_ANVIL_WINDOW_CONTAINER_IMPL;
  @NotNull final static ConstructorAccessor<?> ASM_CONSTRUCTOR_ANVIL_WINDOW_IMPL;

  static {
    try {
      CLASS_CONTAINER = getMinecraftClass("Container");
      CLASS_CONTAINER_ANVIL = getMinecraftClass( "ContainerAnvil");
      Class<?> containerAnvilAbstract;
      try {
        containerAnvilAbstract = getMinecraftClassOrNull("ContainerAnvilAbstract");
      } catch (NullPointerException e) {
        // CatServer 1.12.2 NullPointerException. wtf?
        containerAnvilAbstract = null;
      }
      CLASS_CONTAINER_ABSTRACT = containerAnvilAbstract;
      CLASS_ENTITY = getMinecraftClass("Entity");
      CLASS_ENTITY_HUMAN = getMinecraftClass("EntityHuman");
      CLASS_CRAFT_INVENTORY_VIEW = getCraftBukkitClass("inventory.CraftInventoryView");

      ASM_CLASSES = ASMClassLoader.INSTANCE.defineClasses(AnvilWindowGenerator.generate());
      ASM_CLASS_ANVIL_WINDOW_CONTAINER_IMPL = ASM_CLASSES.get(0);
      ASM_CONSTRUCTOR_ANVIL_WINDOW_IMPL = FuzzyReflection
        .of(ASM_CLASSES.get(ASM_CLASSES.size() - 1), true)
        .useConstructorMatcher()
        .withType(Plugin.class)
        .resultAccessor();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Classes does not exist:", e);
    } catch (NoSuchElementException e) {
      throw new RuntimeException("Error while find structure members:", e);
    } catch (Throwable t) {
      throw new RuntimeException("Internal error:", t);
    }
  }

  // LDK.bukkit.anvil.AnvilWindowImpl$ContainerImpl -> public LDK.bukkit.anvil.AnvilWindowBase getAnvilWindow()
  @NotNull final static Supplier<MethodAccessor<Object, AnvilWindowBase>> METHOD_ANVIL_WINDOW_CONTAINER_IMPL_GET_ANVIL_WINDOW
    = FuzzyReflection.lazySupplier(ASM_CLASS_ANVIL_WINDOW_CONTAINER_IMPL, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withType(AnvilWindowBase.class)
    .withName("getAnvilWindow")
    .resultAccessorAs());

  // OBC.inventory.CraftInventoryView -> public NMS.Container getHandle()
  @NotNull final static Supplier<MethodAccessor<Object, Object>> METHOD_CRAFT_INVENTORY_VIEW_GET_HANDLE
    = FuzzyReflection.lazySupplier(CLASS_CRAFT_INVENTORY_VIEW, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withType(CLASS_CONTAINER)
    .withArgsCount(0)
    .resultAccessor("Missing match: OBC.inventory.CraftInventoryView -> Method: public NMS.Container getHandle()"));

  // NMS.Entity -> public OBC.CraftEntity getBukkitEntity()
  @NotNull final static Supplier<MethodAccessor<Object, Entity>> METHOD_ENTITY_GET_BUKKIT_ENTITY
    = FuzzyReflection.lazySupplier(CLASS_ENTITY, true, fuzzy -> fuzzy
    .useMethodMatcher()
    .withModifiers(Modifier.PUBLIC)
    .withoutModifiers(Modifier.STATIC)
    .withName("getBukkitEntity")
    .resultAccessorAs("Missing match: NMS.Entity -> Method: public OBC.entity.CraftEntity getBukkitEntity()"));

  // 1.16 before    -> NMS.ContainerAnvil -> private final NMS.EntityHuman player
  // 1.16 and after -> NMS.ContainerAnvilAbstract -> protected final NMS.EntityHuman player
  @NotNull final static Supplier<FieldAccessor<Object, Object>> FIELD_CONTAINER_ANVIL_PLAYER
    = FuzzyReflection.lazySupplier(CLASS_CONTAINER_ABSTRACT != null
    ? CLASS_CONTAINER_ABSTRACT : CLASS_CONTAINER_ANVIL, true, fuzzy -> fuzzy
    .useFieldMatcher()
    .withoutModifiers(Modifier.STATIC)
    .withType(CLASS_ENTITY_HUMAN)
    .resultAccessor("Missing match: NMS." + CLASS_CONTAINER_ANVIL.getSimpleName() + " -> Field: NMS.EntityHuman player"));

  /** INTERNAL ONLY */
  @NotNull
  @Deprecated
  static AnvilWindow newInstance(Plugin plugin) {
    Object inst = ASM_CONSTRUCTOR_ANVIL_WINDOW_IMPL.newInstance(plugin);
    return (AnvilWindowBase) inst;
  }

  @NotNull
  @SuppressWarnings("ConstantConditions")
  private static Player getContainerAnvilViewer(@Nullable Object handle) {
    Object human = FIELD_CONTAINER_ANVIL_PLAYER.get().get(handle);
    return (Player) METHOD_ENTITY_GET_BUKKIT_ENTITY.get().invoke(human);
  }

  @Nullable
  private static AnvilWindowBase getInventoryAnvilWindow(InventoryView view) {
    if (view.getTopInventory().getType() != InventoryType.ANVIL) return null;
    Object container = METHOD_CRAFT_INVENTORY_VIEW_GET_HANDLE.get().invoke(view);
    return ASM_CLASS_ANVIL_WINDOW_CONTAINER_IMPL.isInstance(container)
      ? METHOD_ANVIL_WINDOW_CONTAINER_IMPL_GET_ANVIL_WINDOW.get().invoke(container)
      : null;
  }
}
