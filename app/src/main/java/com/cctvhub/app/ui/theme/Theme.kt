package com.cctvhub.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = AccentCyan,
    secondary = AccentViolet,
    tertiary = AccentPink,
    background = BgDeep,
    surface = BgSurface,
    surfaceVariant = BgCard,
    onPrimary = BgDeep,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = StatusOffline
)

@Composable
fun CCTVHubTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}
