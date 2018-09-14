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

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandRoot("sample", description = "A sample of command.")
class Sample : Initializable {

    override fun initialize(manager: CommandManager, registered: RegisteredCommand) {
        // Completing the agent with adaptive commands
        registered.completeProxy = AdaptiveCompleteProxy()
    }

    @Command("test")
    @Permission("sample.test")
    fun test(sender: CommandSender) {
        // -> /sample test
        sender.sendMessage("Sample test")
    }

    @Command("suicide", aliases = ["zs"])
    @Playable   // Can only be executed by the player
    fun suicide(player: Player) {
        // -> /sample [suicide | zs]
        player.health = 0.0
    }

    @Command("kill")
    @Permission("sample.kill")
    fun kill(sender: CommandSender, target: Player) {
        // -> /sample kill <Player>
        target.health = 0.0
        sender.sendMessage("Killed ${target.name} player.")
    }
}
