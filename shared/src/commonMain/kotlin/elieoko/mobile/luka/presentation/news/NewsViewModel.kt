package elieoko.mobile.luka.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import elieoko.mobile.luka.domain.model.LearnerContent
import elieoko.mobile.luka.domain.model.LukaEntitlements
import elieoko.mobile.luka.domain.model.NewsItem
import elieoko.mobile.luka.domain.model.NewsSections
import elieoko.mobile.luka.domain.repository.CatalogRepository
import elieoko.mobile.luka.domain.repository.SessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NewsUiState(
    val articles: List<NewsItem> = LearnerContent.catalog,
    val favorites: Set<String> = emptySet(),
    val unlocked: Boolean = false,
    val tabs: List<String> = NewsSections.tabs(LearnerContent.catalog),
) {
    fun article(id: String?): NewsItem? = articles.firstOrNull { it.id == id }

    fun visible(tab: String): List<NewsItem> = when (tab) {
        NewsSections.FAVORITES -> articles.filter { it.id in favorites && (!it.subscribed || unlocked) }
        NewsSections.SUBSCRIBED -> if (unlocked) articles.filter { it.subscribed } else emptyList()
        else -> articles.filter { !it.subscribed && it.domain == tab }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModel(
    private val sessions: SessionRepository,
    catalog: CatalogRepository,
) : ViewModel() {
    val state: StateFlow<NewsUiState> = sessions.session
        .flatMapLatest { session ->
            val feed = session?.let { catalog.observeFeed(it.profile) } ?: flowOf(null)
            combine(feed, sessions.favoriteNewsIds) { home, favorites ->
                val remote = home?.news.orEmpty().map { it.filled() }
                val merged = (LearnerContent.catalog + remote).distinctBy { it.id }
                    .sortedByDescending { it.publishedAtEpochMs }
                val unlocked = session?.profile?.let(LukaEntitlements::fullMitNews) == true
                NewsUiState(
                    articles = merged,
                    favorites = favorites,
                    unlocked = unlocked,
                    tabs = NewsSections.tabs(merged),
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), NewsUiState())

    fun toggleFavorite(id: String) {
        viewModelScope.launch { sessions.toggleNewsFavorite(id) }
    }
}

private fun NewsItem.filled(): NewsItem = copy(
    body = body.ifBlank { excerpt },
    author = author.ifBlank { source.ifBlank { "Rédaction Luka" } },
    domain = domain.ifBlank { "Marché" },
)
