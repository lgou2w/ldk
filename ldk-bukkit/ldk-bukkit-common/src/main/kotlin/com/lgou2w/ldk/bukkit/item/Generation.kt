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

package com.lgou2w.ldk.bukkit.item

import com.lgou2w.ldk.common.Valuable

/**
 * ## Generation
 *
 * * Represents the generation (or level of copying) of a written book
 *
 * @author lgou2w
 */
enum class Generation : Valuable<Int> {

    /**
     * Book written into a book-and-quill. Can be copied. (Default value)
     */
    ORIGINAL,
    /**
     * Book that was copied from an original. Can be copied.
     */
    COPY_OF_ORIGINAL,
    /**
     * Book that was copied from a copy of an original. Can't be copied.
     */
    COPY_OF_COPY,
    /**
     * Unused; unobtainable by players. Can't be copied.
     */
    TATTERED,
    ;

    override val value: Int
        get() = ordinal
}