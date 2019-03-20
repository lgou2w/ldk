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

package com.lgou2w.ldk.reflect

import java.lang.reflect.Field

/**
 * ## AccessorField (字段访问器)
 *
 * @see [Accessor]
 * @see [Field]
 * @see [Accessors.ofField]
 * @author lgou2w
 */
interface AccessorField<T, R> : Accessor<Field> {

    /**
     * * Get the value of this field from the given [instance] object.
     * * 从给定的实例对象 [instance] 获取该字段的值.
     *
     * @param instance Instance object
     * @param instance 实例对象
     * @throws RuntimeException Throws an exception if the field cannot be used or cannot be read.
     * @throws RuntimeException 如果无法使用或无法读取字段则抛出异常.
     * @see [Field.get]
     */
    @Throws(RuntimeException::class)
    operator fun get(instance: T?): R?

    /**
     * * Sets the given value [value] to the field of the given [instance] object.
     * * 将给定的值 [value] 设置到给定实例对象 [instance] 的字段中.
     *
     * @param instance Instance object
     * @param instance 实例对象
     * @param value Value
     * @param value 值
     * @throws RuntimeException Throws an exception if the field cannot be used or cannot be read.
     * @throws RuntimeException 如果无法使用或无法读取字段则抛出异常.
     * @see [Field.set]
     */
    @Throws(RuntimeException::class)
    operator fun set(instance: T?, value: R?)
}
