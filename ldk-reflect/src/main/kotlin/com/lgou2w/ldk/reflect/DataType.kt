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

package com.lgou2w.ldk.reflect

enum class DataType(
        val primitive: Class<*>,
        val reference: Class<*>
) {

    VOID(java.lang.Void.TYPE, java.lang.Void::class.java),
    BYTE(java.lang.Byte.TYPE, java.lang.Byte::class.java),
    SHORT(java.lang.Short.TYPE, java.lang.Short::class.java),
    INTEGER(java.lang.Integer.TYPE, java.lang.Integer::class.java),
    LONG(java.lang.Long.TYPE, java.lang.Long::class.java),
    CHAR(java.lang.Character.TYPE, java.lang.Character::class.java),
    FLOAT(java.lang.Float.TYPE, java.lang.Float::class.java),
    DOUBLE(java.lang.Double.TYPE, java.lang.Double::class.java),
    BOOLEAN(java.lang.Boolean.TYPE, java.lang.Boolean::class.java),
    ;

    companion object {

        private val CLASS_MAP: MutableMap<Class<*>, DataType> = HashMap()

        init {
            values().forEach {
                CLASS_MAP[it.primitive] = it
                CLASS_MAP[it.reference] = it
            }
        }

        @JvmStatic
        fun fromClass(clazz: Class<*>): DataType? {
            return CLASS_MAP[clazz]
        }

        @JvmStatic
        fun ofPrimitive(clazz: Class<*>): Class<*>
                = fromClass(clazz)?.primitive ?: clazz

        @JvmStatic
        fun ofReference(clazz: Class<*>): Class<*>
                = fromClass(clazz)?.reference ?: clazz

        @JvmStatic
        fun ofPrimitive(clazz: Array<out Class<*>>): Array<Class<*>>
                = clazz.map { ofPrimitive(it) }.toTypedArray()

        @JvmStatic
        fun ofReference(clazz: Array<out Class<*>>): Array<Class<*>>
                = clazz.map { ofReference(it) }.toTypedArray()

        @JvmStatic
        fun compare(left: Array<Class<*>>?, right: Array<Class<*>>?): Boolean {
            if (left === right)
                return true
            if (left != null && right != null && left.size == right.size) {
                (0 until left.size)
                        .filter { left[it] != right[it] && !left[it].isAssignableFrom(right[it]) }
                        .forEach { return false }
                return true
            }
            return left == null && right == null
        }
    }
}
