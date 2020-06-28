/*
 * Copyright (C) 2016-2020 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.bukkit.anvil

import com.lgou2w.ldk.asm.ASMClassLoader
import com.lgou2w.ldk.bukkit.entity.EntityFactory
import com.lgou2w.ldk.bukkit.internal.FakePlugin
import com.lgou2w.ldk.bukkit.reflect.lazyCraftBukkitClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClassOrNull
import com.lgou2w.ldk.common.Constants
import com.lgou2w.ldk.common.Consumer
import com.lgou2w.ldk.common.Enums
import com.lgou2w.ldk.common.notNull
import com.lgou2w.ldk.reflect.AccessorField
import com.lgou2w.ldk.reflect.AccessorMethod
import com.lgou2w.ldk.reflect.FuzzyReflect
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredListener
import java.util.concurrent.atomic.AtomicBoolean

/**
 * ## AnvilWindowBase (铁砧窗口基础)
 *
 * @see [AnvilWindow]
 * @author lgou2w
 * @since LDK 0.1.8-rc
 */
@Suppress("UNUSED") // ASM ONLY
abstract class AnvilWindowBase(
  final override val plugin: Plugin
) : AnvilWindow {

  private var onOpened : Consumer<AnvilWindowOpenEvent>? = null
  private var onClosed : Consumer<AnvilWindowCloseEvent>? = null
  private var onClicked : Consumer<AnvilWindowClickEvent>? = null
  private var onInputted : Consumer<AnvilWindowInputEvent>? = null

  override var isAllowMove : Boolean = true
  override val isOpened : Boolean get() = handle != null

  override fun open(player: Player): AnvilWindow {
    if (isOpened)
      throw IllegalStateException("Anvil window is already opened.")
    safeRegisterListenerHandler()
    return this
  }

  override fun onOpened(block: Consumer<AnvilWindowOpenEvent>?): AnvilWindow {
    this.onOpened = block
    return this
  }
  override fun onClosed(block: Consumer<AnvilWindowCloseEvent>?): AnvilWindow {
    this.onClosed = block
    return this
  }
  override fun onClicked(block: Consumer<AnvilWindowClickEvent>?): AnvilWindow {
    this.onClicked = block
    return this
  }
  override fun onInputted(block: Consumer<AnvilWindowInputEvent>?): AnvilWindow {
    this.onInputted = block
    return this
  }

  override fun getItem(slot: AnvilWindowSlot): ItemStack? {
    if (!isOpened)
      throw IllegalStateException("Anvil window is not opened yet.")
    return inventory.getItem(slot.value)
  }

  override fun setItem(slot: AnvilWindowSlot, stack: ItemStack?): AnvilWindow {
    if (!isOpened)
      throw IllegalStateException("Anvil window is not opened yet.")
    inventory.setItem(slot.value, stack)
    return this
  }

  override fun clearItems(): AnvilWindow {
    if (isOpened)
      inventory.clear() // Only clear when opened
    return this
  }

  /**************************************************************************
   *
   * Implements
   *
   **************************************************************************/

  @Suppress("DEPRECATION")
  companion object {

    private const val FAKE_PLUGIN_NAME = "LDKAnvilInternalFakePlugin"

    @JvmStatic private val CLASS_CONTAINER by lazyMinecraftClass("Container")
    @JvmStatic private val CLASS_CONTAINER_ANVIL by lazyMinecraftClass("ContainerAnvil")
    @JvmStatic private val CLASS_CONTAINER_ANVIL_ABSTRACT by lazyMinecraftClassOrNull("ContainerAnvilAbstract")
    @JvmStatic private val CLASS_CRAFT_INVENTORY_VIEW by lazyCraftBukkitClass("inventory.CraftInventoryView")

    @JvmStatic private val CLASSES by lazy { ASMClassLoader.ofInstance().defineClasses(AnvilWindowImplGenerator.generate()) }
    @JvmStatic private val CLASS_ANVIL_WINDOW_CONTAINER_IMPL by lazy { CLASSES.first() }
    @JvmStatic private val CONSTRUCTOR_ANVIL_WINDOW_IMPL by lazy { CLASSES.last().getConstructor(Plugin::class.java) }

    // LDK.bukkit.anvil.AnvilWindowImpl$ContainerImpl -> public LDK.bukkit.anvil.AnvilWindowBase getAnvilWindow()
    @JvmStatic private val METHOD_ANVIL_WINDOW_CONTAINER_IMPL_GET_ANVIL_WINDOW : AccessorMethod<Any, AnvilWindowBase> by lazy {
      FuzzyReflect.of(CLASS_ANVIL_WINDOW_CONTAINER_IMPL, true)
        .useMethodMatcher()
        .withType(AnvilWindowBase::class.java)
        .withName("getAnvilWindow")
        .resultAccessorAs<Any, AnvilWindowBase>()
    }

    // OBC.inventory.CraftInventoryView -> public NMS.Container getHandle()
    @JvmStatic private val METHOD_INVENTORY_VIEW_GET_HANDLE : AccessorMethod<Any, Any> by lazy {
      FuzzyReflect.of(CLASS_CRAFT_INVENTORY_VIEW, true)
        .useMethodMatcher()
        .withType(CLASS_CONTAINER)
        .withName("getHandle")
        .resultAccessor()
    }

    // 1.16 before -> NMS.ContainerAnvil -> private final NMS.EntityHuman player
    // 1.16 and after ->  NMS.ContainerAnvilAbstract -> protected final NMS.EntityHuman player
    @JvmStatic private val FIELD_CONTAINER_ANVIL_PLAYER : AccessorField<Any, Any> by lazy {
      FuzzyReflect.of(CLASS_CONTAINER_ANVIL_ABSTRACT ?: CLASS_CONTAINER_ANVIL, true)
        .useFieldMatcher()
        .withType(EntityFactory.CLASS_ENTITY_HUMAN)
        .resultAccessor()
    }

    @JvmStatic private fun getContainerAnvilPlayer(handle: Any?): Player {
      val entityHuman = FIELD_CONTAINER_ANVIL_PLAYER[handle]
      return EntityFactory.asBukkitEntity<Player>(entityHuman).notNull()
    }

    @JvmStatic private fun getInventoryAnvilWindow(view: InventoryView): AnvilWindowBase? {
      if (view.topInventory.type != InventoryType.ANVIL) return null // Only as anvil inventory
      val container = METHOD_INVENTORY_VIEW_GET_HANDLE.invoke(view)
      return if (CLASS_ANVIL_WINDOW_CONTAINER_IMPL.isInstance(container)) {
        METHOD_ANVIL_WINDOW_CONTAINER_IMPL_GET_ANVIL_WINDOW.invoke(container)
      } else null
    }

    @JvmStatic private val registered = AtomicBoolean(false)
    @JvmStatic private fun safeRegisterListenerHandler() {
      if (!registered.compareAndSet(false, true))
        return
      val ldk : Plugin? = Bukkit.getPluginManager().getPlugin(Constants.LDK)
      val listener = RegisteredListener(object : Listener {}, EventExecutor { _, event ->
        if (event is InventoryClickEvent) {
          val anvilWindow = getInventoryAnvilWindow(event.view) ?: return@EventExecutor
          if ((event.inventory.type != InventoryType.ANVIL || event.inventory != anvilWindow.inventory) && !anvilWindow.isAllowMove) {
            event.isCancelled = true
            event.result = Event.Result.DENY
          } else {
            val slot = Enums.ofValuable(AnvilWindowSlot::class.java, event.rawSlot)
            val anvilEvent = anvilWindow.callClickedEvent(slot, event.currentItem)
            if ((anvilEvent != null && anvilEvent.isCancelled) || !anvilWindow.isAllowMove) {
              event.isCancelled = true
              event.result = Event.Result.DENY
            }
          }
        } else if (event is PluginDisableEvent) {
          for (player in Bukkit.getOnlinePlayers()) {
            val anvilWindow = getInventoryAnvilWindow(player.openInventory)
            if (anvilWindow != null && (event.plugin.name == Constants.LDK || event.plugin == anvilWindow.plugin))
              player.closeInventory()
          }
        }
      }, EventPriority.MONITOR, ldk ?: FakePlugin(FAKE_PLUGIN_NAME), false)
      InventoryClickEvent.getHandlerList().register(listener)
      PluginDisableEvent.getHandlerList().register(listener)
    }

    /**
     * @see [AnvilWindow.of]
     */
    @JvmStatic
    @Deprecated("INTERNAL ONLY")
    fun of(plugin: Plugin): AnvilWindow {
      val inst = CONSTRUCTOR_ANVIL_WINDOW_IMPL.newInstance(plugin)
      return AnvilWindowBase::class.java.cast(inst)
    }
  }

  protected abstract var handle : Any?
  protected abstract val inventory : Inventory

  ///
  // WARNING: These public methods are only called in ASM, do not use !!!
  ///

  @Deprecated("ASM ONLY")
  fun release() {
    this.onOpened = null
    this.onClosed = null
    this.onClicked = null
    this.onInputted = null
    this.handle = null
  }
  @Deprecated("ASM ONLY")
  fun callOpenedEvent(): AnvilWindowOpenEvent? {
    return if (onOpened == null) null else {
      val player = getContainerAnvilPlayer(handle)
      var event : AnvilWindowOpenEvent? = AnvilWindowOpenEvent(this, player)
      if (event != null) try {
        onOpened?.invoke(event)
      } catch (e: Throwable) {
        e.printStackTrace()
        event = null
      }
      event
    }
  }
  @Deprecated("ASM ONLY")
  fun callClosedEvent(): AnvilWindowCloseEvent? {
    return if (onClosed == null) null else {
      val player = getContainerAnvilPlayer(handle)
      var event : AnvilWindowCloseEvent? = AnvilWindowCloseEvent(this, player)
      if (event != null) try {
        onClosed?.invoke(event)
      } catch (e: Throwable) {
        e.printStackTrace()
        event = null
      }
      event
    }
  }
  @Deprecated("ASM ONLY")
  fun callClickedEvent(slot: AnvilWindowSlot?, clicked: ItemStack?): AnvilWindowClickEvent? {
    return if (slot == null || onClicked == null) null else {
      val player = getContainerAnvilPlayer(handle)
      var event : AnvilWindowClickEvent? = AnvilWindowClickEvent(this, player, slot, clicked)
      if (event != null) try {
        onClicked?.invoke(event)
      } catch (e: Throwable) {
        e.printStackTrace()
        event = null
      }
      event
    }
  }
  @Deprecated("ASM ONLY")
  fun callInputtedEvent(value: String): AnvilWindowInputEvent? {
    return if (onInputted == null) null else {
      val player = getContainerAnvilPlayer(handle)
      var event : AnvilWindowInputEvent? = AnvilWindowInputEvent(this, player, value)
      if (event != null) try {
        onInputted?.invoke(event)
      } catch (e: Throwable) {
        e.printStackTrace()
        event = null
      }
      event
    }
  }
}
