package elieoko.mobile.luka.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.data.remote.FakeCatalog
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.presentation.components.FilterPill
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.components.rememberCvPicker
import elieoko.mobile.luka.presentation.theme.LukaGold
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.imageByName
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val profile = state.profile
    val pickCv = rememberCvPicker(viewModel::saveCv)
    val professionals = FakeCatalog.professionals.filter {
        val q = state.professionalQuery.trim()
        q.isBlank() || it.name.contains(q, true) || it.title.contains(q, true) || it.profession.title.contains(q, true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(LukaRed),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        profile?.displayName?.take(1)?.uppercase().orEmpty().ifBlank { "L" },
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(profile?.displayName.orEmpty().ifBlank { "Ton profil" }, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    Text(
                        listOfNotNull(profile?.profession?.title, profile?.regionId?.replaceFirstChar { it.uppercase() }).joinToString(" · "),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        if (profile?.visibleToRecruiters == true) "Visible par les recruteurs" else "Profil privé — active Pro pour être vu",
                        color = LukaRed,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
        item {
            CvUploadCard(
                fileName = profile?.cvFileName.orEmpty(),
                onClick = pickCv,
            )
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
            Text("Forfaits", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
                        Text(plan.name, color = if (plan.highlight) Color.White else MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.weight(1f))
                        Text("${plan.priceLabel} ${plan.period}", color = if (plan.highlight) Color.White else LukaRed)
                    }
                    plan.perks.forEach { Text("· $it", color = if (plan.highlight) Color.White.copy(0.9f) else MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }
        }
        item {
            Text("Métier supplémentaire", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Plus et Pro débloquent d’autres domaines.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Profession.entries.filter { it.id != profile?.profession?.id }.forEach { profession ->
                    FilterPill(
                        "${profession.emoji} ${profession.title}",
                        state.extraProfession == profession,
                    ) { viewModel.onExtraProfession(profession) }
                }
            }
        }
        item {
            Text("Solliciter une expertise", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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

@Composable
private fun CvUploadCard(fileName: String, onClick: () -> Unit) {
    val hasFile = fileName.isNotBlank()
    val dash = Color(if (hasFile) 0xFF2E7D32 else 0xFFE31B23)
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .drawBehind {
                drawRoundRect(
                    color = dash,
                    style = Stroke(
                        width = 3.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 12f), 0f),
                    ),
                    cornerRadius = CornerRadius(22.dp.toPx()),
                )
            }
            .background(if (hasFile) Color(0xFFE8F5E9) else LukaMist)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            if (hasFile) Icons.Outlined.CheckCircle else Icons.Outlined.UploadFile,
            contentDescription = null,
            tint = if (hasFile) Color(0xFF2E7D32) else LukaRed,
            modifier = Modifier.size(36.dp),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (hasFile) "CV chargé" else "Charger ton CV",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            if (hasFile) fileName else "PDF, DOC ou DOCX — un tap, c’est envoyé",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
