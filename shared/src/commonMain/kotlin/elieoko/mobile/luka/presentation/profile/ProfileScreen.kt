package elieoko.mobile.luka.presentation.profile

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.model.CongoCatalog
import elieoko.mobile.luka.domain.model.LukaPlans
import elieoko.mobile.luka.presentation.components.FilterPill
import elieoko.mobile.luka.presentation.components.LukaBottomNavHeight
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.components.PageBackdrop
import elieoko.mobile.luka.presentation.components.PageBackdropTone
import elieoko.mobile.luka.presentation.components.rememberCvPicker
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import luka.shared.generated.resources.Res
import luka.shared.generated.resources.onboarding_kinshasa_1
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val profile = state.profile
    val pickCv = rememberCvPicker(viewModel::saveCv)
    val plan = LukaPlans.byId(profile?.planId ?: LukaPlans.STARTER)
    val paid = plan.id != LukaPlans.STARTER

    PageBackdrop(Res.drawable.onboarding_kinshasa_1, tone = PageBackdropTone.Cinematic) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = LukaBottomNavHeight + 88.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(LukaRed, Color(0xFF7A0C18)))),
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
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            PlanBadge(plan.name, paid = paid)
                            if (profile?.isCertified == true) {
                                PlanBadge("Certifié", paid = false, certified = true)
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            profile?.displayName.orEmpty().ifBlank { "Ton profil" },
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                        )
                        Text(
                            listOfNotNull(
                                profile?.profession?.title,
                                profile?.cityName,
                            ).distinct().joinToString(" · ").ifBlank { "Complète ton profil" },
                            color = Color.White.copy(0.8f),
                        )
                        val phone = profile?.identifier?.value.orEmpty()
                        if (phone.isNotBlank()) {
                            Text(
                                phone,
                                color = Color.White.copy(0.7f),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
            item {
                Surface(shape = RoundedCornerShape(22.dp), color = Color.White) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Abonnement", color = LukaRed, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        Text(plan.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(
                            when (plan.id) {
                                LukaPlans.PROFESSIONAL -> "Recherche d’emploi, profil proposé aux entreprises, actif 3 mois."
                                LukaPlans.STUDENT -> "Orientation, tendances, universités et revues scientifiques."
                                else -> "Ouvre le menu pour choisir Étudiant (3 $) ou Professionnel (5 $)."
                            },
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
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
                Surface(shape = RoundedCornerShape(22.dp), color = Color.White) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Identité", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        OutlinedTextField(state.displayName, viewModel::onName, label = { Text("Nom") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp))
                        OutlinedTextField(state.email, viewModel::onEmail, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp))
                        OutlinedTextField(state.bio, viewModel::onBio, label = { Text("Bio") }, modifier = Modifier.fillMaxWidth().height(120.dp), shape = RoundedCornerShape(16.dp))
                        Text("${CongoCatalog.rdc.flag}  ${CongoCatalog.rdc.name}", style = MaterialTheme.typography.titleSmall)
                        Text("Pays verrouillé — Luka est centré sur la RDC", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                        Text("Ville", style = MaterialTheme.typography.titleSmall)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            state.cities.forEach { city ->
                                FilterPill(city.name, state.cityName.equals(city.name, true)) { viewModel.onCity(city.name) }
                            }
                        }
                        LukaPrimaryButton(
                            if (state.saving) "Enregistrement…" else "Enregistrer",
                            viewModel::saveProfile,
                            enabled = !state.saving,
                        )
                        if (state.message != null) {
                            Text(state.message.orEmpty(), color = LukaRed)
                        }
                    }
                }
            }
            item {
                TextButton(onClick = viewModel::logout, modifier = Modifier.fillMaxWidth()) {
                    Text("Se déconnecter", color = Color.White.copy(0.75f))
                }
            }
        }
    }
}

@Composable
private fun PlanBadge(label: String, paid: Boolean, certified: Boolean = false) {
    val bg = when {
        paid -> LukaRed
        certified -> Color.White.copy(alpha = 0.92f)
        else -> Color.White.copy(alpha = 0.92f)
    }
    val fg = when {
        paid -> Color.White
        certified -> Color(0xFF1B5E20)
        else -> LukaRed
    }
    Surface(color = bg, shape = RoundedCornerShape(99.dp)) {
        Text(
            label,
            color = fg,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
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
