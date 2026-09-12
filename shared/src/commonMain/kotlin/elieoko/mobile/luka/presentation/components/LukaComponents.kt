package elieoko.mobile.luka.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.LukaWine

private const val LukaMarkWidth = 143f
private const val LukaMarkHeight = 46f
private val LukaMarkInk = Color(0xFF141414)
private val LukaMarkAccent = Color(0xFFE0301E)

@Composable
fun LukaLogo(modifier: Modifier = Modifier, light: Boolean = false) {
    LukaWordmark(
        modifier = modifier.height(40.dp),
        ink = if (light) Color.White else LukaMarkInk,
    )
}

@Composable
fun LukaOfferIcon(modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        LukaWordmark(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 7.dp),
            ink = LukaMarkInk,
        )
    }
}

@Composable
private fun LukaWordmark(
    modifier: Modifier = Modifier,
    ink: Color,
    accent: Color = LukaMarkAccent,
) {
    Canvas(
        modifier
            .aspectRatio(LukaMarkWidth / LukaMarkHeight)
            .semantics {
                contentDescription = "Luka"
                role = Role.Image
            },
    ) {
        drawLukaWordmark(ink = ink, accent = accent)
    }
}

private fun DrawScope.drawLukaWordmark(ink: Color, accent: Color) {
    val s = size.width / LukaMarkWidth
    val stroke = Stroke(
        width = 3.15f * s,
        cap = StrokeCap.Butt,
        join = StrokeJoin.Miter,
        miter = 8f,
    )

    drawCircle(
        color = accent,
        radius = 13.35f * s,
        center = Offset(125.6f * s, 28.6f * s),
    )

    val letterL = Path().apply {
        moveTo(2f * s, 1.6f * s)
        lineTo(2f * s, 43f * s)
        lineTo(25.2f * s, 43f * s)
    }
    drawPath(letterL, ink, style = stroke)

    val uLeft = 33.15f
    val uRight = 60.35f
    val uEquator = 29.1f
    val uRadius = (uRight - uLeft) / 2f
    val uCenterX = (uLeft + uRight) / 2f
    val letterU = Path().apply {
        moveTo(uLeft * s, 1.6f * s)
        lineTo(uLeft * s, uEquator * s)
        arcTo(
            Rect(
                left = (uCenterX - uRadius) * s,
                top = (uEquator - uRadius) * s,
                right = (uCenterX + uRadius) * s,
                bottom = (uEquator + uRadius) * s,
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false,
        )
        lineTo(uRight * s, 1.6f * s)
    }
    drawPath(letterU, ink, style = stroke)

    drawLine(
        color = ink,
        start = Offset(75.6f * s, 1.6f * s),
        end = Offset(75.6f * s, 43f * s),
        strokeWidth = stroke.width,
        cap = StrokeCap.Butt,
    )
    val letterK = Path().apply {
        moveTo(102f * s, 3.2f * s)
        lineTo(78.2f * s, 21.6f * s)
        lineTo(104f * s, 43f * s)
    }
    drawPath(letterK, ink, style = stroke)

    val letterA = Path().apply {
        moveTo(123.5f * s, 2.4f * s)
        lineTo(107.2f * s, 43f * s)
        lineTo(140.8f * s, 43f * s)
        close()
    }
    drawPath(letterA, ink, style = stroke)
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
