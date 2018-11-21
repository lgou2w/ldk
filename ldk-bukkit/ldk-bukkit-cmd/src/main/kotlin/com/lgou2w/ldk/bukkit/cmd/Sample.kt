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

import com.lgou2w.ldk.common.Enums
import com.lgou2w.ldk.nbt.NBTType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandRoot("sample", aliases = ["sp"])
@Permission("sample")
@Deprecated("USELESS")
class Sample : StandardCommand() {

    override fun initialize() {
        command.prefix = "[${command.name.capitalize()}] "
        command.isAllowCompletion = true
        command.manager.transforms
            .addTransform(NBTType::class.java) {
                Enums.ofName(NBTType::class.java, it)
            }
        command.manager.completes
            .addCompleter(NBTType::class.java) { _, _, value ->
                NBTType.values().filter { it.name.startsWith(value) }
                    .map { it.name }
            }
    }

    @Command("help")
    @CommandDescription("View sample command help.")
    fun help(sender: CommandSender) {
        val tooltips = CommandHelper.createSimpleCommandTooltips(command, true)
        tooltips.forEach { sender.sendMessage(it) }
    }

    @Command("sample")
    @CommandDescription("Sample command.")
    fun sample(sender: CommandSender) {
        // => /sample
        sender.send("invoke sample")
    }

    @Command("hello", aliases = ["hi", "nh"])
    @Permission("sample.hello")
    @CommandDescription("Test and say Hello world.")
    fun hello(sender: CommandSender) {
        // => /sample hello
        sender.send("hello world ~")
    }

    @Command("tell")
    @CommandDescription("Send a private chat message to the specified target player.")
    fun tell(sender: CommandSender, target: Player, @Optional("hi~") @Vararg(String::class) msgs: List<String>) {
        val msg = msgs.joinToString(" ")
        sender.sendMessage("You said to ${target.name} : $msg")
        target.sendMessage("${sender.name} tell you : $msg")
    }

    @CommandRoot("user", aliases = ["u"])
    @Permission("sample.user")
    @Description(description = "View sample user command.")
    class User : StandardCommand() {

        @Command("user")
        fun user(sender: CommandSender) {
            // => /sample user
            sender.send("invoke user")
        }

        @Command("add", aliases = ["a", "tj"])
        @Permission("sample.user.add")
        fun add(sender: CommandSender,
                @Parameter("username")
                @Playername
                username: String,
                @Parameter("password")
                @Optional("123456")
                password: String
        ) {
            // => /sample user add <username> [password]
            sender.send("add user => ($username:$password)")
        }

        @Command("remove", aliases = ["r", "yc"])
        @Permission("sample.user.remove")
        fun remove(sender: CommandSender,
                   @Parameter("username")
                   username: String
        ) {
            // => /sample user remove <username>
            sender.send("remove user => ($username)")
        }
    }

    @CommandRoot("nbt")
    @Permission("sample.nbt")
    @Description(description = "View sample nbt command.")
    class NBT : StandardCommand() {

        @Command("type")
        @Permission("sample.nbt.type")
        fun type(sender: CommandSender, type: NBTType) {
            // => /sample nbt type <type>
            sender.send("nbt type wrapped => ${type.wrapped.canonicalName}")
        }
    }
}
