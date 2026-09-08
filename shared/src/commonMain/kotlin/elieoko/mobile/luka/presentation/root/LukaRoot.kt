package elieoko.mobile.luka.presentation.root

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import elieoko.mobile.luka.domain.model.AppDestination
import elieoko.mobile.luka.presentation.auth.AuthScreen
import elieoko.mobile.luka.presentation.setup.AnalysisScreen
import elieoko.mobile.luka.presentation.setup.LocationScreen
import elieoko.mobile.luka.presentation.setup.ProfessionScreen
import elieoko.mobile.luka.presentation.shell.MainShell
import elieoko.mobile.luka.presentation.theme.LukaCream
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.welcome.WelcomeScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LukaRoot(viewModel: RootViewModel = koinViewModel()) {
    val destination by viewModel.destination.collectAsState()
    var welcomeConsumed by rememberSaveable { mutableStateOf(false) }

    AnimatedContent(
        targetState = destination to welcomeConsumed,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "root",
        modifier = Modifier.fillMaxSize().background(LukaCream),
    ) { (dest, consumed) ->
        when {
            dest == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = LukaRed)
            }
            dest == AppDestination.Welcome && !consumed -> WelcomeScreen(onStart = { welcomeConsumed = true })
            dest == AppDestination.Welcome -> AuthScreen()
            dest == AppDestination.Auth -> AuthScreen()
            dest == AppDestination.Profession -> ProfessionScreen()
            dest == AppDestination.Location -> LocationScreen()
            dest == AppDestination.Analysis -> AnalysisScreen()
            dest == AppDestination.Home -> MainShell()
        }
    }
}
