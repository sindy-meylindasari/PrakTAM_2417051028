package com.example.praktam_2417051028.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFD4A017),
    secondary = Color(0xFFF2C94C),
    background = Color(0xFFFFF8E1),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.Black
)

@Composable
fun PrakTAM_2417051028Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography(),
        content = content
    )
}