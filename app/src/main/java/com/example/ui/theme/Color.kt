package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.model.GlassTheme

// Core ThinkX Liquid Glass Dark Palette
val VoidBlack = Color(0xFF030712)
val DeepNavy = Color(0xFF050914)
val DarkMeshNavy = Color(0xFF0B132B)
val MidnightNavy = Color(0xFF070D1E)

// Text
val TextWhite = Color(0xFFF8FAFC)
val TextGrayBlue = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

// Accents
val CyanNeon = Color(0xFF00E5FF)
val ElectricBlue = Color(0xFF2563EB)
val VioletPurple = Color(0xFF8B5CF6)
val DeepPurple = Color(0xFF6D28D9)
val SoftGreen = Color(0xFF10B981)
val SoftRed = Color(0xFFEF4444)
val CrimsonAlert = Color(0xFFEF4444)
val AmberGlow = Color(0xFFF59E0B)

// Glass Surfaces
val GlassSurfaceDark = Color(0x331E293B)
val GlassSurfaceLight = Color(0x1A38BDF8)
val GlassBorderCyan = Color(0x4D00E5FF)
val GlassBorderViolet = Color(0x4D8B5CF6)
val GlassBorderSubtle = Color(0x26FFFFFF)

data class ThemePalette(
    val backgroundBase: Color,
    val surfaceGlass: Color,
    val borderGlass: Color,
    val primaryAccent: Color,
    val secondaryAccent: Color,
    val glowColor1: Color,
    val glowColor2: Color
)

fun getThemePalette(theme: GlassTheme): ThemePalette {
    return when (theme) {
        GlassTheme.LIQUID_PORTAL -> ThemePalette(
            backgroundBase = Color(0xFF030712),
            surfaceGlass = Color(0x2B0B132B),
            borderGlass = Color(0x3800E5FF),
            primaryAccent = Color(0xFF00E5FF),
            secondaryAccent = Color(0xFF8B5CF6),
            glowColor1 = Color(0x4D00E5FF),
            glowColor2 = Color(0x4D8B5CF6)
        )
        GlassTheme.GRADIENT_MESH -> ThemePalette(
            backgroundBase = Color(0xFF050B1A),
            surfaceGlass = Color(0x28101C3D),
            borderGlass = Color(0x333B82F6),
            primaryAccent = Color(0xFF38BDF8),
            secondaryAccent = Color(0xFFEC4899),
            glowColor1 = Color(0x4D2563EB),
            glowColor2 = Color(0x4DEC4899)
        )
        GlassTheme.FLOATING_SPARKLES -> ThemePalette(
            backgroundBase = Color(0xFF031412),
            surfaceGlass = Color(0x2B0B2E2B),
            borderGlass = Color(0x3D14B8A6),
            primaryAccent = Color(0xFF2DD4BF),
            secondaryAccent = Color(0xFFFBBF24),
            glowColor1 = Color(0x4D14B8A6),
            glowColor2 = Color(0x4DF59E0B)
        )
        GlassTheme.CRYSTAL_FROST -> ThemePalette(
            backgroundBase = Color(0xFF060F1E),
            surfaceGlass = Color(0x2D162942),
            borderGlass = Color(0x4D7DD3FC),
            primaryAccent = Color(0xFF7DD3FC),
            secondaryAccent = Color(0xFF60A5FA),
            glowColor1 = Color(0x4038BDF8),
            glowColor2 = Color(0x401D4ED8)
        )
        GlassTheme.FLUID_CRYSTAL -> ThemePalette(
            backgroundBase = Color(0xFF021319),
            surfaceGlass = Color(0x2B0A2633),
            borderGlass = Color(0x4006B6D4),
            primaryAccent = Color(0xFF06B6D4),
            secondaryAccent = Color(0xFF10B981),
            glowColor1 = Color(0x4D06B6D4),
            glowColor2 = Color(0x4D10B981)
        )
        GlassTheme.NEON_FLUID -> ThemePalette(
            backgroundBase = Color(0xFF02040A),
            surfaceGlass = Color(0x3D0B1229),
            borderGlass = Color(0x6600E5FF),
            primaryAccent = Color(0xFF00F0FF),
            secondaryAccent = Color(0xFFA855F7),
            glowColor1 = Color(0x6600E5FF),
            glowColor2 = Color(0x66A855F7)
        )
        GlassTheme.MIDNIGHT_OBSIDIAN -> ThemePalette(
            backgroundBase = Color(0xFF06040C),
            surfaceGlass = Color(0x33120C24),
            borderGlass = Color(0x3B8B5CF6),
            primaryAccent = Color(0xFFA78BFA),
            secondaryAccent = Color(0xFFC084FC),
            glowColor1 = Color(0x4D6D28D9),
            glowColor2 = Color(0x4D4C1D95)
        )
        GlassTheme.OLED_STEALTH -> ThemePalette(
            backgroundBase = Color(0xFF000000),
            surfaceGlass = Color(0x26111827),
            borderGlass = Color(0x4022D3EE),
            primaryAccent = Color(0xFF22D3EE),
            secondaryAccent = Color(0xFF94A3B8),
            glowColor1 = Color(0x2622D3EE),
            glowColor2 = Color(0x1A38BDF8)
        )
    }
}
