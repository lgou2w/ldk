/*
 * Copyright (C) 2018 The lgou2w <lgou2w@hotmail.com>
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
import com.lgou2w.ldk.nbt.NBT
import com.lgou2w.ldk.nbt.NBTSavable
import com.lgou2w.ldk.nbt.NBTTagCompound
import org.bukkit.configuration.serialization.ConfigurationSerializable
import java.util.*

/**
 * ## AttributeItemModifier (属性物品修改器)
 *
 * * Add or remove modifiers to the specified item stack.
 * * 从物品栈中添加或移除此修改器.
 *
 * @see [Attribute]
 * @see [NBTSavable]
 * @see [ConfigurationSerializable]
 * @author lgou2w
 * @constructor AttributeItemModifier
 * @param type Attribute type.
 * @param type 属性类型.
 * @param operation Operation mode.
 * @param operation 运算模式.
 * @param slot This modifier takes effect in which slot of the player. If `null` then all slots.
 * @param slot 此修改器在玩家哪个槽位生效. 如果 `null` 则所有槽位.
 * @param amount Operation amount.
 * @param amount 运算数量.
 * @param uuid Unique id.
 * @param uuid 唯一 Id.
 */
data class AttributeItemModifier(
        /**
         * * The type of this modifier.
         * * 此修改器的类型.
         */
        val type: AttributeType,
        /**
         * * The name of this modifier.
         * * 此修改器的名称.
         */
        val name: String = type.name,
        /**
         * * The operation mode of this modifier.
         * * 此修改器的运算模式.
         */
        val operation: Operation,
        /**
         * * The modifier is takes effect in which slot.  If `null` then all slots.
         * * 此修改器的生效槽位. 如果 `null` 则所有槽位.
         */
        val slot: Slot?,
        /**
         * * The operation amount of this modifier.
         * * 此修改器的运算数量.
         */
        val amount: Double,
        /**
         * * The unique id of this modifier.
         * * 此修改器的唯一 Id.
         */
        val uuid: UUID

) : ConfigurationSerializable,
        NBTSavable,
        Comparable<AttributeItemModifier> {

    override fun save(root: NBTTagCompound) : NBTTagCompound {
        root.putString(NBT.TAG_ATTRIBUTE_TYPE, type.value)
        root.putString(NBT.TAG_ATTRIBUTE_NAME, type.value)
        root.putInt(NBT.TAG_ATTRIBUTE_OPERATION, operation.value)
        if (slot != null) root.putString(NBT.TAG_ATTRIBUTE_SLOT, slot.value)
        root.putDouble(NBT.TAG_ATTRIBUTE_AMOUNT, amount)
        root.putLong(NBT.TAG_ATTRIBUTE_UUID_LEAST, uuid.leastSignificantBits)
        root.putLong(NBT.TAG_ATTRIBUTE_UUID_MOST, uuid.mostSignificantBits)
        return root
    }

    override fun compareTo(other: AttributeItemModifier): Int {
        return ComparisonChain.start()
            .compare(type, other.type)
            .compare(name, other.name)
            .compare(operation, other.operation)
            .compare(slot?.ordinal ?: -1, other.slot?.ordinal ?: -1)
            .compare(amount, other.amount)
            .compare(uuid, other.uuid)
            .result
    }

    override fun serialize(): MutableMap<String, Any> {
        val result = LinkedHashMap<String, Any>()
        result["type"] = type.value
        result["name"] = name
        result["operation"] = operation.value
        if(slot != null) result["slot"] = slot.value
        result["amount"] = amount
        result["uuid"] = uuid.toString()
        return result
    }

    companion object {

        /**
         * @see [ConfigurationSerializable]
         */
        @JvmStatic
        fun deserialize(args: Map<String, Any>): AttributeItemModifier {
            val type: AttributeType = Enums.ofValuableNotNull(AttributeType::class.java, args["type"]?.toString())
            val operation: Operation = Enums.ofValuableNotNull(Operation::class.java, args["operation"].toString().toIntOrNull() ?: 0)
            val slot: Slot? = Enums.ofValuable(Slot::class.java, args["slot"]?.toString())
            val amount = args["amount"].toString().toDoubleOrNull() ?: .0
            val uuid = UUID.fromString(args["uuid"]?.toString())
            val name = args["name"]?.toString() ?: type.name
            return AttributeItemModifier(type, name, operation, slot, amount, uuid)
        }
    }
}
