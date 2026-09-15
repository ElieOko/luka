package elieoko.mobile.luka.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow

@Composable
actual fun rememberShareAction(): (title: String, text: String) -> ShareOutcome = remember {
    { title, text ->
        val payload = "$title\n\n$text"
        val controller = UIActivityViewController(
            activityItems = listOf(payload),
            applicationActivities = null,
        )
        val window = UIApplication.sharedApplication.keyWindow
            ?: UIApplication.sharedApplication.windows.mapNotNull { it as? UIWindow }.firstOrNull()
        window?.rootViewController?.presentViewController(controller, animated = true, completion = null)
        ShareOutcome(copiedToClipboard = false)
    }
}
