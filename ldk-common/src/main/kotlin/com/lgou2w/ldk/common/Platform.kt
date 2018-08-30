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

package com.lgou2w.ldk.common

enum class Platform(
        val bit: String
) : Valuable<String> {

    BIT_32("32"),
    BIT_64("64"),
    UNKNOWN("unknown"),
    ;

    override val value : String
        get() = bit

    companion object {

        @JvmField
        val IS_64_BIT = OperatingSystem.ARCHITECTURE.contains("64")

        @JvmField
        val CURRENT = if (IS_64_BIT) BIT_64 else BIT_32
    }
}
