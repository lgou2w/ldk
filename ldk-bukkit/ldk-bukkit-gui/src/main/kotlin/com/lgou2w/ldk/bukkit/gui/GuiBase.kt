/*
 * Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>
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

package com.lgou2w.ldk.bukkit.gui

import com.lgou2w.ldk.bukkit.internal.FakePlugin
import com.lgou2w.ldk.common.Constants
import com.lgou2w.ldk.common.Consumer
import com.lgou2w.ldk.common.notNull
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredListener
import java.util.Collections
import java.util.Hashtable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * ## GuiBase (界面基础)
 *
 * @see [Gui]
 * @author lgou2w
 */
abstract class GuiBase : Gui {

  final override val plugin : Plugin
  final override var parent : Gui? = null
  final override val type : GuiType
  final override val title : String
  final override val size : Int
  final override fun hasParent(): Boolean {
    return parent != null
  }

  /**
   * @since LDK 0.1.8-rc2
   */
  @JvmOverloads
  @Suppress("ConvertSecondaryConstructorToPrimary", "LeakingThis")
  constructor(plugin: Plugin, type: GuiType, title: String = type.title) {
    this.plugin = plugin
    this.type = type
    this.title = title
    this.inventory = type.createInventory(this, title)
    this.size = inventory.size
    safeRegisterHandlerListener()
  }

  private val inventory : Inventory
  final override fun getInventory(): Inventory {
    return inventory
  }

  override fun open(human: HumanEntity) {
    human.openInventory(inventory)
  }

  override fun iterator(): Iterator<ItemStack> {
    return inventory.iterator()
  }

  /**************************************************************************
   *
   * Controller
   *
   **************************************************************************/

  override var isAllowMove : Boolean = true

  final override var onOpened : ((gui: Gui, event: InventoryOpenEvent) -> Unit)? = null
  final override var onClosed : ((gui: Gui, event: InventoryCloseEvent) -> Unit)? = null
  final override var onClicked : ((gui: Gui, event: InventoryClickEvent) -> Unit)? = null

  /**************************************************************************
   *
   * Properties Holder
   *
   **************************************************************************/

  private val _properties = Hashtable<String, Any>()
  final override val properties : Map<String, Any>
    get() = synchronized (_properties) {
      Collections.unmodifiableMap(_properties)
    }

  override fun setProperty(key: String, value: Any): Any? {
    synchronized (_properties) {
      return _properties.put(key, value)
    }
  }

  override fun getProperty(key: String, def: Any?): Any? {
    synchronized (_properties) {
      return _properties[key] ?: def
    }
  }

  override fun getProperty(key: String): Any?
    = getProperty(key, null)

  override fun <T> getPropertyAs(key: String, def: T?): T? {
    val value = getProperty(key, def) ?: return null
    try {
      @Suppress("UNCHECKED_CAST")
      return value as? T
    } catch (e: ClassCastException) {
      throw IllegalStateException("The value $value of the property $key does not match the expected.")
    }
  }

  override fun <T> getPropertyAs(key: String): T?
    = getPropertyAs(key, null)

  override fun <T> getPropertyAsNotNull(key: String, def: T): T
    = getPropertyAs(key, def).notNull("The property $key does not have a valid value.")

  override fun <T> getPropertyAsNotNull(key: String): T
    = getPropertyAs<T>(key, null).notNull("The property $key does not have a valid value.")

  override fun containsProperty(key: String): Boolean {
    synchronized (_properties) {
      return _properties.containsKey(key)
    }
  }

  override fun removeProperty(key: String): Any? {
    synchronized (_properties) {
      return _properties.remove(key)
    }
  }

  override fun clearProperties() {
    synchronized (_properties) {
      _properties.clear()
    }
  }

  /**************************************************************************
   *
   * Button Implement
   *
   **************************************************************************/

  private val _buttons : MutableList<Button> = ArrayList()
  final override val buttons : List<Button>
    get() = synchronized (_buttons) {
      Collections.unmodifiableList(_buttons)
    }

  override val buttonSize : Int
    get() = synchronized (_buttons) {
      _buttons.size
    }

  /**
   * * Add the given button to the list. if need to verify the [canAdd0] function should be overridden.
   * * 将给定的按钮添加到列表, 如果需要验证应重写 [canAdd0] 函数.
   *
   * @since LDK 0.1.8-rc2
   */
  @Suppress("MemberVisibilityCanBePrivate")
  protected fun <T : Button> addButton0(button: T): T {
    canAddBasic(this, button)
    canAdd0(button)
    synchronized (_buttons) {
      _buttons.add(button)
      return button
    }
  }

