package elieoko.mobile.luka.desktop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.renderComposeScene
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.data.remote.FakeCatalog
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.components.PulseDot
import elieoko.mobile.luka.presentation.components.RedHeroGradient
import elieoko.mobile.luka.presentation.theme.LukaCream
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.LukaTheme
import elieoko.mobile.luka.presentation.theme.image
import elieoko.mobile.luka.presentation.welcome.WelcomeScreen
import org.jetbrains.compose.resources.painterResource
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

@Composable
private fun ProfessionShot() {
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.padding(24.dp)) {
            Text("Un seul métier.", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(8.dp))
            Text("Comme une boussole : Luka se spécialise pour toi.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(Profession.entries.take(4)) { profession ->
                val selected = profession == Profession.SOFTWARE_ENGINEERING
                Box(
                    Modifier.fillMaxWidth().height(148.dp).clip(RoundedCornerShape(24.dp)),
                ) {
                    Image(painterResource(profession.image()), profession.title, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    RedHeroGradient(Modifier.fillMaxSize())
                    Column(Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                        Text(profession.title, color = Color.White, style = MaterialTheme.typography.titleLarge)
                        Text(profession.tagline, color = Color.White.copy(alpha = 0.86f), style = MaterialTheme.typography.bodyMedium)
                    }
                    if (selected) {
                        Box(Modifier.matchParentSize().clip(RoundedCornerShape(24.dp)).background(LukaRed.copy(alpha = 0.12f)))
                    }
                }
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
            Text("Bonjour Grace", style = MaterialTheme.typography.headlineMedium)
            Text("Offres pour Ingénierie logiciel · Kinshasa", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Surface(color = LukaMist, shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Métier le plus demandé en RDC", color = LukaRed, style = MaterialTheme.typography.labelLarge)
                    Text("Ingénierie logiciel · 1860 ouvertures · +18 %", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
        items(FakeCatalog.offers.filter { it.profession == Profession.SOFTWARE_ENGINEERING }.take(3)) { offer ->
            Surface(shape = RoundedCornerShape(22.dp), shadowElevation = 1.dp) {
                Column(Modifier.padding(16.dp)) {
                    Text(offer.title, style = MaterialTheme.typography.titleMedium)
                    Text("${offer.company} · ${offer.city}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text(offer.summary, style = MaterialTheme.typography.bodyMedium)
                    Text("${offer.contract} · ${offer.salary}", color = LukaRed, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
