package elieoko.mobile.luka.presentation.orientation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.model.OrientationPersona
import elieoko.mobile.luka.presentation.components.LukaBottomNavHeight
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrientationScreen(viewModel: OrientationViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    when (state.phase) {
        OrientationPhase.Pick -> PersonaPick(
            selected = state.persona,
            onSelect = viewModel::select,
            onLaunch = viewModel::analyze,
        )
        OrientationPhase.Analyzing -> AnalysisPulse(persona = state.persona)
        OrientationPhase.Results -> ResultsPane(
            persona = state.persona,
            insights = state.insights,
            onReset = viewModel::reset,
        )
    }
}

@Composable
private fun PersonaPick(
    selected: OrientationPersona?,
    onSelect: (OrientationPersona) -> Unit,
    onLaunch: () -> Unit,
) {
    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .padding(bottom = 8.dp),
        ) {
            Text("Orientation", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(8.dp))
            Text(
                "Informatif, comme les offres : on analyse le marché national selon qui tu es. Élève, étudiant, employé ou employeur ?",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(18.dp))
            OrientationPersona.entries.forEach { persona ->
                val active = selected == persona
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (active) LukaRed else LukaMist)
                        .border(1.dp, if (active) LukaRed else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                        .clickable { onSelect(persona) }
                        .padding(16.dp),
                ) {
                    Text(persona.title, color = if (active) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text(persona.subtitle, color = if (active) androidx.compose.ui.graphics.Color.White.copy(0.9f) else MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            LukaPrimaryButton("Lancer l’analyse", onLaunch, enabled = selected != null)
        }
        Spacer(Modifier.height(LukaBottomNavHeight))
    }
}

@Composable
private fun AnalysisPulse(persona: OrientationPersona?) {
    val infinite = rememberInfiniteTransition()
    val pulse by infinite.animateFloat(
        0.2f,
        1f,
        infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Restart),
        label = "pulse",
    )
    Column(
        Modifier.fillMaxSize().statusBarsPadding().padding(24.dp).padding(bottom = LukaBottomNavHeight),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))
        Text("Analyse nationale", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(
            when (persona) {
                OrientationPersona.PUPIL -> "Lecture des filières d’études supérieures les plus demandées…"
                OrientationPersona.STUDENT -> "Lecture des premiers emplois après les études…"
                OrientationPersona.EMPLOYEE -> "Lecture des reconversions et autres emplois…"
                OrientationPersona.EMPLOYER -> "Lecture des domaines où investir…"
                null -> "Scan du marché congolais…"
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(240.dp)) {
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
        Text("INS, telcos, mines, banques — chiffres indicatifs.", color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ResultsPane(
    persona: OrientationPersona?,
    insights: List<elieoko.mobile.luka.domain.model.NationalInsight>,
    onReset: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = LukaBottomNavHeight + 16.dp),
    ) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(persona?.title.orEmpty(), color = LukaRed, fontWeight = FontWeight.Bold)
            Text(persona?.resultTitle.orEmpty(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(
                "À titre informatif. Ce n’est pas une offre, c’est ce que les chiffres disent au Congo.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        insights.forEach { item ->
            var shown by remember(item.label) { mutableStateOf(false) }
            val progress by animateFloatAsState(if (shown) item.sharePercent / 100f else 0f, tween(700 + item.rank * 70), label = "p")
            LaunchedEffect(item.label) { shown = true }
            Column(
                Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${item.rank}", color = LukaRed, fontWeight = FontWeight.Black, modifier = Modifier.width(28.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.label, fontWeight = FontWeight.Bold)
                        Text("${item.sharePercent} % · ${item.detail}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(99.dp)),
                    color = LukaRed,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
        TextButton(onClick = onReset, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Changer de profil", color = LukaRed, fontWeight = FontWeight.Bold)
        }
    }
}
