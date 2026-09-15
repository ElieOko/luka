package elieoko.mobile.luka.desktop

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.renderComposeScene
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.model.AccountKind
import elieoko.mobile.luka.domain.model.JobOffer
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.presentation.components.AccountDrawerPreview
import elieoko.mobile.luka.presentation.legal.PrivacyPolicyScreen
import elieoko.mobile.luka.presentation.components.LukaLogo
import elieoko.mobile.luka.presentation.components.LukaOfferIcon
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.components.LukaTopBarPreview
import elieoko.mobile.luka.presentation.components.OfferCard
import elieoko.mobile.luka.presentation.components.PageBackdrop
import elieoko.mobile.luka.presentation.components.PageBackdropTone
import elieoko.mobile.luka.presentation.components.PulseDot
import elieoko.mobile.luka.domain.model.PaymentMethod
import elieoko.mobile.luka.presentation.subscription.SubscriptionCheckoutContent
import elieoko.mobile.luka.presentation.subscription.SubscriptionUiState
import elieoko.mobile.luka.domain.model.LearnerContent
import elieoko.mobile.luka.domain.model.formatNewsDate
import elieoko.mobile.luka.presentation.theme.LukaCream
import elieoko.mobile.luka.presentation.theme.LukaInk
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaMuted
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
    shot(File(outDir, "brand_logo.png"), width = 840, height = 720) {
        BrandLogoShot()
    }
    shot(File(outDir, "profession_picker.png")) { ProfessionShot() }
    shot(File(outDir, "auth_register.png")) { AuthRegisterShot() }
    shot(File(outDir, "privacy_policy.png")) { PrivacyPolicyScreen(onBack = {}) }
    shot(File(outDir, "news_tabs.png")) { NewsTabsShot() }
    shot(File(outDir, "news_article.png"), height = 1720) { NewsArticleShot() }
    shot(File(outDir, "home_offers.png")) { HomeShot() }
    shot(File(outDir, "abonnement.png"), height = 1480) { SubscriptionShot() }
    shot(File(outDir, "abonnement_pro.png"), height = 1480) { SubscriptionProShot() }
    shot(File(outDir, "menu_drawer.png"), height = 900) { DrawerShot() }
    shot(File(outDir, "luka_topbar.png"), height = 360) {
        LukaTopBarPreview()
    }
    shot(File(outDir, "luka_chrome.png"), height = 1720) { ChromeShot() }
    shot(File(outDir, "orientation_backdrop.png")) { OrientationShot() }
    println("Wrote screenshots to ${outDir.absolutePath}")
}

@OptIn(ExperimentalComposeUiApi::class)
private fun shot(
    file: File,
    width: Int = 840,
    height: Int = 1720,
    content: @Composable () -> Unit,
) {
    val image = renderComposeScene(width = width, height = height, density = Density(2f)) {
        LukaTheme {
            Box(Modifier.fillMaxSize().background(LukaCream), content = { content() })
        }
    }
    file.writeBytes(image.encodeToData(EncodedImageFormat.PNG)!!.bytes)
}

