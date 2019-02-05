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

package com.lgou2w.ldk.bukkit.version

import org.bukkit.Bukkit
import org.bukkit.Server
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Bukkit::class, Server::class)
class MinecraftVersionTest {

    companion object {
        const val VERSION = "1.13.0"
        const val SERVER_VERSION = "(MC $VERSION)"
    }

    @Before
    fun init() {
        PowerMockito.mockStatic(Bukkit::class.java)
        PowerMockito.`when`(Bukkit.getServer()).thenReturn(PowerMockito.mock(Server::class.java))
        PowerMockito.`when`(Bukkit.getServer().version).thenReturn(SERVER_VERSION)
    }

    @Test
    fun testCurrentVersion() {
        val current = MinecraftVersion.CURRENT
        assertEquals(VERSION, current.version)
        assertEquals(MinecraftVersion.V1_13, current)
    }
}
