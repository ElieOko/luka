package elieoko.mobile.luka.presentation.shell

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import elieoko.mobile.luka.presentation.home.HomeScreen
import elieoko.mobile.luka.presentation.profile.ProfileScreen
import elieoko.mobile.luka.presentation.theme.LukaRed

enum class MainTab { Home, Profile }

@Composable
fun MainShell() {
    var tab by rememberSaveable { mutableStateOf(MainTab.Home) }
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = tab == MainTab.Home,
                    onClick = { tab = MainTab.Home },
                    icon = { Icon(Icons.Rounded.Home, contentDescription = "Accueil") },
                    label = { Text("Accueil") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = LukaRed, selectedTextColor = LukaRed),
                )
                NavigationBarItem(
                    selected = tab == MainTab.Profile,
                    onClick = { tab = MainTab.Profile },
                    icon = { Icon(Icons.Rounded.Person, contentDescription = "Profil") },
                    label = { Text("Profil") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = LukaRed, selectedTextColor = LukaRed),
                )
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            AnimatedContent(tab, transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "tab") { current ->
                when (current) {
                    MainTab.Home -> HomeScreen()
                    MainTab.Profile -> ProfileScreen()
                }
            }
        }
    }
}
