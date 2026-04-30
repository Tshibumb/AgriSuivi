package com.agrisuivi.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

// ── Palette Terre & Nature ────────────────────────────────────────────────────
val ForestGreen        = Color(0xFF2D6A4F)
val ForestGreenLight   = Color(0xFF52B788)
val ForestGreenDark    = Color(0xFF1B4332)
val ForestGreenContainer = Color(0xFFB7E4C7)
val OnForestGreenContainer = Color(0xFF0A2E1D)

val EarthBrown         = Color(0xFF795548)
val EarthBrownLight    = Color(0xFFA1887F)
val EarthBrownContainer = Color(0xFFD7CCC8)

val SunflowerYellow    = Color(0xFFF9A825)
val SunflowerContainer = Color(0xFFFFF9C4)

val ParchmentWhite     = Color(0xFFF8F5EF)
val SoilDark           = Color(0xFF1C1B1F)
val SurfaceVariantColor = Color(0xFFDDE5DB)

// ── Schemes ───────────────────────────────────────────────────────────────────
private val LightColors = lightColorScheme(
    primary              = ForestGreen,
    onPrimary            = Color.White,
    primaryContainer     = ForestGreenContainer,
    onPrimaryContainer   = OnForestGreenContainer,
    secondary            = EarthBrown,
    onSecondary          = Color.White,
    secondaryContainer   = EarthBrownContainer,
    onSecondaryContainer = Color(0xFF3E2723),
    tertiary             = SunflowerYellow,
    onTertiary           = SoilDark,
    tertiaryContainer    = SunflowerContainer,
    onTertiaryContainer  = Color(0xFF3E2000),
    background           = ParchmentWhite,
    onBackground         = SoilDark,
    surface              = ParchmentWhite,
    onSurface            = SoilDark,
    surfaceVariant       = SurfaceVariantColor,
    error                = Color(0xFFBA1A1A),
    onError              = Color.White,
)

private val DarkColors = darkColorScheme(
    primary              = ForestGreenLight,
    onPrimary            = ForestGreenDark,
    primaryContainer     = ForestGreenDark,
    onPrimaryContainer   = ForestGreenContainer,
    secondary            = EarthBrownLight,
    onSecondary          = Color(0xFF3E2723),
    tertiary             = SunflowerYellow,
    background           = Color(0xFF1A1C19),
    surface              = Color(0xFF1A1C19),
)

// ── Typography ────────────────────────────────────────────────────────────────
val AgriTypography = Typography(
    headlineLarge  = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold,    fontSize = 28.sp, lineHeight = 36.sp),
    headlineMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold,    fontSize = 22.sp, lineHeight = 30.sp),
    headlineSmall  = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 26.sp),
    titleLarge     = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold,    fontSize = 20.sp),
    titleMedium    = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
    bodyLarge      = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal,  fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium     = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal,  fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge     = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium,  fontSize = 14.sp),
)

// ── Shapes organiques arrondies ───────────────────────────────────────────────
val AgriShapes = Shapes(
    extraSmall  = RoundedCornerShape(8.dp),
    small       = RoundedCornerShape(12.dp),
    medium      = RoundedCornerShape(16.dp),
    large       = RoundedCornerShape(24.dp),
    extraLarge  = RoundedCornerShape(32.dp),
)

// ── Point d'entrée du thème ───────────────────────────────────────────────────
@Composable
fun AgriSuiviTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography  = AgriTypography,
        shapes      = AgriShapes,
        content     = content
    )
}
