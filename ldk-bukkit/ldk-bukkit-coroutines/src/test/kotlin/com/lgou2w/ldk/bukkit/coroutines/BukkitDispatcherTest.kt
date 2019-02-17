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

package com.lgou2w.ldk.bukkit.coroutines

import kotlinx.coroutines.runBlocking
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Bukkit::class, BukkitScheduler::class, Plugin::class)
class BukkitDispatcherTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    lateinit var server: Server
    @Mock lateinit var scheduler: BukkitScheduler
    @Mock lateinit var plugin: Plugin

    @Before
    fun init() {
        PowerMockito.mockStatic(Bukkit::class.java)
        PowerMockito.`when`(Bukkit.getServer()).thenReturn(server)
        PowerMockito.`when`(Bukkit.getScheduler()).thenReturn(scheduler)
        PowerMockito.`when`(Bukkit.getScheduler().runTask(Mockito.eq(plugin), Mockito.any(Runnable::class.java)))
            .then {
                (it.arguments[1] as Runnable).run()
                PowerMockito.mock(BukkitTask::class.java)
            }
        PowerMockito.`when`(Bukkit.getScheduler().runTaskAsynchronously(Mockito.eq(plugin), Mockito.any(Runnable::class.java)))
            .then {
                (it.arguments[1] as Runnable).run()
                PowerMockito.mock(BukkitTask::class.java)
            }
    }

    @Test
    fun testBukkitSyncDispatcher() {
        mockBukkitIsPrimaryThread(true)
        runBlocking {
            val current = Thread.currentThread().name
            val sync = plugin.withBukkit { Thread.currentThread().name }
            Assert.assertEquals(current, sync)
        }
    }

    @Test
    fun testBukkitAsyncDispatcher() {
        mockBukkitIsPrimaryThread(false)
        runBlocking {
            val current = Thread.currentThread().name
            val async = plugin.withBukkitAsync { Thread.currentThread().name }.await()
            Assert.assertNotEquals(current, async)
        }
    }

    private fun mockBukkitIsPrimaryThread(value: Boolean) {
        PowerMockito.`when`(Bukkit.isPrimaryThread()).thenReturn(value)
        Assert.assertEquals(Bukkit.isPrimaryThread(), value)
    }
}
