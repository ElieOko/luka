package elieoko.mobile.luka.core

import androidx.compose.runtime.Composable

@Composable
actual fun AppBackHandler(enabled: Boolean, onBack: () -> Unit) = Unit

@Composable
actual fun rememberAppExitRequest(): () -> Unit = {}
