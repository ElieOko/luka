package elieoko.mobile.luka.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.model.LukaPlans
import elieoko.mobile.luka.domain.model.SubscriptionPlan
import elieoko.mobile.luka.domain.repository.SessionRepository
import elieoko.mobile.luka.domain.usecase.SelectPlanUseCase
import elieoko.mobile.luka.presentation.theme.LukaGold
import elieoko.mobile.luka.presentation.theme.LukaInk
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.LukaWine
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun ProUpgradeHost(
    modifier: Modifier = Modifier,
    bottomInset: Dp = LukaBottomNavHeight,
) {
    val sessions: SessionRepository = koinInject()
    val selectPlan: SelectPlanUseCase = koinInject()
    val session by sessions.session.collectAsState(initial = null)
    val isPro = session?.profile?.isPro == true
    var open by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (isPro) return

    ExtendedFloatingActionButton(
        onClick = { open = true },
        modifier = modifier
            .navigationBarsPadding()
            .padding(end = 16.dp, bottom = bottomInset - 8.dp)
            .shadow(12.dp, RoundedCornerShape(28.dp)),
        containerColor = LukaInk,
        contentColor = LukaGold,
        icon = { Icon(Icons.Rounded.WorkspacePremium, contentDescription = null) },
        text = { Text("Pro", fontWeight = FontWeight.Bold) },
    )

    if (open) {
        ProUpgradeSheet(
            currentPlanId = session?.profile?.planId ?: "starter",
            onDismiss = { open = false },
            onSelect = { plan ->
                scope.launch {
                    runCatching { selectPlan(plan.id, emptyList()) }
                    open = false
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProUpgradeSheet(
    currentPlanId: String,
    onDismiss: () -> Unit,
    onSelect: (SubscriptionPlan) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = LukaWine,
        contentColor = Color.White,
    ) {
        ProPlansContent(currentPlanId = currentPlanId, onSelect = onSelect)
    }
}

@Composable
fun ProPlansContent(
    currentPlanId: String,
    onSelect: (SubscriptionPlan) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(horizontal = 20.dp).padding(bottom = 28.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.WorkspacePremium, contentDescription = null, tint = LukaGold)
            Spacer(Modifier.width(10.dp))
            Text("Passe en Luka Pro", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(8.dp))
        Text(
            "Tous les métiers, profil visible par les recruteurs, stats de la demande en RDC.",
            color = Color.White.copy(0.82f),
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.height(18.dp))
        LukaPlans.all.forEach { plan ->
            val selected = plan.id == currentPlanId
            val highlighted = plan.id == "pro"
            Surface(
                onClick = { onSelect(plan) },
                shape = RoundedCornerShape(20.dp),
                color = when {
                    highlighted -> LukaRed
                    selected -> Color.White.copy(0.16f)
                    else -> Color.White.copy(0.08f)
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(plan.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.weight(1f))
                        Text(
                            "${plan.priceLabel}${if (plan.period != "toujours") " ${plan.period}" else ""}",
                            color = if (highlighted) LukaGold else Color.White.copy(0.9f),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    plan.perks.take(3).forEach { perk ->
                        Text("· $perk", color = Color.White.copy(0.82f), style = MaterialTheme.typography.bodyMedium)
                    }
                    if (selected) {
                        Text("Plan actuel", color = LukaGold, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}
