package elieoko.mobile.luka.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Composable
actual fun rememberShareAction(): (title: String, text: String) -> ShareOutcome = remember {
    { title, text ->
        val payload = "$title\n\n$text"
        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(payload), null)
        ShareOutcome(copiedToClipboard = true)
    }
}
