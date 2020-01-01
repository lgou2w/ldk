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

package com.lgou2w.ldk.reflect

import java.lang.reflect.Method

/**
 * ## AccessorMethod (函数访问器)
 *
 * @see [Accessor]
 * @see [Method]
 * @see [Accessors.ofMethod]
 * @author lgou2w
 */
interface AccessorMethod<T, R> : Accessor<Method> {

  /**
   * * Invoke this method from the given [instance] object with the given [params].
   * * 从给定的实例对象 [instance] 以给定的参数 [params] 调用此函数.
   *
   * @param instance Instance object
   * @param instance 实例对象
   * @param params Parameters
   * @param params 参数
   * @throws RuntimeException Throws an exception if a method or internal error cannot be used.
   * @throws RuntimeException 如果无法使用函数或内部错误则抛出异常.
   * @see [Method.invoke]
   */
  fun invoke(instance: T?, vararg params: Any?): R?
}
