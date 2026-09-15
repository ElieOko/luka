package elieoko.mobile.luka.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import elieoko.mobile.luka.domain.model.AccountKind
import elieoko.mobile.luka.domain.model.CongoCatalog
import elieoko.mobile.luka.domain.model.LukaEntitlements
import elieoko.mobile.luka.domain.model.LukaPlans
import elieoko.mobile.luka.presentation.components.LukaBottomNavHeight
import elieoko.mobile.luka.presentation.components.PageBackdrop
import elieoko.mobile.luka.presentation.components.PageBackdropTone
import elieoko.mobile.luka.presentation.components.rememberCvPicker
import elieoko.mobile.luka.presentation.theme.LukaGold
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import luka.shared.generated.resources.Res
import luka.shared.generated.resources.onboarding_kinshasa_1
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    onEditProfile: () -> Unit,
    onPrivacy: () -> Unit = {},
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val profile = state.profile
    val pickCv = rememberCvPicker(viewModel::saveCv)
    val plan = LukaPlans.byId(profile?.planId ?: LukaPlans.STARTER)
    val premium = profile?.isPremium == true || profile?.isPro == true

    PageBackdrop(Res.drawable.onboarding_kinshasa_1, tone = PageBackdropTone.Cinematic) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = LukaBottomNavHeight + 96.dp),
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
                            PlanBadge(
                                if (profile?.isLearner == true) AccountKind.LEARNER.title else AccountKind.PROFESSIONAL.title,
                                paid = false,
                            )
                            if (premium) PlanBadge("Premium", paid = true)
                            else PlanBadge(plan.name, paid = plan.id != LukaPlans.STARTER)
                        }
                        Spacer(Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                profile?.displayName.orEmpty().ifBlank { "Ton profil" },
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                            )
                            if (profile?.isCertified == true) {
                                Spacer(Modifier.width(6.dp))
                                Icon(
                                    Icons.Rounded.Verified,
                                    contentDescription = "Profil certifié",
                                    tint = LukaGold,
                                    modifier = Modifier.size(22.dp),
                                )
                            }
                        }
                        Text(
                            listOfNotNull(
                                profile?.tradeTitle?.ifBlank { null } ?: profile?.profession?.title,
                                profile?.cityName,
                            ).distinct().joinToString(" · ").ifBlank { "Complète ton profil" },
                            color = Color.White.copy(0.8f),
                        )
                    }
                    IconButton(
                        onClick = onEditProfile,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.92f)),
                    ) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Modifier le profil", tint = LukaRed)
                    }
                }
            }
            item {
                Surface(shape = RoundedCornerShape(22.dp), color = Color.White) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Type de compte", color = LukaRed, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        Text(
                            "Tu peux passer d’apprenant à professionnel sans recréer le compte.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AccountKind.entries.forEach { kind ->
                                val selected = profile?.accountKind == kind
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (selected) LukaRed else LukaMist,
                                    modifier = Modifier.weight(1f).clickable { viewModel.switchAccountKind(kind) },
                                ) {
                                    Column(Modifier.padding(12.dp)) {
                                        Text(
                                            kind.title,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                                        )
                                        Text(
                                            if (kind == AccountKind.LEARNER) "Études, MIT, orientation" else "Offres, CV, marché",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (selected) Color.White.copy(0.9f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                Surface(shape = RoundedCornerShape(22.dp), color = Color.White) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Tes informations", color = LukaRed, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        ProfileField("Nom", profile?.displayName.orEmpty().ifBlank { "—" })
                        ProfileField("Téléphone", profile?.identifier?.value.orEmpty().ifBlank { "—" })
                        ProfileField("E-mail", profile?.email.orEmpty().ifBlank { "—" })
                        ProfileField("Métier", profile?.tradeTitle?.ifBlank { null } ?: profile?.profession?.title.orEmpty().ifBlank { "—" })
                        ProfileField("Ville", profile?.cityName.orEmpty().ifBlank { "—" })
                        ProfileField("Pays", "${CongoCatalog.rdc.flag}  ${CongoCatalog.rdc.name}")
                        ProfileField(
                            "Compte",
                            if (profile?.isLearner == true) "Apprenant" else "Professionnel",
                        )
                        if (profile?.bio?.isNotBlank() == true) {
                            ProfileField("Bio", profile.bio)
                        }
                    }
                }
            }
            item {
                Surface(shape = RoundedCornerShape(22.dp), color = Color.White) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Abonnement", color = LukaRed, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        Text(
                            if (premium) "Premium" else plan.name,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            when {
                                profile?.isLearner == true && LukaEntitlements.learnerUnlocked(profile) ->
                                    "Tendances, MIT, orientation et conseils réguliers débloqués."
                                profile?.isLearner == true ->
                                    "Abonnement Étudiant (3 $) : tendances, news MIT, orientation, conseils."
                                premium || plan.id == LukaPlans.PROFESSIONAL ->
                                    "Offres, analyse CV, analyse des offres, marché temps réel."
                                else -> "Abonnement Professionnel (5 $) : offres illimitées et analyses."
                            },
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
            item {
                val learner = profile?.isLearner == true
                val checks = buildList {
                    add(!profile?.displayName.isNullOrBlank())
                    add(!profile?.cityName.isNullOrBlank())
                    add(profile?.profession != null || profile?.tradeTitle?.isNotBlank() == true)
                    add(profile?.email?.contains("@") == true)
                    if (!learner) add(profile?.cvFileName?.isNotBlank() == true)
                }
                val completeness = checks.count { it }
                val total = checks.size
                Surface(shape = RoundedCornerShape(22.dp), color = Color.White) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Profil", color = LukaRed, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        Text("$completeness / $total éléments remplis", fontWeight = FontWeight.Bold)
                        LinearProgressIndicator(
                            progress = { if (total == 0) 0f else completeness / total.toFloat() },
                            modifier = Modifier.fillMaxWidth(),
                            color = LukaRed,
                        )
                        Text(
                            if (learner) "Nom, ville, métier, e-mail. L’orientation sera plus juste."
                            else "Nom, ville, métier, e-mail, CV. Plus c’est complet, plus Luka est précis.",
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
                TextButton(onClick = onPrivacy, modifier = Modifier.fillMaxWidth()) {
                    Text("Politique de confidentialité", color = Color.White.copy(0.85f), fontWeight = FontWeight.Bold)
                }
                TextButton(onClick = viewModel::logout, modifier = Modifier.fillMaxWidth()) {
                    Text("Se déconnecter", color = Color.White.copy(0.75f))
                }
            }
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelLarge)
        Text(value, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun PlanBadge(label: String, paid: Boolean) {
    val bg = if (paid) LukaGold else Color.White.copy(alpha = 0.92f)
    val fg = if (paid) Color(0xFF3A2208) else LukaRed
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
