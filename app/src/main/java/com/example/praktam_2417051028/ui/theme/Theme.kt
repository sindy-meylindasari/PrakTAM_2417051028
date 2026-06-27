package com.example.praktam_2417051028.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = DustyRose,
    secondary = SoftPink,
    background = SoftCream,
    surface = CardWhite,
    onPrimary = Color.White,
    onSecondary = DarkBrown,
    onBackground = DarkBrown,
    onSurface = DarkBrown
)

@Composable
fun PrakTAM_2417051028Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}