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

package com.lgou2w.ldk.bukkit.attribute

import com.lgou2w.ldk.common.ComparisonChain
import com.lgou2w.ldk.common.Enums
import com.lgou2w.ldk.common.notNull
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.ConfigurationSerialization
import java.util.LinkedHashMap
import java.util.UUID

/**
 * ## AttributeModifier (属性修改器)
 *
 * * Add or remove this modifier from the attribute.
 * * 从属性中添加或移除此修改器.
 *
 * @see [Attribute]
 * @see [Attribute.addModifier]
 * @see [Attribute.removeModifier]
 * @see [ConfigurationSerializable]
 * @author lgou2w
 */
data class AttributeModifier(
  /**
   * * The name of this modifier.
   * * 此修改器的名字.
   */
  val name: String,
  /**
   * * The operation mode of this modifier.
   * * 此修改器的运算模式.
   *
   * @see [Operation]
   */
  val operation: Operation,
  /**
   * * The operation amount of this modifier.
   * * 此修改器的运算数量.
   */
  val amount: Double,
  /**
   * * The unique id of this modifier.
   * * 此修改器的唯一 Id.
   */
  val uuid: UUID = UUID.randomUUID()
) : ConfigurationSerializable,
  Comparable<AttributeModifier> {

  override fun compareTo(other: AttributeModifier): Int {
    return ComparisonChain.start()
      .compare(name, other.name)
      .compare(operation, other.operation)
      .compare(amount, other.amount)
      .compare(uuid, other.uuid)
      .result
  }

  override fun serialize(): MutableMap<String, Any> {
    val result = LinkedHashMap<String, Any>()
    result["name"] = name
    result["operation"] = operation.value
    result["amount"] = amount
    result["uuid"] = uuid.toString()
    return result
  }

  companion object {

    init {
      ConfigurationSerialization.registerClass(AttributeModifier::class.java)
    }

    /**
     * @see [ConfigurationSerializable]
     */
    @JvmStatic
    fun deserialize(args: Map<String, Any>): AttributeModifier {
      val name: String = args["name"]?.toString().notNull()
      val operation: Operation = Enums.ofValuableNotNull(Operation::class.java, args["operation"].toString().toIntOrNull() ?: 0)
      val amount = args["amount"].toString().toDoubleOrNull() ?: 0.0
      val uuid = UUID.fromString(args["uuid"]?.toString())
      return AttributeModifier(name, operation, amount, uuid)
    }
  }
}
