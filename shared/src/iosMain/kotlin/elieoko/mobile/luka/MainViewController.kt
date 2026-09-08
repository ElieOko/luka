package elieoko.mobile.luka

import androidx.compose.ui.window.ComposeUIViewController
import elieoko.mobile.luka.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}
