package com.aidating.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Typeface

private val LightColors = lightColorScheme(
    primary = Color(0xFF007AFF),
    onPrimary = Color.White,
    secondary = Color(0xFF62A3F3),
    surface = Color.White,
    background = Color.White,
    onBackground = Color(0xFF0A0A0A)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF62A3F3),
    onPrimary = Color.Black,
    surface = Color(0xFF101114),
    background = Color(0xFF101114),
    onBackground = Color(0xFFF5F5F5)
)

private val AppTypography = Typography(
    /* Let Material defaults stand; Figma mentions SF Pro, but we'll rely on platform fonts */
    bodyLarge = Typography().bodyLarge.copy(fontWeight = FontWeight.Normal),
    headlineSmall = Typography().headlineSmall.copy(fontWeight = FontWeight.Bold),
    headlineMedium = Typography().headlineMedium.copy(fontWeight = FontWeight.Bold)
)

@Composable
fun AiDatingTheme(useDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (useDarkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, typography = AppTypography, content = content)
}
