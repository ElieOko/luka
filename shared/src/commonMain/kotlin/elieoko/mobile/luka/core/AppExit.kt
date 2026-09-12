package elieoko.mobile.luka.core

import androidx.compose.runtime.Composable

@Composable
expect fun AppBackHandler(enabled: Boolean = true, onBack: () -> Unit)

@Composable
expect fun rememberAppExitRequest(): () -> Unit
