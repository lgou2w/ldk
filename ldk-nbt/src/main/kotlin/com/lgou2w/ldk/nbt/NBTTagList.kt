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
class NBTTagList<E: NBTBase<*>> : NBTBase<MutableList<E>>, MutableList<E> {

    /**
     * * The NBT tag type within this collection.
     * * 此集合内的 NBT 标签类型.
     *
     * @see [NBTType]
     */
    var elementType = NBTType.TAG_END
        private set

    @JvmOverloads
    constructor(name: String = "", value: MutableList<E> = LinkedList()) : super(name, value) {
        for (el in value)
            check(el)
    }

    constructor(value: MutableList<E> = LinkedList()) : super("", value) {
        for (el in value)
            check(el)
    }

    override var value: MutableList<E>
        get() = ArrayList(super.value0)
        set(value) {
            for (el in value)
                check(el)
            super.value0 = ArrayList(value)
        }

    protected fun check(element: E) {
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
            @Suppress("UNCHECKED_CAST")
            add(tag as E)
        }
    }

    override fun write(output: DataOutput) {
        output.writeByte(if (isEmpty()) 0 else elementType.id)
        output.writeInt(size)
        for (el in value0)
            el.write(output)
    }

    override fun clone(): NBTTagList<E> {
        return NBTTagList(name, value)
    }

    override fun hashCode(): Int {
        return super.hashCode() xor elementType.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (super.equals(other) && (other as NBTTagList<*>).elementType == elementType)
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

    // TODO document

    fun addByte(value: Byte): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(NBTTagByte(value) as E)
    }

    fun addByte(value: Int): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(NBTTagByte(value.toByte()) as E)
    }

    fun addShort(value: Short): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(NBTTagShort(value) as E)
    }

    fun addShort(value: Int): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(NBTTagShort(value.toShort()) as E)
    }

    fun addInt(value: Int): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(NBTTagInt(value) as E)
    }

    fun addLong(value: Long): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(NBTTagLong(value) as E)
    }

    fun addFloat(value: Float): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(NBTTagFloat(value) as E)
    }

    fun addDouble(value: Double): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(NBTTagDouble(value) as E)
    }

    fun addByteArray(value: ByteArray): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(NBTTagByteArray(value) as E)
    }

    fun addString(value: String): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(NBTTagString(value) as E)
    }

    fun addList(value: NBTTagList<NBTBase<*>>): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(value as E)
    }

    fun addCompound(value: NBTTagCompound): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(value as E)
    }

    fun addIntArray(value: IntArray): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(NBTTagIntArray(value) as E)
    }

    fun addBoolean(value: Boolean): Boolean {
        @Suppress("UNCHECKED_CAST")
        return add(NBTTagByte(if (value) 1.toByte() else 0.toByte()) as E)
    }

    //
    ///

    override fun iterator(): MutableIterator<E> {
        return value0.iterator()
    }

    override val size: Int
        get() = value0.size

    override fun contains(element: E): Boolean {
        return value0.contains(element)
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        return value0.containsAll(elements)
    }

    override fun get(index: Int): E {
        return value0[index]
    }

    override fun indexOf(element: E): Int {
        return value0.indexOf(element)
    }

    override fun isEmpty(): Boolean {
        return value0.isEmpty()
    }

    override fun lastIndexOf(element: E): Int {
        return value0.lastIndexOf(element)
    }

    override fun add(element: E): Boolean {
        check(element)
        return value0.add(element)
    }

    override fun add(index: Int, element: E) {
        check(element)
        return value0.add(index, element)
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        for (el in elements)
            check(el)
        return value0.addAll(index, elements)
    }

    override fun addAll(elements: Collection<E>): Boolean {
        for (el in elements)
            check(el)
        return value0.addAll(elements)
    }

    override fun clear() {
        value0.clear()
    }

    override fun listIterator(): MutableListIterator<E> {
        return value0.listIterator()
    }

    override fun listIterator(index: Int): MutableListIterator<E> {
        return value0.listIterator(index)
    }

    override fun remove(element: E): Boolean {
        check(element)
        return value0.remove(element)
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        for (el in elements)
            check(el)
        return value0.removeAll(elements)
    }

    override fun removeAt(index: Int): E {
        return value0.removeAt(index)
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        for (el in elements)
            check(el)
        return value0.retainAll(elements)
    }

    override fun set(index: Int, element: E): E {
        check(element)
        return value0.set(index, element)
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> {
        return value0.subList(fromIndex, toIndex)
    }
}
