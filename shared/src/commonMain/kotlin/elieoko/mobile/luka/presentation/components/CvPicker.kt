package elieoko.mobile.luka.presentation.components

import androidx.compose.runtime.Composable

@Composable
expect fun rememberCvPicker(onPicked: (elieoko.mobile.luka.core.CvDocument) -> Unit): () -> Unit
