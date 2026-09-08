package elieoko.mobile.luka.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.renderComposeScene
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.data.remote.FakeCatalog
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.components.PulseDot
import elieoko.mobile.luka.presentation.theme.LukaCream
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.LukaTheme
import elieoko.mobile.luka.presentation.welcome.WelcomeScreen
import org.jetbrains.skia.EncodedImageFormat
import java.io.File

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val outDir = File(System.getProperty("luka.screenshot.dir") ?: "screenshots").apply { mkdirs() }
    shot(File(outDir, "welcome_onboarding.png")) {
        WelcomeScreen(onStart = {})
    }
    shot(File(outDir, "profession_picker.png")) { ProfessionShot() }
    shot(File(outDir, "home_offers.png")) { HomeShot() }
    println("Wrote screenshots to ${outDir.absolutePath}")
}

@OptIn(ExperimentalComposeUiApi::class)
private fun shot(file: File, content: @Composable () -> Unit) {
    val image = renderComposeScene(width = 840, height = 1720, density = Density(2f)) {
        LukaTheme {
            Box(Modifier.fillMaxSize().background(LukaCream), content = { content() })
        }
    }
    file.writeBytes(image.encodeToData(EncodedImageFormat.PNG)!!.bytes)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProfessionShot() {
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.padding(24.dp)) {
            Text("Ton métier.", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(8.dp))
            Text("Un chip, un domaine. Électricité, data, mines…", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        FlowRow(
            modifier = Modifier.padding(horizontal = 20.dp).weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Profession.entries.take(16).forEach { profession ->
                val selected = profession == Profession.ELECTRICITY
                FilterChip(
                    selected = selected,
                    onClick = {},
                    label = { Text(profession.title) },
                    shape = RoundedCornerShape(22.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = LukaRed,
                        selectedLabelColor = Color.White,
                    ),
                )
            }
        }
        LukaPrimaryButton("Continuer", {}, Modifier.padding(20.dp))
    }
}

@Composable
private fun HomeShot() {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PulseDot()
                Spacer(Modifier.width(8.dp))
                Text("Analyses infinies actives", color = LukaRed, style = MaterialTheme.typography.labelLarge)
            }
            Text("Bonjour Grace", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text("5 offres, puis tout voir · Kinshasa", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        items(FakeCatalog.offers.take(5)) { offer ->
            Surface(shape = RoundedCornerShape(22.dp), shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(offer.title, style = MaterialTheme.typography.titleMedium)
                    Text("${offer.company} · ${offer.city}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text(offer.summary, style = MaterialTheme.typography.bodyMedium)
                    Text(offer.contract, color = LukaRed, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        item {
            Surface(color = LukaMist, shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth().height(120.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.Bottom) {
                    Text("PUBLICITÉ", color = LukaRed, style = MaterialTheme.typography.labelSmall)
                    Text("Pubs en carrousel horizontal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
