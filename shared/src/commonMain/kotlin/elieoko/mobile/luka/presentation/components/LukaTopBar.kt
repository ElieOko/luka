package elieoko.mobile.luka.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.LukaRedDeep

private val TopChrome = Brush.horizontalGradient(listOf(LukaRed, LukaRedDeep))
private val Glass = Color.White.copy(alpha = 0.22f)

@Composable
fun LukaTopBar(
    onMenu: () -> Unit,
    onNotifications: () -> Unit,
    notificationCount: Int = 0,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxWidth()
            .background(TopChrome)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        GlassCircleButton(
            onClick = onMenu,
            modifier = Modifier.align(Alignment.CenterStart),
            contentDescription = "Menu",
        ) {
            Icon(Icons.Rounded.Menu, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
        }
        BrandPill(Modifier.align(Alignment.Center))
        Box(Modifier.align(Alignment.CenterEnd)) {
            GlassCircleButton(
                onClick = onNotifications,
                contentDescription = "Notifications",
            ) {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp),
                )
            }
            if (notificationCount > 0) {
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 2.dp, end = 2.dp)
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        notificationCount.coerceAtMost(9).toString(),
                        color = LukaRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
fun LukaTopBarPreview() {
    LukaTopBar(onMenu = {}, onNotifications = {}, notificationCount = 1)
}

@Composable
private fun BrandPill(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = Color.White.copy(alpha = 0.96f),
        shadowElevation = 2.dp,
    ) {
        Box(Modifier.padding(horizontal = 18.dp, vertical = 8.dp)) {
            LukaLogo(Modifier.height(22.dp))
        }
    }
}

@Composable
private fun GlassCircleButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Glass)
            .semantics {
                this.contentDescription = contentDescription
                role = Role.Button
            }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
