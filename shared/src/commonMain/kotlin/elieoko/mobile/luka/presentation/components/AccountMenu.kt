package elieoko.mobile.luka.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.domain.repository.SessionRepository
import elieoko.mobile.luka.presentation.subscription.SubscriptionScreen
import elieoko.mobile.luka.presentation.theme.LukaInk
import elieoko.mobile.luka.presentation.theme.LukaMuted
import elieoko.mobile.luka.presentation.theme.LukaRed
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

enum class AccountPage { Subscription, Notifications, Settings }

@Composable
fun AccountMenuHost(
    drawerOpen: Boolean,
    page: AccountPage?,
    onDrawerChange: (Boolean) -> Unit,
    onPageChange: (AccountPage?) -> Unit,
    showOpportunityFab: Boolean,
    bottomInset: Dp = LukaBottomNavHeight,
) {
    val sessions: SessionRepository = koinInject()
    val scope = rememberCoroutineScope()

    Box(Modifier.fillMaxSize()) {
        if (showOpportunityFab) {
            ExtendedFloatingActionButton(
                onClick = { onPageChange(AccountPage.Subscription) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(end = 16.dp, bottom = bottomInset + 20.dp)
                    .shadow(10.dp, RoundedCornerShape(28.dp)),
                containerColor = LukaRed,
                contentColor = Color.White,
                icon = { Icon(Icons.Rounded.Star, contentDescription = null) },
                text = { Text("Plus d’opportunité", fontWeight = FontWeight.SemiBold) },
            )
        }

        AnimatedVisibility(
            visible = drawerOpen,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) { onDrawerChange(false) },
            )
        }

        AnimatedVisibility(
            visible = drawerOpen,
            enter = slideInHorizontally { -it },
            exit = slideOutHorizontally { -it },
        ) {
            AccountDrawer(
                onClose = { onDrawerChange(false) },
                onOpen = { destination ->
                    onDrawerChange(false)
                    onPageChange(destination)
                },
            )
        }

        if (page != null) {
            Surface(Modifier.fillMaxSize(), color = Color.White) {
                when (page) {
                    AccountPage.Subscription -> SubscriptionPage(onBack = { onPageChange(null) })
                    AccountPage.Notifications -> NotificationsPage(onBack = { onPageChange(null) })
                    AccountPage.Settings -> SettingsPage(
                        onBack = { onPageChange(null) },
                        onLogout = {
                            scope.launch { sessions.resetDemo() }
                            onPageChange(null)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun AccountDrawerPreview() {
    Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.35f))) {
        AccountDrawer(onClose = {}, onOpen = {})
    }
}

@Composable
private fun AccountDrawer(
    onClose: () -> Unit,
    onOpen: (AccountPage) -> Unit,
) {
    Column(
        Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(Color.White)
            .statusBarsPadding()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {},
            ),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LukaLogo(Modifier.height(28.dp))
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onClose) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Fermer")
            }
        }
        HorizontalDivider(color = Color(0xFFF0E4E5))
        Spacer(Modifier.height(8.dp))
        DrawerItem(
            icon = Icons.Outlined.CreditCard,
            label = "Abonnement",
            onClick = { onOpen(AccountPage.Subscription) },
        )
        DrawerItem(
            icon = Icons.Outlined.Notifications,
            label = "Notification",
            onClick = { onOpen(AccountPage.Notifications) },
        )
        DrawerItem(
            icon = Icons.Outlined.Settings,
            label = "Paramètres",
            onClick = { onOpen(AccountPage.Settings) },
        )
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = LukaInk, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(16.dp))
        Text(label, style = MaterialTheme.typography.titleMedium, color = LukaInk)
    }
}

@Composable
fun SubscriptionPage(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
        OverlayTopBar(title = "Abonnement", onBack = onBack)
        SubscriptionScreen()
    }
}

@Composable
private fun NotificationsPage(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
        OverlayTopBar(title = "Notification", onBack = onBack)
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                Icons.Rounded.NotificationsNone,
                contentDescription = null,
                tint = LukaMuted,
                modifier = Modifier.size(40.dp),
            )
            Spacer(Modifier.height(12.dp))
            Text("Aucune notification", fontWeight = FontWeight.SemiBold, color = LukaInk)
            Text("Les alertes Luka apparaîtront ici.", color = LukaMuted, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun SettingsPage(
    onBack: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
        OverlayTopBar(title = "Paramètres", onBack = onBack)
        Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            Text("Compte", style = MaterialTheme.typography.titleSmall, color = LukaMuted)
            Spacer(Modifier.height(8.dp))
            Text("Pays", fontWeight = FontWeight.SemiBold, color = LukaInk)
            Text("République démocratique du Congo", color = LukaMuted, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(24.dp))
            TextButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Text("Se déconnecter", color = LukaRed, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun OverlayTopBar(title: String, onBack: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Retour")
        }
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = LukaInk)
    }
}
