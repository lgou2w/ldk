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

@CommandRoot("sample", aliases = ["sp"])
@Permission("sample")
class Sample : Initializable {

    override fun initialize(command: RegisteredCommand, manager: CommandManager) {
        command.prefix = "[${command.name.capitalize()}] "
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

    @Command("sample")
    fun sample(sender: CommandSender) {
        // => /sample
        sender.sendMessage("invoke sample")
    }

    @Command("hello", aliases = ["hi", "nh"])
    @Permission("sample.hello")
    fun hello(sender: CommandSender) {
        // => /sample hello
        sender.sendMessage("hello world ~")
    }

    @CommandRoot("user", aliases = ["u"])
    @Permission("sample.user")
    class User {

        @Command("user")
        fun user(sender: CommandSender) {
            // => /sample user
            sender.sendMessage("invoke user")
        }

        @Command("add", aliases = ["a", "tj"])
        @Permission("sample.user.add")
        fun add(sender: CommandSender, username: String, @Optional("123456") password: String) {
            // => /sample user add
            sender.sendMessage("add user => ($username:$password)")
        }

        @Command("remove", aliases = ["r", "yc"])
        @Permission("sample.user.remove")
        fun remove(sender: CommandSender, username: String) {
            // => /sample user remove
            sender.sendMessage("remove user => ($username)")
        }
    }

    @CommandRoot("nbt")
    @Permission("sample.nbt")
    class NBT {

        @Command("type")
        @Permission("sample.nbt.type")
        fun type(sender: CommandSender, type: NBTType) {
            sender.sendMessage("nbt type wrapped => ${type.wrapped.canonicalName}")
        }
    }
}
