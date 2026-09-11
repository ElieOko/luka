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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.renderComposeScene
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.components.PageBackdrop
import elieoko.mobile.luka.presentation.components.PageBackdropTone
import elieoko.mobile.luka.presentation.components.PulseDot
import elieoko.mobile.luka.presentation.theme.LukaCream
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.LukaTheme
import elieoko.mobile.luka.presentation.theme.imageByName
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
    shot(File(outDir, "orientation_backdrop.png")) { OrientationShot() }
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
            Text("Les offres viennent du serveur Casanayo.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Surface(shape = RoundedCornerShape(22.dp), shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Offres live", style = MaterialTheme.typography.titleMedium)
                    Text("Home · Kinshasa", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item {
            Surface(color = LukaMist, shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth().height(120.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.Bottom) {
                    Text("Serveur Casanayo", color = LukaRed, style = MaterialTheme.typography.labelSmall)
                    Text("Aucune pub locale — uniquement le backend.", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun OrientationShot() {
    PageBackdrop(imageByName("onboarding_kinshasa_3"), tone = PageBackdropTone.Cinematic) {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            Text("Orientation", color = Color.White, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(8.dp))
            Text("Élève, étudiant, employé ou employeur ?", color = Color.White.copy(0.85f))
            Spacer(Modifier.height(18.dp))
            listOf("Élève" to "Je suis au secondaire", "Étudiant" to "Je cherche un premier emploi").forEach { (title, sub) ->
                Column(
                    Modifier.fillMaxWidth().padding(bottom = 10.dp).clip(RoundedCornerShape(20.dp)).background(Color.White.copy(0.14f)).padding(16.dp),
                ) {
                    Text(title, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(sub, color = Color.White.copy(0.85f))
                }
            }
            Spacer(Modifier.weight(1f))
            LukaPrimaryButton("Lancer l’analyse", {}, Modifier.padding(bottom = 112.dp))
        }
    }
}
