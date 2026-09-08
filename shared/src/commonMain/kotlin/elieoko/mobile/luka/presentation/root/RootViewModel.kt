package elieoko.mobile.luka.presentation.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import elieoko.mobile.luka.core.CrashReporter
import elieoko.mobile.luka.core.PushNotifier
import elieoko.mobile.luka.domain.model.AppDestination
import elieoko.mobile.luka.domain.repository.SessionRepository
import elieoko.mobile.luka.domain.usecase.ResolveDestinationUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RootViewModel(
    sessions: SessionRepository,
    resolveDestination: ResolveDestinationUseCase,
    crashReporter: CrashReporter,
    pushNotifier: PushNotifier,
) : ViewModel() {
    val destination: StateFlow<AppDestination?> = sessions.session
        .map { resolveDestination(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    init {
        crashReporter.initialize()
        pushNotifier.initialize()
    }
}
