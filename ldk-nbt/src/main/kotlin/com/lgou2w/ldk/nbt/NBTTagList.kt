/*
 * Copyright (C) 2018 The lgou2w (lgou2w@hotmail.com)
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

package com.lgou2w.ldk.nbt

import java.io.DataInput
import java.io.DataOutput
import java.util.*

/**
 * ## NBTTagList (集合 NBT 标签)
 *
 * @see [List]
 * @author lgou2w
 */
open class NBTTagList : NBTBase<MutableList<NBTBase<*>>>, MutableList<NBTBase<*>> {

    /**
     * * The NBT tag type within this collection.
     * * 此集合内的 NBT 标签类型.
     *
     * @see [NBTType]
     */
    var elementType = NBTType.TAG_END
        private set

    @JvmOverloads
    constructor(name: String = "", value: MutableList<NBTBase<*>> = LinkedList()) : super(name, value) {
        for (el in value)
            check(el)
    }

    constructor(value: MutableList<NBTBase<*>> = LinkedList()) : super("", value) {
        for (el in value)
            check(el)
    }

    override var value: MutableList<NBTBase<*>>
        get() = ArrayList(super.value0)
        set(value) {
            for (el in value)
                check(el)
            super.value0 = ArrayList(value)
        }

    private fun check(element: NBTBase<*>) {
        if (elementType == NBTType.TAG_END)
            elementType = element.type
        else if (elementType != element.type)
            throw IllegalArgumentException("列表元素值不符合的类型, 应为: $elementType.")
    }

    override val type: NBTType
        get() = NBTType.TAG_LIST

    override fun read(input: DataInput) {
        val elementType = NBTType.fromId(input.readUnsignedByte()) ?: NBTType.TAG_END
        val length = input.readInt()
        this.elementType = elementType
        super.value0 = ArrayList()
        (0 until length).forEach {
            val tag = NBTType.createTag(elementType)
            tag.read(input)
            add(tag)
        }
    }

    override fun write(output: DataOutput) {
        output.writeByte(if (isEmpty()) 0 else elementType.id)
        output.writeInt(size)
        for (el in value0)
            el.write(output)
    }

    override fun clone(): NBTTagList {
        val value = value0.map { it.clone() }.toMutableList()   // clone
        return NBTTagList(name, value)
    }

    override fun hashCode(): Int {
        return super.hashCode() xor elementType.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (super.equals(other) && (other as NBTTagList).elementType == elementType)
            return other.value0 == value0
        return false
    }

    override fun toString(): String {
        return "NBTTagList(${toJson()})"
    }

    override fun toJson(): String {
        return buildString {
            append("[")
            for ((index, el) in value0.withIndex()) {
                if (index > 0)
                    append(",")
                append(el.toJson())
            }
            append("]")
        }
    }

    override fun toMojangson(): String {
        return buildString {
            append("[")
            for ((index, el) in value0.withIndex()) {
                if (index > 0)
                    append(",")
                append(el.toMojangson())
            }
            append("]")
        }
    }

    ///
    //

