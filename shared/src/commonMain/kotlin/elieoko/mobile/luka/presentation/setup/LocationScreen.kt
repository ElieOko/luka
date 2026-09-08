package elieoko.mobile.luka.presentation.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.model.CongoCatalog
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LocationScreen(viewModel: SetupViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Text("Où vis-tu ?", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))
        Text(
            "Pour l’instant, Luka ne propose que des offres en RDC.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(20.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(LukaMist)
                .padding(16.dp),
        ) {
            Text("${CongoCatalog.rdc.flag}  ${CongoCatalog.rdc.name}", style = MaterialTheme.typography.titleMedium)
            Text("Pays verrouillé — d’autres arriveront plus tard.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(24.dp))
        Text("Ta région", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            CongoCatalog.regions.forEach { region ->
                val selected = state.selectedRegionId == region.id
                Column(
                    Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, if (selected) LukaRed else MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                        .background(if (selected) LukaRed.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface)
                        .clickable { viewModel.selectRegion(region.id) }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                ) {
                    Text(region.name, style = MaterialTheme.typography.titleMedium)
                    Text(region.cityHint, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Spacer(Modifier.weight(1f))
        LukaPrimaryButton(
            text = "Continuer",
            onClick = viewModel::confirmLocation,
            enabled = state.selectedRegionId != null,
        )
    }
}
