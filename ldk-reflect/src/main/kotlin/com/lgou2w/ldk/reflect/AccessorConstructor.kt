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

/**
 * ## AccessorConstructor (构造访问器)
 *
 * @see [Accessor]
 * @see [Constructor]
 * @see [Accessors.ofConstructor]
 * @author lgou2w
 */
interface AccessorConstructor<T> : Accessor<Constructor<T>> {

  /**
   * * Create an instance object with the given [params].
   * * 以给定的参数 [params] 创建一个实例对象.
   *
   * @param params Parameters
   * @param params 参数
   * @throws RuntimeException Throws an exception if it cannot be used, an internal error, or an instance error.
   * @throws RuntimeException 如果无法使用、内部错误或实例错误时抛出异常.
   * @see [Constructor.newInstance]
   */
  @Throws(RuntimeException::class)
  fun newInstance(vararg params: Any?): T
}
