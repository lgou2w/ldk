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

package com.lgou2w.ldk.reflect

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

/**
 * ## StructureModifier (结构修改器)
 *
 * @author lgou2w
 * @since LDK 0.1.7
 */
open class StructureModifier<T : Any> {

  protected lateinit var subTypeCached : MutableMap<Class<out Any>, StructureModifier<*>>
  protected lateinit var mFields : MutableList<Field>

  lateinit var targetType : Class<out Any>
    private set
  lateinit var fieldType : Class<out Any>
    private set
  lateinit var target : Any
    private set

  /**************************************************************************
   *
   * Initialize
   *
   **************************************************************************/

  protected fun initialize(other: StructureModifier<T>) {
    initialize(
      other.targetType,
      other.fieldType,
      other.mFields,
      other.subTypeCached
    )
  }

  protected fun initialize(
    targetType: Class<out Any>,
    fieldType: Class<out Any>,
    fields: MutableList<Field>,
    subTypeCached: MutableMap<Class<out Any>, StructureModifier<*>>
  ) {
    this.targetType = targetType
    this.fieldType = fieldType
    this.subTypeCached = subTypeCached
    this.mFields = fields
  }

  /**************************************************************************
   *
   * Constructor
   *
   **************************************************************************/

  protected constructor()

  constructor(targetType: Class<out Any>, superclassExclude: Class<*>?) {
    val fields = getFields(targetType, superclassExclude).toMutableList()
    initialize(targetType, Any::class.java, fields, ConcurrentHashMap())
  }

  /**************************************************************************
   *
   * API
   *
   **************************************************************************/

  val size : Int
    get() = mFields.size

  val fields : List<Field>
    get() = Collections.unmodifiableList(ArrayList(mFields))

  fun <R : Any> withType(fieldType: Class<out R>?): StructureModifier<R> {
    if (fieldType == null) {
      return object : StructureModifier<R>() {
        override fun read(index: Int): R? {
          return null
        }
        override fun write(index: Int, value: R?): StructureModifier<R> {
          return this
        }
      }
    }
    var cached = subTypeCached[fieldType]
    if (cached == null) {
      val filtered = mFields
        .filter { field -> fieldType.isAssignableFrom(field.type) }
        .toMutableList()
      cached = withFieldType<R>(fieldType, filtered)
      subTypeCached[fieldType] = cached
    }
    @Suppress("UNCHECKED_CAST")
    return (cached as StructureModifier<R>)
      .withTarget(target)
  }

  protected open fun <R: Any> withFieldType(
    fieldType: Class<out Any>,
    filtered: MutableList<Field>
  ): StructureModifier<R> {
    val sm = StructureModifier<R>()
    sm.initialize(targetType, fieldType, filtered, ConcurrentHashMap())
    return sm
  }

  @Throws(IllegalStateException::class)
  fun withTarget(target: Any): StructureModifier<T> {
    val validate : Any? = target
    if (validate == null)
      throw NullPointerException("The structure modifier does not support modifying static fields.")
    val sm = StructureModifier<T>()
    sm.initialize(this)
    sm.target = target
    return sm
  }

  /**************************************************************************
   *
   * Significant
   *
   **************************************************************************/

  @Throws(RuntimeException::class)
  open fun read(index: Int): T? {
    if (index < 0)
      throw IllegalArgumentException("The field index '$index' cannot be negative.")
    if (mFields.size == 0)
      throw RuntimeException("Class ${targetType.simpleName} does not have a field of type ${fieldType.name}.")
    if (index > mFields.size)
      throw RuntimeException("The index is out of range. (Index: $index, Size: ${mFields.size})")
    return try {
      @Suppress("UNCHECKED_CAST")
      readField(target, mFields[index]) as? T
    } catch (e: IllegalArgumentException) {
      throw RuntimeException("Unable to read field: $fieldType", e)
    } catch (e: IllegalAccessException) {
      throw RuntimeException("Unable to access field.", e)
    }
  }

  @Throws(RuntimeException::class)
  fun readSafely(index: Int): T? {
    return if (index >= 0 && index < mFields.size)
      read(index)
    else
      null
  }

  @Throws(RuntimeException::class)
  open fun write(index: Int, value: T?): StructureModifier<T> {
    if (index < 0)
      throw IllegalArgumentException("The field index '$index' cannot be negative.")
    if (mFields.size == 0)
      throw RuntimeException("Class ${targetType.simpleName} does not have a field of type ${fieldType.name}.")
    if (index > mFields.size)
      throw RuntimeException("The index is out of range. (Index: $index, Size: ${mFields.size})")
    try {
      writeField(target, mFields[index], value)
    } catch (e: IllegalArgumentException) {
      throw RuntimeException("Unable to set the field: The value of $fieldType is: $value", e)
    } catch (e: IllegalAccessException) {
      throw RuntimeException("Unable to access field.", e)
    }
    return this
  }

  @Throws(RuntimeException::class)
  fun writeSafely(index: Int, value: T?): StructureModifier<T> {
    if (index >= 0 && index < mFields.size)
      write(index, value)
    return this
  }

  override fun toString(): String {
    return "StructureModifier(fieldType=$fieldType, fields=$fields)"
  }

  companion object {

    @JvmStatic
    fun <T : Any> of(targetType: Class<out T>, superclassExclude: Class<*>? = null): StructureModifier<T>
      = StructureModifier(targetType, superclassExclude)

    @JvmStatic
    inline fun <reified T : Any> of(superclassExclude: Class<*>? = null): StructureModifier<T>
      = StructureModifier(T::class.java, superclassExclude)

    @JvmStatic
    private fun getFields(targetType: Class<out Any>, superclassExclude: Class<*>?): List<Field> {
      val fields = LinkedHashSet<Field>()
      var current : Class<*>? = targetType
      while (current != null && current != superclassExclude) {
        fields.addAll(current.declaredFields)
        current = current.superclass
      }
      return fields
        .filter { field ->
          !Modifier.isStatic(field.modifiers) &&
            (superclassExclude == null || field.declaringClass != superclassExclude)
        }
    }

    @JvmStatic
    private fun readField(target: Any, field: Field): Any? {
      if (!field.isAccessible)
        field.isAccessible = true
      return field.get(target)
    }

    @JvmStatic
    private fun writeField(target: Any, field: Field, value: Any?) {
      if (!field.isAccessible)
        field.isAccessible = true
      field.set(target, value)
    }
  }
}
