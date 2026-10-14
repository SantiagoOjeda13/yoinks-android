package com.yoinks.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val YoinksDarkColorScheme = darkColorScheme(
    primary = BrandIndigo,
    secondary = BrandEmerald,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun YoinksTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // For Yoinks we enforce sleek premium dark theme
    MaterialTheme(
        colorScheme = YoinksDarkColorScheme,
        content = content
    )
}
