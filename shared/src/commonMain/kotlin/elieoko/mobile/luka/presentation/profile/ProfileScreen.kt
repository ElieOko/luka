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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val profile = state.profile
    val pickCv = rememberCvPicker(viewModel::saveCv)
    val planId = profile?.planId
    val isFree = planId.isNullOrBlank() || planId == "starter"

    PageBackdrop(Res.drawable.onboarding_kinshasa_1, tone = PageBackdropTone.Soft) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = LukaBottomNavHeight + 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(72.dp).clip(CircleShape).background(LukaRed),
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
                    if (isFree) {
                        Surface(color = LukaMist, shape = RoundedCornerShape(99.dp)) {
                            Text(
                                "Gratuit",
                                color = LukaRed,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                    }
                    Text(profile?.displayName.orEmpty().ifBlank { "Ton profil" }, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    Text(
                        listOfNotNull(profile?.profession?.title, profile?.regionId?.replaceFirstChar { it.uppercase() }).joinToString(" · "),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
            OutlinedTextField(state.email, viewModel::onEmail, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp))
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
            TextButton(onClick = viewModel::resetDemo, modifier = Modifier.fillMaxWidth()) {
                Text("Réinitialiser la démo", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
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
