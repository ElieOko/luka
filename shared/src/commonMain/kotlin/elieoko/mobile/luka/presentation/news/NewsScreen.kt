package elieoko.mobile.luka.presentation.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.model.NewsItem
import elieoko.mobile.luka.domain.model.NewsSections
import elieoko.mobile.luka.domain.model.formatNewsDate
import elieoko.mobile.luka.presentation.components.LockedFeatureCard
import elieoko.mobile.luka.presentation.components.LukaBottomNavHeight
import elieoko.mobile.luka.presentation.components.PageBackdrop
import elieoko.mobile.luka.presentation.components.PageBackdropTone
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.imageByName
import luka.shared.generated.resources.Res
import luka.shared.generated.resources.onboarding_kinshasa_2
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewsScreen(
    onUnlock: () -> Unit = {},
    onOpenArticle: (String) -> Unit = {},
    viewModel: NewsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var tab by rememberSaveable { mutableStateOf(state.tabs.drop(1).firstOrNull() ?: NewsSections.FAVORITES) }
    val tabs = state.tabs
    val selectedTab = tab.takeIf { it in tabs } ?: tabs.getOrElse(1) { NewsSections.FAVORITES }
    val visible = state.visible(selectedTab)
    PageBackdrop(Res.drawable.onboarding_kinshasa_2, tone = PageBackdropTone.Cinematic) {
        Column(Modifier.fillMaxSize()) {
            Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 8.dp)) {
                Text("News", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text(
                    "Les articles se lisent ici, classés par domaine.",
                    color = Color.White.copy(0.85f),
                )
            }
            if (tabs.isNotEmpty()) {
                ScrollableTabRow(
                    selectedTabIndex = tabs.indexOf(selectedTab).coerceAtLeast(0),
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    edgePadding = 16.dp,
                    indicator = { positions ->
                        val index = tabs.indexOf(selectedTab).coerceAtLeast(0)
                        if (positions.isNotEmpty()) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(positions[index]),
                                color = LukaRed,
                            )
                        }
                    },
                    divider = {},
                ) {
                    tabs.forEach { label ->
                        Tab(
                            selected = selectedTab == label,
                            onClick = { tab = label },
                            text = {
                                Text(
                                    label,
                                    fontWeight = if (selectedTab == label) FontWeight.Bold else FontWeight.Medium,
                                )
                            },
                            selectedContentColor = Color.White,
                            unselectedContentColor = Color.White.copy(0.7f),
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = LukaBottomNavHeight + 72.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (selectedTab == NewsSections.SUBSCRIBED && !state.unlocked) {
                    item {
                        LockedFeatureCard(
                            title = "Dossier Souscrit",
                            body = "Briefs de marché, contrats, pipelines de stages : les infos précieuses, avec l’abonnement.",
                            onUnlock = onUnlock,
                        )
                    }
                    items(state.articles.filter { it.subscribed }, key = { it.id }) { item ->
                        SubscribedTeaser(item)
                    }
                } else if (visible.isEmpty()) {
                    item {
                        Text(
                            when (selectedTab) {
                                NewsSections.FAVORITES -> "Aucun favori pour l’instant. Ouvre un article et pose le signet."
                                else -> "Pas d’articles dans ce domaine pour l’instant."
                            },
                            color = Color.White.copy(0.85f),
                        )
                    }
                } else {
                    items(visible, key = { it.id }) { item ->
                        NewsListCard(
                            item = item,
                            favorite = item.id in state.favorites,
                            onOpen = { onOpenArticle(item.id) },
                            onFavorite = { viewModel.toggleFavorite(item.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NewsListCard(
    item: NewsItem,
    favorite: Boolean,
    onOpen: () -> Unit,
    onFavorite: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable(onClick = onOpen),
    ) {
        Image(
            painterResource(imageByName(item.imageName)),
            item.title,
            Modifier.fillMaxWidth().height(148.dp),
            contentScale = ContentScale.Crop,
        )
        Column(Modifier.padding(14.dp)) {
            Text(item.domain, color = LukaRed, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(
                item.excerpt,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${item.author} · ${formatNewsDate(item.publishedAtEpochMs)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onFavorite) {
                    Icon(
                        if (favorite) Icons.Rounded.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (favorite) "Retirer des favoris" else "Ajouter aux favoris",
                        tint = LukaRed,
                    )
                }
            }
        }
    }
}

@Composable
private fun SubscribedTeaser(item: NewsItem) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.14f))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        Spacer(Modifier.size(12.dp))
        Column(Modifier.weight(1f)) {
            Text(item.title, color = Color.White, fontWeight = FontWeight.Bold)
            Text("Réservé aux abonnés", color = Color.White.copy(0.75f), style = MaterialTheme.typography.bodySmall)
        }
    }
}
