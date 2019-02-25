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

package com.lgou2w.ldk.bukkit.i18n

import com.lgou2w.ldk.i18n.LanguageAdapter
import org.bukkit.configuration.file.YamlConfiguration
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter

class YamlConfigurationAdapter : LanguageAdapter {

    override val fileExtension : String = "yml"

    override fun adapt(input: InputStream): Map<String, String> {
        val reader = InputStreamReader(input, Charsets.UTF_8)
        val yaml = YamlConfiguration.loadConfiguration(reader)
        val keys = yaml.getKeys(false)
        return keys?.associate { it to yaml.get(it).toString()  } ?: LinkedHashMap()
    }

    override fun readapt(output: OutputStream, entries: MutableMap<String, String>) {
        val yaml = YamlConfiguration()
        entries.forEach { yaml.set(it.key, it.value) }
        val data = yaml.saveToString()
        val writer = OutputStreamWriter(output, Charsets.UTF_8)
        writer.write(data)
    }
}
