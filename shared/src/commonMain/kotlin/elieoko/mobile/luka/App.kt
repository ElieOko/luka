package elieoko.mobile.luka

import androidx.compose.runtime.Composable
import elieoko.mobile.luka.presentation.root.LukaRoot
import elieoko.mobile.luka.presentation.theme.LukaTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    LukaTheme {
        LukaRoot()
    }
}
