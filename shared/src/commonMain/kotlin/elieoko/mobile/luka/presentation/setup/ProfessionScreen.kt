package elieoko.mobile.luka.presentation.setup

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.components.RedHeroGradient
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.image
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfessionScreen(viewModel: SetupViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.padding(24.dp)) {
            Text("Un seul métier.", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                "Comme une boussole : Luka se spécialise pour toi. Tu pourras en débloquer d’autres avec un forfait.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(Profession.entries.toList(), key = { it.id }) { profession ->
                val selected = state.selectedProfession == profession
                val scale by animateFloatAsState(if (selected) 1.02f else 1f)
                val border by animateColorAsState(if (selected) LukaRed else Color.Transparent)
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(148.dp)
                        .scale(scale)
                        .clip(RoundedCornerShape(24.dp))
                        .border(2.dp, border, RoundedCornerShape(24.dp))
                        .clickable { viewModel.selectProfession(profession) },
                ) {
                    Image(
                        painterResource(profession.image()),
                        contentDescription = profession.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    RedHeroGradient(Modifier.fillMaxSize())
                    Column(
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                    ) {
                        Text(profession.title, color = Color.White, style = MaterialTheme.typography.titleLarge)
                        Text(profession.tagline, color = Color.White.copy(0.86f), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
        LukaPrimaryButton(
            text = "Continuer",
            onClick = viewModel::confirmProfession,
            enabled = state.selectedProfession != null,
            modifier = Modifier.padding(20.dp),
        )
    }
}
