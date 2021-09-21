// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import java.util.*

class Test{
    var str="init"
}
fun main() = application {
    Window(onCloseRequest = { exitApplication() }, state = WindowState()) {
        Box {
            WeatherAndTimeAndLocal()
        }
    }
}
fun MainScreen(){

   // WeatherModel("")
}
