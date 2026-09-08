package elieoko.mobile.luka.presentation.orientation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
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
import elieoko.mobile.luka.presentation.explore.ExploreViewModel
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.imageByName
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrientationScreen(viewModel: ExploreViewModel = koinViewModel()) {
    val feed by viewModel.feed.collectAsState()
    val paths = feed?.orientation.orEmpty()
    Column(
        Modifier.fillMaxSize().statusBarsPadding().verticalScroll(rememberScrollState()).padding(bottom = 16.dp),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(168.dp)
                .background(Brush.verticalGradient(listOf(Color(0xFF4A0710), LukaRed))),
        ) {
            Column(Modifier.padding(24.dp).align(Alignment.BottomStart)) {
                Icon(Icons.Outlined.Explore, contentDescription = null, tint = Color.White)
                Text("Orientation", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text("Parcours concrets pour basculer vers un métier demandé", color = Color.White.copy(0.85f))
            }
        }
        Spacer(Modifier.height(8.dp))
        paths.forEachIndexed { index, path ->
            var ready by remember(path.id) { mutableStateOf(false) }
            val scale by animateFloatAsState(if (ready) 1f else 0.94f, tween(380 + index * 70), label = "o")
            LaunchedEffect(path.id) { ready = true }
            Column(
                Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .scale(scale)
                    .clip(RoundedCornerShape(22.dp))
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                Image(
                    painterResource(imageByName(path.imageName)),
                    path.title,
                    Modifier.fillMaxWidth().height(132.dp),
                    contentScale = ContentScale.Crop,
                )
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(path.domain, color = LukaRed, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text("${path.demandScore}/100", color = LukaRed, fontWeight = FontWeight.Black)
                    }
                    Text(path.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text(path.why, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "${path.duration} · ${path.level}",
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(LukaMist).padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Prochaine étape : ${path.nextStep}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
