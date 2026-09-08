package elieoko.mobile.luka.presentation.welcome

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.presentation.components.BottomCtaBar
import elieoko.mobile.luka.presentation.components.LukaLogo
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.components.RedHeroGradient
import elieoko.mobile.luka.presentation.theme.LukaRed
import kotlinx.coroutines.launch
import luka.shared.generated.resources.Res
import luka.shared.generated.resources.onboarding_kinshasa_1
import luka.shared.generated.resources.onboarding_kinshasa_2
import luka.shared.generated.resources.onboarding_kinshasa_3
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
        WelcomePage(Res.drawable.onboarding_kinshasa_1, "L’emploi vient à toi.", "Kinshasa, le boulevard, les taxis jaunes — Luka trouve les offres pour des millions de jeunes qui ne savent pas où chercher."),
        WelcomePage(Res.drawable.onboarding_kinshasa_2, "Les sièges recrutent.", "Banques, telcos, mines : glisse, choisis un métier, Luka analyse la RDC pour toi."),
        WelcomePage(Res.drawable.onboarding_kinshasa_3, "La RDC d’abord.", "Kinshasa, Lubumbashi, Goma… les liens d’offres arrivent. L’opportunité vient à toi."),
    )
    val pager = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Box(Modifier.fillMaxSize()) {
        HorizontalPager(state = pager, modifier = Modifier.fillMaxSize()) { page ->
            val offset = (pager.currentPage - page) + pager.currentPageOffsetFraction
            Image(
                painterResource(pages[page].image),
                contentDescription = pages[page].title,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationX = offset * 80f
                        scaleX = 1.08f
                        scaleY = 1.08f
                    },
                contentScale = ContentScale.Crop,
            )
        }
        RedHeroGradient(Modifier.fillMaxSize())
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            LukaLogo(Modifier.padding(24.dp), light = true)
            Spacer(Modifier.weight(1f))
            AnimatedContent(
                targetState = pager.currentPage,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "welcome-copy",
                modifier = Modifier.padding(horizontal = 24.dp),
            ) { pageIndex ->
                val page = pages[pageIndex]
                Column {
                    Text(page.title, color = Color.White, style = MaterialTheme.typography.headlineLarge)
                    Spacer(Modifier.height(12.dp))
                    Text(page.body, color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodyLarge)
                }
            }
            Column(Modifier.padding(horizontal = 24.dp)) {
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    pages.indices.forEach { i ->
                        val selected = pager.currentPage == i
                        val width by animateDpAsState(if (selected) 22.dp else 8.dp)
                        Box(
                            Modifier
                                .width(width)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(if (selected) LukaRed else Color.White.copy(alpha = 0.45f)),
                        )
                    }
                }
            }
            BottomCtaBar {
                LukaPrimaryButton(
                    text = if (pager.currentPage == pages.lastIndex) "Créer mon compte" else "Continuer",
                    onClick = {
                        if (pager.currentPage == pages.lastIndex) onStart()
                        else scope.launch { pager.animateScrollToPage(pager.currentPage + 1) }
                    },
                )
            }
        }
    }
}
