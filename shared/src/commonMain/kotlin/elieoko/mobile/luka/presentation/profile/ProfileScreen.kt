package elieoko.mobile.luka.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.data.remote.FakeCatalog
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.theme.LukaGold
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.imageByName
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val profile = state.profile
    val professionals = FakeCatalog.professionals.filter {
        val q = state.professionalQuery.trim()
        q.isBlank() || it.name.contains(q, true) || it.title.contains(q, true) || it.profession.title.contains(q, true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("Ton profil", style = MaterialTheme.typography.headlineMedium)
            Text("Visible selon ton forfait — les entreprises viennent vers toi.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            OutlinedTextField(state.displayName, viewModel::onName, label = { Text("Nom") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp))
        }
        item {
            OutlinedTextField(state.bio, viewModel::onBio, label = { Text("Bio") }, modifier = Modifier.fillMaxWidth().height(120.dp), shape = RoundedCornerShape(16.dp))
        }
        item {
            LukaPrimaryButton("Enregistrer", viewModel::saveProfile)
            if (state.message != null) {
                Text(state.message.orEmpty(), color = LukaRed, modifier = Modifier.padding(top = 8.dp))
            }
        }
        item {
            Text("Forfaits", style = MaterialTheme.typography.titleLarge)
            Text("Plus de métiers = plus d’offres analysées.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        items(state.plans) { plan ->
            val selected = profile?.planId == plan.id
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = if (plan.highlight) LukaRed else MaterialTheme.colorScheme.surface,
                modifier = Modifier.clickable { viewModel.choosePlan(plan) }.then(
                    if (selected) Modifier.border(2.dp, LukaGold, RoundedCornerShape(22.dp)) else Modifier,
                ),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(plan.name, color = if (plan.highlight) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.weight(1f))
                        Text("${plan.priceLabel} ${plan.period}", color = if (plan.highlight) androidx.compose.ui.graphics.Color.White else LukaRed)
                    }
                    plan.perks.forEach { Text("· $it", color = if (plan.highlight) androidx.compose.ui.graphics.Color.White.copy(0.9f) else MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }
        }
        item {
            Text("Métier supplémentaire (Plus et plus)", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Profession.entries.filter { it.id != profile?.profession?.id }.take(4).forEach { profession ->
                    val selected = state.extraProfession == profession
                    Text(
                        profession.title,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (selected) LukaRed else LukaMist)
                            .clickable { viewModel.onExtraProfession(profession) }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        color = if (selected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
        item {
            Text("Solliciter une expertise", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                state.professionalQuery,
                viewModel::onProfessionalQuery,
                placeholder = { Text("Nom, métier, ville…") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            )
        }
        items(professionals) { pro ->
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painterResource(imageByName(pro.photoName)),
                    pro.name,
                    Modifier.size(56.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(pro.name, style = MaterialTheme.typography.titleMedium)
                    Text("${pro.title} · ${pro.city}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(pro.bio, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
                }
                Text("${pro.rating}", color = LukaGold, style = MaterialTheme.typography.titleMedium)
            }
        }
        item {
            TextButton(onClick = viewModel::resetDemo, modifier = Modifier.fillMaxWidth()) {
                Text("Réinitialiser la démo", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
