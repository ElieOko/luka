package elieoko.mobile.luka.presentation.welcome

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.presentation.components.LukaLogo
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.components.RedHeroGradient
import elieoko.mobile.luka.presentation.theme.LukaRed
import luka.shared.generated.resources.Res
import luka.shared.generated.resources.onboarding_city
import luka.shared.generated.resources.onboarding_learn
import luka.shared.generated.resources.onboarding_team
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private data class WelcomePage(
    val image: DrawableResource,
    val title: String,
    val body: String,
)

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    val pages = listOf(
        WelcomePage(Res.drawable.onboarding_team, "L’emploi vient à toi.", "Luka trouve les opportunités pour des millions de jeunes qui ne savent pas où chercher."),
        WelcomePage(Res.drawable.onboarding_learn, "Un métier. Une direction.", "Tu choisis une voie — ingénierie, sécurité, finance — Luka analyse le Congo pour toi."),
        WelcomePage(Res.drawable.onboarding_city, "La RDC d’abord.", "Kinshasa, Lubumbashi, Goma… les offres arrivent, avec le lien pour postuler."),
    )
    var index by remember { mutableIntStateOf(0) }
    val page = pages[index]

    Box(Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = page,
            transitionSpec = { fadeIn() + slideInHorizontally { it / 3 } togetherWith fadeOut() + slideOutHorizontally { -it / 3 } },
            label = "welcome",
        ) { current ->
            Image(
                painterResource(current.image),
                contentDescription = current.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
        RedHeroGradient(Modifier.fillMaxSize())
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            LukaLogo(light = true)
            Column {
                Text(page.title, color = Color.White, style = androidx.compose.material3.MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.height(12.dp))
                Text(page.body, color = Color.White.copy(alpha = 0.88f), style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    pages.indices.forEach { i ->
                        Box(
                            Modifier
                                .size(if (i == index) 22.dp else 8.dp, 8.dp)
                                .clip(CircleShape)
                                .background(if (i == index) LukaRed else Color.White.copy(alpha = 0.45f)),
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                LukaPrimaryButton(
                    text = if (index == pages.lastIndex) "Créer mon compte" else "Continuer",
                    onClick = {
                        if (index == pages.lastIndex) onStart() else index++
                    },
                )
            }
        }
    }
}
