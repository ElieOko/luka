package elieoko.mobile.luka.core

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberShareAction(): (title: String, text: String) -> ShareOutcome {
    val context = LocalContext.current
    return remember(context) {
        { title, text ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(intent, "Partager l’article"))
            ShareOutcome(copiedToClipboard = false)
        }
    }
}
