package com.zenithguard.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Zenith Guard Premium Cyber Dark Palette
val DarkBackground = Color(0xFF0B0F17)
val CardSurface = Color(0xFF161B22)
val AccentCyan = Color(0xFF38BDF8)
val AccentPurple = Color(0xFFA855F7)
val StatusGreen = Color(0xFF10B981)
val StatusWarning = Color(0xFFF59E0B)

@Composable
fun ZenithGuardTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = DarkBackground,
            surface = CardSurface,
            primary = AccentCyan,
            secondary = AccentPurple
        ),
        content = content
    )
}
