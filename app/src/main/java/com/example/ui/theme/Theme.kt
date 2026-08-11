package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AmexGold,
    onPrimary = Color.Black,
    primaryContainer = AmexNavy,
    onPrimaryContainer = Color.White,
    secondary = AmexCardBlue,
    onSecondary = Color.White,
    tertiary = AmexPlatinum,
    background = AmexDarkNavy,
    onBackground = Color.White,
    surface = AmexSlateDark,
    onSurface = Color.White,
    surfaceVariant = AmexSurfaceDark,
    onSurfaceVariant = AmexTextMuted
)

private val LightColorScheme = lightColorScheme(
    primary = AmexNavy,
    onPrimary = Color.White,
    primaryContainer = AmexPlatinum,
    onPrimaryContainer = AmexNavy,
    secondary = AmexGold,
    onSecondary = Color.Black,
    tertiary = AmexCardBlue,
    background = AmexSurfaceLight,
    onBackground = AmexSlateDark,
    surface = Color.White,
    onSurface = AmexSlateDark,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF64748B)
)

@Composable
fun AmexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AmexTheme(darkTheme = darkTheme, content = content)
}

