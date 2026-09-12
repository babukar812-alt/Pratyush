package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.model.GlassTheme

val LocalThemePalette = staticCompositionLocalOf {
    getThemePalette(GlassTheme.LIQUID_PORTAL)
}

@Composable
fun ThinkXTheme(
    selectedTheme: GlassTheme = GlassTheme.LIQUID_PORTAL,
    content: @Composable () -> Unit
) {
    val palette = getThemePalette(selectedTheme)

    val colorScheme = darkColorScheme(
        primary = palette.primaryAccent,
        secondary = palette.secondaryAccent,
        tertiary = palette.glowColor1,
        background = palette.backgroundBase,
        surface = palette.surfaceGlass,
        onPrimary = VoidBlack,
        onSecondary = VoidBlack,
        onBackground = TextWhite,
        onSurface = TextWhite
    )

    CompositionLocalProvider(LocalThemePalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

// Backward-compatibility wrapper
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    ThinkXTheme(selectedTheme = GlassTheme.LIQUID_PORTAL, content = content)
}
