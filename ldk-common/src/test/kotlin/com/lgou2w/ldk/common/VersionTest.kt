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

import org.junit.Assert
import org.junit.Test

class VersionTest {

    @Test
    fun testEquals() {
        val v1 = Version(1, 0, 0)
        val v2 = Version(2, 0, 0)
        Assert.assertTrue(v2 > v1)
        Assert.assertNotEquals(v1, v2)
    }

    @Test
    fun testParse() {
        val ver = "1.0.0"
        val v = Version.parse(ver)
        Assert.assertEquals(ver, v.version)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testParseError() {
        val ver = "error version"
        Version.parse(ver)
    }
}
