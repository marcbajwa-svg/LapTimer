package com.marcbajwa.laptimernative.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1E2D38),
    secondary = Color(0xFFC65D3B),
    tertiary = Color(0xFF345F49),
    background = Color(0xFFF4EFE4),
    surface = Color(0xFFFFF9EF),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFEAD8B7),
    secondary = Color(0xFFC65D3B),
    tertiary = Color(0xFFB9D6BD),
)

@Composable
fun LapTimerTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content,
    )
}
