/*
* Copyright (c) Microsoft Corporation. All rights reserved.
* Licensed under the MIT license.
*/

/*
 * Copyright (C) 2019 The lgou2w <lgou2w@hotmail.com>
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

/*
 *  Modify: Mojang brigadier MojangsonParser and StringReader Java -> Kotlin
 *  by lgou2w on 16/09/2019
 */

import java.util.regex.Pattern

/**
 * @author Mojang AB
 * @author Microsoft
 */
class MojangsonParser private constructor(
        private val reader: StringReader
) {

    companion object {

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun parse(mojangson: String): NBTTagCompound {
            val reader = StringReader(mojangson)
            val parser = MojangsonParser(reader)
            return parser.readSingleStruct()
        }

        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun parseValue(mojangson: String): NBTBase<*> {
            val reader = StringReader(mojangson)
            val parser = MojangsonParser(reader)
            return parser.readValue()
        }

        private val DOUBLE_PATTERN_NOSUFFIX
                = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2)
        private val DOUBLE_PATTERN
                = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2)
        private val FLOAT_PATTERN
                = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2)
        private val BYTE_PATTERN
                = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2)
        private val LONG_PATTERN
                = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2)
        private val SHORT_PATTERN
                = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2)
        private val INT_PATTERN
                = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)")

        /**
         * @author Mojang AB
         * @author Microsoft
         */
        private class StringReader(val string: String) {

            companion object {

                private const val SYNTAX_ESCAPE = '\\'
                private const val SYNTAX_DOUBLE_QUOTE = '"'
                private const val SYNTAX_SINGLE_QUOTE = '\''

                fun isQuotedStringStart(c: Char)
                        = c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE
                fun isAllowedInUnquotedString(c: Char)
                        = c in '0'..'9' || c in 'A'..'Z' || c in 'a'..'z' || c == '_' || c == '-' || c == '.' || c == '+'
            }

            var cursor = 0

            fun canRead(length: Int) = cursor + length <= string.length
            fun canRead() = canRead(1)
            fun peek() = string[cursor]
            fun peek(offset: Int) = string[cursor + offset]
            fun read() = string[cursor++]
            fun skip() { cursor++ }
            fun skipWhitespace() {
                while (canRead() && Character.isWhitespace(peek()))
                    skip()
            }

            fun readUnquotedString(): String {
                val start = cursor
                while (canRead() && isAllowedInUnquotedString(peek()))
                    skip()
                return string.substring(start, cursor)
            }

            fun readQuotedString(): String {
                if (!canRead())
                    return ""
                val next = peek()
                require(isQuotedStringStart(next)) { "Expected quote to start a string" }
                skip()
                return readStringUntil(next)
            }

            fun readStringUntil(terminator: Char): String {
                val result = StringBuilder()
                var escaped = false
                while (canRead()) {
                    val c = read()
                    if (escaped) {
                        if (c == terminator || c == SYNTAX_ESCAPE) {
                            result.append(c)
                            escaped = false
                        } else {
                            cursor -= 1
                            throw IllegalArgumentException("Invalid escape sequence '${c}' in quoted string")
                        }
                    } else if (c == SYNTAX_ESCAPE) {
                        escaped = true
                    } else if (c == terminator) {
                        return result.toString()
                    } else {
                        result.append(c)
                    }
                }
                throw IllegalArgumentException("Unclosed quoted string")
            }

            fun readString(): String {
                if (!canRead())
                    return ""
                val next = peek()
                if (isQuotedStringStart(next)) {
                    skip()
                    return readStringUntil(next)
                }
                return readUnquotedString()
            }

            fun expect(c: Char) {
                require(canRead() && peek() == c) { "Expected '${c}'" }
                skip()
            }

        }
    }

    private fun readSingleStruct(): NBTTagCompound {
        val compound = readStruct()
        this.reader.skipWhitespace()
        require(!this.reader.canRead()) { "Unexpected trailing data" }
        return compound
    }

    private fun readKey(): String {
        this.reader.skipWhitespace()
        require(this.reader.canRead()) { "Expected key" }
        return this.reader.readString()
    }

    private fun readTypedValue(): NBTBase<*> {
        this.reader.skipWhitespace()
        val start = this.reader.cursor
        return if (this.reader.peek() == '"')
            NBTTagString(value = this.reader.readQuotedString())
        else {
            val str = this.reader.readUnquotedString()
            if (str.isEmpty()) {
                this.reader.cursor = start
                throw IllegalArgumentException("Expected value")
            }
            type(str)
        }
    }

    private fun type(str: String): NBTBase<*> {
        try {
            if (FLOAT_PATTERN.matcher(str).matches())
                return NBTTagFloat(str.substring(0, str.length - 1).toFloat())
            if (BYTE_PATTERN.matcher(str).matches())
                return NBTTagByte(str.substring(0, str.length - 1).toByte())
            if (LONG_PATTERN.matcher(str).matches())
                return NBTTagLong(str.substring(0, str.length - 1).toLong())
            if (SHORT_PATTERN.matcher(str).matches())
                return NBTTagShort(str.substring(0, str.length - 1).toShort())
            if (INT_PATTERN.matcher(str).matches())
                return NBTTagInt(str.toInt())
            if (DOUBLE_PATTERN.matcher(str).matches())
                return NBTTagDouble(str.substring(0, str.length - 1).toDouble())
            if (DOUBLE_PATTERN_NOSUFFIX.matcher(str).matches())
                return NBTTagDouble(str.toDouble())
            if (str == "true")
                return NBTTagByte(1)
            if (str == "false")
                return NBTTagByte(0)
        } catch (e: NumberFormatException) {
        }
        return NBTTagString(value = str)
    }

    private fun readValue(): NBTBase<*> {
        this.reader.skipWhitespace()
        require(this.reader.canRead()) { "Expected value" }
        return when (this.reader.peek()) {
            '{' -> readStruct()
            '[' -> readList()
            else -> readTypedValue()
        }
    }

    private fun readList(): NBTBase<*> {
        return if (this.reader.canRead(3) && this.reader.peek(1) != '"' && this.reader.peek(2) == ';')
            readArrayTag()
        else
            readListTag()
    }

    private fun readStruct(): NBTTagCompound {
        this.expect('{')
        val compound = NBTTagCompound()
        this.reader.skipWhitespace()
        while (this.reader.canRead() && this.reader.peek() != '}') {
            val start = this.reader.cursor
            val key = readKey()
            if (key.isEmpty()) {
                this.reader.cursor = start
                throw IllegalArgumentException("Expected key")
            }
            this.expect(':')
            compound[key] = readValue()
            if (!hasElementSeparator())
                break
            require(this.reader.canRead()) { "Expected key" }
        }
        this.expect('}')
        return compound
    }

    private fun readListTag(): NBTBase<*> {
        this.expect('[')
        this.reader.skipWhitespace()
        require(this.reader.canRead()) { "Expected value" }
        val list = NBTTagList()
        var elementTypeId = 0

        while (this.reader.peek() != ']') {
            val start = this.reader.cursor
            val element = readValue()
            if (elementTypeId == 0)
                elementTypeId = element.typeId
            else if (element.typeId != elementTypeId) {
                this.reader.cursor = start
                throw IllegalArgumentException("Can't insert ${element.type} into list of $elementTypeId")
            }
            list.add(element)
            if (!hasElementSeparator())
                break
            require(this.reader.canRead()) { "Expected value" }
        }
        this.expect(']')
        return list
    }

    private fun readArrayTag(): NBTBase<*> {
        this.expect('[')
        val start = this.reader.cursor
        val c = this.reader.read()
        this.reader.read()
        this.reader.skipWhitespace()
        require(this.reader.canRead()) { "Expected value" }
        return when (c) {
            'B' -> {
                val array = readArray<Byte>(NBTType.TAG_BYTE_ARRAY, NBTType.TAG_BYTE)
                NBTTagByteArray(array.toByteArray())
            }
            'L' -> {
                val array = readArray<Long>(NBTType.TAG_LONG_ARRAY, NBTType.TAG_LONG)
                NBTTagLongArray(array.toLongArray())
            }
            'I' -> {
                val array = readArray<Int>(NBTType.TAG_INT_ARRAY, NBTType.TAG_INT)
                NBTTagIntArray(array.toIntArray())
            }
            else -> {
                this.reader.cursor = start
                throw IllegalArgumentException("Invalid array type '${c}'")
            }
        }
    }

    private fun <T : Number> readArray(arrayType: NBTType, elementType: NBTType): List<T> {
        val list : MutableList<Number> = ArrayList()
        while (true) {
            if (this.reader.peek() != ']') {
                val start = this.reader.cursor
                val nbt = readValue()
                if (nbt.type != elementType) {
                    this.reader.cursor = start
                    throw IllegalArgumentException("Can't insert ${nbt.type} into $arrayType")
                }
                when (elementType) {
                    NBTType.TAG_BYTE -> list.add((nbt as NBTTagNumber<*>).value.toByte())
                    NBTType.TAG_LONG -> list.add((nbt as NBTTagNumber<*>).value.toLong())
                    else -> list.add((nbt as NBTTagNumber<*>).value.toInt())
                }
                if (hasElementSeparator()) {
                    require(this.reader.canRead()) { "Expected value" }
                    continue
                }
            }
            this.expect(']')
            @Suppress("UNCHECKED_CAST")
            return list as List<T>
        }
    }

    private fun hasElementSeparator(): Boolean {
        this.reader.skipWhitespace()
        return if (this.reader.canRead() && this.reader.peek() == ',') {
            this.reader.skip()
            this.reader.skipWhitespace()
            true
        } else {
            false
        }
    }

    private fun expect(expected: Char) {
        this.reader.skipWhitespace()
        this.reader.expect(expected)
    }
}
