package elieoko.mobile.luka.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import elieoko.mobile.luka.presentation.theme.LukaCream
import elieoko.mobile.luka.presentation.theme.LukaWine
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class PageBackdropTone { Cinematic, Soft }

@Composable
fun PageBackdrop(
    image: DrawableResource,
    modifier: Modifier = Modifier,
    tone: PageBackdropTone = PageBackdropTone.Soft,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.fillMaxSize()) {
        Image(
            painterResource(image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Box(
            Modifier.fillMaxSize().background(
                when (tone) {
                    PageBackdropTone.Cinematic -> Brush.verticalGradient(
                        listOf(
                            Color(0x66000000),
                            LukaWine.copy(alpha = 0.55f),
                            Color(0xF214080A),
                        ),
                    )
                    PageBackdropTone.Soft -> Brush.verticalGradient(
                        listOf(
                            Color(0x330A0406),
                            LukaCream.copy(alpha = 0.82f),
                            LukaCream.copy(alpha = 0.96f),
                        ),
                    )
                },
            ),
        )
        content()
    }
}
