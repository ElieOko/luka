package elieoko.mobile.luka.presentation.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.model.TradeChip
import elieoko.mobile.luka.presentation.components.BottomCtaBar
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfessionScreen(viewModel: SetupViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    var query by remember { mutableStateOf("") }
    val grouped = state.trades
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
                "Un seul métier à la fois. Coche la ligne, pas le titre de section.",
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
            state.trades.firstOrNull { it.key() == state.selectedTradeKey }?.let { chip ->
                Spacer(Modifier.height(12.dp))
                Surface(color = LukaMist, shape = RoundedCornerShape(18.dp)) {
                    Column(Modifier.padding(14.dp)) {
                        Text(chip.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(chip.family, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    professions.forEach { chip ->
                        UniqueTradeRow(
                            chip = chip,
                            selected = chip.key() == state.selectedTradeKey,
                            onSelect = { viewModel.selectProfession(chip) },
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
        BottomCtaBar {
            LukaPrimaryButton("Continuer", viewModel::confirmProfession, enabled = state.selectedTradeKey != null)
        }
    }
}

@Composable
private fun UniqueTradeRow(
    chip: TradeChip,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) LukaRed else LukaMist)
            .clickable(onClick = onSelect)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.White,
                unselectedColor = LukaRed,
            ),
        )
        Column(Modifier.weight(1f).padding(end = 12.dp, top = 8.dp, bottom = 8.dp)) {
            Text(
                chip.title,
                fontWeight = FontWeight.SemiBold,
                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
            )
            Text(
                chip.tagline,
                style = MaterialTheme.typography.bodySmall,
                color = if (selected) Color.White.copy(0.88f) else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
