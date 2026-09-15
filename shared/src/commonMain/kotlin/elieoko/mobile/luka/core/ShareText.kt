package elieoko.mobile.luka.core

import androidx.compose.runtime.Composable

data class ShareOutcome(val copiedToClipboard: Boolean)

@Composable
expect fun rememberShareAction(): (title: String, text: String) -> ShareOutcome
