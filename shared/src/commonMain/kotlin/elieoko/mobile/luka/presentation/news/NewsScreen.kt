package elieoko.mobile.luka.presentation.news

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.presentation.components.LukaBottomNavHeight
import elieoko.mobile.luka.presentation.components.PageBackdrop
import elieoko.mobile.luka.presentation.components.PageBackdropTone
import elieoko.mobile.luka.presentation.explore.ExploreViewModel
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.imageByName
import luka.shared.generated.resources.Res
import luka.shared.generated.resources.onboarding_kinshasa_2
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun NewsScreen(viewModel: ExploreViewModel = koinViewModel()) {
    val feed by viewModel.feed.collectAsState()
    val items = feed?.news.orEmpty()
    PageBackdrop(Res.drawable.onboarding_kinshasa_2, tone = PageBackdropTone.Cinematic) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = LukaBottomNavHeight + 72.dp),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(188.dp),
        ) {
            Column(Modifier.padding(24.dp).align(Alignment.BottomStart)) {
                Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                Text("Nouveautés numériques", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text(
                    "Les actualités publiées par le serveur, quand il y en a.",
                    color = Color.White.copy(0.85f),
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        if (items.isEmpty()) {
            Text(
                "Pas de nouveautés côté serveur pour l’instant.",
                color = Color.White.copy(0.85f),
                modifier = Modifier.padding(horizontal = 24.dp),
            )
        }
        items.forEachIndexed { index, item ->
            var ready by remember(item.id) { mutableStateOf(false) }
            val scale by animateFloatAsState(if (ready) 1f else 0.94f, tween(420 + index * 80), label = "n")
            LaunchedEffect(item.id) { ready = true }
            Column(
                Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .scale(scale)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(bottom = 12.dp),
            ) {
                Image(
                    painterResource(imageByName(item.imageName)),
                    item.title,
                    Modifier.fillMaxWidth().height(150.dp),
                    contentScale = ContentScale.Crop,
                )
                Text(
                    item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                )
                Text(
                    item.excerpt,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 14.dp),
                )
                Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(item.source, color = LukaRed, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Text("·", color = MaterialTheme.colorScheme.outline)
                    Text(daysAgo(item.publishedAtEpochMs), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
    }
}

@OptIn(ExperimentalTime::class)
private fun daysAgo(epochMs: Long): String {
    val days = ((Clock.System.now().toEpochMilliseconds() - epochMs) / 86_400_000L).coerceAtLeast(0)
    return when (days) {
        0L -> "Aujourd’hui"
        1L -> "Hier"
        else -> "Il y a $days j"
    }
}
