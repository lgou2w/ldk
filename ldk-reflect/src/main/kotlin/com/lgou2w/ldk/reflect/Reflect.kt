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

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * ## Reflect (反射器)
 *
 * @see [FuzzyReflect]
 * @author lgou2w
 */
interface Reflect {

    /**
     * * Get the reference class for this reflect.
     * * 获取此反射器的引用类.
     */
    val reference : Class<*>

    /**
     * * Gets whether this reflect is in forced access mode.
     * * 获取此反射器是否为强制访问模式.
     */
    val isForceAccess : Boolean

    /**
     * * Set this reflect to force access mode.
     * * 将此反射器设置为强制访问模式.
     */
    fun useForceAccess(): Reflect

    /**
     * * Gets all constructor collections for this reflect reference class.
     * * 获取此反射器引用类的所有构造函数集合.
     */
    val constructors : Set<Constructor<*>>

    /**
     * * Gets all method collections for this reflect reference class.
     * * 获取此反射器引用类的所有构造函数集合.
     */
    val methods : Set<Method>

    /**
     * * Gets all field collections for this reflect reference class.
     * * 获取此反射器引用类的所有构造函数集合.
     */
    val fields : Set<Field>
}
