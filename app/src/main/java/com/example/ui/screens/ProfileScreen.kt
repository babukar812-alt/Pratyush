package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GlassTheme
import com.example.model.UserProfile
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onThemeSelected: (GlassTheme) -> Unit,
    onLogout: () -> Unit
) {
    val palette = LocalThemePalette.current

    val accuracy = if (userProfile.totalQuestionsAnswered > 0) {
        (userProfile.totalCorrect * 100) / userProfile.totalQuestionsAnswered
    } else {
        0
    }

    LiquidBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card Header
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderGlow = true,
                    cornerRadius = 24.dp,
                    contentPadding = PaddingValues(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GlassAvatar(initials = userProfile.avatarInitials, size = 64.dp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userProfile.username,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "NCERT Class ${userProfile.classLevel} Scholar",
                                fontSize = 13.sp,
                                color = palette.primaryAccent
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                GlassBadge(text = "LEVEL ${(userProfile.xp / 250) + 1}", color = palette.secondaryAccent)
                                Spacer(modifier = Modifier.width(6.dp))
                                GlassBadge(text = "${userProfile.streak}D STREAK", color = AmberGlow)
                            }
                        }
                    }
                }
            }

            // Stats Grid
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 20.dp,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(
                        text = "CAREER PERFORMANCE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryAccent,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem(label = "QUIZZES", value = "${userProfile.totalQuizzes}")
                        StatItem(label = "QUESTIONS", value = "${userProfile.totalQuestionsAnswered}")
                        StatItem(label = "ACCURACY", value = "$accuracy%")
                        StatItem(label = "TOTAL XP", value = "${userProfile.xp}")
                    }
                }
            }

            // Liquid Glass Themes Section
            item {
                Column {
                    Text(
                        text = "LIQUID GLASS THEMES",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryAccent,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Choose your atmospheric aesthetic & nebula lighting.",
                        fontSize = 12.sp,
                        color = TextGrayBlue
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        GlassTheme.entries.forEach { theme ->
                            val isSelected = userProfile.theme == theme
                            val themePalette = getThemePalette(theme)

                            GlassCard(
                                modifier = Modifier.fillMaxWidth(),
                                cornerRadius = 14.dp,
                                borderGlow = isSelected,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                                onClick = { onThemeSelected(theme) },
                                testTag = "theme_option_${theme.name.lowercase()}"
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(themePalette.primaryAccent)
                                                .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = theme.displayName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) palette.primaryAccent else TextWhite
                                            )
                                        }
                                    }

                                    if (isSelected) {
                                        GlassBadge(text = "ACTIVE", color = palette.primaryAccent)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Account Actions
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    GlassButton(
                        text = "SIGN OUT / CHANGE PROFILE",
                        onClick = onLogout,
                        isSecondary = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Logout,
                                contentDescription = null,
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        testTag = "profile_logout_button"
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 0.5.sp
        )
    }
}
