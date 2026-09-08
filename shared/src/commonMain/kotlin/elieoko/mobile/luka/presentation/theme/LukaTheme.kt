package elieoko.mobile.luka.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import luka.shared.generated.resources.Res
import luka.shared.generated.resources.outfit_bold
import luka.shared.generated.resources.outfit_regular
import luka.shared.generated.resources.outfit_semibold
import org.jetbrains.compose.resources.Font

val LukaRed = Color(0xFFE31B23)
val LukaRedDeep = Color(0xFF9B1020)
val LukaWine = Color(0xFF4A0710)
val LukaInk = Color(0xFF1A0A0C)
val LukaCream = Color(0xFFFFF6F5)
val LukaMist = Color(0xFFF8E8E9)
val LukaMuted = Color(0xFF7A5C5F)
val LukaGold = Color(0xFFE8B86D)

private val LightColors = lightColorScheme(
    primary = LukaRed,
    onPrimary = Color.White,
    primaryContainer = LukaMist,
    onPrimaryContainer = LukaWine,
    secondary = LukaWine,
    onSecondary = Color.White,
    background = LukaCream,
    onBackground = LukaInk,
    surface = Color.White,
    onSurface = LukaInk,
    surfaceVariant = LukaMist,
    onSurfaceVariant = LukaMuted,
    outline = Color(0xFFE2C9CB),
    error = LukaRedDeep,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFF6B73),
    onPrimary = LukaWine,
    background = Color(0xFF14080A),
    onBackground = Color(0xFFFFF1F1),
    surface = Color(0xFF1E0F12),
    onSurface = Color(0xFFFFF1F1),
)

@Composable
fun LukaFontFamily() = FontFamily(
    Font(Res.font.outfit_regular, FontWeight.Normal),
    Font(Res.font.outfit_semibold, FontWeight.SemiBold),
    Font(Res.font.outfit_bold, FontWeight.Bold),
)

@Composable
fun LukaTheme(dark: Boolean = false, content: @Composable () -> Unit) {
    val fonts = LukaFontFamily()
    val typography = Typography(
        displayLarge = TextStyle(fontFamily = fonts, fontWeight = FontWeight.Bold, fontSize = 40.sp, lineHeight = 44.sp),
        headlineLarge = TextStyle(fontFamily = fonts, fontWeight = FontWeight.Bold, fontSize = 30.sp, lineHeight = 36.sp),
        headlineMedium = TextStyle(fontFamily = fonts, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 30.sp),
        titleLarge = TextStyle(fontFamily = fonts, fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
        titleMedium = TextStyle(fontFamily = fonts, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
        bodyLarge = TextStyle(fontFamily = fonts, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 22.sp),
        bodyMedium = TextStyle(fontFamily = fonts, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
        labelLarge = TextStyle(fontFamily = fonts, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    )
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = typography,
        content = content,
    )
}
