package elieoko.mobile.luka.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import elieoko.mobile.luka.App
import elieoko.mobile.luka.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Luka — l’emploi vient à toi",
            state = rememberWindowState(width = 420.dp, height = 860.dp),
        ) {
            App()
        }
    }
}
