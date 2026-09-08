package elieoko.mobile.luka.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import elieoko.mobile.luka.domain.model.HomeFeed
import elieoko.mobile.luka.domain.repository.CatalogRepository
import elieoko.mobile.luka.domain.repository.SessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ExploreViewModel(
    sessions: SessionRepository,
    catalog: CatalogRepository,
) : ViewModel() {
    val feed: StateFlow<HomeFeed?> = sessions.session
        .filterNotNull()
        .flatMapLatest { catalog.observeFeed(it.profile) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
