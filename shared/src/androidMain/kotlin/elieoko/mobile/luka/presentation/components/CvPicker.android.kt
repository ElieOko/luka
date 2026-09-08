package elieoko.mobile.luka.presentation.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import elieoko.mobile.luka.core.CvDocument
import elieoko.mobile.luka.core.isSupportedCv
import elieoko.mobile.luka.core.mimeForCv

@Composable
actual fun rememberCvPicker(onPicked: (CvDocument) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        val name = uri?.lastPathSegment?.substringAfterLast('/').orEmpty()
        if (name.isNotBlank() && isSupportedCv(name)) {
            onPicked(CvDocument(name, mimeForCv(name)))
        } else if (uri != null) {
            onPicked(CvDocument("cv.pdf", "application/pdf"))
        }
    }
    return remember(launcher) {
        {
            launcher.launch(
                arrayOf(
                    "application/pdf",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                ),
            )
        }
    }
}
