package elieoko.mobile.luka.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import elieoko.mobile.luka.core.CvDocument
import elieoko.mobile.luka.core.isSupportedCv
import elieoko.mobile.luka.core.mimeForCv
import java.awt.FileDialog
import java.awt.Frame
import java.io.FilenameFilter

@Composable
actual fun rememberCvPicker(onPicked: (CvDocument) -> Unit): () -> Unit = remember(onPicked) {
    {
        val dialog = FileDialog(Frame(), "CV Luka — PDF, DOC, DOCX", FileDialog.LOAD)
        dialog.filenameFilter = FilenameFilter { _, name -> isSupportedCv(name) }
        dialog.isVisible = true
        val file = dialog.file
        if (!file.isNullOrBlank() && isSupportedCv(file)) {
            onPicked(CvDocument(file, mimeForCv(file)))
        }
    }
}