  /**
   * * Verify that the button can be added to the list. If not, a suitable exception should be thrown.
   * * 验证按钮是否可以添加到列表. 如不能, 应当抛出适合的异常.
   *
   * @since LDK 0.1.8-rc2
   */
  protected open fun <T : Button> canAdd0(button: T) {
  }

  /**
   * * Get the button object from the given [index]. Note the list synchronization problem.
   * * 从给定的索引 [index] 获取按钮对象. 注意列表同步问题.
   */
  protected open fun getButton0(index: Int): Button? {
    var button = _buttons.find { it.index == index }
    if (button == null)
      button = _buttons.asSequence().filterIsInstance(ButtonSame::class.java).find { it.isSame(index) }
    return button
  }

  /**
   * * Get the next available index value for this Gui button. If not, throws an [IllegalStateException] exception.
   * * 获取此 Gui 按钮的下一个可用索引值. 如果没有则抛出 [IllegalStateException] 异常.
   */
  @Throws(IllegalStateException::class)
  protected open fun nextAvailableIndex(): Int {
    synchronized (_buttons) {
      for (index in 0 until size)
        if (getButton0(index) == null)
          return index
      throw IllegalStateException("Gui can't add more buttons.")
    }
  }

  override fun hasButton(): Boolean {
    synchronized (_buttons) {
      return _buttons.isNotEmpty()
    }
  }

  override fun getButton(index: Int): Button? {
    synchronized (_buttons) {
      return getButton0(index)
    }
  }

  override fun getButton(x: Int, y: Int): Button? {
    return getButton(GuiFactory.coordinateToIndex(x, y))
  }

  override fun isButton(index: Int): Boolean {
    return getButton(index) != null
  }

  override fun isButton(x: Int, y: Int): Boolean {
    return isButton(GuiFactory.coordinateToIndex(x,  y))
  }

  /**
   * * Indicates that the button has been removed from the collection for the final cleanup.
   * * 表示按钮已经从集合中移除, 进行最后的清理工作.
   *
   * @since LDK 0.1.8-rc2
   */
  protected open fun removeButton0(button: Button) {
    button.stack = null
    button.onClicked = null
  }

  override fun removeButton(button: Button): Boolean {
    synchronized (_buttons) {
      return _buttons.remove(button).apply {
        if (this)
          removeButton0(button)
      }
    }
  }

  override fun removeButton(index: Int): Boolean {
    val button = getButton0(index) ?: return true
    return removeButton(button)
  }

  override fun removeButton(x: Int, y: Int): Boolean {
    return removeButton(GuiFactory.coordinateToIndex(x, y))
  }

  override fun removeButtons() {
    synchronized (_buttons) {
      val iterator = _buttons.iterator()
      while (iterator.hasNext()) {
        val next = iterator.next()
        iterator.remove()
        removeButton0(next)
      }
    }
  }

  override fun addButton(): Button {
    val index = nextAvailableIndex()
    return setButton(index)
  }

  override fun addButton(stack: ItemStack?): Button {
    return addButton(stack, null)
  }

  override fun addButton(onClicked: Consumer<ButtonEvent>?): Button {
    return addButton(null, onClicked)
  }

  override fun addButton(stack: ItemStack?, onClicked: Consumer<ButtonEvent>?): Button {
    val button = addButton()
    button.stack = stack
    button.onClicked = onClicked
    return button
  }

  override fun setButton(index: Int): Button {
    val button = ButtonBase(this, index)
    return addButton0(button)
  }

  override fun setButton(index: Int, stack: ItemStack?): Button {
    return setButton(index, stack, null)
  }

  override fun setButton(index: Int, onClicked: Consumer<ButtonEvent>?): Button {
    return setButton(index, null, onClicked)
  }

  override fun setButton(index: Int, stack: ItemStack?, onClicked: Consumer<ButtonEvent>?): Button {
    val button = setButton(index)
    button.stack = stack
    button.onClicked = onClicked
    return button
  }

  override fun setButton(x: Int, y: Int): Button {
    return setButton(GuiFactory.coordinateToIndex(x, y), null, null)
  }

  override fun setButton(x: Int, y: Int, stack: ItemStack?): Button {
    return setButton(GuiFactory.coordinateToIndex(x, y), stack, null)
  }

  override fun setButton(x: Int, y: Int, onClicked: Consumer<ButtonEvent>?): Button {
    return setButton(GuiFactory.coordinateToIndex(x, y), null, onClicked)
  }

  override fun setButton(x: Int, y: Int, stack: ItemStack?, onClicked: Consumer<ButtonEvent>?): Button {
    return setButton(GuiFactory.coordinateToIndex(x, y), stack, onClicked)
  }

