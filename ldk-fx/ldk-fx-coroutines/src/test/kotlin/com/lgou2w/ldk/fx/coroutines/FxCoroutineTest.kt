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

package com.lgou2w.ldk.fx.coroutines

import com.lgou2w.ldk.coroutines.SimpleCoroutineFactory
import com.lgou2w.ldk.coroutines.SingleThreadDispatcherProvider
import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Test
import java.net.URL
import kotlin.system.exitProcess

class FxCoroutineTest {

    class MyApp : Application() {
        val cfProvider = SingleThreadDispatcherProvider("FxCoroutines")
        val cf = SimpleCoroutineFactory(cfProvider)
        override fun start(primaryStage: Stage) {
            val root = StackPane()
            val valueProperty = SimpleStringProperty("Hello Coroutines!")
            val label = Label().apply { textProperty().bind(valueProperty) }
            val button = Button("Click me")
            button.setOnMouseClicked {
                valueProperty.value = "Async..."
                cf.launch {
                    val text = async { URL("https://github.com/lgou2w").readBytes().size }.await()
                    withFx { valueProperty.value = "Done. Total $text bytes." }
                }
            }
            root.children.add(VBox(label, button).apply {
                alignment = Pos.CENTER
                spacing = 24.0
                padding = Insets(24.0)
                minWidth = 300.0
            })
            primaryStage.scene = Scene(root)
            primaryStage.centerOnScreen()
            primaryStage.show()
        }

        override fun stop() {
            cfProvider.dispatcher.close()
        }
    }

    @Test
    @Ignore
    fun testFxCoroutines() {
        runBlocking {
            Application.launch(MyApp::class.java)
            exitProcess(0)
        }
    }
}