    /**
     * * Add the given byte [value] to this collection tag.
     * * 将给定的字节值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addByte(value: Byte): Boolean {
        return add(NBTTagByte(value))
    }

    /**
     * * Add the given byte [value] to this collection tag.
     * * 将给定的字节值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addByte(value: Int): Boolean {
        return add(NBTTagByte(value.toByte()))
    }

    /**
     * * Add the given short [value] to this collection tag.
     * * 将给定的短整数值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addShort(value: Short): Boolean {
        return add(NBTTagShort(value))
    }

    /**
     * * Add the given short [value] to this collection tag.
     * * 将给定的短整数值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addShort(value: Int): Boolean {
        return add(NBTTagShort(value.toShort()))
    }

    /**
     * * Add the given int [value] to this collection tag.
     * * 将给定的整数值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addInt(value: Int): Boolean {
        return add(NBTTagInt(value))
    }

    /**
     * * Add the given long [value] to this collection tag.
     * * 将给定的长整数值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addLong(value: Long): Boolean {
        return add(NBTTagLong(value))
    }

    /**
     * * Add the given float [value] to this collection tag.
     * * 将给定的单精度浮点值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addFloat(value: Float): Boolean {
        return add(NBTTagFloat(value))
    }

    /**
     * * Add the given double [value] to this collection tag.
     * * 将给定的双精度浮点值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addDouble(value: Double): Boolean {
        return add(NBTTagDouble(value))
    }

    /**
     * * Add the given byte array [value] to this collection tag.
     * * 将给定的字节数组值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addByteArray(value: ByteArray): Boolean {
        return add(NBTTagByteArray(value))
    }

    /**
     * * Add the given string [value] to this collection tag.
     * * 将给定的字符串值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addString(value: String): Boolean {
        return add(NBTTagString(value))
    }

    /**
     * * Add the given list tag [value] to this collection tag.
     * * 将给定的集合标签值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addList(value: NBTTagList): Boolean {
        return add(value)
    }

    /**
     * * Add the given compound tag [value] to this collection tag.
     * * 将给定的复合标签值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addCompound(value: NBTTagCompound): Boolean {
        return add(value)
    }

    /**
     * * Add the given int array [value] to this collection tag.
     * * 将给定的整数数组值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addIntArray(value: IntArray): Boolean {
        return add(NBTTagIntArray(value))
    }

    /**
     * * Add the given boolean [value] to this collection tag.
     * * 将给定的布尔值 [value] 添加到此集合标签内.
     *
     * @throws IllegalArgumentException If the collection element type and parameter type do not match.
     * @throws IllegalArgumentException 如果集合元素类型和参数类型不匹配.
     */
    fun addBoolean(value: Boolean): Boolean {
        return add(NBTTagByte(if (value) 1.toByte() else 0.toByte()))
    }

    //
    ///

    override fun iterator(): MutableIterator<NBTBase<*>> {
        return value0.iterator()
    }

    override val size: Int
        get() = value0.size

    override fun contains(element: NBTBase<*>): Boolean {
        return value0.contains(element)
    }

    override fun containsAll(elements: Collection<NBTBase<*>>): Boolean {
        return value0.containsAll(elements)
    }

    override fun get(index: Int): NBTBase<*> {
        return value0[index]
    }

    override fun indexOf(element: NBTBase<*>): Int {
        return value0.indexOf(element)
    }

    override fun isEmpty(): Boolean {
        return value0.isEmpty()
    }

    override fun lastIndexOf(element: NBTBase<*>): Int {
        return value0.lastIndexOf(element)
    }

    override fun add(element: NBTBase<*>): Boolean {
        check(element)
        return value0.add(element)
    }

    override fun add(index: Int, element: NBTBase<*>) {
        check(element)
        return value0.add(index, element)
    }

    override fun addAll(index: Int, elements: Collection<NBTBase<*>>): Boolean {
        for (el in elements)
            check(el)
        return value0.addAll(index, elements)
    }

    override fun addAll(elements: Collection<NBTBase<*>>): Boolean {
        for (el in elements)
            check(el)
        return value0.addAll(elements)
    }

    override fun clear() {
        value0.clear()
    }

    override fun listIterator(): MutableListIterator<NBTBase<*>> {
        return value0.listIterator()
    }

    override fun listIterator(index: Int): MutableListIterator<NBTBase<*>> {
        return value0.listIterator(index)
    }

    override fun remove(element: NBTBase<*>): Boolean {
        check(element)
        return value0.remove(element)
    }

    override fun removeAll(elements: Collection<NBTBase<*>>): Boolean {
        for (el in elements)
            check(el)
        return value0.removeAll(elements)
    }

    override fun removeAt(index: Int): NBTBase<*> {
        return value0.removeAt(index)
    }

    override fun retainAll(elements: Collection<NBTBase<*>>): Boolean {
        for (el in elements)
            check(el)
        return value0.retainAll(elements)
    }

    override fun set(index: Int, element: NBTBase<*>): NBTBase<*> {
        check(element)
        return value0.set(index, element)
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<NBTBase<*>> {
        return value0.subList(fromIndex, toIndex)
    }
}
