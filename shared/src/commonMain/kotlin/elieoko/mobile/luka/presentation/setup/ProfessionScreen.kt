package elieoko.mobile.luka.presentation.setup

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.presentation.components.BottomCtaBar
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfessionScreen(viewModel: SetupViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    var query by remember { mutableStateOf("") }
    val grouped = Profession.entries
        .filter {
            it.title.contains(query, true) ||
                it.family.contains(query, true) ||
                it.tagline.contains(query, true)
        }
        .groupBy { it.family }

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
            Text("Ton métier.", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(6.dp))
            Text(
                "Un chip, un domaine. Tu pourras en débloquer d’autres avec un forfait.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(14.dp))
            OutlinedTextField(
                query,
                { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Électricité, data, mines…") },
                shape = RoundedCornerShape(18.dp),
                singleLine = true,
            )
            state.selectedProfession?.let { selected ->
                Spacer(Modifier.height(12.dp))
                Surface(color = LukaMist, shape = RoundedCornerShape(18.dp)) {
                    Column(Modifier.padding(14.dp)) {
                        Text(selected.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(selected.tagline, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            grouped.forEach { (family, professions) ->
                Text(
                    family.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = LukaRed,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 14.dp, bottom = 6.dp),
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    professions.forEach { profession ->
                        val selected = state.selectedProfession == profession
                        val scale by animateFloatAsState(if (selected) 1.04f else 1f)
                        val selectedColor by animateColorAsState(if (selected) LukaRed else LukaMist)
                        FilterChip(
                            selected = selected,
                            onClick = { viewModel.selectProfession(profession) },
                            label = { Text(profession.title) },
                            modifier = Modifier.scale(scale),
                            shape = RoundedCornerShape(22.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = selectedColor,
                                selectedContainerColor = LukaRed,
                                selectedLabelColor = Color.White,
                                labelColor = MaterialTheme.colorScheme.onSurface,
                            ),
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
        BottomCtaBar {
            LukaPrimaryButton("Continuer", viewModel::confirmProfession, enabled = state.selectedProfession != null)
        }
    }
}
