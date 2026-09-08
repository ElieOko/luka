package elieoko.mobile.luka.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import elieoko.mobile.luka.core.CvDocument

@Composable
actual fun rememberCvPicker(onPicked: (CvDocument) -> Unit): () -> Unit = remember(onPicked) {
    { onPicked(CvDocument("CV_Luka.pdf", "application/pdf")) }
}
