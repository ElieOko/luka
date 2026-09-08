package elieoko.mobile.luka.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import elieoko.mobile.luka.domain.model.HomeFeed
import elieoko.mobile.luka.domain.model.JobOffer
import elieoko.mobile.luka.domain.model.UserProfile
import elieoko.mobile.luka.domain.repository.CatalogRepository
import elieoko.mobile.luka.domain.repository.SessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val profile: UserProfile? = null,
    val feed: HomeFeed? = null,
    val liveOffer: JobOffer? = null,
    val query: String = "",
)

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    sessions: SessionRepository,
    catalog: CatalogRepository,
) : ViewModel() {
    private val query = MutableStateFlow("")
    private val live = MutableStateFlow<JobOffer?>(null)

    val state: StateFlow<HomeUiState> = combine(
        sessions.session.filterNotNull(),
        query,
        live,
    ) { session, q, liveOffer -> Triple(session, q, liveOffer) }
        .flatMapLatest { (session, q, liveOffer) ->
            catalog.observeFeed(session.profile).map { feed ->
                val needle = q.trim()
                val filtered = if (needle.isBlank()) feed else feed.copy(
                    offers = feed.offers.filter {
                        it.title.contains(needle, true) || it.company.contains(needle, true) || it.city.contains(needle, true)
                    },
                    professionals = feed.professionals.filter {
                        it.name.contains(needle, true) || it.title.contains(needle, true)
                    },
                )
                HomeUiState(session.profile, filtered, liveOffer, q)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    init {
        viewModelScope.launch {
            catalog.observeLiveOffers().collect { live.value = it }
        }
    }

    fun onQuery(value: String) {
        query.value = value
    }
}
