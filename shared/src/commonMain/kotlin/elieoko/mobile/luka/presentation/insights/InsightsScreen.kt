package elieoko.mobile.luka.presentation.insights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.model.LukaEntitlements
import elieoko.mobile.luka.domain.model.UserProfile
import elieoko.mobile.luka.domain.repository.SessionRepository
import elieoko.mobile.luka.presentation.components.LockedFeatureCard
import elieoko.mobile.luka.presentation.components.LukaBottomNavHeight
import elieoko.mobile.luka.presentation.components.PageBackdrop
import elieoko.mobile.luka.presentation.components.PageBackdropTone
import elieoko.mobile.luka.presentation.explore.ExploreViewModel
import elieoko.mobile.luka.presentation.theme.LukaRed
import luka.shared.generated.resources.Res
import luka.shared.generated.resources.onboarding_kinshasa_2
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InsightsScreen(
    onUnlock: () -> Unit,
    viewModel: ExploreViewModel = koinViewModel(),
    sessions: SessionRepository = koinInject(),
) {
    val feed by viewModel.feed.collectAsState()
    val session by sessions.session.collectAsState(null)
    val profile = session?.profile
    val offers = feed?.offers.orEmpty()
    PageBackdrop(Res.drawable.onboarding_kinshasa_2, tone = PageBackdropTone.Cinematic) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = LukaBottomNavHeight + 72.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text("Analyses", color = Color.White, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
            Text(
                "CV, offres et signaux du marché — pour viser juste.",
                color = Color.White.copy(0.85f),
            )
            if (profile == null || !LukaEntitlements.cvAnalysis(profile)) {
                LockedFeatureCard(
                    title = "Analyse de CV",
                    body = "Score de clarté, mots-clés manquants et pistes pour matcher les offres RDC.",
                    onUnlock = onUnlock,
                )
            } else {
                CvAnalysisCard(profile)
            }
            if (profile == null || !LukaEntitlements.offerAnalysis(profile)) {
                LockedFeatureCard(
                    title = "Analyse des offres",
                    body = "Lis le marché de ton métier : contrats, villes, volume sur les offres ouvertes.",
                    onUnlock = onUnlock,
                )
            } else {
                OfferAnalysisCard(
                    profession = profile.tradeTitle.ifBlank { profile.profession?.title.orEmpty() },
                    total = offers.size,
                    cities = offers.map { it.city }.distinct().take(4),
                    contracts = offers.groupingBy { it.contract }.eachCount().entries.sortedByDescending { it.value }.take(3),
                )
            }
        }
    }
}

@Composable
private fun CvAnalysisCard(profile: UserProfile) {
    val hasCv = profile.cvFileName.isNotBlank()
    val hasName = profile.displayName.length >= 2
    val hasCity = !profile.cityName.isNullOrBlank()
    val hasMail = profile.email.contains("@")
    val score = listOf(hasCv, hasName, hasCity, hasMail, profile.profession != null).count { it } * 20
    Surface(shape = RoundedCornerShape(22.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Analyse de CV", color = LukaRed, fontWeight = FontWeight.Bold)
            Text("$score / 100", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            LinearProgressIndicator(progress = { score / 100f }, modifier = Modifier.fillMaxWidth(), color = LukaRed)
            Text(if (hasCv) "CV : ${profile.cvFileName}" else "Ajoute un CV PDF pour un diagnostic plus fin.", style = MaterialTheme.typography.bodyMedium)
            Text(if (hasCity) "Ville renseignée." else "Indique ta ville : les offres Luka sont locales.", style = MaterialTheme.typography.bodyMedium)
            Text(if (hasMail) "E-mail présent." else "Un e-mail professionnel accélère les retours.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun OfferAnalysisCard(
    profession: String,
    total: Int,
    cities: List<String>,
    contracts: List<Map.Entry<String, Int>>,
) {
    Surface(shape = RoundedCornerShape(22.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Analyse des offres", color = LukaRed, fontWeight = FontWeight.Bold)
            Text(
                if (profession.isBlank()) "$total offres ouvertes en RDC."
                else "$total offres suivies pour « $profession ».",
                fontWeight = FontWeight.SemiBold,
            )
            if (cities.isNotEmpty()) {
                Text("Villes actives : ${cities.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
            }
            contracts.forEach { (name, count) ->
                Text("$name · $count", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(4.dp))
            Text("Actualisé à partir des offres actuellement ouvertes.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
    }
}
