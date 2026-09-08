package elieoko.mobile.luka.presentation.setup

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.presentation.components.BottomCtaBar
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.theme.LukaRed
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnalysisScreen(viewModel: SetupViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val infinite = rememberInfiniteTransition()
    val pulse by infinite.animateFloat(
        0.2f,
        1f,
        infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Restart),
    )

    LaunchedEffect(state.launching) {
        if (state.launching) delay(2200)
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))
        Text("Analyses infinies", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(
            "Une seule fois. Ensuite Luka scrute le marché congolais sans s’arrêter — et t’envoie les offres.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(260.dp)) {
                val center = Offset(size.width / 2, size.height / 2)
                for (i in 1..4) {
                    val radius = size.minDimension / 8 * i * (0.65f + pulse * 0.55f)
                    drawCircle(
                        color = LukaRed.copy(alpha = (1f - pulse) * 0.45f / i),
                        radius = radius,
                        center = center,
                        style = Stroke(width = 4f),
                    )
                }
                drawCircle(LukaRed, radius = 18f, center = center)
            }
        }
        Text(
            if (state.launching) "Scan des banques, telcos, ONG et mines…" else "Prêt à lancer le radar Luka.",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(20.dp))
        BottomCtaBar {
            LukaPrimaryButton(
                text = if (state.launching) "Analyse en cours…" else "Lancer les analyses infinies",
                onClick = viewModel::launch,
                enabled = !state.launching && !state.launched,
            )
        }
    }
}
