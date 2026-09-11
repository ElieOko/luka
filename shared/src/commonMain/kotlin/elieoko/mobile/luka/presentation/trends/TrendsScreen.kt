package elieoko.mobile.luka.presentation.trends

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.presentation.components.LukaBottomNavHeight
import elieoko.mobile.luka.presentation.components.LukaProgressBar
import elieoko.mobile.luka.presentation.components.PageBackdrop
import elieoko.mobile.luka.presentation.components.PageBackdropTone
import elieoko.mobile.luka.presentation.explore.ExploreViewModel
import elieoko.mobile.luka.presentation.theme.LukaRed
import luka.shared.generated.resources.Res
import luka.shared.generated.resources.onboarding_kinshasa_2
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrendsScreen(viewModel: ExploreViewModel = koinViewModel()) {
    val feed by viewModel.feed.collectAsState()
    val stats = feed?.stats.orEmpty()
    PageBackdrop(Res.drawable.onboarding_kinshasa_2, tone = PageBackdropTone.Cinematic) {
    Column(
        Modifier.fillMaxSize().statusBarsPadding().verticalScroll(rememberScrollState()).padding(bottom = LukaBottomNavHeight + 16.dp),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(160.dp),
        ) {
            Column(Modifier.padding(24.dp).align(Alignment.BottomStart)) {
                Icon(Icons.AutoMirrored.Outlined.ShowChart, contentDescription = null, tint = Color(0xFFFFB4A8))
                Text("Tendances", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text("Les métiers les plus demandés cette semaine", color = Color.White.copy(0.85f))
            }
        }
        Spacer(Modifier.height(8.dp))
        if (stats.isEmpty()) {
            Text(
                "Les tendances se construisent à partir des offres du backend.",
                color = Color.White.copy(0.85f),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            )
        }
        stats.forEachIndexed { index, item ->
            var shown by remember(item.profession.id) { mutableStateOf(false) }
            val progress by animateFloatAsState(if (shown) item.sharePercent / 100f else 0f, tween(700 + index * 90), label = "p")
            LaunchedEffect(item.profession.id) { shown = true }
            Column(
                Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${index + 1}", color = LukaRed, fontWeight = FontWeight.Black, modifier = Modifier.width(28.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.profession.title, fontWeight = FontWeight.Bold)
                        Text(
                            "${item.openings} ouvertures · ${item.sharePercent} % · ${item.trend}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                LukaProgressBar(progress)
            }
        }
    }
    }
}
