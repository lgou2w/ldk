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

package com.lgou2w.ldk.bukkit.cmd

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(CommandHelper::class, RegisteredCommand::class, CommandExecutor::class)
class CommandHelperTest {

    @Before
    fun init() {
        PowerMockito.mockStatic(CommandHelper::class.java)
        PowerMockito.spy(CommandHelper::class.java)
    }

    @Test
    fun testExecutorParameters() {
        val executor = mockExecutor("test", null,
                mockExecutorParameter(0, String::class.java, "username"),
                mockExecutorParameter(1, String::class.java, "password", "123456", true)
        )
        val value = CommandHelper.createSimpleExecutorUsage(executor)
        println(value)
    }

    @Test
    fun testCommand() {
        val children = HashMap<String, RegisteredCommand>()
        val command = mockCommand(null, "sample", null,
                children,
                mapOf(
                        "hello" to mockExecutor("hello", "你好世界."),
                        "tell" to mockExecutor("tell", "给指定目标玩家发送私聊消息.",
                                mockExecutorParameter(0, String::class.java, "target"),
                                mockExecutorParameter(1, List::class.java, "messages", vararg = String::class.java)
                        ),
                        "a" to mockExecutor("a")
                )
        )
        val child = mockCommand(command, "child", "子命令.", emptyMap(), mapOf(
                "a" to mockExecutor("a", "子命令 A 执行器."),
                "b" to mockExecutor("b", "子命令 B 执行器.")
        ))
        children["child"] = child
        println("Page 1")
        CommandHelper.createSimpleCommandTooltip(command, 0, 2).forEach { println(it) }
        println()
        println("Page 2")
        CommandHelper.createSimpleCommandTooltip(command, 1, 2).forEach { println(it) }
    }

    private fun mockCommand(
            parent: RegisteredCommand?,
            name: String,
            description: String?,
            children: Map<String, RegisteredCommand>,
            executors: Map<String, CommandExecutor>
    ): RegisteredCommand {
        val command = PowerMockito.mock(RegisteredCommand::class.java)
        PowerMockito.`when`(command.name).thenReturn(name)
        PowerMockito.`when`(command.parent).thenReturn(parent)
        PowerMockito.`when`(command.children).thenReturn(children)
        PowerMockito.`when`(command.executors).thenReturn(executors)
        PowerMockito.`when`(command.description).thenReturn(description)
        return command
    }

    private fun mockExecutor(name: String, description: String? = null, vararg parameters: CommandExecutor.Parameter): CommandExecutor {
        val executor = PowerMockito.mock(CommandExecutor::class.java)
        PowerMockito.`when`(executor.name).thenReturn(name)
        PowerMockito.`when`(executor.description).thenReturn(description)
        PowerMockito.`when`(executor.parameters).thenReturn(parameters)
        return executor
    }

    private fun mockExecutorParameter(
            index: Int,
            type: Class<*>,
            name: String? = null,
            defValue: String? = null,
            isNullable: Boolean = false,
            isPlayerName: Boolean = false,
            vararg: Class<*>? = null
    ): CommandExecutor.Parameter {
        val parameter = PowerMockito.mock(CommandExecutor.Parameter::class.java)
        PowerMockito.`when`(parameter.index).thenReturn(index)
        PowerMockito.`when`<Class<*>>(parameter, PowerMockito.method(CommandExecutor.Parameter::class.java, "getType"))
            .withNoArguments()
            .thenReturn(type)
        PowerMockito.`when`(parameter.name).thenReturn(name)
        PowerMockito.`when`(parameter.defValue).thenReturn(defValue)
        PowerMockito.`when`(parameter.isNullable).thenReturn(isNullable)
        PowerMockito.`when`(parameter.isPlayerName).thenReturn(isPlayerName)
        PowerMockito.`when`<Class<*>>(parameter, PowerMockito.method(CommandExecutor.Parameter::class.java, "getVararg"))
            .withNoArguments()
            .thenReturn(vararg)
        PowerMockito.`when`(parameter.canNullable).thenReturn(defValue != null || isNullable)
        return parameter
    }
}