  override fun setSameButton(indexes: IntArray): ButtonSame {
    val button = ButtonSameBase(this, indexes)
    return addButton0(button)
  }

  override fun setSameButton(indexes: IntArray, stack: ItemStack?): ButtonSame {
    return setSameButton(indexes, stack, null)
  }

  override fun setSameButton(indexes: IntArray, onClicked: Consumer<ButtonEvent>?): ButtonSame {
    return setSameButton(indexes, null, onClicked)
  }

  override fun setSameButton(indexes: IntArray, stack: ItemStack?, onClicked: Consumer<ButtonEvent>?): ButtonSame {
    val button = setSameButton(indexes)
    button.stack = stack
    button.onClicked = onClicked
    return button
  }

  override fun hashCode(): Int {
    var result = type.hashCode()
    result = 31 * result + (parent?.hashCode() ?: 0)
    result = 31 * result + inventory.hashCode()
    return result
  }

  override fun equals(other: Any?): Boolean {
    if (other === this)
      return true
    if (other is GuiBase)
      return type == other.type && parent == other.parent && inventory == other.inventory
    if (other is Inventory)
      return inventory == other
    return false
  }

  override fun toString(): String {
    return "Gui(type=$type, title=$title, size=$size, hasParent=${hasParent()})"
  }

  @Suppress("DEPRECATION")
  companion object {

    private const val FAKE_PLUGIN_NAME = "LDKGuiInternalFakePlugin"

    @Throws(IllegalArgumentException::class)
    private fun canAddBasic(gui: Gui, button: Button) {
      val size = gui.size
      if (button.index < 0 || button.index + 1 > size)
        throw IllegalArgumentException("Invalid button index: ${button.index} (should: >= 0 || <= ${size - 1})")
      if (button is ButtonSame) {
        val indexes = button.indexes
        val min = indexes.min()
        val max = indexes.max()
        if ((min != null && min < 0) || (max != null && max > size))
          throw IllegalArgumentException("Invalid button index: ${if (min != null && min < 0) min else max} (should: >= 0 || <= ${size - 1})")
      }
      if (gui.isButton(button.index))
        throw IllegalArgumentException("The current index ${button.index} already has a valid button.")
      var invalid = 0
      if (button is ButtonSame && button.indexes.any { invalid = it; gui.isButton(it) })
        throw IllegalArgumentException("The same button index $invalid already has a valid button.")
    }

    @JvmStatic private val registered = AtomicBoolean(false)
    @JvmStatic private fun safeRegisterHandlerListener() {
      if (!registered.compareAndSet(false, true))
        return
      val ldk : Plugin? = Bukkit.getPluginManager().getPlugin(Constants.LDK)
      val listener = RegisteredListener(object : Listener {}, EventExecutor { _, event ->
        when (event) {
          is InventoryOpenEvent -> {
            val gui = GuiFactory.fromInventory(event.inventory)
            if (gui?.onOpened != null)
              gui.onOpened?.invoke(gui, event)
          }
          is InventoryCloseEvent -> {
            val gui = GuiFactory.fromInventory(event.inventory)
            if (gui?.onClosed != null)
              gui.onClosed?.invoke(gui, event)
          }
          is InventoryClickEvent -> {
            val gui = GuiFactory.fromInventory(event.view.topInventory)
            if (gui != null) {
              if (!gui.isAllowMove)
                event.isCancelled = true
              if (event.rawSlot < gui.size) {
                val button = gui.getButton(event.rawSlot)
                if (button?.onClicked != null) {
                  val buttonEvent = ButtonEvent(button, event.whoClicked, event.currentItem, event)
                  button.onClicked?.invoke(buttonEvent)
                }
              }
              // Processed after the button
              if (gui.onClicked != null)
                gui.onClicked?.invoke(gui, event)
            }
          }
          is PluginDisableEvent -> {
            for (player in Bukkit.getOnlinePlayers()) {
              val gui = player.openInventory.topInventory.holder as? Gui
              if (gui != null && (event.plugin.name == Constants.LDK || event.plugin == gui.plugin))
                player.closeInventory()
            }
          }
        }
      }, EventPriority.MONITOR, ldk ?: FakePlugin(FAKE_PLUGIN_NAME), false)
      InventoryOpenEvent.getHandlerList().register(listener)
      InventoryCloseEvent.getHandlerList().register(listener)
      InventoryClickEvent.getHandlerList().register(listener)
      PluginDisableEvent.getHandlerList().register(listener)
    }
  }
}
