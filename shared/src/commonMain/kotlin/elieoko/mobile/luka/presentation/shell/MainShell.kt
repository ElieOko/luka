package elieoko.mobile.luka.presentation.shell

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import elieoko.mobile.luka.presentation.components.ProUpgradeHost
import elieoko.mobile.luka.presentation.home.HomeScreen
import elieoko.mobile.luka.presentation.home.HomeViewModel
import elieoko.mobile.luka.presentation.home.OffersSeeAllScreen
import elieoko.mobile.luka.presentation.news.NewsScreen
import elieoko.mobile.luka.presentation.orientation.OrientationScreen
import elieoko.mobile.luka.presentation.profile.ProfileScreen
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.trends.TrendsScreen
import org.koin.compose.viewmodel.koinViewModel

enum class MainTab { Home, News, Trends, Orientation, Profile }

private val TikTokBar = Color(0xFF121212)
private val TikTokMuted = Color(0xFF8A8A8A)

@Composable
fun MainShell() {
    var tab by rememberSaveable { mutableStateOf(MainTab.Home) }
    var showAllOffers by rememberSaveable { mutableStateOf(false) }
    val homeVm: HomeViewModel = koinViewModel()

    Box(Modifier.fillMaxSize()) {
        if (showAllOffers) {
            OffersSeeAllScreen(onBack = { showAllOffers = false }, viewModel = homeVm)
        } else {
            AnimatedContent(tab, transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "tab") { current ->
                when (current) {
                    MainTab.Home -> HomeScreen(
                        onSeeAllOffers = { showAllOffers = true },
                        onOpenNews = { tab = MainTab.News },
                        onOpenTrends = { tab = MainTab.Trends },
                        onOpenOrientation = { tab = MainTab.Orientation },
                        viewModel = homeVm,
                    )
                    MainTab.News -> NewsScreen()
                    MainTab.Trends -> TrendsScreen()
                    MainTab.Orientation -> OrientationScreen()
                    MainTab.Profile -> ProfileScreen()
                }
            }
            TikTokBottomBar(
                tab = tab,
                onTab = { tab = it },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
        ProUpgradeHost(
            modifier = Modifier.align(Alignment.BottomEnd),
            bottomInset = if (showAllOffers) 24.dp else 64.dp,
        )
    }
}

@Composable
private fun TikTokBottomBar(
    tab: MainTab,
    onTab: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxWidth()
            .background(TikTokBar)
            .navigationBarsPadding()
            .height(56.dp),
    ) {
        Row(
            Modifier.fillMaxSize().padding(bottom = 4.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            TikTokItem(
                selected = tab == MainTab.Home,
                label = "Accueil",
                outlined = Icons.Outlined.Home,
                filled = Icons.Rounded.Home,
                onClick = { onTab(MainTab.Home) },
            )
            TikTokItem(
                selected = tab == MainTab.News,
                label = "News",
                outlined = Icons.AutoMirrored.Outlined.MenuBook,
                filled = Icons.AutoMirrored.Rounded.MenuBook,
                onClick = { onTab(MainTab.News) },
            )
            Spacer(Modifier.weight(1f))
            TikTokItem(
                selected = tab == MainTab.Orientation,
                label = "Orientation",
                outlined = Icons.Outlined.School,
                filled = Icons.Rounded.School,
                onClick = { onTab(MainTab.Orientation) },
            )
            TikTokItem(
                selected = tab == MainTab.Profile,
                label = "Profil",
                outlined = Icons.Outlined.Person,
                filled = Icons.Rounded.Person,
                onClick = { onTab(MainTab.Profile) },
            )
        }
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-6).dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { onTab(MainTab.Trends) },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                Modifier
                    .size(44.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(if (tab == MainTab.Trends) Color.White else LukaRed),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    if (tab == MainTab.Trends) Icons.Rounded.Whatshot else Icons.Outlined.Whatshot,
                    contentDescription = "Tendances",
                    tint = if (tab == MainTab.Trends) LukaRed else Color.White,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
    }
}

@Composable
private fun RowScope.TikTokItem(
    selected: Boolean,
    label: String,
    outlined: ImageVector,
    filled: ImageVector,
    onClick: () -> Unit,
) {
    Column(
        Modifier
            .weight(1f)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .padding(bottom = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        Icon(
            if (selected) filled else outlined,
            contentDescription = label,
            tint = if (selected) Color.White else TikTokMuted,
            modifier = Modifier.size(22.dp),
        )
        Text(
            label,
            color = if (selected) Color.White else TikTokMuted,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
        )
    }
}
