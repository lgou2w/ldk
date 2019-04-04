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

import com.lgou2w.ldk.bukkit.i18n.YamlConfigurationAdapter
import com.lgou2w.ldk.i18n.ExternalProvider
import com.lgou2w.ldk.i18n.SimpleLanguageManager
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.Locale

class Demo : JavaPlugin() {

    val provider = ExternalProvider(File(dataFolder, "languages"))      //  plugins/name/languages/xxx
    val manager = SimpleLanguageManager("demo", YamlConfigurationAdapter(), provider)

    override fun onEnable() {
        if (!dataFolder.exists())
            dataFolder.mkdirs()
        val language = manager.load(Locale.ROOT)
        if (!manager.isValid(language)) {
            language["version"] = "1.0"
            language["demo"] = "Bukkit YAML i18n Demo."
            language["a.b.c.d"] = "abcdef"
            manager.save(language)
        }
        println("version=${language["version"]}")
        println("demo=${language["demo"]}")
        println("a.b.c.d=${language["a.b.c.d"]}")
    }
}
