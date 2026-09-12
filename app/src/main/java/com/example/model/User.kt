package com.example.model

enum class GlassTheme(val displayName: String, val description: String) {
    LIQUID_PORTAL("Liquid Glass Portal", "Electric cyan & violet glow on deep navy"),
    GRADIENT_MESH("Liquid Gradient Mesh", "Atmospheric blue & magenta mesh lighting"),
    FLOATING_SPARKLES("Floating Sparkles", "Teal & amber ethereal glow"),
    CRYSTAL_FROST("Crystal Frost Glass", "Ice cyan frost & cold deep blue highlights"),
    FLUID_CRYSTAL("Fluid Crystal Glass", "Sapphire & emerald prismatic radiance"),
    NEON_FLUID("Neon Fluid Glass", "High-contrast electric neon cyan & violet"),
    MIDNIGHT_OBSIDIAN("Midnight Obsidian Glass", "Deep onyx velvet with purple accents"),
    OLED_STEALTH("OLED Stealth Glass", "Pure black void with precision cyan borders")
}

data class UserProfile(
    val username: String = "Guest Player",
    val avatarInitials: String = "GU",
    val classLevel: String = "10",
    val xp: Int = 0,
    val streak: Int = 1,
    val lastActiveDate: String = "",
    val totalQuizzes: Int = 0,
    val totalQuestionsAnswered: Int = 0,
    val totalCorrect: Int = 0,
    val theme: GlassTheme = GlassTheme.LIQUID_PORTAL
) {
    val level: Int
        get() = (xp / 250) + 1

    val currentLevelXp: Int
        get() = xp % 250

    val accuracyPercent: Int
        get() = if (totalQuestionsAnswered > 0) (totalCorrect * 100) / totalQuestionsAnswered else 0
}

data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean,
    val requirementText: String
)
