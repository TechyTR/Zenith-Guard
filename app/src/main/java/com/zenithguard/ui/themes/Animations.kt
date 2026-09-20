package com.zenithguard.ui.theme

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * OriginOS / HarmonyOS / OneUI style ultra-smooth animated gradient brush.
 */
@Composable
fun rememberAnimatedOSGradient(): Brush {
    val infiniteTransition = rememberInfiniteTransition(label = "os_fluid_gradient")

    val color1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF0F172A),
        targetValue = Color(0xFF0284C7),
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Reverse),
        label = "color1"
    )
    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF311B92),
        targetValue = Color(0xFF0D9488),
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Reverse),
        label = "color2"
    )
    val color3 by infiniteTransition.animateColor(
        initialValue = Color(0xFF020617),
        targetValue = Color(0xFF1E1B4B),
        animationSpec = infiniteRepeatable(tween(7000, easing = LinearEasing), RepeatMode.Reverse),
        label = "color3"
    )

    return Brush.verticalGradient(colors = listOf(color1, color2, color3))
}
