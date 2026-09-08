package elieoko.mobile.luka.presentation.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.model.CongoCatalog
import elieoko.mobile.luka.presentation.components.BottomCtaBar
import elieoko.mobile.luka.presentation.components.FilterPill
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.theme.LukaMist
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LocationScreen(viewModel: SetupViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
        ) {
            Text("Où vis-tu ?", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                "Pour l’instant, Luka ne propose que des offres en RDC.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(20.dp))
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(LukaMist).padding(16.dp),
            ) {
                Text("${CongoCatalog.rdc.flag}  ${CongoCatalog.rdc.name}", style = MaterialTheme.typography.titleMedium)
                Text("Pays verrouillé — d’autres arriveront plus tard.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(24.dp))
            Text("Ta région", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CongoCatalog.regions.forEach { region ->
                    FilterPill(region.name, state.selectedRegionId == region.id) { viewModel.selectRegion(region.id) }
                }
            }
            val cities = CongoCatalog.citiesIn(state.selectedRegionId)
            if (state.selectedRegionId != null && cities.isNotEmpty()) {
                Spacer(Modifier.height(24.dp))
                Text("Villes couvertes", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Tu pourras filtrer par ville sur l’accueil.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(12.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    cities.forEach { city ->
                        FilterPill(city.name, false) {}
                    }
                }
            }
        }
        BottomCtaBar {
            LukaPrimaryButton("Continuer", viewModel::confirmLocation, enabled = state.selectedRegionId != null)
        }
    }
}
