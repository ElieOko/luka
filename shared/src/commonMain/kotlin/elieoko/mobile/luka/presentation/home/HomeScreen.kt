package elieoko.mobile.luka.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.model.PlatformAd
import elieoko.mobile.luka.presentation.components.FilterBottomSheet
import elieoko.mobile.luka.presentation.components.LukaBottomNavHeight
import elieoko.mobile.luka.presentation.components.OfferCard
import elieoko.mobile.luka.presentation.components.PageBackdrop
import elieoko.mobile.luka.presentation.components.PageBackdropTone
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.imageByName
import kotlinx.coroutines.delay
import luka.shared.generated.resources.Res
import luka.shared.generated.resources.onboarding_kinshasa_1
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSeeAllOffers: () -> Unit,
    onOpenNews: () -> Unit,
    onOpenTrends: () -> Unit,
    onOpenOrientation: () -> Unit,
    viewModel: HomeViewModel,
) {
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current
    var showFilters by rememberSaveable { mutableStateOf(false) }
    val isFree = state.profile?.planId.isNullOrBlank() || state.profile?.planId == "starter"

    if (showFilters) {
        FilterBottomSheet(
            filters = state.filters,
            query = state.query,
            onQuery = viewModel::onQuery,
            onCity = viewModel::onCity,
            onProfession = viewModel::onProfession,
            onReset = viewModel::resetFilters,
            onDismiss = { showFilters = false },
            cities = state.cities,
            trades = state.trades,
        )
    }

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(tween(400)) + slideInVertically { it / 12 },
    ) {
        PageBackdrop(Res.drawable.onboarding_kinshasa_1, tone = PageBackdropTone.Cinematic) {
            val ptr = rememberPullToRefreshState()
            PullToRefreshBox(
                isRefreshing = state.refreshing,
                onRefresh = viewModel::refresh,
                state = ptr,
                modifier = Modifier.fillMaxSize(),
                indicator = {
                    PullToRefreshDefaults.Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = state.refreshing,
                        state = ptr,
                        color = LukaRed,
                        containerColor = Color.White,
                    )
                },
            ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = LukaBottomNavHeight + 16.dp),
            ) {
            HomeHeader(
                firstName = state.profile?.firstName().orEmpty(),
                isFree = isFree,
                filterActive = !state.filters.isEmpty || state.query.isNotBlank(),
                onSearch = { showFilters = true },
            )
            HomeQuickRow(
                onNews = onOpenNews,
                onTrends = onOpenTrends,
                onOpenOrientation = onOpenOrientation,
            )
            SectionTitle(
                title = "Offres pour toi",
                action = "Voir tout",
                onAction = onSeeAllOffers,
            )
            Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                state.previewOffers.forEachIndexed { index, offer ->
                    OfferCard(offer, index) {
                        runCatching { uriHandler.openUri(offer.applyUrl) }
                    }
                }
            }
            if (state.previewOffers.isEmpty()) {
                Text(
                    "Aucune offre pour ces filtres. Change de ville ou de métier, ou réinitialise.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(0.85f),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )
            }
            SectionTitle(title = "Pubs partenaires")
            AdsCarousel(state.feed?.ads.orEmpty())
            Spacer(Modifier.height(12.dp))
            }
            }
        }
    }
}

private fun elieoko.mobile.luka.domain.model.UserProfile.firstName(): String =
    displayName.substringBefore(" ").ifBlank { displayName }

@Composable
private fun HomeHeader(
    firstName: String,
    isFree: Boolean,
    filterActive: Boolean,
    onSearch: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            if (isFree) {
                Surface(color = LukaMist, shape = RoundedCornerShape(99.dp)) {
                    Text(
                        "Gratuit",
                        color = LukaRed,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
                Spacer(Modifier.height(8.dp))
            }
            Text("Bonjour $firstName", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = Color.White)
            Text(
                "L’emploi vient à toi. 5 pistes, puis tout voir.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(0.85f),
            )
        }
        IconButton(onClick = onSearch) {
            Box {
                Icon(Icons.Outlined.Search, contentDescription = "Recherche et filtres", tint = Color.White)
                if (filterActive) {
                    Box(
                        Modifier
                            .align(Alignment.TopEnd)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(LukaRed),
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeQuickRow(
    onNews: () -> Unit,
    onTrends: () -> Unit,
    onOpenOrientation: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        QuickTile("Nouveautés", "Presse tech", Icons.AutoMirrored.Outlined.MenuBook, onNews, Modifier.weight(1f))
        QuickTile("Tendances", "Demandés", Icons.AutoMirrored.Outlined.ShowChart, onTrends, Modifier.weight(1f))
        QuickTile("Orientation", "Chiffres", Icons.Outlined.School, onOpenOrientation, Modifier.weight(1f))
    }
}

@Composable
private fun QuickTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .clip(RoundedCornerShape(18.dp))
            .background(LukaMist)
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(icon, contentDescription = null, tint = LukaRed)
        Spacer(Modifier.height(6.dp))
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun SectionTitle(title: String, action: String? = null, onAction: (() -> Unit)? = null) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.weight(1f),
        )
        if (action != null && onAction != null) {
            TextButton(onClick = onAction) {
                Text(action, color = LukaRed, fontWeight = FontWeight.Bold)
                Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = LukaRed)
            }
        }
    }
}

@Composable
private fun AdsCarousel(ads: List<PlatformAd>) {
    if (ads.isEmpty()) return
    val pager = rememberPagerState(pageCount = { ads.size })
    LaunchedEffect(ads.size) {
        while (true) {
            delay(3800)
            pager.animateScrollToPage((pager.currentPage + 1) % ads.size)
        }
    }
    Column {
        HorizontalPager(
            state = pager,
            contentPadding = PaddingValues(horizontal = 28.dp),
            pageSpacing = 14.dp,
            modifier = Modifier.fillMaxWidth().height(200.dp),
        ) { page ->
            val ad = ads[page]
            val pageOffset = (pager.currentPage - page) + pager.currentPageOffsetFraction
            Box(
                Modifier
                    .fillMaxSize()
                    .scale(1f - abs(pageOffset).coerceAtMost(1f) * 0.08f)
                    .clip(RoundedCornerShape(24.dp)),
            ) {
                Image(
                    painterResource(imageByName(ad.imageName)),
                    ad.title,
                    Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                Box(
                    Modifier.fillMaxSize().background(
                        Brush.verticalGradient(listOf(Color.Transparent, Color(0xE6000000))),
                    ),
                )
                Column(Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                    Text("PUBLICITÉ", color = Color.White.copy(0.7f), style = MaterialTheme.typography.labelSmall)
                    Text(ad.title, color = Color.White, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
                    Text(ad.subtitle, color = Color.White.copy(0.85f), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            ads.indices.forEach { i ->
                Box(
                    Modifier
                        .padding(horizontal = 3.dp)
                        .size(width = if (pager.currentPage == i) 18.dp else 7.dp, height = 7.dp)
                        .clip(CircleShape)
                        .background(if (pager.currentPage == i) LukaRed else MaterialTheme.colorScheme.outlineVariant),
                )
            }
        }
    }
}
