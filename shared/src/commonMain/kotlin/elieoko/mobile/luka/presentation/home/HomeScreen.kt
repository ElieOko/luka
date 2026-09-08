package elieoko.mobile.luka.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import elieoko.mobile.luka.presentation.components.FilterStrip
import elieoko.mobile.luka.presentation.components.OfferCard
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.imageByName
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs

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
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(tween(400)) + slideInVertically { it / 12 },
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
        ) {
            HomeHeader(firstName = state.profile?.firstName().orEmpty())
            FilterStrip(
                filters = state.filters,
                onRegion = viewModel::onRegion,
                onCity = viewModel::onCity,
                onProfession = viewModel::onProfession,
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
                    "Aucune offre pour ces filtres. Change de ville ou de métier.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )
            }
            SectionTitle(title = "Pubs partenaires")
            AdsCarousel(state.feed?.ads.orEmpty())
            Spacer(Modifier.height(12.dp))
        }
    }
}

private fun elieoko.mobile.luka.domain.model.UserProfile.firstName(): String =
    displayName.substringBefore(" ").ifBlank { displayName }

@Composable
private fun HomeHeader(firstName: String) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text("Bonjour $firstName", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(
                "L’emploi vient à toi. 5 pistes, puis tout voir.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = {}) {
            Icon(Icons.Outlined.NotificationsNone, contentDescription = "Alertes")
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
        QuickTile("Nouveautés", "Numérique", Icons.Outlined.Bolt, onNews, Modifier.weight(1f))
        QuickTile("Tendances", "Demandés", Icons.Outlined.Whatshot, onTrends, Modifier.weight(1f))
        QuickTile("Orientation", "Pistes", Icons.Outlined.Explore, onOpenOrientation, Modifier.weight(1f))
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
    val pulse by rememberInfiniteTransition(label = "q").animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(tween(1400), RepeatMode.Reverse),
        label = "qs",
    )
    Column(
        modifier
            .scale(pulse)
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
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
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
