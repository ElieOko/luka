package elieoko.mobile.luka.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import elieoko.mobile.luka.core.AppBackHandler
import elieoko.mobile.luka.core.rememberAppExitRequest
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarHostState
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val ExitWindowMs = 2_000L

@OptIn(ExperimentalTime::class)
@Composable
fun DoubleBackToExit(
    snackbar: SnackbarHostState,
    enabled: Boolean = true,
) {
    val exitApp = rememberAppExitRequest()
    val scope = rememberCoroutineScope()
    var lastBackAt by remember { mutableStateOf(0L) }
    AppBackHandler(enabled = enabled) {
        val now = Clock.System.now().toEpochMilliseconds()
        if (now - lastBackAt < ExitWindowMs) {
            exitApp()
        } else {
            lastBackAt = now
            scope.launch { snackbar.showSnackbar("Appuie encore pour quitter.") }
        }
    }
}