@Composable
private fun BrandLogoShot() {
    Column(Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.spacedBy(28.dp)) {
        Text("Luka", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        Surface(shape = RoundedCornerShape(22.dp), shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
            Box(Modifier.padding(24.dp), contentAlignment = Alignment.CenterStart) {
                LukaLogo()
            }
        }
        Surface(color = Color(0xFF141414), shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
            Box(Modifier.padding(24.dp), contentAlignment = Alignment.CenterStart) {
                LukaLogo(light = true)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            LukaOfferIcon()
            Text("Marque sur les offres", style = MaterialTheme.typography.titleMedium)
        }
        OfferCard(
            offer = JobOffer(
                id = "logo-preview",
                title = "Développeur Android",
                company = "Casanayo",
                companyLogoUrl = "",
                profession = Profession.SOFTWARE_ENGINEERING,
                regionId = "kin",
                city = "Kinshasa",
                contract = "CDI",
                salary = "",
                summary = "Le logo LUKA reste net à toutes les tailles.",
                applyUrl = "",
                postedAtEpochMs = 0L,
                isRemote = false,
            ),
            onOpen = {},
        )
    }
}

@Composable
private fun ProfessionShot() {
    val rows = listOf(
        "Développement logiciel" to "Apps, APIs, produits numériques",
        "Support système" to "Helpdesk, maintenance, postes",
        "Cybersécurité" to "SOC, pentest, gouvernance",
    )
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.padding(24.dp)) {
            Text("Ton métier.", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(8.dp))
            Text("Un seul métier à la fois. Coche la ligne, pas le titre de section.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(
            "NUMÉRIQUE",
            color = LukaRed,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        )
        Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            rows.forEachIndexed { index, (title, tagline) ->
                val selected = index == 1
                Surface(
                    color = if (selected) LukaRed else LukaMist,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(title, fontWeight = FontWeight.SemiBold, color = if (selected) Color.White else Color.Unspecified)
                        Text(tagline, color = if (selected) Color.White.copy(0.88f) else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        Spacer(Modifier.weight(1f))
        LukaPrimaryButton("Continuer", {}, Modifier.padding(20.dp))
    }
}

@Composable
private fun AuthRegisterShot() {
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        LukaLogo()
        Spacer(Modifier.height(28.dp))
        Text("Crée ton compte.", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))
        Text("Numéro congolais. Un code SMS, puis tu restes connecté.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(20.dp))
        Surface(color = LukaMist, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Text("+243 81 000 0000", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(20.dp))
        Text("Quel compte ?", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(10.dp))
        AccountKind.entries.forEach { kind ->
            val selected = kind == AccountKind.LEARNER
            Surface(
                color = if (selected) LukaRed else LukaMist,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text(kind.title, fontWeight = FontWeight.Bold, color = if (selected) Color.White else Color.Unspecified)
                    Text(kind.subtitle, color = if (selected) Color.White.copy(0.9f) else MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("J’accepte la politique de confidentialité.")
        Text("Lire la politique de confidentialité", color = LukaRed, fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))
        LukaPrimaryButton("Créer le compte", {})
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
            OfferCard(
                offer = JobOffer(
                    id = "home-preview",
                    title = "Chargé de recrutement",
                    company = "Casanayo",
                    companyLogoUrl = "",
                    profession = Profession.HUMAN_RESOURCES,
                    regionId = "kin",
                    city = "Kinshasa",
                    contract = "CDI",
                    salary = "",
                    summary = "Les offres live portent le logo LUKA.",
                    applyUrl = "",
                    postedAtEpochMs = 0L,
                    isRemote = false,
                ),
                onOpen = {},
            )
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
private fun SubscriptionShot() {
    Box(Modifier.fillMaxSize().background(Color.White).padding(top = 24.dp)) {
        SubscriptionCheckoutContent(
            state = SubscriptionUiState(
                selectedApiId = 1,
                phoneNational = "827824163",
                loadingCatalog = false,
            ),
            onSelectApiId = {},
            onSelectMethod = {},
            onPhoneChange = {},
            onSelectCurrency = {},
            onPay = {},
        )
    }
}

@Composable
private fun SubscriptionProShot() {
    Box(Modifier.fillMaxSize().background(Color.White).padding(top = 24.dp)) {
        SubscriptionCheckoutContent(
            state = SubscriptionUiState(
                selectedApiId = 2,
                currencyCode = "CDF",
                method = PaymentMethod.Card,
                phoneNational = "827824163",
                loadingCatalog = false,
            ),
            onSelectApiId = {},
            onSelectMethod = {},
            onPhoneChange = {},
            onSelectCurrency = {},
            onPay = {},
        )
    }
}

@Composable
private fun DrawerShot() {
    AccountDrawerPreview()
}

@Composable
private fun ChromeShot() {
    Box(Modifier.fillMaxSize().background(Color(0xFF2A0C12))) {
        Column(Modifier.fillMaxSize()) {
            LukaTopBarPreview()
            Column(Modifier.weight(1f).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Bonjour Grace", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text("L’emploi vient à toi.", color = Color.White.copy(0.85f))
                OfferCard(
                    offer = JobOffer(
                        id = "chrome-preview",
                        title = "Développeur Android",
                        company = "Casanayo",
                        companyLogoUrl = "",
                        profession = Profession.SOFTWARE_ENGINEERING,
                        regionId = "kin",
                        city = "Kinshasa",
                        contract = "CDI",
                        salary = "",
                        summary = "Top bar Luka, menu, cloche, Plus d’opportunité.",
                        applyUrl = "",
                        postedAtEpochMs = 0L,
                        isRemote = false,
                    ),
                    onOpen = {},
                )
            }
            Box(Modifier.fillMaxWidth().height(56.dp).background(Color(0xFF121212)))
        }
        Surface(
            onClick = {},
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 76.dp),
            shape = RoundedCornerShape(28.dp),
            color = LukaRed,
            shadowElevation = 8.dp,
        ) {
            Text(
                "Plus d’opportunité",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
            )
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

@Composable
private fun NewsTabsShot() {
    val tabs = listOf("Favoris", "IA", "Énergie", "Formation", "Souscrit")
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("News", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        Text("Les articles se lisent ici, classés par domaine.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            tabs.forEach { label ->
                val selected = label == "IA"
                Surface(
                    color = if (selected) LukaRed else LukaMist,
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Text(label, color = if (selected) Color.White else LukaInk, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        LearnerContent.publicNews.filter { it.domain == "IA" }.forEach { item ->
            Surface(shape = RoundedCornerShape(18.dp), color = Color.White, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                Column(Modifier.padding(14.dp)) {
                    Text(item.domain, color = LukaRed, fontWeight = FontWeight.Bold)
                    Text(item.title, fontWeight = FontWeight.Bold)
                    Text(item.excerpt, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                    Text("${item.author} · ${formatNewsDate(item.publishedAtEpochMs)}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun NewsArticleShot() {
    val item = LearnerContent.publicNews.first()
    Column(Modifier.fillMaxSize().background(LukaCream).padding(22.dp)) {
        Text(item.domain.uppercase(), color = LukaRed, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(item.title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black, color = LukaInk)
        Spacer(Modifier.height(10.dp))
        Text(item.author, fontWeight = FontWeight.SemiBold, color = LukaInk)
        Text("Publié le ${formatNewsDate(item.publishedAtEpochMs)} · ${item.source}", color = LukaMuted)
        Spacer(Modifier.height(16.dp))
        item.body.split("\n\n").take(3).forEach { paragraph ->
            Text(paragraph.trim(), style = MaterialTheme.typography.bodyLarge, color = LukaInk, modifier = Modifier.padding(bottom = 14.dp))
        }
        LukaPrimaryButton("Partager l’article", {})
    }
}
