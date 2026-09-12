package elieoko.mobile.luka

import androidx.compose.ui.window.ComposeUIViewController
import elieoko.mobile.luka.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { App() }
}
