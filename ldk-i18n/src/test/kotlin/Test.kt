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

import com.lgou2w.ldk.i18n.*
import org.junit.Test
import java.io.File
import java.util.*

class Test {

    @Test
    fun test_LanguageSimple() {
        val adapter = PropertiesAdapter()
        val provider = ResourceProvider()
        val manager = SimpleLanguageManager("language", adapter, provider)
        val language = manager.load(Locale.ROOT)
        language.formatter = MessageFormatter()
        println(language)
        println(language["version"])
        println(language["helloWorld"])
    }

    @Test
    fun test_LanguageDynamic() {
        val adapter = PropertiesAdapter()
        val provider = ResourceProvider()
        val manager = DynamicLanguageManager("language", adapter, provider)
        manager.loadAll(Locale.ROOT, Locale.SIMPLIFIED_CHINESE)
        manager.switch(Locale.ROOT)
        println(manager.dynamic)
        println(manager.dynamicUnsafe["version"])
        println(manager.dynamicUnsafe["helloWorld"])
        manager.switch(Locale.SIMPLIFIED_CHINESE)
        println(manager.dynamic)
        println(manager.dynamicUnsafe["version"])
        println(manager.dynamicUnsafe["helloWorld"])
    }

    @Test
    fun test_LanguageDefault() {
        val adapter = PropertiesAdapter()
        val provider = ResourceExternalizableProvider(File(System.getProperty("user.dir"), "src/test"))
        val manager = SimpleLanguageManager("default", adapter, provider)
        val language = manager.load(Locale.ROOT)
        if (!manager.isValid(language)) {
            language["version"] = "1.0"
            language["helloWorld"] = "HelloWorld!"
            manager.save(language)
            println("初始化.")
        } else {
            println("读取=$language")
            println("version=${language["version"]}")
            println("helloWorld=${language["helloWorld"]}")
        }
    }

    @Test
    fun test_LanguageDefaultDynamic() {
        val adapter = PropertiesAdapter()
        val provider = ResourceExternalizableProvider(File(System.getProperty("user.dir"), "src/test"))
        val manager = DynamicLanguageManager("language", adapter, provider)
        val language = manager.load(Locale.ROOT)
        var updated = false

        if (language["version"] ?: "" < "1.1") {
            // 1.1 版本更新
            language["version"] = "1.1"
            language["1.1"] = "xxxx更新"
            updated = true
        }
        if (language["version"] ?: "" < "1.2") {
            // 1.2 版本更新
            language["version"] = "1.2"
            language["1.2"] = "xxxx更新"
            updated = true
        }

        if (!manager.isValid(language) || updated) {
            manager.save(language)
            println("资源已更新")
        }
        language.entries.forEach { println(it) }
    }
}
