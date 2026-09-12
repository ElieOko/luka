package elieoko.mobile.luka.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.LukaWine
import luka.shared.generated.resources.Res
import luka.shared.generated.resources.logo_luka
import luka.shared.generated.resources.logo_luka_light
import org.jetbrains.compose.resources.painterResource

@Composable
fun LukaLogo(modifier: Modifier = Modifier, light: Boolean = false) {
    Image(
        painter = painterResource(if (light) Res.drawable.logo_luka_light else Res.drawable.logo_luka),
        contentDescription = "Luka",
        modifier = modifier.height(32.dp),
        contentScale = ContentScale.FillHeight,
    )
}

@Composable
fun LukaOfferIcon(modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.logo_luka),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().padding(7.dp),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
fun LukaPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = LukaRed, contentColor = Color.White),
        contentPadding = PaddingValues(horizontal = 20.dp),
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun LukaProgressBar(progress: Float, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(99.dp))
            .background(LukaRed.copy(alpha = 0.22f)),
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .background(LukaRed),
        )
    }
}

@Composable
fun PulseDot(modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition()
    val scale by infinite.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Reverse),
    )
    Box(
        modifier
            .size(10.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(LukaRed),
    )
}

@Composable
fun RedHeroGradient(modifier: Modifier = Modifier) {
    Box(
        modifier.background(
            Brush.verticalGradient(
                listOf(Color.Transparent, LukaWine.copy(alpha = 0.55f), LukaWine.copy(alpha = 0.92f)),
            ),
        ),
    )
}
