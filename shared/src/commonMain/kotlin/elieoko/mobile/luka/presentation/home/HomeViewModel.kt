package elieoko.mobile.luka.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import elieoko.mobile.luka.domain.model.HomeFeed
import elieoko.mobile.luka.domain.model.JobOffer
import elieoko.mobile.luka.domain.model.UserProfile
import elieoko.mobile.luka.domain.repository.CatalogRepository
import elieoko.mobile.luka.domain.repository.SessionRepository
import elieoko.mobile.luka.domain.usecase.OfferFilters
import elieoko.mobile.luka.domain.usecase.applyFilters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val profile: UserProfile? = null,
    val feed: HomeFeed? = null,
    val liveOffer: JobOffer? = null,
    val query: String = "",
    val filters: OfferFilters = OfferFilters(),
    val previewOffers: List<JobOffer> = emptyList(),
    val allOffers: List<JobOffer> = emptyList(),
)

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    sessions: SessionRepository,
    catalog: CatalogRepository,
) : ViewModel() {
    private val query = MutableStateFlow("")
    private val live = MutableStateFlow<JobOffer?>(null)
    private val filters = MutableStateFlow(OfferFilters())

    val state: StateFlow<HomeUiState> = combine(
        sessions.session.filterNotNull(),
        query,
        live,
        filters,
    ) { session, q, liveOffer, currentFilters ->
        arrayOf(session, q, liveOffer, currentFilters)
    }
        .flatMapLatest { args ->
            val session = args[0] as elieoko.mobile.luka.domain.model.UserSession
            val q = args[1] as String
            val liveOffer = args[2] as JobOffer?
            val currentFilters = args[3] as OfferFilters
            catalog.observeFeed(session.profile).map { feed ->
                val needle = q.trim()
                var offers = feed.offers.applyFilters(currentFilters)
                if (needle.isNotBlank()) {
                    offers = offers.filter {
                        it.title.contains(needle, true) || it.company.contains(needle, true) || it.city.contains(needle, true)
                    }
                }
                HomeUiState(
                    profile = session.profile,
                    feed = feed.copy(offers = offers),
                    liveOffer = liveOffer,
                    query = q,
                    filters = currentFilters,
                    previewOffers = offers.take(5),
                    allOffers = offers,
                )
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

    fun onRegion(id: String?) = filters.update { it.copy(regionId = id, city = null) }
    fun onCity(name: String?) = filters.update { it.copy(city = name) }
    fun onProfession(id: String?) = filters.update { it.copy(professionId = id) }
    fun clearFilters() {
        filters.value = OfferFilters()
    }
}
